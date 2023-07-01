package com.lnatit.bchat.components;

import com.lnatit.bchat.configs.BulletChatConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.FormattedCharSequence;

import static com.lnatit.bchat.BulletChat.MINECRAFT;

public class BulletMessage
{
    private boolean launched = false;
    private boolean terminated = false;
    private float posX;
    private int track = 0;
    private FormattedCharSequence fullMessage;
    private final MutableComponent message;
    private final String sender;

    public BulletMessage(MutableComponent content, String sender)
    {
        this.message = content;
        this.sender = sender;
        this.createMessage();
    }

    public void launch(int posX, int track)
    {
        this.posX = posX;
        this.track = track;
        this.launched = true;
    }

    public void tick(boolean[] trackMap)
    {
        if (!this.isHidden())
        {
            // DONE add terminate judgement
            float endPos = this.posX + MINECRAFT.font.getSplitter().stringWidth(this.fullMessage);
            if (endPos < -5.0F)
            {
                this.terminated = true;
                return;
            }

            // DONE check departure & write to map
            if (!trackMap[this.track] && endPos > MINECRAFT.getWindow().getGuiScaledWidth())
                trackMap[this.track] = true;
            this.posX -= BulletChatConfig.getSpeed();
        }
    }

    public void render(GuiGraphics graphics, float partialTick)
    {
        // DONE add conditions
        if (this.isHidden())
            return;

        // DONE subtract & reuse logic
        int trackHeight = BulletChatConfig.getTrackHeight();
        int posY = BulletChatConfig.getTrackOffset() - this.track * trackHeight;

        graphics.pose().pushPose();
        // DONE 使用 pose stack 缩放字体 in BulletComponent::render
        graphics.pose().translate(getExactPosX(partialTick), posY, 50.0F);
        graphics.drawString(MINECRAFT.font, this.fullMessage, 0, 0,
                            16777215 + (BulletChatConfig.getOpacity() << 24), true
        );
        graphics.pose().popPose();
    }

    public float getExactPosX(float partialTick)
    {
        return this.posX - BulletChatConfig.getSpeed() * partialTick + 0.5f;
    }

    public void createMessage()
    {
        MutableComponent msg = this.message;
        if (BulletChatConfig.getShowSender())
            msg = Component.literal("<" + this.sender + "> ").append(msg);
        if (this.sender.equals(MINECRAFT.getUser().getName()))
            msg.withStyle(ChatFormatting.UNDERLINE);
        this.fullMessage = msg.getVisualOrderText();
    }

    public void remap(int maxTracksLast, int maxTracks)
    {
        this.createMessage();

        if (maxTracksLast == 1)
            return;

        double k = ((double) maxTracks - 1) / ((double) maxTracksLast - 1);
        this.track =(int) (((double) this.track) * k + 0.5D);
    }

    public boolean isHidden()
    {
        return !this.launched || this.terminated;
    }

    public boolean isTerminated()
    {
        return this.terminated;
    }
}
