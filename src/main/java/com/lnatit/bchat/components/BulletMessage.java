package com.lnatit.bchat.components;

import net.minecraft.client.GuiMessage;
import net.minecraft.client.GuiMessageTag;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MessageSignature;
import net.minecraft.util.FormattedCharSequence;

import javax.annotation.Nullable;

import static com.lnatit.bchat.BulletChat.MINECRAFT;

public class BulletMessage
{
    // SPs' per tick
    public static final float SPEED = 1.0f;

    private boolean launched = false;
    private boolean terminated = false;
    private int posX;
    private int track = 0;
    public GuiMessage message;

    public BulletMessage(int addedTime, Component content, @Nullable MessageSignature signature, @Nullable GuiMessageTag tag)
    {
        this.message = new GuiMessage(addedTime, content, signature, tag);
    }

    public void launch(int track)
    {
        this.posX = MINECRAFT.getWindow().getGuiScaledWidth();
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
            this.posX -= SPEED;
        }
    }

    public void render(GuiGraphics graphics, int trackOffset, int trackHeight, float partialTick)
    {
        // DONE add conditions
        if (this.isHidden())
            return;

        // DONE subtract & reuse logic
        int posY = trackOffset - this.track * trackHeight;

        graphics.pose().pushPose();
        // TODO 使用 pose stack 缩放字体
        graphics.pose().translate(0.0F, 0.0F, 50.0F);
        graphics.drawString(MINECRAFT.font, this.getMessageText(), getExactPosX(partialTick), (float) posY,
                            16777215 + (255 << 24), true
        );
        graphics.pose().popPose();
    }

    public float getExactPosX(float partialTick)
    {
        return ((float) this.posX) - SPEED * partialTick + 0.5f;
    }

    public FormattedCharSequence getMessageText()
    {
        return this.message.content().getVisualOrderText();
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
