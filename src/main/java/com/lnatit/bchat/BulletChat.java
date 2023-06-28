package com.lnatit.bchat;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.common.Mod;
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
//        Event
//        TitleScreen
    }
}
