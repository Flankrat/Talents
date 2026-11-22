package com.seniors.talents.network.packet.client;

import java.util.function.Supplier;

import com.seniors.talents.client.core.ClientSideEvents;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class DodgeClientPacket {
	public DodgeClientPacket() {}
    public DodgeClientPacket(FriendlyByteBuf buffer) {}
    public void toBytes(FriendlyByteBuf buf) {}


    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientSideEvents.triggerDodge();
        });
        ctx.get().setPacketHandled(true);
    }
}
