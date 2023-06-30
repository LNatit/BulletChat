package com.lnatit.bchat.handlers;

import com.lnatit.bchat.components.BulletComponent;
import com.lnatit.bchat.components.ChatBadge;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ChatReceivedHandler
{
    @SubscribeEvent
    public static void onChatReceived(ClientChatReceivedEvent event)
    {
        if (event instanceof ClientChatReceivedEvent.Player)
        {
            MutableComponent component = (MutableComponent) event.getMessage();
            if (component.getContents() instanceof TranslatableContents contents)
                BulletComponent.INSTANCE.addMessage(contents);
        }
        else
            ChatBadge.INSTANCE.setVisible(true);
    }
}
