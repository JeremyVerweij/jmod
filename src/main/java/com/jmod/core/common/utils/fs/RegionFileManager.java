package com.jmod.core.common.utils.fs;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class RegionFileManager {
    private static final ThreadLocal<ByteBuffer> POINTER_BUFFER = ThreadLocal.withInitial(() ->ByteBuffer.allocate(4));
    private static final byte[] NULL = new byte[0];

    private static void writeChunkDataAtPosAndSetPointer(ByteBuffer buffer, FileChannel channel,
                                                         int offset, int fileSize, int writePos, @Nonnull ByteBuffer size) throws IOException {
        size.flip();
        ByteBuffer newPointer = ByteBuffer.allocate(4);
        newPointer.putInt(fileSize);
        newPointer.flip();
        FileUtils.writeSectionNio(channel, writePos, size);
        FileUtils.writeSectionNio(channel, writePos + 4, buffer);
        FileUtils.writeSectionNio(channel, offset, newPointer);
    }

    private static int getPointerInt(@Nonnull FileChannel channel, int offset) throws IOException {
        ByteBuffer pointer = POINTER_BUFFER.get();

        pointer.clear();
        channel.read(pointer, offset);
        pointer.flip();

        return pointer.getInt();
    }

    //4096 bytes of header
    public static @Nullable ByteBuffer readChunk(int chunkX, int chunkZ, int dimension){
        Path filePath = PathUtils.getRegionFile(chunkX, chunkZ, dimension);

        if (Files.exists(filePath)){
            byte chunkXO = (byte) (chunkX & 0b11111);
            byte chunkZO = (byte) (chunkZ & 0b11111);
            int offset = (chunkXO | (chunkZO << 5)) << 2;

            try(FileChannel channel = FileUtils.createChannelRead(filePath)) {
                int pointer = getPointerInt(channel, offset) << 12;
                if (pointer == 0){
                    return null;
                }

                int size = getPointerInt(channel, pointer);

                return FileUtils.readSectionNIO(channel, pointer + 4, size);
            } catch (IOException e) {
                return null;
            }
        }else return null;
    }

    public static boolean writeChunk(int chunkX, int chunkZ, int dimension, ByteBuffer buffer){
        Path filePath = PathUtils.getRegionFile(chunkX, chunkZ, dimension);

        boolean createHeader = false;

        if (!Files.exists(filePath)){
            try {
                Files.createDirectories(filePath.getParent());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try {
                Files.write(filePath, NULL);
            } catch (IOException e) {throw new RuntimeException(e);}

            createHeader = true;
        }

        try (FileChannel channel = FileUtils.createChannelWrite(filePath)) {
            if (createHeader){
                ByteBuffer header = ByteBuffer.allocate(4096);
                FileUtils.writeSectionNio(channel, 0, header);
            }

            byte chunkXO = (byte) (chunkX & 0b11111);
            byte chunkZO = (byte) (chunkZ & 0b11111);
            int offset = (chunkXO | (chunkZO << 5)) << 2;

            int pointer = getPointerInt(channel, offset) << 12;

            boolean createNew = false;
            boolean pointerMoved = false;

            if (pointer == 0){
                //allocate space and write new pointer
                createNew = true;
            }else{
                //check if sizes are compatible
                int sections = ((getPointerInt(channel, pointer) + 4) >> 12) + 1; //how many sections
                int sectionsRequired = ((buffer.capacity() + 4) >> 12) + 1; //add for bytes for the size int

                if (sectionsRequired > sections){
                    //allocate new space, move pointer
                    createNew = true;
                    pointerMoved = true;
                }else{
                    //write directly, there is space
                    ByteBuffer size = ByteBuffer.allocate(4);
                    size.putInt(buffer.capacity());
                    size.flip();
                    FileUtils.writeSectionNio(channel, pointer, size);
                    FileUtils.writeSectionNio(channel, pointer + 4, buffer);
                }
            }

            if (createNew){
                int fileSize = (int) ((channel.size() + 4095) >> 12);
                int writePos = fileSize << 12;
                ByteBuffer size = ByteBuffer.allocate(4);
                size.putInt(buffer.capacity());
                writeChunkDataAtPosAndSetPointer(buffer, channel, offset, fileSize, writePos, size);
            }

            return pointerMoved;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean removeChunk(int chunkX, int chunkZ, int dimension){
        Path filePath = PathUtils.getRegionFile(chunkX, chunkZ, dimension);

        if (Files.exists(filePath)) {
            try (FileChannel channel = FileUtils.createChannelWrite(filePath)) {
                byte chunkXO = (byte) (chunkX & 0b11111);
                byte chunkZO = (byte) (chunkZ & 0b11111);
                int offset = (chunkXO | (chunkZO << 5)) << 2;

                ByteBuffer newPointer = ByteBuffer.allocate(4);
                newPointer.putInt(0);
                newPointer.flip();

                int pointer = getPointerInt(channel, offset) << 12;

                FileUtils.writeSectionNio(channel, offset, newPointer);

                return pointer > 0;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return false;
    }

    public static void compactRegion(@Nonnull Path filePath) throws IOException {
        if (!Files.exists(filePath)) return;

        Path tempPath = filePath.resolveSibling(filePath.getFileName() + "_temp");

        try (FileChannel oldChan = FileChannel.open(filePath, StandardOpenOption.READ);
             FileChannel newChan = FileChannel.open(tempPath, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.READ)) {

            // 1. Initialize Temp Header
            ByteBuffer emptyHeader = ByteBuffer.allocate(4096);
            newChan.write(emptyHeader, 0);

            // 2. Loop through all 1024 possible chunks in the region
            for (int i = 0; i < 1024; i++) {
                int headerOffset = i * 4;
                int oldPointer = getPointerInt(oldChan, headerOffset) << 12;

                if (oldPointer > 0) {
                    int size = getPointerInt(oldChan, oldPointer);
                    ByteBuffer data = FileUtils.readSectionNIO(oldChan, oldPointer + 4, size);

                    int fileSize = (int) ((newChan.size() + 4095) >> 12);
                    int writePos = fileSize << 12;

                    ByteBuffer sizeBuf = ByteBuffer.allocate(4);
                    sizeBuf.putInt(size);
                    writeChunkDataAtPosAndSetPointer(data, newChan, headerOffset, fileSize, writePos, sizeBuf);
                }
            }
        }

        // 3. Swap Files
        Files.delete(filePath);
        Files.move(tempPath, filePath);
    }

    public static void compactRegion(int chunkX, int chunkZ, int dimension){
        Path filePath = PathUtils.getRegionFile(chunkX, chunkZ, dimension);

        try {
            compactRegion(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
