package com.lnatit.bchat.handlers;

import com.lnatit.bchat.components.BulletComponent;
import com.lnatit.bchat.components.ChatBadge;
import com.lnatit.bchat.configs.ConfigManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.LiteralContents;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.lnatit.bchat.BulletChat.BulletChatClient.MINECRAFT;
import static com.lnatit.bchat.BulletChat.MODID;
import static com.lnatit.bchat.BulletChat.MODLOG;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ChatReceivedHandler
{
    public static final Pattern PATTERN = Pattern.compile("^<[0-9a-z_]+> ", Pattern.CASE_INSENSITIVE);

    @SubscribeEvent
    public static void onChatReceived(ClientChatReceivedEvent event)
    {
        if (ConfigManager.shouldSkipRender())
            return;

        MutableComponent component = (MutableComponent) event.getMessage();

        try
        {
            TranslatableContents contents = (TranslatableContents) component.getContents();
            // don't parse /tell messages
            if (contents.getKey().equals("commands.message.display.outgoing") ||
                    !ConfigManager.getParseTell() && contents.getKey().equals(
                            "commands.message.display.incoming"))
                return;

            Object[] args = contents.getArgs();
            String message = ((LiteralContents) ((Component) args[1]).getContents()).text();
            String sender = ((LiteralContents) ((Component) args[0]).getSiblings().get(0).getContents()).text();

            BulletComponent.INSTANCE.addMessage(message, sender);
        }
        catch (Exception exception)
        {
            MODLOG.debug("Vanilla Format failed to parse!");
            MODLOG.debug(exception.getMessage());

            try
            {
                String[] msg = getModifiedMessage(component);
                if (msg != null)
                {
                    BulletComponent.INSTANCE.addMessage(msg[1], msg[0]);
                }
                else
                {
                    MODLOG.debug("Modified Message failed to parse!");
                    ChatBadge.INSTANCE.setVisible(true);
                }
            }
            catch (Exception e)
            {
                MODLOG.debug(e.getMessage());
                MODLOG.debug(component.toString());
                ChatBadge.INSTANCE.setVisible(true);
            }
        }
    }

    private static String[] getModifiedMessage(MutableComponent component)
    {
        if (component.getSiblings().get(0).getContents() instanceof LiteralContents contents)
        {
            String text = contents.text();
            Matcher matcher = PATTERN.matcher(text);

            if (matcher.find())
            {
                String[] msg = new String[2];
                msg[0] = text.substring(matcher.start() + 1, matcher.end() - 2);
                msg[1] = text.substring(matcher.end());
                return msg;
            }
        }

        return null;
    }
}
