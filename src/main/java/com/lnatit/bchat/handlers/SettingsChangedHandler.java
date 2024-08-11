package com.lnatit.bchat.handlers;

import com.lnatit.bchat.configs.BulletChatConfig;
import net.minecraft.client.gui.screens.options.ChatOptionsScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;

import static com.lnatit.bchat.BulletChat.MODID;

@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class SettingsChangedHandler
{
    // refer to ModBusHandler::onConfigLoaded
//    @SubscribeEvent
//    public static void onSettingsChanged(ScreenEvent.Closing event)
//    {
//        // DONE update config
//        if (event.getScreen() instanceof ConfigurationScreen.ConfigurationSectionScreen)
//        {
//            BulletChatConfig.init(false);
//        }
//    }

    // Fired when you expand your windows
    @SubscribeEvent
    public static void onScreenInit(ScreenEvent.Init.Post event)
    {
        BulletChatConfig.init(false);
    }
}
