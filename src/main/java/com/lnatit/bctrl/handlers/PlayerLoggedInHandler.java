package com.lnatit.bctrl.handlers;

import com.lnatit.bctrl.networks.ControllerPacket;
import com.lnatit.bctrl.networks.NetworkManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.lnatit.bctrl.BulletChatController.MODID;

@Mod.EventBusSubscriber(modid = MODID, value = Dist.DEDICATED_SERVER)
public class PlayerLoggedInHandler
{
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event)
    {
        NetworkManager.sendUpdatePack((ServerPlayer) event.getEntity(), new ControllerPacket(false, (byte) 0));
    }
}
