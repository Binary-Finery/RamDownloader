package spencerstudios.com.ramdownloader;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;

public class LaucherActivity extends AppCompatActivity {

    private LinearLayout llConnect;
    private TextView tvConnect;
    private Handler handler;
    private Runnable runnable;
    private int c = 0;
    private boolean isConnecting = true, btnConnecting = false;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laucher);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) getSupportActionBar().hide();

        TextView tvDev = (TextView) findViewById(R.id.tv_device);
        btn = (Button) findViewById(R.id.btn);
        final TextView mem = (TextView) findViewById(R.id.tv_mem);
        llConnect = (LinearLayout) findViewById(R.id.ll_connecting);
        tvConnect = (TextView) findViewById(R.id.tv_connect);
        llConnect.setVisibility(View.INVISIBLE);

        double mb = RamInfo.getRAM(this);

        mem.setText("total RAM " + (int) mb + " MB " + String.format(Locale.getDefault(), "/ %.2f GB", mb * 1.0 / 1000));

        tvDev.setText(Build.MANUFACTURER.toUpperCase() + " " + Build.MODEL);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!btnConnecting) {
                    connect();
                    btnConnecting = true;
                    btn.setText("Cancel");
                    btn.setBackgroundResource(R.color.colorBtnCancel);
                }else{
                    handler.removeCallbacks(runnable);
                    btnConnecting = false;
                    c = 0;
                    btn.setText("Download 1GB free of RAM now");
                    llConnect.setVisibility(View.INVISIBLE);
                    btn.setBackgroundResource(R.color.colorBtn);
                }
            }
        });
    }

    private void connect() {

        llConnect.setVisibility(View.VISIBLE);

        handler = new Handler();

        runnable = new Runnable() {
            @Override
            public void run() {
                if (c==0) tvConnect.setText("processing...");
                if (c == 1000) tvConnect.setText("checking network requirements...");
                if (c == 2000) tvConnect.setText("requesting server...");
                if (c == 3500) tvConnect.setText("connecting to cloud ram...");
                if (c == 7000) tvConnect.setText("configuring your ram...");
                if (c == 10000) tvConnect.setText("preparing your download...");
                if (c == 12500) isConnecting = false;

                if (isConnecting) {
                    handler.postDelayed(this, 700);
                    c += 500;
                } else {
                    handler.removeCallbacks(runnable);
                    startActivity(new Intent(LaucherActivity.this, MainActivity.class));
                    overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit);
                }
            }
        };
        handler.post(runnable);
    }

    public void onBackPressed() {
        handler.removeCallbacks(runnable);
        finishAffinity();
    }
}
