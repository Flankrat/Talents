package com.seniors.talents.api;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

/*
 * Event fires only on server side.
 */
@Cancelable
public class DodgeEvent extends Event {

	private final Player player;

	public DodgeEvent(Player player) {
		this.player = player;
	}
	
	public Player getEntity() {
		return this.player;
	}
	
}
