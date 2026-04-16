package com.jmod.core.client.overlay;

import com.jmod.JMod;
import com.jmod.core.common.block.IWrenchable;
import com.jmod.core.common.item.WrenchItem;
import com.jmod.core.common.utils.random.RotationUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;

import static com.jmod.core.common.utils.random.RotationUtils.rotateSide;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = JMod.MODID)
public class WrenchOverlayHandler {

    @SubscribeEvent
    public static void onDrawBlockHighlight(DrawBlockHighlightEvent event) {
        EntityPlayer player = event.getPlayer();
        ItemStack heldItem = player.getHeldItemMainhand();

        if (heldItem.getItem() instanceof WrenchItem) {
            RayTraceResult target = event.getTarget();

            if (target != null && target.typeOfHit == RayTraceResult.Type.BLOCK) {
                BlockPos pos = target.getBlockPos();
                World world = player.world;
                IBlockState state = world.getBlockState(pos);

                if (state.getBlock() instanceof IWrenchable) {
                    drawOverlay(world, state, player, pos, event.getPartialTicks(), target.sideHit);
                }
            }
        }
    }

    private static void drawOverlay(World world, IBlockState state, EntityPlayer player, BlockPos pos, float partialTicks, EnumFacing side) {
        // Prepare OpenGL state
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(true);
        GlStateManager.glLineWidth(5);

        //Get Tessellator
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        // Translate to the block's position relative to the camera
        double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
        double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
        double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;
        float r = 0, g = 0, b = 0, a = 1;

        AxisAlignedBB box = new AxisAlignedBB(pos).offset(-d0, -d1, -d2); // Slightly larger to avoid Z-fighting

        buffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
        drawLineOnSide(side, 0, 0, 0, 1, box, buffer, r, g, b, a);
        drawLineOnSide(side, 0, 1, 1, 1, box, buffer, r, g, b, a);
        drawLineOnSide(side, 1, 1, 1, 0, box, buffer, r, g, b, a);
        drawLineOnSide(side, 1, 0, 0, 0, box, buffer, r, g, b, a);

        drawLineOnSide(side, 0.2, 0, 0.2, 1, box, buffer, r, g, b, a);
        drawLineOnSide(side, 0.8, 0, 0.8, 1, box, buffer, r, g, b, a);
        drawLineOnSide(side, 0, 0.2, 1, 0.2, box, buffer, r, g, b, a);
        drawLineOnSide(side, 0, 0.8, 1, 0.8, box, buffer, r, g, b, a);

        byte connected = ((IWrenchable) state.getBlock()).getSidesConnectForOverlay(world, pos);

        if (((1 << side.getIndex()) & connected) > 0){
            drawXOnSide(side, 0.2, 0.2, 0.8, 0.8, box, buffer, r, g, b, a);
        }

        if (((1 << side.getOpposite().getIndex()) & connected) > 0){
            drawXOnSide(side, 0, 0, 0.2, 0.2, box, buffer, r, g, b, a);
            drawXOnSide(side, 0.8, 0, 1, 0.2, box, buffer, r, g, b, a);
            drawXOnSide(side, 0.8, 0.8, 1, 1, box, buffer, r, g, b, a);
            drawXOnSide(side, 0, 0.8, 0.2, 1, box, buffer, r, g, b, a);
        }

        EnumFacing left = rotateSide(side, RotationUtils.EnumSide2D.LEFT);
        EnumFacing right = rotateSide(side, RotationUtils.EnumSide2D.RIGHT);
        EnumFacing top = rotateSide(side, RotationUtils.EnumSide2D.UP);
        EnumFacing bottom = rotateSide(side, RotationUtils.EnumSide2D.BOTTOM);

        if (((top != null ? 1 <<  top.getIndex() : 0) & connected) > 0) {
            drawXOnSide(side, 0.2, 0.8, 0.8, 1.0, box, buffer, r, g, b, a);
        }

        if (((bottom != null ? 1 << bottom.getIndex() : 0) & connected) > 0) {
            drawXOnSide(side, 0.2, 0.0, 0.8, 0.2, box, buffer, r, g, b, a);
        }

        if (((left != null ? 1 << left.getIndex() : 0) & connected) > 0) {
            drawXOnSide(side, 0.0, 0.2, 0.2, 0.8, box, buffer, r, g, b, a);
        }

        if (((right != null ? 1 << right.getIndex() : 0) & connected) > 0) {
            drawXOnSide(side, 0.8, 0.2, 1.0, 0.8, box, buffer, r, g, b, a);
        }

        tessellator.draw();

        GlStateManager.glLineWidth(1f);
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    private static void drawXOnSide(EnumFacing side, double xS, double yS, double xE, double yE,
                                    @Nonnull AxisAlignedBB box, BufferBuilder buffer, float r, float g, float b, float a){
        drawLineOnSide(side, xS, yS, xE, yE, box, buffer, r, g, b, a);
        drawLineOnSide(side, xS, yE, xE, yS, box, buffer, r, g, b, a);
    }

    //NORTH: -Z, SOUTH: +Z, WEST: -X, EAST: +X
    private static final double OFFSET = 0.002;
    private static void drawLineOnSide(EnumFacing side, double xS, double yS, double xE, double yE,
                                       @Nonnull AxisAlignedBB box, BufferBuilder buffer, float r, float g, float b, float a){
        double minX = box.minX, minY = box.minY, minZ = box.minZ;
        double maxX = box.maxX, maxY = box.maxY, maxZ = box.maxZ;

        double zStart = 0, zEnd = 0, xStart = 0, xEnd = 0, yStart = 0, yEnd = 0;

        if (side == EnumFacing.NORTH || side == EnumFacing.SOUTH){
            xStart = minX + xS;
            xEnd = minX + xE;
            yStart = minY + yS;
            yEnd = minY + yE;
            double z = side == EnumFacing.NORTH ? minZ - OFFSET : maxZ + OFFSET;
            zStart = z;
            zEnd = z;
        } else if (side == EnumFacing.EAST || side == EnumFacing.WEST){
            double x = side == EnumFacing.WEST ? minX - OFFSET : maxX + OFFSET;
            xStart = x;
            xEnd = x;
            yStart = minY+ yS;
            yEnd = minY + yE;
            zStart = minZ + xS;
            zEnd = minZ + xE;
        } else if (side == EnumFacing.DOWN || side == EnumFacing.UP){
            xStart = minX + xS;
            xEnd = minX + xE;
            double y = side == EnumFacing.DOWN ? minY - OFFSET : maxY + OFFSET;
            yStart = y;
            yEnd = y;
            zStart = minZ + yS;
            zEnd = minZ + yE;
        }

        buffer.pos(xStart, yStart, zStart).color(r, g, b, a).endVertex();
        buffer.pos(xEnd, yEnd, zEnd).color(r, g, b, a).endVertex();
    }
}
