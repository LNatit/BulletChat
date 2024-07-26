package com.lnatit.bchat.handlers;

import com.lnatit.bchat.components.BulletComponent;
import com.lnatit.bchat.configs.ConfigManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import org.lwjgl.opengl.GL11;

import static com.lnatit.bchat.BulletChat.BulletChatClient.MINECRAFT;
import static com.lnatit.bchat.BulletChat.MODID;

@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModBusHandler
{
    public static final ResourceLocation BULLET = ResourceLocation.fromNamespaceAndPath(MODID, "bullet_layer");

    @SubscribeEvent
    public static void onGuiOverlayRegistered(RegisterGuiLayersEvent event)
    {
        event.registerAbove(VanillaGuiLayers.CHAT, BULLET,
                            ((guiGraphics, deltaTracker) ->
                            {
                                RenderSystem.enableBlend();
                                RenderSystem.blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);

                                MINECRAFT.getProfiler().push("bullet");
                                BulletComponent.INSTANCE.render(guiGraphics, deltaTracker.getGameTimeDeltaPartialTick(true));
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
