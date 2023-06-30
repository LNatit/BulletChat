package com.lnatit.bchat.handlers;

import com.lnatit.bchat.components.BulletComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class BulletRenderHandler
{
    /*
     DONE
     change to player_list to render bullet on top of chat_panel won't work
     set Z offset!!!
    */
    @SubscribeEvent
    public static void onGuiRendered(RenderGuiOverlayEvent.Post event)
    {
        if (!event.getOverlay().id().toString().equals("minecraft:chat_panel"))
            return;
        BulletComponent.INSTANCE.render(event.getGuiGraphics(), event.getPartialTick());
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event)
    {
        BulletComponent.INSTANCE.tick();
    }
}
