package com.seniors.talents.common.capability;

import com.seniors.talents.client.core.Aptitudes;
import com.seniors.talents.client.gui.OverlayAptitudeGui;
import com.seniors.talents.handler.HandlerAptitude;
import com.seniors.talents.handler.HandlerCommonConfig;
import com.seniors.talents.network.packet.client.AptitudeOverlayCP;
import com.seniors.talents.registry.*;
import com.seniors.talents.registry.aptitude.Aptitude;
import com.seniors.talents.registry.passive.Passive;
import com.seniors.talents.registry.skills.Skill;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AptitudeCapability implements INBTSerializable<CompoundTag> {
    public Map<String, Integer> aptitudeLevel = mapAptitudes();
    // learn by doing: XP (vanilla XP points) collected toward each aptitude's next level
    public Map<String, Double> aptitudeXp = new HashMap<>();
    public Map<String, Integer> passiveLevel = mapPassive();
    public Map<String, Boolean> toggleSkill = mapSkills();
    public double betterCombatEntityRange = 0.0D;

    public int counterAttackTimer = 0;
    public boolean counterAttack = false;

    private Map<String, Integer> mapAptitudes() {
        Map<String, Integer> map = new HashMap<>();
        List<Aptitude> aptitudeList = RegistryAptitudes.APTITUDES_REGISTRY.get().getValues().stream().toList();
        for (Aptitude aptitude : aptitudeList) {
            map.put(aptitude.getName(), 1);
        }
        return map;
    }

    private Map<String, Integer> mapPassive() {
        Map<String, Integer> map = new HashMap<>();
        List<Passive> passiveList = RegistryPassives.PASSIVES_REGISTRY.get().getValues().stream().toList();
        for (Passive passive : passiveList) {
            map.put(passive.getName(), 0);
        }
        return map;
    }

    private Map<String, Boolean> mapSkills() {
        Map<String, Boolean> map = new HashMap<>();
        List<Skill> skillList = RegistrySkills.SKILLS_REGISTRY.get().getValues().stream().toList();
        for (Skill skill : skillList) {
            map.put(skill.getName(), false);
        }
        return map;
    }

    public boolean getCounterAttack() {
        return this.counterAttack;
    }

    public void setCounterAttack(boolean set) {
        this.counterAttack = set;
    }

    public int getCounterAttackTimer() {
        return this.counterAttackTimer;
    }

    public void setCounterAttackTimer(int timer) {
        this.counterAttackTimer = timer;
    }

    @Nullable
    public static AptitudeCapability get(Player player) {
        LazyOptional<AptitudeCapability> capability = player.getCapability(RegistryCapabilities.APTITUDE);
        if(capability.isPresent() && capability.resolve().isPresent()){
            return capability.resolve().get();
        }

        return null;
    }

    @Nullable
    public static AptitudeCapability get() {
    	Minecraft instance = Minecraft.getInstance();
        LocalPlayer player = instance.player;
        if (player == null ) {
            return null;
        }
        return player.getCapability(RegistryCapabilities.APTITUDE).orElseThrow(() -> new IllegalArgumentException("Player does not have Capabilities!"));
    }

    public int getAptitudeLevel(Aptitude aptitude) {
        return this.aptitudeLevel.get(aptitude.getName());
    }

    public int getAptitudeLevel(String aptitudeName) {
        return this.aptitudeLevel.get(aptitudeName);
    }

    /** Learn by doing: XP collected toward this aptitude's next level (0 if none yet). */
    public double getAptitudeXp(Aptitude aptitude) {
        return this.aptitudeXp.getOrDefault(aptitude.getName(), 0.0);
    }

    public void setAptitudeXp(Aptitude aptitude, double xp) {
        this.aptitudeXp.put(aptitude.getName(), xp);
    }

    public void setAptitudeLevel(Aptitude aptitude, int lvl) {
        this.aptitudeLevel.put(aptitude.getName(), lvl);
    }

    public int getGlobalLevel(){
        return this.aptitudeLevel.values().stream().mapToInt(Integer::intValue).sum();
    }

    public void addAptitudeLevel(Aptitude aptitude, int addLvl) {
        this.aptitudeLevel.put(aptitude.getName(), Math.min(this.aptitudeLevel.get(aptitude.getName()) + addLvl, HandlerCommonConfig.HANDLER.instance().aptitudeMaxLevel));
    }

    public int getPassiveLevel(Passive passive) {
        return this.passiveLevel.get(passive.getName());
    }

    public void addPassiveLevel(Passive passive, int addLvl) {
        this.passiveLevel.put(passive.getName(), Math.min(this.passiveLevel.get(passive.getName()) + addLvl, passive.levelsRequired.length));
    }

    public void subPassiveLevel(Passive passive, int subLvl) {
        this.passiveLevel.put(passive.getName(), Math.max(this.passiveLevel.get(passive.getName()) - subLvl, 0));
    }

    public boolean getToggleSkill(Skill skill) {
        return this.toggleSkill.get(skill.getName());
    }

    public void setToggleSkill(Skill skill, boolean toggle) {
        this.toggleSkill.put(skill.getName(), toggle);
    }

    public boolean canUseItem(Player player, ItemStack item) {
        List<Aptitudes> lock = HandlerAptitude.getValue(item.getItem());
        if (meets(lock)) return true;
        warn(player, lock.get(0).getResource());
        return false;
    }

    /** Like canUseItem, but without the "locked" warning; for checks that run often or on the client. */
    public boolean canUseItemQuiet(ItemStack item) {
        return meets(HandlerAptitude.getValue(item.getItem()));
    }

    public boolean canUseItem(Player player, ResourceLocation resourceLocation) {
        return canUse(player, resourceLocation);
    }

    // Required for locking PointBlank
    public boolean canUseItemClient(ItemStack item) {
        return canUseClient(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item.getItem())));
    }

    public boolean canUseSpecificID(Player player, String specificID){
        return canUse(player, specificID);
    }

    public boolean canUseBlock(Player player, Block block) {
        return canUse(player, Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)));
    }

    public boolean canUseEntity(Player player, Entity entity) {
        return canUse(player, Objects.requireNonNull(ForgeRegistries.ENTITY_TYPES.getKey(entity.getType())));
    }

    private boolean canUse(Player player, ResourceLocation resource) {
        String id = resource.toString();
        if (meets(HandlerAptitude.getValue(id))) return true;
        warn(player, id);
        return false;
    }

    /** True when the player has every aptitude level the lock asks for (or there is no lock). */
    private boolean meets(@Nullable List<Aptitudes> lock) {
        if (lock == null) return true;
        for (Aptitudes aptitudes : lock) {
            if (getAptitudeLevel(aptitudes.getAptitude()) < aptitudes.getAptitudeLvl()) return false;
        }
        return true;
    }

    // last "locked" warning sent, so the same item isn't re-sent every check (curio slots and the crafting grid
    // check the same item many times in one click)
    private String lastWarning = "";
    private int lastWarningTick = Integer.MIN_VALUE;

    /** Shows the "locked" overlay for an id, at most once a second for the same id. Server side only. */
    private void warn(Player player, String id) {
        if (!(player instanceof net.minecraft.server.level.ServerPlayer)) return;
        if (id.equals(this.lastWarning) && player.tickCount - this.lastWarningTick < 20 && player.tickCount >= this.lastWarningTick) return;
        this.lastWarning = id;
        this.lastWarningTick = player.tickCount;
        AptitudeOverlayCP.send(player, id);
    }

    // Required for locking PointBlank
    private boolean canUseClient(ResourceLocation resource) {
        List<Aptitudes> aptitude = HandlerAptitude.getValue(resource.toString());
        if (aptitude != null) {
            for (Aptitudes aptitudes : aptitude) {
                if (getAptitudeLevel(aptitudes.getAptitude()) < aptitudes.getAptitudeLvl()) {
                    OverlayAptitudeGui.showWarning(resource.toString());
                    return false;
                }
            }
        }
        return true;
    }

    private boolean canUse(Player player, String restrictionID) {
        if (meets(HandlerAptitude.getValue(restrictionID))) return true;
        warn(player, restrictionID);
        return false;
    }

    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        for (Aptitude aptitude : RegistryAptitudes.APTITUDES_REGISTRY.get().getValues().stream().toList()){
            nbt.putInt("aptitude." + aptitude.getName(), this.aptitudeLevel.get(aptitude.getName()));
            nbt.putDouble("aptitudeXp." + aptitude.getName(), getAptitudeXp(aptitude));
        }
        for (Passive passive : RegistryPassives.PASSIVES_REGISTRY.get().getValues().stream().toList()){
            nbt.putInt("passive." + passive.getName(), this.passiveLevel.get(passive.getName()));
        }
        for (Skill skill : RegistrySkills.SKILLS_REGISTRY.get().getValues().stream().toList()){
            nbt.putBoolean("skill." + skill.getName(), this.toggleSkill.get(skill.getName()));
        }
        nbt.putInt("counterAttackTimer", this.counterAttackTimer);
        nbt.putBoolean("counterAttack", this.counterAttack);
        nbt.putDouble("betterCombatEntityRange", this.betterCombatEntityRange);
        return nbt;
    }

    public void deserializeNBT(CompoundTag nbt) {
        for (Aptitude aptitude : RegistryAptitudes.APTITUDES_REGISTRY.get().getValues().stream().toList()){
            this.aptitudeLevel.put(aptitude.getName(), nbt.getInt("aptitude." + aptitude.getName()));
            this.aptitudeXp.put(aptitude.getName(), nbt.getDouble("aptitudeXp." + aptitude.getName()));   // 0 in old saves
        }
        for (Passive passive : RegistryPassives.PASSIVES_REGISTRY.get().getValues().stream().toList()){
            this.passiveLevel.put(passive.getName(), nbt.getInt("passive." + passive.getName()));
        }
        for (Skill skill : RegistrySkills.SKILLS_REGISTRY.get().getValues().stream().toList()){
            this.toggleSkill.put(skill.getName(), nbt.getBoolean("skill." + skill.getName()));
        }
        this.counterAttackTimer = nbt.getInt("counterAttackTimer");
        this.counterAttack = nbt.getBoolean("counterAttack");
        this.betterCombatEntityRange = nbt.getDouble("betterCombatEntityRange");
    }

    public void copyFrom(AptitudeCapability source) {
        for (Aptitude aptitude : RegistryAptitudes.APTITUDES_REGISTRY.get().getValues().stream().toList()){
            this.aptitudeLevel.put(aptitude.getName(), source.aptitudeLevel.get(aptitude.getName()));
            this.aptitudeXp.put(aptitude.getName(), source.getAptitudeXp(aptitude));
        }
        for (Passive passive : RegistryPassives.PASSIVES_REGISTRY.get().getValues().stream().toList()){
            this.passiveLevel.put(passive.getName(), source.passiveLevel.get(passive.getName()));
        }
        for (Skill skill : RegistrySkills.SKILLS_REGISTRY.get().getValues().stream().toList()){
            this.toggleSkill.put(skill.getName(), source.toggleSkill.get(skill.getName()));
        }
        this.counterAttackTimer = source.counterAttackTimer;
        this.counterAttack = source.counterAttack;
        this.betterCombatEntityRange = source.betterCombatEntityRange;
    }
}


