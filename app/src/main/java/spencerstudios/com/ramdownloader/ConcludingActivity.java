package spencerstudios.com.ramdownloader;

import android.media.Image;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Locale;
import java.util.Random;

public class ConcludingActivity extends AppCompatActivity {


    private Handler handler;
    private Runnable runnable;
    private ProgressBar p1;
    private TextView tvUnzip, tvStats;
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
        tvStats = (TextView)findViewById(R.id.tv_stats) ;

        Button btn = (Button)findViewById(R.id.btnClose);
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
            Toast.makeText(getApplication(), "Enjoy you extra Extra RAM ;)", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(), "Installing RAM, please be patient", Toast.LENGTH_SHORT).show();
        }
    }
}
