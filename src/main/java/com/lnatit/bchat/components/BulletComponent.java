package com.lnatit.bchat.components;

import com.lnatit.bchat.configs.BulletChatConfig;
import net.minecraft.client.gui.GuiGraphics;
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
    private boolean[] trackMap;

    private BulletComponent()
    {
        this.init();
    }

    public void init()
    {
        int maxTracks = BulletChatConfig.getTracks();
        this.trackMap = new boolean[maxTracks];
        Arrays.fill(trackMap, false);
        // TODO remap all the bullets
    }

    public void tick()
    {
        int bulletNum = 0;
        Iterator<BulletMessage> iter = this.bulletBuff.listIterator();
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
                    int posX = Mth.ceil((float) MINECRAFT.getWindow().getGuiScaledWidth() / BulletChatConfig.getScale());
                    bullet.launch(posX, 0);
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
        // DONE consider font size impact
        float scale = BulletChatConfig.getScale();
        graphics.pose().pushPose();
        // modify pose with chat scale
        graphics.pose().scale(scale, scale, 1.0F);
        graphics.pose().translate(0.0F, 0.0F, 0.0F);

        for (BulletMessage bulletMessage : this.bulletBuff)
        {
            bulletMessage.render(graphics, partialTick);
        }

        graphics.pose().popPose();
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

    /*
        TODO
        Iterate all tracks and return a track which is not occupied,
        if all tracks are occupied, then return -1.
    */
    public int getTrack()
    {
        return 0;
    }
}
