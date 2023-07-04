package com.lnatit.bchat.handlers;

import com.lnatit.bchat.components.BulletComponent;
import com.lnatit.bchat.components.ChatBadge;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.LiteralContents;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.lnatit.bchat.BulletChat.MODLOG;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ChatReceivedHandler
{
    public static final Pattern PATTERN = Pattern.compile("^<[0-9a-z_]+> ", Pattern.CASE_INSENSITIVE);

    @SubscribeEvent
    public static void onChatReceived(ClientChatReceivedEvent event)
    {
        MutableComponent component = (MutableComponent) event.getMessage();
        if (event instanceof ClientChatReceivedEvent.Player)
        {
            if (component.getContents() instanceof TranslatableContents contents)
                BulletComponent.INSTANCE.addMessage(contents);
        }
        else
        {
            try
            {
                String[] msg = getModifiedMessage(component);
                if (msg != null)
                {
                    BulletComponent.INSTANCE.addMessage(msg[1], msg[0]);
                    return;
                }

                BulletComponent.INSTANCE.addMessage(
                        (TranslatableContents) component.getContents());
            }
            catch (Exception e)
            {
                MODLOG.debug(event.getMessage().toString());
                MODLOG.debug(Arrays.toString(e.getStackTrace()));
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

                MODLOG.debug("Modified Message Received!");
                return msg;
            }
        }

        return null;
    }
}
