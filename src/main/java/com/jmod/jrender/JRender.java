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
public class JRender {
    public static final String MODID = "jrender";
    public static final String NAME = "J's Render Engine";
    public static final String VERSION = "1.0";

    private static com.jmod.jrender.client.gui.SodiumGameOptions CONFIG;
    public static Logger LOGGER = LogManager.getLogger(NAME);

    public static com.jmod.jrender.client.gui.SodiumGameOptions options() {
        if (CONFIG == null) {
            CONFIG = loadConfig();
        }

        return CONFIG;
    }

    public static Logger logger() {
        if (LOGGER == null) {
            LOGGER = LogManager.getLogger(NAME);
        }

        return LOGGER;
    }

    private static com.jmod.jrender.client.gui.SodiumGameOptions loadConfig() {
        return com.jmod.jrender.client.gui.SodiumGameOptions.load(Minecraft.getMinecraft().gameDir.toPath().resolve("config").resolve(MODID + "-options.json"));
    }

    public static String getVersion() {
        return VERSION;
    }

    public static boolean isDirectMemoryAccessEnabled() {
        return options().advanced.allowDirectMemoryAccess;
    }
}
