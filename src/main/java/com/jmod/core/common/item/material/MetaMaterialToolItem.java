package com.jmod.core.common.item.material;

import com.jmod.JMod;
import com.jmod.core.common.block.interfaces.IRequireTool;
import com.jmod.core.common.item.ToolType;
import com.jmod.core.common.item.interfaces.nbt.IDamageable;
import com.jmod.core.common.item.interfaces.IToolItem;
import com.jmod.core.common.utils.MiningTier;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;

public abstract class MetaMaterialToolItem extends MetaMaterialItem implements IToolItem, IDamageable {
    private final ToolType toolType;
    
    public MetaMaterialToolItem(String modId, String registryName, ToolType toolType) {
        super(modId, registryName);
        this.toolType = toolType;
    }

    @Override
    public void addInformation(@NonNull ItemStack stack, @Nullable World worldIn, List<String> tooltip, @NonNull ITooltipFlag flagIn) {
        tooltip.add(I18n.format("jmod.tools.durability", (getMaxDamageFromItemStack(stack) - getDamageFromItemStack(stack)), getMaxDamageFromItemStack(stack)));

        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public boolean showDurabilityBar(@NonNull ItemStack stack) {
        return true;
    }

    @Override
    public double getDurabilityForDisplay(@NonNull ItemStack stack) {
        return (double)getDamageFromItemStack(stack) / (double)getMaxDamageFromItemStack(stack);
    }

    @Override
    public boolean canHarvestBlock(IBlockState state, @NonNull ItemStack stack) {
        if (state.getBlock() instanceof IRequireTool requireTool){
            return requireTool.isToolEffective(stack);
        }
        
        return true;
    }

    @Override
    public boolean onBlockDestroyed(@NonNull ItemStack stack, @NonNull World worldIn, @NonNull IBlockState state, @NonNull BlockPos pos, @NonNull EntityLivingBase entityLiving) {
        if ((entityLiving instanceof EntityPlayer player) && !player.isCreative() &&
                !worldIn.isRemote && (double)state.getBlockHardness(worldIn, pos) != (double)0.0F) {
            damageItemStack(stack, 1);
        }

        return true;
    }

    @Override
    public float getDestroySpeed(@NonNull ItemStack stack, IBlockState state) {
        if (state.getBlock() instanceof IRequireTool requireTool){
            if (requireTool.isToolEffective(stack)){
                return this.getToolEfficiency(stack);
            }
        }

        return 1.0F;
    }

    @Override
    public boolean hitEntity(@NonNull ItemStack stack, @NonNull EntityLivingBase target, @NonNull EntityLivingBase attacker) {
        if ((attacker instanceof EntityPlayer player) && !player.isCreative()) {
            damageItemStack(stack, 2);
        }

        return true;
    }

    @Override
    public int getMaxDamageFromItemStack(ItemStack stack){
        return JMod.proxy.getMaterialRegistry().toList().get(stack.getMetadata()).getToolDurability();
    }

    @Override
    public ToolType getToolType() {
        return toolType;
    }

    @Override
    public MiningTier getToolTier(@NonNull ItemStack stack){
        return JMod.proxy.getMaterialRegistry().toList().get(stack.getMetadata()).getToolTier();
    }

    public float getToolEfficiency(ItemStack stack){
        return JMod.proxy.getMaterialRegistry().toList().get(stack.getMetadata()).getToolMiningSpeed();
    }
}
