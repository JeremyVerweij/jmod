package com.jmod.jrender;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.jmod.jrender.JRender.MODID;
import static com.jmod.jrender.JRender.VERSION;
import static com.jmod.jrender.JRender.NAME;
import static net.minecraft.client.renderer.vertex.DefaultVertexFormats.*;

@Mod(
        modid = MODID,
        name = NAME,
        version = VERSION
)
public class JRender {
    public static final String MODID = "jrender";
    public static final String NAME = "J's Render Engine";
    public static final String VERSION = "1.0";

    public static final Logger LOGGER = LogManager.getLogger(MODID);

    @Mod.Instance(MODID)
    public static JRender instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent preInit) {
        BLOCK.clear();
        BLOCK.addElement(POSITION_3F);
        BLOCK.addElement(COLOR_4UB);
        BLOCK.addElement(TEX_2F);
        BLOCK.addElement(TEX_2S);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent init) {
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent postInit) {

    }

    @Mod.EventHandler
    public void serverStart(FMLServerStartingEvent event){

    }

    @Mod.EventHandler
    public void serverStop(FMLServerStoppingEvent event){

    }
}
