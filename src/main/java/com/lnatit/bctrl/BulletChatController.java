package com.lnatit.bctrl;

import com.lnatit.bctrl.networks.NetworkManager;
import com.lnatit.bctrl.presets.BulletVisionFlatItem;
import com.lnatit.bctrl.presets.BulletVisionItem;
import com.lnatit.bctrl.presets.BulletControllerItem;
import com.mojang.logging.LogUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

import static com.lnatit.bctrl.BulletChatController.MODID;
import static net.minecraft.core.registries.Registries.CREATIVE_MODE_TAB;
import static net.minecraft.world.item.CreativeModeTabs.SPAWN_EGGS;

@Mod(MODID)
public class BulletChatController
{
    public static final String MODID = "bctrl";
    public static final Logger MODLOG = LogUtils.getLogger();

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(CREATIVE_MODE_TAB, MODID);

    public static final RegistryObject<BulletControllerItem> CONTROLLER =
            ITEMS.register(BulletControllerItem.ITEM_NAME, BulletControllerItem::new);
    public static final RegistryObject<BulletVisionItem> VISION =
            ITEMS.register(BulletVisionItem.ITEM_NAME, BulletVisionItem::new);
    public static final RegistryObject<BulletVisionFlatItem> VISION_FLAT =
            ITEMS.register(BulletVisionFlatItem.ITEM_NAME, BulletVisionFlatItem::new);

    public static final RegistryObject<CreativeModeTab> TAB =
            TABS.register(MODID,
                          () -> CreativeModeTab.builder()
                                               .withTabsBefore(SPAWN_EGGS.location())
                                               .icon(() -> VISION.get().getDefaultInstance())
                                               .title(Component.translatable("itemgroup." + MODID))
                                               .displayItems((p, o) -> {
                                                   o.accept(CONTROLLER.get());
                                                   o.accept(VISION_FLAT.get());
                                                   o.accept(VISION.get());
                                               })
                                               .build()
            );

    public BulletChatController()
    {
        if (FMLEnvironment.dist == Dist.DEDICATED_SERVER)
            NetworkManager.register();

        var bus = FMLJavaModLoadingContext.get().getModEventBus();

        ITEMS.register(bus);
        TABS.register(bus);
    }
}
