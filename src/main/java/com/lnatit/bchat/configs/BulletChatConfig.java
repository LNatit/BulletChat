package com.lnatit.bchat.configs;

import net.minecraftforge.common.ForgeConfigSpec;

public class BulletChatConfig
{
    public static ForgeConfigSpec CLIENT_CONFIG;
    public static ForgeConfigSpec.DoubleValue BULLET_SPEED;
    public static ForgeConfigSpec.IntValue BULLET_LIFE;
    public static ForgeConfigSpec.IntValue MAX_BULLET;
    public static ForgeConfigSpec.IntValue MIN_FPS;
    public static ForgeConfigSpec.BooleanValue SHOW_SENDER;
    public static ForgeConfigSpec.BooleanValue HIDE_CHAT;
    public static ForgeConfigSpec.BooleanValue PARSE_TELL;
    public static ForgeConfigSpec.BooleanValue ADOPT_CHAT;
    public static ForgeConfigSpec.DoubleValue TEXT_SIZE;
    public static ForgeConfigSpec.DoubleValue OPACITY;
    public static ForgeConfigSpec.DoubleValue LINE_SPACING;
    public static ForgeConfigSpec.IntValue TOP_OFFSET;
    public static ForgeConfigSpec.IntValue TRACK_NUM;

    public static ForgeConfigSpec.ConfigValue<String> SERVER_TOKEN;

    static
    {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.comment("Client settings for Bullet Chat").push("client");

        BULLET_SPEED = builder
                .comment("Speed of shotting bullets",
                         "Unit: SPs per tick",
                         "default: 2.0"
                )
                .defineInRange("bullet_speed", 2.0D, 0.2D, 5.0D);

        BULLET_LIFE = builder
                .comment("Life of centered bullets",
                         "Unit: ticks (1/20 sec)",
                         "default: 160")
                .defineInRange("bullet_life", 160, 20, 400);

        MAX_BULLET = builder
                .comment("Max number of bullets displayed on screen",
                         "default: 200"
                )
                .defineInRange("max_bullet", 200, 10, 800);

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
                .define("hide_chat", false);

        PARSE_TELL = builder
                .comment("Whether to parse /tell infos",
                         "default: false"
                )
                .define("parse_tell", false);

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
                .defineInRange("top_offset", 0, 0, 400);

        TRACK_NUM = builder
                .comment("Number of bullet tracks on screen",
                         "This value will be chopped by other settings"
                )
                .defineInRange("track_num", 12, 1, 64);

        SERVER_TOKEN = builder
                .comment("Check Quoth I for more info",
                         "You should not modify this entry in any case!!!",
                         "default: \"bchat\""
                )
                .define("server_token", "bchat");

        CLIENT_CONFIG = builder.build();
    }
}
