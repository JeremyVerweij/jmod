package com.jmod;

import com.jmod.core.proxy.CommonProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
	modid = Tags.MOD_ID,
	name = Tags.MOD_NAME,
	version = Tags.MOD_VERSION
)
public class JMod {
	public static final Logger LOGGER = LogManager.getLogger(Tags.MOD_ID);

	@Mod.Instance("jmod")
	public static JMod instance;

	@SidedProxy(clientSide = "com.jmod.core.proxy.ClientProxy")
	public static CommonProxy proxy;

	public JMod(){

	}

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
