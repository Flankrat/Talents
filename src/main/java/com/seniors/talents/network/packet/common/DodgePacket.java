package com.seniors.talents.network.packet.common;

import java.util.function.Supplier;

import com.seniors.talents.api.DodgeEvent;
import com.seniors.talents.common.capability.AptitudeCapability;
import com.seniors.talents.handler.HandlerCommonConfig;
import com.seniors.talents.registry.RegistryCommonEvents;
import com.seniors.talents.registry.RegistrySkills;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.NetworkEvent;

public class DodgePacket {
    private final short left;

    public DodgePacket(short left){ 
    	this.left = left; 
    }
    public DodgePacket(FriendlyByteBuf buf) {
        this.left = buf.readShort();
    }

    public void toBytes(FriendlyByteBuf buf) {
    	buf.writeShort(this.left);
    }
    
    
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            DodgeEvent event = new DodgeEvent(player);
            MinecraftForge.EVENT_BUS.post(event);
            if (event.isCanceled()) {
            	return;
            }
            if (player == null) return;
            // Perform same checks server-side
            if (AptitudeCapability.get(player) == null) return;
    		AptitudeCapability cap = AptitudeCapability.get(player);
    		if (!cap.getToggleSkill(RegistrySkills.SIDESTEP.get())) return;
            if (player.onGround() && !player.isSpectator() && player.getDeltaMovement().y <= 0 && !RegistryCommonEvents.DODGE_CD.containsKey(player)) {
            	var config = HandlerCommonConfig.HANDLER.instance();
            	float yaw = (float)Math.toRadians(player.getYRot());
				double x = -Math.sin(yaw);
		        double z =  Math.cos(yaw);
		        RegistryCommonEvents.DODGE_CD.put(player, config.sidestepCooldown);
		        double xVel = 0, zVel = 0;
            	switch(this.left) {
            		case 0 -> { //Forward
            			xVel = x * config.sidestepPower;
                        zVel = z * config.sidestepPower;
            		}
            		case 1 -> { //Backwards
            			xVel = -x * config.sidestepPower;
                        zVel = -z * config.sidestepPower;
            		}
            		case 2 -> { // Left
            			xVel = z * config.sidestepPower;
                        zVel = -x * config.sidestepPower;
            		}
            		case 3 -> { // Right
            			xVel = -z * config.sidestepPower;
                        zVel = x * config.sidestepPower;
            		}
            		default -> {
            			xVel = x * config.sidestepPower;
                        zVel = z * config.sidestepPower;
            		}
            	}
            	Vec3 delta = player.getDeltaMovement();
            	player.setDeltaMovement(delta.x + xVel, 0.25d, delta.z + zVel);
                player.hasImpulse = true;
                player.hurtMarked = true;
            }
        });
        ctx.get().setPacketHandled(true);
    }
}