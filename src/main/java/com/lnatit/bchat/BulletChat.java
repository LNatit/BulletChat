package com.lnatit.bchat;

import com.lnatit.bchat.configs.BulletChatConfig;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.slf4j.Logger;

import static com.lnatit.bchat.BulletChat.MODID;

@Mod(MODID)
public class BulletChat
{
    public static final String MODID = "bchat";
    public static final Logger MODLOG = LogUtils.getLogger();
    public static final Minecraft MINECRAFT = Minecraft.getInstance();

    public BulletChat()
    {
        ModLoadingContext
                .get()
                .registerConfig(ModConfig.Type.CLIENT, BulletChatConfig.CLIENT_CONFIG);
    }
}
