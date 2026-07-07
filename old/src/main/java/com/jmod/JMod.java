package com.jmod;

import com.jmod.core.proxy.CommonProxy;
import com.jmod.jmod.Reference;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
	modid = Reference.MOD_ID,
	name = Reference.MOD_NAME,
	version = Reference.VERSION
)
public class JMod {
	public static final Logger LOGGER = LogManager.getLogger(Reference.MOD_ID);

	@Mod.Instance("jmod")
	public static JMod instance;

	public static CommonProxy proxy;

	public JMod(){
		System.out.println("JMOD CONSTRUCTOR");
	}

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent preInit) {
		System.out.println("JMOD PREINIT");

		Side side = FMLCommonHandler.instance().getSide();

		if (side == Side.CLIENT) {
			proxy = new com.jmod.core.proxy.ClientProxy();
		} else {
			proxy = new com.jmod.core.proxy.CommonProxy();
		}

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
