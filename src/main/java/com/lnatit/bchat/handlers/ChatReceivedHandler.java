package com.lnatit.bchat.handlers;

import com.lnatit.bchat.components.BulletComponent;
import com.lnatit.bchat.components.ChatBadge;
import com.lnatit.bchat.configs.BulletChatConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.PlainTextContents;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientChatReceivedEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.lnatit.bchat.BulletChat.MODID;
import static com.lnatit.bchat.BulletChat.MODLOG;

@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class ChatReceivedHandler
{
    public static Pattern CUSTOMIZED_CHAT = Pattern.compile("^(?<sender>\\w{4,16}): (?<msg>.*$)");
    public static Pattern CUSTOMIZED_TELL = Pattern.compile("^(?<sender>\\w{4,16}) 对你说道: (?<msg>.*$)");

    // "^(?<sender>\\w{4,16}): (?<msg>.*$)"
    // ^<(?<sender>\w{3,16})> (?<msg>.*$)

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onChatReceived(ClientChatReceivedEvent event)
    {
        if (event instanceof ClientChatReceivedEvent.System system && system.isOverlay())
            return;

        MutableComponent component = (MutableComponent) event.getMessage();

        try
        {
            TranslatableContents contents = (TranslatableContents) component.getContents();
            // don't parse /tell messages
            if (contents.getKey().equals("commands.message.display.outgoing") ||
                    !(boolean) BulletChatConfig.INSTANCE.parseTell.get() && contents.getKey().equals(
                            "commands.message.display.incoming"))
            {
                ChatBadge.INSTANCE.setVisible(true);
                return;
            }

            Object[] args = contents.getArgs();
            String message = ((PlainTextContents.LiteralContents) ((Component) args[1]).getContents()).text();
            String sender = ((PlainTextContents.LiteralContents) ((Component) args[0]).getSiblings().get(0).getContents()).text();

            BulletComponent.INSTANCE.addMessage(message, sender);
        }
        catch (Exception exception)
        {
            MODLOG.debug("Vanilla Format failed to parse!");
            customizedCompat(component.getString());
        }
        MODLOG.debug(component.getString());
    }

    private static void customizedCompat(String raw)
    {
        Matcher chat = CUSTOMIZED_CHAT.matcher(raw);
        Matcher tell = CUSTOMIZED_TELL.matcher(raw);

        if (chat.matches())
            BulletComponent.INSTANCE.addMessage(chat.group("msg"), chat.group("sender"));
        else if (BulletChatConfig.INSTANCE.parseTell.get() && tell.matches())
            BulletComponent.INSTANCE.addMessage(tell.group("msg"), tell.group("sender"));
        else
        {
            ChatBadge.INSTANCE.setVisible(true);
            MODLOG.debug("Customized format failed to parse!");
        }
    }
}
