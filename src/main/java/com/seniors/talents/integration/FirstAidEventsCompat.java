package com.seniors.talents.integration;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.PacketDistributor;
import ichttt.mods.firstaid.FirstAid;
import ichttt.mods.firstaid.api.damagesystem.AbstractDamageablePart;
import ichttt.mods.firstaid.api.damagesystem.AbstractPlayerDamageModel;

import java.util.ArrayList;
import java.util.List;

import com.seniors.talents.handler.HandlerCommonConfig;
import com.seniors.talents.network.ServerNetworking;
import com.seniors.talents.network.packet.client.UnderShirtOverlay;
import com.seniors.talents.registry.RegistrySkills;
import ichttt.mods.firstaid.api.event.FirstAidLivingDamageEvent;
import ichttt.mods.firstaid.common.network.MessageUpdatePart;

public class FirstAidEventsCompat {
	@SubscribeEvent(priority = EventPriority.HIGH)
    public static void onFirstAidLivingDamageHigh(FirstAidLivingDamageEvent event) {
        if(event.getEntity()== null || event.getEntity().level().isClientSide()) return;
        if(event.getUndistributedDamage() > 1000) return; //Don't protect against attacks meant to instakill
        if(event.getSource().is(DamageTypeTags.BYPASSES_SHIELD)) return;//Don't protect against certain non-attack damage sources

        AbstractPlayerDamageModel before = event.getBeforeDamage();
        AbstractPlayerDamageModel after = event.getAfterDamage();
        Player entity = event.getEntity();
        if (RegistrySkills.SURVIVAL_INSTINCTS.get().isEnabled(entity) && entity.getRandom().nextFloat() <= HandlerCommonConfig.HANDLER.instance().survivalinstinctschance) {
        	float deviate = 0;
        	boolean head = true;
        	if (before.HEAD.canCauseDeath && (before.HEAD.currentHealth - after.HEAD.currentHealth) > 0) {
        		deviate = before.HEAD.currentHealth - after.HEAD.currentHealth;
        		after.HEAD.heal(deviate, null, false);
        	} else if (before.BODY.canCauseDeath && (before.BODY.currentHealth - after.BODY.currentHealth) > 0) {
        		deviate = before.BODY.currentHealth - after.BODY.currentHealth;
        		after.BODY.heal(deviate, null, false);
        		head = false;
        	}
        	if (deviate > 0 && after.LEFT_ARM.currentHealth + after.RIGHT_ARM.currentHealth >= deviate) {
        		float rest = after.RIGHT_ARM.damage(deviate, entity, false);
        		if (rest > 0) {
        			after.LEFT_ARM.damage(deviate, entity, false);
        		}
        		if (entity instanceof ServerPlayer player) {
        			if (head) {
        				FirstAid.NETWORKING.send(PacketDistributor.PLAYER.with(() -> player),
                                new MessageUpdatePart(after.HEAD));
        				FirstAid.NETWORKING.send(PacketDistributor.PLAYER.with(() -> player),
                                new MessageUpdatePart(after.RIGHT_ARM));
        				if (rest > 0) {
        					FirstAid.NETWORKING.send(PacketDistributor.PLAYER.with(() -> player),
                                    new MessageUpdatePart(after.LEFT_ARM));
        				}
        			} else {
        				FirstAid.NETWORKING.send(PacketDistributor.PLAYER.with(() -> player),
                                new MessageUpdatePart(after.BODY));
        				FirstAid.NETWORKING.send(PacketDistributor.PLAYER.with(() -> player),
                                new MessageUpdatePart(after.RIGHT_ARM));
        				if (rest > 0) {
        					FirstAid.NETWORKING.send(PacketDistributor.PLAYER.with(() -> player),
                                    new MessageUpdatePart(after.LEFT_ARM));
        				}
        			}                   
                }
        	}
        }
        List<AbstractDamageablePart> parts = new ArrayList<>();
        for(AbstractDamageablePart part : after) {//Check if they are going to die first before bothering with trait check
            if(part.canCauseDeath && part.currentHealth <= 0) {
                parts.add(part);
            }
        }

        if(!parts.isEmpty() && RegistrySkills.UNDERSHIRT.get().isEnabled(entity)) {
            if(entity.getPersistentData().getInt("talents:UndershirtCD") <= 0) {
                boolean saved = false;
                for(AbstractDamageablePart part : parts) {//Iterate parts
                    if(before.getFromEnum(part.part).currentHealth >= 4.0) {//Only proc undershirt if the part had atleast 2 hearts
                        part.heal(1.0F, null, false);
                        if (entity instanceof ServerPlayer player) {
                            FirstAid.NETWORKING.send(PacketDistributor.PLAYER.with(() -> player),
                                    new MessageUpdatePart(part));
                        }
                        saved = true;
                    }
                }
                if(saved) {
                    entity.getPersistentData().putInt("talents:UndershirtCD", 1200);
                    entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ANVIL_PLACE, SoundSource.PLAYERS, 0.8F, (entity.level().getRandom().nextFloat()-entity.level().getRandom().nextFloat())*0.1F+0.8F);
                    if (entity instanceof ServerPlayer player) {
                    	ServerNetworking.sendToPlayer(new UnderShirtOverlay(entity.getUUID()), player);
                    }
                }
            }
        }
    }
	
	@SubscribeEvent
    public static void undershirttick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && event.player != null) {
        	int undertick = event.player.getPersistentData().getInt("talents:UndershirtCD");
        	if (undertick > 0) {
        		event.player.getPersistentData().putInt("talents:UndershirtCD", undertick - 1);
        	}
        }
    }
	
}
