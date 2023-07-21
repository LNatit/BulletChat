package com.lnatit.bchat.handlers;

import com.lnatit.bchat.configs.ConfigManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import static com.lnatit.bchat.BulletChat.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ConfigLoadedHandler
{
    @SubscribeEvent
    public static void onConfigLoaded(ModConfigEvent event)
    {
        if (event.getConfig().getModId().equals(MODID))
        {
            ConfigManager.init(true);
        }
    }
}
