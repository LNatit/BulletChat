package com.lnatit.bctrl.networks;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.slf4j.Logger;

import java.util.Optional;

import static net.minecraftforge.network.NetworkRegistry.ABSENT;

public class NetworkManager
{
    public static final String CHANNEL_ID = "bctrl";
    public static final Logger CHANNEL_LOG = LogUtils.getLogger();

    private static final String PROTOCOL_VER = "be33";

    private static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(CHANNEL_ID, "channel"),
            () -> PROTOCOL_VER,
            ABSENT.version()::equals,
            PROTOCOL_VER::equals
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
}
