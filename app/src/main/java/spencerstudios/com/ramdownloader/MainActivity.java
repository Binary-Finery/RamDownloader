package spencerstudios.com.ramdownloader;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tuyenmonkey.mkloader.MKLoader;

import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private int mb = 0;
    private Handler handler;
    private Runnable runnable;
    private TextView tvInfo, per;
    private Random random;
    private MKLoader mkLoader;
    private double gb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) getSupportActionBar().hide();

        mkLoader = (MKLoader) findViewById(R.id.loader);
        tvInfo = (TextView) findViewById(R.id.tv_info);
        per = (TextView) findViewById(R.id.per);
        mkLoader.setVisibility(View.INVISIBLE);

        random = new Random();

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (mb < 50) {
                    handler.postDelayed(this, ((random.nextInt(5) + 1) * 50));
                    int r = random.nextInt(4) + 1;
                    mb += r;
                    int p = (mb * 100) / 2000;
                    gb = mb * 1.0 / 1000;
                    per.setText(String.format(Locale.getDefault(), "%d%%", p));
                    tvInfo.setText(mb + " MB " + String.format(Locale.getDefault(), "(%.2f GB)", gb));

                } else {
                    tvInfo.setText(getString(R.string.complete));
                    handler.removeCallbacks(runnable);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(MainActivity.this, ConcludingActivity.class));
                            overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit);
                        }
                    }, 1000);
                }
            }
        };

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                beginDownload();
            }
        }, 100);
    }

    public void onBackPressed() {
        Toast.makeText(getApplication(), "processing, please be patient", Toast.LENGTH_SHORT).show();
    }

    private void beginDownload() {
        mkLoader.setVisibility(View.VISIBLE);
        handler.post(runnable);
    }
}
