package com.lnatit.bchat.components;

import com.lnatit.bchat.configs.BulletChatOptions;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.MutableComponent;

import static com.lnatit.bchat.BulletChat.BulletChatClient.MINECRAFT;

public abstract class BulletMessageCentered extends AbstractBullet
{
    protected int age = 0;

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
            if (this.isHidden())
                return;

            if (this.age > BulletChatOptions.getLife())
            {
                this.terminate();
                BulletComponent.INSTANCE.trackMapTop[this.getTrack()] -= 1;
                return;
            }

            this.age += 1;
        }

        @Override
        public void render(GuiGraphics graphics, float partialTick)
        {
            if (this.isHidden())
                return;

            int trackHeight = BulletChatOptions.getTrackHeight();
            int posY = this.getTrack() * trackHeight;

            graphics.pose().pushPose();
            graphics.pose().translate(posX, posY, 50.0F);
            graphics.drawString(MINECRAFT.font, this.getFullMessage(), 0, 0,
                                16777215 + (BulletChatOptions.getOpacity() << 24), true
            );
            graphics.pose().popPose();
        }

        @Override
        public char getId()
        {
            return TOP;
        }
    }

    public static class Button extends BulletMessageCentered
    {
        public Button(MutableComponent content, String sender)
        {
            super(content, sender);
        }

        @Override
        public void tick()
        {
            if (this.isHidden())
                return;

            if (this.age > BulletChatOptions.getLife())
            {
                this.terminate();
                BulletComponent.INSTANCE.trackMapButton[this.getTrack()] -= 1;
                return;
            }

            this.age += 1;
        }

        @Override
        public void render(GuiGraphics graphics, float partialTick)
        {
            if (this.isHidden())
                return;

            int trackHeight = BulletChatOptions.getTrackHeight();
            int posY = MINECRAFT.getWindow().getGuiScaledHeight() - (this.getTrack() + 1) * trackHeight;

            graphics.pose().pushPose();
            graphics.pose().translate(posX, posY, 50.0F);
            graphics.drawString(MINECRAFT.font, this.getFullMessage(), 0, 0,
                                16777215 + (BulletChatOptions.getOpacity() << 24), true
            );
            graphics.pose().popPose();
        }

        @Override
        public char getId()
        {
            return BUTTON;
        }
    }
}
