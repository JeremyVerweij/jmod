package com.jmod.mixin.jui;

import it.unimi.dsi.fastutil.objects.Reference2ReferenceMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.registries.IRegistryDelegate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemColors.class)
public class MixinItemColors {
    /**
     * @author jui
     * @reason fixed weird rendering stuff caused by Minecraft
     */
    @Overwrite
    public static ItemColors init(final BlockColors colors){
        ItemColors itemcolors = new ItemColors();
        itemcolors.registerItemColorHandler(new IItemColor() {
            public int colorMultiplier(ItemStack stack, int tintIndex) {
                return tintIndex > 0 ? -1 : ((ItemArmor)stack.getItem()).getColor(stack) | 0xFF000000;
            }
        }, Items.LEATHER_HELMET, Items.LEATHER_CHESTPLATE, Items.LEATHER_LEGGINGS, Items.LEATHER_BOOTS);
        itemcolors.registerItemColorHandler(new IItemColor() {
            public int colorMultiplier(ItemStack stack, int tintIndex) {
                BlockDoublePlant.EnumPlantType blockdoubleplant$enumplanttype = BlockDoublePlant.EnumPlantType.byMetadata(stack.getMetadata());
                return (blockdoubleplant$enumplanttype != BlockDoublePlant.EnumPlantType.GRASS && blockdoubleplant$enumplanttype != BlockDoublePlant.EnumPlantType.FERN ? -1 : ColorizerGrass.getGrassColor((double)0.5F, (double)1.0F)) | 0xFF000000;
            }
        }, Blocks.DOUBLE_PLANT);
        itemcolors.registerItemColorHandler(new IItemColor() {
            public int colorMultiplier(ItemStack stack, int tintIndex) {
                if (tintIndex != 1) {
                    return -1;
                } else {
                    NBTBase nbtbase = ItemFireworkCharge.getExplosionTag(stack, "Colors");
                    if (!(nbtbase instanceof NBTTagIntArray)) {
                        return 9079434 | 0xFF000000;
                    } else {
                        int[] aint = ((NBTTagIntArray)nbtbase).getIntArray();
                        if (aint.length == 1) {
                            return aint[0] | 0xFF000000;
                        } else {
                            int i = 0;
                            int j = 0;
                            int k = 0;

                            for(int l : aint) {
                                i += (l & 16711680) >> 16;
                                j += (l & '\uff00') >> 8;
                                k += (l & 255) >> 0;
                            }

                            i /= aint.length;
                            j /= aint.length;
                            k /= aint.length;
                            return (i << 16 | j << 8 | k) | 0xFF000000;
                        }
                    }
                }
            }
        }, Items.FIREWORK_CHARGE);
        itemcolors.registerItemColorHandler(new IItemColor() {
            public int colorMultiplier(ItemStack stack, int tintIndex) {
                return tintIndex > 0 ? -1 : PotionUtils.getColor(stack) | 0xFF000000;
            }
        }, Items.POTIONITEM, Items.SPLASH_POTION, Items.LINGERING_POTION);
        itemcolors.registerItemColorHandler(new IItemColor() {
            public int colorMultiplier(ItemStack stack, int tintIndex) {
                EntityList.EntityEggInfo entitylist$entityegginfo = (EntityList.EntityEggInfo)EntityList.ENTITY_EGGS.get(ItemMonsterPlacer.getNamedIdFrom(stack));
                if (entitylist$entityegginfo == null) {
                    return -1;
                } else {
                    return (tintIndex == 0 ? entitylist$entityegginfo.primaryColor : entitylist$entityegginfo.secondaryColor) | 0xFF000000;
                }
            }
        }, Items.SPAWN_EGG);
        itemcolors.registerItemColorHandler((stack, tintIndex) -> {
            IBlockState iblockstate = ((ItemBlock)stack.getItem()).getBlock().getStateFromMeta(stack.getMetadata());
            return (colors.colorMultiplier(iblockstate, (IBlockAccess)null, (BlockPos)null, tintIndex)) | 0xFF000000;
        }, Blocks.GRASS, Blocks.TALLGRASS, Blocks.VINE, Blocks.LEAVES, Blocks.LEAVES2, Blocks.WATERLILY);
        itemcolors.registerItemColorHandler(new IItemColor() {
            public int colorMultiplier(ItemStack stack, int tintIndex) {
                return (tintIndex == 0 ? PotionUtils.getColor(stack) : -1) | 0xFF000000;
            }
        }, Items.TIPPED_ARROW);
        itemcolors.registerItemColorHandler(new IItemColor() {
            public int colorMultiplier(ItemStack stack, int tintIndex) {
                return (tintIndex == 0 ? -1 : ItemMap.getColor(stack)) | 0xFF000000;
            }
        }, Items.FILLED_MAP);
        ForgeHooksClient.onItemColorsInit(itemcolors, colors);
        return itemcolors;
    }
}
