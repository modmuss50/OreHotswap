package me.modmuss50.orehotswap;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import me.modmuss50.orehotswap.config.ConfigLoader;
import net.minecraftforge.common.MinecraftForge;
import reborncore.common.IModInfo;
import reborncore.common.util.LogHelper;

import java.io.File;
import java.io.FileNotFoundException;

@Mod(modid = "orehotswap", name = "OreHotSwap", version = "1")
public class OreHotSwap implements IModInfo {

    public static ConfigLoader loader;
    public static LogHelper logHelper;
    public static OreGenerator oreGenerator;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logHelper = new LogHelper(this);
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
        event.registerServerCommand(new Command());
    }


    @Override
    public String MOD_NAME() {
        return "OreHotSwap";
    }

    @Override
    public String MOD_ID() {
        return "orehotswap";
    }

    @Override
    public String MOD_VERSION() {
        return "1";
    }

    @Override
    public String MOD_DEPENDENCUIES() {
        return "";
    }
}
