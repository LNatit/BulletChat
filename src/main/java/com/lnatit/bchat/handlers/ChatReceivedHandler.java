package com.lnatit.bchat.handlers;

import com.lnatit.bchat.components.BulletComponent;
import com.lnatit.bchat.components.ChatBadge;
import com.lnatit.bchat.configs.BulletChatConfig;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.PlainTextContents;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceKey;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientChatReceivedEvent;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.lnatit.bchat.BulletChat.MODID;
import static com.lnatit.bchat.BulletChat.MODLOG;

@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class ChatReceivedHandler
{
    public static Pattern CUSTOMIZED_CHAT = Pattern.compile("^<(?<sender>\\w{4,16})> (?<msg>.*$)");
    public static Pattern CUSTOMIZED_TELL = Pattern.compile("^(?<sender>\\w{4,16})悄悄地对你说：(?<msg>.*$)");
    public static Pattern IGNORED_MESSAGE = Pattern.compile("");

    private static final Set<ResourceKey<ChatType>> IGNORED_TYPES = Set.of(ChatType.EMOTE_COMMAND, ChatType.SAY_COMMAND,
                                                                           ChatType.MSG_COMMAND_OUTGOING,
                                                                           ChatType.TEAM_MSG_COMMAND_OUTGOING
    );

    // "^(?<sender>\\w{4,16}): (?<msg>.*$)"
    // ^<(?<sender>\w{3,16})> (?<msg>.*$)

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onChatReceived(ClientChatReceivedEvent event) {
        if (event instanceof ClientChatReceivedEvent.System system && system.isOverlay()) {
            return;
        }

        MutableComponent component = (MutableComponent) event.getMessage();
        ChatType.Bound bound = event.getBoundChatType();

        if (shouldIgnoreMsg(component, bound) || handleTellMsg(component, bound)) {
            return;
        }

        try {
            TranslatableContents contents = (TranslatableContents) component.getContents();
            Object[] args = contents.getArgs();
            String message = ((PlainTextContents.LiteralContents) ((Component) args[1]).getContents()).text();
            String sender = ((PlainTextContents.LiteralContents) ((Component) args[0]).getSiblings().getFirst().getContents()).text();

            BulletComponent.INSTANCE.addMessage(message, sender);
        }
        catch (Exception ignored) {
            MODLOG.debug("Vanilla Format failed to parse!");
            customizedCompat(component.getString());
        }
        MODLOG.debug(component.getString());
    }

    private static boolean shouldIgnoreMsg(Component msg, ChatType.Bound type) {
        if (type != null && type.chatType().is(
                IGNORED_TYPES::contains) || msg instanceof TranslatableContents translatable && translatable.getKey().equals(
                "commands.message.display.outgoing") || IGNORED_MESSAGE.matcher(msg.getString()).matches()) {
            return true;
        }
        return false;
    }

    private static boolean handleTellMsg(Component msg, ChatType.Bound type) {
        if (!(boolean) BulletChatConfig.INSTANCE.parseTell.get()) {
            if (type != null && type.chatType().is(
                    ChatType.MSG_COMMAND_INCOMING) || msg instanceof TranslatableContents translatable && translatable.getKey().equals(
                    "commands.message.display.incoming")) {
                ChatBadge.INSTANCE.setVisible(true);
                return true;
            }
        }
        return false;
    }

    private static void customizedCompat(String raw) {
        Matcher chat = CUSTOMIZED_CHAT.matcher(raw);
        Matcher tell = CUSTOMIZED_TELL.matcher(raw);

        if (chat.matches()) {
            BulletComponent.INSTANCE.addMessage(chat.group("msg"), chat.group("sender"));
        }
        else if (BulletChatConfig.INSTANCE.parseTell.get() && tell.matches()) {
            BulletComponent.INSTANCE.addMessage(tell.group("msg"), tell.group("sender"));
        }
        else {
            ChatBadge.INSTANCE.setVisible(true);
            MODLOG.debug("Customized format failed to parse!");
        }
    }
}
