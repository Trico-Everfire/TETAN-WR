package com.tricoeverfire.tetanwr.util.handlers;

import com.tricoeverfire.tetanwr.blocks.TileEntityRefinery;
import com.tricoeverfire.tetanwr.blocks.containers.RefineryContainer;
import com.tricoeverfire.tetanwr.blocks.gui.GuiRefineryContainer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler{

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(ID == 0) {
			return new RefineryContainer(player.inventory,(TileEntityRefinery)world.getTileEntity(new BlockPos(x,y,z)));
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(ID == 0) {
			return new GuiRefineryContainer(player.inventory,(TileEntityRefinery)world.getTileEntity(new BlockPos(x,y,z)));
		}
		return null;
	}

}
