package com.lnatit.bchat.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static com.lnatit.bchat.BulletChat.MINECRAFT;
import static com.lnatit.bchat.BulletChat.MODLOG;

public class BulletComponent
{
    public static final BulletComponent INSTANCE = new BulletComponent();
    public static final int MAX_BULLET_NUM = 200;

    // DONE use LinkedList for better performance
    private final List<BulletMessage> bulletBuff = new LinkedList<>();
    private int maxTracks = 1;
    private boolean[] trackMap;

    private BulletComponent()
    {
    }

    public void init()
    {
        this.maxTracks = getTracks();
        this.trackMap = new boolean[maxTracks];
        Arrays.fill(trackMap, false);
        MODLOG.debug(String.valueOf(maxTracks));
        MODLOG.debug(String.valueOf(MINECRAFT == Minecraft.getInstance()));
        // TODO remap all the bullets
    }

    public void tick()
    {
        int bulletNum = 0;
        Iterator<BulletMessage> iter = bulletBuff.listIterator();
        while (iter.hasNext())
        {
            BulletMessage bullet = iter.next();
            if (bullet.isTerminated())
                iter.remove();
            else if (!bullet.isHidden())
            {
                bullet.tick(this.trackMap);
                bulletNum++;
            }
            else if (bulletNum < MAX_BULLET_NUM)
            {
                int track = this.getTrack();
                if (track != -1)
                {
                    // TODO randomly assign track
                    bullet.launch(0);
                    bulletNum++;
                }
                else
                    MODLOG.info("Failed to launch new bullet because all tracks are occupied!");
            }
            else break;
        }
    }

    public void render(GuiGraphics graphics, float partialTick)
    {
        // TODO consider font size impact
        float scale = (float) (double) MINECRAFT.options.chatScale().get();
        int gh = graphics.guiHeight();
        double ls = MINECRAFT.options.chatLineSpacing().get();
        int trackOffset = (int) Math.round(-8.0D * (ls + 1.0D) + 4.0D * ls);
        trackOffset += Mth.floor((float) (gh - 40) / scale);
        int trackHeight = (int) (9.0D * (ls + 1.0D));

        for (BulletMessage bulletMessage : this.bulletBuff)
        {
            bulletMessage.render(graphics, trackOffset, trackHeight, partialTick);
        }
    }

    public void addMessage(Component msg)
    {
        this.bulletBuff.add(new BulletMessage(MINECRAFT.gui.getGuiTicks(), msg, null, null));
    }

    public void clearMessages(boolean all)
    {
        if (all)
            this.bulletBuff.clear();
        else this.bulletBuff.removeIf(BulletMessage::isHidden);
    }

    public int getTrack()
    {
        return 0;
    }

    // Aka ChatComponent::getLinesPerPage()
    public int getTracks()
    {
        int trackHeight = (int) (9.0D * (MINECRAFT.options.chatLineSpacing().get() + 1.0D));
        int totalHeight = ChatComponent.getHeight(MINECRAFT.options.chatHeightFocused().get());
        return totalHeight / trackHeight;
    }
}
