package com.tricoeverfire.tetanwr.blocks;

import java.util.Arrays;
import java.util.Random;

import com.charles445.simpledifficulty.item.ItemCanteen;
import com.tricoeverfire.tetanwr.Main;
import com.tricoeverfire.tetanwr.blocks.containers.RefineryContainer;
import com.tricoeverfire.tetanwr.init.ModCompat;
import com.tricoeverfire.tetanwr.items.LargeCharcoalFilter;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.common.Loader;

public class TileEntityRefinery extends TileEntityLockable implements ITickable, ISidedInventory
{
    private static final int[] SLOTS_FOR_UP = new int[] {3};
    private static final int[] SLOTS_FOR_DOWN = new int[] {0, 1, 2, 3};
    private static final int[] OUTPUT_SLOTS = new int[] {0, 1, 2, 4};
    private NonNullList<ItemStack> refiningItemStacks = NonNullList.<ItemStack>withSize(5, ItemStack.EMPTY);
    private int refineTime;
    private boolean[] filledSlots;
    private Item ingredientID;
    private String customName;
    private int fuel;

    public String getName()
    {
        return this.hasCustomName() ? this.customName : "container.refining";
    }

    public boolean hasCustomName()
    {
        return this.customName != null && !this.customName.isEmpty();
    }

    public void setName(String name)
    {
        this.customName = name;
    }

    public int getSizeInventory()
    {
        return this.refiningItemStacks.size();
    }

    public boolean isEmpty()
    {
        for (ItemStack itemstack : this.refiningItemStacks)
        {
            if (!itemstack.isEmpty())
            {
                return false;
            }
        }

        return true;
    }

    public void update()
    {
        ItemStack itemstack = this.refiningItemStacks.get(4);

        if (this.fuel <= 0 && TileEntityFurnace.getItemBurnTime(itemstack) > 120)
        {
            this.fuel = TileEntityFurnace.getItemBurnTime(itemstack) / 120;
            itemstack.shrink(1);
            this.markDirty();
        }

        boolean flag = this.canRefine();
        boolean flag1 = this.refineTime > 0;
        ItemStack itemstack1 = this.refiningItemStacks.get(3);
        
        if(!this.world.isRemote) {
	        if(flag&& this.fuel > 0 && !Refinary.isActive(this.world,this.pos)) {
	        	Refinary.SetState(true, this.world, this.pos);
	        }
	        if(!flag && Refinary.isActive(this.world,this.pos)) {
	        	Refinary.SetState(false, this.world, this.pos);
	        }
        }        

        if (flag1)
        {
            --this.refineTime;
            boolean flag2 = this.refineTime == 0;

            if (flag2 && flag)
            {
                this.refineWater();
                this.markDirty();
            }
            else if (!flag)
            {
                this.refineTime = 0;
                this.markDirty();
            }
            else if (this.ingredientID != itemstack1.getItem())
            {
                this.refineTime = 0;
                this.markDirty();
            }
        }
        else if (flag && this.fuel > 0)
        {
            --this.fuel;
            this.refineTime = 400;
            this.ingredientID = itemstack1.getItem();
            this.markDirty();
        }

        if (!this.world.isRemote)
        {
            boolean[] aboolean = this.createFilledSlotsArray();

            if (!Arrays.equals(aboolean, this.filledSlots))
            {
                this.filledSlots = aboolean;
                IBlockState iblockstate = this.world.getBlockState(this.getPos());
//TODO add visualization model.
//                if (!(iblockstate.getBlock() instanceof BlockrefiningStand))
//                {
//                    return;
//                }
////                TODO
//                for (int i = 0; i < BlockrefiningStand.HAS_BOTTLE.length; ++i)
//                {
//                    iblockstate = iblockstate.withProperty(BlockrefiningStand.HAS_BOTTLE[i], Boolean.valueOf(aboolean[i]));
//                }

                this.world.setBlockState(this.pos, iblockstate, 2);
            }
        }
    }

    public boolean[] createFilledSlotsArray()
    {
        boolean[] aboolean = new boolean[3];

        for (int i = 0; i < 3; ++i)
        {
            if (!((ItemStack)this.refiningItemStacks.get(i)).isEmpty())
            {
                aboolean[i] = true;
            }
        }

        return aboolean;
    }

    private boolean canRefine()
    { //TODO
  
        ItemStack itemstack = this.refiningItemStacks.get(3); //get Filter
        
        if (itemstack.isEmpty())
        {
            return false;
        }
        if((itemstack.getItem() instanceof LargeCharcoalFilter) && itemstack.getItemDamage() != itemstack.getMaxDamage()) {
        	boolean isItemInside = false;
        	int damage = 0;
            for(int i = 0; i < 3; i++) {
            	
            		ItemStack stacky = this.refiningItemStacks.get(i);

	                PotionType type = PotionUtils.getPotionFromItem(stacky);
	            	//System.out.println(type.getRegistryName());
	                if(type == PotionTypes.WATER) {
	                	isItemInside = true;
	                	damage += 1;
	                }
	                if(stacky.getItem() == Items.WATER_BUCKET) {
	                	isItemInside = true;
	                	damage += 3;
	                	//System.out.println(damage);
	                }
	                if(Loader.isModLoaded("simpledifficulty")) {
	                	if(stacky.getItem() instanceof ItemCanteen) {
	                		boolean isFull = stacky.getItemDamage() - stacky.getMaxDamage() == 0; 
	                		if(!isFull && !ModCompat.IsPureCanteen(stacky)) {
	                			damage += stacky.getMaxDamage() - stacky.getItemDamage();
	                			isItemInside = true;
	                		}
	                	}
	                }
                }
            //	System.out.println(itemstack.getItemDamage() < (itemstack.getMaxDamage() - damage) && isItemInside);
          return isItemInside && itemstack.getItemDamage() <= (itemstack.getMaxDamage() - damage);
        }

        return false;
    }

    private void refineWater()
    {
        
        ItemStack itemstack = this.refiningItemStacks.get(3);
        BlockPos blockpos = this.getPos();
        int damage = 0;
        for(int i = 0; i < 3; i++) {
        	ItemStack stacky = this.refiningItemStacks.get(i);
	        PotionType type = PotionUtils.getPotionFromItem(stacky);
	        if(type == PotionTypes.WATER) {
	        	this.setInventorySlotContents(i, new ItemStack(ModCompat.PurifiedWaterBottle));
	        	damage += 1;
	        }
	        if(stacky.getItem() == Items.WATER_BUCKET) {
	        	damage += 3;
	        	System.out.println(ModCompat.PurifiedWater);
	        	this.setInventorySlotContents(i, FluidUtil.getFilledBucket(new FluidStack(ModCompat.PurifiedWater,1)));
	        }
	        
            if(Loader.isModLoaded("simpledifficulty")) {
            	if(stacky.getItem() instanceof ItemCanteen) {
            		boolean isFull = stacky.getItemDamage() - stacky.getMaxDamage() == 0; 
            		if(!isFull && !ModCompat.IsPureCanteen(stacky)) {
            			damage += stacky.getMaxDamage() - stacky.getItemDamage();
            			this.setInventorySlotContents(i, ModCompat.PurifyCanteen(stacky));
            		}
            	}
            }
        }

        itemstack.attemptDamageItem(damage, new Random(), null);

        this.refiningItemStacks.set(3, itemstack);
        this.world.playEvent(1035, blockpos, 0);
        //net.minecraftforge.event.ForgeEventFactory.onPotionBrewed(refiningItemStacks);
    }



    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.refiningItemStacks = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, this.refiningItemStacks);
        this.refineTime = compound.getShort("BrewTime");

        if (compound.hasKey("CustomName", 8))
        {
            this.customName = compound.getString("CustomName");
        }

        this.fuel = compound.getByte("Fuel");
    }
    
    public NBTTagCompound saveToNBT(NBTTagCompound comp) {
    	
    	if(this.fuel > 0) {
    		comp.setByte("Fuel", (byte)this.fuel);
    	}
    	return comp;
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setShort("BrewTime", (short)this.refineTime);
        ItemStackHelper.saveAllItems(compound, this.refiningItemStacks);

        if (this.hasCustomName())
        {
            compound.setString("CustomName", this.customName);
        }

        compound.setByte("Fuel", (byte)this.fuel);
        return compound;
    }

    public ItemStack getStackInSlot(int index)
    {
        return index >= 0 && index < this.refiningItemStacks.size() ? (ItemStack)this.refiningItemStacks.get(index) : ItemStack.EMPTY;
    }

    public ItemStack decrStackSize(int index, int count)
    {
        return ItemStackHelper.getAndSplit(this.refiningItemStacks, index, count);
    }

    public ItemStack removeStackFromSlot(int index)
    {
        return ItemStackHelper.getAndRemove(this.refiningItemStacks, index);
    }

    public void setInventorySlotContents(int index, ItemStack stack)
    {
        if (index >= 0 && index < this.refiningItemStacks.size())
        {
            this.refiningItemStacks.set(index, stack);
        }
    }

    public int getInventoryStackLimit()
    {
        return 1;
    }

    public boolean isUsableByPlayer(EntityPlayer player)
    {
        if (this.world.getTileEntity(this.pos) != this)
        {
            return false;
        }
        else
        {
            return player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    public void openInventory(EntityPlayer player)
    {
    }

    public void closeInventory(EntityPlayer player)
    {
    }

    public boolean isItemValidForSlot(int index, ItemStack stack)
    { //TODO
        if (index == 3)
        {
            return stack.getItem() instanceof LargeCharcoalFilter;
        }
        else
        {
            Item item = stack.getItem();

            if (index == 4)
            {
                return TileEntityFurnace.getItemBurnTime(stack) > 0;
            }
            else
            {
 
                if(Loader.isModLoaded("simpledifficulty")) {
                	
                	if(stack.getItem() instanceof ItemCanteen) {
                		boolean isFull = stack.getItemDamage() - stack.getMaxDamage() == 0; 
                		if(!isFull && !ModCompat.IsPureCanteen(stack)) {
                			return true;
                		}
                	}
                }
                
                PotionType type = PotionUtils.getPotionFromItem(stack);
                return type == PotionTypes.WATER || item == Items.WATER_BUCKET;
            }
        }
    }

    public int[] getSlotsForFace(EnumFacing side)
    {
        if (side == EnumFacing.UP)
        {
            return SLOTS_FOR_UP;
        }
        else
        {
            return side == EnumFacing.DOWN ? SLOTS_FOR_DOWN : OUTPUT_SLOTS;
        }
    }

    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction)
    {
        return this.isItemValidForSlot(index, itemStackIn);
    }

    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
    {
        if (index == 3)
        {
            return stack.getItem() instanceof LargeCharcoalFilter && stack.getMaxDamage() == stack.getItemDamage();
        }
        else
        {
        	return stack.getItem() == ModCompat.PurifiedWaterBottle || FluidUtil.getFilledBucket(new FluidStack(ModCompat.PurifiedWater,1)).getItem() == stack.getItem();
        }
    }

    public String getGuiID()
    {
        return Main.MODID+":refinary";
    }

    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
    { //TODO
        return new RefineryContainer(playerInventory, this);
    }

    public int getField(int id)
    {
        switch (id)
        {
            case 0:
                return this.refineTime;
            case 1:
                return this.fuel;
            default:
                return 0;
        }
    }

    public void setField(int id, int value)
    {
        switch (id)
        {
            case 0:
                this.refineTime = value;
                break;
            case 1:
                this.fuel = value;
        }
    }

    net.minecraftforge.items.IItemHandler handlerInput = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, net.minecraft.util.EnumFacing.UP);
    net.minecraftforge.items.IItemHandler handlerOutput = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, net.minecraft.util.EnumFacing.DOWN);
    net.minecraftforge.items.IItemHandler handlerSides = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, net.minecraft.util.EnumFacing.NORTH);

    @SuppressWarnings("unchecked")
    @Override
    @javax.annotation.Nullable
    public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @javax.annotation.Nullable net.minecraft.util.EnumFacing facing)
    {
        if (facing != null && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        {
            if (facing == EnumFacing.UP)
                return (T) handlerInput;
            else if (facing == EnumFacing.DOWN)
                return (T) handlerOutput;
            else
                return (T) handlerSides;
        }
        return super.getCapability(capability, facing);
    }

    public int getFieldCount()
    {
        return 2;
    }

    public void clear()
    {
        this.refiningItemStacks.clear();
    }
}
