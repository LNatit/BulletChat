package com.lnatit.bchat;

import com.lnatit.bchat.configs.AdvancedSettingsManager;
import com.lnatit.bchat.configs.BulletChatConfig;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.slf4j.Logger;

import static com.lnatit.bchat.BulletChat.MODID;

@Mod(MODID)
public class BulletChat
{
    public static final String MODID = "bchat";
    public static final Logger MODLOG = LogUtils.getLogger();

    public BulletChat(IEventBus modEventBus, ModContainer modContainer)
    {
        if (FMLLoader.getDist() == Dist.DEDICATED_SERVER)
        {
            MODLOG.warn("Bullet Chat is not designed for dedicated server!");
            MODLOG.warn("Please remove it from mods directory if possible!");
            return;
        }

        modContainer.registerConfig(ModConfig.Type.CLIENT, BulletChatConfig.CLIENT_CONFIG);

        // Link Options Screen to Config button
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);

        AdvancedSettingsManager.init();
    }

    @OnlyIn(Dist.CLIENT)
    public static final class BulletChatClient
    {
        public static final Minecraft MINECRAFT = Minecraft.getInstance();
//        public static final IConfigScreenFactory FACTORY = new ConfigurationScreen()
//        );
    }
}
