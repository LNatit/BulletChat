package com.lnatit.bchat.components;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.MutableComponent;

import static com.lnatit.bchat.BulletChat.BulletChatClient.MINECRAFT;

public abstract class BulletMessageCentered extends AbstractBullet
{
    public BulletMessageCentered(MutableComponent content, String sender)
    {
        super(content, sender);
        this.posX = (((float) MINECRAFT.getWindow().getGuiScaledWidth()) - MINECRAFT.font.getSplitter().stringWidth(this.getFullMessage())) / 2.0f;
    }

    public static class Top extends BulletMessageCentered
    {
        public Top(MutableComponent content, String sender)
        {
            super(content, sender);
        }

        @Override
        public void tick()
        {

        }

        @Override
        public void render(GuiGraphics graphics, float partialTick)
        {

        }

        @Override
        public char getId()
        {
            return TOP;
        }
    }
}
