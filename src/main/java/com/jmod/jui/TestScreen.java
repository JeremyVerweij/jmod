package com.jmod.jui;

import com.jmod.jui.components.list.VerticalListComponent;
import com.jmod.jui.proxy.ClientProxy;
import com.jmod.jui.ui.JUIScreen;
import com.jmod.jui.ui.UIDocument;

public class TestScreen extends JUIScreen {
    @Override
    protected UIDocument getUIDocument() {
        return ClientProxy.getClientProxy().controls;
    }

    @Override
    protected String getTitleTranslationKey() {
        return "test";
    }

    @Override
    protected void initJUI(UIDocument document) {
//        document.<ButtonComponent>getComponent("test").setOnClickEvent((comp, btn) -> {
//            System.out.println("I AM CLICKED");
//        });

        VerticalListComponent comp = document.getComponent("test");
        comp.addEntry(new VerticalListComponent.ListEntry(comp, () -> "1", () -> "2"));
        comp.addEntry(new VerticalListComponent.ListEntry(comp, () -> "3", () -> "4"));
    }
}
