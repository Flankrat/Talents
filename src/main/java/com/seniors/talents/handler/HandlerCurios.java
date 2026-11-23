package com.seniors.talents.handler;

import com.seniors.talents.common.capability.AptitudeCapability;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.event.CurioEquipEvent;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

public class HandlerCurios {
    public static boolean isModLoaded() {
        return ModList.get().isLoaded("curios");
    }

    /**
     * Curio locks. Curios fires this from the slot's mayPlace on both sides, many times per click (shift-click
     * tries every curio slot), so the client checks too (otherwise it shows the item going in, then the server
     * puts it back) and only the server sends the "locked" warning, which is limited to once a second per item.
     */
    @SubscribeEvent
    public void onCurioCanEquipEvent(CurioEquipEvent event){
        if (!(event.getEntity() instanceof Player player) || player.isCreative()) return;
        ItemStack item = event.getStack();
        if (item.isEmpty()) return;

        AptitudeCapability aptitudeCapability = AptitudeCapability.get(player);
        if (aptitudeCapability == null) return;

        try {
            boolean allowed = player.level().isClientSide()
                    ? aptitudeCapability.canUseItemQuiet(item)
                    : aptitudeCapability.canUseItem(player, item);
            if (!allowed) {
                event.setResult(Event.Result.DENY);
            }
        }
        // This NullPointerException can happen if this event is triggered before the player fully establish the connection.
        catch (NullPointerException ignored) {
            event.setResult(Event.Result.DENY);
        }
    }

    @SubscribeEvent
    public void onPlayerChangeGameModeEvent(PlayerEvent.PlayerChangeGameModeEvent event){
        if(!HandlerCurios.isModLoaded()){
            return;
        }

        if (event.getNewGameMode() == GameType.SURVIVAL){
            Player player = event.getEntity();

            AptitudeCapability aptitudeCapability = AptitudeCapability.get(player);
            if (aptitudeCapability == null) return;
            CuriosApi.getCuriosInventory(player).ifPresent(curiosInventory -> {
                curiosInventory.getCurios().forEach((id, slotInventory) -> {
                    IDynamicStackHandler stackHandler =  slotInventory.getStacks();
                    for(int i = 0; i < stackHandler.getSlots(); i++){
                        ItemStack itemStack = stackHandler.getStackInSlot(i);

                        if (!itemStack.isEmpty() && !aptitudeCapability.canUseItem(player, itemStack)) {
                            // take it out of the slot first and drop that: dropping the stack and then emptying
                            // it (setCount(0)) emptied the dropped item too, so the curio was destroyed
                            stackHandler.setStackInSlot(i, ItemStack.EMPTY);
                            player.drop(itemStack, false);
                            curiosInventory.clearSlotModifiers();
                        }
                    }
                });
            });
        }
    }
}