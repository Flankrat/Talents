package com.seniors.talents;

import com.mojang.logging.LogUtils;
import com.seniors.talents.config.Configuration;
import com.seniors.talents.handler.HandlerCommonConfig;
import com.seniors.talents.handler.HandlerCurios;
import com.seniors.talents.handler.HandlerLearnByDoing;
import com.seniors.talents.integration.FirstAidEventsCompat;
import com.seniors.talents.integration.WizardryLearnByDoing;
import com.seniors.talents.network.ServerNetworking;
import com.seniors.talents.registry.*;
import com.seniors.talents.registry.skills.TransmutationSkill;
import com.seniors.talents.utils.GreenThumbHandler;
import com.seniors.talents.utils.ModCompatsList;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.registries.RegisterEvent;

import org.apache.commons.lang3.tuple.MutablePair;
import org.slf4j.Logger;

@Mod(Talents.MOD_ID)
public class Talents {
    public static final String MOD_ID = "talents";
    public static final String MOD_NAME = "talents";

    private static final Logger LOGGER = LogUtils.getLogger();

    public static Logger getLOGGER() {
        return LOGGER;
    }

    public static MutablePair<Boolean, String> UpdatesAvailable = new MutablePair<>(false, "");

    // Required for the titles prefix
    public static MinecraftServer server;

	@SuppressWarnings("removal")
	public Talents() {
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		eventBus.addListener(this::attributeSetup);
		eventBus.addListener(this::commonSetup);
		eventBus.addListener(this::registerLootData);
		Configuration.Init();

		RegistryItems.load(eventBus);
		RegistryAptitudes.load(eventBus);
		RegistryPassives.load(eventBus);
		RegistrySkills.load(eventBus);
		RegistryAttributes.load(eventBus);
		RegistrySounds.load(eventBus);
		RegistryArguments.load(eventBus);

		TalentsLootConditions.LOOT_CONDITIONS.register(eventBus);
		MinecraftForge.EVENT_BUS.register(new RegistryCommonEvents());
		MinecraftForge.EVENT_BUS.register(HandlerLearnByDoing.class);   // learn by doing (checks its config option itself)
		if (FMLLoader.getLoadingModList().getModFileById("ebwizardry") != null) {
			WizardryLearnByDoing.register();   // wand spells -> magic XP (only loaded with Electroblob's Wizardry)
		}
		if (HandlerCurios.isModLoaded())
			MinecraftForge.EVENT_BUS.register(new HandlerCurios());
		if (FMLLoader.getLoadingModList().getModFileById("firstaid") != null) {
			MinecraftForge.EVENT_BUS.register(FirstAidEventsCompat.class);
		}
		
		// if (IronsSpellsbooksIntegration.isModLoaded())
		// MinecraftForge.EVENT_BUS.register(new IronsSpellsbooksIntegration());

		ServerNetworking.init();

	}
	
    private void commonSetup(final FMLCommonSetupEvent event) {
    	event.enqueueWork(() -> {
            TransmutationSkill.loadTransmutations(HandlerCommonConfig.HANDLER.instance().transmutationItemList);
            TransmutationSkill.setTransmutator(HandlerCommonConfig.HANDLER.instance().transmutationItem);
            GreenThumbHandler.chance = HandlerCommonConfig.HANDLER.instance().greenthumbchance / 100f;
            ModCompatsList.Load();
        });
    }
    
    @SuppressWarnings("removal")
	public void registerLootData(RegisterEvent event)
    {
        if (!event.getRegistryKey().equals(Registries.LOOT_CONDITION_TYPE))
            return;

        event.register(Registries.LOOT_CONDITION_TYPE, new ResourceLocation(MOD_ID ,"perfect_recovery"), () -> PerfectRecoveryCondition.LOOT_CONDITION_TYPE);
    }

	private void attributeSetup(EntityAttributeModificationEvent event) {
		event.add(EntityType.PLAYER, RegistryAttributes.CRITICAL_CHANCE.get());
		event.add(EntityType.PLAYER, RegistryAttributes.MAGIC_RESIST.get());
		event.add(EntityType.PLAYER, RegistryAttributes.BREAK_SPEED.get());
		event.add(EntityType.PLAYER, RegistryAttributes.PROJECTILE_SPEED.get());
		event.add(EntityType.PLAYER, RegistryAttributes.DAMAGE_REDUCTION.get());
		event.add(EntityType.PLAYER, RegistryAttributes.STEALTH.get());
		event.add(EntityType.PLAYER, RegistryAttributes.WOOD_SPEED.get());
	}

}
