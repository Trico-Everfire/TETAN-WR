package com.tricoeverfire.tetanwr;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tricoeverfire.tetanwr.init.ModCompat;
import com.tricoeverfire.tetanwr.init.ModRecipes;
import com.tricoeverfire.tetanwr.proxy.CommonProxy;
import com.tricoeverfire.tetanwr.util.handlers.GuiHandler;

import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

@Mod(modid = Main.MODID, name = Main.NAME, version = Main.VERSION)
public class Main
{
    public static final String MODID = "tatanaw";
    public static final String NAME = "TATAN WR Mod";
    public static final String VERSION = "1.0";

    @Instance
    public static Main instance;
    
    public static final String CLIENTPROXY = "com.tricoeverfire.tetanwr.proxy.ClientProxy";
    public static final String COMMONPROXY = "com.tricoeverfire.tetanwr.proxy.CommonProxy";
    
    @SidedProxy(clientSide = CLIENTPROXY, serverSide = COMMONPROXY)
    public static CommonProxy proxy;
    
    private static Logger logger = LogManager.getLogger(MODID);

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	
    	com.tricoeverfire.tetanwr.init.ModItems.init();
    	com.tricoeverfire.tetanwr.init.ModBlocks.init();
    	
        logger = event.getModLog();
    
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	NetworkRegistry.INSTANCE.registerGuiHandler(Main.instance, new GuiHandler());
    	ModRecipes.init();
        logger.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    	if(Loader.isModLoaded("simpledifficulty") && Loader.isModLoaded("toughasnails")) {
    		//logger.fatal("Cannot have both ToughAsNails and SimpleDifficulty installed at the same time.");
    		throw new Error("Cannot have both ToughAsNails and SimpleDifficulty installed at the same time.");
    	}
    	
    	if(!Loader.isModLoaded("simpledifficulty") && !Loader.isModLoaded("toughasnails")) {
    		//logger.fatal("You do not have either ToughAsNails or SimpleDifficulty Installed.");
    		throw new Error("You do not have either ToughAsNails or SimpleDifficulty Installed.");
    	}
    	
    	if(Loader.isModLoaded("simpledifficulty")) {
    		ModCompat.SDInit();
    	}
    	if(Loader.isModLoaded("toughasnails")) {
    		ModCompat.TANinit();
    	}
    }
}
