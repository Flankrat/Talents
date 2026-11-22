package com.seniors.talents.client.gui;

import java.util.HashMap;
import java.util.UUID;
import net.minecraft.world.entity.LivingEntity;

public class UnderShirtOverlayHandler {
    private static final HashMap<UUID, Integer> OVERLAY_TIMERS = new HashMap<>();

    public static void triggerOverlay(LivingEntity entity) {
        OVERLAY_TIMERS.put(entity.getUUID(), 300); 
    }
    
    public static void triggerOverlay(UUID id) {
        OVERLAY_TIMERS.put(id, 300); 
    }

    public static int getTime(LivingEntity entity) {
        return OVERLAY_TIMERS.getOrDefault(entity.getUUID(), 0);
    }

    public static void tick() {
        OVERLAY_TIMERS.replaceAll((uuid, time) -> time > 0 ? time - 1 : 0);
    }
}
