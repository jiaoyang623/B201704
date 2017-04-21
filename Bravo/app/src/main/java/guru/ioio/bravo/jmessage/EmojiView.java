package guru.ioio.bravo.jmessage;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import guru.ioio.bravo.R;

/**
 * Created by daniel on 17-4-19.
 * for emoji
 */

public class EmojiView extends RelativeLayout {
    private int mPageCount = 0;
    private PagerIndicator mIndicator;
    private ViewPager mPager;
    private Button mSendButton;
    private List<GridView> mGridViewList = new ArrayList<>();
    private EmojiPagerAdapter mPagerAdapter;

    public EmojiView(Context context) {
        super(context);
        init(context);
    }

    public EmojiView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EmojiView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public EmojiView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.widget_emoji, this, true);
        mIndicator = (PagerIndicator) findViewById(R.id.indicator);
        mSendButton = (Button) findViewById(R.id.send);
        mPager = (ViewPager) findViewById(R.id.pager);

        mPagerAdapter = new EmojiPagerAdapter();
        mPager.setAdapter(mPagerAdapter);

        setEmojiList(getResources().getIntArray(R.array.emoji));
    }

    private void refreshPage() {
        mIndicator.setCount(mPageCount);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        mGridViewList.clear();
        for (int i = 0; i < mPageCount; i++) {
            GridView v = (GridView) inflater.inflate(R.layout.widget_gridview, mPager, false);
            EmojiAdapter adapter = new EmojiAdapter();
            v.setAdapter(adapter);
            v.setOnItemClickListener(adapter);

            mGridViewList.add(v);
        }
        if (mPagerAdapter != null) {
            mPagerAdapter.notifyDataSetChanged();
        }
    }

    private class EmojiPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mGridViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            GridView v = mGridViewList.get(position);
            container.addView(v);
            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            mIndicator.setSelection(position);
        }
    }

    private class EmojiAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {
        private List<String> emojiList = new ArrayList<>();

        public EmojiAdapter setEmoji(String[] emoji) {
            emojiList.clear();
            if (emoji == null) {
                emojiList.add("");
            } else {
                for (String s : emoji) {
                    emojiList.add(s);
                }
                emojiList.add("");
            }
            notifyDataSetChanged();

            return this;
        }

        @Override
        public int getCount() {
            return emojiList.size();
        }

        @Override
        public String getItem(int position) {
            return emojiList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).hashCode();
        }

        private static final int TYPE_EMOJI = 0;
        private static final int TYPE_DELETE = 1;

        @Override
        public int getItemViewType(int position) {
            return position == getCount() - 1 ? TYPE_DELETE : TYPE_EMOJI;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (getItemViewType(position) == TYPE_EMOJI) {
                TextView t = (TextView) convertView;
                if (t == null) {
                    t = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_emoji_text, parent, false);
                }

                t.setText(getItem(position));
                t.setCompoundDrawables(null, null, null, null);
                return t;
            } else {
                ImageView v = (ImageView) convertView;
                if (v == null) {
                    v = new ImageView(parent.getContext());
                }

                v.setImageResource(R.drawable.message_back_icon);

                return v;
            }
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position == getCount() - 1) {
                if (mOnDeleteClickListener != null) {
                    mOnDeleteClickListener.onDelete();
                }
            } else {
                if (mEmojiListener != null) {
                    mEmojiListener.onEmojiClick(getItem(position).toString());
                }
            }
        }
    }

    public void setOnSendClick(OnClickListener l) {
        mSendButton.setOnClickListener(l);
    }

    public void setOnEmojiClick(OnEmojiClickListener emojiListener) {
        this.mEmojiListener = emojiListener;
    }

    private OnEmojiClickListener mEmojiListener = null;

    public interface OnEmojiClickListener {
        void onEmojiClick(String emoji);
    }

    public interface OnDeleteClickListener {
        void onDelete();
    }

    public void setOnDeleteClick(OnDeleteClickListener onDeleteClickListener) {
        mOnDeleteClickListener = onDeleteClickListener;
    }

    private OnDeleteClickListener mOnDeleteClickListener = null;
    private static final int EMOJI_PAGE_COUNT = 3 * 8 - 1;

    public void setEmojiList(int[] emojiList) {
        String[] list = new String[emojiList.length];
        for (int i = 0; i < list.length; i++) {
            list[i] = new String(Character.toChars(emojiList[i]));
        }
        mPageCount = list.length / EMOJI_PAGE_COUNT + 1;
        refreshPage();
        for (int i = 0; i < mPageCount; i++) {
            int start = EMOJI_PAGE_COUNT * i;
            int length = Math.min(list.length - start, EMOJI_PAGE_COUNT);
            ((EmojiAdapter) mGridViewList.get(i).getAdapter()).setEmoji(subArray(list, start, length));
        }
    }

    public String[] subArray(String[] main, final int start, final int length) {
        String[] sub = new String[length];
        for (int i = 0; i < length && i + start < main.length; i++) {
            sub[i] = main[i + start];
        }
        return sub;
    }
}
