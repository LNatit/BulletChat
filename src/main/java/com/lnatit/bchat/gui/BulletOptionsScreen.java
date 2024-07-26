package com.lnatit.bchat.gui;

import com.lnatit.bchat.configs.AdvancedSettingsManager;
import com.lnatit.bchat.configs.ConfigManager;
import com.lnatit.bchat.configs.BulletChatOptions;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import static com.lnatit.bchat.BulletChat.BulletChatClient.MINECRAFT;
import static com.lnatit.bchat.BulletChat.MODLOG;

public class BulletOptionsScreen extends OptionsSubScreen
{
    private static final Tooltip advancedTooltip = Tooltip.create(
            Component.translatable("options.bchat.tooltip.open_advanced"));

    public BulletOptionsScreen(Screen preScreen)
    {
        super(preScreen, MINECRAFT.options, Component.translatable("options.bchat.title"));
        BulletChatOptions.bindScreen(this, true);
    }

    @Override
    public void removed()
    {
        ConfigManager.writeToConfig();
        ConfigManager.init(false);
        BulletChatOptions.writeToOptions();
        BulletChatOptions.bindScreen(this, false);
    }

    @Override
    protected void init()
    {
        super.init();
        this.updateButtons(ConfigManager.getAdoptChat());
    }

    @Override
    protected void addOptions()
    {
        this.list.addSmall(BulletChatOptions.INSTANCE.OPTIONS);
    }

    @Override
    protected void addFooter()
    {
        this.addRenderableWidget(Button.builder(Component.translatable("options.bchat.open_advanced"),
                                                AdvancedSettingsManager::openAdvancedFile
        ).tooltip(advancedTooltip).bounds(this.width / 2 - 155, this.height - 27, 150, 20).build());

        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, (button) ->
                MINECRAFT.setScreen(this.lastScreen)).bounds(this.width / 2 + 5, this.height - 27, 150, 20).build());
    }

    public void updateButtons(boolean inactive)
    {
        for (OptionInstance<?> instance : BulletChatOptions.INSTANCE.OPTIONS_ADOPT)
        {
            AbstractWidget button = this.list.findOption(instance);
            if (button == null)
                MODLOG.warn("Failed to find OptionInstance: " + instance);
            else
            {
                button.active = !inactive;
                button.visible = !inactive;
            }
        }
    }
}
