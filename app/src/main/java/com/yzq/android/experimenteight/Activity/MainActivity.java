package com.yzq.android.experimenteight.Activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.yzq.android.experimenteight.Adapter.BirthAdapter;
import com.yzq.android.experimenteight.Listener.EditTextFocusListener;
import com.yzq.android.experimenteight.R;
import com.yzq.android.experimenteight.Util.BirthDBHelper;
import com.yzq.android.experimenteight.Util.BirthItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private static final View.OnFocusChangeListener of = new EditTextFocusListener();

    private FloatingActionButton fab;
    private ListView birthListView;
    private ArrayList<BirthItem> birthItemList = new ArrayList<BirthItem>();
    private BirthAdapter birthAdapter;

    private TextView name, phone;
    private EditText gift;
    private DatePicker datePicker;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");

    private BirthDBHelper birthDatabase;

    private void showPhonePermissions() {
        int permissionsCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        if (permissionsCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Permission Needed")
                        .setMessage("Rationale")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 1);
                            }
                        });
                builder.create().show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 1);
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab = (FloatingActionButton)findViewById(R.id.add_list_item);
        birthListView = (ListView)findViewById(R.id.birth_list);
        birthDatabase = new BirthDBHelper(this);

        showPhonePermissions();

        birthItemList = birthDatabase.getAlldatabase();
        birthAdapter = new BirthAdapter(this, birthItemList);
        birthListView.setAdapter(birthAdapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        birthListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showMessageDialog(i);
            }
        });

        birthListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                showConfirmDialog(i);
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String user_name = bundle.get("name").toString();
            String user_birth = bundle.get("birth").toString();
            String user_gift = bundle.get("gift").toString();
            birthItemList.add(new BirthItem(user_name, user_birth, user_gift));
            birthDatabase.insert(user_name, user_birth, user_gift);
            birthAdapter.notifyDataSetChanged();
        }
    }

    private void showMessageDialog(int i) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = MainActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.birth_message_dialog, null);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();
        final BirthItem birthItem = birthItemList.get(i);

        name = (TextView)dialogView.findViewById(R.id.msg_name);
        datePicker = (DatePicker)dialogView.findViewById(R.id.message_dialog_picker);
        gift = (EditText)dialogView.findViewById(R.id.msg_gift);
        phone = (TextView)dialogView.findViewById(R.id.msg_phone);
        Button quit_change = (Button)dialogView.findViewById(R.id.quit_change);
        Button confirm_change = (Button)dialogView.findViewById(R.id.confirm_change);

        gift.setOnFocusChangeListener(of);

        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(format.parse(birthItem.getBirth()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        datePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        name.setText(birthItem.getName());
        gift.setText(birthItem.getGift());
        phone.setText(getPhoneNumber(birthItem.getName()));

        quit_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        confirm_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth();
                int year = datePicker.getYear();
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);
                String userBirth = format.format(calendar.getTime());

                birthItem.setAll(userBirth, gift.getText().toString());
                birthDatabase.update(name.getText().toString(), userBirth, gift.getText().toString());
                birthAdapter.notifyDataSetChanged();
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void showConfirmDialog(final int i) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = MainActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.delete_confirm_dialog, null);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();

        Button delete = (Button)dialogView.findViewById(R.id.result_yes);
        Button no_delete = (Button)dialogView.findViewById(R.id.result_no);
        
        no_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = birthItemList.get(i).getName();
                birthItemList.remove(i);
                birthAdapter.notifyDataSetChanged();
                birthDatabase.delete(username);
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private String getPhoneNumber(String user_name) {
        Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        String phoneNumber = "";
        while (cursor.moveToNext()) {

            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

            if (contactName.equals(user_name) && Integer.parseInt(hasPhone) == 1) {
                Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+contactId, null, null);
                while (phones.moveToNext()) {
                    if (phoneNumber.equals("")) {
                        phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    } else {
                        phoneNumber += "\n" + phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    }
                }
                phones.close();
            }
        }
        cursor.close();
        return phoneNumber;
    }
}
