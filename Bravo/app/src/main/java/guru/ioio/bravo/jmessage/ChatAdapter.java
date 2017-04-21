package guru.ioio.bravo.jmessage;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.jpush.im.android.api.content.MediaContent;
import cn.jpush.im.android.api.enums.ContentType;
import cn.jpush.im.android.api.enums.MessageDirect;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;
import guru.ioio.bravo.BR;
import guru.ioio.bravo.jmessage.model.JMessageConstants;
import guru.ioio.bravo.utils.Utils;

/**
 * Created by daniel on 17-4-18.
 * adapter for conversation
 */

public class ChatAdapter extends BaseAdapter {
    private static final int TYPE_COUNT = 16;
    private List<Message> messageList = new ArrayList<>();
    private Map<ContentType, Integer> layoutIdMap;

    public ChatAdapter(Map<ContentType, Integer> layoutIdMap) {
        this.layoutIdMap = layoutIdMap;
    }

    public void setData(List<Message> messageList) {
        this.messageList.clear();
        if (messageList != null) {
            this.messageList.addAll(messageList);
        }
        notifyDataSetChanged();
    }

    public void add(Message message) {
        if (message != null) {
            messageList.add(message);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getContentType().ordinal();
    }

    @Override
    public int getCount() {
        return messageList.size();
    }

    @Override
    public Message getItem(int position) {
        return messageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewDataBinding binding;
        Message message = getItem(position);
        if (convertView == null) {
            binding = DataBindingUtil.inflate(LayoutInflater.from(
                    parent.getContext()), layoutIdMap.get(message.getContentType()), parent, false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
        } else {
            binding = (ViewDataBinding) convertView.getTag();
//            ViewCleaner.clean(convertView);
        }

        boolean isFromMe = message.getDirect() == MessageDirect.send;
        binding.setVariable(BR.isFromMe, isFromMe);
        binding.setVariable(BR.message, message);
        binding.setVariable(BR.content, message.getContent());
        binding.setVariable(BR.showDate, false);

        if (message.getContent() instanceof MediaContent) {
            MediaContent content = (MediaContent) message.getContent();
            Utils.j("MediaID", content.getMediaID(), content.getResourceId());
        }

        UserInfo fromUser = message.getFromUser();
        if (fromUser.getAvatar() != null) {
            binding.setVariable(BR.avatar, JMessageConstants.BASE_URI + fromUser.getAvatar());
        }
        binding.executePendingBindings();

        return convertView;
    }
}
