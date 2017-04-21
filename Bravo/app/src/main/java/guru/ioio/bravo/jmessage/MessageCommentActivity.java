package guru.ioio.bravo.jmessage;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.android.databinding.library.baseAdapters.BR;

import java.util.ArrayList;
import java.util.List;

import guru.ioio.bravo.R;
import guru.ioio.bravo.base.BindingBaseAdapter;
import guru.ioio.bravo.jmessage.model.CommentBean;

/**
 * Created by daniel on 17-4-20.
 */

public class MessageCommentActivity extends MessageBaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        title = "评论";
        adapter = new BindingBaseAdapter<>(R.layout.item_comment, BR.data);
        super.onCreate(savedInstanceState);

        adapter.setData(getMock());
    }

    public List<CommentBean> getMock() {
        List<CommentBean> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            CommentBean bean = new CommentBean();

            bean.avatarUri = "https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo_top_ca79a146.png";
            bean.description = "asldkfja;wljkeflje";
            bean.imageUri = "https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo_top_ca79a146.png";
            bean.name = "awefwefaw";
            bean.showDate = true;
            bean.time = System.currentTimeMillis();
            bean.title = "lakjwewafekjawefkjwkejhf";

            list.add(bean);
        }
        return list;
    }
}
