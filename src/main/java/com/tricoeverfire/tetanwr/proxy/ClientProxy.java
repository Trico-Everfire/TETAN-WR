package com.tricoeverfire.tetanwr.proxy;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class ClientProxy extends CommonProxy {
	@Override
	public void registerItemRenderer(Item item, int meta, String id) {
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));	
		//ModItemColors.registerItemColors();
	}
	
  //  @SuppressWarnings("rawtypes")
	@Override
    public void registerBlockSided(Block block)
    {
//        if (block instanceof ITANBlock)
//        {
//            ITANBlock bopBlock = (ITANBlock) block;
//
//            //Register non-rendering properties
//            IProperty[] nonRenderingProperties = bopBlock.getNonRenderingProperties();
//
//            if (nonRenderingProperties != null)
//            {
//                // use a custom state mapper which will ignore the properties specified in the block as being non-rendering
//                IStateMapper custom_mapper = (new StateMap.Builder()).ignore(nonRenderingProperties).build();
//                ModelLoader.setCustomStateMapper(block, custom_mapper);
//            }
//        }
    }
	
//	@Override
//	public void abstractItemRenderer(GemBase item, int meta, String id){
//		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation("voxelitemod:"+item.getType(), id));
//	}
	

	
	 
}
