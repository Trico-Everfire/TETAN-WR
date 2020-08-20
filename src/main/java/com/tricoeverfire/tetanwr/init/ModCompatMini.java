package com.tricoeverfire.tetanwr.init;

import net.minecraft.block.BlockCauldron;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import toughasnails.api.TANCapabilities;
import toughasnails.api.thirst.WaterType;
import toughasnails.item.ItemCanteen;
import toughasnails.thirst.ThirstHandler;

public class ModCompatMini {

	public static final Item Canteen = new ItemCanteen() {
	    @Override
	    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	    {
	        ItemStack stack = player.getHeldItem(hand);
	        ThirstHandler thirstStats = (ThirstHandler)player.getCapability(TANCapabilities.THIRST, null);
	        WaterType waterType = this.getTypeFromMeta(stack.getMetadata());
	        
	        if (!attemptCanteenFill(player, stack) && waterType != null && getTimesUsed(stack) < 3 && thirstStats.isThirsty())
	        {
	            player.setActiveHand(hand);
	        }
	        
	        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
	    }
	  
	    private int getTimesUsed(ItemStack stack)
	    {
	        return stack.getItemDamage() >> 2;
	    }
	    
	    /**
	     * Attempt to fill the provided canteen stack with water.
	     * @param player The player holding the canteen.
	     * @param stack The canteen item stack.
	     * @return true if successful, otherwise false.
	     */
	   
	    private boolean attemptCanteenFill(EntityPlayer player, ItemStack stack)
	    {
	        World world = player.world;
	        RayTraceResult movingObjectPos = this.rayTrace(world, player, true);
	        
	        if (movingObjectPos != null && movingObjectPos.typeOfHit == RayTraceResult.Type.BLOCK)
	        {
	            BlockPos pos = movingObjectPos.getBlockPos();
	            IBlockState state = world.getBlockState(pos);
	            Fluid fluid = FluidRegistry.lookupFluidForBlock(state.getBlock());

	            if (fluid != null && fluid == FluidRegistry.WATER) //Temporary, until a registry is created
	            {
	                stack.setItemDamage(1);
	                return true;
	            }
	            else if (state.getBlock() instanceof BlockCauldron)
	            {
	                BlockCauldron cauldron = (BlockCauldron)state.getBlock();
	                int level = ((Integer)state.getValue(BlockCauldron.LEVEL));
	                
	                if (level > 0 && !world.isRemote)
	                {
	                    if (!player.capabilities.isCreativeMode)
	                    {
	                        player.addStat(StatList.CAULDRON_USED);
	                        cauldron.setWaterLevel(world, pos, state, level - 1); //fixed cauldron drainage issue in survival
	                        stack.setItemDamage(1);
	                        return true;
	                    }

	                    
	                }
	            }
	        }
	        
	        return false;
	    };
	};

};