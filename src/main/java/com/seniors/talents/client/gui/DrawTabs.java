package com.seniors.talents.client.gui;

import com.seniors.talents.client.core.Tabs;
import com.seniors.talents.client.core.Utils;
import com.seniors.talents.client.screen.TalentsScreen;
import com.seniors.talents.registry.RegistryItems;
import com.seniors.talents.Talents;
import com.mojang.blaze3d.systems.RenderSystem;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.StandingAndWallBlockItem;

public class DrawTabs {
    public static final ResourceLocation TEXTURE = new ResourceLocation(Talents.MOD_ID, "textures/gui/container/tabs.png");
    // Vanilla advancement tabs: the ones on the left side of the window are 32 x 28, at v 64 (v 92 when selected)
    public static final ResourceLocation SIDE_TEXTURE = new ResourceLocation("textures/gui/advancements/tabs.png");
    public static final Minecraft client = Minecraft.getInstance();
    public static ArrayList<Tabs> tabList = new ArrayList<>();
    public static boolean isMouseCheck = false;
    public static boolean checkMouse = false;

    public static void render(GuiGraphics matrixStack, int mouseX, int mouseY, int textureWidth, int textureHeight, int recipe) {
        Screen screen = client.screen;
        if (client.player != null) {
            isMouseCheck = false;
            tabList = new ArrayList<>();
            tabList.add(new Tabs("inventory", Utils.playerHead(), new InventoryScreen(client.player), screen instanceof InventoryScreen, Component.translatable("container.inventory")));
            tabList.add(new Tabs("leveling", RegistryItems.LEVELING_BOOK.get().getDefaultInstance(), new TalentsScreen(), screen instanceof TalentsScreen, Component.translatable("screen.aptitude.title")));
        }
        for (int i = 0; i < tabList.size(); i++) {
            Tabs type = tabList.get(i);
            // A column down the left side of the window, so the top edge stays free for other mods' tabs.
            // The tab's right 4 pixels sit on the window's border, like the advancement screen's side tabs.
            int x = (client.getWindow().getGuiScaledWidth() - textureWidth) / 2 + recipe - 28;
            int y = (client.getWindow().getGuiScaledHeight() - textureHeight) / 2 + 4 + i * 30;
            renderWidget(matrixStack, type, x, y, mouseX, mouseY);
        }
    }

    public static void renderWidget(GuiGraphics matrixStack, Tabs type, int x, int y, int mouseX, int mouseY) {
        matrixStack.pose().pushPose();
        RenderSystem.enableBlend();
        // Middle side tab (u 32), selected one from the row below
        matrixStack.blit(SIDE_TEXTURE, x, y, 32, type.isScreen() ? 92 : 64, 32, 28);
        float scale = (type.getItemStack().getItem() instanceof StandingAndWallBlockItem) ? 1.125F : 1.0F;
        // Icon centred in the part of the tab outside the window (28 x 28)
        float newX = (x + 14.0F - 8.0F) / scale;
        float newY = (y + 14.0F - 8.0F) / scale;
        matrixStack.pose().pushPose();
        matrixStack.pose().scale(scale, scale, 1.0F);
        matrixStack.renderItem(type.getItemStack(), (int) newX, (int) newY);
        matrixStack.pose().popPose();
        matrixStack.pose().popPose();

        // Only the part outside the window is clickable, so clicks on the window's border reach the screen
        if (Utils.checkMouse(x, y, mouseX, mouseY, 28, 28) && !type.isScreen()) {
            Utils.drawToolTip(matrixStack, type.getComponentName(), mouseX, mouseY);
            isMouseCheck = true;
            if (checkMouse) {
                setScreen(tabList.indexOf(type));
                checkMouse = false;
            }
        }
    }

    public static void setScreen(int i) {
        Utils.playSound();
        client.setScreen(tabList.get(i).getScreen());
    }

    public static void mouseClicked(int button) {
        if (button == 0 && isMouseCheck) checkMouse = true;
    }

    public static void onClose() {
        checkMouse = false;
    }
}


