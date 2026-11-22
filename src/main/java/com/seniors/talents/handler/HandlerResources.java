package com.seniors.talents.handler;

import com.seniors.talents.Talents;

import net.minecraft.resources.ResourceLocation;

public class HandlerResources {
    public static final ResourceLocation[] SKILL_PAGE = new ResourceLocation[]{
            create("textures/gui/container/skill_page_1.png"),
            create("textures/gui/container/skill_page_2.png"),
            create("textures/gui/container/skill_page_3.png")
    };

    public static final ResourceLocation[] ATTACK_LOCKED_ICON = new ResourceLocation[]{
            create("textures/skill/attack/locked_0.png"),
            create("textures/skill/attack/locked_8.png"),
            create("textures/skill/attack/locked_16.png"),
            create("textures/skill/attack/locked_24.png")
    };

    public static final ResourceLocation[] GATHERING_LOCKED_ICON = new ResourceLocation[]{
            create("textures/skill/gathering/locked_0.png"),
            create("textures/skill/gathering/locked_8.png"),
            create("textures/skill/gathering/locked_16.png"),
            create("textures/skill/gathering/locked_24.png")
    };

    public static final ResourceLocation[] AGILITY_LOCKED_ICON = new ResourceLocation[]{
            create("textures/skill/agility/locked_0.png"),
            create("textures/skill/agility/locked_8.png"),
            create("textures/skill/agility/locked_16.png"),
            create("textures/skill/agility/locked_24.png")
    };

    public static final ResourceLocation[] DEFENSE_LOCKED_ICON = new ResourceLocation[]{
            create("textures/skill/defense/locked_0.png"),
            create("textures/skill/defense/locked_8.png"),
            create("textures/skill/defense/locked_16.png"),
            create("textures/skill/defense/locked_24.png")
    };

    public static final ResourceLocation[] MINING_LOCKED_ICON = new ResourceLocation[]{
            create("textures/skill/mining/locked_0.png"),
            create("textures/skill/mining/locked_8.png"),
            create("textures/skill/mining/locked_16.png"),
            create("textures/skill/mining/locked_24.png")
    };

    public static final ResourceLocation[] BUILDING_LOCKED_ICON = new ResourceLocation[]{
            create("textures/skill/building/locked_0.png"),
            create("textures/skill/building/locked_8.png"),
            create("textures/skill/building/locked_16.png"),
            create("textures/skill/building/locked_24.png")
    };

    public static final ResourceLocation[] MAGIC_LOCKED_ICON = new ResourceLocation[]{
            create("textures/skill/magic/locked_0.png"),
            create("textures/skill/magic/locked_8.png"),
            create("textures/skill/magic/locked_16.png"),
            create("textures/skill/magic/locked_24.png")
    };

    public static final ResourceLocation[] FARMING_LOCKED_ICON = new ResourceLocation[]{
            create("textures/skill/farming/locked_0.png"),
            create("textures/skill/farming/locked_8.png"),
            create("textures/skill/farming/locked_16.png"),
            create("textures/skill/farming/locked_24.png")
    };

    public static final ResourceLocation SKILL_ICONS = create("textures/skill/icons.png");
    public static final ResourceLocation NULL_SKILL = create("textures/skill/null_skill.png");
    //Attack
    public static final ResourceLocation ONE_HANDED_SKILL = create("textures/skill/attack/one_handed.png");
    public static final ResourceLocation FIGHTING_SPIRIT_SKILL = create("textures/skill/attack/fighting_spirit.png");
    public static final ResourceLocation BERSERKER_SKILL = create("textures/skill/attack/berserker.png");
    public static final ResourceLocation ATTACK_DAMAGE_PASSIVE = create("textures/skill/attack/passive_attack_damage.png");
    public static final ResourceLocation CRITICAL_CHANCE_PASSIVE = create("textures/skill/attack/passive_critical_chance.png");
    //Gathering
    public static final ResourceLocation LUCKY_FISHERMAN = create("textures/skill/gathering/lucky_fisherman.png");
    public static final ResourceLocation DROP_GUARANTEED = create("textures/skill/gathering/drop_guaranteed.png");
    public static final ResourceLocation BOUNTY_HUNTER = create("textures/skill/gathering/bounty_hunter.png");
    public static final ResourceLocation FISHING_PASSIVE = create("textures/skill/gathering/fishing_proficiency.png");
    public static final ResourceLocation WOOD_CHOPPING_PASSIVE = create("textures/skill/gathering/wood_chopping_passive.png");
    //To add mercenary skill
    //Agility
    public static final ResourceLocation QUICK_REPOSITION_SKILL = create("textures/skill/agility/quick_reposition.png");
    public static final ResourceLocation LEDGE_GRAB = create("textures/skill/agility/ledge_grab.png");
    public static final ResourceLocation SIDESTEP = create("textures/skill/agility/sidestep.png");
    public static final ResourceLocation SPRINTER = create("textures/skill/agility/sprinter.png");
    public static final ResourceLocation MOVEMENT_SPEED_PASSIVE = create("textures/skill/agility/passive_movement_speed.png");
    public static final ResourceLocation PROJECTILE_SPEED_PASSIVE = create("textures/skill/agility/passive_projectile_speed.png");
    public static final ResourceLocation STEALTH_PASSIVE = create("textures/skill/agility/stealth.png");
    //Defense
    public static final ResourceLocation ATHLETICS_SKILL = create("textures/skill/defense/athletics.png");
    public static final ResourceLocation SNOW_WALKER_SKILL = create("textures/skill/defense/snow_walker.png");
    public static final ResourceLocation UNDERSHIRT_SKILL = create("textures/skill/defense/undershirt.png");
    public static final ResourceLocation SURVIVAL_INSTINCTS = create("textures/skill/defense/survival_instincts.png");
    public static final ResourceLocation MITIGATE = create("textures/skill/defense/mitigate.png");
    public static final ResourceLocation SHIELD_PROFICIENCY = create("textures/skill/defense/shield_proficiency.png");
    
    //Mining
    public static final ResourceLocation FOSSIL_DIGGER = create("textures/skill/mining/fossil_digger.png");
    public static final ResourceLocation OBSIDIAN_SMASHER_SKILL = create("textures/skill/mining/obsidian_smasher.png");
    public static final ResourceLocation TREASURE_HUNTER_SKILL = create("textures/skill/mining/treasure_hunter.png");
    public static final ResourceLocation BLOCK_REACH_PASSIVE = create("textures/skill/mining/passive_block_reach.png");
    public static final ResourceLocation MINING_SPEED = create("textures/skill/mining/passive_break_speed.png");
    //Building
    public static final ResourceLocation PERFECT_RECOVERY = create("textures/skill/building/perfect_recover.png");
    public static final ResourceLocation TRANSMUTATION = create("textures/skill/building/transmutation.png");
    public static final ResourceLocation CONVERGENCE_SKILL = create("textures/skill/building/convergence.png");
    public static final ResourceLocation MINERAL_EFFICIENCY_PASSIVE = create("textures/skill/building/mineral_efficiency.png");
    public static final ResourceLocation SMELTING_SPEED_PASSIVE = create("textures/skill/building/smelting_speed.png");
    //Magic
    public static final ResourceLocation SCHOLAR_SKILL = create("textures/skill/magic/scholar.png");
    public static final ResourceLocation SAFE_PORT_SKILL = create("textures/skill/magic/safe_port.png");
    public static final ResourceLocation GOLDEN_OSMOSIS = create("textures/skill/magic/golden_osmosis.png");
    public static final ResourceLocation TELEPORT_DISRUPTION = create("textures/skill/magic/teleport_disruption.png");
    public static final ResourceLocation MANA_REGEN_PASSIVE = create("textures/skill/magic/mana_regen.png");
    public static final ResourceLocation LUCK_PASSIVE = create("textures/skill/magic/passive_luck.png");
    
    //Farming
    public static final ResourceLocation HAGGLER_SKILL = create("textures/skill/farming/haggler.png");
    public static final ResourceLocation BOUNTIFUL_HARVEST = create("textures/skill/farming/bountiful_harvest.png");
    public static final ResourceLocation HUNGRY_FARMER = create("textures/skill/farming/hungry_farmer.png");
    public static final ResourceLocation COOKING_SPEED = create("textures/skill/farming/cooking_speed.png");
    public static final ResourceLocation GREEN_THUMB = create("textures/skill/farming/green_thumb.png");
    
    public static ResourceLocation create(String path) {
        return new ResourceLocation(Talents.MOD_ID, path);
    }
}


