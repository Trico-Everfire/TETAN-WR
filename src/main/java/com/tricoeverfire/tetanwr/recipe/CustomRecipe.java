package com.tricoeverfire.tetanwr.recipe;

import java.util.List;

import com.google.common.collect.Lists;
import com.tricoeverfire.tetanwr.init.ModCompat;
import com.tricoeverfire.tetanwr.init.ModItems;
import com.tricoeverfire.tetanwr.items.LargeCharcoalFilter;
import com.tricoeverfire.tetanwr.util.ItemSoftRepair;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeRepairItem;
import net.minecraft.world.World;

public class CustomRecipe extends RecipeRepairItem {

    public boolean matches(InventoryCrafting inv, World worldIn)
  {
      List<ItemStack> repairList = Lists.<ItemStack>newArrayList();
      List<ItemStack> ingredientList = Lists.<ItemStack>newArrayList();

      for (int i = 0; i < inv.getSizeInventory(); ++i)
      {
          ItemStack itemstack = inv.getStackInSlot(i);

          if (!itemstack.isEmpty())
          {
              if(itemstack.getItem() instanceof ItemSoftRepair)
                  repairList.add(itemstack);
              else
                  ingredientList.add(itemstack);
          }
      }

      //Check if there are too little repairables or too many items
      if(repairList.size() == 0 || (repairList.size() + ingredientList.size()) > 4)
          return false;

      //Verify all repairables are the same
      if(!verifyAllEqualUsingALoop(repairList))
          return false;

      //Check if there are enough repairables or enough ingredients
      if(repairList.size() == 1 && ingredientList.size() == 0)
          return false;

      //Check all ingredients for compatibility
      ItemSoftRepair repositoryItem = (ItemSoftRepair) repairList.get(0).getItem();

      for(ItemStack ingredient : ingredientList)
      {
          if(repositoryItem.repairWith() != ingredient.getItem())
              return false;
      }

      //All checks succeeded
      return true;
  }
	
    @Override
   	public ItemStack getCraftingResult(InventoryCrafting inv) {

   	        List<ItemStack> list = Lists.<ItemStack>newArrayList();

   	        for (int i = 0; i < inv.getSizeInventory(); ++i)
   	        {
   	            ItemStack itemstack = inv.getStackInSlot(i);

   	            if (!itemstack.isEmpty())
   	            {
   	                list.add(itemstack);

   	            }
   	        }


   	        if (list.size() <= 4)
   	        {


   	            List<ItemStack> repList = Lists.<ItemStack>newArrayList();
   	            List<ItemStack> ingList = Lists.<ItemStack>newArrayList();

   	            for(ItemStack entry : list) {

   	            	if(entry.getItem() instanceof ItemSoftRepair) {
   	            		repList.add(entry);
   	            	} else {
   	            		ingList.add(entry);
   	            	}

   	            }
   	            if(repList.size() == 1 && ingList.size() == 0) {
   	            	return ItemStack.EMPTY;
   	            }

   	            if(repList.size() == 0) return ItemStack.EMPTY;

   	            		ItemStack repositoryItemStack = repList.get(0);
   	            		ItemSoftRepair repositoryItem = (ItemSoftRepair) repList.get(0).getItem();
   	            		if(repositoryItemStack.getItemDamage() - repositoryItemStack.getMaxDamage() != repositoryItemStack.getMaxDamage() ) {

   	            			for(ItemStack repit : ingList) {
   	            				if(repositoryItem.repairWith() != repit.getItem()) {
   	            					return ItemStack.EMPTY;
   	            				}
   	            			}

   		            		if(!verifyAllEqualUsingALoop(repList)) {
   		            			return ItemStack.EMPTY;
   		            		}

   	            			int oldDamage = 0;

   	            			for(ItemStack stack : repList) {
   	            				oldDamage += stack.getMaxDamage() - stack.getItemDamage();
   	            			}

   	            			int totalDamage = oldDamage + (repositoryItemStack.getMaxDamage() * ingList.size()) / repositoryItem.devideMaxHealthBy(0);
   	            			System.out.println(totalDamage);
   	    	                return new ItemStack(repositoryItemStack.getItem(), 1, repositoryItemStack.getMaxDamage() - totalDamage);
   	            		}
   	            	}


   	        return ItemStack.EMPTY;
   	}
	public boolean verifyAllEqualUsingALoop(List<ItemStack> list) {
		if(list.size() == 0) {
			return false;
		}
	    for (ItemStack s : list) {
	        if (!s.getItem().equals(list.get(0).getItem()))
	            return false;
	    }
	    return true;
	}
}
