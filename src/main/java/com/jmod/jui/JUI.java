package com.jmod.jui;

import com.jmod.jui.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
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

    @SidedProxy(clientSide = "com.jmod.jui.proxy.ClientProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event){
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event){
        proxy.postInit(event);
    }
}
