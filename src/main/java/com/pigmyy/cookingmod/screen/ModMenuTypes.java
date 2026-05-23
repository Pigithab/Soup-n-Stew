package com.pigmyy.cookingmod.screen;

import com.pigmyy.cookingmod.CookingMod;
import com.pigmyy.cookingmod.screen.custom.CauldronMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, CookingMod.MOD_ID);

    public static final RegistryObject<MenuType<CauldronMenu>> CAULDRON_MENU =
            MENUS.register("cauldron_name", () -> IForgeMenuType.create(CauldronMenu::new));

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
