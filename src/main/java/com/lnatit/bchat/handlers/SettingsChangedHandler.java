package com.lnatit.bchat.handlers;

import com.lnatit.bchat.components.BulletComponent;
import com.lnatit.bchat.components.ChatBadge;
import com.lnatit.bchat.configs.BulletChatConfig;
import net.minecraft.client.gui.screens.ChatOptionsScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class SettingsChangedHandler
{
    @SubscribeEvent
    public static void onSettingsChanged(ScreenEvent.Closing event)
    {
        // DONE update config
        if (event.getScreen() instanceof ChatOptionsScreen)
        {
            BulletChatConfig.init();
            BulletComponent.INSTANCE.init();
            ChatBadge.INSTANCE.init();
        }
    }

    // Fired when you expand your windows
    @SubscribeEvent
    public static void onScreenInit(ScreenEvent.Init.Post event)
    {
        BulletChatConfig.init();
        BulletComponent.INSTANCE.init();
        ChatBadge.INSTANCE.init();
    }
}
