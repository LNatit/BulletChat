package com.lnatit.bchat.components;

import com.lnatit.bchat.configs.BulletChatConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;

import java.util.*;

import static com.lnatit.bchat.BulletChat.MINECRAFT;
import static com.lnatit.bchat.BulletChat.MODLOG;

public class BulletComponent
{
    public static final BulletComponent INSTANCE = new BulletComponent();

    // DONE use LinkedList for better performance
    private final List<BulletMessage> bulletBuff = new LinkedList<>();
    private final Random rng = new Random();
    private boolean[] trackMap;

    private BulletComponent()
    {
        this.init();
    }

    public void init()
    {
        int maxTracksLast = this.trackMap == null ? 0 : this.trackMap.length;
        int maxTracks = BulletChatConfig.getTracks();
        this.trackMap = new boolean[maxTracks];
        Arrays.fill(trackMap, false);

        if (maxTracksLast == 0)
            return;

        // DONE remap all the bullets
        for (BulletMessage bulletMessage : this.bulletBuff) bulletMessage.reMap(maxTracksLast, maxTracks);
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
            else if (bulletNum < BulletChatConfig.getMaxBullet())
            {
                int track = this.getTrack();
                if (track != -1)
                {
                    int posX = Mth.ceil(
                            (float) MINECRAFT.getWindow().getGuiScaledWidth() / BulletChatConfig.getScale());
                    bullet.launch(posX, getTrack());
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
        // add Z offset to render bullets on top of chats
        graphics.pose().translate(0.0F, 0.0F, 1000.0F);

        for (BulletMessage bulletMessage : this.bulletBuff)
        {
            bulletMessage.render(graphics, partialTick);
        }

        graphics.pose().popPose();
    }

    public void addMessage(MutableComponent msg, UUID sender)
    {
        this.bulletBuff.add(new BulletMessage(msg, sender));
    }

    public void clearMessages(boolean all)
    {
        if (all)
            this.bulletBuff.clear();
        else this.bulletBuff.removeIf(BulletMessage::isHidden);
    }

    /*
        DONE
        Iterate all tracks and return a track which is not occupied,
        if all tracks are occupied, then return -1.
    */
    public int getTrack()
    {
        int len = trackMap.length;
        ArrayList<Integer> pool = new ArrayList<>(len);

        for (int i = 0; i < len; i++)
            if (!trackMap[i])
                pool.add(i);

        len = pool.size();
        if (len == 0)
            return -1;
        else return pool.get(this.rng.nextInt(len));
    }
}
