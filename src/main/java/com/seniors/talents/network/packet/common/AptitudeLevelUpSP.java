package com.seniors.talents.network.packet.common;

import com.seniors.talents.common.capability.AptitudeCapability;
import com.seniors.talents.handler.HandlerCommonConfig;
import com.seniors.talents.network.ServerNetworking;
import com.seniors.talents.network.packet.client.SyncAptitudeCapabilityCP;
import com.seniors.talents.registry.RegistryAptitudes;
import com.seniors.talents.registry.aptitude.Aptitude;
import com.seniors.talents.Talents;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class AptitudeLevelUpSP {
    private final String aptitude;

    public AptitudeLevelUpSP(Aptitude aptitude) {
        this.aptitude = aptitude.getName();
    }

    public AptitudeLevelUpSP(FriendlyByteBuf buffer) {
        this.aptitude = buffer.readUtf();
    }

    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeUtf(this.aptitude);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                AptitudeCapability capability = AptitudeCapability.get(player);
                Aptitude aptitudePlayer = RegistryAptitudes.getAptitude(this.aptitude);
                int aptitudeLevel = capability.getAptitudeLevel(aptitudePlayer);

                boolean canLevelUpAptitude = (player.isCreative()
                        || AptitudeLevelUpSP.requiredPoints(aptitudeLevel) <= player.totalExperience
                        || AptitudeLevelUpSP.requiredExperienceLevels(aptitudeLevel) <= player.experienceLevel);

                if (!canLevelUpAptitude){
                    Talents.getLOGGER().info("Received level up packet without the required EXP needed to level up, skipping packet...");
                    return;
                }

                int requiredPoints = requiredPoints(aptitudeLevel);

                capability.addAptitudeLevel(aptitudePlayer, 1);
                SyncAptitudeCapabilityCP.send(player);
                if (!player.isCreative()) {
                    addPlayerXP(player, requiredPoints * -1);
                }
            }
        });
        context.setPacketHandled(true);
    }

    public static int getPlayerXP(Player player) {
        return (int)(getExperienceForLevel(player.experienceLevel) + (player.experienceProgress * player.getXpNeededForNextLevel()));
    }

    public static int xpBarCap(int level) {
        if (level >= 30)
            return 112 + (level - 30) * 9;

        if (level >= 15)
            return 37 + (level - 15) * 5;

        return 7 + level * 2;
    }

    public void addPlayerXP(Player player, int amount) {
        int experience = getPlayerXP(player) + amount;
        player.totalExperience = experience;
        player.experienceLevel = getLevelForExperience(experience);
        int expForLevel = getExperienceForLevel(player.experienceLevel);
        player.experienceProgress = (experience - expForLevel) / (float)player.getXpNeededForNextLevel();
    }

    public static int getLevelForExperience(int targetXp) {
        int level = 0;
        while (true) {
            final int xpToNextLevel = xpBarCap(level);
            if (targetXp < xpToNextLevel) return level;
            level++;
            targetXp -= xpToNextLevel;
        }
    }

    public static int requiredPoints(int aptitudeLevel) {
        return getExperienceForLevel(aptitudeLevel + HandlerCommonConfig.HANDLER.instance().aptitudeFirstCostLevel - 1);
    }

    public static int requiredExperienceLevels(int aptitudeLevel) {
        return aptitudeLevel + HandlerCommonConfig.HANDLER.instance().aptitudeFirstCostLevel - 1;
    }

    public static int getExperienceForLevel(int level) {
        if (level == 0) return 0;
        if (level <= 15) return sum(level, 7, 2);
        if (level <= 30) return 315 + sum(level - 15, 37, 5);
        return 1395 + sum(level - 30, 112, 9);
    }

    private static int sum(int n, int a0, int d) {
        return n * (2 * a0 + (n - 1) * d) / 2;
    }

    public static void send(Aptitude aptitude) {
        ServerNetworking.sendToServer(new AptitudeLevelUpSP(aptitude));
    }
}


