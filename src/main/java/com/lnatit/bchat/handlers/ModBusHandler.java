package com.lnatit.bchat.handlers;

import com.lnatit.bchat.components.BulletComponent;
import com.lnatit.bchat.configs.ConfigManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.lwjgl.opengl.GL11;

import static com.lnatit.bchat.BulletChat.BulletChatClient.MINECRAFT;
import static com.lnatit.bchat.BulletChat.MODID;
import static net.minecraftforge.client.gui.overlay.VanillaGuiOverlay.CHAT_PANEL;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModBusHandler
{
    @SubscribeEvent
    public static void onGuiOverlayRegistered(RegisterGuiOverlaysEvent event)
    {
        event.registerAbove(CHAT_PANEL.id(), "bullet_layer",
                            ((gui, guiGraphics, partialTick, screenWidth, screenHeight) ->
                            {
                                if (ConfigManager.disabled())
                                    return;

                                RenderSystem.enableBlend();
                                RenderSystem.blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);

                                MINECRAFT.getProfiler().push("bullet");
                                BulletComponent.INSTANCE.render(guiGraphics, partialTick);
                                MINECRAFT.getProfiler().pop();
                            })
        );
    }

    @SubscribeEvent
    public static void onConfigLoaded(ModConfigEvent event)
    {
        if (event.getConfig().getModId().equals(MODID))
        {
            ConfigManager.init(true);
        }
    }
}
