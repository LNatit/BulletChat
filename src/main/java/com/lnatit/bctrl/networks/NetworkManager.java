package com.lnatit.bctrl.networks;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Arrays;
import java.util.Optional;

import static com.lnatit.bctrl.BulletChatController.MODID;
import static com.lnatit.bctrl.BulletChatController.MODLOG;
import static net.minecraftforge.network.NetworkRegistry.ABSENT;

public class NetworkManager
{
    private static final String SERVER_VER = "h9c5";
    private static final String CLIENT_VER = "be33";

    private static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(MODID, "channel"),
            () -> SERVER_VER,
            SERVER_VER::equals,
            NetworkManager::serverHandShake
    );

    public static boolean serverHandShake(String clientVersion)
    {
        if (clientVersion.equals(ABSENT.version()))
            return true;
        else if (clientVersion.equals(CLIENT_VER))
            MODLOG.info("Client have Bullet Chat.");
        return clientVersion.equals(CLIENT_VER);
    }

    public static void register()
    {
        // no longer useful
//        CHANNEL.registerMessage(0, ControllerPacket.class,
//                                ControllerPacket::encode,
//                                ControllerPacket::new,
//                                ControllerPacket::handle,
//                                Optional.of(NetworkDirection.PLAY_TO_CLIENT)
//        );
    }

    @Deprecated
    public static void sendUpdatePack(ServerPlayer player, ControllerPacket packet)
    {
//        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), packet);
    }
}
