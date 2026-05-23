package com.pigmyy.cookingmod.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import com.pigmyy.cookingmod.CookingMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class CauldronScreen extends AbstractContainerScreen<CauldronMenu> {
    // Textures / Directories
    private static final ResourceLocation GUI_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(CookingMod.MOD_ID, "textures/gui/cauldron/cauldron_gui.png");
    private static final ResourceLocation BAR_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(CookingMod.MOD_ID, "textures/gui/progress_bar.png");
    private static final ResourceLocation FIRE_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(CookingMod.MOD_ID, "textures/gui/lit_progress.png");
    private static final ResourceLocation FUEL_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(CookingMod.MOD_ID, "textures/gui/fuel_bar.png");

    public CauldronScreen(CauldronMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    // Render GUI
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F,  1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        pGuiGraphics.blit(GUI_TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        renderProgressBar(pGuiGraphics, x, y);
        renderFire(pGuiGraphics, x, y);
        renderFuel(pGuiGraphics, x, y);
    }
    // Render Progress Bar Animation
    private void renderProgressBar(GuiGraphics guiGraphics, int x, int y) {
        if(menu.isCooking()) {
            guiGraphics.blit(BAR_TEXTURE, x + 45, y + 40, 0, 0, menu.getScaledProgressBar(), 7, 87, 7);
        }

    }
    // Render Fire Animation
    private void renderFire(GuiGraphics guiGraphics, int x, int y) {
        if(menu.isCooking() && this.minecraft != null && this.minecraft.level != null) {
            long gameTicks = this.minecraft.level.getGameTime();
            int activeTicks = (int) (gameTicks % 14);
            int flameHeightFrame = activeTicks + 1;
            guiGraphics.blit(FIRE_TEXTURE, x + 81, y + 49 + (14 - flameHeightFrame), 0, 14 - flameHeightFrame, 14, flameHeightFrame, 14, 14);
        }
    }

    private void renderFuel(GuiGraphics guiGraphics, int x, int y) {
        int fuelWidth = menu.getScaledFuel();
        if (fuelWidth > 0) {
            guiGraphics.blit(FUEL_TEXTURE, x + 78, y + 63, 0, 0, fuelWidth, 6, 21, 6);
        }
    }



    // Render Labels
    protected void renderLabels(GuiGraphics pGuiGraphics, int mouseX, int MouseY) {
        String titleText = this.title.getString();

        int x = (this.imageWidth / 2) - (this.font.width(titleText) / 2);

        // this makes the Label for the Cauldron Centered in line with other GUI's
        pGuiGraphics.drawString(this.font, titleText, x, 6, 4210752, false);

        pGuiGraphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 4210752, false);
    }
}
