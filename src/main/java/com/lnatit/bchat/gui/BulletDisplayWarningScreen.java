package com.lnatit.bchat.gui;

import com.lnatit.bchat.configs.BulletChatConfig;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.FocusableTextWidget;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.util.List;

public class BulletDisplayWarningScreen extends Screen
{
    public static final Component TITLE = Component.translatable("bchat.streamer_warning.title");
    public static final Component MODE = Component.translatable("bchat.streamer_warning.mode");
    private FocusableTextWidget textWidget;
    private final CycleButton<BulletChatConfig.Mode> mode = CycleButton.builder(BulletChatConfig.Mode::getComponent)
                                                                       .withValues(CycleButton.ValueListSupplier.create(
                                                                         List.of(BulletChatConfig.Mode.NORMAL,
                                                                                 BulletChatConfig.Mode.HIDE_CHAT,
                                                                                 BulletChatConfig.Mode.STREAMER
                                                                         )))
                                                                       .withTooltip(BulletChatConfig.Mode::getTooltip)
                                                                       .withInitialValue(BulletChatConfig.Mode.HIDE_CHAT)
                                                                       .create(MODE,
                                                                         (cycleButton, value) ->
                                                                                 BulletChatConfig.INSTANCE.setTempMode(
                                                                                         value)
                                                                 );
    private final Button proceed = Button.builder(CommonComponents.GUI_CONTINUE, button -> this.onClose()).build();
    // modify layout
    private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this, 48, 64);

    public BulletDisplayWarningScreen() {
        super(TITLE);
    }

    @Override
    protected void init() {
        LinearLayout linearlayout = this.layout.addToContents(LinearLayout.vertical());
        linearlayout.defaultCellSetting().alignHorizontallyCenter().padding(4);
        this.textWidget = linearlayout.addChild(new FocusableTextWidget(this.width, this.title, this.font),
                                                p_329717_ -> p_329717_.padding(8)
        );
        linearlayout.addChild(mode);
        linearlayout.addChild(Button.builder(Component.translatable("bchat.streamer_warning.dont_ask"), button ->
        {
            BulletChatConfig.INSTANCE.validateMode();
            this.onClose();
        }).build());
        this.layout.addToFooter(proceed);
        this.layout.visitWidgets(this::addRenderableWidget);
        this.repositionElements();
    }

    @Override
    protected void repositionElements() {
        if (this.textWidget != null) {
            this.textWidget.containWithin(this.width);
        }

        this.layout.arrangeElements();
    }

    @Override
    protected void setInitialFocus() {
        if (this.proceed != null) {
            this.proceed.setFocused(true);
        }
        else {
            super.setInitialFocus();
        }
    }

    @Override
    public void onClose() {
        BulletChatConfig.init(false);
        super.onClose();
    }
}
