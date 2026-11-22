package com.seniors.talents.registry;

import com.seniors.talents.handler.HandlerCommonConfig;
import com.seniors.talents.handler.HandlerResources;
import com.seniors.talents.registry.aptitude.Aptitude;
import com.seniors.talents.registry.passive.Passive;
import com.seniors.talents.registry.passive.PercentualPassive;
import com.seniors.talents.Talents;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class RegistryPassives {
    public static final ResourceKey<Registry<Passive>> PASSIVES_KEY = ResourceKey.createRegistryKey(new ResourceLocation(Talents.MOD_ID, "passives"));
    public static final DeferredRegister<Passive> PASSIVES = DeferredRegister.create(PASSIVES_KEY, Talents.MOD_ID);
    public static final Supplier<IForgeRegistry<Passive>> PASSIVES_REGISTRY = PASSIVES.makeRegistry(() -> new RegistryBuilder<Passive>().disableSaving());

	public static final RegistryObject<Passive> ATTACK_DAMAGE = PASSIVES.register("attack_damage",
			() -> percentualregister("attack_damage", RegistryAptitudes.ATTACK.get(),
					HandlerResources.ATTACK_DAMAGE_PASSIVE,
					Attributes.ATTACK_DAMAGE, "96a891fe-5919-418d-8205-f50464391500",
					HandlerCommonConfig.HANDLER.instance().attackDamageValue,
					AttributeModifier.Operation.MULTIPLY_TOTAL,
					HandlerCommonConfig.HANDLER.instance().attackPassiveLevels,
					HandlerCommonConfig.HANDLER.instance().attackdamagecosts));

	public static final RegistryObject<Passive> CRITICAL_CHANCE = PASSIVES.register("critical_chance",
			() -> percentualregister("critical_chance", RegistryAptitudes.ATTACK.get(), HandlerResources.CRITICAL_CHANCE_PASSIVE,
					RegistryAttributes.CRITICAL_CHANCE.get(), "96a891fe-5919-418d-8205-f50464391515",
					HandlerCommonConfig.HANDLER.instance().criticalChanceValue,
					HandlerCommonConfig.HANDLER.instance().criticalChancePassiveLevels,
					HandlerCommonConfig.HANDLER.instance().criticalChancecosts));

	public static final RegistryObject<Passive> DAMAGE_REDUCTION = PASSIVES.register("damage_reduction",
			() -> percentualregister("damage_reduction", RegistryAptitudes.DEFENSE.get(), HandlerResources.MITIGATE,
					RegistryAttributes.DAMAGE_REDUCTION.get(), "a77bb275-1077-4a2f-b737-5ef43791b88c",
					HandlerCommonConfig.HANDLER.instance().damagereduction,
					HandlerCommonConfig.HANDLER.instance().damagereductionlevels,
					HandlerCommonConfig.HANDLER.instance().damagereductioncosts));
	public static final RegistryObject<Passive> SHIELD_PROFICIENCY = PASSIVES.register("shield_proficiency",
			() -> percentualregister("shield_proficiency", RegistryAptitudes.DEFENSE.get(),
					HandlerResources.SHIELD_PROFICIENCY, null, "a77bb275-1077-4a2f-b737-5ef43791b88c",
					HandlerCommonConfig.HANDLER.instance().shieldproficiency,
					HandlerCommonConfig.HANDLER.instance().shieldproficiencylevels,
					HandlerCommonConfig.HANDLER.instance().shieldproficiencycosts));
	
	public static final RegistryObject<Passive> STEALTH = PASSIVES.register("stealth",
			() -> percentualregister("stealth", RegistryAptitudes.AGILITY.get(),
					HandlerResources.STEALTH_PASSIVE, RegistryAttributes.STEALTH.get(),
					"3ec1f8f5-da37-4c6a-9f3c-ce62b1f8d609", HandlerCommonConfig.HANDLER.instance().stealthvalue,
					HandlerCommonConfig.HANDLER.instance().stealthLevels,
					HandlerCommonConfig.HANDLER.instance().stealthcosts));
	
	public static final RegistryObject<Passive> MOVEMENT_SPEED = PASSIVES.register("movement_speed",
			() -> register("movement_speed", RegistryAptitudes.AGILITY.get(),
					HandlerResources.MOVEMENT_SPEED_PASSIVE,
					Attributes.MOVEMENT_SPEED, "96a891fe-5919-418d-8205-f50464391504",
					HandlerCommonConfig.HANDLER.instance().movementSpeedValue,
					AttributeModifier.Operation.ADDITION,
					HandlerCommonConfig.HANDLER.instance().movementSpeedPassiveLevels,
					HandlerCommonConfig.HANDLER.instance().movementspeedcosts));
	
	public static final RegistryObject<Passive> PROJECTILE_SPEED = PASSIVES.register("projectile_speed",
			() -> percentualregister("projectile_speed", RegistryAptitudes.AGILITY.get(),
					HandlerResources.PROJECTILE_SPEED_PASSIVE, RegistryAttributes.PROJECTILE_SPEED.get(),
					"3ec1f8f5-da37-4c6a-9f3c-ce62b1f8d609", HandlerCommonConfig.HANDLER.instance().projectileSpeedValue,
					HandlerCommonConfig.HANDLER.instance().projectileSpeedPassiveLevels,
					HandlerCommonConfig.HANDLER.instance().projectilespeedcosts));

	public static final RegistryObject<Passive> BLOCK_REACH = PASSIVES.register("block_reach",
			() -> register("block_reach", RegistryAptitudes.MINING.get(),
					HandlerResources.BLOCK_REACH_PASSIVE,
					ForgeMod.BLOCK_REACH.get(), "96a891fe-5919-418d-8205-f50464391510",
					HandlerCommonConfig.HANDLER.instance().blockReachValue,
					HandlerCommonConfig.HANDLER.instance().blockReachPassiveLevels,
					HandlerCommonConfig.HANDLER.instance().blockreachcosts));

	public static final RegistryObject<Passive> BREAK_SPEED = PASSIVES.register("break_speed",
			() -> percentualregister("break_speed", RegistryAptitudes.MINING.get(),
					HandlerResources.MINING_SPEED,
					RegistryAttributes.BREAK_SPEED.get(), "96a891fe-5919-418d-8205-f50464391511",
					HandlerCommonConfig.HANDLER.instance().breakSpeedValue,
					HandlerCommonConfig.HANDLER.instance().breakSpeedPassiveLevels,
					HandlerCommonConfig.HANDLER.instance().breakSpeedcosts));


	public static final RegistryObject<Passive> LUCK = PASSIVES.register("luck",
			() -> register("luck", RegistryAptitudes.MAGIC.get(),
					HandlerResources.LUCK_PASSIVE, Attributes.LUCK,
					"96a891fe-5919-418d-8205-f50464391514", HandlerCommonConfig.HANDLER.instance().luckValue,
					HandlerCommonConfig.HANDLER.instance().luckPassiveLevels,
					HandlerCommonConfig.HANDLER.instance().luckpassivecosts));
	public static final RegistryObject<Passive> MANA_REGEN = PASSIVES.register("mana_regen",
			() -> register("mana_regen", RegistryAptitudes.MAGIC.get(),
					HandlerResources.MANA_REGEN_PASSIVE, null,
					"96a891fe-5919-418d-8205-f50464391514", HandlerCommonConfig.HANDLER.instance().manaRegenvalue,
					HandlerCommonConfig.HANDLER.instance().manaRegenLevels,
					HandlerCommonConfig.HANDLER.instance().manaRegencosts));
	
	public static final RegistryObject<Passive> COOKING_SPEED = PASSIVES.register("cooking_speed",
			() -> percentualregister("cooking_speed", RegistryAptitudes.FARMING.get(), HandlerResources.COOKING_SPEED, null,
					"e114c837-20b6-47eb-8efe-0a37a9f275cc", HandlerCommonConfig.HANDLER.instance().cookingspeedvalue,
					HandlerCommonConfig.HANDLER.instance().cookingspeedLevels,
					HandlerCommonConfig.HANDLER.instance().cookingspeedcosts));
	public static final RegistryObject<Passive> SMELTING_SPEED = PASSIVES.register("smelting_speed",
			() -> percentualregister("smelting_speed", RegistryAptitudes.BUILDING.get(), HandlerResources.SMELTING_SPEED_PASSIVE,
					null, "e114c837-20b6-47eb-8efe-0a37a9f255cc",
					HandlerCommonConfig.HANDLER.instance().smeltingspeedvalue,
					HandlerCommonConfig.HANDLER.instance().smeltingspeedLevels,
					HandlerCommonConfig.HANDLER.instance().smeltingspeedcosts));
	public static final RegistryObject<Passive> WOOD_CHOPPING = PASSIVES.register("wood_chopping",
			() -> percentualregister("wood_chopping", RegistryAptitudes.GATHERING.get(), HandlerResources.WOOD_CHOPPING_PASSIVE,
					null, "e114c837-20b6-47eb-8efe-0a37a9f255cc",
					HandlerCommonConfig.HANDLER.instance().woodChoppingValue,
					HandlerCommonConfig.HANDLER.instance().woodChoppingPassiveLevels,
					HandlerCommonConfig.HANDLER.instance().woodChoppingcosts));
	public static final RegistryObject<Passive> FISHING_PROFICIENCY = PASSIVES.register("fishing_proficiency",
			() -> percentualregister("fishing_proficiency", RegistryAptitudes.GATHERING.get(),
					HandlerResources.FISHING_PASSIVE, null, "e114c837-20b6-47eb-8efe-0a37a9f255cc",
					HandlerCommonConfig.HANDLER.instance().fishingproficiencyvalue,
					HandlerCommonConfig.HANDLER.instance().fishingproficiencyLevels,
					HandlerCommonConfig.HANDLER.instance().fishingproficiencycosts));
	public static final RegistryObject<Passive> MINERAL_EFFICIENCY = PASSIVES.register("mineral_efficiency",
			() -> percentualregister("mineral_efficiency", RegistryAptitudes.BUILDING.get(),
					HandlerResources.MINERAL_EFFICIENCY_PASSIVE, null, "e114c837-20b6-47eb-8efe-0a37a9f255cc",
					HandlerCommonConfig.HANDLER.instance().mineralefficiencyvalue,
					HandlerCommonConfig.HANDLER.instance().mineralefficiencyLevels,
					HandlerCommonConfig.HANDLER.instance().mineralefficiencycosts));
	public static final RegistryObject<Passive> GREEN_THUMB = PASSIVES.register("green_thumb",
			() -> register("green_thumb", RegistryAptitudes.FARMING.get(), HandlerResources.GREEN_THUMB, null,
					"e114c837-20b6-47eb-8efe-0a37a9f255cc", HandlerCommonConfig.HANDLER.instance().greenthumbrange,
					HandlerCommonConfig.HANDLER.instance().greenthumbLevels,
					HandlerCommonConfig.HANDLER.instance().greenthumbcosts));
	
	private static Passive register(String name, Aptitude aptitude, ResourceLocation texture, Attribute attribute,
			String attributeUuid, Object attributeValue, int[] levelsRequired, int[] xpcosts) {
		ResourceLocation key = new ResourceLocation(Talents.MOD_ID, name);
		return new Passive(key, aptitude, texture, attribute, attributeUuid, attributeValue, levelsRequired, xpcosts);
	}
	
	private static Passive register(String name, Aptitude aptitude, ResourceLocation texture, Attribute attribute,
			String attributeUuid, Object attributeValue, AttributeModifier.Operation operation, int[] levelsRequired, int[] xpcosts) {
		ResourceLocation key = new ResourceLocation(Talents.MOD_ID, name);
		return new Passive(key, aptitude, texture, attribute, attributeUuid, attributeValue, operation,levelsRequired, xpcosts);
	}
	
	private static Passive percentualregister(String name, Aptitude aptitude, ResourceLocation texture, Attribute attribute, String attributeUuid, Object attributeValue, int[] levelsRequired, int... costs) {
        ResourceLocation key = new ResourceLocation(Talents.MOD_ID, name);
        return new PercentualPassive(key, aptitude, texture, attribute, attributeUuid, attributeValue, levelsRequired, costs);
    }
	
	private static Passive percentualregister(String name, Aptitude aptitude, ResourceLocation texture, Attribute attribute, String attributeUuid, Object attributeValue, AttributeModifier.Operation operation, int[] levelsRequired, int... costs) {
        ResourceLocation key = new ResourceLocation(Talents.MOD_ID, name);
        return new PercentualPassive(key, aptitude, texture, attribute, attributeUuid, attributeValue, operation, levelsRequired, costs);
    }

    public static void load(IEventBus eventBus) {
        PASSIVES.register(eventBus);
    }

    public static Passive getPassive(String passiveName) {
        return PASSIVES_REGISTRY.get().getValues().stream().collect(Collectors.toMap(Passive::getName, Passive::get)).get(passiveName);
    }
}


