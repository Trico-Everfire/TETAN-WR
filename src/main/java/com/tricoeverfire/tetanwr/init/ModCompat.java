package com.tricoeverfire.tetanwr.init;

import java.util.List;

import com.charles445.simpledifficulty.api.SDFluids;
import com.charles445.simpledifficulty.api.SDItems;
import com.charles445.simpledifficulty.api.config.JsonConfig;
import com.charles445.simpledifficulty.api.config.json.JsonPropertyValue;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.tricoeverfire.tetanwr.Main;
import com.tricoeverfire.tetanwr.blocks.Refinary;
import com.tricoeverfire.tetanwr.items.Canteen;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import toughasnails.api.TANBlocks;
import toughasnails.api.item.TANItems;
import toughasnails.config.json.BlockStatePredicate;
import toughasnails.config.json.BlockTemperatureData;
import toughasnails.util.inventory.CreativeTabTAN;

public class ModCompat {
	
	public static Item CharcoalFilter;
	public static Item PurifiedWaterBottle;
	public static Fluid PurifiedWater;
	public static Item Canteen;
	public static ItemStack PurifiedWaterCanteen;
	
	public static void TANinit() {	
    	TANItems.canteen = toughasnails.init.ModItems.registerItem(new Canteen(), "canteen", CreativeTabTAN.instance);
    	List<BlockTemperatureData> defaultBlockTemperatureData = Lists.newArrayList(new BlockTemperatureData(new BlockStatePredicate(ModBlocks.Refinery.getDefaultState().withProperty(Refinary.REFINING, true), Sets.newHashSet(Refinary.REFINING)), 4.0F));
    	toughasnails.init.ModConfig.blockTemperatureData.put(Main.MODID+"refinery", defaultBlockTemperatureData);
    	CharcoalFilter = TANItems.charcoal_filter;
    	PurifiedWater = TANBlocks.purified_water_fluid;
    	System.out.println(PurifiedWater);
    	PurifiedWaterBottle = TANItems.purified_water_bottle;
    	PurifiedWaterCanteen = null; //TAN does not have this.
    	Canteen = TANItems.canteen;
	}

	public static void SDInit() {
		JsonConfig.registerBlockTemperature(ModBlocks.Refinery, 4, new JsonPropertyValue("refining","true"));
		CharcoalFilter = SDItems.charcoalFilter;
		PurifiedWaterBottle = SDItems.purifiedWaterBottle;
		PurifiedWater = SDFluids.blockPurifiedWater.getFluid();
		ItemStack canteenFull = new ItemStack(SDItems.canteen);
		canteenFull.setItemDamage(0);
		PurifiedWaterCanteen = canteenFull;
		
	}
	
}
