package com.lnatit.bchat.handlers;

import com.lnatit.bchat.components.BulletComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ChatReceivedHandler
{
    @SubscribeEvent
    public static void onChatReceived(ClientChatReceivedEvent.Player event)
    {
        BulletComponent.INSTANCE.addMessage(event.getMessage());
    }
}
