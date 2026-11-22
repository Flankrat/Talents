package com.seniors.talents.client.core;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.seniors.talents.Talents;
import com.seniors.talents.TalentsClient;
import com.seniors.talents.client.gui.UnderShirtOverlayHandler;
import com.seniors.talents.common.capability.AptitudeCapability;
import com.seniors.talents.handler.HandlerCommonConfig;
import com.seniors.talents.network.ServerNetworking;
import com.seniors.talents.network.packet.common.DodgePacket;
import com.seniors.talents.network.packet.common.LedgeGrabPacket;
import com.seniors.talents.registry.RegistrySkills;
import com.seniors.talents.registry.RegistrySounds;
import com.seniors.talents.utils.ResourceLoader;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ClientSideEvents {

	public static int cd;
	@SuppressWarnings("removal")
	private static ResourceLocation undershirt_texture = new ResourceLocation(Talents.MOD_ID,"textures/overlay/damaged0.png");

	@SuppressWarnings("deprecation")
	@SubscribeEvent
	public static void onKeyInput(InputEvent.Key event) {
		Minecraft instance = Minecraft.getInstance();
		if (instance.isPaused()) return;
		LocalPlayer player = instance.player;
		//Ledge Grab ability
		if (player != null) {
			if (AptitudeCapability.get(player) == null) return;
			if (event.getKey() == GLFW.GLFW_KEY_SPACE && event.getAction() == GLFW.GLFW_PRESS) {
				if (player != null && !player.onGround() && player.level().getBlockState(player.blockPosition().above()).getFluidState().isEmpty() && RegistrySkills.LEDGE_GRAB.get().isEnabled(player)) {
					float yaw = (float)Math.toRadians(player.getYRot());
					double dx = -Math.sin(yaw);
			        double dz =  Math.cos(yaw);
					Vec3 look = new Vec3(dx , 0, dz);

					// Player base position (around feet)
					Vec3 base = player.position().add(0, 0.5, 0);

					// Check up to 0.75 blocks ahead for a solid block
					double checkDistance = 0.75;
					Vec3 ahead = base.add(look.scale(checkDistance));
					BlockPos posAhead = BlockPos.containing(ahead);
					BlockPos posAbove1 = posAhead.above();
					BlockPos posAbove2 = posAbove1.above();
					Level level = player.level();
					BlockState front = level.getBlockState(posAhead);
					BlockState up = level.getBlockState(posAbove1);
					BlockState up2 = level.getBlockState(posAbove2);
					boolean flag = false;
					if (!front.canBeReplaced() && front.isSolid()) { 
						if (up.canBeReplaced() || !up.isSolid()) {
							flag = up2.canBeReplaced() || !up2.isSolid();
						}
					}
					if (!player.getPersistentData().getBoolean("VaultUsed") && flag) {
						ServerNetworking.sendToServer(new LedgeGrabPacket());
						level.playSound(player, player.blockPosition(), RegistrySounds.LEDGE_GRAB.get(),
								SoundSource.PLAYERS);
						player.getPersistentData().putBoolean("VaultUsed", true);
						player.resetFallDistance();
					}
				} else if (player.onGround()) {
					player.getPersistentData().putBoolean("VaultUsed", false);
				}
			}
			//Dodge ability
			AptitudeCapability cap = AptitudeCapability.get(player);
			if (!cap.getToggleSkill(RegistrySkills.SIDESTEP.get())) return;
			if ((ResourceLoader.DodgeDenyEffect != null) && player.hasEffect(ResourceLoader.DodgeDenyEffect)) return;
			if (cd <= 0 && !player.isCrouching() && player.onGround() && player.level().getBlockState(player.blockPosition().above()).getFluidState().isEmpty()) {
				if (TalentsClient.DODGE_KEY.getKey().getValue() == event.getKey() && event.getAction() == GLFW.GLFW_PRESS) {
					var config = HandlerCommonConfig.HANDLER.instance();
            		if (instance.options.keyUp.isDown()) {
						short direction = 0;
						player.level().playSound(player, player.blockPosition(), RegistrySounds.LEDGE_GRAB.get(),
								SoundSource.PLAYERS, 1.0f, 2f);
						ServerNetworking.sendToServer(new DodgePacket(direction));
		            	cd = config.sidestepCooldown;
					} else if (instance.options.keyDown.isDown()) {
						short direction = 1;
						instance.player.level().playSound(player, player.blockPosition(), RegistrySounds.LEDGE_GRAB.get(),
								SoundSource.PLAYERS, 1.0f, 2f);
						ServerNetworking.sendToServer(new DodgePacket(direction));
		            	cd = config.sidestepCooldown;
					} else if (instance.options.keyLeft.isDown()) {
						short direction = 2;
						player.level().playSound(player, player.blockPosition(), RegistrySounds.LEDGE_GRAB.get(),
								SoundSource.PLAYERS, 1.0f, 2f);
						ServerNetworking.sendToServer(new DodgePacket(direction));
		            	cd = config.sidestepCooldown;
					} else if (instance.options.keyRight.isDown()) {
						short direction = 3;
						player.level().playSound(player, player.blockPosition(), RegistrySounds.LEDGE_GRAB.get(),
								SoundSource.PLAYERS, 1.0f, 2f);
						ServerNetworking.sendToServer(new DodgePacket(direction));
		            	cd = config.sidestepCooldown;
					} else {
						short direction = 0;
						player.level().playSound(player, player.blockPosition(), RegistrySounds.LEDGE_GRAB.get(),
								SoundSource.PLAYERS, 1.0f, 2f);
						ServerNetworking.sendToServer(new DodgePacket(direction));
		            	cd = config.sidestepCooldown;
					}
		        }
			}
		}
	}
    
    @SubscribeEvent
    public static void clientTick(ClientTickEvent event) {
        if (event.phase == Phase.END) {
            Minecraft instance = Minecraft.getInstance();
            if (instance != null && !instance.isPaused()) {
            	if (cd > 0) {
                	cd--;
                }
            	UnderShirtOverlayHandler.tick();
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void renderHUD(RenderGuiOverlayEvent.Post event) {
		Window res = event.getWindow();
		Minecraft mc = Minecraft.getInstance();
		var config = HandlerCommonConfig.HANDLER.instance();
		int xo = res.getGuiScaledWidth() / 2 - 30;
		int y = res.getGuiScaledHeight() / 2 + 20;
		GuiGraphics gui = event.getGuiGraphics();
		gui.pose().pushPose();
		RenderSystem.enableBlend();
		if (!mc.player.getAbilities().flying) {
			int width = Math.min((int) (((cd - event.getPartialTick()) * 2)*(60f/(float)config.sidestepCooldown)), 60);
			gui.setColor(1F, 1F, 1F, 1F);
			if (width > 0) {
				gui.fill(xo, y - 2, xo + 60, y - 1, 0x44000000);
				gui.fill(xo, y - 2, xo + width, y - 1, 0xFFFFFFFF);
			}
		}

		gui.setColor(1F, 1F, 1F, 1F);
		gui.pose().popPose();
	}
    
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void vignette(RenderGuiOverlayEvent.Pre event)
    {
    	Minecraft instance = Minecraft.getInstance();
        LocalPlayer entity = instance.player;
        int time = UnderShirtOverlayHandler.getTime(entity);
        if (time <= 0) return;
        if (event.getOverlay() == VanillaGuiOverlay.VIGNETTE.type())
        {
        	double width = event.getWindow().getWidth();
            double height = event.getWindow().getHeight();
            double scale = event.getWindow().getGuiScale();

            RenderSystem.disableDepthTest();
            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            // Set up shader and texture
            float opacity = time * 0.0025f;
            Vector4f color = new Vector4f(1f, 0f, 0f, opacity);
            RenderSystem.setShaderColor(color.x, color.y, color.z, color.w);
            RenderSystem.setShaderTexture(0, undershirt_texture);
            // Render vignette
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            Tesselator tesselator = Tesselator.getInstance();
            BufferBuilder bufferbuilder = tesselator.getBuilder();
            bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
            bufferbuilder.vertex(0.0D, height / scale, -90.0D).uv(0.0F, 1.0F).endVertex();
            bufferbuilder.vertex(width / scale, height / scale, -90.0D).uv(1.0F, 1.0F).endVertex();
            bufferbuilder.vertex(width / scale, 0.0D, -90.0D).uv(1.0F, 0.0F).endVertex();
            bufferbuilder.vertex(0.0D, 0.0D, -90.0D).uv(0.0F, 0.0F).endVertex();
            tesselator.end();
            RenderSystem.depthMask(true);
            RenderSystem.enableDepthTest();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.defaultBlendFunc();
        }
    }
    
    public static void triggerDodge() {
    	cd = HandlerCommonConfig.HANDLER.instance().sidestepCooldown;
    }
}