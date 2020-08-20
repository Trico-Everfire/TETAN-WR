package com.tricoeverfire.tetanwr.blocks.containers;

import com.charles445.simpledifficulty.item.ItemCanteen;
import com.tricoeverfire.tetanwr.init.ModCompat;
import com.tricoeverfire.tetanwr.items.LargeCharcoalFilter;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RefineryContainer extends Container{

    private final IInventory tileRefiningStand;
    private final Slot slot;
    private int prevRefineTime;
    private int prevFuel;

    public RefineryContainer(InventoryPlayer playerInventory, IInventory tileRefiningStandIn)
    {
        this.tileRefiningStand = tileRefiningStandIn;
        this.addSlotToContainer(new RefineryContainer.Water(tileRefiningStandIn, 0, 56, 51));
        this.addSlotToContainer(new RefineryContainer.Water(tileRefiningStandIn, 1, 79, 58));
        this.addSlotToContainer(new RefineryContainer.Water(tileRefiningStandIn, 2, 102, 51));
        this.slot = this.addSlotToContainer(new RefineryContainer.Filter(tileRefiningStandIn, 3, 79, 17));
        this.addSlotToContainer(new RefineryContainer.Fuel(tileRefiningStandIn, 4, 17, 17));

        for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k)
        {
            this.addSlotToContainer(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
    }

    public void addListener(IContainerListener listener)
    {
        super.addListener(listener);
        listener.sendAllWindowProperties(this, this.tileRefiningStand);
    }

    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (int i = 0; i < this.listeners.size(); ++i)
        {
            IContainerListener icontainerlistener = this.listeners.get(i);

            if (this.prevRefineTime != this.tileRefiningStand.getField(0))
            {
                icontainerlistener.sendWindowProperty(this, 0, this.tileRefiningStand.getField(0));
            }

            if (this.prevFuel != this.tileRefiningStand.getField(1))
            {
                icontainerlistener.sendWindowProperty(this, 1, this.tileRefiningStand.getField(1));
            }
        }

        this.prevRefineTime = this.tileRefiningStand.getField(0);
        this.prevFuel = this.tileRefiningStand.getField(1);
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data)
    {
        this.tileRefiningStand.setField(id, data);
    }

    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return this.tileRefiningStand.isUsableByPlayer(playerIn);
    }

    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if ((index < 0 || index > 2) && index != 3 && index != 4)
            {
                if (this.slot.isItemValid(itemstack1))
                {
                    if (!this.mergeItemStack(itemstack1, 3, 4, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (RefineryContainer.Water.canHoldWater(itemstack) && itemstack.getCount() == 1)
                {
                    if (!this.mergeItemStack(itemstack1, 0, 3, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (RefineryContainer.Fuel.isValidRefiningFuel(itemstack))
                {
                    if (!this.mergeItemStack(itemstack1, 4, 5, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index >= 5 && index < 32)
                {
                    if (!this.mergeItemStack(itemstack1, 32, 41, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index >= 32 && index < 41)
                {
                    if (!this.mergeItemStack(itemstack1, 5, 32, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (!this.mergeItemStack(itemstack1, 5, 41, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else
            {
                if (!this.mergeItemStack(itemstack1, 5, 41, true))
                {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }

            if (itemstack1.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount())
            {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }

    static class Fuel extends Slot
        {
            public Fuel(IInventory iInventoryIn, int index, int xPosition, int yPosition)
            {
                super(iInventoryIn, index, xPosition, yPosition);
            }

            public boolean isItemValid(ItemStack stack)
            {
                return isValidRefiningFuel(stack);
            }

            public static boolean isValidRefiningFuel(ItemStack itemStackIn)
            {
                return TileEntityFurnace.getItemBurnTime(itemStackIn) > 120;
            }

            public int getSlotStackLimit()
            {
                return 64;
            }
        }

    static class Filter extends Slot
        {
            public Filter(IInventory iInventoryIn, int index, int xPosition, int yPosition)
            {
                super(iInventoryIn, index, xPosition, yPosition);
            }

            public boolean isItemValid(ItemStack stack)
            {
                return stack.getItem() instanceof LargeCharcoalFilter;
            }

            public int getSlotStackLimit()
            {
                return 1;
            }
        }

    static class Water extends Slot
        {
            public Water(IInventory p_i47598_1_, int p_i47598_2_, int p_i47598_3_, int p_i47598_4_)
            {
                super(p_i47598_1_, p_i47598_2_, p_i47598_3_, p_i47598_4_);
            }

            public boolean isItemValid(ItemStack stack)
            {
                return canHoldWater(stack);
            }

            public int getSlotStackLimit()
            {
                return 1;
            }

            public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack) {

                super.onTake(thePlayer, stack);
                return stack;
            }

            public static boolean canHoldWater(ItemStack stack)
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
                return type == PotionTypes.WATER || stack.getItem() == Items.WATER_BUCKET;
            }
        }
}
