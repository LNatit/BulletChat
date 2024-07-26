package com.lnatit.bchat.handlers;

import com.lnatit.bchat.components.BulletComponent;
import com.lnatit.bchat.components.ChatBadge;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;

import static com.lnatit.bchat.BulletChat.MODID;
import static com.lnatit.bchat.BulletChat.MODLOG;

@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class PlayerQuitHandler
{
    @SubscribeEvent
    public static void onPlayerLoggedOut(ClientPlayerNetworkEvent.LoggingOut event)
    {
        if (event.getPlayer() == null)
            return;

        BulletComponent.INSTANCE.clearMessages(true);
        ChatBadge.INSTANCE.setVisible(false);
        MODLOG.info("Bullets' all Clear~");
    }
}
