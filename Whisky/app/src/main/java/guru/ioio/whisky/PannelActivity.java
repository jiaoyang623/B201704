package guru.ioio.whisky;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.BaseMovementMethod;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import guru.ioio.whisky.base.Utils;
import guru.ioio.whisky.databinding.ActivityPannelBinding;


/**
 * Created by daniel on 6/14/17.
 * for small test
 */

public class PannelActivity extends Activity {
    private ActivityPannelBinding mBinding;

    public CharSequence getContent() {
        String data = getString(R.string.test_too_much);
        SpannableString span = new SpannableString(data);
        span.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Utils.toast("click");
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(0xffffaa11);
                ds.setUnderlineText(false);
            }
        }, 0, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return span;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_pannel);
        mBinding.setPresenter(this);

        mBinding.spanText.setMovementMethod(new BaseMovementMethod(){
            @Override
            public boolean onTouchEvent(TextView widget, Spannable buffer,
                                        MotionEvent event) {
                int action = event.getAction();

                if (action == MotionEvent.ACTION_UP ||
                        action == MotionEvent.ACTION_DOWN) {
                    int x = (int) event.getX();
                    int y = (int) event.getY();

                    x -= widget.getTotalPaddingLeft();
                    y -= widget.getTotalPaddingTop();

                    x += widget.getScrollX();
                    y += widget.getScrollY();

                    Layout layout = widget.getLayout();
                    int line = layout.getLineForVertical(y);
                    int off = layout.getOffsetForHorizontal(line, x);

                    ClickableSpan[] link = buffer.getSpans(off, off, ClickableSpan.class);

                    if (link.length != 0) {
                        if (action == MotionEvent.ACTION_UP) {
                            link[0].onClick(widget);
                        } else if (action == MotionEvent.ACTION_DOWN) {
                            Selection.setSelection(buffer,
                                    buffer.getSpanStart(link[0]),
                                    buffer.getSpanEnd(link[0]));
                        }

                        return true;
                    } else {
                        Selection.removeSelection(buffer);
                    }
                }

                return super.onTouchEvent(widget, buffer, event);
            }
        });
    }
}
