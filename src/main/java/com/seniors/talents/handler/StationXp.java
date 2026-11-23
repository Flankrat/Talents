package com.seniors.talents.handler;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Learn by doing: building XP for using workstations (anvil, smithing table, grindstone, stonecutter, loom,
 * cartography table, modded forges... listed in talents.learn-by-doing.json5 by menu type).
 * <p>
 * Most stations fire no event when you take their output, so MixAbstractContainerMenu watches the clicked slot
 * instead: before the click, if the menu is a listed station and the slot is an output slot (not the player's
 * inventory, and it won't accept the item it holds), its stack is remembered; if the click took items from it,
 * that counts as one use. Crafting tables use Forge's ItemCraftedEvent instead (in HandlerLearnByDoing).
 */
public final class StationXp {

    // the output slot and its stack before the current click (server thread only)
    private static Slot watched;
    private static ItemStack stackBefore = ItemStack.EMPTY;
    private static int countBefore;

    private StationXp() {}

    /** Before a click: remember a station's output slot and how much it holds. */
    public static void beforeClick(AbstractContainerMenu menu, int slotId, Player player) {
        watched = null;
        if (!HandlerLearnByDoing.enabled() || !(player instanceof ServerPlayer) || slotId < 0 || slotId >= menu.slots.size()) return;
        if (stationXp(menu) <= 0) return;
        Slot slot = menu.getSlot(slotId);
        ItemStack stack = slot.getItem();
        if (stack.isEmpty() || slot.container instanceof Inventory || slot.mayPlace(stack)) return;   // not an output slot
        watched = slot;
        stackBefore = stack;
        countBefore = stack.getCount();
    }

    /**
     * After the click: if the remembered output was taken, the player used the station. Taken means the slot now
     * holds less, or a different stack: stations like the stonecutter put a fresh output in the moment you take one.
     */
    public static void afterClick(AbstractContainerMenu menu, Player player) {
        Slot slot = watched;
        ItemStack before = stackBefore;
        watched = null;
        stackBefore = ItemStack.EMPTY;
        if (slot == null || !(player instanceof ServerPlayer serverPlayer)) return;
        ItemStack now = slot.getItem();
        if (now != before || now.getCount() < countBefore)
            HandlerLearnByDoing.give(serverPlayer, HandlerLearnByDoing.BUILDING, stationXp(menu));
    }

    /** Building XP for one use of this menu's station, or 0 if it isn't a listed station. */
    private static double stationXp(AbstractContainerMenu menu) {
        MenuType<?> type;
        try {
            type = menu.getType();
        } catch (UnsupportedOperationException e) {   // the player inventory has no menu type
            return 0;
        }
        ResourceLocation id = ForgeRegistries.MENU_TYPES.getKey(type);
        return id == null ? 0 : HandlerLearnByDoingConfig.HANDLER.instance().stationXp(id.toString());
    }
}
