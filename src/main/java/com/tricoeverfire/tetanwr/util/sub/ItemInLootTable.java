package com.tricoeverfire.tetanwr.util.sub;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class ItemInLootTable {
	
	//public static final List<ItemInLootTable> IILT = Arrays.asList(new ItemInLootTable(ModItems.,null,null));
	
	private final ResourceLocation location;
	private final Item item;
	private final int weight;
	
	public ItemInLootTable(ResourceLocation location,Item item, int weight) {
		this.item = item;
		this.location = location;
		this.weight = weight;
	}
	
	public ResourceLocation getLocation() {
		return this.location;
	}
	
	public Item getItem() {
		return this.item;
	}
	
	public int getWeight() {
		return this.weight;
	}
}
