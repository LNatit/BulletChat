package com.lnatit.bchat.handlers;

import com.lnatit.bchat.components.BulletComponent;
import net.minecraft.client.gui.screens.ChatOptionsScreen;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.client.gui.screens.Screen;
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
        Screen screen = event.getScreen();
        if (screen instanceof LevelLoadingScreen || screen instanceof ChatOptionsScreen)
        {
            BulletComponent.INSTANCE.init();
        }
    }
}
