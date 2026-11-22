package com.seniors.talents.utils;

import com.kettle.simpledifficultyreforged.network.BlightCapability;
import com.kettle.simpledifficultyreforged.network.IBlightCapability;

import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.fml.ModList;

public class ModCompatsList {

	public static boolean isFirstAidOn;
	public static boolean isTrinketsandBaublesOn;
	public static boolean isEVOn;
	public static boolean isSDon; //Simple Difficulty Reforged
	public static boolean isChampionsOn;
	public static boolean isInfernalOn;
	
	public static void Load() {
		isFirstAidOn = ModList.get().isLoaded("firstaid");
		isTrinketsandBaublesOn = ModList.get().isLoaded("xat");
		isEVOn = ModList.get().isLoaded("enhancedvisuals");
		isSDon = ModList.get().isLoaded("simple_difficulty");
		isChampionsOn = ModList.get().isLoaded("champions");
		isInfernalOn = ModList.get().isLoaded("infernalmobs");
	}
	
	
	public static boolean isBlight(LivingEntity entity) {
	    return entity.getCapability(BlightCapability.INSTANCE)
	            .map(IBlightCapability::isBlight)
	            .orElse(false);
	}
}
