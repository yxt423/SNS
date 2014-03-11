package irdc.sns;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class kaiji extends Activity
{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kaiji);
        Start();
    }
    
    public void Start() {
        new Thread() {
                public void run() {
                        try {
                                Thread.sleep(1000);
                        } catch (InterruptedException e) {
                                e.printStackTrace();
                        }
                        Intent intent = new Intent();
                        intent.setClass(kaiji.this, sns.class);
                        startActivity(intent);
                        finish();
                }
        }.start();
}
}