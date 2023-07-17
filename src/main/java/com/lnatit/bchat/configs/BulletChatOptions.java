package com.lnatit.bchat.configs;

import com.lnatit.bchat.gui.BulletOptionsScreen;
import com.mojang.serialization.Codec;
import net.minecraft.client.OptionInstance;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import javax.annotation.Nonnull;
import java.util.Arrays;

import static com.lnatit.bchat.BulletChat.MODLOG;

public class BulletChatOptions
{
    public static BulletChatOptions INSTANCE;
    private static BulletOptionsScreen screen;

    // DONE init on game startup
    public final OptionInstance<?>[] OPTIONS;
    public final OptionInstance<?>[] OPTIONS_ADOPT;


    private BulletChatOptions()
    {
        OptionInstance<Double> bulletSpeed = new OptionInstance<>("options.bchat.bullet_speed",
                                                                  OptionInstance.noTooltip(),
                                                                  (component, value) -> percentValueLabel(component,
                                                                                                          (int) (value * 100.0D)
                                                                  ),
                                                                  OptionInstance.UnitDouble.INSTANCE.xmap(
                                                                          (value -> value * 4.8D + 0.2D),
                                                                          (value -> (value - 0.2D) / 4.8D)
                                                                  ),
                                                                  Codec.doubleRange(0.2D, 5.0D),
                                                                  (double) BulletChatInitializer.getSpeed(),
                                                                  BulletChatInitializer::setSpeed
        );
        OptionInstance<Integer> bulletLife = new OptionInstance<>("options.bchat.bullet_life",
                                                                  OptionInstance.noTooltip(),
                                                                  BulletChatOptions::genericValueLabel,
                                                                  new OptionInstance.IntRange(20, 400),
                                                                  BulletChatInitializer.getLife(),
                                                                  BulletChatInitializer::setLife
        );
        OptionInstance<Integer> maxBullet = new OptionInstance<>("options.bchat.max_bullet",
                                                                 OptionInstance.noTooltip(),
                                                                 BulletChatOptions::genericValueLabel,
                                                                 new OptionInstance.IntRange(10, 800),
                                                                 BulletChatInitializer.getMaxBullet(),
                                                                 BulletChatInitializer::setMaxBullet
        );
        OptionInstance<Integer> minFps = new OptionInstance<>("options.bchat.min_fps", OptionInstance.noTooltip(),
                                                              BulletChatOptions::genericValueLabel,
                                                              new OptionInstance.IntRange(0, 144),
                                                              BulletChatInitializer.getMinFps(),
                                                              BulletChatInitializer::setMinFps
        );
        OptionInstance<Boolean> showSender = OptionInstance.createBoolean("options.bchat.show_sender",
                                                                          BulletChatInitializer.getShowSender(),
                                                                          BulletChatInitializer::setShowSender
        );
        OptionInstance<Boolean> hideChat = OptionInstance.createBoolean("options.bchat.hide_chat",
                                                                        BulletChatInitializer.getHideChat(),
                                                                        BulletChatInitializer::setHideChat
        );
        OptionInstance<Boolean> parseTell = OptionInstance.createBoolean("options.bchat.parse_tell",
                                                                         BulletChatInitializer.getParseTell(),
                                                                         BulletChatInitializer::setParseTell
        );
        OptionInstance<Boolean> adoptChat = OptionInstance.createBoolean("options.bchat.adopt_chat",
                                                                         BulletChatInitializer.getAdoptChat(),
                                                                         BulletChatOptions::onAdoptChatChanged
        );
        // DONE modify xmap()
        OptionInstance<Double> textSize = new OptionInstance<>("options.bchat.text_size", OptionInstance.noTooltip(),
                                                               (component, value) ->
                                                                       value == 0.0D ?
                                                                               CommonComponents.optionStatus(component,
                                                                                                             false
                                                                               ) :
                                                                               percentValueLabel(component,
                                                                                                 (int) (value * 100.0D)
                                                                               ),
                                                               OptionInstance.UnitDouble.INSTANCE.xmap(
                                                                       (value -> value * 1.8D + 0.2D),
                                                                       (value -> (value - 0.2D) / 1.8D)
                                                               ),
                                                               Codec.doubleRange(0.2D, 2.0D),
                                                               (double) BulletChatInitializer.getScale(),
                                                               BulletChatInitializer::setTextSize
        );
        OptionInstance<Double> opacity = new OptionInstance<>("options.bchat.opacity", OptionInstance.noTooltip(),
                                                              (component, value) -> percentValueLabel(component,
                                                                                                      (int) (value * 100.0D)
                                                              ),
                                                              OptionInstance.UnitDouble.INSTANCE.xmap(
                                                                      (value -> value * 0.8D + 0.2D),
                                                                      (value -> (value - 0.2D) / 0.8D)
                                                              ),
                                                              Codec.doubleRange(0.2D, 1.0D),
                                                              BulletChatInitializer.getOpacity(),
                                                              BulletChatInitializer::setOpacity
        );
        OptionInstance<Double> lineSpacing = new OptionInstance<>("options.bchat.line_spacing",
                                                                  OptionInstance.noTooltip(),
                                                                  (component, value) -> percentValueLabel(component,
                                                                                                          (int) (value * 100.0D)
                                                                  ),
                                                                  OptionInstance.UnitDouble.INSTANCE,
                                                                  BulletChatInitializer.getLineSpacing(),
                                                                  BulletChatInitializer::setLineSpacing
        );
        OptionInstance<Integer> topOffset = new OptionInstance<>("options.bchat.top_offset",
                                                                 OptionInstance.noTooltip(),
                                                                 BulletChatOptions::genericValueLabel,
                                                                 new OptionInstance.IntRange(0, 400),
                                                                 BulletChatInitializer.getTopOffset(),
                                                                 BulletChatInitializer::setTopOffset
        );
        OptionInstance<Integer> trackNum = new OptionInstance<>("options.bchat.track_num", OptionInstance.noTooltip(),
                                                                BulletChatOptions::genericValueLabel,
                                                                new OptionInstance.IntRange(1, 64),
                                                                BulletChatInitializer.getTracks(),
                                                                BulletChatInitializer::setTrackNum
        );

        OPTIONS = new OptionInstance[]{bulletSpeed, bulletLife, maxBullet, minFps, showSender, hideChat, parseTell, adoptChat, textSize, opacity, lineSpacing, topOffset, trackNum};
        OPTIONS_ADOPT = new OptionInstance[]{textSize, opacity, lineSpacing, topOffset, trackNum};
    }

    public static void initInstance()
    {
        if (INSTANCE == null)
            INSTANCE = new BulletChatOptions();
    }

    public static void bindScreen(BulletOptionsScreen s, boolean bind)
    {
        if (bind)
            screen = s;
        else screen = null;
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

    // DONE disable options when adoptChat changed
    private static void onAdoptChatChanged(boolean adoptChat)
    {
        BulletChatInitializer.setAdoptChat(adoptChat);
        if (screen == null)
            MODLOG.warn("Illegal changes on options!");
        else
            screen.updateButtons(adoptChat);
    }
}