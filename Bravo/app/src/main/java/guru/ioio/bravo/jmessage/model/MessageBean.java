package guru.ioio.bravo.jmessage.model;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import java.util.Date;
import java.util.List;

import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;

/**
 * Created by daniel on 17-4-18.
 * data holder for message list
 */

public class MessageBean {
    public Type type;
    public String headUri;
    public String title;
    public String description;
    public String time;
    public int count;
    public Drawable defaultDrawable;
    public Conversation conversation;

    public MessageBean(String headUri, String title, String description, String time, int count, Type type) {
        this.headUri = headUri;
        this.title = title;
        this.description = description;
        this.time = time;
        this.count = count;
        this.type = type;
    }

    public MessageBean(Drawable headDrawable, String title, String description, String time, int count, Type type) {
        this.defaultDrawable = headDrawable;
        this.title = title;
        this.description = description;
        this.time = time;
        this.count = count;
        this.type = type;
    }


    public MessageBean(Conversation conversation) {
        type = Type.Chat;
        this.conversation = conversation;
        this.title = conversation.getTitle();

        Message message = conversation.getLatestMessage();
        List<Message> list = conversation.getMessagesFromOldest(0, 999);
        if (list != null && list.size() > 0) {
            for (Message msg : list) {
                if (title.equals(msg.getFromUser().getUserName())) {
                    headUri = JMessageConstants.BASE_URI + msg.getFromUser().getAvatar();
                    break;
                }
            }
        }
        if (message != null) {
            switch (message.getContentType()) {
                case text:
                    description = ((TextContent) message.getContent()).getText();
                    break;
                case voice:
                    description = "[语音]";
                    break;
                case image:
                    description = "[图片]";
                    break;
                default:
                    description = "...";
                    break;
            }

            time = new Date(message.getCreateTime()).toString();
            if (TextUtils.isEmpty(headUri)) {
                headUri = JMessageConstants.BASE_URI + message.getFromUser().getAvatar();
            }
        }
    }

    public enum Type {
        Chat, Comment, System
    }

}
