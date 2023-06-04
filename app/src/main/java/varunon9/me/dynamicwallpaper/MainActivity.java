package varunon9.me.dynamicwallpaper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate called");
        setContentView(R.layout.activity_main);
        startServiceViaWorker();
        startService();
    }

//    public void onStartServiceClick(View v) {
//
//
//// サービスのスタート
//    }

//    public void onStopServiceClick(View v) {
//        stopService();
//    }

//    @Override
//    protected void onDestroy() {
//        Log.d(TAG, "onDestroy called");
//        stopService();
//        super.onDestroy();
//        //サービスのストップ
//    }

    public void startService() {
        Log.d(TAG, "startService called");
        if (!MyService.isServiceRunning) {
            Intent serviceIntent = new Intent(this, MyService.class);
            ContextCompat.startForegroundService(this, serviceIntent);
        }
    }

//    public void stopService() {
//        Log.d(TAG, "stopService called");
//        if (MyService.isServiceRunning) {
//            Intent serviceIntent = new Intent(this, MyService.class);
//            stopService(serviceIntent);
//        }
//    }

    public void startServiceViaWorker() {
        Log.d(TAG, "startServiceViaWorker called");
        String UNIQUE_WORK_NAME = "StartMyServiceViaWorker";
        WorkManager workManager = WorkManager.getInstance(this);

        // As per Documentation: The minimum repeat interval that can be defined is 15 minutes
        // (same as the JobScheduler API), but in practice 15 doesn't work. Using 16 here
        PeriodicWorkRequest request =
                new PeriodicWorkRequest.Builder(
                        MyWorker.class,
                        15,
                        TimeUnit.MINUTES)
                        .build();
        // 15分感覚に設定(Workerで15分よりも前にするとずれが生じる)
        // Handlerクラスをインスタンス化し、postDelayedメソッドを呼んでいる
//        new Handler().postDelayed(new Runnable() {
//            // Runnable型のインスタンス化と定義
//            @Override
//            public void run() {
//
//                // 遅らせて実行したい処理
//                Toast.makeText(MainActivity.this,"本日は晴天なり",Toast.LENGTH_SHORT).show();
//            }
//        }, 3000); // 遅らせたい時間(ミリ秒) 3000ミリ秒 -> 3秒
        // to schedule a unique work, no matter how many times app is opened i.e. startServiceViaWorker gets called
        // do check for AutoStart permission
        workManager.enqueueUniquePeriodicWork(UNIQUE_WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, request);

    }
}
