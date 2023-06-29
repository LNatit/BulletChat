package com.lnatit.bchat.configs;

import net.minecraft.util.Mth;
import net.minecraftforge.common.ForgeConfigSpec;

import static com.lnatit.bchat.BulletChat.MINECRAFT;

public class BulletChatConfig
{
    public static ForgeConfigSpec CLIENT_CONFIG;
    public static ForgeConfigSpec.BooleanValue ADOPT_CHAT;
    public static ForgeConfigSpec.DoubleValue TEXT_SIZE;
    public static ForgeConfigSpec.DoubleValue LINE_SPACING;
    public static ForgeConfigSpec.IntValue TRACK_NUM;

    static
    {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.comment("Client settings for Bullet Chat").push("client");

        ADOPT_CHAT = builder
                .comment(" Adopt the settings from the Chat Settings panel",
                         " If true, all the other settings will be invalid!",
                         " default: true"
                )
                .define("adopt_chat", true);

        TEXT_SIZE = builder
                .comment(" Size of the bullet text",
                         " range: [0, 2]",
                         " default: 1.0"
                )
                .defineInRange("text_size", 1.0D, 0.0D, 2.0D);

        LINE_SPACING = builder
                .comment(" The size of line spacing",
                         " range: [0, 1]",
                         " default: 0.0"
                )
                .defineInRange("line_spacing", 0.0D, 0.0D, 1.0D);

        TRACK_NUM = builder
                .comment(" Number of bullet tracks on screen",
                         " The value will be chopped by your Video Settings"
                )
                .defineInRange("track_num", 1, 1, 65535);

        CLIENT_CONFIG = builder.build();
    }

    private static int trackOffset;
    private static int trackHeight;

    public static void init()
    {
        double ls = BulletChatConfig.getLineSpacing();
        float scale = BulletChatConfig.getScale();
        int gh = MINECRAFT.getWindow().getGuiScaledHeight();
        trackHeight = (int) (9.0D * (ls + 1.0D));

        if (ADOPT_CHAT.get())
        {
            trackOffset = (int) Math.round(-8.0D * (ls + 1.0D) + 4.0D * ls);
            trackOffset += Mth.floor((float) (gh - 40) / scale);
            int tracks = trackOffset / trackHeight;
            trackOffset = tracks * trackHeight;
        }
        else
            trackOffset = TRACK_NUM.get() * trackHeight;
    }

    public static float getScale()
    {
        if (ADOPT_CHAT.get())
            return (float) (double) MINECRAFT.options.chatScale().get();
        else return (float) (double) TEXT_SIZE.get();
    }

    public static double getLineSpacing()
    {
        if (ADOPT_CHAT.get())
            return MINECRAFT.options.chatLineSpacing().get();
        else return LINE_SPACING.get();
    }

    public static int getTracks()
    {
        return trackOffset / trackHeight;
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
