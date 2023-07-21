package com.lnatit.bctrl.networks;

import com.lnatit.bchat.configs.ConfigManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

// TODO modify logic
public class ControllerPacket
{
    private final boolean enable;
    /**
     * {title, subtitle, overlayMsg, coloredOMsg, chat/bullet}
     */
    private final byte typeFlags;
    /**
     * {title, subtitle, overlayMsg, chat/bullet}
     */
    private final Component[] prompts = new Component[4];

    public ControllerPacket(FriendlyByteBuf buf)
    {
        enable = buf.readBoolean();
        typeFlags = buf.readByte();
        for (int i = 0; i < 4 ; i ++)
            prompts[i] = buf.readComponent();
    }

    public void encode(FriendlyByteBuf buf)
    {
        buf.writeBoolean(enable);
        buf.writeByte(typeFlags);
        for (int i = 0; i < 4 ; i ++)
            buf.writeComponent(prompts[i]);
    }

    public static void handle(ControllerPacket packet, Supplier<NetworkEvent.Context> contextSupplier)
    {
        NetworkEvent.Context context = contextSupplier.get();

        context.enqueueWork(() -> {
            if (packet.enable)
                ConfigManager.enable(packet.typeFlags, packet.prompts);
            else ConfigManager.disable(packet.typeFlags, packet.prompts);

            context.setPacketHandled(true);
        });
    }
}
