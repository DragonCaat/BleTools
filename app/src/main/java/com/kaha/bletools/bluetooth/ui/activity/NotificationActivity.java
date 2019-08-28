package com.kaha.bletools.bluetooth.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * @author Darcy
 * @Date 2019/7/22
 * @package com.kaha.bletools.bluetooth.ui.activity
 * @Desciption
 */
public class NotificationActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // If this activity is the root activity of the task, the app is not running
        if (isTaskRoot()) {
            // Start the app before finishing
            final Intent intent = new Intent(this, DfuActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtras(getIntent().getExtras()); // copy all extras
            startActivity(intent);
        }

        // Now finish, which will drop you to the activity at which you were at the top of the task stack
        finish();
    }
}
