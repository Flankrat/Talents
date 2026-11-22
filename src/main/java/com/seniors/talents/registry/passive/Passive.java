package com.seniors.talents.registry.passive;

import com.seniors.talents.client.core.Utils;
import com.seniors.talents.common.capability.AptitudeCapability;
import com.seniors.talents.handler.HandlerConfigClient;
import com.seniors.talents.handler.HandlerResources;
import com.seniors.talents.registry.RegistryAptitudes;
import com.seniors.talents.registry.aptitude.Aptitude;
import com.seniors.talents.Talents;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeConfigSpec;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Passive {
    public final ResourceLocation key;
    public final Aptitude aptitude;
    public final ResourceLocation texture;
    public final Attribute attribute;
    public final String attributeUuid;
    public final Object attributeValue;
    public final int[] levelsRequired;
    public final AttributeModifier.Operation operation;
    public final int[] xpcosts;
	private final double ratio;

    public Passive(ResourceLocation passiveKey, Aptitude aptitude, ResourceLocation passiveTexture, Attribute attribute, String attributeUuid, Object attributeValue, int[] levelsRequired, int[] xpcosts) {
        this.key = passiveKey;
        this.aptitude = aptitude;
        this.texture = passiveTexture;
        this.attribute = attribute;
        this.attributeUuid = attributeUuid;
        this.attributeValue = attributeValue;
        this.levelsRequired = levelsRequired;
        this.operation = AttributeModifier.Operation.ADDITION;
        this.xpcosts = xpcosts;
        this.ratio = 1f/((double)levelsRequired.length);
    }
    
    public Passive(ResourceLocation passiveKey, Aptitude aptitude, ResourceLocation passiveTexture, Attribute attribute, String attributeUuid, Object attributeValue, AttributeModifier.Operation operation, int[] levelsRequired, int[] xpcosts) {
        this.key = passiveKey;
        this.aptitude = aptitude;
        this.texture = passiveTexture;
        this.attribute = attribute;
        this.attributeUuid = attributeUuid;
        this.attributeValue = attributeValue;
        this.levelsRequired = levelsRequired;
        this.operation = operation;
        this.xpcosts = xpcosts;
        this.ratio = 1f/((double)levelsRequired.length);
    }

    // KubeJS support
    public static Passive add(String passiveName, String aptitudeName, String texture, Attribute attribute, String attributeUUID, Object attributeValue, int[] levelsRequired, int[] xpcosts){
        Aptitude aptitude = RegistryAptitudes.getAptitude(aptitudeName);
        if (aptitude == null){
            throw new IllegalArgumentException("Aptitude name doesn't exist: " + aptitudeName);
        }

        ResourceLocation key = new ResourceLocation(Talents.MOD_ID, passiveName);
        return new Passive(key, aptitude, HandlerResources.create(texture), attribute, attributeUUID, attributeValue, levelsRequired, xpcosts);
    }

    public Passive get() {
        return this;
    }

    public String getMod() {
        return this.key.getNamespace();
    }

    public String getName() {
        return this.key.getPath();
    }

    public String getKey() {
        return "passive." + this.key.toLanguageKey();
    }

    public String getDescription() {
        return getKey() + ".description";
    }

    public double getValue() {
        double newValue = 0.0D;
        if (this.attributeValue != null) {
            Object object = this.attributeValue;
            if (object instanceof ForgeConfigSpec.DoubleValue) {
                ForgeConfigSpec.DoubleValue value = (ForgeConfigSpec.DoubleValue) object;
                newValue = value.get();
            }
            if (object instanceof ForgeConfigSpec.IntValue) {
                ForgeConfigSpec.IntValue value = (ForgeConfigSpec.IntValue) object;
                newValue = value.get();
            }
            if (object instanceof Number) {
                Number value = (Number) object;
                newValue = value.doubleValue();
            }

        }
        return newValue;
    }

    public int getNextLevelUp() {
        int[] requirement = new int[this.levelsRequired.length + 2];
        requirement[0] = 0;
        System.arraycopy(this.levelsRequired, 0, requirement, 1, this.levelsRequired.length);
        return requirement[getLevel() + 1];
    }

    public List<Component> tooltip() {
    	DecimalFormat df = new DecimalFormat("0.##");
        String valuePerLevel = df.format(getValue() / this.levelsRequired.length);
        String valueActualLevel = df.format(getValue()/ this.levelsRequired.length * getLevel());
        String valueMaxLevel = df.format(getValue());
        boolean notmaxed = this.getLevel() < this.levelsRequired.length;
        List<Component> list = new ArrayList<>();
        list.add(Component.translatable("tooltip.passive.title").append(Component.translatable(getKey())).withStyle(ChatFormatting.GREEN));
        list.add(Component.translatable("tooltip.passive.description.passive_level", this.getLevel(), this.levelsRequired.length).withStyle(ChatFormatting.GRAY));
        if (notmaxed) {
        	list.add(Component.translatable("passive.talents.next_cost", getLevelCost(this.getLevel())).withStyle(ChatFormatting.YELLOW));
        }
        list.add(Component.empty());
        if (Screen.hasShiftDown()) {
            list.add(Component.empty()
                    .append(Component.translatable(getKey()).withStyle(ChatFormatting.GOLD).withStyle(ChatFormatting.UNDERLINE))
                    .append(Component.literal(": ").withStyle(ChatFormatting.GRAY))
                    .append(Component.translatable(getDescription()).withStyle(ChatFormatting.GRAY)));
            list.add(Component.empty());
            list.add(Component.translatable("tooltip.passive.description.other_info").withStyle(ChatFormatting.GRAY));
            list.add(Component.literal(" ").append(Component.translatable("tooltip.passive.description.level", valuePerLevel)).withStyle(ChatFormatting.DARK_GREEN));
            list.add(Component.literal(" ").append(Component.translatable("tooltip.passive.description.actual_level", valueActualLevel)).withStyle(ChatFormatting.DARK_GREEN));
            list.add(Component.literal(" ").append(Component.translatable("tooltip.passive.description.max_level", valueMaxLevel)).withStyle(ChatFormatting.DARK_GREEN));
            list.add(Component.empty());
            list.add(Component.translatable("tooltip.passive.description.level_requirement").withStyle(ChatFormatting.DARK_PURPLE));
            if (notmaxed) {
                list.add(Component.literal(" ").append(Component.translatable("tooltip.passive.description.passive_required", Component.translatable(this.aptitude.getKey()).withStyle(ChatFormatting.GREEN),
                        Component.literal(String.valueOf(getNextLevelUp())).withStyle(ChatFormatting.GREEN),
                        Component.literal(String.valueOf(getLevel() + 1)).withStyle(ChatFormatting.GREEN))).withStyle(ChatFormatting.DARK_AQUA));
            } else {
                list.add(Component.literal(" ").append(Component.translatable("tooltip.passive.description.passive_max_level")).withStyle(ChatFormatting.DARK_AQUA));
            }
        } else {
            list.add(Component.translatable("tooltip.general.description.more_information").withStyle(ChatFormatting.YELLOW));
        }
        if (HandlerConfigClient.showSkillModName.get()) {
            list.add(Component.literal(Utils.getModName(getMod())).withStyle(ChatFormatting.BLUE).withStyle(ChatFormatting.ITALIC));
        }

        return list;
    }

    public int getLevel() {
        return AptitudeCapability.get().getPassiveLevel(this);
    }

    public int getLevel(Player player) {
        return AptitudeCapability.get(player).getPassiveLevel(this);
    }

    public int getMaxLevel() {
        return this.levelsRequired.length;
    }

    public ResourceLocation getTexture() {
        return Objects.requireNonNullElse(this.texture, HandlerResources.NULL_SKILL);
    }
    
    public int getLevelCost(int level) {
		return this.xpcosts[level];
	}
	
	public double getLevelRatio() {
		return this.ratio;
	}

	public int getXPCost(int level) {
		return getTotalXpForLevel(this.xpcosts[level]);
	}

    private static int getTotalXpForLevel(int level) {
        if (level <= 16) {
            return level * level + 6 * level;
        } else if (level <= 31) {
            return (int) (2.5 * level * level - 40.5 * level + 360);
        } else {
            return (int) (4.5 * level * level - 162.5 * level + 2220);
        }
    }
    
}


