package com.seniors.talents.utils;

import com.seniors.talents.handler.HandlerCommonConfig;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.ForgeRegistries;

public class ResourceLoader {

	public static MobEffect DodgeDenyEffect;
	public static int dodge_cd;
	
	public static void load() {
		@SuppressWarnings("removal")
		ResourceLocation dodgedenyloc = new ResourceLocation(HandlerCommonConfig.HANDLER.instance().dodgeDenyEffect);
		if (ForgeRegistries.MOB_EFFECTS.containsKey(dodgedenyloc)) {
			DodgeDenyEffect = ForgeRegistries.MOB_EFFECTS.getValue(dodgedenyloc);		
		}
		dodge_cd = HandlerCommonConfig.HANDLER.instance().sidestepCooldown;
	}
	
}
