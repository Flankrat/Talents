package com.seniors.talents.handler;

import com.seniors.talents.config.Configuration;
import com.seniors.talents.config.models.LockItem;
import com.seniors.talents.Talents;
import com.seniors.talents.config.LockItemListGroup;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.autogen.ListGroup;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class HandlerLockItemsConfig {

    public static ConfigClassHandler<HandlerLockItemsConfig> HANDLER = ConfigClassHandler.createBuilder(HandlerLockItemsConfig.class)
            .id(new ResourceLocation(Talents.MOD_ID, "config"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(Configuration.getAbsoluteDirectory().resolve("talents.lockItems.json5"))
                    .setJson5(true)
                    .build())
            .build();

    @SerialEntry(comment = "Lock item list")
    @ListGroup(controllerFactory = LockItemListGroup.class, valueFactory = LockItemListGroup.class, addEntriesToBottom = true)
    public List<LockItem> lockItemList = List.of(
            new LockItem("minecraft:anvil", new LockItem.Aptitude("building", 12)),
            new LockItem("minecraft:chipped_anvil", new LockItem.Aptitude("building", 12)),
            new LockItem("minecraft:damaged_anvil", new LockItem.Aptitude("building", 12)),
            new LockItem("minecraft:brewing_stand", new LockItem.Aptitude("building", 8), new LockItem.Aptitude("magic", 8)),
            new LockItem("minecraft:enchanting_table", new LockItem.Aptitude("magic", 12)),
            new LockItem("minecraft:beacon", new LockItem.Aptitude("building", 24), new LockItem.Aptitude("magic", 16)),
            new LockItem("minecraft:end_crystal", new LockItem.Aptitude("magic", 32), new LockItem.Aptitude("building", 24)),
            new LockItem("minecraft:ender_chest", new LockItem.Aptitude("magic", 24)),
            new LockItem("minecraft:respawn_anchor", new LockItem.Aptitude("magic", 20)),
            new LockItem("minecraft:shulker_box", new LockItem.Aptitude("magic", 32)),
            new LockItem("minecraft:white_shulker_box", new LockItem.Aptitude("magic", 32)),
            new LockItem("minecraft:light_gray_shulker_box", new LockItem.Aptitude("magic", 32)),
            new LockItem("minecraft:gray_shulker_box", new LockItem.Aptitude("magic", 32)),
            new LockItem("minecraft:black_shulker_box", new LockItem.Aptitude("magic", 32)),
            new LockItem("minecraft:brown_shulker_box", new LockItem.Aptitude("magic", 32)),
            new LockItem("minecraft:red_shulker_box", new LockItem.Aptitude("magic", 32)),
            new LockItem("minecraft:orange_shulker_box", new LockItem.Aptitude("magic", 32)),
            new LockItem("minecraft:yellow_shulker_box", new LockItem.Aptitude("magic", 32)),
            new LockItem("minecraft:lime_shulker_box", new LockItem.Aptitude("magic", 32)),
            new LockItem("minecraft:green_shulker_box", new LockItem.Aptitude("magic", 32)),
            new LockItem("minecraft:cyan_shulker_box", new LockItem.Aptitude("magic", 32)),
            new LockItem("minecraft:light_blue_shulker_box", new LockItem.Aptitude("magic", 32)),
            new LockItem("minecraft:blue_shulker_box", new LockItem.Aptitude("magic", 32)),
            new LockItem("minecraft:purple_shulker_box", new LockItem.Aptitude("magic", 32)),
            new LockItem("minecraft:magenta_shulker_box", new LockItem.Aptitude("magic", 32)),
            new LockItem("minecraft:pink_shulker_box", new LockItem.Aptitude("magic", 32)),
            new LockItem("minecraft:dragon_egg", new LockItem.Aptitude("magic", 32)),
            new LockItem("minecraft:wither_skeleton_skull", new LockItem.Aptitude("building", 8), new LockItem.Aptitude("magic", 8)),
            new LockItem("minecraft:lodestone", new LockItem.Aptitude("building", 16), new LockItem.Aptitude("mining", 16)),
            new LockItem("minecraft:smithing_table", new LockItem.Aptitude("building", 20), new LockItem.Aptitude("mining", 16)),
            new LockItem("minecraft:grindstone", new LockItem.Aptitude("building", 16), new LockItem.Aptitude("mining", 16)),
            new LockItem("minecraft:cartography_table", new LockItem.Aptitude("building", 8), new LockItem.Aptitude("gathering", 8)),
            new LockItem("minecraft:stonecutter", new LockItem.Aptitude("building", 6), new LockItem.Aptitude("attack", 4)),
            new LockItem("minecraft:smoker", new LockItem.Aptitude("farming", 8)),
            new LockItem("minecraft:blast_furnace", new LockItem.Aptitude("mining", 8)),
            new LockItem("minecraft:loom", new LockItem.Aptitude("building", 8), new LockItem.Aptitude("gathering", 8)),
            new LockItem("minecraft:name_tag", new LockItem.Aptitude("magic", 10)),
            new LockItem("minecraft:fishing_rod", new LockItem.Aptitude("farming", 4)),
            new LockItem("minecraft:bone_meal", new LockItem.Aptitude("farming", 14)),
            new LockItem("minecraft:shears", new LockItem.Aptitude("building", 4)),
            new LockItem("minecraft:lead", new LockItem.Aptitude("gathering", 4)),
            new LockItem("minecraft:spyglass", new LockItem.Aptitude("agility", 4)),
            new LockItem("minecraft:brush", new LockItem.Aptitude("building", 12), new LockItem.Aptitude("mining", 8)),
            new LockItem("minecraft:fire_charge", new LockItem.Aptitude("mining", 4)),
            new LockItem("minecraft:flint_and_steel", new LockItem.Aptitude("mining", 4)),
            new LockItem("minecraft:redstone", new LockItem.Aptitude("building", 4)),
            new LockItem("minecraft:redstone_torch", new LockItem.Aptitude("building", 4)),
            new LockItem("minecraft:repeater", new LockItem.Aptitude("building", 4)),
            new LockItem("minecraft:comparator", new LockItem.Aptitude("building", 4)),
            new LockItem("minecraft:tnt", new LockItem.Aptitude("mining", 12)),
            new LockItem("minecraft:lectern", new LockItem.Aptitude("magic", 8), new LockItem.Aptitude("building", 4)),
            new LockItem("minecraft:ender_pearl", new LockItem.Aptitude("magic", 8)),
            new LockItem("minecraft:ender_eye", new LockItem.Aptitude("magic", 16)),
            new LockItem("minecraft:bow", new LockItem.Aptitude("agility", 2)),
            new LockItem("minecraft:crossbow", new LockItem.Aptitude("agility", 2), new LockItem.Aptitude("attack", 4)),
            new LockItem("minecraft:saddle", new LockItem.Aptitude("agility", 6)),
            new LockItem("minecraft:elytra", new LockItem.Aptitude("magic", 32)),
            new LockItem("minecraft:firework_rocket", new LockItem.Aptitude("agility", 10), new LockItem.Aptitude("mining", 20)),
            new LockItem("minecraft:wheat_seeds", new LockItem.Aptitude("farming", 2)),
            new LockItem("minecraft:cocoa_beans", new LockItem.Aptitude("farming", 2)),
            new LockItem("minecraft:pumpkin_seeds", new LockItem.Aptitude("farming", 2)),
            new LockItem("minecraft:melon_seeds", new LockItem.Aptitude("farming", 2)),
            new LockItem("minecraft:beetroot_seeds", new LockItem.Aptitude("farming", 2)),
            new LockItem("minecraft:torchflower_seeds", new LockItem.Aptitude("farming", 2)),
            new LockItem("minecraft:pitcher_pod", new LockItem.Aptitude("gathering", 6)),
            new LockItem("minecraft:glow_berries", new LockItem.Aptitude("gathering", 8)),
            new LockItem("minecraft:sweet_berries", new LockItem.Aptitude("farming", 4)),
            new LockItem("minecraft:nether_wart", new LockItem.Aptitude("magic", 16)),
            new LockItem("minecraft:egg", new LockItem.Aptitude("farming", 4)),
            new LockItem("minecraft:frogspawn", new LockItem.Aptitude("gathering", 16)),
            new LockItem("minecraft:turtle_egg", new LockItem.Aptitude("gathering", 16)),
            new LockItem("minecraft:sniffer_egg", new LockItem.Aptitude("farming", 16)),
            new LockItem("minecraft:azalea", new LockItem.Aptitude("gathering", 4)),
            new LockItem("minecraft:flowering_azalea", new LockItem.Aptitude("gathering", 4)),
            new LockItem("minecraft:brown_mushroom", new LockItem.Aptitude("gathering", 4)),
            new LockItem("minecraft:red_mushroom", new LockItem.Aptitude("gathering", 4)),
            new LockItem("minecraft:crimson_fungus", new LockItem.Aptitude("magic", 8)),
            new LockItem("minecraft:warped_fungus", new LockItem.Aptitude("magic", 8)),
            new LockItem("minecraft:bamboo", new LockItem.Aptitude("gathering", 8)),
            new LockItem("minecraft:sugar_cane", new LockItem.Aptitude("gathering", 8)),
            new LockItem("minecraft:cactus", new LockItem.Aptitude("gathering", 8)),
            new LockItem("minecraft:chorus_plant", new LockItem.Aptitude("gathering", 12)),
            new LockItem("minecraft:chorus_flower", new LockItem.Aptitude("gathering", 12)),
            new LockItem("minecraft:shield", new LockItem.Aptitude("defense", 12)),
            new LockItem("minecraft:chainmail_helmet", new LockItem.Aptitude("defense", 4)),
            new LockItem("minecraft:chainmail_chestplate", new LockItem.Aptitude("defense", 4)),
            new LockItem("minecraft:chainmail_leggings", new LockItem.Aptitude("defense", 4)),
            new LockItem("minecraft:chainmail_boots", new LockItem.Aptitude("defense", 4)),
            new LockItem("minecraft:turtle_helmet", new LockItem.Aptitude("defense", 6), new LockItem.Aptitude("agility", 6)),
            new LockItem("minecraft:golden_horse_armor", new LockItem.Aptitude("defense", 4), new LockItem.Aptitude("agility", 4)),
            new LockItem("minecraft:iron_horse_armor", new LockItem.Aptitude("defense", 6), new LockItem.Aptitude("agility", 6)),
            new LockItem("minecraft:diamond_horse_armor", new LockItem.Aptitude("defense", 12), new LockItem.Aptitude("agility", 12)),
            new LockItem("minecraft:totem_of_undying", new LockItem.Aptitude("magic", 16)),
            new LockItem("minecraft:trident", new LockItem.Aptitude("attack", 20), new LockItem.Aptitude("agility", 18)),
            new LockItem("minecraft:honey_bottle", new LockItem.Aptitude("gathering", 4)),
            new LockItem("minecraft:splash_potion", new LockItem.Aptitude("magic", 8), new LockItem.Aptitude("agility", 2)),
            new LockItem("minecraft:lingering_potion", new LockItem.Aptitude("magic", 8))
    );
}
