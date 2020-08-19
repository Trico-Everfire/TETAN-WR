package com.tricoeverfire.tetanwr.init;

import com.tricoeverfire.tetanwr.Main;
import com.tricoeverfire.tetanwr.items.LargeCharcoalFilter;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ModItems {

//	public static final List<Item> ITEMS = new ArrayList<Item>();
	

	
	public static Item LargeCharcoalFilter;
	public static Item IndustrialGradeLargeCharcoalFilter;

	public static void init() {
		LargeCharcoalFilter = registerItem(new LargeCharcoalFilter().setNoRepair(),"large_charcoal_filter",TatanwrCreativeTab.instance);
		IndustrialGradeLargeCharcoalFilter = registerItem(new LargeCharcoalFilter(2,(stack)->{
			float getItemDurability = stack.getMaxDamage() / ((com.tricoeverfire.tetanwr.items.LargeCharcoalFilter) IndustrialGradeLargeCharcoalFilter).devideMaxHealthBy(0);

			if((stack.getMaxDamage() - stack.getItemDamage()) >= (getItemDurability * 2) + 1) {
				return "item.industrial_grade_large_charcoal_filter_full";
			}
			if((stack.getMaxDamage() - stack.getItemDamage()) >= (getItemDurability + 1) && (stack.getMaxDamage() - stack.getItemDamage()) < (getItemDurability * 2) + 1) {
				return "item.industrial_grade_large_charcoal_filter_almost_full";
			}
			if((stack.getMaxDamage() - stack.getItemDamage()) >= 1 && (stack.getMaxDamage() - stack.getItemDamage()) < getItemDurability + 1) {
				return "item.industrial_grade_large_charcoal_filter_almost_empty";
			} else {
				return "item.industrial_grade_large_charcoal_filter_empty";
			}
			
		
		}).setNoRepair(),"industrial_grade_large_charcoal_filter",TatanwrCreativeTab.instance);
	}
	

    public static Item registerItem(Item item, String name, CreativeTabs tab)
    {
        item.setUnlocalizedName(name);
        if (tab != null)
        {
            item.setCreativeTab(tab);
        }

        item.setRegistryName(new ResourceLocation(Main.MODID, name));
        ForgeRegistries.ITEMS.register(item);

        Main.proxy.registerItemRenderer(item, 0, "inventory");

        return item;
    }
}


