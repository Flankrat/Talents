package com.seniors.talents.network.packet.client;

import com.seniors.talents.handler.HandlerCommonConfig;
import com.seniors.talents.network.ServerNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Client packet to update dynamic config options.
 * Can be sent multiple times during a player session.
 */
public class DynamicConfigSyncCP {

    private final int aptitudeMaxLevel;
    private final int playersMaxGlobalLevel;
    private final int[] attackPassiveLevels;
    private final int[] movementSpeedPassiveLevels;
    private final int[] projectileSpeedPassiveLevels;
    private final int[] blockReachPassiveLevels;
    private final int[] breakSpeedPassiveLevels;
    private final int[] criticalChancePassiveLevels;
    private final int[] luckPassiveLevels;
    private final int[] damagereductionlevels;
    private final int[] shieldproficiencylevels;
    private final int[] stealthLevels;
    private final int[] cookingspeedLevels;
    private final int[] smeltingspeedLevels;
    private final int[] mineralefficiencyLevels;
    private final int[] greenthumbLevels;
    private final int[] fishingproficiencyLevels;
    private final int[] manaRegenLevels;
    private final int[] woodChoppingPassiveLevels;
    
    // Costs
    private final int[] attackdamagecosts;
    private final int[] movementspeedcosts;
    private final int[] projectilespeedcosts;
    private final int[] blockreachcosts;
    private final int[] breakSpeedcosts;
    private final int[] criticalChancecosts;
    private final int[] luckpassivecosts;
    private final int[] damagereductioncosts;
    private final int[] shieldproficiencycosts;
    private final int[] stealthcosts;
    private final int[] cookingspeedcosts;
    private final int[] smeltingspeedcosts;
    private final int[] mineralefficiencycosts;
    private final int[] greenthumbcosts;
    private final int[] fishingproficiencycosts;
    private final int[] manaRegencosts;
    private final int[] woodChoppingcosts;

    private final int oneHandedRequiredLevel;
    private final int fightingSpiritRequiredLevel;
    private final int berserkerRequiredLevel;
    private final int athleticsRequiredLevel;
    private final int quickRepositionRequiredLevel;
    private final int snowWalkerRequiredLevel;
    private final int scholarRequiredLevel;
    private final int hagglerRequiredLevel;
    private final int obsidianSmasherRequiredLevel;
    private final int treasureHunterRequiredLevel;
    private final int convergenceRequiredLevel;
    private final int safePortRequiredLevel;
    private final int sidestepRequiredLevel;

    public DynamicConfigSyncCP(){
    	var config = HandlerCommonConfig.HANDLER.instance();

        // General
        aptitudeMaxLevel = config.aptitudeMaxLevel;
        playersMaxGlobalLevel = config.playersMaxGlobalLevel;

        // Passive Levels
        attackPassiveLevels = config.attackPassiveLevels;
        movementSpeedPassiveLevels = config.movementSpeedPassiveLevels;
        projectileSpeedPassiveLevels = config.projectileSpeedPassiveLevels;
        blockReachPassiveLevels = config.blockReachPassiveLevels;
        breakSpeedPassiveLevels = config.breakSpeedPassiveLevels;
        criticalChancePassiveLevels = config.criticalChancePassiveLevels;
        luckPassiveLevels = config.luckPassiveLevels;
        damagereductionlevels = config.damagereductionlevels;
        shieldproficiencylevels = config.shieldproficiencylevels;
        stealthLevels = config.stealthLevels;
        cookingspeedLevels = config.cookingspeedLevels;
        smeltingspeedLevels = config.smeltingspeedLevels;
        mineralefficiencyLevels = config.mineralefficiencyLevels;
        greenthumbLevels = config.greenthumbLevels;
        fishingproficiencyLevels = config.fishingproficiencyLevels;
        manaRegenLevels = config.manaRegenLevels;
        woodChoppingPassiveLevels = config.woodChoppingPassiveLevels;

        // Costs
        attackdamagecosts = config.attackdamagecosts;
        movementspeedcosts = config.movementspeedcosts;
        projectilespeedcosts = config.projectilespeedcosts;
        blockreachcosts = config.blockreachcosts;
        breakSpeedcosts = config.breakSpeedcosts;
        criticalChancecosts = config.criticalChancecosts;
        luckpassivecosts = config.luckpassivecosts;
        damagereductioncosts = config.damagereductioncosts;
        shieldproficiencycosts = config.shieldproficiencycosts;
        stealthcosts = config.stealthcosts;
        cookingspeedcosts = config.cookingspeedcosts;
        smeltingspeedcosts = config.smeltingspeedcosts;
        mineralefficiencycosts = config.mineralefficiencycosts;
        greenthumbcosts = config.greenthumbcosts;
        fishingproficiencycosts = config.fishingproficiencycosts;
        manaRegencosts = config.manaRegencosts;
        woodChoppingcosts = config.woodChoppingcosts;

        // Skill Requirements
        oneHandedRequiredLevel = config.oneHandedRequiredLevel;
        fightingSpiritRequiredLevel = config.fightingSpiritRequiredLevel;
        berserkerRequiredLevel = config.berserkerRequiredLevel;
        athleticsRequiredLevel = config.athleticsRequiredLevel;
        quickRepositionRequiredLevel = config.quickRepositionRequiredLevel;
        snowWalkerRequiredLevel = config.snowWalkerRequiredLevel;
        scholarRequiredLevel = config.scholarRequiredLevel;
        hagglerRequiredLevel = config.hagglerRequiredLevel;
        obsidianSmasherRequiredLevel = config.obsidianSmasherRequiredLevel;
        treasureHunterRequiredLevel = config.treasureHunterRequiredLevel;
        convergenceRequiredLevel = config.convergenceRequiredLevel;
        safePortRequiredLevel = config.safePortRequiredLevel;
        sidestepRequiredLevel = config.sidestepRequiredLevel;
    }

    public DynamicConfigSyncCP(FriendlyByteBuf buffer){
    	// General
        aptitudeMaxLevel = buffer.readInt();
        playersMaxGlobalLevel = buffer.readInt();

        // Read all arrays from string
        String[] all = buffer.readUtf().split("\\|");

        int index = 0;

        // ===== LEVELS =====
        attackPassiveLevels = parse(all[index++]);
        movementSpeedPassiveLevels = parse(all[index++]);
        projectileSpeedPassiveLevels = parse(all[index++]);
        blockReachPassiveLevels = parse(all[index++]);
        breakSpeedPassiveLevels = parse(all[index++]);
        criticalChancePassiveLevels = parse(all[index++]);
        luckPassiveLevels = parse(all[index++]);
        damagereductionlevels = parse(all[index++]);
        shieldproficiencylevels = parse(all[index++]);
        stealthLevels = parse(all[index++]);
        cookingspeedLevels = parse(all[index++]);
        smeltingspeedLevels = parse(all[index++]);
        mineralefficiencyLevels = parse(all[index++]);
        greenthumbLevels = parse(all[index++]);
        fishingproficiencyLevels = parse(all[index++]);
        manaRegenLevels = parse(all[index++]);
        woodChoppingPassiveLevels = parse(all[index++]);

        // ===== COSTS =====
        attackdamagecosts = parse(all[index++]);
        movementspeedcosts = parse(all[index++]);
        projectilespeedcosts = parse(all[index++]);
        blockreachcosts = parse(all[index++]);
        breakSpeedcosts = parse(all[index++]);
        criticalChancecosts = parse(all[index++]);
        luckpassivecosts = parse(all[index++]);
        damagereductioncosts = parse(all[index++]);
        shieldproficiencycosts = parse(all[index++]);
        stealthcosts = parse(all[index++]);
        cookingspeedcosts = parse(all[index++]);
        smeltingspeedcosts = parse(all[index++]);
        mineralefficiencycosts = parse(all[index++]);
        greenthumbcosts = parse(all[index++]);
        fishingproficiencycosts = parse(all[index++]);
        manaRegencosts = parse(all[index++]);
        woodChoppingcosts = parse(all[index++]);

        // ===== REQUIREMENTS =====
        oneHandedRequiredLevel = buffer.readInt();
        fightingSpiritRequiredLevel = buffer.readInt();
        berserkerRequiredLevel = buffer.readInt();
        athleticsRequiredLevel = buffer.readInt();
        quickRepositionRequiredLevel = buffer.readInt();
        snowWalkerRequiredLevel = buffer.readInt();
        scholarRequiredLevel = buffer.readInt();
        hagglerRequiredLevel = buffer.readInt();
        obsidianSmasherRequiredLevel = buffer.readInt();
        treasureHunterRequiredLevel = buffer.readInt();
        convergenceRequiredLevel = buffer.readInt();
        safePortRequiredLevel = buffer.readInt();
        sidestepRequiredLevel = buffer.readInt();
    }

    public void toBytes(FriendlyByteBuf buffer) {
    	// General
        buffer.writeInt(this.aptitudeMaxLevel);
        buffer.writeInt(this.playersMaxGlobalLevel);

        // ===== ARRAYS → STRING =====
        String arrays = convertArraysToString(

                // ===== LEVELS =====
                attackPassiveLevels,
                movementSpeedPassiveLevels,
                projectileSpeedPassiveLevels,
                blockReachPassiveLevels,
                breakSpeedPassiveLevels,
                criticalChancePassiveLevels,
                luckPassiveLevels,
                damagereductionlevels,
                shieldproficiencylevels,
                stealthLevels,
                cookingspeedLevels,
                smeltingspeedLevels,
                mineralefficiencyLevels,
                greenthumbLevels,
                fishingproficiencyLevels,
                manaRegenLevels,
                woodChoppingPassiveLevels,

                // ===== COSTS =====
                attackdamagecosts,
                movementspeedcosts,
                projectilespeedcosts,
                blockreachcosts,
                breakSpeedcosts,
                criticalChancecosts,
                luckpassivecosts,
                damagereductioncosts,
                shieldproficiencycosts,
                stealthcosts,
                cookingspeedcosts,
                smeltingspeedcosts,
                mineralefficiencycosts,
                greenthumbcosts,
                fishingproficiencycosts,
                manaRegencosts,
                woodChoppingcosts
        );

        buffer.writeUtf(arrays);

        // ===== REQUIREMENTS =====
        buffer.writeInt(oneHandedRequiredLevel);
        buffer.writeInt(fightingSpiritRequiredLevel);
        buffer.writeInt(berserkerRequiredLevel);
        buffer.writeInt(athleticsRequiredLevel);
        buffer.writeInt(quickRepositionRequiredLevel);
        buffer.writeInt(snowWalkerRequiredLevel);
        buffer.writeInt(scholarRequiredLevel);
        buffer.writeInt(hagglerRequiredLevel);
        buffer.writeInt(obsidianSmasherRequiredLevel);
        buffer.writeInt(treasureHunterRequiredLevel);
        buffer.writeInt(convergenceRequiredLevel);
        buffer.writeInt(safePortRequiredLevel);
        buffer.writeInt(sidestepRequiredLevel);
    }

    private String convertArraysToString(int[]... arrays) {
        return Arrays.stream(arrays)
                .map(array -> Arrays.stream(array)
                        .mapToObj(String::valueOf)
                        .collect(Collectors.joining(",")))
                .collect(Collectors.joining("|"));
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
        	Minecraft instance = Minecraft.getInstance();
            LocalPlayer localPlayer = instance.player;
            if(localPlayer != null){
            	var config = HandlerCommonConfig.HANDLER.instance();

                // ===== GENERAL =====
                config.aptitudeMaxLevel = this.aptitudeMaxLevel;
                config.playersMaxGlobalLevel = this.playersMaxGlobalLevel;

                // ===== LEVELS =====
                config.attackPassiveLevels = this.attackPassiveLevels;
                config.movementSpeedPassiveLevels = this.movementSpeedPassiveLevels;
                config.projectileSpeedPassiveLevels = this.projectileSpeedPassiveLevels;
                config.blockReachPassiveLevels = this.blockReachPassiveLevels;
                config.breakSpeedPassiveLevels = this.breakSpeedPassiveLevels;
                config.criticalChancePassiveLevels = this.criticalChancePassiveLevels;
                config.luckPassiveLevels = this.luckPassiveLevels;
                config.damagereductionlevels = this.damagereductionlevels;
                config.shieldproficiencylevels = this.shieldproficiencylevels;
                config.stealthLevels = this.stealthLevels;
                config.cookingspeedLevels = this.cookingspeedLevels;
                config.smeltingspeedLevels = this.smeltingspeedLevels;
                config.mineralefficiencyLevels = this.mineralefficiencyLevels;
                config.greenthumbLevels = this.greenthumbLevels;
                config.fishingproficiencyLevels = this.fishingproficiencyLevels;
                config.manaRegenLevels = this.manaRegenLevels;
                config.woodChoppingPassiveLevels = this.woodChoppingPassiveLevels;

                // ===== COSTS =====
                config.attackdamagecosts = this.attackdamagecosts;
                config.movementspeedcosts = this.movementspeedcosts;
                config.projectilespeedcosts = this.projectilespeedcosts;
                config.blockreachcosts = this.blockreachcosts;
                config.breakSpeedcosts = this.breakSpeedcosts;
                config.criticalChancecosts = this.criticalChancecosts;
                config.luckpassivecosts = this.luckpassivecosts;
                config.damagereductioncosts = this.damagereductioncosts;
                config.shieldproficiencycosts = this.shieldproficiencycosts;
                config.stealthcosts = this.stealthcosts;
                config.cookingspeedcosts = this.cookingspeedcosts;
                config.smeltingspeedcosts = this.smeltingspeedcosts;
                config.mineralefficiencycosts = this.mineralefficiencycosts;
                config.greenthumbcosts = this.greenthumbcosts;
                config.fishingproficiencycosts = this.fishingproficiencycosts;
                config.manaRegencosts = this.manaRegencosts;
                config.woodChoppingcosts = this.woodChoppingcosts;

                // ===== REQUIREMENTS =====
                config.oneHandedRequiredLevel = this.oneHandedRequiredLevel;
                config.fightingSpiritRequiredLevel = this.fightingSpiritRequiredLevel;
                config.berserkerRequiredLevel = this.berserkerRequiredLevel;
                config.athleticsRequiredLevel = this.athleticsRequiredLevel;
                config.quickRepositionRequiredLevel = this.quickRepositionRequiredLevel;
                config.snowWalkerRequiredLevel = this.snowWalkerRequiredLevel;
                config.scholarRequiredLevel = this.scholarRequiredLevel;
                config.hagglerRequiredLevel = this.hagglerRequiredLevel;
                config.obsidianSmasherRequiredLevel = this.obsidianSmasherRequiredLevel;
                config.treasureHunterRequiredLevel = this.treasureHunterRequiredLevel;
                config.convergenceRequiredLevel = this.convergenceRequiredLevel;
                config.safePortRequiredLevel = this.safePortRequiredLevel;
                config.sidestepRequiredLevel = this.sidestepRequiredLevel;

                // Save client config
                HandlerCommonConfig.HANDLER.save();
            }
        });
        context.setPacketHandled(true);
    }

    public static void sendToPlayer(Player player) {
        ServerNetworking.sendToPlayer(new DynamicConfigSyncCP(), (ServerPlayer) player);
    }

    public static void sendToAllPlayers(){
        ServerNetworking.sendToAllClients(new DynamicConfigSyncCP());
    }

    private int[] parse(String data) {
        return Arrays.stream(data.split(","))
                .mapToInt(Integer::parseInt)
                .toArray();
    }
}
