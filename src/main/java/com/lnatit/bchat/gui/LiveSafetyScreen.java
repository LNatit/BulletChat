package com.lnatit.bchat.gui;

import com.lnatit.bchat.configs.BulletChatConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.layouts.Layout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.multiplayer.WarningScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.util.List;

public class LiveSafetyScreen extends WarningScreen
{
    private static final Component TITLE = Component.translatable("bchat.streamer_warning.head").withStyle(
            ChatFormatting.BOLD);
    private static final Component CONTENT = Component.translatable("bchat.streamer_warning.message");
    private static final Component CHECK = Component.translatable("multiplayerWarning.check");
    private static final Component NARRATION = TITLE.copy().append("\n").append(CONTENT);
    private static final Component MODE = Component.translatable("bchat.streamer_warning.mode");

    private final CycleButton<BulletChatConfig.Mode> mode = CycleButton.builder(BulletChatConfig.Mode::getComponent)
                                                                       .withValues(CycleButton.ValueListSupplier.create(
                                                                               List.of(BulletChatConfig.Mode.NORMAL,
                                                                                       BulletChatConfig.Mode.HIDE_CHAT,
                                                                                       BulletChatConfig.Mode.HIDE_BULLET,
                                                                                       BulletChatConfig.Mode.STREAMER
                                                                               )))
                                                                       .withTooltip(BulletChatConfig.Mode::getTooltip)
                                                                       .withInitialValue(
                                                                               BulletChatConfig.Mode.HIDE_CHAT)
                                                                       .create(MODE,
                                                                               (cycleButton, value) ->
                                                                                       BulletChatConfig.INSTANCE.setTempMode(
                                                                                               value)
                                                                       );

    private final Button proceed = Button.builder(CommonComponents.GUI_PROCEED, btn ->
    {
        if (this.stopShowing != null && this.stopShowing.selected()) {
            BulletChatConfig.INSTANCE.validateMode();
        }
        this.onClose();
    }).build();

    public LiveSafetyScreen() {
        super(TITLE, CONTENT, CHECK, NARRATION);
    }

    @Override
    protected Layout addFooterButtons() {
        LinearLayout linearlayout = LinearLayout.horizontal().spacing(8);
        BulletChatConfig.INSTANCE.setTempMode(BulletChatConfig.Mode.HIDE_CHAT);
        linearlayout.addChild(mode);
        linearlayout.addChild(proceed);
        return linearlayout;
    }

    @Override
    public void onClose() {
        BulletChatConfig.init(false);
        super.onClose();
    }
}
