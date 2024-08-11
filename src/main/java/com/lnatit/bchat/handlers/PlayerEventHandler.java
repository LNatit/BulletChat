package com.lnatit.bchat.handlers;

import com.lnatit.bchat.components.BulletComponent;
import com.lnatit.bchat.components.ChatBadge;
import com.lnatit.bchat.configs.BulletChatConfig;
import com.lnatit.bchat.gui.BulletDisplayWarningScreen;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.ClientHooks;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import static com.lnatit.bchat.BulletChat.MODID;
import static com.lnatit.bchat.BulletChat.MODLOG;

@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class PlayerEventHandler
{
    @SubscribeEvent
    public static void onPlayerLoggedIn(ClientPlayerNetworkEvent.LoggingIn event)
    {
        if (BulletChatConfig.INSTANCE.displayMode.get() == BulletChatConfig.Mode.ALWAYS_ASK)
        {
            Minecraft.getInstance().execute(() -> ClientHooks.pushGuiLayer(Minecraft.getInstance(), new BulletDisplayWarningScreen()));
        }
    }

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
