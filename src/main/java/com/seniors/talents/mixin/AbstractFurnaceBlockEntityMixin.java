package com.seniors.talents.mixin;

import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlastFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.SmokerBlockEntity;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.seniors.talents.common.capability.AptitudeCapability;
import com.seniors.talents.registry.RegistryPassives;
import com.seniors.talents.registry.passive.Passive;


@Mixin(AbstractFurnaceBlockEntity.class)
public class AbstractFurnaceBlockEntityMixin {
	
	@SuppressWarnings("removal")
	private static final TagKey<Item> RawMineral = TagKey.create(Registries.ITEM, new ResourceLocation("forge", "raw_materials"));

    // Inject at the HEAD of getTotalCookTime()
    @Inject(method = "getTotalCookTime(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/level/block/entity/AbstractFurnaceBlockEntity;)I",
            at = @At("RETURN"), cancellable = true)
    private static void onGetTotalCookTime(Level level, AbstractFurnaceBlockEntity furnace, CallbackInfoReturnable<Integer> cir) {

        // Retrieve the UUID of the last interacting player
        if (furnace.getPersistentData().hasUUID("LastInteractingPlayer")) {
            UUID playerUUID = furnace.getPersistentData().getUUID("LastInteractingPlayer");
            Player player = level.getPlayerByUUID(playerUUID);
            if (player != null && AptitudeCapability.get(player) != null) {
            	Passive cooking = RegistryPassives.COOKING_SPEED.get();
            	float levels = (float) (cooking.getLevel(player) * cooking.getLevelRatio());
            	if (levels > 0 && (furnace instanceof SmokerBlockEntity || furnace.getItem(0).isEdible())) {
                    // Example: reduce cook time by 50% for this player
                    int baseCookTime = cir.getReturnValueI();
                    int reducedCookTime = Math.max(1, (int) ((float) baseCookTime
                    	      * (1.0F - cooking.getValue() * levels)));
                    // Override vanilla return value
                    cir.setReturnValue(reducedCookTime);
                    return;
                }
            	Passive smelt = RegistryPassives.SMELTING_SPEED.get();
            	float smelting =(float) (smelt.getLevel(player)*smelt.getLevelRatio());
            	if (smelting > 0 && (furnace instanceof BlastFurnaceBlockEntity || furnace.getItem(0).is(RawMineral))) {
                    // Example: reduce cook time by 50% for this player
                    int baseCookTime = cir.getReturnValueI();
                    int reducedCookTime = Math.max(1, (int) ((float) baseCookTime
                    	      * (1.0F - smelt.getValue() * smelting)));
                    // Override vanilla return value
                    cir.setReturnValue(reducedCookTime);
                    return;
                }
            }
        }
    }
}