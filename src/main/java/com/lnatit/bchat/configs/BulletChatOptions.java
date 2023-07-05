package com.lnatit.bchat.configs;

import com.lnatit.bchat.components.BulletComponent;
import com.lnatit.bchat.components.ChatBadge;
import net.minecraft.client.OptionInstance;
import net.minecraft.util.Mth;

import static com.lnatit.bchat.BulletChat.BulletChatClient.MINECRAFT;

public class BulletChatOptions
{
    private static int trackOffset;
    private static int trackHeight;
    private static float speed;
    private static int life;
    private static int maxBullet;
    private static int minFps;
    private static boolean hideChat;
    private static boolean showSender;
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
        BulletComponent.INSTANCE.lazyInit();
        ChatBadge.INSTANCE.init();
    }

    private static void init()
    {
        speed = (float) (double) BulletChatConfig.BULLET_SPEED.get();
        life = BulletChatConfig.BULLET_LIFE.get();
        maxBullet = BulletChatConfig.MAX_BULLET.get();
        minFps = BulletChatConfig.MIN_FPS.get();
        hideChat = BulletChatConfig.HIDE_CHAT.get();
        showSender = BulletChatConfig.SHOW_SENDER.get();
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
            trackOffset = topOffset + (trackNum - 1) * trackHeight;
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

    public static boolean getHideChat()
    {
        return hideChat;
    }

    public static boolean getShowSender()
    {
        return showSender;
    }

    public static float getScale()
    {
        if (adoptChat)
            return (float) (double) MINECRAFT.options.chatScale().get();
        else return textSize;
    }

    public static int getOpacity()
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

    public static int getTracks()
    {
        return trackOffset / trackHeight + 1;
    }

    public static int getTrackOffset()
    {
        return trackOffset;
    }

    public static int getTrackHeight()
    {
        return trackHeight;
    }

    public static void setTrackOffset(int trackOffset)
    {
        BulletChatOptions.trackOffset = trackOffset;
    }

    public static void setTrackHeight(int trackHeight)
    {
        BulletChatOptions.trackHeight = trackHeight;
    }

    public static void setSpeed(float speed)
    {
        BulletChatOptions.speed = speed;
    }

    public static void setLife(int life)
    {
        BulletChatOptions.life = life;
    }

    public static void setMaxBullet(int maxBullet)
    {
        BulletChatOptions.maxBullet = maxBullet;
    }

    public static void setMinFps(int minFps)
    {
        BulletChatOptions.minFps = minFps;
    }

    public static void setHideChat(boolean hideChat)
    {
        BulletChatOptions.hideChat = hideChat;
    }

    public static void setShowSender(boolean showSender)
    {
        BulletChatOptions.showSender = showSender;
    }

    public static void setAdoptChat(boolean adoptChat)
    {
        BulletChatOptions.adoptChat = adoptChat;
    }

    public static void setTextSize(float textSize)
    {
        BulletChatOptions.textSize = textSize;
    }

    public static void setOpacity(double opacity)
    {
        BulletChatOptions.opacity = opacity;
    }

    public static void setLineSpacing(float lineSpacing)
    {
        BulletChatOptions.lineSpacing = lineSpacing;
    }

    public static void setTopOffset(int topOffset)
    {
        BulletChatOptions.topOffset = topOffset;
    }

    public static void setTrackNum(int trackNum)
    {
        BulletChatOptions.trackNum = trackNum;
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
        BulletChatConfig.ADOPT_CHAT.set(adoptChat);
        BulletChatConfig.TEXT_SIZE.set((double) textSize);
        BulletChatConfig.OPACITY.set(opacity);
        BulletChatConfig.LINE_SPACING.set((double) lineSpacing);
        BulletChatConfig.TOP_OFFSET.set(topOffset);
        BulletChatConfig.TRACK_NUM.set(trackNum);

        BulletChatConfig.CLIENT_CONFIG.save();
    }

    //    private static final OptionInstance<Double> BULLET_SPEED;
//    private static final OptionInstance<Integer> BULLET_LIFE;
//    private static final OptionInstance<Integer> MAX_BULLET;
//    private static final OptionInstance<Integer> MIN_FPS;
    private static final OptionInstance<Boolean> SHOW_SENDER =
            OptionInstance.createBoolean("options.bchat.show_sender",
                                         BulletChatConfig.SHOW_SENDER.getDefault(),
                                         BulletChatOptions::setShowSender
            );
    private static final OptionInstance<Boolean> HIDE_CHAT =
            OptionInstance.createBoolean("options.bchat.hide_chat",
                                         false,
                                         BulletChatOptions::setHideChat
            );
    private static final OptionInstance<Boolean> ADOPT_CHAT =
            OptionInstance.createBoolean("options.bchat.adopt_chat",
                                         true,
                                         BulletChatOptions::setAdoptChat
            );
//    private static final OptionInstance<Double> TEXT_SIZE;
//    private static final OptionInstance<Double> OPACITY;
//    private static final OptionInstance<Double> LINE_SPACING;
//    private static final OptionInstance<Integer> TOP_OFFSET;
//    private static final OptionInstance<Integer> TRACK_NUM;

    public static OptionInstance<?>[] getOptionInstances()
    {
        return new OptionInstance[]{SHOW_SENDER, HIDE_CHAT, ADOPT_CHAT};
    }
}
