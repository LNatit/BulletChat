package com.lnatit.bctrl.networks;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import static com.lnatit.bctrl.BulletChatController.MODLOG;

@Deprecated
public class ControllerPacket
{
    private final boolean enable;
    /**
     * {title, subtitle, overlayMsg, coloredOMsg, chat/bullet}
     */
    private final byte typeFlags;
    /**
     * {chat/bullet, overlayMsg, subtitle, title}
     */
    private final Component[] prompts = new Component[]{Component.empty(), Component.literal("bc"), Component.empty(), Component.empty()};

    public ControllerPacket(boolean enable)
    {
        this.enable = enable;
        this.typeFlags = 0b00001100;
    }

    public ControllerPacket(boolean enable, byte typeFlags)
    {
        this.enable = enable;
        this.typeFlags = typeFlags;
    }

    public ControllerPacket(boolean enable, byte typeFlags, Component[] prompts)
    {
        this.enable = enable;
        this.typeFlags = typeFlags;
        System.arraycopy(prompts, 0, this.prompts, 0, 4);
    }

    public ControllerPacket(FriendlyByteBuf buf)
    {
        enable = buf.readBoolean();
        typeFlags = buf.readByte();
        for (int i = 0; i < 4; i++)
            prompts[i] = buf.readComponent();
    }

    public void encode(FriendlyByteBuf buf)
    {
        buf.writeBoolean(enable);
        buf.writeByte(typeFlags);
        for (int i = 0; i < 4; i++)
            buf.writeComponent(prompts[i]);
    }

    public static void handle(ControllerPacket packet, Supplier<NetworkEvent.Context> contextSupplier)
    {
        NetworkEvent.Context context = contextSupplier.get();

        context.enqueueWork(() ->
                            {
                                MODLOG.warn("Packet wrongly registered on client!");
                                context.setPacketHandled(true);
                            });
    }
}
