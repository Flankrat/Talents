package com.seniors.talents.mixin;

import com.seniors.talents.common.capability.AptitudeCapability;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Crafting locks: after the crafting grid (crafting table or inventory 2x2) works out its result, the result is
 * cleared if the player can't use it, or can't use a damageable ingredient (a locked tool used in a recipe).
 * Runs on every grid change, so it returns as early as it can: nothing to do when there's no result, and the
 * result (one item) is checked before the up to 9 ingredients.
 */
@Mixin(CraftingMenu.class)
public abstract class MixCraftingMenu {
    @Inject(at = @At("TAIL"), method = "slotChangedCraftingGrid")
	private static void slotChangedCraftingGrid(AbstractContainerMenu pMenu, Level level, Player player,
			CraftingContainer container, ResultContainer resultContainer, CallbackInfo ci) {
		if (!(player instanceof ServerPlayer serverPlayer) || serverPlayer.isCreative())
			return;
		ItemStack result = resultContainer.getItem(0);
		if (result.isEmpty())
			return;   // no recipe matched, nothing to lock
		AptitudeCapability provider = AptitudeCapability.get(player);
		if (provider == null)
			return;
		if (!provider.canUseItem(serverPlayer, result)) {
			resultContainer.setItem(0, ItemStack.EMPTY);
			return;
		}
		for (ItemStack stack : container.getItems()) {
			if (!stack.isEmpty() && stack.isDamageableItem() && !provider.canUseItem(serverPlayer, stack)) {
				resultContainer.setItem(0, ItemStack.EMPTY);
				return;
			}
		}
	}
}
