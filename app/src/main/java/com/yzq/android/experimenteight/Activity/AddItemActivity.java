package com.yzq.android.experimenteight.Activity;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.yzq.android.experimenteight.Listener.EditTextFocusListener;
import com.yzq.android.experimenteight.R;
import com.yzq.android.experimenteight.Util.BirthDBHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddItemActivity extends AppCompatActivity {

    private EditText name, gift;
    private DatePicker datePicker;
    private LinearLayout linearLayout;
    private static final View.OnFocusChangeListener of = new EditTextFocusListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        name = (EditText)findViewById(R.id.edit_name);
        datePicker = (DatePicker) findViewById(R.id.item_add_date_picker);
        gift = (EditText)findViewById(R.id.edit_gift);
        Button add = (Button)findViewById(R.id.add_item);
        linearLayout = (LinearLayout)findViewById(R.id.activity_add_item);

        name.setOnFocusChangeListener(of);
        gift.setOnFocusChangeListener(of);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check(view)) {
                    Intent intent = new Intent();
                    intent.putExtra("name", name.getText().toString());

                    int day = datePicker.getDayOfMonth();
                    int month = datePicker.getMonth();
                    int year = datePicker.getYear();
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, month, day);
                    SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");

                    intent.putExtra("birth", format.format(calendar.getTime()));
                    intent.putExtra("gift", gift.getText().toString());
                    AddItemActivity.this.setResult(RESULT_OK, intent);
                    AddItemActivity.this.finish();
                }
            }
        });
    }

    private boolean check(View view) {
        if (name.getText().toString().length() == 0) {
            Snackbar.make(view, "名字为空，请完善", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        BirthDBHelper db = new BirthDBHelper(this);
        if (db.isFindName(name.getText().toString())) {
            Snackbar.make(view, "名字重复啦，请核查", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
