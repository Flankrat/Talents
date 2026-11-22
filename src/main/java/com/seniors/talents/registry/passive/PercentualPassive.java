package com.seniors.talents.registry.passive;

import com.seniors.talents.client.core.Utils;
import com.seniors.talents.handler.HandlerConfigClient;
import com.seniors.talents.registry.aptitude.Aptitude;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PercentualPassive extends Passive {
    
    public PercentualPassive(ResourceLocation passiveKey, Aptitude aptitude, ResourceLocation passiveTexture,
			Attribute attribute, String attributeUuid, Object attributeValue, int[] levelsRequired, int[] xpcosts) {
		super(passiveKey, aptitude, passiveTexture, attribute, attributeUuid, attributeValue, levelsRequired, xpcosts);
	}
    
    public PercentualPassive(ResourceLocation passiveKey, Aptitude aptitude, ResourceLocation passiveTexture, Attribute attribute, String attributeUuid, Object attributeValue, AttributeModifier.Operation operation, int[] levelsRequired, int[] xpcosts) {
    	super(passiveKey, aptitude, passiveTexture, attribute, attributeUuid, attributeValue, operation, levelsRequired, xpcosts);
    }

	@Override
    public List<Component> tooltip() {
		DecimalFormat df = new DecimalFormat("0.##");
        String valuePerLevel = df.format(getValue() * 100 / this.levelsRequired.length) + "%";
        String valueActualLevel = df.format(getValue() * 100/ this.levelsRequired.length * getLevel()) + "%";
        String valueMaxLevel = df.format(getValue() * 100) + "%";

        List<Component> list = new ArrayList<>();
        boolean notmaxed = this.getLevel() < this.levelsRequired.length;
        list.add(Component.translatable("tooltip.passive.title").append(Component.translatable(getKey())).withStyle(ChatFormatting.GREEN));
        list.add(Component.translatable("tooltip.passive.description.passive_level", getLevel(), this.levelsRequired.length).withStyle(ChatFormatting.GRAY));
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
    
}


