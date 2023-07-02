package com.lnatit.bchat.components;

import com.lnatit.bchat.configs.BulletChatConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;

import static com.lnatit.bchat.BulletChat.BulletChatClient.MINECRAFT;

public class BulletMessage extends AbstractBullet
{
    protected boolean departed;

    public BulletMessage(MutableComponent content, String sender)
    {
        super(content, sender);
    }

    @Override
    public void launch(int track)
    {
        super.launch(track);
        this.departed = false;
        this.posX = Mth.ceil((float) MINECRAFT.getWindow().getGuiScaledWidth() / BulletChatConfig.getScale());
    }

    @Override
    public void tick()
    {
        if (!this.isHidden())
        {
            // DONE add terminate judgement
            float endPos = this.posX + MINECRAFT.font.getSplitter().stringWidth(this.getFullMessage());
            if (endPos < -5.0F)
            {
                this.terminate();
                return;
            }

            // DONE check departure & write to map
            if (endPos < MINECRAFT.getWindow().getGuiScaledWidth() != departed)
            {
                BulletComponent.INSTANCE.trackMap[this.getTrack()] = false;
                departed = true;
            }

            this.posX -= BulletChatConfig.getSpeed();
        }
    }

    @Override
    public void render(GuiGraphics graphics, float partialTick)
    {
        // DONE add conditions
        if (this.isHidden())
            return;

        // DONE subtract & reuse logic
        int trackHeight = BulletChatConfig.getTrackHeight();
        int posY = BulletChatConfig.getTrackOffset() - this.getTrack() * trackHeight;

        graphics.pose().pushPose();
        // DONE 使用 pose stack 缩放字体 in BulletComponent::render
        graphics.pose().translate(getExactPosX(partialTick), posY, 50.0F);
        graphics.drawString(MINECRAFT.font, this.getFullMessage(), 0, 0,
                            16777215 + (BulletChatConfig.getOpacity() << 24), true
        );
        graphics.pose().popPose();
    }

    @Override
    public char getId()
    {
        return NORMAL;
    }

    public float getExactPosX(float partialTick)
    {
        return this.posX - BulletChatConfig.getSpeed() * partialTick;
    }

    public static class Reversed extends BulletMessage
    {
        public Reversed(MutableComponent content, String sender)
        {
            super(content, sender);
        }

        @Override
        public void launch(int track)
        {
            super.launch(track);
            this.posX = -MINECRAFT.font.getSplitter().stringWidth(this.getFullMessage());
        }

        @Override
        public void tick()
        {
            if (!this.isHidden())
            {
                // DONE add terminate judgement
                if (this.posX - MINECRAFT.getWindow().getGuiScaledWidth() > 5.0F)
                {
                    this.terminate();
                    return;
                }

                // DONE check departure & write to map
                if (this.posX > 0 != this.departed)
                {
                    BulletComponent.INSTANCE.trackMapReversed[this.getTrack()] = false;
                    departed = true;
                }

                this.posX += BulletChatConfig.getSpeed();
            }
        }

        @Override
        public char getId()
        {
            return REVERSED;
        }

        @Override
        public float getExactPosX(float partialTick)
        {
            return this.posX + BulletChatConfig.getSpeed() * partialTick;
        }
    }
}
