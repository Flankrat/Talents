package com.seniors.talents.utils;

import com.seniors.talents.common.capability.AptitudeCapability;
import com.seniors.talents.registry.RegistrySkills;
import com.seniors.talents.registry.RegistrySounds;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class LedgeGrabHandler {

	public static void tryJump(ServerPlayer player) {
	    Level level = player.level();
	    if (player.isSpectator() || player.isCreative()) return;
	    if (player.onGround()) return; // only midair
	    if (player.getPersistentData().getBoolean("VaultUsed")) return;
	    if (AptitudeCapability.get(player) == null) return;
	    AptitudeCapability cap = AptitudeCapability.get(player);
	    if (!cap.getToggleSkill(RegistrySkills.LEDGE_GRAB.get())) return;
	    Vec3 current = player.getDeltaMovement();
	    player.setDeltaMovement(current.x, 0.5, current.z);
        player.hurtMarked = true;
        player.fallDistance = 0f;
        player.getPersistentData().putBoolean("VaultUsed", true);
        level.playSound(player, player.blockPosition(), RegistrySounds.LEDGE_GRAB.get(), SoundSource.PLAYERS);
	}

    public static void tickPlayer(ServerPlayer player) {
        if (player.onGround()) {
            player.getPersistentData().putBoolean("VaultUsed", false);
        }
    }
}