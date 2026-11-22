package com.seniors.talents.registry;

import com.seniors.talents.registry.passive.Passive;
import com.seniors.talents.Talents;

import java.util.UUID;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RegistryAttributes {
    private static final DeferredRegister<Attribute> REGISTER = DeferredRegister.create(ForgeRegistries.Keys.ATTRIBUTES, Talents.MOD_ID);

    public static final RegistryObject<Attribute> BREAK_SPEED = REGISTER.register("break_speed", () -> (new RangedAttribute("break_speed", 0.0D, 0.0D, 1024.0D)).setSyncable(true));
    public static final RegistryObject<Attribute> CRITICAL_CHANCE = REGISTER.register("critical_chance", () -> (new RangedAttribute("critical_chance", 0.0D, 0.0D, 1024.0D)).setSyncable(true));
    public static final RegistryObject<Attribute> PROJECTILE_SPEED = REGISTER.register("projectile_speed", () -> (new RangedAttribute("projectile_speed", 0.0D, 0.0D, 1024.0D)).setSyncable(true));
    public static final RegistryObject<Attribute> MAGIC_RESIST = REGISTER.register("magic_resist", () -> (new RangedAttribute("magic_resist", 0.0D, 0.0D, 1024.0D)).setSyncable(true));
    public static final RegistryObject<Attribute> DAMAGE_REDUCTION = REGISTER.register("damage_reduction",
			() -> new RangedAttribute("damage_reduction", 0.0D, 0.0D, 1.0D)
					.setSyncable(true));
	public static final RegistryObject<Attribute> STEALTH = REGISTER.register("stealth",
			() -> new RangedAttribute("stealth", 0.0D, 0.0D, 1.0D)
					.setSyncable(true));
	public static final RegistryObject<Attribute> WOOD_SPEED = REGISTER.register("wood_speed", () -> (new RangedAttribute("wood_speed", 0.0D, 0.0D, 1024.0D)).setSyncable(true));
    
    public static void load(IEventBus eventBus) {
        REGISTER.register(eventBus);
    }

    public static void modifierAttributes(ServerPlayer serverPlayer) {
        serverPlayer.getCapability(RegistryCapabilities.APTITUDE).ifPresent(aptitudeCapability -> {
            for (int i = 0; i < RegistryPassives.PASSIVES_REGISTRY.get().getValues().stream().toList().size(); i++) {
                Passive passive = RegistryPassives.PASSIVES_REGISTRY.get().getValues().stream().toList().get(i);
                if (passive.attribute == null) continue;
                updateModifier(serverPlayer, passive.attribute, passive.getValue() * passive.getLevelRatio() * passive.getLevel(serverPlayer), UUID.fromString(passive.attributeUuid), passive.operation);
            }
        });
    }
    
    public static void updateModifier(Player player, Attribute attribute, double modifier, UUID uuid, AttributeModifier.Operation operation) {
    	AttributeInstance instance = player.getAttribute(attribute);
        if (instance == null)
            return;
        AttributeModifier oldModifier = instance.getModifier(uuid);
        if (oldModifier == null) {
        	AttributeModifier newModifier = new AttributeModifier(uuid, Talents.MOD_ID, modifier, operation);
            instance.addPermanentModifier(newModifier);
        } else {
        	if (oldModifier.getAmount() != modifier) {
        		instance.removeModifier(oldModifier);
            	AttributeModifier newModifier = new AttributeModifier(uuid, Talents.MOD_ID, modifier, operation);
                instance.addPermanentModifier(newModifier);
        	}
        }
    }
}


