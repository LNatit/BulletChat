package com.lnatit.bchat.components;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;

import static com.lnatit.bchat.BulletChat.MINECRAFT;

public class ChatBadge
{
    public static final ChatBadge INSTANCE = new ChatBadge();

    private boolean visible;
    private int chatOffset;
    private int chatHeight;
    private final MutableComponent badge;

    private ChatBadge()
    {
        this.visible = false;
        this.init();
        badge = Component.translatable("chat.bchat.newmsg");
    }

    public void init()
    {
        float scale = (float) (double) MINECRAFT.options.chatScale().get();
        int gh = MINECRAFT.getWindow().getGuiScaledHeight();
        double ls = MINECRAFT.options.chatLineSpacing().get();
        this.chatOffset = (int) Math.round(-8.0D * (ls + 1.0D) + 4.0D * ls);
        this.chatOffset += Mth.floor((float) (gh - 40) / scale);
        this.chatHeight = (int) (9.0D * (ls + 1.0D));
    }

    public void render(GuiGraphics graphics)
    {
        if (this.visible)
        {
            int fontOpacity = (int) (255.0D * MINECRAFT.options.chatOpacity().get() * (double)0.9F + (double)0.1F);
            int backOpacity = (int) (255.0D * MINECRAFT.options.textBackgroundOpacity().get());
            int width = Mth.ceil(MINECRAFT.font.getSplitter().stringWidth(badge.getVisualOrderText())) + 2;
            float scale = (float) (double) MINECRAFT.options.chatScale().get();

            graphics.pose().pushPose();
            graphics.pose().scale(scale, scale, 1.0F);
            graphics.pose().translate(4.0F, 0.0F, 50.0F);

            graphics.fill(-4, chatOffset + chatHeight, width, chatOffset - 1, backOpacity << 24);
            graphics.fill(-4, chatOffset + chatHeight, -2, chatOffset - 1, 0xFF0000 | fontOpacity << 24);

            graphics.pose().translate(0.0F, 0.0F, 50.0F);
            graphics.drawString(MINECRAFT.font, badge, 0, chatOffset, 16777215 | fontOpacity << 24);

            graphics.pose().popPose();
        }
    }

    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }
}
