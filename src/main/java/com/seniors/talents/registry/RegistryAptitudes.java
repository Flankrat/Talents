package com.seniors.talents.registry;

import com.seniors.talents.handler.HandlerResources;
import com.seniors.talents.registry.aptitude.Aptitude;
import com.seniors.talents.Talents;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;
import java.util.stream.Collectors;

public class RegistryAptitudes {

    private static final ResourceKey<Registry<Aptitude>> APTITUDES_KEY = ResourceKey.createRegistryKey(new ResourceLocation(Talents.MOD_ID, "aptitudes"));
    private static final DeferredRegister<Aptitude> APTITUDES = DeferredRegister.create(APTITUDES_KEY, Talents.MOD_ID);
    public static Supplier<IForgeRegistry<Aptitude>> APTITUDES_REGISTRY = APTITUDES.makeRegistry(() -> new RegistryBuilder<Aptitude>().disableSaving());

    public static final RegistryObject<Aptitude> ATTACK = APTITUDES.register("attack", () -> register(0, "attack", HandlerResources.ATTACK_LOCKED_ICON, new ResourceLocation("minecraft:textures/block/yellow_terracotta.png")));
    public static final RegistryObject<Aptitude> GATHERING = APTITUDES.register("gathering", () -> register(1, "gathering", HandlerResources.GATHERING_LOCKED_ICON, new ResourceLocation("minecraft:textures/block/red_terracotta.png")));
    public static final RegistryObject<Aptitude> AGILITY = APTITUDES.register("agility", () -> register(2, "agility", HandlerResources.AGILITY_LOCKED_ICON, new ResourceLocation("minecraft:textures/block/blue_terracotta.png")));
    public static final RegistryObject<Aptitude> DEFENSE = APTITUDES.register("defense", () -> register(3, "defense", HandlerResources.DEFENSE_LOCKED_ICON, new ResourceLocation("minecraft:textures/block/cyan_terracotta.png")));
    public static final RegistryObject<Aptitude> MINING = APTITUDES.register("mining", () -> register(4, "mining", HandlerResources.MINING_LOCKED_ICON, new ResourceLocation("minecraft:textures/block/orange_terracotta.png")));
    public static final RegistryObject<Aptitude> BUILDING = APTITUDES.register("building", () -> register(5, "building", HandlerResources.BUILDING_LOCKED_ICON, new ResourceLocation("minecraft:textures/block/brown_terracotta.png")));
    public static final RegistryObject<Aptitude> MAGIC = APTITUDES.register("magic", () -> register(6, "magic", HandlerResources.MAGIC_LOCKED_ICON, new ResourceLocation("minecraft:textures/block/purple_terracotta.png")));
    public static final RegistryObject<Aptitude> FARMING = APTITUDES.register("farming", () -> register(7, "farming", HandlerResources.FARMING_LOCKED_ICON, new ResourceLocation("minecraft:textures/block/lime_terracotta.png")));

    public static void load(IEventBus eventBus) {
        APTITUDES.register(eventBus);
    }

    private static Aptitude register(int index, String name, ResourceLocation[] lockedTexture, ResourceLocation background) {
        ResourceLocation key = new ResourceLocation(Talents.MOD_ID, name);
        return new Aptitude(index, key, lockedTexture, background);
    }

    public static Aptitude getAptitude(String aptitudeName) {
        return APTITUDES_REGISTRY.get().getValues().stream().collect(Collectors.toMap(Aptitude::getName, Aptitude::get)).get(aptitudeName.toLowerCase());
    }
}


