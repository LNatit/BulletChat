package com.lnatit.bchat.compat;

import cn.zbx1425.worldcomment.data.CommentEntry;
import com.lnatit.bchat.components.BulletComponent;

import java.util.regex.Pattern;

public class WorldCommentCompat {
    public static final String REGEX = "^\\{(?<msg>.*)}";
    public static final Pattern PATTERN = Pattern.compile(REGEX);

    public static void addWorldCommentMessage(CommentEntry comment) {
        String msg = comment.message.replace('\n', ' ').trim();
        String sender = comment.initiatorName;
        if (sender.isBlank())
            sender = "Anonym";
        BulletComponent.INSTANCE.addMessage(msg, sender, true);
    }
}
