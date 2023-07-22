package com.lnatit.bchat.bctrl;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.slf4j.Logger;

import static net.minecraftforge.network.NetworkRegistry.ABSENT;

public class NetworkManager
{
    public static final String CHANNEL_ID = "bctrl";
    public static final Logger CHANNEL_LOG = LogUtils.getLogger();

    private static final String SERVER_VER = "h9c5";
    private static final String CLIENT_VER = "be33";

    private static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(CHANNEL_ID, "channel"),
            () -> CLIENT_VER,
            NetworkManager::clientHandShake,
            CLIENT_VER::equals
    );

    public static boolean clientHandShake(String serverVersion)
    {
        if (serverVersion.equals(ABSENT.version()))
        {
            CHANNEL_LOG.info("Joining server without bullet controller.");
            return true;
        }
        else if (serverVersion.equals(SERVER_VER))
        {
            CHANNEL_LOG.info("Joining server with bullet controller.");
            return true;
        }
        else return serverVersion.equals(CLIENT_VER);
    }

    public static void register()
    {
        // packets are now useless
//        CHANNEL.registerMessage(0, ControllerPacket.class,
//                                ControllerPacket::encode,
//                                ControllerPacket::new,
//                                ControllerPacket::handle,
//                                Optional.of(NetworkDirection.PLAY_TO_CLIENT)
//        );
    }
}
