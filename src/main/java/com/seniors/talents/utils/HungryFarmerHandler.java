package com.seniors.talents.utils;

import java.util.ArrayList;

import com.seniors.talents.handler.HandlerCommonConfig;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

public class HungryFarmerHandler {
	public static ArrayList<Item> blacklist = new ArrayList<Item>();
	
	public static void load() {
		for (String value : HandlerCommonConfig.HANDLER.instance().hungryfarmerBlackList) {
			ResourceLocation id = new ResourceLocation(value);
			Item item = ForgeRegistries.ITEMS.getValue(id);
			if (item != null) {
				blacklist.add(item);
			}
		}
	}
}
