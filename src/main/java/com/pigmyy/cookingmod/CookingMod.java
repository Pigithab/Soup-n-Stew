package com.pigmyy.cookingmod;

import com.mojang.logging.LogUtils;
import com.pigmyy.cookingmod.block.ModBlocks;
import com.pigmyy.cookingmod.block.custom.Cauldron;
import com.pigmyy.cookingmod.block.entity.ModBlockEntities;
import com.pigmyy.cookingmod.item.ModCreativeModeTabs;
import com.pigmyy.cookingmod.item.ModItems;
import com.pigmyy.cookingmod.recipe.ModRecipes;
import com.pigmyy.cookingmod.screen.ModMenuTypes;
import com.pigmyy.cookingmod.screen.custom.CauldronScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CookingMod.MOD_ID)
public class CookingMod {
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "cookingmod";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public CookingMod(FMLJavaModLoadingContext context)    {
        IEventBus modEventBus = context.getModEventBus();
        modEventBus.addListener(this::commonSetup);
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);


        ModCreativeModeTabs.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);

        ModMenuTypes.register(modEventBus);
        ModRecipes.register(modEventBus);


        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);
        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    
    private void commonSetup(final FMLCommonSetupEvent event)    {

    }
    private void addCreative(BuildCreativeModeTabContentsEvent event)    {

        // Adds the ModItems to their respective Creative Categories
        if(event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS) {
            event.accept(ModItems.FISH_STEW);
            event.accept(ModItems.VEGETABLE_STEW);
            event.accept(ModItems.ROTTEN_STEW);
            event.accept(ModItems.PUMPKIN_SOUP);
            event.accept(ModItems.PORK_STEW);
            event.accept(ModItems.BEEF_STEW);
            event.accept(ModItems.CHICKEN_STEW);
            event.accept(ModBlocks.CAULDRON);
        }

        if(event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(ModBlocks.CAULDRON);
        }

    }


    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)    {

    }


    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            MenuScreens.register(ModMenuTypes.CAULDRON_MENU.get(), CauldronScreen::new);
        }
        @SubscribeEvent
        public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
            event.register(
                    (state, level, pos, tintIndex) -> {
                        var soup = state.getValue(Cauldron.SOUPTYPE);

                        return switch (soup) {
                            case WATER      -> 0x3F76E4;
                            case MUSHROOM   -> 0xC78C65;
                            case RABBIT     -> 0x9D724C;
                            case BEETROOT   -> 0x8C1616;
                            case FISH       -> 0xBCC2BA;
                            case PORK       -> 0xD1A396;
                            case BEEF       -> 0x704A33;
                            case ROTTEN     -> 0x4D5E35;
                            case CHICKEN    -> 0xD3CCAA;
                            case PUMPKIN    -> 0xD16A19;
                            case VEGETABLE  -> 0xBC5843;
                        };



                    },
                    ModBlocks.CAULDRON.get()
            );
        }
    }
}
