package com.seniors.talents.mixin;

import com.mojang.datafixers.kinds.K1;

import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import java.util.Optional;

import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Debug(export=true)
@Mixin(MemoryAccessor.class)
public class MemoryAccessorMixin<F extends K1, Value> {

	@Shadow
	@Final
	private Brain<?> brain;

	@Shadow
	@Final
	private MemoryModuleType<Value> memoryType;

	@Inject(method = "set", at = @At("HEAD"), cancellable = true)
	private void avoidnulloptional(Value value, CallbackInfo ci) {
		if (value == null) {
			this.brain.setMemory(this.memoryType, Optional.empty());

			ci.cancel();
		}
	}
}