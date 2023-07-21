package com.lnatit.bctrl.networks;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

import static com.lnatit.bctrl.BulletChatController.MODID;

public class NetworkManager
{
    private static final String PROTOCOL_VER = "be33";

    private static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(MODID, "channel"),
                                                                                 () -> PROTOCOL_VER,
                                                                                 PROTOCOL_VER::equals,
                                                                                 PROTOCOL_VER::equals
    );

    private static int id = 0;

    public static void register()
    {
        CHANNEL.registerMessage(id++, SyncTagPacket.class,
                                SyncTagPacket::encode,
                                SyncTagPacket::new,
                                SyncTagPacket::handle,
                                Optional.of(NetworkDirection.PLAY_TO_CLIENT)
        );
    }

    public static void sendTagUpdate(ServerPlayer player, boolean add, String tag)
    {
        if (add)
            player.addTag(tag);
        else player.removeTag(tag);

        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new SyncTagPacket(add, tag));
    }
}
