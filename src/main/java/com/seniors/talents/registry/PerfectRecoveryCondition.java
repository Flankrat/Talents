package com.seniors.talents.registry;

import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class PerfectRecoveryCondition implements LootItemCondition {

	public static final PerfectRecoveryCondition INSTANCE = new PerfectRecoveryCondition();
	public static final LootItemConditionType LOOT_CONDITION_TYPE = new LootItemConditionType(new PerfectRecoveryCondition.Serializer());

    private PerfectRecoveryCondition() {}

	@Override
	public LootItemConditionType getType() {
		return TalentsLootConditions.PERFECT_RECOVERY_FLAG.get();
	}
	
	public Set<LootContextParam<?>> getReferencedContextParams() {
        return ImmutableSet.of(LootContextParams.THIS_ENTITY);
    }

	@Override
	public boolean test(LootContext context) {
		Entity entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
		if (entity instanceof Player player) {
			return RegistrySkills.PERFECT_RECOVERY.get().isEnabled(player);
		}
		return false;
	}
	
	 // -----------------------
    // Serializer implementation
    // -----------------------
	public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<PerfectRecoveryCondition> {
        @Override
        public void serialize(JsonObject json, PerfectRecoveryCondition condition, JsonSerializationContext context) {}

        @Override
        public PerfectRecoveryCondition deserialize(JsonObject json, JsonDeserializationContext context) {
            return INSTANCE;
        }
    }
}
