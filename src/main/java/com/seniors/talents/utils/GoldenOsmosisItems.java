package com.seniors.talents.utils;

import com.seniors.talents.Talents;
import com.seniors.talents.handler.HandlerCommonConfig;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The goldenOsmosisExtraItems config list: modded items Golden Osmosis repairs like vanilla gold.
 * Entries can be:
 * <ul>
 *     <li>{@code modid:item} - one item</li>
 *     <li>{@code modid:prefix*} - every item of that mod whose name starts with prefix</li>
 *     <li>{@code #modid:tag} - every item in an item tag</li>
 * </ul>
 * The list is parsed once and parsed again only when the config list changes (config reload).
 */
public final class GoldenOsmosisItems {

    // parsed entries, and the config list they were parsed from
    private static Set<ResourceLocation> ids = Set.of();
    private static List<String[]> prefixes = List.of();
    private static List<TagKey<Item>> tags = List.of();
    private static List<String> parsedFrom;

    private GoldenOsmosisItems() {}

    /** True when the stack is one of the extra golden items. */
    public static boolean matches(ItemStack stack) {
        if (stack.isEmpty()) return false;
        parseIfChanged();

        for (TagKey<Item> tag : tags) {
            if (stack.is(tag)) return true;
        }
        if (ids.isEmpty() && prefixes.isEmpty()) return false;

        ResourceLocation id = ForgeRegistries.ITEMS.getKey(stack.getItem());
        if (id == null) return false;
        if (ids.contains(id)) return true;
        for (String[] prefix : prefixes) {
            if (id.getNamespace().equals(prefix[0]) && id.getPath().startsWith(prefix[1])) return true;
        }
        return false;
    }

    private static synchronized void parseIfChanged() {
        List<String> list = HandlerCommonConfig.HANDLER.instance().goldenOsmosisExtraItems;
        if (list == parsedFrom) return;

        Set<ResourceLocation> newIds = new HashSet<>();
        List<String[]> newPrefixes = new ArrayList<>();
        List<TagKey<Item>> newTags = new ArrayList<>();
        for (String raw : list == null ? List.<String>of() : list) {
            String entry = raw.trim();
            if (entry.isEmpty()) continue;

            if (entry.startsWith("#")) {
                // item tag
                ResourceLocation tag = ResourceLocation.tryParse(entry.substring(1));
                if (tag != null) newTags.add(TagKey.create(Registries.ITEM, tag));
                else Talents.getLOGGER().warn("Golden Osmosis: ignoring invalid item tag \"{}\"", entry);
            } else if (entry.endsWith("*")) {
                // namespace + name prefix
                String body = entry.substring(0, entry.length() - 1);
                int colon = body.indexOf(':');
                if (colon > 0) newPrefixes.add(new String[]{body.substring(0, colon), body.substring(colon + 1)});
                else Talents.getLOGGER().warn("Golden Osmosis: ignoring \"{}\", a prefix needs modid:", entry);
            } else {
                // single item
                ResourceLocation id = ResourceLocation.tryParse(entry);
                if (id != null) newIds.add(id);
                else Talents.getLOGGER().warn("Golden Osmosis: ignoring invalid item id \"{}\"", entry);
            }
        }
        ids = Set.copyOf(newIds);
        prefixes = List.copyOf(newPrefixes);
        tags = List.copyOf(newTags);
        parsedFrom = list;
    }
}
