package com.lnatit.bctrl;

import com.mojang.logging.LogUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLLoader;
import org.slf4j.Logger;

import static com.lnatit.bctrl.BulletChatController.MODID;

@Mod(MODID)
public class BulletChatController
{
    public static final String MODID = "bctrl";
    public static final Logger MODLOG = LogUtils.getLogger();

    public BulletChatController()
    {
        if (FMLLoader.getDist() == Dist.CLIENT)
            return;
    }
}
