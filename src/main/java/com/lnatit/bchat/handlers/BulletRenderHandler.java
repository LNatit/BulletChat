package com.lnatit.bchat.handlers;

import com.lnatit.bchat.components.BulletComponent;
import com.lnatit.bchat.components.ChatBadge;
import com.lnatit.bchat.configs.BulletChatConfig;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.lnatit.bchat.BulletChat.BulletChatClient.MINECRAFT;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class BulletRenderHandler
{
    /*
     DONE
     change to player_list to render bullet on top of chat_panel won't work
     set Z offset!!!
    */
    @SubscribeEvent
    public static void onGuiRendered(RenderGuiOverlayEvent.Pre event)
    {
        if (!event.getOverlay().id().toString().equals("minecraft:chat_panel"))
            return;

        if (MINECRAFT.screen instanceof ChatScreen)
            ChatBadge.INSTANCE.setVisible(false);
        else if (BulletChatConfig.getHideChat())
        {
            event.setCanceled(true);
            ChatBadge.INSTANCE.render(event.getGuiGraphics());
        }

        BulletComponent.INSTANCE.render(event.getGuiGraphics(), event.getPartialTick());
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event)
    {
        BulletComponent.INSTANCE.tick();
    }
}
