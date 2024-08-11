package com.lnatit.bchat.configs;

import com.lnatit.bchat.components.BulletComponent;
import com.lnatit.bchat.components.ChatBadge;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import static com.lnatit.bchat.BulletChat.BulletChatClient.MINECRAFT;
import static com.lnatit.bchat.BulletChat.MODLOG;

public class BulletChatConfig
{
    public final ModConfigSpec.DoubleValue bulletSpeed;
    public final ModConfigSpec.IntValue bulletLife;
    public final ModConfigSpec.IntValue maxBullet;
    public final ModConfigSpec.IntValue minFPS;
    public final ModConfigSpec.BooleanValue showSender;
    public final ModConfigSpec.BooleanValue hideChat;
    public final ModConfigSpec.BooleanValue parseTell;
    public final ModConfigSpec.EnumValue<Mode> displayMode;
    public final ModConfigSpec.BooleanValue adoptChat;
    public final ModConfigSpec.DoubleValue textSize;
    public final ModConfigSpec.DoubleValue opacity;
    public final ModConfigSpec.DoubleValue lineSpacing;
    public final ModConfigSpec.IntValue topOffset;
    public final ModConfigSpec.IntValue trackNum;

    private Mode tempMode = Mode.HIDE_CHAT;

    public void setTempMode(Mode tempMode) {
        this.tempMode = tempMode;
    }

    public void validateMode() {
        this.displayMode.set(this.tempMode);
    }

    public static final Pair<BulletChatConfig, ModConfigSpec> pair = new ModConfigSpec.Builder().configure(
            BulletChatConfig::new);
    public static final ModConfigSpec CLIENT_CONFIG = pair.getRight();
    public static final BulletChatConfig INSTANCE = pair.getLeft();

    public BulletChatConfig(ModConfigSpec.Builder builder) {
        bulletSpeed = builder
                .comment("Speed of bullets",
                         "Unit: SPs per tick",
                         "default: 2.0"
                )
                .defineInRange("bullet_speed", 6.0D, 3.0D, 12.0D);

        bulletLife = builder
                .comment("Life of centered bullets",
                         "Unit: ticks (1/20 sec)",
                         "default: 160"
                )
                .defineInRange("bullet_life", 160, 20, 400);

        maxBullet = builder
                .comment("Max number of bullets displayed on screen",
                         "default: 200"
                )
                .defineInRange("max_bullet", 200, 10, 800);

        minFPS = builder
                .comment("When client fps lower than it, new bullets will not launch",
                         "default: 30"
                )
                .defineInRange("min_fps", 30, 0, 144);

        showSender = builder
                .comment("Whether to show senders name before bullets",
                         "default: false"
                )
                .define("show_sender", false);

        hideChat = builder
                .comment("Whether to hide vanilla chat panel when not focused",
                         "default: true"
                )
                .define("hide_chat", false);

        // TODO
        displayMode = builder.defineEnum("mode", Mode.ALWAYS_ASK);

        parseTell = builder
                .comment("Whether to parse /tell infos",
                         "default: false"
                )
                .define("parse_tell", false);

        adoptChat = builder
                .comment("Adopt the settings from the Chat Settings panel",
                         "If true, all the settings below will be invalid!",
                         "default: true"
                )
                .define("adopt_chat", true);

        textSize = builder
                .comment("Size of the bullet text",
                         "default: 1.0"
                )
                .defineInRange("text_size", 1.0D, 0.2D, 2.0D);

        opacity = builder
                .comment("Opacity of the bullets",
                         "default: 1.0"
                )
                .defineInRange("opacity", 1.0D, 0.2D, 1.0D);

        lineSpacing = builder
                .comment("The size of line spacing",
                         "default: 0.0"
                )
                .defineInRange("line_spacing", 0.0D, 0.0D, 1.0D);

        topOffset = builder
                .comment("Bullet display offset on top",
                         "Unit: SP",
                         "default: 0"
                )
                .defineInRange("top_offset", 0, 0, 400);

        trackNum = builder
                .comment("Number of bullet tracks on screen",
                         "This value will be chopped by other settings"
                )
                .defineInRange("track_num", 12, 1, 64);
    }

    private int trackOffset;
    private int trackHeight;

    public int getTrackOffset() {
        return trackOffset;
    }

    public int getTrackHeight() {
        return trackHeight;
    }

    public float getScale() {
        if (adoptChat.get()) {
            return (float) (double) MINECRAFT.options.chatScale().get();
        }
        else {
            return (float) (double) textSize.get();
        }
    }

    public int getOpacityInt() {
        if (adoptChat.get()) {
            return 255;
        }
        else {
            return (int) ((double) 255 * opacity.get() + 0.5D);
        }
    }

    public double getLineSpacing() {
        if (adoptChat.get()) {
            return MINECRAFT.options.chatLineSpacing().get();
        }
        else {
            return lineSpacing.get();
        }
    }

    public boolean shouldHideChat() {
        return displayMode.get() == Mode.ALWAYS_ASK ? tempMode == Mode.HIDE_CHAT : displayMode.get() == Mode.HIDE_CHAT;
    }

    public boolean shouldHideBullet() {
        return displayMode.get() == Mode.ALWAYS_ASK ? tempMode == Mode.STREAMER : displayMode.get() == Mode.STREAMER;
    }

    private void init() {
        double ls = getLineSpacing();
        float scale = getScale();
        int gh = MINECRAFT.getWindow().getGuiScaledHeight();
        trackHeight = (int) (9.0D * (ls + 1.0D));

        if (adoptChat.get()) {
            trackOffset = (int) Math.round(-8.0D * (ls + 1.0D) + 4.0D * ls);
            trackOffset += Mth.floor((float) (gh - 40) / scale);
            int m = trackOffset / trackHeight;
            trackOffset = m * trackHeight;
            trackNum.set(m + 1);
        }
        else {
            // Chop settings to fit screen
            float scaledHeight = (float) (gh / textSize.get());
            int maxTrackNum = (int) ((scaledHeight - topOffset.get()) / trackHeight);
            if (trackNum.get() > maxTrackNum) {
                trackNum.set(maxTrackNum);
                MODLOG.info("Chopped track number to {}!", maxTrackNum);
            }

            int maxTopOffset = (int) (scaledHeight - trackHeight);
            if (topOffset.get() > maxTopOffset) {
                topOffset.set(maxTopOffset);
                MODLOG.info("Chopped top offset to {}!", maxTopOffset);
            }

            trackOffset = topOffset.get() + (trackNum.get() - 1) * trackHeight;
        }

//        CLIENT_CONFIG.save();
//        MODLOG.info("Settings all validated!");
    }

    // DONE
    @Deprecated(since = "for removal")
    public static void writeToConfig() {
//        BulletChatConfig.BULLET_SPEED.set((double) ConfigManager.speed);
//        BulletChatConfig.BULLET_LIFE.set(ConfigManager.life);
//        BulletChatConfig.MAX_BULLET.set(ConfigManager.maxBullet);
//        BulletChatConfig.MIN_FPS.set(ConfigManager.minFps);
//        BulletChatConfig.SHOW_SENDER.set(ConfigManager.showSender);
//        BulletChatConfig.HIDE_CHAT.set(ConfigManager.hideChat);
//        BulletChatConfig.PARSE_TELL.set(ConfigManager.parseTell);
//        BulletChatConfig.ADOPT_CHAT.set(ConfigManager.adoptChat);
//        BulletChatConfig.TEXT_SIZE.set((double) ConfigManager.textSize);
//        BulletChatConfig.OPACITY.set(ConfigManager.opacity);
//        BulletChatConfig.LINE_SPACING.set((double) ConfigManager.lineSpacing);
//        BulletChatConfig.TOP_OFFSET.set(ConfigManager.topOffset);
//        BulletChatConfig.TRACK_NUM.set(ConfigManager.trackNum);
//
//        CLIENT_CONFIG.save();
//
//        MODLOG.info("Settings all validated!");
    }

    public static void init(boolean advanced) {
        if (advanced) {
            AdvancedSettingsManager.init();
        }

        BulletChatConfig.INSTANCE.init();
        BulletComponent.INSTANCE.lazyInit();
        ChatBadge.INSTANCE.init();
    }

    public enum Mode
    {
        NORMAL("bchat.streamer_warning.mode.normal"),
        HIDE_CHAT("bchat.streamer_warning.mode.hide_chat"),
        STREAMER("bchat.streamer_warning.mode.streamer"),
        ALWAYS_ASK("bchat.streamer_warning.mode.always_ask");

        private final String translationKey;

        Mode(String translationKey) {
            this.translationKey = translationKey;
        }

        public Component getComponent() {
            return Component.translatable("bchat.streamer_warning.mode").append(Component.translatable(this.translationKey));
        }

        // TODO
        public Tooltip getTooltip() {
            return Tooltip.create(Component.literal("TODO here"));
        }
    }
}
