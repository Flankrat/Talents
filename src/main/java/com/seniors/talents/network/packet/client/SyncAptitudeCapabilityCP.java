package com.seniors.talents.network.packet.client;

import com.seniors.talents.common.capability.AptitudeCapability;
import com.seniors.talents.network.ServerNetworking;

import java.util.function.Supplier;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

public class SyncAptitudeCapabilityCP {
    private final CompoundTag nbt;

    public SyncAptitudeCapabilityCP(CompoundTag nbt) {
        this.nbt = nbt;
    }

    public SyncAptitudeCapabilityCP(FriendlyByteBuf buffer) {
        this.nbt = buffer.readNbt();
    }

    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeNbt(this.nbt);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> AptitudeCapability.get().deserializeNBT(this.nbt));

        context.setPacketHandled(true);
    }

    public static void send(Player player) {
        ServerNetworking.sendToPlayer(new SyncAptitudeCapabilityCP(AptitudeCapability.get(player).serializeNBT()), (ServerPlayer) player);
    }
}


