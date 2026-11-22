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

public class PerfectRecoveryConditionLootTable implements LootItemCondition {

	public static final PerfectRecoveryConditionLootTable INSTANCE = new PerfectRecoveryConditionLootTable();
	public static final LootItemConditionType LOOT_CONDITION_TYPE = new LootItemConditionType(new PerfectRecoveryConditionLootTable.Serializer());

    private PerfectRecoveryConditionLootTable() {}

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
	public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<PerfectRecoveryConditionLootTable> {
        @Override
        public void serialize(JsonObject json, PerfectRecoveryConditionLootTable condition, JsonSerializationContext context) {}

        @Override
        public PerfectRecoveryConditionLootTable deserialize(JsonObject json, JsonDeserializationContext context) {
            return INSTANCE;
        }
    }
}
