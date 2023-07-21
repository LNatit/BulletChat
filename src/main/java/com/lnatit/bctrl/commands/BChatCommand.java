package com.lnatit.bctrl.commands;

import com.lnatit.bctrl.networks.ControllerPacket;
import com.lnatit.bctrl.networks.NetworkManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.ModList;

public class BChatCommand
{
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        if (ModList.get().isLoaded("bchat"))
        {
            LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("bchat");

            builder.requires(executor -> executor.hasPermission(2))
                   .then(Commands.literal("enable")
                                 .then(Commands.argument("targets", EntityArgument.players())
                                               .executes((c) -> setBchat(c, true))
                                 )
                   )
                   .then(Commands.literal("disable")
                                 .then(Commands.argument("targets", EntityArgument.players())
                                               .executes((c) -> setBchat(c, false))
                                 )
                   );

            dispatcher.register(builder);
        }
    }

    private static int setBchat(CommandContext<CommandSourceStack> context, boolean enable) throws CommandSyntaxException
    {
        for (ServerPlayer player : EntityArgument.getPlayers(context, "targets"))
            NetworkManager.sendUpdatePack(player, new ControllerPacket(enable, (byte) 0));

        return 0;
    }
}
