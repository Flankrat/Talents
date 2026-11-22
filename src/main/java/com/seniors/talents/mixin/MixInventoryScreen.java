package com.seniors.talents.mixin;

import com.seniors.talents.client.gui.DrawTabs;
import com.seniors.talents.integration.L2TabsIntegration;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({InventoryScreen.class})
public abstract class MixInventoryScreen extends EffectRenderingInventoryScreen<InventoryMenu> {
    @Unique
    public boolean this$checkMouse = false;

    public MixInventoryScreen(Player player) {
        super(player.inventoryMenu, player.getInventory(), Component.translatable("container.crafting"));
    }

    @Unique
    public boolean this$isMouseCheck = false;

    @Shadow
    public abstract RecipeBookComponent getRecipeBookComponent();

    @Inject(method = {"renderBg"}, at = {@At("TAIL")})
    private void render(GuiGraphics matrixStack, float delta, int mouseX, int mouseY, CallbackInfo info) {
        if(L2TabsIntegration.isModLoaded()){
            return;
        }

        DrawTabs.render(matrixStack, mouseX, mouseY, 176, 166, getRecipeBookComponent().isVisible() ? 77 : 0);

    }
    
    @Inject(method = {"mouseClicked"}, at = {@At("HEAD")})
    private void mouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> info) {
        if(L2TabsIntegration.isModLoaded()){
            return;
        }
        if (button == 0 && this.this$isMouseCheck) this.this$checkMouse = true;
        DrawTabs.mouseClicked(button);
    }

    public void onClose() {
        this.this$checkMouse = false;
        DrawTabs.onClose();
        super.onClose();
    }
}
