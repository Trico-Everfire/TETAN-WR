package com.tricoeverfire.tetanwr.util.handlers;

import com.tricoeverfire.tetanwr.Main;
import com.tricoeverfire.tetanwr.blocks.TileEntityRefinery;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TileEntityHandler {

	public static void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityRefinery.class, new ResourceLocation(Main.MODID,"refinery"));
	}
	
}
