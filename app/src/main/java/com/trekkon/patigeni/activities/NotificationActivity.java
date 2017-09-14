package com.trekkon.patigeni.activities;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.trekkon.patigeni.R;
import com.trekkon.patigeni.adapter.RV_Titik;
import com.trekkon.patigeni.dao.DbAdapter;
import com.trekkon.patigeni.fragment.TaskListFragment;
import com.trekkon.patigeni.helper.BaseFunction;
import com.trekkon.patigeni.model.Titik;
import com.trekkon.patigeni.model.User;
import com.trekkon.patigeni.utils.SessionManagement;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Iwan on 13-Aug-17.
 */

public class NotificationActivity extends Activity {
    private Bundle extras;
    private String doAction = "";
    private TextView txtMessage;
    private Button btnYa, btnNanti;
    DbAdapter hotspotDao = null;
    Double lat, longit;

    String id, taskId, tingkat_kepercayaan;
    private SessionManagement sessionManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.activity_dialog);
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        lp.dimAmount = 0;
        lp.flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;

        LayoutInflater inflater = getLayoutInflater();
        setTheme(R.style.DialogTheme);
        LinearLayout ll = (LinearLayout) inflater.inflate(
                R.layout.activity_dialog, null);
        setContentView(ll, lp);
        txtMessage = (TextView) findViewById(R.id.txtMessage);
        btnYa = (Button) findViewById(R.id.btn_ya);
        id = BaseFunction.GetUserID(NotificationActivity.this);
        extras = getIntent().getExtras();
        taskId = extras.getString("taskId");
        lat = Double.parseDouble(extras.getString("latitude"));
        longit = Double.parseDouble(extras.getString("longitude"));
        tingkat_kepercayaan = extras.getString("tingkatKepercayaan");
        if (extras != null) {
            doAction = extras.getString("action");
            Log.i("action", doAction);

            if (doAction.contains(".MainActivity")) {
                txtMessage.setText("Tingkat Kepercayaan : " + extras.getString("tingkatKepercayaan") + "%");
                /*showDialog(this,"notification", "latitude : " + extras.getString("latitude") +
                        "\nlongitude : " + extras.getString("longitude") +
                        "\ntimestamp : " + extras.getString("timestamp") +
                        "\ntingkat kepercayaan : " + extras.getString("tingkatKepercayaan"), "OK", "", 1);*/


                hotspotDao = new DbAdapter(NotificationActivity.this);


                Calendar cal = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentDateandTime = df.format(Calendar.getInstance().getTime());
                ContentValues cValue = new ContentValues();
                cValue.put("contact_id", id);
                cValue.put("hotspot_id", extras.getString("taskId"));
                cValue.put("latitude", Double.parseDouble(extras.getString("latitude")));
                cValue.put("longitude", Double.parseDouble(extras.getString("longitude")));
                cValue.put("by_human", "H");
                cValue.put("status", "R");
                cValue.put("tingkat_kepercayaan", extras.getString("tingkatKepercayaan"));
                cValue.put("timestamp_original", extras.getString("timestamp"));
                cValue.put("timestamp_received", currentDateandTime);
                try {
                    hotspotDao.insert("Task", cValue);
                } catch (Exception e) {
//                        MessageHelper.show(getActivity(), "Tidak Bisa Insert Database\n" + e.toString());
                    Log.i("error", e.toString());
                    e.printStackTrace();
                }

                TaskListFragment fragment = new TaskListFragment();
                fragment.setRecyclerView(BaseFunction.GetUserID(NotificationActivity.this), fragment.getRv());

            }

            btnYa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sessionManagement = new SessionManagement(NotificationActivity.this);
                    Intent intent = new Intent(NotificationActivity.this, MapsActivity.class);

                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("taskId", Integer.parseInt(taskId));
                    intent.putExtra("latitude", lat);
                    intent.putExtra("longitude",longit);
                    intent.putExtra("tingkatKepercayaan", tingkat_kepercayaan);
                    sessionManagement.setCurrentHotspot(Integer.parseInt(taskId));
                    startActivity(intent);
                    finish();
                /*
                ke listview detail
                 */
                }
            });

            setIntent(new Intent());

        }

        btnNanti = (Button) findViewById(R.id.btn_nanti);
        btnNanti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        this.setFinishOnTouchOutside(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        extras = getIntent().getExtras();

    }

    @Override
    public void onBackPressed() {

    }


}
