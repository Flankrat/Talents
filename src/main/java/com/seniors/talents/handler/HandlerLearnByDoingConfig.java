package com.seniors.talents.handler;

import com.google.gson.GsonBuilder;
import com.seniors.talents.Talents;
import com.seniors.talents.config.Configuration;

import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Rates for learn-by-doing aptitude XP (only used when learnByDoing is on in talents.common.json5).
 * Every amount is in vanilla XP points: an aptitude needs as many points to level up as buying that level
 * with XP costs, so "1.0" of something that drops 5 XP is worth the same as picking up those 5 XP.
 * Server side only, so it isn't synced to clients.
 */
public class HandlerLearnByDoingConfig {
    public static ConfigClassHandler<HandlerLearnByDoingConfig> HANDLER = ConfigClassHandler.createBuilder(HandlerLearnByDoingConfig.class)
            .id(new ResourceLocation(Talents.MOD_ID, "learn_by_doing"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(Configuration.getAbsoluteDirectory().resolve("talents.learn-by-doing.json5"))
                    .appendGsonBuilder(GsonBuilder::setPrettyPrinting)
                    .setJson5(true)
                    .build())
            .build();

    @SerialEntry(comment = "Multiplies every amount below. 2.0 = aptitudes level twice as fast.")
    public double scale = 1.0;

    // kills: the XP the mob actually drops (after other mods change it, e.g. Simple Difficulty blights x40)
    @SerialEntry(comment = "Melee kill: share of the XP the mob drops that goes to attack.")
    public double meleeKillAttack = 1.0;
    @SerialEntry(comment = "Melee kill: share of the dropped XP that goes to defense.")
    public double meleeKillDefense = 0.5;
    @SerialEntry(comment = "Ranged kill (arrow, bolt, thrown weapon): share of the dropped XP that goes to agility.")
    public double rangedKillAgility = 1.0;
    @SerialEntry(comment = "Ranged kill: share of the dropped XP that goes to attack.")
    public double rangedKillAttack = 0.5;
    @SerialEntry(comment = "Ranged kill: share of the dropped XP that goes to defense.")
    public double rangedKillDefense = 0.25;
    @SerialEntry(comment = "Extra XP counted for killing an Infernal Mobs elite. Infernal Mobs drops its own 25 XP outside the normal XP drop, so it isn't in the mob's dropped XP.")
    public double infernalBonusXp = 25.0;

    // damage
    @SerialEntry(comment = "Defense XP per point of damage (half a heart) taken from a mob or player, after armor.")
    public double damageTakenDefense = 0.25;
    @SerialEntry(comment = "Defense XP per point of damage blocked with a shield.")
    public double shieldBlockDefense = 0.25;
    @SerialEntry(comment = "Magic XP per point of magic damage dealt (potions, spells: damage types in minecraft:witch_resistant_to).")
    public double magicDamageMagic = 0.25;

    // blocks
    @SerialEntry(comment = "Share of the XP a broken block drops that goes to mining (ores, spawners) or farming (crops).")
    public double blockXp = 1.0;
    @SerialEntry(comment = "Mining XP per stone or ore block broken (forge:stone, forge:ores, forge:netherrack, base_stone_overworld/nether).")
    public double stoneMining = 0.1;
    @SerialEntry(comment = "Gathering XP per log broken (minecraft:logs).")
    public double logGathering = 0.2;
    @SerialEntry(comment = "Farming XP per fully grown crop harvested.")
    public double cropFarming = 0.3;
    @SerialEntry(comment = "Building XP per block placed.")
    public double placeBuilding = 0.25;

    // fishing, breeding, smelting, brewing, levels spent
    @SerialEntry(comment = "Share of a catch's vanilla XP (1 to 6) that goes to gathering.")
    public double fishingGathering = 1.0;
    @SerialEntry(comment = "Share of breeding's vanilla XP (1 to 7) that goes to farming.")
    public double breedingFarming = 1.0;
    @SerialEntry(comment = "Share of a furnace recipe's XP given when you take the output: food -> farming, ingots/gems/nuggets -> mining, anything else -> building.")
    public double smelting = 1.0;
    @SerialEntry(comment = "Share of the XP points spent on enchanting and anvils that goes to magic.")
    public double levelsSpentMagic = 0.5;
    @SerialEntry(comment = "Magic XP per potion taken out of a brewing stand.")
    public double brewingMagic = 0.5;

    // crafting and workstations
    @SerialEntry(comment = "Building XP per craft in a crafting table or the inventory grid (shift-click counts each craft).")
    public double craftBuilding = 0.25;
    @SerialEntry(comment = "Building XP for each use of a workstation (taking its output), as \"menu_type_id=xp\". Menus not listed give nothing.")
    public List<String> stations = Arrays.asList(
            "minecraft:anvil=1.0", "minecraft:smithing=2.0", "minecraft:grindstone=0.5", "minecraft:stonecutter=0.25",
            "minecraft:loom=0.5", "minecraft:cartography_table=0.5",
            "lycanitesmobs:equipment_forge=2.0", "lycanitesmobs:equipment_station=1.0", "lycanitesmobs:equipment_infuser=1.0",
            "lycanitesmobs:summoning_pedestal=1.0", "quality_forked:reforging_station=1.0",
            "forgottenitems:rune_reader=1.0", "forgottenitems:advanced_rune_reader=2.0");

    // movement
    @SerialEntry(comment = "Agility XP per block sprinted on the ground.")
    public double sprintAgility = 0.01;
    @SerialEntry(comment = "Agility XP per jump.")
    public double jumpAgility = 0.02;

    // parsed copy of the stations list, rebuilt when the list object changes (config reload)
    private transient Map<String, Double> parsedStations;
    private transient List<String> parsedFrom;

    /** Building XP for one use of the station with this menu type id, or 0 if it isn't listed. */
    public double stationXp(String menuTypeId) {
        if (parsedFrom != stations) {
            Map<String, Double> map = new HashMap<>();
            for (String entry : stations) {
                int eq = entry.lastIndexOf('=');
                if (eq <= 0) continue;
                try {
                    map.put(entry.substring(0, eq).trim(), Double.parseDouble(entry.substring(eq + 1).trim()));
                } catch (NumberFormatException e) {
                    Talents.getLOGGER().warn("Bad learn-by-doing station entry \"{}\", skipping it", entry);
                }
            }
            parsedStations = map;
            parsedFrom = stations;
        }
        return parsedStations.getOrDefault(menuTypeId, 0.0);
    }
}
