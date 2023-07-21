package com.lnatit.bchat.components;

import com.lnatit.bchat.configs.BlackListManager;
import com.lnatit.bchat.configs.ConfigManager;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
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

    private static final String COLOR_REGEX = "^#([0-9a-f]{6}|black|(d(ark_)?(blue|green|aqua|red|purple|gray))|gold|gray|blue|green|aqua|red|l(ight_)?purple|yellow|white) ?";
    private static final Pattern COLOR_PATTERN = Pattern.compile(COLOR_REGEX, Pattern.CASE_INSENSITIVE);

    public static final String TYPE_REGEX = "^ *-[tbr] ?";
    public static final Pattern TYPE_PATTERN = Pattern.compile(TYPE_REGEX, Pattern.CASE_INSENSITIVE);

    // DONE use LinkedList for better performance
    private final Random rng = new Random();
    private final List<AbstractBullet> bulletBuff = new LinkedList<>();

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
        this.shouldInit = true;
    }

    private void init()
    {
        int maxTracksLast = this.trackMap == null ? 0 : this.trackMap.length;
        int maxTracks = ConfigManager.getTracks();

        if (this.trackMap == null || maxTracks != this.trackMap.length)
        {
            this.trackMap = new boolean[maxTracks];
            Arrays.fill(trackMap, false);
            this.trackMapReversed = new boolean[maxTracks];
            Arrays.fill(trackMapReversed, false);
            this.trackMapTop = new int[maxTracks];
            Arrays.fill(trackMapTop, 0);
            this.trackMapButton = new int[maxTracks];
            Arrays.fill(trackMapButton, 0);
        }

        if (maxTracksLast == 0)
            return;

        // DONE remap all the bullets
        for (AbstractBullet bulletMessage : this.bulletBuff) bulletMessage.remap(maxTracksLast, maxTracks);

        this.shouldInit = false;
    }

    public void tick()
    {
        if (shouldInit)
            this.init();

        int bulletNum = 0;
        Iterator<AbstractBullet> iter = this.bulletBuff.listIterator();
        while (iter.hasNext())
        {
            AbstractBullet bullet = iter.next();
            if (bullet.isTerminated())
                iter.remove();
            else if (!bullet.isHidden())
            {
                bullet.tick();
                bulletNum++;
            }
            else if (bulletNum <= ConfigManager.getMaxBullet() && MINECRAFT.getFps() >= ConfigManager.getMinFps())
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
        float scale = ConfigManager.getScale();
        graphics.pose().pushPose();
        // modify pose with chat scale
        graphics.pose().scale(scale, scale, 1.0F);
        // add Z offset to render bullets on top of chats
        graphics.pose().translate(0.0F, 0.0F, 1000.0F);

        for (AbstractBullet bulletMessage : this.bulletBuff)
        {
            bulletMessage.render(graphics, partialTick);
        }

        graphics.pose().popPose();
    }

    public void addMessage(String message, String sender)
    {
        char id = NORMAL;

        if (BlackListManager.match(message, sender))
            return;

        StringBuffer buffer = new StringBuffer(message);

        MODLOG.debug("Message preprocessed successful!");

        // find color code first
        Style style = Style.EMPTY;
        Matcher matcher = COLOR_PATTERN.matcher(buffer);

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

        MODLOG.debug("Message color parsed successful!");

        // DONE then find type code
        matcher = TYPE_PATTERN.matcher(buffer);
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

        MutableComponent msg = Component.literal(buffer.toString()).setStyle(style);

        MODLOG.debug("Message type parsed successful!");

        switch (id)
        {
            case TOP -> this.bulletBuff.add(new BulletMessageCentered.Top(msg, sender));
            case BUTTON -> this.bulletBuff.add(new BulletMessageCentered.Button(msg, sender));
            case REVERSED -> this.bulletBuff.add(new BulletMessage.Reversed(msg, sender));
            default -> this.bulletBuff.add(new BulletMessage(msg, sender));
        }
    }

    public void clearMessages(boolean all)
    {
        if (all)
            this.bulletBuff.clear();
        else this.bulletBuff.removeIf(AbstractBullet::isHidden);
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
        int[] map = button ? this.trackMapButton : this.trackMapTop;
        int len = map.length;
        int temp = 0;

        for (int i = 1; i < len; i++)
        {
            if (map[i] < map[temp])
                temp = i;
        }

        if (button)
            this.trackMapButton[temp] += 1;
        else this.trackMapTop[temp] += 1;

        return temp;
    }
}
