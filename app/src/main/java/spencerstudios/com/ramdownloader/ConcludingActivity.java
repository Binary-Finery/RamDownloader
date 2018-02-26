package spencerstudios.com.ramdownloader;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class ConcludingActivity extends AppCompatActivity {


    private Handler handler;
    private Runnable runnable;
    private ProgressBar p1;
    private TextView tvUnzip;
    private int c = 0;
    private LinearLayout ll;
    private boolean isInstalled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concluding);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        p1 = (ProgressBar) findViewById(R.id.progressBar1);
        ll = (LinearLayout) findViewById(R.id.ll_conclude);
        ll.setVisibility(View.INVISIBLE);
        tvUnzip = (TextView)findViewById(R.id.zip);
        TextView tvStats = (TextView) findViewById(R.id.tv_stats);
        Button btn = (Button)findViewById(R.id.btnClose);

        double mb = RamInfo.getRAM(this) + 1000;

        String newMB = String.format(Locale.getDefault(), "%dMB", (int) mb);
        String newGB = String.format(Locale.getDefault(), "(%.2fGB)", mb * 1.0 / 1000);

        tvStats.setText("Your device has been successfully equipped with an additional 1GB of DDR4 RAM.\n\nYour new total RAM capacity is " + newMB + " " + newGB);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
            }
        });

        p1.setMax(500);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (c < 500) {
                    handler.postDelayed(this, 1);
                    p1.setProgress(c);
                    tvUnzip.setText(String.format(Locale.getDefault(), "%d%%", c / 5));
                    c++;
                } else {
                    handler.removeCallbacks(runnable);
                    p1.setProgress(500);
                    tvUnzip.setText("100%");

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ll.setVisibility(View.VISIBLE);
                            isInstalled = true;
                        }
                    }, 1000);
                }
            }
        };
        handler.post(runnable);
    }

    public void onBackPressed() {
        if (isInstalled) {
            handler.removeCallbacks(runnable);
            Toast.makeText(getApplication(), "Enjoy your extra Extra RAM ;)", Toast.LENGTH_SHORT).show();
            finishAffinity();
        }else{
            Toast.makeText(getApplicationContext(), "Installing RAM, please be patient", Toast.LENGTH_SHORT).show();
        }
    }
}
