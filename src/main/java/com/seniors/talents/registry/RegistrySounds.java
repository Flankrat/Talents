package com.seniors.talents.registry;

import com.seniors.talents.Talents;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RegistrySounds {
    public static final DeferredRegister<SoundEvent> REGISTER = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Talents.MOD_ID);
    public static RegistryObject<SoundEvent> GAIN_TITLE = REGISTER.register("gain_title", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Talents.MOD_ID, "gain_title")));
    public static RegistryObject<SoundEvent> LEDGE_GRAB = REGISTER.register("ledge_grab", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Talents.MOD_ID, "ledge_grab")));
    
    public static void load(IEventBus eventBus) {
        REGISTER.register(eventBus);
    }
}


