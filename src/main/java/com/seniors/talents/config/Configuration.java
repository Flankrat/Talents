package com.seniors.talents.config;

import com.seniors.talents.handler.*;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Configuration {

    private static Path _absoluteDirectory = FMLPaths.CONFIGDIR.get().resolve("Talents");

    public static Path getAbsoluteDirectory() {
        return _absoluteDirectory;
    }

    public static void Init() {
        Path clientConfigPath = Paths.get("Talents").resolve("talents-client.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, HandlerConfigClient.SPEC, clientConfigPath.toString());

        HandlerConvergenceItemsConfig.HANDLER.load();

        ManageOldConfig();
    }

    private static void ManageOldConfig() {
        HandlerCommonConfig.HANDLER.load();
        TalentsItemTagsLock.HANDLER.load();
        HandlerLockItemsConfig.HANDLER.load();
    }
}

