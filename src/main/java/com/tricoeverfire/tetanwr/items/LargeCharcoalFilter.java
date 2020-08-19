package com.tricoeverfire.tetanwr.items;

import javax.annotation.Nullable;

import com.tricoeverfire.tetanwr.init.ModCompat;
import com.tricoeverfire.tetanwr.util.ItemSoftRepair;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;



public class LargeCharcoalFilter extends Item implements ItemSoftRepair{
	
	public interface getStackNameResult {
		String result(ItemStack stack);
	}
	
	private final @Nullable getStackNameResult predic;
	
	public LargeCharcoalFilter() {
		this(1,null);
	}
	
	public LargeCharcoalFilter(int multiplier) {
		this(multiplier,null);
	}
	
	public LargeCharcoalFilter(int multiplier, getStackNameResult predic){
		this.predic = predic;
	//	super();
		this.addPropertyOverride(new ResourceLocation("filterlength"), new IItemPropertyGetter() {

			@Override
			public float apply(ItemStack stack, World worldIn, EntityLivingBase entityIn) {
				
				float getItemDurability = stack.getMaxDamage() / devideMaxHealthBy(0);

				if((stack.getMaxDamage() - stack.getItemDamage()) >= (getItemDurability * 2) + 1) {
					return 3;
				}
				if((stack.getMaxDamage() - stack.getItemDamage()) >= (getItemDurability + 1) && (stack.getMaxDamage() - stack.getItemDamage()) < (getItemDurability * 2) + 1) {
					return 2;
				}
				if((stack.getMaxDamage() - stack.getItemDamage()) >= 1 && (stack.getMaxDamage() - stack.getItemDamage()) < getItemDurability + 1) {
					return 1;
				} else {
					return 0;
				}
			}

		});
		
		setMaxDamage((8*multiplier)*devideMaxHealthBy(0));
		setMaxStackSize(1);
		//setNoRepair();
		
	}

public LargeCharcoalFilter(int i, Object object) {
		this(i);
	}

@Override
public String getUnlocalizedName(ItemStack stack) {
	
	if(predic == null) {
		float getItemDurability = stack.getMaxDamage() / devideMaxHealthBy(0);

		if((stack.getMaxDamage() - stack.getItemDamage()) >= (getItemDurability * 2) + 1) {
			return "item.large_charcoal_filter_full";
		}
		if((stack.getMaxDamage() - stack.getItemDamage()) >= (getItemDurability + 1) && (stack.getMaxDamage() - stack.getItemDamage()) < (getItemDurability * 2) + 1) {
			return "item.large_charcoal_filter_almost_full";
		}
		if((stack.getMaxDamage() - stack.getItemDamage()) >= 1 && (stack.getMaxDamage() - stack.getItemDamage()) < getItemDurability + 1) {
			return "item.large_charcoal_filter_almost_empty";
		} else {
			return "item.large_charcoal_filter_empty";
		}
	} else {
		return predic.result(stack);
	}
}
	
	@Override
	public Item repairWith() {
		// TODO Auto-generated method stub
		return ModCompat.CharcoalFilter;
	}

	@Override
	public int devideMaxHealthBy(@Nullable int i) {
		
		return 3;
	}


	
	

}
