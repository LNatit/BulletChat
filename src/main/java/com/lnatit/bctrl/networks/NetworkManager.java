package com.lnatit.bctrl.networks;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

import static com.lnatit.bctrl.BulletChatController.MODID;
import static net.minecraftforge.network.NetworkRegistry.ABSENT;

public class NetworkManager
{
    private static final String PROTOCOL_VER = "be33";

    private static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(MODID, "channel"),
                                                                                 () -> PROTOCOL_VER,
                                                                                  PROTOCOL_VER::equals,
                                                                                  ABSENT.version()::equals
    );

    public static void register()
    {
        CHANNEL.registerMessage(0, ControllerPacket.class,
                                ControllerPacket::encode,
                                ControllerPacket::new,
                                ControllerPacket::handle,
                                Optional.of(NetworkDirection.PLAY_TO_CLIENT)
        );
    }

    public static void sendUpdatePack(ServerPlayer player, ControllerPacket packet)
    {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), packet);
    }
}
