package guru.ioio.whisky.utils;

import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;

/**
 * Created by daniel on 8/11/17.
 */

public class AtTextWatcher implements TextWatcher {
    private boolean isDeletingSpace = false;

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        isDeletingSpace = count == 1 && after == 0 && s.charAt(start) == ' ';
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!isDeletingSpace || (start > 1 && s.charAt(start - 1) == ' ')) {
            return;
        }
        int index = s.toString().lastIndexOf('@');
        if (index != -1 && s instanceof SpannableStringBuilder) {
            ((SpannableStringBuilder) s).delete(index, start);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
