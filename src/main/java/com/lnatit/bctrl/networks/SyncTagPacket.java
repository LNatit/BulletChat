package com.lnatit.bctrl.networks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncTagPacket
{
    private final boolean add;
    private final String tag;

    public SyncTagPacket(FriendlyByteBuf buf)
    {
        add = buf.readBoolean();
        tag = buf.readUtf();
    }

    protected SyncTagPacket(boolean add, String tag)
    {
        this.add = add;
        this.tag = tag;
    }

    public void encode(FriendlyByteBuf buf)
    {
        buf.writeBoolean(this.add);
        buf.writeUtf(this.tag);
    }

    public static void handle(SyncTagPacket packet, Supplier<NetworkEvent.Context> contextSupplier)
    {
        NetworkEvent.Context context = contextSupplier.get();

        context.enqueueWork(() -> {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null)
            {
                if (packet.add)
                    player.addTag(packet.tag);
                else player.removeTag(packet.tag);
            }

            context.setPacketHandled(true);
        });
    }
}
