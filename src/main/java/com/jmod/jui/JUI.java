package com.jmod.jui;

import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.jmod.jui.JUI.MODID;
import static com.jmod.jui.JUI.NAME;
import static com.jmod.jui.JUI.VERSION;

@Mod(
        modid = MODID,
        name = NAME,
        version = VERSION
)
public class JUI {
    public static final String MODID = "jui";
    public static final String NAME = "J's UI Library";
    public static final String VERSION = "1.0";

    public static Logger LOGGER = LogManager.getLogger(NAME);

    public static Logger logger() {
        if (LOGGER == null) {
            LOGGER = LogManager.getLogger(NAME);
        }

        return LOGGER;
    }
}
