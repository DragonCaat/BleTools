package com.kaha.bletools.bluetooth.ui.service;

import android.app.Activity;
import android.content.Intent;
import android.os.IBinder;

import com.kaha.bletools.bluetooth.ui.activity.NotificationActivity;

import no.nordicsemi.android.dfu.DfuBaseService;

/**
 * 蓝牙dfu升级服务
 *
 * @author Darcy
 * @Date 2019/7/22
 * @package com.kaha.bletools.bluetooth.ui.service
 * @Desciption
 */
public class DfuService extends DfuBaseService {
    public DfuService() {
    }

    @Override
    protected Class<? extends Activity> getNotificationTarget() {
        return NotificationActivity.class;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
