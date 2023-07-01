package com.lnatit.bchat.components;

import com.lnatit.bchat.configs.BlackListManager;
import com.lnatit.bchat.configs.BulletChatConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.contents.LiteralContents;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.util.Mth;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.lnatit.bchat.BulletChat.BulletChatClient.MINECRAFT;
import static com.lnatit.bchat.BulletChat.MODLOG;

public class BulletComponent
{
    public static final BulletComponent INSTANCE = new BulletComponent();

    private static final String regExp = "^#([0-9a-z]{6}|black|(d(ark_)?(blue|green|aqua|red|purple|gray))|gold|gray|blue|green|aqua|red|l(ight_)?purple|yellow|white)[ ]";
    private static final Pattern colorPattern = Pattern.compile(regExp, Pattern.CASE_INSENSITIVE);

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
        for (BulletMessage bulletMessage : this.bulletBuff) bulletMessage.remap(maxTracksLast, maxTracks);
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

    // DONE add stop words here
    // TODO add other types of bullets & logic
    // #FFFFFF-T
    public void addMessage(TranslatableContents msgContents)
    {
        MutableComponent message = (MutableComponent) msgContents.getArgs()[1];
        String senderName = ((LiteralContents) ((MutableComponent) msgContents.getArgs()[0]).getSiblings().get(
                0).getContents()).text();

        if (BlackListManager.match(message, senderName))
            return;

        StringBuffer buffer = new StringBuffer(((LiteralContents) message.getContents()).text());
        // find color code first
        Style style = Style.EMPTY;
        Matcher matcher = colorPattern.matcher(buffer);

        if (matcher.find())
        {
            // There is a space at the end of the string
            TextColor color = TextColor.parseColor(buffer.substring(matcher.start(), matcher.end() - 1));
            if (color != null)
            {
                style = style.withColor(color);
                buffer.delete(matcher.start(), matcher.end());
            }
            else
            {
                // remove # when parsing
                ChatFormatting format = ChatFormatting.getByName(buffer.substring(matcher.start() + 1, matcher.end() - 1));
                if (format != null)
                {
                    style = style.applyFormat(format);
                    buffer.delete(matcher.start(), matcher.end());
                }
            }
        }
        // TODO then find type code


        message = Component.literal(buffer.toString()).setStyle(style);

        this.bulletBuff.add(new BulletMessage(message, senderName));
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
