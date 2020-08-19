package com.tricoeverfire.tetanwr.util.handlers;

import java.util.List;

import com.google.common.collect.Lists;
import com.tricoeverfire.tetanwr.Main;
import com.tricoeverfire.tetanwr.init.ModBlocks;
import com.tricoeverfire.tetanwr.init.ModItems;
import com.tricoeverfire.tetanwr.recipe.CustomRecipe;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class RegistryHandler {

	
	
	private static List<ResourceLocation> thisList = Lists.newArrayList(LootTableList.CHESTS_ABANDONED_MINESHAFT,LootTableList.CHESTS_DESERT_PYRAMID,LootTableList.CHESTS_END_CITY_TREASURE,LootTableList.CHESTS_IGLOO_CHEST,LootTableList.CHESTS_JUNGLE_TEMPLE,LootTableList.CHESTS_STRONGHOLD_CORRIDOR,LootTableList.CHESTS_STRONGHOLD_CROSSING,LootTableList.CHESTS_VILLAGE_BLACKSMITH,LootTableList.CHESTS_WOODLAND_MANSION,LootTableList.CHESTS_SIMPLE_DUNGEON);

	@SubscribeEvent //add IRecipe event method
	public static void onRecipeRegister(RegistryEvent.Register<IRecipe> event) {
		event.getRegistry().register(new CustomRecipe().setRegistryName(new ResourceLocation(Main.MODID,"repairCustomItem")));

	}
	@SubscribeEvent //add IRecipe event method
	public static void onblockRegister(RegistryEvent.Register<Block> event) {
		TileEntityHandler.registerTileEntities();
	}
	


	@SubscribeEvent
    public static void onLootTableLoad(LootTableLoadEvent event)
    {
		
		if (event.getName().equals(LootTableList.CHESTS_SPAWN_BONUS_CHEST))
		{
            LootPool main = event.getTable().getPool("main");
            if (main != null)
            {
                main.addEntry(new LootEntryItem(ModItems.LargeCharcoalFilter, 3, 1, new LootFunction[0], new LootCondition[0],  ModItems.LargeCharcoalFilter.getRegistryName().toString()));
                main.addEntry(new LootEntryItem(Item.getItemFromBlock(ModBlocks.Refinery),2,1, new LootFunction[0],new LootCondition[0], Main.MODID+":refinery"));
            }
		}
		
		for(ResourceLocation table : thisList) {
			if(event.getName().equals(table)) {
				
	            LootPool main = event.getTable().getPool("main");
	            if (main != null){
	                main.addEntry(new LootEntryItem(ModItems.LargeCharcoalFilter, 2, 1, new LootFunction[0], new LootCondition[0], ModItems.LargeCharcoalFilter.getRegistryName().toString()));
	                main.addEntry(new LootEntryItem(Item.getItemFromBlock(ModBlocks.Refinery),1,1, new LootFunction[0],new LootCondition[0], Main.MODID+":refinery"));
	                main.addEntry(new LootEntryItem(ModItems.IndustrialGradeLargeCharcoalFilter, 1, 1, new LootFunction[0], new LootCondition[0], ModItems.IndustrialGradeLargeCharcoalFilter.getRegistryName().toString()));
	            }
			}
		}
		//CHESTS_SIMPLE_DUNGEON
		//CHESTS_VILLAGE_BLACKSMITH
		//CHESTS_ABANDONED_MINESHAFT
		//CHESTS_NETHER_BRIDGE
		
    }
	
	
	
	
	
}

