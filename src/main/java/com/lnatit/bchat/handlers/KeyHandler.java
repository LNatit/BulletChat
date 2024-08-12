package com.lnatit.bchat.handlers;

import com.lnatit.bchat.configs.AdvancedSettingsManager;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;

import static com.lnatit.bchat.BulletChat.MODID;

@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class KeyHandler
{
    public static final KeyMapping ADVANCED_SETTINGS =
            new KeyMapping("key.bchat.advanced_settings",
                           KeyConflictContext.IN_GAME,
                           InputConstants.Type.KEYSYM.getOrCreate(InputConstants.KEY_HOME),
                           "key.categories.bchat"
            );

    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class RegistryHandler
    {
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(ADVANCED_SETTINGS);
        }
    }

    @SubscribeEvent
    public static void onKeyPressed(InputEvent.Key event) {
        if (ADVANCED_SETTINGS.isDown()) {
            AdvancedSettingsManager.openAdvancedFile();
        }
    }
}
