package com.seniors.talents.mixin;

import com.seniors.talents.handler.HandlerCommonConfig;
import com.seniors.talents.registry.RegistryPassives;
import com.seniors.talents.registry.RegistrySkills;
import com.seniors.talents.registry.passive.Passive;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({Player.class})
public abstract class MixPlayer extends LivingEntity {
    @Unique
    private final Player this$class = (Player) (Object) this;

    protected MixPlayer(Level level) {
        super(EntityType.PLAYER, level);
    }


    public int getMaxAirSupply() {
        if (RegistrySkills.ATHLETICS != null && RegistrySkills.ATHLETICS.get().isEnabled(this.this$class)) {
            return (int) (300.0D * RegistrySkills.ATHLETICS.get().getValue()[0]);
        }
        return 300;
    }
    
    @Inject(method = "disableShield(Z)V", at = @At(value = "HEAD"), cancellable = true)
	private void shield_proficiency(boolean flag, CallbackInfo cir) {
		// Check chance here
		float resistChance = HandlerCommonConfig.HANDLER.instance().shieldproficiency;
		Player player = (Player)(Object) this;
		if (RegistryPassives.SHIELD_PROFICIENCY != null) {
			Passive shield = RegistryPassives.SHIELD_PROFICIENCY.get();
			if (shield.getLevel(player) > 0) {
				if (player.getRandom().nextFloat() < resistChance * (shield.getLevel(player) * shield.getLevelRatio())) {
					cir.cancel();; // Skip the disableShield call completely
				}
			}
		}
	}
	
	@Inject(method = "m_36384_(Z)V", at = @At(value = "HEAD"), cancellable = true, remap = false)
	private void shield_proficiency_remap(boolean flag, CallbackInfo cir) {
		// Check chance here
		float resistChance = HandlerCommonConfig.HANDLER.instance().shieldproficiency;
		Player player = (Player)(Object) this;
		if (RegistryPassives.SHIELD_PROFICIENCY != null) {
			Passive shield = RegistryPassives.SHIELD_PROFICIENCY.get();
			if (shield.getLevel(player) > 0) {
				if (player.getRandom().nextFloat() < resistChance * (shield.getLevel(player) * shield.getLevelRatio())) {
					cir.cancel();; // Skip the disableShield call completely
				}
			}
		}
	}
}


