package com.lnatit.bchat.handlers;

import com.lnatit.bchat.components.BulletComponent;
import com.lnatit.bchat.components.ChatBadge;
import com.lnatit.bchat.configs.BlackListManager;
import com.lnatit.bchat.configs.BulletChatConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import static com.lnatit.bchat.BulletChat.MODID;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ConfigLoadedHandler
{
    @SubscribeEvent
    public static void onConfigLoaded(ModConfigEvent event)
    {
        if (event.getConfig().getModId().equals(MODID))
        {
            BulletChatConfig.init();
            BlackListManager.init();
            BulletComponent.INSTANCE.init();
            ChatBadge.INSTANCE.init();
        }
    }
}
