package com.lnatit.bchat.configs;

import com.lnatit.bchat.components.BulletComponent;
import com.lnatit.bchat.components.ChatBadge;
import net.minecraft.util.Mth;

import static com.lnatit.bchat.BulletChat.BulletChatClient.MINECRAFT;
import static com.lnatit.bchat.BulletChat.MODLOG;

// TODO optimize logic
public class BulletChatInitializer
{
    private static int trackOffset;
    private static int trackHeight;

    private static float speed;
    private static int life;
    private static int maxBullet;
    private static int minFps;
    private static boolean showSender;
    private static boolean hideChat;
    private static boolean parseTell;
    private static boolean adoptChat;
    private static float textSize;
    private static double opacity;
    private static float lineSpacing;
    private static int topOffset;
    private static int trackNum;

    public static void init(boolean blackList)
    {
        if (blackList)
            BlackListManager.init();

        init();
        BulletChatOptions.initInstance();
        BulletComponent.INSTANCE.lazyInit();
        ChatBadge.INSTANCE.init();
    }

    private static void init()
    {
        speed = (float) (double) BulletChatConfig.BULLET_SPEED.get();
        life = BulletChatConfig.BULLET_LIFE.get();
        maxBullet = BulletChatConfig.MAX_BULLET.get();
        minFps = BulletChatConfig.MIN_FPS.get();
        showSender = BulletChatConfig.SHOW_SENDER.get();
        hideChat = BulletChatConfig.HIDE_CHAT.get();
        parseTell = BulletChatConfig.PARSE_TELL.get();
        adoptChat = BulletChatConfig.ADOPT_CHAT.get();
        textSize = (float) (double) BulletChatConfig.TEXT_SIZE.get();
        opacity = BulletChatConfig.OPACITY.get();
        lineSpacing = (float) (double) BulletChatConfig.LINE_SPACING.get();
        topOffset = BulletChatConfig.TOP_OFFSET.get();
        trackNum = BulletChatConfig.TRACK_NUM.get();

        double ls = getLineSpacing();
        float scale = getScale();
        int gh = MINECRAFT.getWindow().getGuiScaledHeight();
        trackHeight = (int) (9.0D * (ls + 1.0D));

        if (adoptChat)
        {
            trackOffset = (int) Math.round(-8.0D * (ls + 1.0D) + 4.0D * ls);
            trackOffset += Mth.floor((float) (gh - 40) / scale);
            int m = trackOffset / trackHeight;
            trackOffset = m * trackHeight;
            trackNum = m + 1;
        }
        else
        {
            // Chop settings to fit screen
            float scaledHeight = (float) gh / textSize;
            int maxTopOffset = (int) (scaledHeight - trackHeight);
            if (topOffset > maxTopOffset)
            {
                topOffset = maxTopOffset;
                MODLOG.info("Chopped top offset to {}!", topOffset);
            }

            int maxTrackNum = (int) ((scaledHeight - topOffset) / trackHeight);
            if (trackNum > maxTrackNum)
            {
                trackNum = maxTrackNum;
                MODLOG.info("Chopped track number to {}!", trackNum);
            }

            trackOffset = topOffset + (trackNum - 1) * trackHeight;
        }
    }

    public static int getTracks()
    {
        return trackNum;
    }

    public static int getTrackOffset()
    {
        return trackOffset;
    }

    public static float getSpeed()
    {
        return speed;
    }

    public static int getLife()
    {
        return life;
    }

    public static int getMaxBullet()
    {
        return maxBullet;
    }

    public static int getMinFps()
    {
        return minFps;
    }

    public static boolean getShowSender()
    {
        return showSender;
    }

    public static boolean getHideChat()
    {
        return hideChat;
    }

    public static boolean getParseTell()
    {
        return parseTell;
    }

    public static boolean getAdoptChat()
    {
        return adoptChat;
    }

    public static float getScale()
    {
        if (adoptChat)
            return (float) (double) MINECRAFT.options.chatScale().get();
        else return textSize;
    }

    public static double getOpacity()
    {
        return opacity;
    }

    public static int getOpacityInt()
    {
        if (adoptChat)
            return 255;
        else return (int) ((double) 255 * opacity + 0.5D);
    }

    public static double getLineSpacing()
    {
        if (adoptChat)
            return MINECRAFT.options.chatLineSpacing().get();
        else return lineSpacing;
    }

    public static int getTopOffset()
    {
        return topOffset;
    }

    public static int getTrackHeight()
    {
        return trackHeight;
    }

    public static void setSpeed(double speed)
    {
        BulletChatInitializer.speed = (float) speed;
    }

    public static void setLife(int life)
    {
        BulletChatInitializer.life = life;
    }

    public static void setMaxBullet(int maxBullet)
    {
        BulletChatInitializer.maxBullet = maxBullet;
    }

    public static void setMinFps(int minFps)
    {
        BulletChatInitializer.minFps = minFps;
    }

    public static void setShowSender(boolean showSender)
    {
        BulletChatInitializer.showSender = showSender;
    }

    public static void setHideChat(boolean hideChat)
    {
        BulletChatInitializer.hideChat = hideChat;
    }

    public static void setParseTell(boolean parseTell)
    {
        BulletChatInitializer.parseTell = parseTell;
    }

    public static void setAdoptChat(boolean adoptChat)
    {
        BulletChatInitializer.adoptChat = adoptChat;
    }

    public static void setTextSize(double textSize)
    {
        BulletChatInitializer.textSize = (float) textSize;
    }

    public static void setOpacity(double opacity)
    {
        BulletChatInitializer.opacity = opacity;
    }

    public static void setLineSpacing(double lineSpacing)
    {
        BulletChatInitializer.lineSpacing = (float) lineSpacing;
    }

    public static void setTopOffset(int topOffset)
    {
        BulletChatInitializer.topOffset = topOffset;
    }

    public static void setTrackNum(int trackNum)
    {
        BulletChatInitializer.trackNum = trackNum;
    }

    // DONE
    public static void writeToConfig()
    {
        BulletChatConfig.BULLET_SPEED.set((double) speed);
        BulletChatConfig.BULLET_LIFE.set(life);
        BulletChatConfig.MAX_BULLET.set(maxBullet);
        BulletChatConfig.MIN_FPS.set(minFps);
        BulletChatConfig.SHOW_SENDER.set(showSender);
        BulletChatConfig.HIDE_CHAT.set(hideChat);
        BulletChatConfig.PARSE_TELL.set(parseTell);
        BulletChatConfig.ADOPT_CHAT.set(adoptChat);
        BulletChatConfig.TEXT_SIZE.set((double) textSize);
        BulletChatConfig.OPACITY.set(opacity);
        BulletChatConfig.LINE_SPACING.set((double) lineSpacing);
        BulletChatConfig.TOP_OFFSET.set(topOffset);
        BulletChatConfig.TRACK_NUM.set(trackNum);

        BulletChatConfig.CLIENT_CONFIG.save();

        MODLOG.info("Settings all validated!");
    }
}
