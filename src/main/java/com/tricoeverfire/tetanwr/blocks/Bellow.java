package com.tricoeverfire.tetanwr.blocks;

import com.tricoeverfire.tetanwr.init.ModConfig;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Bellow extends Block {

	public static final PropertyInteger bellowStage = PropertyInteger.create("bellow_stage", 0, 2);
	public int bellowTimer = 0;
	
	public Bellow(Material materialIn) {
		super(materialIn);
		setSoundType(SoundType.WOOD);
		setHarvestLevel("axe", 0);
		setResistance(100.0f);
		setHardness(3.5f);
	}
	
	@Override
	public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
		
		super.onEntityWalk(worldIn, pos, entityIn);
	}
	
	@Override
	public boolean isFullCube(IBlockState state) {
		
		return false;
	}
	@Override
	public boolean isFullBlock(IBlockState state) {
		
		return false;
	}
	
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }
    
	@Override
	protected BlockStateContainer createBlockState() {
		
		return new BlockStateContainer(this, new IProperty[] {bellowStage});
	}

}
