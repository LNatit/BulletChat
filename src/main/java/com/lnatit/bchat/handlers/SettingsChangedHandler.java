package com.lnatit.bchat.handlers;

import com.lnatit.bchat.configs.ConfigManager;
import net.minecraft.client.gui.screens.options.ChatOptionsScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;

import static com.lnatit.bchat.BulletChat.MODID;

@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class SettingsChangedHandler
{
    @SubscribeEvent
    public static void onSettingsChanged(ScreenEvent.Closing event)
    {
        // DONE update config
        if (event.getScreen() instanceof ChatOptionsScreen)
        {
            ConfigManager.init(false);
        }
    }

    // Fired when you expand your windows
    @SubscribeEvent
    public static void onScreenInit(ScreenEvent.Init.Post event)
    {
        ConfigManager.init(false);
    }
}
