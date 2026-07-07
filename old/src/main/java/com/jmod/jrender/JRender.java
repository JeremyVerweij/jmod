package com.jmod.jrender;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.jmod.jrender.JRender.MODID;
import static com.jmod.jrender.JRender.VERSION;
import static com.jmod.jrender.JRender.NAME;

@Mod(
        modid = MODID,
        name = NAME,
        version = VERSION
)
//VANILLA: 1200-1800
public class JRender {
    public static final String MODID = "jrender";
    public static final String NAME = "J's Render Engine";
    public static final String VERSION = "1.0";

    public static Logger LOGGER = LogManager.getLogger(NAME);

    public static Logger logger() {
        if (LOGGER == null) {
            LOGGER = LogManager.getLogger(NAME);
        }

        return LOGGER;
    }

    public static String getVersion() {
        return VERSION;
    }
}
