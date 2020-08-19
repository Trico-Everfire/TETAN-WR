package com.tricoeverfire.tetanwr.init;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class TatanwrCreativeTab  extends CreativeTabs{

	public static final CreativeTabs instance = new TatanwrCreativeTab(CreativeTabs.getNextID(),"tatanwr");
	
	public TatanwrCreativeTab(int index, String label) {
		super(index, label);
		
	}

	@Override
	public ItemStack getTabIconItem() {
		return new ItemStack(ModItems.LargeCharcoalFilter);
	}
	
	

}
