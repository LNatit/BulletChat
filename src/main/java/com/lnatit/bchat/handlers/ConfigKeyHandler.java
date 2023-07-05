package com.lnatit.bchat.handlers;

import com.lnatit.bchat.gui.BulletOptionsScreen;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.lnatit.bchat.BulletChat.BulletChatClient.MINECRAFT;
import static com.lnatit.bchat.BulletChat.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ConfigKeyHandler
{
    public static final KeyMapping CONFIG_KEY =
            new KeyMapping("key.bchat.config",
                           KeyConflictContext.IN_GAME,
                           InputConstants.Type.KEYSYM.getOrCreate(InputConstants.KEY_HOME),
                           "key.categories.bchat"
            );

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class RegistryHandler
    {
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event)
        {
            event.register(CONFIG_KEY);
        }
    }

    @SubscribeEvent
    public static void onKeyPressed(InputEvent.Key event)
    {
        if (CONFIG_KEY.isDown())
            MINECRAFT.setScreen(new BulletOptionsScreen(MINECRAFT.screen));
    }
}
