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
    private static final ResourceLocation GUI_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(CookingMod.MOD_ID, "textures/gui/cauldron/cauldron_gui.png");
    private static final ResourceLocation BAR_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(CookingMod.MOD_ID, "textures/gui/progress_bar.png");

    public CauldronScreen(CauldronMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F,  1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        pGuiGraphics.blit(GUI_TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        renderProgressBar(pGuiGraphics, x, y);
    }

    private void renderProgressBar(GuiGraphics guiGraphics, int x, int y) {
        if(menu.isCooking()) {
            guiGraphics.blit(BAR_TEXTURE, x + 45, y + 40, 0, 0, menu.getScaledProgressBar(), 7, 87, 7);
        }
    }

    protected void renderLabels(GuiGraphics pGuiGraphics, int mouseX, int MouseY) {
        String titleText = this.title.getString();

        int x = (this.imageWidth / 2) - (this.font.width(titleText) / 2);

        pGuiGraphics.drawString(this.font, titleText, x, 6, 4210752, false);

        pGuiGraphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 4210752, false);
    }
}
