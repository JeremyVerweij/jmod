package com.jmod.jqol;

import com.jmod.jqol.settings.KeyBindComponent;
import com.jmod.jui.ui.UIComponentCollection;
import com.jmod.jui.ui.UIDocument;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = "jqol", useMetadata = true, dependencies = "required-after:jmod;required-after:jui")
public class JQol {
    @Mod.Instance
    public static JQol instance;

    public UIDocument controls;

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event){
        if (event.getSide() == Side.CLIENT){
            UIComponentCollection.addComponent("keyBinding", KeyBindComponent::new);

            this.controls = new UIDocument("/ui/controls.xml", Minecraft.getMinecraft());
        }
    }
}
