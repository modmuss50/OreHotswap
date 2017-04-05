package me.modmuss50.orehotswap;

import me.modmuss50.orehotswap.config.ConfigLoader;
import me.modmuss50.orehotswap.lib.LogHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import java.io.File;
import java.io.FileNotFoundException;

@Mod(modid = "orehotswap", name = "OreHotSwap", version = "1")
public class OreHotSwap {

	public static ConfigLoader loader;
	public static LogHelper logHelper;
	public static OreGenerator oreGenerator;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logHelper = new LogHelper();
		this.loader = new ConfigLoader(new File(event.getModConfigurationDirectory(), "orehotswap"));
		try {
			loader.load();
		} catch (FileNotFoundException e) {
			//TODO logger
			System.out.println("Bad things happened, fix your .json file");
			e.printStackTrace();
		}
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		oreGenerator = new OreGenerator();
		MinecraftForge.EVENT_BUS.register(oreGenerator);
		FMLCommonHandler.instance().bus().register(oreGenerator);
	}

	@Mod.EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandHS());
	}
}
