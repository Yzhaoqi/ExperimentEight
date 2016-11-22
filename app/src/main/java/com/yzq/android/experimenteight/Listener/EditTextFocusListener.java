package com.yzq.android.experimenteight.Listener;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by YZQ on 2016/11/22.
 */

public class EditTextFocusListener implements View.OnFocusChangeListener {
    @Override
    public void onFocusChange(View view, boolean b) {
        if (!b) {
            InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
