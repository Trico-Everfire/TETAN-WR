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
import toughasnails.init.ModItems;

public class ModCompat {
	
	public static Item CharcoalFilter;
	public static Item PurifiedWaterBottle;
	public static Fluid PurifiedWater;
	public static Item Canteen;
	public static ItemStack PurifiedWaterCanteen;
	
	public ModCompat() {
		
	}
	
	public static void TANinit() {
		try {
    	TANItems.canteen = ModItems.registerItem(ModCompatMini.Canteen, "canteen", CreativeTabTAN.instance);
    	List<BlockTemperatureData> defaultBlockTemperatureData = Lists.newArrayList(new BlockTemperatureData(new BlockStatePredicate(ModBlocks.Refinery.getDefaultState().withProperty(Refinary.REFINING, true), Sets.newHashSet(Refinary.REFINING)), 4.0F));
    	toughasnails.init.ModConfig.blockTemperatureData.put(Main.MODID+"refinery", defaultBlockTemperatureData);
    	CharcoalFilter = TANItems.charcoal_filter;
    	PurifiedWater = TANBlocks.purified_water_fluid;
    	PurifiedWaterBottle = TANItems.purified_water_bottle;
    	PurifiedWaterCanteen = null; //TAN does not have this.
    	Canteen = TANItems.canteen;
		} catch(Exception e) {
			
		}
	}
	
	public static void SDInit() {
		try {
		JsonConfig.registerBlockTemperature(ModBlocks.Refinery, 4, new JsonPropertyValue("refining","true"));
		CharcoalFilter = SDItems.charcoalFilter;
		PurifiedWaterBottle = SDItems.purifiedWaterBottle;
		PurifiedWater = SDFluids.blockPurifiedWater.getFluid();
		ItemStack canteenFull = new ItemStack(SDItems.canteen);
		PurifiedWaterCanteen = PurifyCanteen(canteenFull);
		Canteen = SDItems.canteen;
		} catch (Exception e) {
			
		}
		
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
	
//	public static class Canteen extends toughasnails.item.ItemCanteen{
//
//	    public Canteen()
//	    {
//	        super();
//	    }
//	    
//	    @Override
//	    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
//	    {
//	        ItemStack stack = player.getHeldItem(hand);
//	        ThirstHandler thirstStats = (ThirstHandler)player.getCapability(TANCapabilities.THIRST, null);
//	        WaterType waterType = this.getTypeFromMeta(stack.getMetadata());
//	        
//	        if (!attemptCanteenFill(player, stack) && waterType != null && getTimesUsed(stack) < 3 && thirstStats.isThirsty())
//	        {
//	            player.setActiveHand(hand);
//	        }
//	        
//	        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
//	    }
//	  
//	    private int getTimesUsed(ItemStack stack)
//	    {
//	        return stack.getItemDamage() >> 2;
//	    }
//	    
//	    /**
//	     * Attempt to fill the provided canteen stack with water.
//	     * @param player The player holding the canteen.
//	     * @param stack The canteen item stack.
//	     * @return true if successful, otherwise false.
//	     */
//	   
//	    private boolean attemptCanteenFill(EntityPlayer player, ItemStack stack)
//	    {
//	        World world = player.world;
//	        RayTraceResult movingObjectPos = this.rayTrace(world, player, true);
//	        
//	        if (movingObjectPos != null && movingObjectPos.typeOfHit == RayTraceResult.Type.BLOCK)
//	        {
//	            BlockPos pos = movingObjectPos.getBlockPos();
//	            IBlockState state = world.getBlockState(pos);
//	            Fluid fluid = FluidRegistry.lookupFluidForBlock(state.getBlock());
//
//	            if (fluid != null && fluid == FluidRegistry.WATER) //Temporary, until a registry is created
//	            {
//	                stack.setItemDamage(1);
//	                return true;
//	            }
//	            else if (state.getBlock() instanceof BlockCauldron)
//	            {
//	                BlockCauldron cauldron = (BlockCauldron)state.getBlock();
//	                int level = ((Integer)state.getValue(BlockCauldron.LEVEL));
//	                
//	                if (level > 0 && !world.isRemote)
//	                {
//	                    if (!player.capabilities.isCreativeMode)
//	                    {
//	                        player.addStat(StatList.CAULDRON_USED);
//	                        cauldron.setWaterLevel(world, pos, state, level - 1); //fixed cauldron drainage issue in survival
//	                        stack.setItemDamage(1);
//	                        return true;
//	                    }
//
//	                    
//	                }
//	            }
//	        }
//	        
//	        return false;
//	    }
//	  
//	}

	
	
	
}
