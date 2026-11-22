package com.seniors.talents.mixin;

import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.seniors.talents.common.capability.AptitudeCapability;
import com.seniors.talents.registry.RegistrySkills;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DragonEggBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

@Debug(export=true)
@Mixin(DragonEggBlock.class)
public class DragonEggBlockMixin {

	@Inject(method = "use", at = @At("HEAD"), cancellable = true)
	private void preventUse(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand,
			BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir) {
		if (AptitudeCapability.get(player) != null) {
			if (RegistrySkills.TELEPORT_DISRUPTION.get().isEnabled(player)) {
				cir.setReturnValue(InteractionResult.sidedSuccess(level.isClientSide));
			}
		}
	}

	@Inject(method = "attack", at = @At("HEAD"), cancellable = true)
	private void preventAttack(BlockState state, Level level, BlockPos pos, Player player, CallbackInfo ci) {
		if (AptitudeCapability.get(player) != null) {
			if (RegistrySkills.TELEPORT_DISRUPTION.get().isEnabled(player)) {
				ci.cancel();
			}
		}
	}

}
