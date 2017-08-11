package guru.ioio.whisky.utils;

import android.text.Editable;
import android.text.Layout;
import android.text.NoCopySpan;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.BaseMovementMethod;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.ClickableSpan;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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

    public static class AtMovementMethod extends BaseMovementMethod {
        private static final int CLICK = 1;
        private static final int UP = 2;
        private static final int DOWN = 3;

        @Override
        public boolean canSelectArbitrarily() {
            return true;
        }

        @Override
        protected boolean handleMovementKey(TextView widget, Spannable buffer, int keyCode,
                                            int movementMetaState, KeyEvent event) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_DPAD_CENTER:
                case KeyEvent.KEYCODE_ENTER:
                    if (KeyEvent.metaStateHasNoModifiers(movementMetaState)) {
                        if (event.getAction() == KeyEvent.ACTION_DOWN &&
                                event.getRepeatCount() == 0 && action(CLICK, widget, buffer)) {
                            return true;
                        }
                    }
                    break;
            }
            return super.handleMovementKey(widget, buffer, keyCode, movementMetaState, event);
        }

        @Override
        protected boolean up(TextView widget, Spannable buffer) {
            if (action(UP, widget, buffer)) {
                return true;
            }

            return super.up(widget, buffer);
        }

        @Override
        protected boolean down(TextView widget, Spannable buffer) {
            if (action(DOWN, widget, buffer)) {
                return true;
            }

            return super.down(widget, buffer);
        }

        @Override
        protected boolean left(TextView widget, Spannable buffer) {
            if (action(UP, widget, buffer)) {
                return true;
            }

            return super.left(widget, buffer);
        }

        @Override
        protected boolean right(TextView widget, Spannable buffer) {
            if (action(DOWN, widget, buffer)) {
                return true;
            }

            return super.right(widget, buffer);
        }

        private boolean action(int what, TextView widget, Spannable buffer) {
            Layout layout = widget.getLayout();

            int padding = widget.getTotalPaddingTop() +
                    widget.getTotalPaddingBottom();
            int areatop = widget.getScrollY();
            int areabot = areatop + widget.getHeight() - padding;

            int linetop = layout.getLineForVertical(areatop);
            int linebot = layout.getLineForVertical(areabot);

            int first = layout.getLineStart(linetop);
            int last = layout.getLineEnd(linebot);

            ClickableSpan[] candidates = buffer.getSpans(first, last, ClickableSpan.class);

            int a = Selection.getSelectionStart(buffer);
            int b = Selection.getSelectionEnd(buffer);

            int selStart = Math.min(a, b);
            int selEnd = Math.max(a, b);

            if (selStart < 0) {
                if (buffer.getSpanStart(FROM_BELOW) >= 0) {
                    selStart = selEnd = buffer.length();
                }
            }

            if (selStart > last)
                selStart = selEnd = Integer.MAX_VALUE;
            if (selEnd < first)
                selStart = selEnd = -1;

            switch (what) {
                case CLICK:
                    if (selStart == selEnd) {
                        return false;
                    }

                    ClickableSpan[] link = buffer.getSpans(selStart, selEnd, ClickableSpan.class);

                    if (link.length != 1)
                        return false;

                    link[0].onClick(widget);
                    break;

                case UP:
                    int beststart, bestend;

                    beststart = -1;
                    bestend = -1;

                    for (int i = 0; i < candidates.length; i++) {
                        int end = buffer.getSpanEnd(candidates[i]);

                        if (end < selEnd || selStart == selEnd) {
                            if (end > bestend) {
                                beststart = buffer.getSpanStart(candidates[i]);
                                bestend = end;
                            }
                        }
                    }

                    if (beststart >= 0) {
                        Selection.setSelection(buffer, bestend, beststart);
                        return true;
                    }

                    break;

                case DOWN:
                    beststart = Integer.MAX_VALUE;
                    bestend = Integer.MAX_VALUE;

                    for (int i = 0; i < candidates.length; i++) {
                        int start = buffer.getSpanStart(candidates[i]);

                        if (start > selStart || selStart == selEnd) {
                            if (start < beststart) {
                                beststart = start;
                                bestend = buffer.getSpanEnd(candidates[i]);
                            }
                        }
                    }

                    if (bestend < Integer.MAX_VALUE) {
                        Selection.setSelection(buffer, beststart, bestend);
                        return true;
                    }

                    break;
            }

            return false;
        }

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

        @Override
        public void initialize(TextView widget, Spannable text) {
            Selection.removeSelection(text);
            text.removeSpan(FROM_BELOW);
        }

        @Override
        public void onTakeFocus(TextView view, Spannable text, int dir) {
            Selection.removeSelection(text);

            if ((dir & View.FOCUS_BACKWARD) != 0) {
                text.setSpan(FROM_BELOW, 0, 0, Spannable.SPAN_POINT_POINT);
            } else {
                text.removeSpan(FROM_BELOW);
            }
        }

        public static MovementMethod getInstance() {
            if (sInstance == null)
                sInstance = new LinkMovementMethod();

            return sInstance;
        }

        private static LinkMovementMethod sInstance;
        private static Object FROM_BELOW = new NoCopySpan.Concrete();
    }
}
