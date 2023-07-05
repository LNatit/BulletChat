package com.lnatit.bchat.configs;

import com.mojang.serialization.Codec;
import net.minecraft.client.OptionInstance;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import javax.annotation.Nonnull;

public class BulletChatOptions
{
    public static BulletChatOptions INSTANCE;

    // DONE init on game startup
    private final OptionInstance<Double> BULLET_SPEED;
    private final OptionInstance<Integer> BULLET_LIFE;
    private final OptionInstance<Integer> MAX_BULLET;
    private final OptionInstance<Integer> MIN_FPS;
    private final OptionInstance<Boolean> SHOW_SENDER;
    private final OptionInstance<Boolean> HIDE_CHAT;
    private final OptionInstance<Boolean> ADOPT_CHAT;
    private final OptionInstance<Double> TEXT_SIZE;
    private final OptionInstance<Double> OPACITY;
    private final OptionInstance<Double> LINE_SPACING;
    private final OptionInstance<Integer> TOP_OFFSET;
    private final OptionInstance<Integer> TRACK_NUM;

    private BulletChatOptions()
    {
        BULLET_SPEED = new OptionInstance<>("options.bchat.bullet_speed", OptionInstance.noTooltip(),
                                            (component, value) -> percentValueLabel(component, (int) (value * 100.0D)),
                                            OptionInstance.UnitDouble.INSTANCE.xmap((value -> value * 4.8D + 0.2D),
                                                                                    (value -> (value - 0.2D) / 4.8D)
                                            ),
                                            Codec.doubleRange(0.2D, 5.0D), (double) BulletChatInitializer.getSpeed(),
                                            BulletChatInitializer::setSpeed
        );
        BULLET_LIFE = new OptionInstance<>("options.bchat.bullet_life", OptionInstance.noTooltip(),
                                           BulletChatOptions::genericValueLabel,
                                           new OptionInstance.IntRange(20, 400),
                                           BulletChatInitializer.getLife(), BulletChatInitializer::setLife
        );
        MAX_BULLET = new OptionInstance<>("options.bchat.max_bullet", OptionInstance.noTooltip(),
                                          BulletChatOptions::genericValueLabel,
                                          new OptionInstance.IntRange(10, 800),
                                          BulletChatInitializer.getMaxBullet(), BulletChatInitializer::setMaxBullet
        );
        MIN_FPS = new OptionInstance<>("options.bchat.min_fps", OptionInstance.noTooltip(),
                                       BulletChatOptions::genericValueLabel,
                                       new OptionInstance.IntRange(0, 144),
                                       BulletChatInitializer.getMinFps(), BulletChatInitializer::setMinFps
        );
        SHOW_SENDER = OptionInstance.createBoolean("options.bchat.show_sender",
                                                   BulletChatInitializer.getShowSender(),
                                                   BulletChatInitializer::setShowSender
        );
        HIDE_CHAT = OptionInstance.createBoolean("options.bchat.hide_chat",
                                                 BulletChatInitializer.getHideChat(),
                                                 BulletChatInitializer::setHideChat
        );
        ADOPT_CHAT = OptionInstance.createBoolean("options.bchat.adopt_chat",
                                                  BulletChatInitializer.getAdoptChat(),
                                                  BulletChatInitializer::setAdoptChat
        );

        // DONE modify xmap()
        TEXT_SIZE = new OptionInstance<>("options.bchat.text_size", OptionInstance.noTooltip(),
                                         (component, value) ->
                                                 value == 0.0D ?
                                                         CommonComponents.optionStatus(component, false) :
                                                         percentValueLabel(component, (int) (value * 100.0D)),
                                         OptionInstance.UnitDouble.INSTANCE.xmap((value -> value * 1.8D + 0.2D),
                                                                                 (value -> (value - 0.2D) / 1.8D)
                                         ),
                                         Codec.doubleRange(0.2D, 2.0D), (double) BulletChatInitializer.getScale(),
                                         BulletChatInitializer::setTextSize
        );
        OPACITY = new OptionInstance<>("options.bchat.opacity", OptionInstance.noTooltip(),
                                       (component, value) -> percentValueLabel(component, (int) (value * 100.0D)),
                                       OptionInstance.UnitDouble.INSTANCE.xmap((value -> value * 0.8D + 0.2D),
                                                                               (value -> (value - 0.2D) / 0.8D)
                                       ),
                                       Codec.doubleRange(0.2D, 1.0D), BulletChatInitializer.getOpacity(),
                                       BulletChatInitializer::setOpacity
        );
        LINE_SPACING = new OptionInstance<>("options.bchat.line_spacing", OptionInstance.noTooltip(),
                                            (component, value) -> percentValueLabel(component, (int) (value * 100.0D)),
                                            OptionInstance.UnitDouble.INSTANCE, BulletChatInitializer.getLineSpacing(),
                                            BulletChatInitializer::setLineSpacing
        );
        TOP_OFFSET = new OptionInstance<>("options.bchat.top_offset", OptionInstance.noTooltip(),
                                          BulletChatOptions::genericValueLabel,
                                          new OptionInstance.IntRange(0, 400),
                                          BulletChatInitializer.getTopOffset(), BulletChatInitializer::setTopOffset
        );
        TRACK_NUM = new OptionInstance<>("options.bchat.track_num", OptionInstance.noTooltip(),
                                         BulletChatOptions::genericValueLabel,
                                         new OptionInstance.IntRange(1, 64),
                                         BulletChatInitializer.getTracks(), BulletChatInitializer::setTrackNum
        );
    }

    public OptionInstance<?>[] getOptionInstances()
    {
        return new OptionInstance[]{BULLET_SPEED, BULLET_LIFE, MAX_BULLET, MIN_FPS, SHOW_SENDER, HIDE_CHAT, ADOPT_CHAT, TEXT_SIZE, OPACITY, LINE_SPACING, TOP_OFFSET, TRACK_NUM};
    }

    public static void initInstance()
    {
        if (INSTANCE == null)
            INSTANCE = new BulletChatOptions();
    }

    @Nonnull
    private static Component genericValueLabel(Component component, int value)
    {
        return Component.translatable("options.generic_value", component, value);
    }

    @Nonnull
    private static Component percentValueLabel(Component component, int value)
    {
        return Component.translatable("options.percent_value", component, value);
    }
}
