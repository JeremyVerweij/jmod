package com.jrender.mixin;

import com.jrender.common.ICustomDebug;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiOverlayDebug;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.common.property.IExtendedBlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(GuiOverlayDebug.class)
public class MixinGuiDebug {
    @Inject(method = "getDebugInfoRight", at = @At("RETURN"), cancellable = true)
    private void injectCustomDebugInfo(CallbackInfoReturnable<List<String>> cir) {
        Minecraft mc = Minecraft.getMinecraft();

        if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK && mc.objectMouseOver.getBlockPos() != null) {
            BlockPos pos = mc.objectMouseOver.getBlockPos();
            IBlockState state = mc.world.getBlockState(pos);
            List<String> list = cir.getReturnValue();

            if (state.getBlock() instanceof ICustomDebug customDebug) {
                customDebug.addToDebug(list, (IExtendedBlockState) state.getBlock().getExtendedState(state, mc.world, pos));
            }
        }
    }
}
