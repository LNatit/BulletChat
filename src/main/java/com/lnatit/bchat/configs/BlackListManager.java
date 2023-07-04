package com.lnatit.bchat.configs;

import com.google.gson.Gson;
import net.minecraft.Util;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.LiteralContents;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static com.lnatit.bchat.BulletChat.MODLOG;

public class BlackListManager
{
    private static final File BLACK_LIST = FMLPaths.CONFIGDIR.get().resolve("bchat_blacklist.json").toFile();
    private static final Gson GSON = new Gson();
    private static BlackList blackList;

    public static void init()
    {
        if (!BLACK_LIST.isFile())
            try
            {
                Files.createFile(BLACK_LIST.toPath());
                blackList = new BlackList(new String[0], new String[0], new String[0]);
                FileUtils.write(BLACK_LIST, GSON.toJson(blackList), StandardCharsets.UTF_8);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        else
            try
            {
                blackList = GSON.fromJson(new FileReader(BLACK_LIST), BlackList.class);
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
    }

    public static boolean match(String message, String sender)
    {
        for (int i = 0; i < blackList.blockUsers.length; i++)
            if (sender.equalsIgnoreCase(blackList.blockUsers[i]))
            {
                MODLOG.info("Bullet blocked from user: " + sender);
                return true;
            }

        for (int i = 0; i < blackList.stopWords.length; i++)
            if (message.contains(blackList.stopWords[i]))
            {
                MODLOG.info("Bullet blocked by stopWords: " + blackList.stopWords[i]);
                return true;
            }

        for (int i = 0; i < blackList.regExp.length; i++)
            if (message.matches(blackList.regExp[i]))
            {
                MODLOG.info("Bullet blocked by regExp: " + blackList.regExp[i]);
                return true;
            }

        return false;
    }

    public static void openBlackListFile()
    {
        Util.getPlatform().openFile(BLACK_LIST);
    }

    public static record BlackList(String[] stopWords, String[] blockUsers, String[] regExp)
    {
    }
}
