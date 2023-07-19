package com.lnatit.bchat.handlers;

import com.lnatit.bchat.components.BulletComponent;
import com.lnatit.bchat.components.ChatBadge;
import com.lnatit.bchat.configs.BulletChatConfig;
import com.lnatit.bchat.configs.BulletChatInitializer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.common.MinecraftForge;
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
    @SuppressWarnings("UnstableApiUsage")
    @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
    public static void onGuiRendered(RenderGuiOverlayEvent.Pre event)
    {
        // Check QuothI token
        if (MINECRAFT.player != null && MINECRAFT.player.getTags().contains(BulletChatConfig.SERVER_TOKEN.get()))
            return;

        if (event.getOverlay() != VanillaGuiOverlay.CHAT_PANEL.type())
            return;

        GuiGraphics guiGraphics = event.getGuiGraphics();
        float partialTick = event.getPartialTick();

        if (MINECRAFT.screen instanceof ChatScreen)
            ChatBadge.INSTANCE.setVisible(false);
        else if (BulletChatInitializer.getHideChat())
        {
            event.setCanceled(true);
            ChatBadge.INSTANCE.render(guiGraphics);

            // Post post event for compatibility
            MinecraftForge.EVENT_BUS.post(
                    new RenderGuiOverlayEvent.Post(MINECRAFT.getWindow(),
                                                   guiGraphics,
                                                   partialTick,
                                                   event.getOverlay()
                    ));
        }

        BulletComponent.INSTANCE.render(guiGraphics, partialTick);
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event)
    {
        BulletComponent.INSTANCE.tick();
    }
}
