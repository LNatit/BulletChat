package com.lnatit.bchat.handlers;

import com.lnatit.bchat.components.BulletComponent;
import com.lnatit.bchat.components.ChatBadge;
import com.lnatit.bchat.configs.BulletChatConfig;
import net.minecraft.client.gui.screens.ChatScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

import static com.lnatit.bchat.BulletChat.BulletChatClient.MINECRAFT;
import static com.lnatit.bchat.BulletChat.MODID;

@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class BulletRenderHandler
{
    /*
     DONE
     change to player_list to render bullet on top of chat_panel won't work
     set Z offset!!!
    */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onGuiRendered(RenderGuiLayerEvent.Pre event)
    {
        if (event.getName() != VanillaGuiLayers.CHAT)
            return;

        // Move to ModBusHandler
//        BulletComponent.INSTANCE.render(guiGraphics, partialTick);

        if (MINECRAFT.screen instanceof ChatScreen)
            ChatBadge.INSTANCE.setVisible(false);
        else if (BulletChatConfig.INSTANCE.shouldHideChat())
        {
            event.setCanceled(true);
            ChatBadge.INSTANCE.render(event.getGuiGraphics());
        }
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Pre event)
    {
        BulletComponent.INSTANCE.tick();
    }
}
