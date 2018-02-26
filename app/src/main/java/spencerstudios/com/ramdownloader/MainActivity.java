package spencerstudios.com.ramdownloader;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tuyenmonkey.mkloader.MKLoader;

import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private int mb = 0;
    private Handler handler;
    private Runnable runnable;
    private TextView tvInfo, percent;
    private Random random;
    private MKLoader mkLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) getSupportActionBar().hide();

        initViews();

        random = new Random();
        handler = new Handler();

        runnable = new Runnable() {
            @Override
            public void run() {
                if (mb < 995) {
                    handler.postDelayed(this, ((random.nextInt(5) + 1) * 50)); //add random delay
                    int r = random.nextInt(4) + 1; //increment downloaded RAM by a random amount
                    mb += r;
                    percent.setText(String.format(Locale.getDefault(), "%d%%", (mb * 100) / 1000));
                    tvInfo.setText(mb + " MB " + String.format(Locale.getDefault(), "(%.2f GB)", mb * 1.0 / 1000));

                } else {
                    tvInfo.setText(getString(R.string.complete));
                    percent.setText("100%");
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

    private void beginDownload() {
        mkLoader.setVisibility(View.VISIBLE);
        handler.post(runnable);
    }

    public void onBackPressed() {
        Toast.makeText(getApplication(), "processing, please be patient", Toast.LENGTH_SHORT).show();
    }

    private void initViews() {
        mkLoader = (MKLoader) findViewById(R.id.loader);
        tvInfo = (TextView) findViewById(R.id.tv_info);
        percent = (TextView) findViewById(R.id.per);
        mkLoader.setVisibility(View.INVISIBLE);
    }
}
