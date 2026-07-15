package com.jmod.jui.proxy;

import com.jmod.jui.JUI;
import com.jmod.jui.components.*;
import com.jmod.jui.ui.UIComponentCollection;
import com.jmod.jui.ui.UIDocument;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

public class ClientProxy extends CommonProxy{
    public static ClientProxy getClientProxy(){
        return ((ClientProxy) JUI.proxy);
    }

    public UIDocument controls;

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        UIComponentCollection.addComponent("undefined", UndefinedComponent::new);
        UIComponentCollection.addComponent("ui", UIComponent::new);
        UIComponentCollection.addComponent("label", LabelComponent::new);
        UIComponentCollection.addComponent("button", ButtonComponent::new);
        UIComponentCollection.addComponent("input", TextInputComponent::new);
        UIComponentCollection.addComponent("scrollbar", ScrollbarComponent::new);
        UIComponentCollection.addComponent("verticalViewport", VerticalViewportComponent::new);
        UIComponentCollection.addComponent("verticalList", VerticalListComponent::new);
        UIComponentCollection.addComponent("choice", ChoiceComponent::new);
        UIComponentCollection.addComponent("option", OptionComponent::new);

        this.controls = new UIDocument("/ui/controls.xml", Minecraft.getMinecraft());
    }
}
