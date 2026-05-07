package com.jmod.mixin.jrender.features.debug;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.jmod.jrender.JRender;
import com.jmod.jrender.client.render.SodiumWorldRenderer;
import com.jmod.jrender.client.render.chunk.ChunkRenderBackend;
import com.jmod.jrender.common.ICustomDebug;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiOverlayDebug;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.property.IExtendedBlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

@Mixin(GuiOverlayDebug.class)
public abstract class MixinDebugHud {
    @Shadow
    private static long bytesToMb(long bytes) {
        throw new UnsupportedOperationException();
    }

    @Redirect(method = "getDebugInfoRight", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/Lists;newArrayList([Ljava/lang/Object;)Ljava/util/ArrayList;"))
    private ArrayList<String> redirectRightTextEarly(Object[] elements) {
        ArrayList<String> strings = Lists.newArrayList((String[]) elements);
        strings.add("");
        strings.add(JRender.NAME + " Renderer");
        strings.add(TextFormatting.UNDERLINE + getFormattedVersionText());
        strings.add("");
        strings.addAll(getChunkRendererDebugStrings());

        if (JRender.options().advanced.ignoreDriverBlacklist) {
            strings.add(TextFormatting.RED + "(!!) Driver blacklist ignored");
        }

        for (int i = 0; i < strings.size(); i++) {
            String str = strings.get(i);

            if (str.startsWith("Allocated:")) {
                strings.add(i + 1, getNativeMemoryString());

                break;
            }
        }

        return strings;
    }

    @Inject(method = "getDebugInfoRight", at = @At(value = "RETURN"))
    private void addRExtendedBlockInfoToDebugMenu(CallbackInfoReturnable<List<String>> cir){
        if (Minecraft.getMinecraft().objectMouseOver != null &&
                Minecraft.getMinecraft().objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK &&
                Minecraft.getMinecraft().objectMouseOver.getBlockPos() != null) {

            BlockPos blockpos = Minecraft.getMinecraft().objectMouseOver.getBlockPos();
            IBlockState iblockstate = Minecraft.getMinecraft().world.getBlockState(blockpos);

            if (iblockstate.getBlock() instanceof ICustomDebug customDebug && iblockstate instanceof IExtendedBlockState){
                customDebug.addToDebug(cir.getReturnValue(), (IExtendedBlockState)
                        iblockstate.getBlock().getExtendedState(iblockstate,
                                Minecraft.getMinecraft().world,
                                blockpos));
            }
        }
    }

    private static String getFormattedVersionText() {
        String version = JRender.getVersion();
        TextFormatting color;

        if (version.contains("git.")) {
            color = TextFormatting.RED;
        } else {
            color = TextFormatting.GREEN;
        }

        return color + version;
    }

    private static List<String> getChunkRendererDebugStrings() {
        SodiumWorldRenderer renderer = SodiumWorldRenderer.getInstanceNullable();
        if (renderer == null)
            return ImmutableList.of();
        ChunkRenderBackend<?> backend = renderer.getChunkRenderer();

        List<String> strings = new ArrayList<>(5);
        strings.add("Chunk Renderer: " + backend.getRendererName());
        strings.addAll(backend.getDebugStrings());

        return strings;
    }

    private static String getNativeMemoryString() {
        return "Off-Heap: +" + bytesToMb(ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage().getUsed()) + "MB";
    }
}
