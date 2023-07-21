package com.lnatit.bchat.components;

import com.lnatit.bchat.configs.ConfigManager;
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
        this.prePosX = getScaledWidth();
        this.prePosX = alignPosXInNDS(prePosX);
        this.posX = prePosX - ConfigManager.getSpeed();
    }

    @Override
    public void tick()
    {
        if (!this.isHidden())
        {
            // DONE add terminate judgement
            float endPos = this.prePosX + MINECRAFT.font.getSplitter().stringWidth(this.getFullMessage());
            if (endPos < -5.0F)
            {
                this.terminate();
                return;
            }

            // DONE check departure & write to map
            if (endPos < getScaledWidth() != departed)
            {
                BulletComponent.INSTANCE.trackMap[this.getTrack()] = false;
                departed = true;
            }

            this.prePosX = posX;
            this.posX -= ConfigManager.getSpeed();
        }
    }

    @Override
    public void render(GuiGraphics graphics, float partialTick)
    {
        // DONE add conditions
        if (this.isHidden())
            return;

        // DONE subtract & reuse logic
        int trackHeight = ConfigManager.getTrackHeight();
        int posY = ConfigManager.getTrackOffset() - this.getTrack() * trackHeight;

        graphics.pose().pushPose();
        // DONE 使用 pose stack 缩放字体 in BulletComponent::render
        graphics.pose().translate(getExactPosX(partialTick), posY, 50.0F);
        graphics.drawString(MINECRAFT.font, this.getFullMessage(), 0, 0,
                            16777215 + (ConfigManager.getOpacityInt() << 24), true
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
        return Mth.lerp(partialTick, prePosX, posX);
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
            this.prePosX = -MINECRAFT.font.getSplitter().stringWidth(this.getFullMessage());
            this.prePosX = alignPosXInNDS(prePosX);
            this.posX = prePosX + ConfigManager.getSpeed();
        }

        @Override
        public void tick()
        {
            if (!this.isHidden())
            {
                // DONE add terminate judgement
                if (this.prePosX - getScaledWidth() > 5.0F)
                {
                    this.terminate();
                    return;
                }

                // DONE check departure & write to map
                if (this.prePosX > 0 != this.departed)
                {
                    BulletComponent.INSTANCE.trackMapReversed[this.getTrack()] = false;
                    departed = true;
                }

                this.prePosX = posX;
                this.posX += ConfigManager.getSpeed();
            }
        }

        @Override
        public char getId()
        {
            return REVERSED;
        }
    }
}
