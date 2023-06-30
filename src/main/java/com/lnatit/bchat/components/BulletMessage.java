package com.lnatit.bchat.components;

import com.lnatit.bchat.configs.BulletChatConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.FormattedCharSequence;

import java.util.UUID;

import static com.lnatit.bchat.BulletChat.MINECRAFT;

public class BulletMessage
{
    private boolean launched = false;
    private boolean terminated = false;
    private float posX;
    private int track = 0;
    private final MutableComponent message;
    private final UUID sender;

    public BulletMessage(MutableComponent content, UUID sender)
    {
        this.message = content;
        this.sender = sender;
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
            float endPos = this.posX + MINECRAFT.font.getSplitter().stringWidth(this.getMessageText());
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
        graphics.pose().translate(0.0F, 0.0F, 50.0F);
        graphics.drawString(MINECRAFT.font, this.getMessageText(), getExactPosX(partialTick), (float) posY,
                            16777215 + (BulletChatConfig.getOpacity() << 24), true
        );
        graphics.pose().popPose();
    }

    public float getExactPosX(float partialTick)
    {
        return this.posX - BulletChatConfig.getSpeed() * partialTick + 0.5f;
    }

    public FormattedCharSequence getMessageText()
    {
        if (this.sender.toString().equals(MINECRAFT.getUser().getUuid()))
            return this.message.withStyle(ChatFormatting.UNDERLINE).getVisualOrderText();
        return this.message.getVisualOrderText();
    }

    public void reMap(int maxTracksLast, int maxTracks)
    {
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
