
package com.pigmyy.cookingmod.item;

import com.pigmyy.cookingmod.CookingMod;
import com.pigmyy.cookingmod.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CookingMod.MOD_ID);


    // Custom Creative Tab for all Mod Related Items
    public static final RegistryObject<CreativeModeTab> COOKING_ITEMS_TAB = CREATIVE_MODE_TABS.register("cooking_items_tab", () ->
            CreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.CAULDRON.get()))
                    .title(Component.translatable("creativetab.cookingmod.cooking_items"))
                    .displayItems((itemDisplayParameters, output) -> {
                        // items in the tab
                        output.accept(ModBlocks.CAULDRON.get());
                        output.accept(Items.WATER_BUCKET);
                        output.accept(Items.COAL);

                        output.accept(ModItems.FISH_STEW.get());
                        output.accept(ModItems.PORK_STEW.get());
                        output.accept(ModItems.BEEF_STEW.get());
                        output.accept(ModItems.ROTTEN_STEW.get());
                        output.accept(ModItems.CHICKEN_STEW.get());
                        output.accept(ModItems.PUMPKIN_SOUP.get());
                        output.accept(ModItems.VEGETABLE_STEW.get());
                        output.accept(Items.MUSHROOM_STEW);
                        output.accept(Items.RABBIT_STEW);
                        output.accept(Items.BEETROOT_SOUP);

                        output.accept(Items.BROWN_MUSHROOM);
                        output.accept(Items.RED_MUSHROOM);
                        output.accept(Items.BEETROOT);
                        output.accept(Items.POTATO);
                        output.accept(Items.CARROT);
                        output.accept(Items.SALMON);
                        output.accept(Items.COD);
                        output.accept(Items.BEEF);
                        output.accept(Items.PORKCHOP);
                        output.accept(Items.CHICKEN);
                        output.accept(Items.RABBIT);
                        output.accept(Items.PUMPKIN);
                        output.accept(Items.HONEY_BOTTLE);
                        output.accept(Items.SUGAR);
                        output.accept(Items.ROTTEN_FLESH);



                    }).build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }


}
