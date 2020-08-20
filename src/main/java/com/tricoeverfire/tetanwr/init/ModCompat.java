package com.tricoeverfire.tetanwr.init;

import java.util.List;

import com.charles445.simpledifficulty.api.SDFluids;
import com.charles445.simpledifficulty.api.SDItems;
import com.charles445.simpledifficulty.api.config.JsonConfig;
import com.charles445.simpledifficulty.api.config.json.JsonPropertyValue;
import com.charles445.simpledifficulty.api.thirst.ThirstEnum;
import com.charles445.simpledifficulty.item.ItemCanteen;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.tricoeverfire.tetanwr.Main;
import com.tricoeverfire.tetanwr.blocks.Refinary;
import com.tricoeverfire.tetanwr.items.Canteen;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagInt;
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
		PurifiedWaterCanteen = PurifyCanteen(canteenFull);
		Canteen = SDItems.canteen;
		
	}
	
	public static ItemStack PurifyCanteen(ItemStack stack) {
		stack.setTagInfo(ItemCanteen.CANTEENTYPE, new NBTTagInt(ThirstEnum.PURIFIED.ordinal()));
		return stack;
	}
	
	public static boolean IsPureCanteen(ItemStack stack) {
		return getTypeTag(stack).getInt() == ThirstEnum.PURIFIED.ordinal();
	}
	
	public static NBTTagInt getTypeTag(ItemStack stack)
	{
		if(stack.getTagCompound()==null)
		{
			//setTypeTag(stack,ThirstEnum.NORMAL.ordinal());
			stack.setTagInfo(ItemCanteen.CANTEENTYPE, new NBTTagInt(ThirstEnum.NORMAL.ordinal()));
			stack.setItemDamage(stack.getMaxDamage());
			//setCanteenEmpty(stack);
		}
		
		NBTBase tag = stack.getTagCompound().getTag(ItemCanteen.CANTEENTYPE);
		if(tag instanceof NBTTagInt)
		{
			return (NBTTagInt)tag;
		}
		else
		{
			stack.getTagCompound().removeTag(ItemCanteen.CANTEENTYPE);
			stack.setTagInfo(ItemCanteen.CANTEENTYPE, new NBTTagInt(ThirstEnum.NORMAL.ordinal()));
			return new NBTTagInt(ThirstEnum.NORMAL.ordinal());
		}
	}
	
	
}
