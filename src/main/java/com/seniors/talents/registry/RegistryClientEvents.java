package com.seniors.talents.registry;

import com.seniors.talents.client.core.Aptitudes;
import com.seniors.talents.common.capability.AptitudeCapability;
import com.seniors.talents.handler.HandlerAptitude;
import com.seniors.talents.registry.aptitude.Aptitude;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Objects;


/**
 * registry CLIENT events into the forge mod bus.
 */
@SuppressWarnings("removal")
public class RegistryClientEvents {
	private static ResourceLocation iebook = new ResourceLocation("immersiveenchanting", "ancient_book");
	private static ResourceLocation script = new ResourceLocation("teo", "enchanted_script");
	
	//this should tackle IE ancient books and Enchanted Scripts from TEO
    @SubscribeEvent
    public void removeEnchantmentTooltips(ItemTooltipEvent event) {
        if (RegistrySkills.SCHOLAR != null && !RegistrySkills.SCHOLAR.get().isEnabled()) {
            ItemStack stack = event.getItemStack();
            ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(stack.getItem());
            boolean ancientBook = itemId.equals(iebook);
            boolean enchantedScript = itemId.equals(script);
            if (ancientBook || enchantedScript) {
                List<Component> tooltip = event.getToolTip();
                Component locked = Component.translatable("tooltip.skill.scholar.lock_item").withStyle(ChatFormatting.GRAY);
                // script: replace the name line; book: replace the line under the name
                int line = enchantedScript ? 0 : 1;
                // a blank ancient book (like the icons on Immersive Enchanting's table tabs) only has its name,
                // and set(1) on a one-line tooltip threw IndexOutOfBoundsException and crashed the game
                if (line < tooltip.size()) {
                    tooltip.set(line, locked);
                } else {
                    tooltip.add(locked);
                }
            }
        }
    }
	
    @SubscribeEvent
    public void onTooltipDisplay(ItemTooltipEvent event) {
    	Minecraft instance = Minecraft.getInstance();
        if (instance.player != null && (Minecraft.getInstance()).player.isAlive()) {
            List<Component> tooltips = event.getToolTip();
            ItemStack itemStack = event.getItemStack();

            ResourceLocation location = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(itemStack.getItem()));
            List<Aptitudes> list = HandlerAptitude.getValue(location.toString());
            if (list != null) {
                tooltips.add(Component.empty());
                tooltips.add(Component.translatable("tooltip.aptitude.item_requirement").withStyle(ChatFormatting.DARK_PURPLE));
                for (Aptitudes aptitudes : list) {
                    Aptitude aptitude = aptitudes.getAptitude();
                    if (aptitude != null) {
                        ChatFormatting colour = (AptitudeCapability.get().getAptitudeLevel(aptitude) >= aptitudes.getAptitudeLvl()) ? ChatFormatting.GREEN : ChatFormatting.RED;
                        tooltips.add(Component.translatable("tooltip.aptitude.item_requirements", Component.translatable(aptitude.getKey()), Component.literal(String.valueOf(aptitudes.getAptitudeLvl())).withStyle(colour)));
                    }
                }
            }
        }
    }
}


