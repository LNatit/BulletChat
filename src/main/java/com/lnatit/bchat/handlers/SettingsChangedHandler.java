package com.lnatit.bchat.handlers;

import com.lnatit.bchat.components.BulletComponent;
import com.lnatit.bchat.components.ChatBadge;
import com.lnatit.bchat.configs.BlackListManager;
import com.lnatit.bchat.configs.BulletChatOptions;
import net.minecraft.client.gui.screens.ChatOptionsScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.lnatit.bchat.BulletChat.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class SettingsChangedHandler
{
    @SubscribeEvent
    public static void onSettingsChanged(ScreenEvent.Closing event)
    {
        // DONE update config
        if (event.getScreen() instanceof ChatOptionsScreen)
        {
            BulletChatOptions.init(false);
        }
    }

    // Fired when you expand your windows
    @SubscribeEvent
    public static void onScreenInit(ScreenEvent.Init.Post event)
    {
        BulletChatOptions.init(false);
    }
}
