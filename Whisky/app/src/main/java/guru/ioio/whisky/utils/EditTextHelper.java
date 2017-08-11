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
    private List<Object> mSpannableList = new LinkedList<>();

    private TextWatcher mWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            Utils.j(EditTextHelper.class, "beforeTextChanged", s, start, count, after);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Utils.j(EditTextHelper.class, "onTextChanged", s, start, before, count);
            if (before > 0 && count == 0) {
                // 判断删除
            } else {
                return;
            }

            if (s instanceof SpannableStringBuilder) {
                SpannableStringBuilder builder = (SpannableStringBuilder) s;
                for (Object span : mSpannableList) {
                    int end = builder.getSpanEnd(span);
                    if (end == start) {
                        builder.insert(start, " ");
                        mEdit.setSelection(builder.getSpanStart(span), end + 1);
                        break;
                    }
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            Utils.j(EditTextHelper.class, "afterTextChanged", s);
            for (Object span : mSpannableList) {
                if (s.getSpanStart(span) == -1) {
                    mSpannableList.remove(span);
                }
            }
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

    public EditTextHelper addSpannable(final String msg) {
        int pos = mEdit.getSelectionStart();
        ClickableSpan span = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Utils.toast(msg);
                int start = mEdit.getText().getSpanStart(this);
                if (start != -1) {
                    mEdit.setSelection(start, start);
                }
            }
        };

        Editable text = mEdit.getText();
        text.insert(pos, msg + " ");
        text.setSpan(span, pos, pos + msg.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mSpannableList.add(span);

        return this;
    }

}
