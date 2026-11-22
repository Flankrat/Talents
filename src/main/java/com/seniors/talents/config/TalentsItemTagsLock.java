package com.seniors.talents.config;

import java.util.List;

import com.google.gson.GsonBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.autogen.ListGroup;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.loading.FMLPaths;

import com.seniors.talents.Talents;

public class TalentsItemTagsLock {

    @SuppressWarnings("removal")
	public static ConfigClassHandler<TalentsItemTagsLock> HANDLER = ConfigClassHandler.createBuilder(TalentsItemTagsLock.class)
    		.id(new ResourceLocation(Talents.MOD_ID, "config"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FMLPaths.CONFIGDIR.get().resolve("Talents").resolve("talents-extra.lockitems.json5"))
                    .appendGsonBuilder(GsonBuilder::setPrettyPrinting)
                    .setJson5(true)
                    .build())
            .build();

    @SerialEntry(comment = "Lock item tags list, this is overrided by the original config.")
    @ListGroup(controllerFactory = StringListGroup.class, valueFactory = StringListGroup.class)
    public List<String> itemtagsList = List.of(
            "minecraft:boats;building:2,mining:2",
            "minecraft:sapplings;gathering:8"
    );
    
    @SerialEntry(comment = "Lock items by materials list, this is overrided by the original config.")
    @ListGroup(controllerFactory = StringListGroup.class, valueFactory = StringListGroup.class)
    public List<String> itemMaterialList = List.of(
            "swordItem,minecraft:iron_ingot;attack:8",
            "pickaxeItem,minecraft:iron_ingot;mining:8",
            "axeItem,minecraft:iron_ingot;gathering:8",
            "hoeItem,minecraft:iron_ingot;farming:8",
            "shovelItem,minecraft:iron_ingot;building:8",
            "armorItem,minecraft:iron_ingot;defense:8",
            "swordItem,minecraft:diamond;attack:16",
            "pickaxeItem,minecraft:diamond;mining:16",
            "axeItem,minecraft:diamond;gathering:16",
            "hoeItem,minecraft:diamond;farming:16",
            "shovelItem,minecraft:diamond;building:16",
            "armorItem,minecraft:diamond;defense:16",
            "swordItem,minecraft:netherite_ingot;attack:20",
            "pickaxeItem,minecraft:netherite_ingot;mining:20",
            "axeItem,minecraft:netherite_ingot;gathering:20",
            "hoeItem,minecraft:netherite_ingot;farming:20",
            "shovelItem,minecraft:netherite_ingot;building:20",
            "armorItem,minecraft:netherite_ingot;defense:20"
    );
}
