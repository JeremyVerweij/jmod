package com.jmod.jui.ui.interfaces;

import com.jmod.jui.components.BaseComponent;

public interface IOffsetProvider {
    int getOffsetX(BaseComponent component);
    int getOffsetY(BaseComponent component);

    IOffsetProvider DEFAULT_OFFSET_PROVIDER = new IOffsetProvider(){
        @Override
        public int getOffsetX(BaseComponent component) {
            return 0;
        }

        @Override
        public int getOffsetY(BaseComponent component) {
            return 0;
        }
    };
}
