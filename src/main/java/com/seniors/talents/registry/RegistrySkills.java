package com.seniors.talents.registry;

import com.seniors.talents.client.core.Value;
import com.seniors.talents.client.core.ValueType;
import com.seniors.talents.handler.HandlerCommonConfig;
import com.seniors.talents.handler.HandlerResources;
import com.seniors.talents.registry.aptitude.Aptitude;
import com.seniors.talents.registry.skills.Skill;
import com.seniors.talents.registry.skills.TransmutationSkill;
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


public class RegistrySkills {
    @SuppressWarnings("removal")
	public static final ResourceKey<Registry<Skill>> SKILLS_KEY = ResourceKey.createRegistryKey(new ResourceLocation(Talents.MOD_ID, "skills"));
    public static final DeferredRegister<Skill> SKILLS = DeferredRegister.create(SKILLS_KEY, Talents.MOD_ID);
    public static final Supplier<IForgeRegistry<Skill>> SKILLS_REGISTRY = SKILLS.makeRegistry(() -> new RegistryBuilder<Skill>().disableSaving());

    public static final RegistryObject<Skill> ONE_HANDED = HandlerCommonConfig.HANDLER.instance().oneHandedRequiredLevel < 0 ? null : SKILLS.register("one_handed", () -> register("one_handed", RegistryAptitudes.ATTACK.get(), HandlerCommonConfig.HANDLER.instance().oneHandedRequiredLevel, HandlerResources.ONE_HANDED_SKILL, new Value(ValueType.AMPLIFIER, HandlerCommonConfig.HANDLER.instance().oneHandedAmplifier)));

    public static final RegistryObject<Skill> FIGHTING_SPIRIT = HandlerCommonConfig.HANDLER.instance().fightingSpiritRequiredLevel < 0 ? null : SKILLS.register("fighting_spirit", () -> register("fighting_spirit", RegistryAptitudes.ATTACK.get(), HandlerCommonConfig.HANDLER.instance().fightingSpiritRequiredLevel, HandlerResources.FIGHTING_SPIRIT_SKILL, new Value(ValueType.BOOST, HandlerCommonConfig.HANDLER.instance().fightingSpiritBoost), new Value(ValueType.DURATION, HandlerCommonConfig.HANDLER.instance().fightingSpiritDuration)));

    public static final RegistryObject<Skill> BERSERKER = HandlerCommonConfig.HANDLER.instance().berserkerRequiredLevel < 0 ? null : SKILLS.register("berserker", () -> register("berserker", RegistryAptitudes.ATTACK.get(), HandlerCommonConfig.HANDLER.instance().berserkerRequiredLevel, HandlerResources.BERSERKER_SKILL, new Value(ValueType.PERCENT, HandlerCommonConfig.HANDLER.instance().berserkerPercent)));

    public static final RegistryObject<Skill> QUICK_REPOSITION = HandlerCommonConfig.HANDLER.instance().quickRepositionRequiredLevel < 0 ? null : SKILLS.register("quick_reposition", () -> register("quick_reposition", RegistryAptitudes.AGILITY.get(), HandlerCommonConfig.HANDLER.instance().quickRepositionRequiredLevel, HandlerResources.QUICK_REPOSITION_SKILL, new Value(ValueType.BOOST, HandlerCommonConfig.HANDLER.instance().quickRepositionBoost), new Value(ValueType.DURATION, HandlerCommonConfig.HANDLER.instance().quickRepositionDuration)));

    public static final RegistryObject<Skill> UNDERSHIRT = HandlerCommonConfig.HANDLER.instance().undershirtRequiredLevel < 0 ? null : SKILLS.register("undershirt", () -> register("undershirt", RegistryAptitudes.DEFENSE.get(), HandlerCommonConfig.HANDLER.instance().undershirtRequiredLevel, HandlerResources.UNDERSHIRT_SKILL));

    public static final RegistryObject<Skill> SURVIVAL_INSTINCTS = SKILLS.register("survival_instincts", () -> register("survival_instincts", RegistryAptitudes.DEFENSE.get(), HandlerCommonConfig.HANDLER.instance().survivalinstinctsLevel, HandlerResources.SURVIVAL_INSTINCTS, new Value(ValueType.PERCENT, HandlerCommonConfig.HANDLER.instance().survivalinstinctschance * 100)));
    
    public static final RegistryObject<Skill> ATHLETICS = HandlerCommonConfig.HANDLER.instance().athleticsRequiredLevel < 0 ? null : SKILLS.register("athletics", () -> register("athletics", RegistryAptitudes.DEFENSE.get(), HandlerCommonConfig.HANDLER.instance().athleticsRequiredLevel, HandlerResources.ATHLETICS_SKILL, new Value(ValueType.MODIFIER, HandlerCommonConfig.HANDLER.instance().athleticsModifier)));
    
    public static final RegistryObject<Skill> SNOW_WALKER = HandlerCommonConfig.HANDLER.instance().snowWalkerRequiredLevel < 0 ? null : SKILLS.register("snow_walker", () -> register("snow_walker", RegistryAptitudes.DEFENSE.get(), HandlerCommonConfig.HANDLER.instance().snowWalkerRequiredLevel, HandlerResources.SNOW_WALKER_SKILL));

    public static final RegistryObject<Skill> SCHOLAR = HandlerCommonConfig.HANDLER.instance().scholarRequiredLevel < 0 ? null : SKILLS.register("scholar", () -> register("scholar", RegistryAptitudes.MAGIC.get(), HandlerCommonConfig.HANDLER.instance().scholarRequiredLevel, HandlerResources.SCHOLAR_SKILL));

    public static final RegistryObject<Skill> HAGGLER = HandlerCommonConfig.HANDLER.instance().hagglerRequiredLevel < 0 ? null : SKILLS.register("haggler", () -> register("haggler", RegistryAptitudes.FARMING.get(), HandlerCommonConfig.HANDLER.instance().hagglerRequiredLevel, HandlerResources.HAGGLER_SKILL, new Value(ValueType.PERCENT, HandlerCommonConfig.HANDLER.instance().hagglerPercent)));

    public static final RegistryObject<Skill> OBSIDIAN_SMASHER = HandlerCommonConfig.HANDLER.instance().obsidianSmasherRequiredLevel < 0 ? null : SKILLS.register("obsidian_smasher", () -> register("obsidian_smasher", RegistryAptitudes.MINING.get(), HandlerCommonConfig.HANDLER.instance().obsidianSmasherRequiredLevel, HandlerResources.OBSIDIAN_SMASHER_SKILL, new Value(ValueType.MODIFIER, HandlerCommonConfig.HANDLER.instance().obsidianSmasherModifier)));

    public static final RegistryObject<Skill> TREASURE_HUNTER = HandlerCommonConfig.HANDLER.instance().treasureHunterRequiredLevel < 0 ? null : SKILLS.register("treasure_hunter", () -> register("treasure_hunter", RegistryAptitudes.MINING.get(), HandlerCommonConfig.HANDLER.instance().treasureHunterRequiredLevel, HandlerResources.TREASURE_HUNTER_SKILL, new Value(ValueType.PROBABILITY, HandlerCommonConfig.HANDLER.instance().treasureHunterProbability)));

    public static final RegistryObject<Skill> CONVERGENCE = HandlerCommonConfig.HANDLER.instance().convergenceRequiredLevel < 0 ? null : SKILLS.register("convergence", () -> register("convergence", RegistryAptitudes.BUILDING.get(), HandlerCommonConfig.HANDLER.instance().convergenceRequiredLevel, HandlerResources.CONVERGENCE_SKILL, new Value(ValueType.PROBABILITY, HandlerCommonConfig.HANDLER.instance().convergenceProbability)));

    public static final RegistryObject<Skill> SAFE_PORT = HandlerCommonConfig.HANDLER.instance().safePortRequiredLevel < 0 ? null : SKILLS.register("safe_port", () -> register("safe_port", RegistryAptitudes.MAGIC.get(), HandlerCommonConfig.HANDLER.instance().safePortRequiredLevel, HandlerResources.SAFE_PORT_SKILL));

    public static final RegistryObject<Skill> LUCKY_FISHERMAN = SKILLS.register("lucky_fisherman", () -> register("lucky_fisherman", RegistryAptitudes.GATHERING.get(), HandlerCommonConfig.HANDLER.instance().luckyfishermanRequiredLevel, HandlerResources.LUCKY_FISHERMAN));
    
	public static final RegistryObject<Skill> LEDGE_GRAB = SKILLS.register("ledge_grab",
			() -> register("ledge_grab", RegistryAptitudes.AGILITY.get(),
					HandlerCommonConfig.HANDLER.instance().ledgeGrabRequiredLevel, HandlerResources.LEDGE_GRAB,
					new Value(ValueType.DURATION, 60)));
	public static final RegistryObject<Skill> SPRINTER = SKILLS.register("sprinter",
			() -> register("sprinter", RegistryAptitudes.AGILITY.get(),
					HandlerCommonConfig.HANDLER.instance().sprinterRequiredLevel, HandlerResources.SPRINTER,
					new Value(ValueType.DURATION, 60)));
	public static final RegistryObject<Skill> SIDESTEP = SKILLS.register("sidestep",
			() -> register("sidestep", RegistryAptitudes.AGILITY.get(),
					HandlerCommonConfig.HANDLER.instance().sidestepRequiredLevel, HandlerResources.SIDESTEP,
					new Value(ValueType.DURATION, 60)));
	public static final RegistryObject<Skill> BOUNTIFUL_HARVEST = SKILLS.register("bountiful_harvest",
			() -> register("bountiful_harvest", RegistryAptitudes.FARMING.get(),
					HandlerCommonConfig.HANDLER.instance().bountifulharvestRequiredLevel,
					HandlerResources.BOUNTIFUL_HARVEST,
					new Value(ValueType.PERCENT, HandlerCommonConfig.HANDLER.instance().bountifulharvestchance * 100)));
	public static final RegistryObject<Skill> HUNGRY_FARMER = SKILLS.register("hungry_farmer",
			() -> register("hungry_farmer", RegistryAptitudes.FARMING.get(),
					HandlerCommonConfig.HANDLER.instance().hungryfarmerRequiredLevel, HandlerResources.HUNGRY_FARMER));
	public static final RegistryObject<Skill> DROP_GUARANTEED = SKILLS.register("drop_guaranteed",
			() -> register("drop_guaranteed", RegistryAptitudes.GATHERING.get(),
					HandlerCommonConfig.HANDLER.instance().dropGuaranteedRequiredLevel,
					HandlerResources.DROP_GUARANTEED));
	public static final RegistryObject<Skill> BOUNTY_HUNTER = SKILLS.register("bounty_hunter",
			() -> register("bounty_hunter", RegistryAptitudes.GATHERING.get(),
					HandlerCommonConfig.HANDLER.instance().bountyHunterRequiredLevel,
					HandlerResources.BOUNTY_HUNTER));
	public static final RegistryObject<Skill> FOSSIL_DIGGER = SKILLS.register("fossil_digger",
			() -> register("fossil_digger", RegistryAptitudes.MINING.get(),
					HandlerCommonConfig.HANDLER.instance().fossilDiggerRequiredLevel, HandlerResources.FOSSIL_DIGGER,
					new Value(ValueType.PERCENT, HandlerCommonConfig.HANDLER.instance().fossilDiggerchance * 100)));
	public static final RegistryObject<Skill> PERFECT_RECOVERY = SKILLS.register("perfect_recovery",
			() -> register("perfect_recovery", RegistryAptitudes.BUILDING.get(),
					HandlerCommonConfig.HANDLER.instance().perfectRecoveryRequiredLevel,
					HandlerResources.PERFECT_RECOVERY));
	@SuppressWarnings("removal")
	public static final RegistryObject<Skill> TRANSMUTATION = SKILLS.register("transmutation",
			() -> new TransmutationSkill(new ResourceLocation(Talents.MOD_ID, "transmutation"),
					RegistryAptitudes.BUILDING.get(), HandlerCommonConfig.HANDLER.instance().transmutationRequiredLevel,
					HandlerResources.TRANSMUTATION));
	public static final RegistryObject<Skill> GOLDEN_OSMOSIS = SKILLS.register("golden_osmosis",
			() -> register("golden_osmosis", RegistryAptitudes.MAGIC.get(),
					HandlerCommonConfig.HANDLER.instance().goldenosmosisRequiredLevel, HandlerResources.GOLDEN_OSMOSIS));
	public static final RegistryObject<Skill> TELEPORT_DISRUPTION = SKILLS.register("teleport_disruption",
			() -> register("teleport_disruption", RegistryAptitudes.MAGIC.get(),
					HandlerCommonConfig.HANDLER.instance().teleportDisruptionRequiredLevel,
					HandlerResources.TELEPORT_DISRUPTION));
	
	
	private static Skill register(String name, Aptitude aptitude, int requiredLvl, ResourceLocation texture, Value... configValues) {
        @SuppressWarnings("removal")
		ResourceLocation key = new ResourceLocation(Talents.MOD_ID, name);
        return new Skill(key, aptitude, requiredLvl, texture, configValues);
    }

    public static void load(IEventBus eventBus) {
        SKILLS.register(eventBus);
    }

    public static Skill getSkill(String skillName) {
        return SKILLS_REGISTRY.get().getValues().stream().collect(Collectors.toMap(Skill::getName, Skill::get)).get(skillName);
    }
}


