package guru.ioio.whisky.utils;

import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.EditText;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by daniel on 8/10/17.
 */

public class EditTextHelper {
    private EditText mEdit;
    private List<AtSpannable> mSpannableList = new LinkedList<>();

    private TextWatcher mWatcher = new TextWatcher() {
        private String beforeText;
        private boolean isChanging = false;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (isChanging) {
                return;
            } else {
                isChanging = true;
            }
            Utils.j(EditTextHelper.class, "beforeTextChanged", s, start, count, after);
            beforeText = s.subSequence(start, start + count).toString();

            if (s instanceof SpannableStringBuilder) {
                SpannableStringBuilder builder = (SpannableStringBuilder) s;
                for (AtSpannable span : mSpannableList) {
                    span.start = builder.getSpanStart(span);
                    span.end = builder.getSpanEnd(span);
                }
            }
            isChanging = false;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (isChanging) {
                return;
            } else {
                isChanging = true;
            }
            Utils.j(EditTextHelper.class, "onTextChanged", s, start, before, count);
            int end = start + before;
            if (s instanceof SpannableStringBuilder) {
                SpannableStringBuilder builder = (SpannableStringBuilder) s;
                for (AtSpannable span : mSpannableList) {
                    int s0 = span.start;
                    int s1 = span.end;
                    if ((s0 <= start && end < s1) || (s0 < start && end <= s1)) {
                        //恢复，选择
                        if (count != 0) {
                            builder.delete(start, start + count);
                        }
                        if (before != 0) {
                            builder.insert(start, beforeText);
                        }
                        mEdit.setSelection(builder.getSpanStart(span), builder.getSpanEnd(span));
                        break;
                    } else if ((start <= s0 && s1 <= end) || end < s0 || s1 < start) {
                        // pass
                    } else {
                        // 删除
                        builder.delete(builder.getSpanStart(span), builder.getSpanEnd(span));
                    }
                }
            }
            isChanging = false;
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (isChanging) {
                return;
            } else {
                isChanging = true;
            }
            Utils.j(EditTextHelper.class, "afterTextChanged", s);
            for (AtSpannable span : mSpannableList) {
                if (s.getSpanStart(span) == -1) {
                    mSpannableList.remove(span);
                }
            }
            isChanging = false;
        }
    };

    public EditTextHelper(EditText edit) {
        if (edit == null) {
            throw new IllegalArgumentException("Params should not be null");
        } else {
            mEdit = edit;
        }

        edit.addTextChangedListener(mWatcher);
        edit.setMovementMethod(AtMovementMethod.getInstance());
    }

    public EditTextHelper addSpannable(String msg) {
        msg += " ";
        int pos = mEdit.getSelectionStart();

        AtSpannable span = new AtSpannable(msg);

        Editable text = mEdit.getText();
        text.insert(pos, msg);
        text.setSpan(span, pos, pos + msg.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mSpannableList.add(span);

        return this;
    }


    private static class AtSpannable extends ClickableSpan {
        private String msg;
        public int start, end;

        public AtSpannable(String msg) {
            this.msg = msg;
        }

        @Override
        public void onClick(View widget) {
            Utils.toast(msg);
        }
    }
}
