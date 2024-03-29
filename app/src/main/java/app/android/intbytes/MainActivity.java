package app.android.intbytes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends Activity  {
    private static final String TAG = "MainActivity";
    private Intent serviceIntent = null;
    private int PERMISSION_CODE = 110;
    private AutoClickService autoClickService = null;
    private static Context context = null;

    // start project
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);// generate view to program
        MainActivity.context = this.getApplicationContext();
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);// check for accessibility setting
        startActivity(intent);// open setting->accessibility setting

        // check android version and ask for permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            askPermission();
        }

        Button start_btn = (Button) findViewById(R.id.start_btn);
        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//set event to start button
                try {

                    startService(new Intent(MainActivity.this, AutoClickService.class));
                } catch (Exception ex) {
                    Toast.makeText(MainActivity.this, ex.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private static final int SYSTEM_ALERT_WINDOW_PERMISSION = 2084;

    private void askPermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, SYSTEM_ALERT_WINDOW_PERMISSION);
    }


    @Override
    public void onDestroy() {
        this.autoClickService.stopSelf();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//			return it.disableSelf();
        }
        this.autoClickService = null;
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }


}