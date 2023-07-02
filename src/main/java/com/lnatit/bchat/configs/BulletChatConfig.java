package com.lnatit.bchat.configs;

import net.minecraft.util.Mth;
import net.minecraftforge.common.ForgeConfigSpec;

import static com.lnatit.bchat.BulletChat.BulletChatClient.MINECRAFT;

public class BulletChatConfig
{
    public static ForgeConfigSpec CLIENT_CONFIG;
    public static ForgeConfigSpec.DoubleValue BULLET_SPEED;
    public static ForgeConfigSpec.IntValue MAX_BULLET;
    public static ForgeConfigSpec.IntValue MIN_FPS;
    public static ForgeConfigSpec.BooleanValue SHOW_SENDER;
    public static ForgeConfigSpec.BooleanValue HIDE_CHAT;
    public static ForgeConfigSpec.BooleanValue ADOPT_CHAT;
    public static ForgeConfigSpec.DoubleValue TEXT_SIZE;
    public static ForgeConfigSpec.DoubleValue OPACITY;
    public static ForgeConfigSpec.DoubleValue LINE_SPACING;
    public static ForgeConfigSpec.IntValue TOP_OFFSET;
    public static ForgeConfigSpec.IntValue TRACK_NUM;

    static
    {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.comment("Client settings for Bullet Chat").push("client");

        BULLET_SPEED = builder
                .comment("Speed of bullets",
                         "Unit: SPs per tick",
                         "default: 2.0"
                )
                .defineInRange("bullet_speed", 2.0D, 0.2D, 20.0D);

        MAX_BULLET = builder
                .comment("Max number of bullets displayed on screen",
                         "default: 200"
                )
                .defineInRange("max_bullet", 200, 10, 65535);

        MIN_FPS = builder
                .comment("When client fps lower than it, new bullets will not launch",
                         "default: 30"
                )
                .defineInRange("min_fps", 30, 0, 144);

        SHOW_SENDER = builder
                .comment("Whether to show senders name before bullets",
                         "default: false"
                )
                .define("show_sender", false);

        HIDE_CHAT = builder
                .comment("Whether to hide vanilla chat panel when not focused",
                         "default: true"
                )
                .define("hide_chat", true);

        ADOPT_CHAT = builder
                .comment("Adopt the settings from the Chat Settings panel",
                         "If true, all the settings below will be invalid!",
                         "default: true"
                )
                .define("adopt_chat", true);

        TEXT_SIZE = builder
                .comment("Size of the bullet text",
                         "default: 1.0"
                )
                .defineInRange("text_size", 1.0D, 0.2D, 2.0D);

        OPACITY = builder
                .comment("Opacity of the bullets",
                         "default: 1.0"
                )
                .defineInRange("opacity", 1.0D, 0.2D, 1.0D);

        LINE_SPACING = builder
                .comment("The size of line spacing",
                         "default: 0.0"
                )
                .defineInRange("line_spacing", 0.0D, 0.0D, 1.0D);

        TOP_OFFSET = builder
                .comment("Bullet display offset on top",
                         "Unit: SP",
                         "default: 0"
                )
                .defineInRange("top_offset", 0, 0, 65535);

        TRACK_NUM = builder
                .comment("Number of bullet tracks on screen",
                         "This value will be chopped by other settings"
                )
                .defineInRange("track_num", 1, 1, 65535);

        CLIENT_CONFIG = builder.build();
    }

    private static int trackOffset;
    private static int trackHeight;
    private static float speed;
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

    public static void init()
    {
        speed = (float) (double) BULLET_SPEED.get();
        maxBullet = MAX_BULLET.get();
        minFps = MIN_FPS.get();
        hideChat = HIDE_CHAT.get();
        showSender = SHOW_SENDER.get();
        adoptChat = ADOPT_CHAT.get();
        textSize = (float) (double) TEXT_SIZE.get();
        opacity = OPACITY.get();
        lineSpacing = (float) (double) LINE_SPACING.get();
        topOffset = TOP_OFFSET.get();
        trackNum = TRACK_NUM.get();

        double ls = BulletChatConfig.getLineSpacing();
        float scale = BulletChatConfig.getScale();
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
}
