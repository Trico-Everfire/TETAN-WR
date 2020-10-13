package com.tricoeverfire.tetanwr.blocks;

import java.util.Random;

import com.tricoeverfire.tetanwr.Main;
import com.tricoeverfire.tetanwr.init.ModBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Refinary extends Block implements ITileEntityProvider{
	
	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	public static final PropertyBool REFINING = PropertyBool.create("refining");
	protected static final AxisAlignedBB BASE_AABBX = new AxisAlignedBB(0.1D, 0.05D, 0.0D, 0.9D, 0.95D, 1.0D);
	protected static final AxisAlignedBB BASE_AABBY = new AxisAlignedBB(0.0D, 0.05D, 0.1D, 1.0D, 0.95D, 0.9D);
	//private final VertexGroup

	public Refinary(Material materialIn) {
		super(materialIn);
		setSoundType(SoundType.METAL);
		setHarvestLevel("pickaxe", 0);
		setResistance(100.0f);
		setHardness(3.5f);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(REFINING, Boolean.FALSE));
		
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		
		return state.getValue(FACING) == EnumFacing.NORTH || state.getValue(FACING) == EnumFacing.SOUTH ? BASE_AABBX : BASE_AABBY;
	}

	
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
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
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		
		return new ItemStack(ModBlocks.Refinery);
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(!worldIn.isRemote) {
			playerIn.openGui(Main.instance, 0, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
		//return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
	}
	
	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {

		
		if(worldIn.isRemote) {
		IBlockState north = worldIn.getBlockState(pos.north());
		IBlockState south = worldIn.getBlockState(pos.south());
		IBlockState west = worldIn.getBlockState(pos.west());
		IBlockState east = worldIn.getBlockState(pos.east());
		EnumFacing face = (EnumFacing)state.getValue(FACING);
		
		if(face == EnumFacing.NORTH && north.isFullBlock() && !south.isFullBlock()) face = EnumFacing.SOUTH;
		else if(face == EnumFacing.SOUTH && south.isFullBlock() && !north.isFullBlock()) face = EnumFacing.NORTH;
		else if(face == EnumFacing.WEST && west.isFullBlock() && !east.isFullBlock()) face = EnumFacing.EAST;
		else if(face == EnumFacing.EAST && east.isFullBlock() && !west.isFullBlock()) face = EnumFacing.WEST;
		worldIn.setBlockState(pos, state.withProperty(FACING, face),2);

		}		
		
		
	}
	
	public static void SetState(boolean active, World worldIn, BlockPos pos) {
		IBlockState state = worldIn.getBlockState(pos);
		TileEntity tilentity = worldIn.getTileEntity(pos);
		
		if(active)worldIn.setBlockState(pos, ModBlocks.Refinery.getDefaultState().withProperty(FACING, state.getValue(FACING)).withProperty(REFINING, true),3);
		else worldIn.setBlockState(pos, ModBlocks.Refinery.getDefaultState().withProperty(FACING, state.getValue(FACING)).withProperty(REFINING, false),3);
		
		if(tilentity != null) {
			tilentity.validate();
			worldIn.setTileEntity(pos, tilentity);
		}
	}
	
	public static boolean isActive(World worldIn, BlockPos pos) {
		IBlockState state = worldIn.getBlockState(pos);
		//System.out.println((boolean) state.getValue(REFINING));
		return state.getValue(REFINING) != null && (boolean) state.getValue(REFINING);
	}
	
    @SideOnly(Side.CLIENT)
    @SuppressWarnings("incomplete-switch")
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        if ((boolean)stateIn.getValue(REFINING))
        {
            EnumFacing enumfacing = (EnumFacing)stateIn.getValue(FACING);
            double d0 = (double)pos.getX() + 1.2D;
            double d1 = (double)pos.getY() + rand.nextDouble() * 3.0D / 16.0D;
            double d2 = (double)pos.getZ() + 0.5D;
            double d4 = rand.nextDouble() * 0.6D - 0.3D;

            if (rand.nextDouble() < 0.1D)
            {
                worldIn.playSound((double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            }

            switch (enumfacing)
            {
                case WEST:
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 - 0.52D, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
                    worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 - 0.52D, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
                    break;
                case EAST:
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 - 0.8D , d1, d2 + d4, 0.0D, 0.0D, 0.0D);
                    worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 - 0.8D , d1, d2 + d4, 0.0D, 0.0D, 0.0D);
                    break;
                case NORTH:
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 - 0.7 + d4, d1, d2 + 0.12D, 0.0D, 0.0D, 0.0D);
                    worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 - 0.7 + d4, d1, d2 + 0.12D, 0.0D, 0.0D, 0.0D);
                    break;
                case SOUTH:
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4 - 0.8d, d1, d2 - 0.12D, 0.0D, 0.0D, 0.0D);
                    worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + d4 - 0.8d, d1, d2 - 0.12D, 0.0D, 0.0D, 0.0D);
            }
        }
    }
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		
		return new TileEntityRefinery();
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		
		//return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand);
		return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		
		 worldIn.setBlockState(pos, this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite()),2);
	}
	
	 @Override
	public int quantityDropped(IBlockState state, int fortune, Random random) {
		
		return 0;
	}
	 
	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
		
		TileEntityRefinery tileentity = (TileEntityRefinery) worldIn.getTileEntity(pos);
		if(!worldIn.isRemote) {
			ItemStack stack = new ItemStack(this);
	        NBTTagCompound nbttagcompound = new NBTTagCompound();
	        NBTTagCompound nbttagcompound1 = new NBTTagCompound();
	        nbttagcompound.setTag("BlockEntityTag", ((TileEntityRefinery)tileentity).saveToNBT(nbttagcompound1));
	        stack.setTagCompound(nbttagcompound);
	        
	        if (tileentity.hasCustomName())
	        {
	            stack.setStackDisplayName(tileentity.getName());
	            tileentity.setName("");
	        }
	        
	        if(player.canHarvestBlock(state) && !player.isCreative())
	        if(true) {
	    
	        InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), stack);
	        }
			}    
		super.onBlockHarvested(worldIn, pos, state, player);
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		TileEntityRefinery tileentity = (TileEntityRefinery) worldIn.getTileEntity(pos);
		InventoryHelper.dropInventoryItems(worldIn, pos, tileentity);
		super.breakBlock(worldIn, pos, state);
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		
		return EnumBlockRenderType.MODEL;
	}
	
	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot) {
		
		return state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
	}
	
	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
		
		 return state.withRotation(mirrorIn.toRotation((EnumFacing)state.getValue(FACING)));
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		
		return new BlockStateContainer(this, new IProperty[] {REFINING,FACING});
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {

		EnumFacing facing = EnumFacing.getFront(meta);
		if(facing.getAxis() == EnumFacing.Axis.Y) facing = EnumFacing.NORTH;
		return this.getDefaultState().withProperty(FACING, facing);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		
		return ((EnumFacing)state.getValue(FACING)).getIndex();
	}

}
