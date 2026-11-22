package com.seniors.talents.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.util.Mth;
@Mixin(ClientboundSetEntityMotionPacket.class)
public class ClientboundSetEntityMotionPacketMixin {

	// Intercept the constructor and adjust clamping after original values are set.
	@Redirect(method = "<init>(ILnet/minecraft/world/phys/Vec3;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;m_14008_(DDD)D"), remap = false)
	private double increaseCapClamp(double value, double min, double max) {
		double maxVelocity = Short.MAX_VALUE; // New max cap
		return Mth.clamp(value, -maxVelocity, maxVelocity);
	}
}
