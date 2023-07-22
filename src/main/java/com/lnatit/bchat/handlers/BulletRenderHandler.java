package com.lnatit.bchat.handlers;

import com.lnatit.bchat.components.BulletComponent;
import com.lnatit.bchat.components.ChatBadge;
import com.lnatit.bchat.configs.ConfigManager;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.lnatit.bchat.BulletChat.BulletChatClient.MINECRAFT;
import static com.lnatit.bchat.BulletChat.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class BulletRenderHandler
{
    /*
     DONE
     change to player_list to render bullet on top of chat_panel won't work
     set Z offset!!!
    */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onGuiRendered(RenderGuiOverlayEvent.Pre event)
    {
        if (event.getOverlay() != VanillaGuiOverlay.CHAT_PANEL.type() || ConfigManager.shouldRender())
            return;

        // Move to ModBusHandler
//        BulletComponent.INSTANCE.render(guiGraphics, partialTick);

        if (MINECRAFT.screen instanceof ChatScreen)
            ChatBadge.INSTANCE.setVisible(false);
        else if (ConfigManager.getHideChat())
        {
            event.setCanceled(true);
            ChatBadge.INSTANCE.render(event.getGuiGraphics());
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event)
    {
        if (event.phase == TickEvent.Phase.START)
            BulletComponent.INSTANCE.tick();
    }
}
