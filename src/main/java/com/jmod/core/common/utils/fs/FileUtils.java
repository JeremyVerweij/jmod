package com.jmod.core.common.utils.fs;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileUtils {
    public static FileChannel createChannelRead(@NotNull Path path) throws IOException {
        return FileChannel.open(path, StandardOpenOption.READ);
    }

    public static FileChannel createChannelWrite(@NotNull Path path) throws IOException {
        return FileChannel.open(path, StandardOpenOption.WRITE, StandardOpenOption.READ);
    }

    public static @NotNull ByteBuffer readSectionNIO(@NotNull FileChannel channel, int offset, int length) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(length);

        channel.read(buffer, offset);

        buffer.flip();
        return buffer;
    }

    public static void writeSectionNio(@NotNull FileChannel channel, int offset, ByteBuffer buffer) throws IOException{
        channel.write(buffer, offset);
    }
}
