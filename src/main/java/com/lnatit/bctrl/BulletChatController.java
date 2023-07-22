package com.lnatit.bctrl;

import com.lnatit.bctrl.networks.NetworkManager;
import com.lnatit.bctrl.presets.BulletControllerHat;
import com.lnatit.bctrl.presets.BulletControllerItem;
import com.mojang.logging.LogUtils;
import net.minecraft.server.commands.TriggerCommand;
import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
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

@Mod(MODID)
public class BulletChatController
{
    public static final String MODID = "bctrl";
    public static final Logger MODLOG = LogUtils.getLogger();

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public static final RegistryObject<BulletControllerItem> CONTROLLER = ITEMS.register(BulletControllerItem.ITEM_NAME, BulletControllerItem::new);
    public static final RegistryObject<BulletControllerHat> HAT = ITEMS.register(BulletControllerHat.ITEM_NAME, BulletControllerHat::new);

    public BulletChatController()
    {
        if (FMLEnvironment.dist == Dist.DEDICATED_SERVER)
            NetworkManager.register();

        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
