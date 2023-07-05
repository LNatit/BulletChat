package com.lnatit.bchat.components;

import com.lnatit.bchat.configs.BulletChatInitializer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.FormattedCharSequence;

import static com.lnatit.bchat.BulletChat.BulletChatClient.MINECRAFT;

public abstract class AbstractBullet
{
    public static final char NORMAL = 'n';
    public static final char REVERSED = 'r';
    public static final char TOP = 't';
    public static final char BUTTON = 'b';

    protected float posX;
    protected final MutableComponent message;
    protected final String sender;
    private boolean launched = false;
    private boolean terminated = false;
    private int track = 0;
    private FormattedCharSequence fullMessage;

    public AbstractBullet(MutableComponent content, String sender)
    {
        this.message = content;
        this.sender = sender;
        this.createMessage();
    }

    public int getTrack()
    {
        return track;
    }

    public FormattedCharSequence getFullMessage()
    {
        return fullMessage;
    }

    public void launch(int track)
    {
        this.track = track;
        this.launched = true;
    }

    public abstract void tick();

    public abstract void render(GuiGraphics graphics, float partialTick);

    public void createMessage()
    {
        MutableComponent msg = this.message;
        if (BulletChatInitializer.getShowSender())
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
        this.track = (int) (((double) this.track) * k + 0.5D);
    }

    public void terminate()
    {
        this.terminated = true;
    }

    public boolean isHidden()
    {
        return !this.launched || this.terminated;
    }

    public boolean isTerminated()
    {
        return this.terminated;
    }

    public abstract char getId();
}
