package com.lnatit.bctrl.handlers;

import com.lnatit.bctrl.commands.ChatCommand;
import com.lnatit.bctrl.commands.EchoCommand;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.lnatit.bctrl.BulletChatController.MODID;

@Mod.EventBusSubscriber(modid = MODID)
public class CommandRegistryHandler
{
    @SubscribeEvent
    public static void onCommandRegister(RegisterCommandsEvent event)
    {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        EchoCommand.register(dispatcher);
        ChatCommand.register(dispatcher);
    }
}
