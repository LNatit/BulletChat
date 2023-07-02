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
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.lnatit.bchat.BulletChat.BulletChatClient.MINECRAFT;
import static com.lnatit.bchat.BulletChat.MODLOG;
import static com.lnatit.bchat.components.AbstractBullet.*;

public class BulletComponent
{
    public static final BulletComponent INSTANCE = new BulletComponent();

    private static final String colorRegex = "^#([0-9a-f]{6}|black|(d(ark_)?(blue|green|aqua|red|purple|gray))|gold|gray|blue|green|aqua|red|l(ight_)?purple|yellow|white) ?";
    private static final Pattern colorPattern = Pattern.compile(colorRegex, Pattern.CASE_INSENSITIVE);

    public static final String typeRegex = "^ *-[tbr] ?";
    public static final Pattern typePattern = Pattern.compile(typeRegex, Pattern.CASE_INSENSITIVE);

    // DONE use LinkedList for better performance
    private final Random rng = new Random();
    private final List<BulletMessage> bulletBuff = new LinkedList<>();

    private boolean shouldInit = false;

    // track maps for different types;
    // true: occupied, false: idle
    protected boolean[] trackMap;
    protected boolean[] trackMapReversed;
    protected int[] trackMapTop;
    protected int[] trackMapButton;

    private BulletComponent()
    {
        this.init();
    }

    // Init on screen init may cause incorrect map values, so adapt to lazy function
    public void lazyInit()
    {
        this.shouldInit = false;
    }

    private void init()
    {
        int maxTracksLast = this.trackMap == null ? 0 : this.trackMap.length;
        int maxTracks = BulletChatConfig.getTracks();

        if (this.trackMap == null || maxTracks != this.trackMap.length)
        {
            this.trackMap = new boolean[maxTracks];
            Arrays.fill(trackMap, false);
            this.trackMapReversed = new boolean[maxTracks];
            Arrays.fill(trackMapReversed, false);
        }

        if (maxTracksLast == 0)
            return;

        // DONE remap all the bullets
        for (BulletMessage bulletMessage : this.bulletBuff) bulletMessage.remap(maxTracksLast, maxTracks);

        this.shouldInit = true;
    }

    public void tick()
    {
        if (shouldInit)
            this.init();

        int bulletNum = 0;
        Iterator<BulletMessage> iter = this.bulletBuff.listIterator();
        while (iter.hasNext())
        {
            BulletMessage bullet = iter.next();
            if (bullet.isTerminated())
                iter.remove();
            else if (!bullet.isHidden())
            {
                bullet.tick();
                bulletNum++;
            }
            else if (bulletNum < BulletChatConfig.getMaxBullet() && MINECRAFT.getFps() > BulletChatConfig.getMinFps())
            {
                int track = this.getTrack(bullet.getId());
                if (track != -1)
                {
                    bullet.launch(track);
                    bulletNum++;
                }
                else
                {
                    MODLOG.info("Failed to launch new bullet because all tracks are occupied!");
                    break;
                }
            }
            else
            {
                MODLOG.info("Failed to launch new bullet because config limit! (max_bullet or min_fps)");
                break;
            }
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
    // #FFFFFF -T contents (space is a MUST)
    public void addMessage(TranslatableContents msgContents)
    {
        MutableComponent message = (MutableComponent) msgContents.getArgs()[1];
        String senderName = ((LiteralContents) ((MutableComponent) msgContents.getArgs()[0]).getSiblings().get(
                0).getContents()).text();
        char id = NORMAL;

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
                style = style.withColor(color);
            else
            {
                // remove # when parsing
                ChatFormatting format = ChatFormatting.getByName(
                        buffer.substring(matcher.start() + 1, matcher.end() - 1));
                if (format != null)
                    style = style.applyFormat(format);
            }

            buffer.delete(matcher.start(), matcher.end());
        }

        // DONE then find type code
        matcher = typePattern.matcher(buffer);
        if (matcher.find())
        {
            id = Character.toLowerCase(buffer.charAt(matcher.end() - 2));
            if (id != '-')
                buffer.delete(matcher.start(), matcher.end());
        }

        if (StringUtils.isBlank(buffer))
        {
            MODLOG.info("There's no contents left!");
            return;
        }

        message = Component.literal(buffer.toString()).setStyle(style);

        switch (id)
        {
            case TOP:
                return;
            case BUTTON:
                return;
            case REVERSED:
                this.bulletBuff.add(new BulletMessage.Reversed(message, senderName));
                return;
            default:
                this.bulletBuff.add(new BulletMessage(message, senderName));
        }
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
        the trackMap should be updated after calling.
    */
    public int getTrack(char bulletId)
    {
        return switch (bulletId)
                {
                    case TOP -> getTrackCentered(false);
                    case BUTTON -> getTrackCentered(true);
                    case REVERSED -> getTrackShotting(true);
                    default -> getTrackShotting(false);
                };
    }

    private int getTrackShotting(boolean reversed)
    {
        boolean[] map = reversed ? this.trackMapReversed : this.trackMap;
        int len = map.length;
        ArrayList<Integer> pool = new ArrayList<>(len);

        for (int i = 0; i < len; i++)
            if (!map[i])
                pool.add(i);

        len = pool.size();
        if (len == 0)
            return -1;
        else
        {
            len = pool.get(this.rng.nextInt(len));
            if (reversed)
                this.trackMapReversed[len] = true;
            else this.trackMap[len] = true;
            return len;
        }
    }

    private int getTrackCentered(boolean button)
    {
        return 0;
    }
}
