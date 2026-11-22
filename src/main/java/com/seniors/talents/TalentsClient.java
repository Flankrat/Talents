package com.seniors.talents;

import com.mojang.blaze3d.platform.InputConstants;
import com.seniors.talents.client.gui.OverlayAptitudeGui;
import com.seniors.talents.client.gui.TabJustLeveling;
import com.seniors.talents.client.screen.TalentsScreen;
import com.seniors.talents.handler.HandlerCommonConfig;
import com.seniors.talents.integration.L2TabsIntegration;
import com.seniors.talents.registry.RegistryClientEvents;
import com.seniors.talents.registry.RegistryItems;
import dev.xkmc.l2tabs.tabs.core.TabRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class TalentsClient {
    public static Minecraft client = Minecraft.getInstance();
    public static KeyMapping OPEN_JUSTLEVELING_SCREEN = new KeyMapping("key.talents.open_aptitudes", InputConstants.Type.KEYSYM, 89, "key.talents.title");
    public static final KeyMapping DODGE_KEY =
            new KeyMapping("key.talents.dodge", InputConstants.KEY_CAPSLOCK, "key.talents.title");

    @EventBusSubscriber(modid = Talents.MOD_ID, value = {Dist.CLIENT})
    public static class ClientForgeEvents {
        @SubscribeEvent
        public static void checkKeyboard(InputEvent.Key event) {
            if (TalentsClient.client.player != null && TalentsClient.client.level != null &&
                    TalentsClient.OPEN_JUSTLEVELING_SCREEN.consumeClick())
                TalentsClient.client.setScreen(new TalentsScreen());
        }
    }

    @EventBusSubscriber(modid = Talents.MOD_ID, value = {Dist.CLIENT}, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientProxy {
        @SubscribeEvent
        public static void clientSetup(FMLClientSetupEvent event) {
            ModLoadingContext.get().registerExtensionPoint(
                    ConfigScreenHandler.ConfigScreenFactory.class,
                    () -> new ConfigScreenHandler.ConfigScreenFactory(
                            (client, parent) ->
                                    HandlerCommonConfig.HANDLER.generateGui().generateScreen(parent)
                    )
            );

            MinecraftForge.EVENT_BUS.register(new RegistryClientEvents());
            MinecraftForge.EVENT_BUS.register(new OverlayAptitudeGui());

            if (L2TabsIntegration.isModLoaded()) {
                event.enqueueWork(() -> {
                    TabRegistry.registerTab(3500, TabJustLeveling::new, RegistryItems.LEVELING_BOOK, Component.literal("Aptitudes"));
                });
            }
        }

        @SubscribeEvent
        public static void registerKeys(RegisterKeyMappingsEvent event) {
            event.register(TalentsClient.OPEN_JUSTLEVELING_SCREEN);
            event.register(DODGE_KEY);
        }
    }
}
