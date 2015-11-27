package com.o.democanvas;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.os.magic.progressbar.soda.view.OrangeSodaProgressBar;

public class MainActivity extends AppCompatActivity {
    private OrangeSodaProgressBar m_orangeSodaProgressBar;
    private DemoAsync m_demoAsync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_orangeSodaProgressBar = (OrangeSodaProgressBar) findViewById(R.id.id_progressBar);
        findViewById(R.id.id_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_demoAsync = new DemoAsync();
                m_demoAsync.execute();
            }
        });
    }

    private class DemoAsync extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            for (int i = 0; i < 100; ++i) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                publishProgress(i);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            m_orangeSodaProgressBar.setProgress(values[0]);
        }
    }
}
