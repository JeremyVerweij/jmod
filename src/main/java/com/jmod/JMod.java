package com.jmod;

import com.jmod.core.proxy.CommonProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
	modid = JMod.MODID,
	name = JMod.NAME,
	version = JMod.VERSION
)
public class JMod {
	public static final String MODID = "jmod";
	public static final String NAME = "J's Mod";
	public static final String VERSION = "1.0";
	
	public static final Logger LOGGER = LogManager.getLogger(MODID);

	@Mod.Instance("jmod")
	public static JMod instance;

	@SidedProxy(
			clientSide = "com.jmod.core.proxy.ClientProxy"
	)
	public static CommonProxy proxy;
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent preInit) {
		MinecraftForge.EVENT_BUS.register(proxy);
		proxy.preInit(preInit);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent init) {
		proxy.init(init);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent postInit) {
		proxy.postInit(postInit);
	}

	@Mod.EventHandler
	public void serverStart(FMLServerStartingEvent event){
		proxy.onServerStart(event);
	}

	@Mod.EventHandler
	public void serverStop(FMLServerStoppingEvent event){
		proxy.onServerStop(event);
	}
}
