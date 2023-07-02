package com.lnatit.bchat;

import com.lnatit.bchat.configs.BlackListManager;
import com.lnatit.bchat.configs.BulletChatConfig;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLLoader;
import org.slf4j.Logger;

import static com.lnatit.bchat.BulletChat.MODID;

@Mod(MODID)
public class BulletChat
{
    public static final String MODID = "bchat";
    public static final Logger MODLOG = LogUtils.getLogger();

    public BulletChat()
    {
        if (FMLLoader.getDist() == Dist.DEDICATED_SERVER)
        {
            MODLOG.warn("Bullet Chat is not designed for dedicated server!");
            MODLOG.warn("Please remove it from mods directory if possible!");
            return;
        }

        ModLoadingContext
                .get()
                .registerConfig(ModConfig.Type.CLIENT, BulletChatConfig.CLIENT_CONFIG);

        BlackListManager.init();
    }

    @OnlyIn(Dist.CLIENT)
    public static final class BulletChatClient
    {
        public static final Minecraft MINECRAFT = Minecraft.getInstance();
    }
}
