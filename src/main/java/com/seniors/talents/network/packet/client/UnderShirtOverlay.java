package com.seniors.talents.network.packet.client;

import java.util.UUID;
import java.util.function.Supplier;

import com.seniors.talents.client.gui.UnderShirtOverlayHandler;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class UnderShirtOverlay {
	private final UUID id;
    public UnderShirtOverlay(UUID id) {
    	this.id = id;
    }

    public UnderShirtOverlay(FriendlyByteBuf buf) {
        this.id = buf.readUUID();
     }
    
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUUID(this.id);
     }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            UnderShirtOverlayHandler.triggerOverlay(this.id);
        });
        ctx.get().setPacketHandled(true);
    }
}
