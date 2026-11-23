package com.seniors.talents.mixin;

import com.seniors.talents.handler.StationXp;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Learn by doing: lets {@link StationXp} see a workstation's output being taken. Every inventory click goes
 * through {@code clicked}, so the output slot is checked before and after it. Does nothing when learn by doing is off.
 */
@Mixin(AbstractContainerMenu.class)
public abstract class MixAbstractContainerMenu {

    @Inject(method = "clicked", at = @At("HEAD"))
    private void talents$stationXpBefore(int slotId, int button, ClickType clickType, Player player, CallbackInfo ci) {
        StationXp.beforeClick((AbstractContainerMenu) (Object) this, slotId, player);
    }

    @Inject(method = "clicked", at = @At("RETURN"))
    private void talents$stationXpAfter(int slotId, int button, ClickType clickType, Player player, CallbackInfo ci) {
        StationXp.afterClick((AbstractContainerMenu) (Object) this, player);
    }
}
