package com.lnatit.bchat.configs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lnatit.bchat.handlers.ChatReceivedHandler;
import net.minecraft.Util;
import net.neoforged.fml.loading.FMLPaths;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.regex.Pattern;

import static com.lnatit.bchat.BulletChat.MODLOG;

public class AdvancedSettingsManager
{
    private static final File ADVANCED_SETTINGS = FMLPaths.CONFIGDIR.get().resolve("bchat-advanced.json").toFile();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static AdvancedSettings advancedSettings;

    public static void init()
    {
        if (!ADVANCED_SETTINGS.isFile())
            try
            {
                Files.createFile(ADVANCED_SETTINGS.toPath());
                advancedSettings = new AdvancedSettings(new String[0], new String[0], new String[0],
                                                        "^(?<sender>\\w{3,16}): (?<msg>.*$)",
                                                        "^(?<sender>\\w{3,16}) 对你说道: (?<msg>.*$)"
                );
                FileUtils.write(ADVANCED_SETTINGS, GSON.toJson(advancedSettings), StandardCharsets.UTF_8);
            }
            catch (IOException e)
            {
                MODLOG.error("Failed to load advanced settings", e);
            }
        else
            try
            {
                advancedSettings = GSON.fromJson(new FileReader(ADVANCED_SETTINGS), AdvancedSettings.class);
            }
            catch (FileNotFoundException e)
            {
                MODLOG.error("Failed to load advanced settings", e);
            }

        // init patterns
        ChatReceivedHandler.CUSTOMIZED_CHAT = Pattern.compile(advancedSettings.chatRegCustomized);
        ChatReceivedHandler.CUSTOMIZED_TELL = Pattern.compile(advancedSettings.tellRegCustomized);
    }

    public static boolean match(String message, String sender)
    {
        for (int i = 0; i < advancedSettings.blockUsers.length; i++)
            if (sender.equalsIgnoreCase(advancedSettings.blockUsers[i]))
            {
                MODLOG.info("Bullet blocked from user: {}", sender);
                return true;
            }

        for (int i = 0; i < advancedSettings.stopWords.length; i++)
            if (message.contains(advancedSettings.stopWords[i]))
            {
                MODLOG.info("Bullet blocked by stopWords: {}", advancedSettings.stopWords[i]);
                return true;
            }

        for (int i = 0; i < advancedSettings.regExp.length; i++)
            if (message.matches(advancedSettings.regExp[i]))
            {
                MODLOG.info("Bullet blocked by regExp: {}", advancedSettings.regExp[i]);
                return true;
            }

        return false;
    }

    public static void openAdvancedFile()
    {
        Util.getPlatform().openFile(ADVANCED_SETTINGS);
    }

    public record AdvancedSettings(String[] stopWords, String[] blockUsers, String[] regExp,
                                   String chatRegCustomized, String tellRegCustomized)
    {
    }
}
