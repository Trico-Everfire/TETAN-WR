package com.tricoeverfire.tetanwr.util;

import javax.annotation.Nullable;

import net.minecraft.item.Item;

public interface ItemSoftRepair {
	
	Item repairWith();
	
	int devideMaxHealthBy(@Nullable int i);

}
