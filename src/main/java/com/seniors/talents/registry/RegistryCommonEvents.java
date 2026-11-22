package com.seniors.talents.registry;

import com.seniors.talents.common.capability.AptitudeCapability;
import com.seniors.talents.common.capability.LazyAptitudeCapability;
import com.seniors.talents.common.command.*;
import com.seniors.talents.handler.HandlerCommonConfig;
import com.seniors.talents.network.packet.client.*;
import com.seniors.talents.registry.passive.Passive;
import com.seniors.talents.registry.skills.ConvergenceSkill;
import com.seniors.talents.registry.skills.Skill;
import com.seniors.talents.registry.skills.TransmutationSkill;
import com.seniors.talents.registry.skills.TreasureHunterSkill;
import com.seniors.talents.utils.GreenThumbHandler;
import com.seniors.talents.utils.HungryFarmerHandler;
import com.seniors.talents.utils.LedgeGrabHandler;
import com.seniors.talents.utils.ModCompatsList;
import com.seniors.talents.utils.ResourceLoader;
import com.seniors.talents.utils.TalentsItemLocksHandler;

import atomicstryker.infernalmobs.common.SidedCache;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import com.kettle.pml.core.DamageControl;
import com.kettle.pml.events.DamageHandler;
import com.mojang.datafixers.util.Pair;
import com.seniors.talents.Talents;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags.Blocks;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.champions.common.util.ChampionHelper;
import xzeroair.trinkets.capabilities.magic.MagicStats;

import java.util.*;

/**
 * registry COMMON events into the forge mod bus.
 */
@Mod.EventBusSubscriber(modid = Talents.MOD_ID)
public class RegistryCommonEvents {
	
	private static final UUID SPRINTER_TALENT_UUID = UUID.fromString("0dd5da9c-b4cc-478f-93a9-0824905b793b");
	private static final AttributeModifier SPRINTER_TALENT = new AttributeModifier(SPRINTER_TALENT_UUID, "Sprinter", (double)0.2F, AttributeModifier.Operation.MULTIPLY_TOTAL);
	private static final UUID LUCKYFISHERMAN_TALENT_UUID = UUID.fromString("8fce454e-3d08-4f01-8c48-522a7cdf6efc");
	private static final AttributeModifier LUCKYFISHERMAN_TALENT = new AttributeModifier(LUCKYFISHERMAN_TALENT_UUID, "fisherman", (double)0.5F, AttributeModifier.Operation.ADDITION);
	private static final UUID ATTACK_FIX_UUID = UUID.fromString("4ef484df-7965-4f41-af76-a6d854d64a3f");
	@SuppressWarnings("removal")
	private static final TagKey<Item> Ingots = TagKey.create(Registries.ITEM, new ResourceLocation("forge", "ingots"));
	public static final Map<UUID, Long> disrupted = new HashMap<>();
	public static final HashMap<Player, Integer> DODGE_CD = new HashMap<>();
	
	@SubscribeEvent
	public static void onTagsUpdated(TagsUpdatedEvent event) {
		HungryFarmerHandler.load();
		if (event.getUpdateCause() == TagsUpdatedEvent.UpdateCause.SERVER_DATA_LOAD) {
	        TalentsItemLocksHandler.loadLocks();
	    }
	}

    @SubscribeEvent
    public static void onPlayerLoggedInEvent(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        if (!player.level().isClientSide()) {
            if (player instanceof ServerPlayer serverPlayer && !(player instanceof FakePlayer)) {
                ConfigSyncCP.sendToPlayer(serverPlayer);
                CommonConfigSyncCP.sendToPlayer(serverPlayer);
                DynamicConfigSyncCP.sendToPlayer(serverPlayer);
                ResourceLoader.load();
            }
        }
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        AptitudeLevelCommand.register(event.getDispatcher());
        AptitudesReloadCommand.register(event.getDispatcher());
        RegisterItem.register(event.getDispatcher());
        GlobalLimitCommand.register(event.getDispatcher());
        UpdateAptitudeLevelCommand.register(event.getDispatcher());
    }

    @SuppressWarnings("removal")
	@SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player && !(event.getObject() instanceof FakePlayer)) {
            AptitudeCapability AptitudeCapability = new AptitudeCapability();
            LazyAptitudeCapability lazyAptitudeCapability = new LazyAptitudeCapability(AptitudeCapability);
            event.addCapability(new ResourceLocation(Talents.MOD_ID, "aptitudes"), lazyAptitudeCapability);
        }
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(AptitudeCapability.class);
    }

	@SubscribeEvent
	public static void onPlayerRespawnedSyncPlayerVariables(PlayerEvent.PlayerRespawnEvent event) {
		Player player = event.getEntity();
        if (!player.level().isClientSide()) {
            if (player instanceof ServerPlayer serverPlayer && !(player instanceof FakePlayer)) {
                SyncAptitudeCapabilityCP.send(serverPlayer);
                RegistryAttributes.modifierAttributes(serverPlayer);
            }
        }
	}

	@SubscribeEvent
	public static void onPlayerChangedDimensionSyncPlayerVariables(PlayerEvent.PlayerChangedDimensionEvent event) {
		Player player = event.getEntity();
        if (!player.level().isClientSide()) {
            if (player instanceof ServerPlayer serverPlayer && !(player instanceof FakePlayer)) {
                SyncAptitudeCapabilityCP.send(serverPlayer);
                RegistryAttributes.modifierAttributes(serverPlayer);
            }
        }
	}
    
    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        Player player = event.getEntity();
        if (player instanceof ServerPlayer serverPlayerNew) {
            player = event.getOriginal();
            if (player instanceof ServerPlayer serverPlayerOld) {
                serverPlayerOld.reviveCaps();
                serverPlayerOld.getCapability(RegistryCapabilities.APTITUDE).ifPresent((oldAbilities) -> {
                    serverPlayerNew.getCapability(RegistryCapabilities.APTITUDE).ifPresent((newAbilities) -> {
                        newAbilities.copyFrom(oldAbilities);
                    });
                });
                RegistryAttributes.modifierAttributes(serverPlayerNew);
                if (!serverPlayerOld.isDeadOrDying()) {
                    serverPlayerNew.setHealth(serverPlayerOld.getHealth());
                } else {
                    serverPlayerNew.setHealth(serverPlayerOld.getMaxHealth());
                }
                RegistryAttributes.modifierAttributes(serverPlayerOld);
                serverPlayerOld.invalidateCaps();
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerJoinWorld(EntityJoinLevelEvent event) {
        if (!event.getLevel().isClientSide()) {
            Entity entity = event.getEntity();
            if (entity instanceof FakePlayer) return;
            if (entity instanceof ServerPlayer serverPlayer) {
                SyncAptitudeCapabilityCP.send(serverPlayer);
                RegistryAttributes.modifierAttributes(serverPlayer);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        Player player = event.getEntity();
        if (player.isCreative() || player instanceof FakePlayer) return;
        ItemStack item = event.getItemStack();
        Block block = event.getLevel().getBlockState(event.getPos()).getBlock();
        AptitudeCapability provider = AptitudeCapability.get(player);
        if (provider == null) {
            return;
        }

        ResourceLocation location = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item.getItem()));

        if ((!provider.canUseItem(player, location) || !provider.canUseBlock(player, block))) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        if (player.isCreative() || player instanceof FakePlayer) return;
        ItemStack item = event.getItemStack();
        Block block = event.getLevel().getBlockState(event.getPos()).getBlock();
        AptitudeCapability provider = AptitudeCapability.get(player);
        if (provider == null) {
            return;
        }

        ResourceLocation location = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item.getItem()));

		// Food is only locked when it would place a block here (planting a crop), not
		// when it's eaten
		boolean checkItem = !item.isEdible() || placesBlock(event, item);
		if ((checkItem && !provider.canUseItem(player, location)) || !provider.canUseBlock(player, block)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        if (player.isCreative() || player instanceof FakePlayer) return;
        ItemStack item = event.getItemStack();
        AptitudeCapability provider = AptitudeCapability.get(player);
        if (provider == null) {
            return;
        }
     // Eating isn't locked: food locks only apply to planting it (see onRightClickBlock)
        if (item.isEdible()) return;
        ResourceLocation location = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item.getItem()));
        FoodProperties props = item.getFoodProperties(player);
        if (!provider.canUseItem(player, location) && (props == null || props.getNutrition() == 0 || props.getSaturationModifier() == 0f)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRightClickEntity(PlayerInteractEvent.EntityInteract event) {
        Player player = event.getEntity();
        if (player.isCreative() || player instanceof FakePlayer) return;
        Entity entity = event.getTarget();
        ItemStack item = event.getItemStack();
        AptitudeCapability provider = AptitudeCapability.get(player);
        if (provider == null) {
            return;
        }

        ResourceLocation location = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item.getItem()));

        if (!provider.canUseEntity(player, entity) || !provider.canUseItem(player, location)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onChangeEquipment(LivingEquipmentChangeEvent event) {
        LivingEntity livingEntity = event.getEntity();
        if (livingEntity instanceof Player player) {
            if (!player.isCreative() && event.getSlot().getType() == EquipmentSlot.Type.ARMOR) {
                AptitudeCapability provider = AptitudeCapability.get(player);
                // Check added due some mods make changes to the equipment before the player fully connects
                // So if the player haven't fully connect to the server yet, it won't be able to find its capabilities.
                if(provider == null) return;
                ItemStack item = event.getTo();

                if (!provider.canUseItem(player, item)) {
                    player.drop(item.copy(), false);
                    item.setCount(0);
                }
            }
        }
    }

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		Player player = event.player;
		if (event.phase == TickEvent.Phase.END && player != null) {
			if (DODGE_CD.containsKey(player) && event.side == LogicalSide.SERVER) {
				int dodge_cd = DODGE_CD.get(player) - 1;
				if (dodge_cd <= 0) {
					DODGE_CD.remove(player);
				} else {
					DODGE_CD.put(player, dodge_cd);
				}
			}
			if (!player.isCreative()) {
				if (HandlerCommonConfig.HANDLER.instance().dropLockedItems) {
					player.getCapability(RegistryCapabilities.APTITUDE).ifPresent(aptitudeCapability -> {
						AptitudeCapability provider = AptitudeCapability.get(player);
						if (provider == null) {
							return;
						}

						ItemStack hand = player.getMainHandItem();
						ItemStack offHand = player.getOffhandItem();
						if (!provider.canUseItem(player, hand)) {
							player.drop(hand.copy(), false);
							hand.setCount(0);
						}
						if (!provider.canUseItem(player, offHand)) {
							player.drop(offHand.copy(), false);
							offHand.setCount(0);
						}
					});
				}
			}
			if (player instanceof ServerPlayer serverPlayer) {
				RegistryAttributes.modifierAttributes(serverPlayer);
				LedgeGrabHandler.tickPlayer(serverPlayer);
				if (serverPlayer.getHealth() > serverPlayer.getMaxHealth())
					serverPlayer.setHealth(serverPlayer.getMaxHealth());

			}
			if (event.side == LogicalSide.SERVER) {
				AttributeInstance speed = player.getAttribute(Attributes.MOVEMENT_SPEED);
				if (AptitudeCapability.get(player) != null) {
					if (RegistrySkills.SPRINTER.get().isEnabled(player) && player.isSprinting()) {
						if (!speed.hasModifier(SPRINTER_TALENT)) {
							speed.addTransientModifier(SPRINTER_TALENT);
						}
					} else if (speed.hasModifier(SPRINTER_TALENT)) {
						speed.removeModifier(SPRINTER_TALENT);
					}
					AttributeInstance luck = player.getAttribute(Attributes.LUCK);
					if (RegistrySkills.LUCKY_FISHERMAN.get().isEnabled(player)
							&& player.getMainHandItem().getItem() instanceof FishingRodItem) {
						if (!luck.hasModifier(LUCKYFISHERMAN_TALENT)) {
							luck.addTransientModifier(LUCKYFISHERMAN_TALENT);
						}
					} else if (luck.hasModifier(LUCKYFISHERMAN_TALENT)) {
						luck.removeModifier(LUCKYFISHERMAN_TALENT);
					}
					if (player.tickCount % 10 == 0) { // those skills and passives run each half a second
						if (RegistrySkills.GOLDEN_OSMOSIS.get().isEnabled(player)) {
							if (player.totalExperience >= 3) {
								ItemStack mainHand = player.getMainHandItem();
								if (isGolden(mainHand)) {
									if (mainHand.getDamageValue() > 0) {
										mainHand.setDamageValue(mainHand.getDamageValue() - 9);
										player.giveExperiencePoints(-3);
									}
								}
								ItemStack offHand = player.getOffhandItem();
								if (isGolden(offHand)) {
									if (offHand.getDamageValue() > 0) {
										offHand.setDamageValue(offHand.getDamageValue() - 9);
										player.giveExperiencePoints(-3);
									}
								}
								for (ItemStack stack : player.getArmorSlots()) {
									if (isGolden(stack)) {
										if (stack.getDamageValue() > 0) {
											stack.setDamageValue(stack.getDamageValue() - 9);
											player.giveExperiencePoints(-3);
										}
									}
								}
							}
						}
						
					}
					if (player.tickCount % 20 == 0) { // passives and skills that run each second
						if (RegistrySkills.HUNGRY_FARMER.get().isEnabled(player)) {
							FoodData food = player.getFoodData();
							if (food.needsFood()) {
								Inventory inv = player.getInventory();

								for (int i = 0; i < inv.getContainerSize(); i++) {
									ItemStack stack = inv.getItem(i);
									if (!stack.isEmpty() && stack.isEdible()) {
										Item item = stack.getItem();
										if (HungryFarmerHandler.blacklist.contains(item))
											continue;
										// Get the food properties (base values)
										FoodProperties props = item.getFoodProperties(stack, player);
										if (props == null || props.getNutrition() == 0
												|| props.getSaturationModifier() == 0f)
											continue;

										// Consume one item
										ItemStack copy = stack.copy();
										item.finishUsingItem(stack, player.level(), player);
										ForgeEventFactory.onItemUseFinish(player, copy, i, stack);

										// Play eating sound & animation
										player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
												SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 1.0F, 1.0F);

										// Done after eating one food
										break;
									}
								}
							}
						}
						if (RegistryPassives.GREEN_THUMB.get().getLevel(player) > 0) {
							Passive greenthumb = RegistryPassives.GREEN_THUMB.get();
							float ratio = (float) (greenthumb.getLevel(player) * greenthumb.getLevelRatio());
							if (player.getRandom().nextFloat() <= GreenThumbHandler.chance * ratio) {
								Level level = player.level();
								float radius = HandlerCommonConfig.HANDLER.instance().greenthumbrange * ratio;
								AABB box = new AABB(player.getX() - radius, player.getY() - radius, player.getZ() - radius,
										player.getX() + radius, player.getY() + radius, player.getZ() + radius);

								List<BlockPos> crops = new ArrayList<>();

								// Search for growable blocks (like crops, stems, etc.)
								BlockPos.betweenClosedStream(box).forEach(pos -> {
									BlockState state = level.getBlockState(pos);
									Block block = state.getBlock();

									if (block instanceof CropBlock crop && !crop.isMaxAge(state)) {
										crops.add(pos.immutable());
									}
								});

								if (!crops.isEmpty()) {
									// Pick a random crop to grow
									BlockPos target = crops.get(player.getRandom().nextInt(crops.size()));
									BlockState state = level.getBlockState(target);

									if (state.getBlock() instanceof CropBlock crop) {
										crop.growCrops(level, target, state);
										level.levelEvent(2005, target, 0); // happy villager particles
									}
								}

							}
						}
						if (ModCompatsList.isTrinketsandBaublesOn) {
							if (RegistryPassives.MANA_REGEN.get().getLevel(player) > 0) {
								Passive manaRegen = RegistryPassives.MANA_REGEN.get();
								MagicStats mana = MagicStats.get(player);
								if (mana != null) {
									if (mana.getMana() < mana.getMaxMana()) {
										float regen = (float)(HandlerCommonConfig.HANDLER.instance().manaRegenvalue * manaRegen.getLevel(player) * manaRegen.getLevelRatio());
										mana.addMana(regen);
									}
								}
							}
						}
					}
					AttributeInstance attack = player.getAttribute(Attributes.ATTACK_SPEED);
					if (RegistrySkills.ONE_HANDED.get().isEnabled(player) && player.getOffhandItem().isEmpty()) {
						double value = (double) HandlerCommonConfig.HANDLER.instance().oneHandedAmplifier;
						if (attack.getModifier(ATTACK_FIX_UUID) == null) {
							AttributeModifier ATTACK_FIX = new AttributeModifier(ATTACK_FIX_UUID, "onehanded", value,
									AttributeModifier.Operation.MULTIPLY_BASE);
							attack.addTransientModifier(ATTACK_FIX);
						}
					} else if (attack.getModifier(ATTACK_FIX_UUID) != null) {
						attack.removeModifier(ATTACK_FIX_UUID);
					}
				}
			}
		}
	}

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPlayerAttackEntity(AttackEntityEvent event) {
        Player player = event.getEntity();
        if (player instanceof FakePlayer) return;
        if (player != null) {
            AptitudeCapability provider = AptitudeCapability.get(player);
            if (!player.isCreative() && provider != null) {
                ItemStack item = player.getMainHandItem();

                if (!provider.canUseItem(player, item)) {
                	event.setCanceled(true);
                }
            }

            if(event.isCanceled()){
                return;
            }
        }
    }

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onPlayerMining(PlayerEvent.BreakSpeed event) {
		Player player = event.getEntity();
		if (player instanceof FakePlayer)
			return;
		ItemStack tool = player.getMainHandItem();
		BlockState state = event.getState();
		// the passive's bonus only (was originalSpeed * (1 + attribute), which also
		// added the base speed again)
		float bonus = event.getOriginalSpeed() * (float) player.getAttributeValue(RegistryAttributes.BREAK_SPEED.get());
		if (tool.getItem() instanceof PickaxeItem && state.is(BlockTags.MINEABLE_WITH_PICKAXE)) {
			if (state.is(RegistryTags.Blocks.OBSIDIAN)) {
				if (RegistrySkills.OBSIDIAN_SMASHER != null
						&& RegistrySkills.OBSIDIAN_SMASHER.get().isEnabled(player)) {
					event.setNewSpeed(
							(float) (event.getNewSpeed() * RegistrySkills.OBSIDIAN_SMASHER.get().getValue()[0])
									+ bonus);
				}
			} else {
				event.setNewSpeed(event.getNewSpeed() + bonus);
			}
		}
		float wood = event.getOriginalSpeed() * (float) player.getAttributeValue(RegistryAttributes.WOOD_SPEED.get());
		if (tool.getItem() instanceof AxeItem && state.is(BlockTags.MINEABLE_WITH_AXE)) {
			event.setNewSpeed(event.getNewSpeed() + wood);
		}
	}

	@SubscribeEvent
	public static void onteleportdeny(EntityTeleportEvent.EnderEntity event) {
	   if (event.isCanceled()) return;
	   LivingEntity entity = event.getEntityLiving();
	   Long until = disrupted.get(entity.getUUID());
	   if (until == null) return;
	   if (entity.level().getGameTime() < until) event.setCanceled(true);
	   else disrupted.remove(entity.getUUID());
	}

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPlayerCriticalHit(CriticalHitEvent event) {
        Player player = event.getEntity();
        if (!event.isCanceled() && event.getDamageModifier() >= 1.5f) {
			if (RegistrySkills.TELEPORT_DISRUPTION.get().isEnabled(player)) {

				if (player.getRandom().nextBoolean()) {
					Entity target = event.getTarget();
					disrupted.put(target.getUUID(), target.level().getGameTime() + 80L); // 4 seconds
				}
			}
		}
        if (player != null) {
            if (player instanceof FakePlayer) return;

            if (RegistrySkills.BERSERKER != null && RegistrySkills.BERSERKER.isPresent()){
                if (RegistrySkills.BERSERKER.get().isEnabled(player) && player.getHealth() <= player.getMaxHealth() * (float) (RegistrySkills.BERSERKER.get().getValue()[0] / 100.0D)) {
                    float newDamage = event.getDamageModifier();
                    if (player.onGround() || player.isInWater()) {
                        event.setResult(Event.Result.ALLOW);
                        event.setDamageModifier(newDamage * 1.5F);
                    }
                }
            }

    	    
        }
    }
    
    @SubscribeEvent
    public static void onDisruptedDeath(LivingDeathEvent event) {
       disrupted.remove(event.getEntity().getUUID());
    }

	@SubscribeEvent(priority = EventPriority.LOW)
	public static void onRandomCrit(CriticalHitEvent event) {
		if (event.getResult() != Result.DENY && event.isVanillaCritical()) {
			Player player = event.getEntity();
			float attribute = (float) player.getAttributeValue(RegistryAttributes.CRITICAL_CHANCE.get());
			if (player.getRandom().nextFloat() <= attribute) {
				event.setResult(Result.ALLOW);
				event.setDamageModifier(event.getDamageModifier() * 1.5F);
			}
		}
	}

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onAttackEntity(LivingHurtEvent event) {
    	if (!event.isCanceled() && event.getEntity() instanceof Player player
				&& player.getAttribute(RegistryAttributes.DAMAGE_REDUCTION.get()) != null
				&& event.getSource().getEntity() instanceof LivingEntity) {

			DamageControl controller = DamageHandler.getDamageControl(event.getSource());
			float flatFromAdditions = (float) player.getAttribute(RegistryAttributes.DAMAGE_REDUCTION.get())
					.getModifiers(AttributeModifier.Operation.ADDITION).stream()
					.mapToDouble(AttributeModifier::getAmount).sum();
			// apply flat reduction
			controller.addBasePercentBonus(-Mth.clamp(flatFromAdditions, 0, 1));
		}
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPlayerShootArrow(ProjectileImpactEvent event) {
        Projectile projectile = event.getProjectile();
        if (projectile instanceof Arrow arrow) {
            Entity entity = projectile.getOwner();
            if (entity instanceof ServerPlayer serverPlayer) {
                if (event.getRayTraceResult().getType() == HitResult.Type.ENTITY) {
                	if (RegistrySkills.QUICK_REPOSITION != null && RegistrySkills.QUICK_REPOSITION.get().isEnabled(serverPlayer)) {
                		Skill quick_rep = RegistrySkills.QUICK_REPOSITION.get();
                		serverPlayer.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, (int)(10.0D + 20.0D * quick_rep.getValue()[1]), (Math.max(0, (int) (quick_rep.getValue()[0] - 1)))));
                	}
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onArrowNockEvent(ArrowNockEvent event) {
        Player player = event.getEntity();
        if(player == null){
            return;
        }
        ItemStack projectile = player.getProjectile(event.getBow());

        AptitudeCapability provider = AptitudeCapability.get(player);
        if (provider != null && !provider.canUseItem(player, projectile)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPlayerTeleport(EntityTeleportEvent.EnderPearl event) {
        if (event.getEntity() != null) {
            Entity entity = event.getEntity();
            if (entity instanceof Player player) {
                if (RegistrySkills.SAFE_PORT != null && RegistrySkills.SAFE_PORT.get().isEnabled(player)) event.setAttackDamage(0.0F);
            }
        }
    }
    
    @SubscribeEvent
	public static void onProjectileJoin(EntityJoinLevelEvent event) {
		if (!event.isCanceled() && event.getEntity() instanceof Projectile arrow && arrow.getOwner() instanceof Player) {
			Player owner = (Player) arrow.getOwner();
			if (owner.getAttribute(RegistryAttributes.PROJECTILE_SPEED.get()) != null) {
				float increase = (float) owner.getAttribute(RegistryAttributes.PROJECTILE_SPEED.get())
						.getModifiers(AttributeModifier.Operation.ADDITION).stream()
						.mapToDouble(AttributeModifier::getAmount).sum() + 1;
				Vec3 vector = new Vec3((arrow.getDeltaMovement().x()*increase), (arrow.getDeltaMovement().y() * increase), (arrow.getDeltaMovement().z() * increase));
				arrow.setDeltaMovement(vector);
			}
		}
	}
    
    @SubscribeEvent
    public static void onPlayerInteract(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        BlockEntity be = level.getBlockEntity(event.getPos());
        Player player = event.getEntity();
        BlockState state = level.getBlockState(event.getPos());
    	Block block = state.getBlock();
        if (be instanceof AbstractFurnaceBlockEntity furnace) {
            // Store the player UUID or name in the BlockEntity
            furnace.getPersistentData().putUUID("LastInteractingPlayer", event.getEntity().getUUID());
        }
        if (AptitudeCapability.get(player) == null) return;
        if (!event.isCanceled() && RegistrySkills.TRANSMUTATION.get().isEnabled(player)) {
        	ItemStack transformator = player.getMainHandItem();
        	if (transformator.is(TransmutationSkill.transmutator)) {
        		Block result = null;
        		for (Pair<Block,Block> pair : TransmutationSkill.CHORUS_TRANSMUTATIONS) {
        			if (pair.getFirst() == block) {
        				result = pair.getSecond();
        				break;
        			}
        		}
        		if (result != null) {
        			level.setBlockAndUpdate(event.getPos(), result.defaultBlockState());
        		}
        	}
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPlayerBreakBlock(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        Level level = (Level) event.getLevel();
        BlockState state = event.getState();
        if (RegistrySkills.TREASURE_HUNTER != null && player != null &&
                event.getState().is(RegistryTags.Blocks.DIRT) && RegistrySkills.TREASURE_HUNTER.get().isEnabled(player)) {
            BlockPos pos = event.getPos();
            ItemStack stack = TreasureHunterSkill.drop();
            if (stack != null) {
                ItemEntity itemEntity = new ItemEntity(level, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, stack);
                level.addFreshEntity(itemEntity);
            }
        }
        Block block = state.getBlock();
        if (block instanceof CropBlock crop && RegistrySkills.BOUNTIFUL_HARVEST.get().isEnabled(player)) {
            // Ensure the crop is mature
            if (!crop.isMaxAge(state)) return;
            ServerLevel serverWorld = (ServerLevel) level; // Ensure it's a server-level world
            List<ItemStack> drops = Block.getDrops(state, serverWorld, event.getPos(), null, player, player.getMainHandItem());
            if (player.getRandom().nextFloat() <= HandlerCommonConfig.HANDLER.instance().bountifulharvestchance) {
            	for (ItemStack drop : drops) {
                    if (drop.getItem() == block.asItem()) {
                    	continue;
                    }
                    drop.setCount(1);
                    ItemEntity itemtype = new ItemEntity(serverWorld, event.getPos().getX() + 0.5, event.getPos().getY() + 0.5, event.getPos().getZ() + 0.5, drop);

                        // Spawn the entity in the world
                    serverWorld.addFreshEntity(itemtype);
                }
            }
		}
		if (state.is(Blocks.ORES_COAL) && RegistrySkills.FOSSIL_DIGGER.get().isEnabled(player)) {
			ServerLevel serverWorld = (ServerLevel) level; // Ensure it's a server-level world
			if (player.getRandom().nextFloat() <= HandlerCommonConfig.HANDLER.instance().fossilDiggerchance) {
				ItemStack mainhand = player.getMainHandItem();
				if (!mainhand.isEmpty() && mainhand.getMaxDamage() >= 250) { //iron tools durability starts at 250 so higher tiers should be higher than this value
					ItemStack extracoal = new ItemStack(Items.COAL, 1);
					ItemEntity itemtype = new ItemEntity(serverWorld, event.getPos().getX() + 0.5,
							event.getPos().getY() + 0.5, event.getPos().getZ() + 0.5, extracoal);
					serverWorld.addFreshEntity(itemtype);
				}
			}
		}
    }
    
    @SubscribeEvent
	public static void StealthTargetReduction(LivingChangeTargetEvent event) {
		if (event.getOriginalTarget() instanceof Player player && player.isCrouching()
				&& event.getEntity() instanceof Enemy && player.getAttribute(RegistryAttributes.STEALTH.get()) != null) {
			float flatFromAdditions = (float) player.getAttribute(RegistryAttributes.STEALTH.get())
					.getModifiers(AttributeModifier.Operation.ADDITION).stream()
					.mapToDouble(AttributeModifier::getAmount).sum();
			LivingEntity mob = event.getEntity();
			float vision = (float) mob.getAttributeValue(Attributes.FOLLOW_RANGE) * (1 - flatFromAdditions);
			if (mob.distanceToSqr(player) >= vision * vision) {
				event.setNewTarget(null);
			}
		}
	}


    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPlayerCraft(PlayerEvent.ItemCraftedEvent event) {
        Player player = event.getEntity();
        if (player != null && RegistrySkills.CONVERGENCE != null) {
            if (player instanceof FakePlayer) return; // Probably not needed
            int randomizer = (int) Math.floor(Math.random() * RegistrySkills.CONVERGENCE.get().getValue()[0]);
            if (RegistrySkills.CONVERGENCE.get().isEnabled(player) && (RegistrySkills.CONVERGENCE.get().getValue()[0] >= 100 || randomizer == 1)) {
                ItemStack convergenceItem = ConvergenceSkill.drop(event.getCrafting());
                if (convergenceItem != null) {
                    player.drop(convergenceItem, false);
                }
            }
        }
    }
    

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onEntityDrops(LivingDropsEvent event) {
        if (event.getEntity() != null) {
            if (!(event.getEntity() instanceof Player)) {
                Entity entity1 = event.getSource().getEntity();
                if (entity1 instanceof Player player) {
                	if (RegistrySkills.FIGHTING_SPIRIT != null && RegistrySkills.FIGHTING_SPIRIT.get().isEnabled(player)) {
                		Skill fighting = RegistrySkills.FIGHTING_SPIRIT.get();
                		if (event.getEntity() instanceof Enemy) {
                    		player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, (int)(10.0D + 20.0D * fighting.getValue()[1]), (Math.max(0, (int) (fighting.getValue()[0] - 1)))));
                    	}
                	}
                }
            }
        }
        if (event.getEntity().level().isClientSide()) return;

        // Check if killed by a player
        if (!(event.isRecentlyHit()) || !(event.getEntity() instanceof Enemy)) return;
        if (!(event.getEntity().getKillCredit() instanceof Player player) || AptitudeCapability.get(player) == null) return;
        LivingEntity mob = event.getEntity();
        if (RegistrySkills.DROP_GUARANTEED.get().isEnabled(player)) {
            if (event.getDrops().isEmpty()) {
                ServerLevel serverLevel = (ServerLevel) mob.level();

                // Get the mob's loot table
                ResourceLocation lootTableId = mob.getLootTable();
                LootTable lootTable = serverLevel.getServer().getLootData().getLootTable(lootTableId);

                // Build context for the loot table
                LootParams.Builder contextBuilder = new LootParams.Builder(serverLevel)
                        .withParameter(LootContextParams.THIS_ENTITY, mob)
                        .withParameter(LootContextParams.ORIGIN, mob.position())
                        .withParameter(LootContextParams.DAMAGE_SOURCE, event.getSource())
                        .withOptionalParameter(LootContextParams.DIRECT_KILLER_ENTITY, player)
                        .withOptionalParameter(LootContextParams.LAST_DAMAGE_PLAYER, player).withLuck(player.getLuck());

                LootParams context = contextBuilder.create(LootContextParamSets.ENTITY);
                // Add drops to world
				for (int roll = 0; roll < 10; roll++) {
					ObjectArrayList<ItemStack> items = lootTable.getRandomItems(context);
					if (items.isEmpty())
						continue;
					for (ItemStack stack : items)
						mob.spawnAtLocation(stack);
					break;
				}
            }
        }
		if (RegistrySkills.BOUNTY_HUNTER.get().isEnabled(player)
				&& player.getRandom().nextFloat() <= HandlerCommonConfig.HANDLER.instance().bountyHunterchance) {
			if (HandlerCommonConfig.HANDLER.instance().bountyhunterCompat) {
				int amount = 0;
				if (ModCompatsList.isSDon) {
					if (ModCompatsList.isBlight(mob)) {
						System.out.println("Simple Difficulty is detected as loaded");
						amount += player.getRandom().nextIntBetweenInclusive(1,
								HandlerCommonConfig.HANDLER.instance().bountyHunterMaxEmeralds);
					}
				}
				if (ModCompatsList.isChampionsOn) {
					if (ChampionHelper.isChampionEntity(mob)) {
						System.out.println("Champions is detected as loaded");
						amount += player.getRandom().nextIntBetweenInclusive(1,
								HandlerCommonConfig.HANDLER.instance().bountyHunterMaxEmeralds);
					}
				}
				if (ModCompatsList.isInfernalOn) {
					if (SidedCache.getInfernalMobs(player.level()).containsKey(mob) ) {
						System.out.println("Infernal Mobs is detected as loaded");
						amount += player.getRandom().nextIntBetweenInclusive(1,
								HandlerCommonConfig.HANDLER.instance().bountyHunterMaxEmeralds);
					}
				}
				if (amount != 0) {
					ItemStack emerald = new ItemStack(Items.EMERALD, amount);
					mob.spawnAtLocation(emerald);
				}
			} else {
				System.out.println("compat is not enable for bountyhunter");
				ItemStack emerald = new ItemStack(Items.EMERALD, player.getRandom().nextIntBetweenInclusive(1,
						HandlerCommonConfig.HANDLER.instance().bountyHunterMaxEmeralds));
				mob.spawnAtLocation(emerald);
			}
		}
    }
    
    @SubscribeEvent
    public static void onItemFished(ItemFishedEvent event) {
        Player player = event.getEntity();
        Level level = player.level();
        FishingHook hook = event.getHookEntity();
        ItemStack fishingrod = player.getMainHandItem();
        int enchlevel = EnchantmentHelper.getFishingLuckBonus(fishingrod);
        if (level.isClientSide()) return;

        if (AptitudeCapability.get(player) == null) return;
        Passive fishing_passive = RegistryPassives.FISHING_PROFICIENCY.get();
        float fishing_proficiency = (float)(fishing_passive.getLevel(player) * fishing_passive.getLevelRatio());
        if (fishing_proficiency <= 0) return;

        // Chance increases with enchantment level
        RandomSource random = level.getRandom();
        float chance = HandlerCommonConfig.HANDLER.instance().fishingproficiencyvalue * fishing_proficiency;
        if (random.nextFloat() > chance) return;

        // Build a context for fishing loot table
        ServerLevel serverLevel = (ServerLevel) level;
        LootParams lootparams = (new LootParams.Builder(serverLevel)).withParameter(LootContextParams.ORIGIN, hook.position()).withParameter(LootContextParams.TOOL, fishingrod).withParameter(LootContextParams.THIS_ENTITY, hook).withParameter(LootContextParams.KILLER_ENTITY, player).withParameter(LootContextParams.THIS_ENTITY, hook).withLuck(enchlevel + player.getLuck()).create(LootContextParamSets.FISHING);
        LootTable junktable = level.getServer().getLootData().getLootTable(BuiltInLootTables.FISHING_JUNK);
        LootTable treasuretable = level.getServer().getLootData().getLootTable(BuiltInLootTables.FISHING_TREASURE);
        List<ItemStack> list = new ArrayList<>();
        list.addAll(junktable.getRandomItems(lootparams));
        list.addAll(treasuretable.getRandomItems(lootparams));
        if (list.isEmpty()) return;
        ItemStack selected = list.get(random.nextInt(list.size()));
        // Drop it at hook location with vanilla-like physics
        ItemEntity entity = new ItemEntity(level, hook.getX(), hook.getY(), hook.getZ(), selected.copy());
        double dx = player.getX() - hook.getX();
        double dy = player.getY() - hook.getY();
        double dz = player.getZ() - hook.getZ();
        entity.setDeltaMovement(dx * 0.1D, dy * 0.1D + Math.sqrt(Math.sqrt(dx * dx + dy * dy + dz * dz)) * 0.08D, dz * 0.1D);
        level.addFreshEntity(entity);
        player.displayClientMessage(Component.translatable("passive.talents.fishing_proficiency.message"), true);
    }
	
	@SubscribeEvent
	public static void onPlayerSmelting(PlayerEvent.ItemSmeltedEvent event) {
		if (!event.isCanceled() && event.getEntity() != null && event.getSmelting().is(Ingots) && !event.getEntity().level().isClientSide()) {
			if (RegistryPassives.MINERAL_EFFICIENCY != null) {
				Passive mineralefficiency = RegistryPassives.MINERAL_EFFICIENCY.get();
				Player player = event.getEntity();
				if (AptitudeCapability.get(player) == null) return;
				if (mineralefficiency.getLevel(player) > 0) {
					float value = (float)(HandlerCommonConfig.HANDLER.instance().mineralefficiencyvalue * (mineralefficiency.getLevel(player) * mineralefficiency.getLevelRatio()));
					if (value > 0) {
						ItemStack bonus = event.getSmelting().copy();
						int extra = 0;
						RandomSource random = event.getEntity().getRandom();
						for (int i = 0; i < bonus.getCount(); i++) {
							if (random.nextFloat() <= value) {
								extra++;
							}
						}
						bonus.setCount(extra);
						if (extra > 0) {
							if (!player.getInventory().add(bonus)) {
					            player.drop(bonus, false);
					        }
						}
					}
				}
			}
		}
	}

    public static void enqueueTask(Level world, Runnable task, int delay) {
        if (!(world instanceof ServerLevel)) {
            return;
        }

        MinecraftServer server = ((ServerLevel) world).getServer();
        server.submit(new TickTask(server.getTickCount() + delay, task));
    }
    
    private static boolean isGolden(ItemStack stack) {
	    Item item = stack.getItem();

	    if (item instanceof TieredItem tiered) {
	        return tiered.getTier() == Tiers.GOLD;
	    }

	    if (item instanceof ArmorItem armor) {
	        return armor.getMaterial() == ArmorMaterials.GOLD;
	    }

	    return false;
	}
    
	private static boolean placesBlock(PlayerInteractEvent.RightClickBlock event, ItemStack item) {
		if (!(item.getItem() instanceof BlockItem blockItem))
			return false;
		BlockPlaceContext context = new BlockPlaceContext(event.getEntity(), event.getHand(), item, event.getHitVec());
		if (!context.canPlace())
			return false;
		BlockState state = blockItem.getBlock().getStateForPlacement(context);
		return state != null && state.canSurvive(event.getLevel(), context.getClickedPos());
	}
}