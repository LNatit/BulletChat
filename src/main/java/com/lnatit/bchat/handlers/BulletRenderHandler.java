package com.lnatit.bchat.handlers;

import com.lnatit.bchat.components.BulletComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.lnatit.bchat.BulletChat.MODLOG;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class BulletRenderHandler
{
    @SubscribeEvent
    public static void onGuiRendered(RenderGuiOverlayEvent.Post event)
    {
        // TODO change to next id to render bullet on top of chat
        if (!event.getOverlay().id().toString().equals("minecraft:chat_panel"))
            return;
        BulletComponent.INSTANCE.render(event.getGuiGraphics(), event.getPartialTick());
//        MODLOG.info(String.valueOf(event.getPartialTick()));
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event)
    {
        BulletComponent.INSTANCE.tick();
    }
}
