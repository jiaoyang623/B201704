package guru.ioio.bravo.jmessage;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.Conversation;
import guru.ioio.bravo.BR;
import guru.ioio.bravo.R;
import guru.ioio.bravo.base.BindingBaseAdapter;
import guru.ioio.bravo.databinding.ActivityMessageListBinding;
import guru.ioio.bravo.jmessage.model.MessageBean;

/**
 * Created by daniel on 17-4-18.
 * test for jmessage
 */

public class MessageListActivity extends Activity {
    public BindingBaseAdapter<MessageBean> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMessageListBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_message_list);
        adapter = new BindingBaseAdapter<>(R.layout.item_message_list, BR.data);
        binding.setPresenter(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshMessageList();
    }

    public boolean onDismiss() {
        return true;
    }

    public boolean onAddContact() {
        return true;
    }

    public List<MessageBean> getMessageList() {
        List<MessageBean> list = new ArrayList<>();

        list.add(new MessageBean(getResources().getDrawable(R.drawable.message_system),
                "官方通知", "this is a notification from the company", "this time", 3, MessageBean.Type.System));

        list.add(new MessageBean(getResources().getDrawable(R.drawable.message_comment),
                "评论通知", "this is a notification from the company", "this time", 2, MessageBean.Type.Comment));

        if (JMessageClient.getConversationList() != null) {
            for (Conversation conversation : JMessageClient.getConversationList()) {
                list.add(new MessageBean(conversation));
            }
        }
        return list;
    }

    private void refreshMessageList() {
        adapter.setData(getMessageList());
    }

    public void onItemClick(int position) {
        MessageBean bean = adapter.getItem(position);
        switch (bean.type) {
            case Chat: {
                Conversation conversation = adapter.getItem(position).conversation;
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                intent.putExtra(ChatActivity.KEY_TARGET, conversation.getTitle());
                startActivity(intent);
            }
            break;
            case System:
                startActivity(new Intent(getApplicationContext(), MessageSystemActivity.class));
                break;
            case Comment:
                startActivity(new Intent(getApplicationContext(), MessageCommentActivity.class));
                break;
        }
    }
}
