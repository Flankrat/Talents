package com.seniors.talents.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.seniors.talents.Talents;
import com.seniors.talents.config.TalentsItemTagsLock;
import com.seniors.talents.config.models.LockItem;
import com.seniors.talents.config.models.LockItem.Aptitude;
import com.seniors.talents.handler.HandlerAptitude;
import com.seniors.talents.handler.HandlerLockItemsConfig;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TieredItem;
import net.minecraftforge.registries.ForgeRegistries;

public class TalentsItemLocksHandler {
	private static List<String> validAptitudes = List.of("attack","gathering","building","magic","farming","agility","mining", "defense");
	private static List<String> validclassess = List.of("swordItem","shovelItem","pickaxeItem","axeItem","hoeItem",	"armorItem");

	@SuppressWarnings("deprecation")
	public static void loadLocks() {
		Set<String> addedtags = new HashSet<String>();
		Set<String> existingItems = HandlerLockItemsConfig.HANDLER.instance()
		        .lockItemList
		        .stream()
		        .map(li -> li.Item)
		        .collect(Collectors.toSet());
		ArrayList<LockItem> mutable = new ArrayList<>(HandlerLockItemsConfig.HANDLER.instance().lockItemList);
		if (!TalentsItemTagsLock.HANDLER.instance().itemtagsList.isEmpty()) {
			for (String data : TalentsItemTagsLock.HANDLER.instance().itemtagsList) {
				String[] info = data.split(";");
				if (info.length > 2) {
					Talents.getLOGGER().info("Excessive arguments in lock items config, data: " + data);
					continue;
				}
				// 0 is item tag, the rest are aptitudes
				ResourceLocation itemtag = new ResourceLocation(info[0]);
				TagKey<Item> tagKey = TagKey.create(ForgeRegistries.Keys.ITEMS, itemtag);
				List<Aptitude> aptitudeList = new ArrayList<>();
				for (String aptitude : info[1].split(",")) {
					String[] text = aptitude.split(":");
					String cleaned = text[0].strip(); //aptitude name
					String level = text[1].strip(); //aptitude level
					if (!validAptitudes.contains(cleaned)) {
						Talents.getLOGGER().info("Aptitude specified does not exist, data: " + data);
						continue;
					}
					
					aptitudeList.add(new LockItem.Aptitude(cleaned, Integer.parseInt(level)));
					
				}
				LockItem.Aptitude[] aptitudes = aptitudeList.toArray(new LockItem.Aptitude[0]);
				if (aptitudeList.isEmpty()) {
					continue;
				}
				aptitudeList.clear();
				BuiltInRegistries.ITEM.getTag(tagKey).ifPresent(tag -> {
				    for (Holder<Item> holder : tag) {
				        Item item = holder.value();
				        ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
				        String idString = id.toString();
				        if (existingItems.contains(idString)) continue;
				        addedtags.add(idString);
				        mutable.add(new LockItem(idString, aptitudes));
				    }
				});
			}
		}
		
		if (!TalentsItemTagsLock.HANDLER.instance().itemMaterialList.isEmpty()) {
			for (String data : TalentsItemTagsLock.HANDLER.instance().itemMaterialList) {
				String[] info = data.split(";");
				if (info.length > 2) {
					Talents.getLOGGER().info("Excessive arguments in lock items config, data: " + data);
					continue;
				}
				// 0 is item tag, the rest are aptitudes
				if (!validclassess.contains( info[0].strip().split(",")[0].strip())) {
					Talents.getLOGGER().info("ItemClass defined in : " + data + " does not exist.");
					continue;
				}
				Class<? extends Item> currentclass = getClass(info[0].strip().split(",")[0].strip());
				ResourceLocation repairLoc = new ResourceLocation(info[0].strip().split(",")[1].strip());
				Item repair = ForgeRegistries.ITEMS.getValue(repairLoc);
				if (repair == null) continue;
				List<Aptitude> aptitudeList = new ArrayList<>();
				for (String aptitude : info[1].split(",")) {
					String[] text = aptitude.split(":");
					String cleaned = text[0].strip(); //aptitude name
					String level = text[1].strip(); //aptitude level
					if (!validAptitudes.contains(cleaned)) {
						Talents.getLOGGER().info("Aptitude specified does not exist, data: " + data);
						continue;
					}
					aptitudeList.add(new LockItem.Aptitude(cleaned, Integer.parseInt(level)));
				}
				LockItem.Aptitude[] aptitudes = aptitudeList.toArray(new LockItem.Aptitude[0]);
				if (aptitudeList.isEmpty()) {
					continue;
				}
				aptitudeList.clear();
				ItemStack repairStack = new ItemStack(repair);
				for (Item item : ForgeRegistries.ITEMS) {
					if (currentclass.isAssignableFrom(item.getClass()) ) {
						if (item instanceof TieredItem tiered && tiered.getTier() != null && 
								tiered.getTier().getRepairIngredient() != null && tiered.getTier().getRepairIngredient().getItems() != null) {
							if (tiered.getTier().getRepairIngredient().test(repairStack)) {
								ResourceLocation id = ForgeRegistries.ITEMS.getKey(item);
						        String idString = id.toString();
						        if (existingItems.contains(idString) || addedtags.contains(idString)) continue;
						        mutable.add(new LockItem(idString, aptitudes));
							}
						}
						if (item instanceof ArmorItem tiered && tiered.getMaterial() != null && 
								tiered.getMaterial().getRepairIngredient() != null && tiered.getMaterial().getRepairIngredient().getItems() != null) {
							if (tiered.getMaterial().getRepairIngredient().test(repairStack)) {
								ResourceLocation id = ForgeRegistries.ITEMS.getKey(item);
						        String idString = id.toString();
						        if (existingItems.contains(idString) || addedtags.contains(idString)) continue;
						        mutable.add(new LockItem(idString, aptitudes));
							}
						}
						
					}
				}
			}
		}
		HandlerLockItemsConfig.HANDLER.instance().lockItemList = mutable;
		HandlerAptitude.invalidate();   // the cached locks were built without the tag and material locks
	}
	
	
	private static Class<? extends Item> getClass(String string) {
		Class<? extends Item> result = SwordItem.class;
		switch (string) {
	    case "swordItem":
	        result = SwordItem.class;
	        break;
	    case "axeItem":
	    	result = AxeItem.class;
	        break;
	    case "pickaxeItem":
	    	result = PickaxeItem.class;
	        break;
	    case "shovelItem":
	    	result = ShovelItem.class;
	        break;
	    case "hoeItem":
	    	result = HoeItem.class;
	        break;
	    case "armorItem":
	    	result = ArmorItem.class;
	    	break;
	    default:
	        result = SwordItem.class;
	        break;
		}
		return result;
	}
}
