package com.jmod.core.common.item.material;

import com.jmod.JMod;
import com.jmod.core.common.block.interfaces.IRequireTool;
import com.jmod.core.common.item.ToolType;
import com.jmod.core.common.utils.MiningTier;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jspecify.annotations.Nullable;

import java.util.List;

public abstract class MetaMaterialToolItem extends MetaMaterialItem{
    public static final String DAMAGE_NBT_TAG = "damage";
    private final ToolType toolType;
    
    public MetaMaterialToolItem(String modId, String registryName, ToolType toolType) {
        super(modId, registryName);
        this.toolType = toolType;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add((getMaxDamageFromItemStack(stack) - getDamageFromItemStack(stack)) + "/"
                + getMaxDamageFromItemStack(stack));

        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return (double)getDamageFromItemStack(stack) / (double)getMaxDamageFromItemStack(stack);
    }

    @Override
    public boolean canHarvestBlock(IBlockState state, ItemStack stack) {
        if (state.getBlock() instanceof IRequireTool requireTool){
            return requireTool.isToolEffective(stack);
        }
        
        return true;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
        if ((entityLiving instanceof EntityPlayer player) && !player.isCreative() &&
                !worldIn.isRemote && (double)state.getBlockHardness(worldIn, pos) != (double)0.0F) {
            damageItemStack(stack, 1);
        }

        return true;
    }

    @Override
    public float getDestroySpeed(ItemStack stack, IBlockState state) {
        if (state.getBlock() instanceof IRequireTool requireTool){
            if (requireTool.isToolEffective(stack)){
                return this.getToolEfficiency(stack);
            }
        }

        return 1.0F;
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        if ((attacker instanceof EntityPlayer player) && !player.isCreative()) {
            damageItemStack(stack, 2);
        }

        return true;
    }

    private void createTag(ItemStack stack){
        stack.setTagCompound(new NBTTagCompound());
        stack.getTagCompound().setInteger(DAMAGE_NBT_TAG, 0);
    }

    public int getDamageFromItemStack(ItemStack stack){
        if (!stack.hasTagCompound())
            createTag(stack);

        return stack.getTagCompound().getInteger(DAMAGE_NBT_TAG);
    }

    public void setDamageToItemStack(ItemStack stack, int damage){
        if (!stack.hasTagCompound())
            createTag(stack);

        stack.getTagCompound().setInteger(DAMAGE_NBT_TAG, damage);
    }

    public void damageItemStack(ItemStack stack, int damage){
        setDamageToItemStack(stack, getDamageFromItemStack(stack) + damage);

        if (getDamageFromItemStack(stack) > getMaxDamageFromItemStack(stack)){
            stack.shrink(1);
        }
    }

    public int getMaxDamageFromItemStack(ItemStack stack){
        return JMod.proxy.getMaterialRegistry().toList().get(stack.getMetadata()).getToolDurability();
    }

    public ToolType getToolType() {
        return toolType;
    }

    public MiningTier getToolTier(ItemStack stack){
        return JMod.proxy.getMaterialRegistry().toList().get(stack.getMetadata()).getToolTier();
    }

    public float getToolEfficiency(ItemStack stack){
        return JMod.proxy.getMaterialRegistry().toList().get(stack.getMetadata()).getToolMiningSpeed();
    }
}
