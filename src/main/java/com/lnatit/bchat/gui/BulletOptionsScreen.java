package com.lnatit.bchat.gui;

import com.lnatit.bchat.configs.BlackListManager;
import com.lnatit.bchat.configs.BulletChatInitializer;
import com.lnatit.bchat.configs.BulletChatOptions;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.SimpleOptionsSubScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import static com.lnatit.bchat.BulletChat.BulletChatClient.MINECRAFT;
import static com.lnatit.bchat.BulletChat.MODLOG;

public class BulletOptionsScreen extends SimpleOptionsSubScreen
{
    public BulletOptionsScreen(Screen preScreen)
    {
        super(preScreen, MINECRAFT.options, Component.translatable("options.bchat.title"),
              BulletChatOptions.INSTANCE.OPTIONS
        );
        BulletChatOptions.bindScreen(this, true);
    }

    @Override
    public void removed()
    {
        BulletChatInitializer.writeToConfig();
        BulletChatInitializer.init(false);
        BulletChatOptions.bindScreen(this, false);
    }

    @Override
    protected void init()
    {
        super.init();
        this.updateButtons(BulletChatInitializer.getAdoptChat());
    }

    @Override
    protected void createFooter()
    {
        this.addRenderableWidget(Button.builder(Component.translatable("options.bchat.open_blacklist"),
                                                BlackListManager::openBlackListFile
        ).bounds(this.width / 2 - 155, this.height - 27, 150, 20).build());

        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, (button) ->
                MINECRAFT.setScreen(this.lastScreen)).bounds(this.width / 2 + 5, this.height - 27, 150, 20).build());
    }

    public void updateButtons(boolean active)
    {
        for (OptionInstance<?> instance : BulletChatOptions.INSTANCE.OPTIONS_ADOPT)
        {
            AbstractWidget button = this.list.findOption(instance);
            if (button == null)
                MODLOG.warn("Failed to find OptionInstance: " + instance);
            else
            {
                button.active = active;
                button.visible = active;
            }
        }
    }
}