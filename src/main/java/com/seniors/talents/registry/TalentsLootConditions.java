package com.seniors.talents.registry;

import com.seniors.talents.Talents;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class TalentsLootConditions {

	public static final DeferredRegister<LootItemConditionType> LOOT_CONDITIONS =
	        DeferredRegister.create(Registries.LOOT_CONDITION_TYPE, Talents.MOD_ID);

	    public static final RegistryObject<LootItemConditionType> PERFECT_RECOVERY_FLAG =
	        LOOT_CONDITIONS.register("perfect_recovery",
	            () -> new LootItemConditionType(new PerfectRecoveryCondition.Serializer()));
	
}
