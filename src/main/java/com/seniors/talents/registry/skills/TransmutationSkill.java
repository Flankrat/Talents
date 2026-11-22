package com.seniors.talents.registry.skills;

import java.util.ArrayList;
import java.util.List;

import com.mojang.datafixers.util.Pair;
import com.seniors.talents.client.core.Value;
import com.seniors.talents.registry.aptitude.Aptitude;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

public class TransmutationSkill extends Skill {

	public static List<Pair<Block, Block>> CHORUS_TRANSMUTATIONS = new ArrayList<>();
	public static Item transmutator; 
	
	public TransmutationSkill(ResourceLocation skillKey, Aptitude aptitude, int levelRequirement,
			ResourceLocation skillTexture, Value... skillValues) {
		super(skillKey, aptitude, levelRequirement, skillTexture, skillValues);
		// TODO Auto-generated constructor stub
	}

	@Override
	public MutableComponent getMutableDescription(String description) {
        return Component.translatable(description, transmutator.getDescription());
    }
	
	public static void loadTransmutations(List<? extends String> list) {
	    CHORUS_TRANSMUTATIONS.clear();
	    for (String entry : list) {
	        String[] parts = entry.split(";");
	        if (parts.length == 2) {
	        	ResourceLocation firstitem = new ResourceLocation(parts[0]);
	        	ResourceLocation seconditem = new ResourceLocation(parts[1]);
	        	if (firstitem != null && seconditem != null) {
	        		Block from = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(parts[0]));
		            Block to = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(parts[1]));
		            if (from != null && to != null) {
		                CHORUS_TRANSMUTATIONS.add(Pair.of(from, to));
		            }
	        	}
	            
	        }
	    }
	}
	
	public static void setTransmutator(String string) {
		ResourceLocation itemloc = new ResourceLocation(string);
		if (itemloc != null) {
			Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(string));
			if (item != null) {
				transmutator = item;
			} else {
				transmutator = Items.CHORUS_FRUIT;
			}
		}
	}
}
