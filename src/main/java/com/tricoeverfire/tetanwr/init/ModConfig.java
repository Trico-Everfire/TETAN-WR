package com.tricoeverfire.tetanwr.init;

import com.tricoeverfire.tetanwr.Main;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Config(modid = Main.MODID)
public class ModConfig {

	@Config.Comment("the total amount of refine time needed to refine an item.")
	@Config.Name("Refine Time")
	public static int refinetime = 400;
	
	@Config.Comment("the total heat level")
	@Config.Name("Refinery Heat Level")
	@Config.RequiresMcRestart
	public static float refineryHeatLevel = 4.0f;
	
	@Mod.EventBusSubscriber(modid = Main.MODID)
	private static class EventHandler
	{
		@SubscribeEvent
		@SideOnly(Side.CLIENT)
		public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
		{
			if(event.getModID().equals(Main.MODID))
			{
				ConfigManager.sync(Main.MODID, Config.Type.INSTANCE);
			}
		}
	}
	
}
