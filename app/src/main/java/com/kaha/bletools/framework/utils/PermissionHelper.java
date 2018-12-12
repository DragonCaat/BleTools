package com.kaha.bletools.framework.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import java.util.ArrayList;

/**
 * 处理Android 6.0动态权限的工具类.<br>
 * 使用：
 * 1、权限申请：
 * PermissionHelper mHelper = new PermissionHelper(this);
 * mHelper.requestPermission(listener,@NonNull String... permissions)
 * <p>
 * 2、处理申请的结果:
 * 在activity/Fragment重写onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) 方法，
 * 然后调用 mHelper.handleRequestPermissionsResult(requestCode, permissions, grantResults);
 * <p>
 * 例如：
 * //@Override
 * public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {<br>
 * mHelper.handleRequestPermissionsResult(requestCode, permissions, grantResults);
 * }
 * </p>
 *
 * @author : Darcy
 * @package com.kaha.bletools.framework.utils
 * @Date 2018-11-8 13:51
 * @Description 动态请求权限工具类
 */
public class PermissionHelper {

    private static final int REQUEST_PERMISSION_CODE = 1000;
    private Activity activity;
    //权限请求回调
    private PermissionCallBack permissionCallBack;

    public PermissionHelper(@NonNull Activity activity) {
        this.activity = activity;
    }

    /**
     * 请求动态权限
     *
     * @param permissionCallBack 回调
     * @param permissions        权限
     * @return void
     * @Date 2018-11-08
     */
    public void requestPermission(PermissionCallBack permissionCallBack, @NonNull String... permissions) {
        this.permissionCallBack = permissionCallBack;
        boolean isMinSdkM = Build.VERSION.SDK_INT < Build.VERSION_CODES.M;
        if (isMinSdkM || permissions.length == 0) {
            if (permissionCallBack != null) {
                permissionCallBack.onPermissionGrant(permissions);
            }
            return;
        }
        ArrayList<String> listDenied = new ArrayList<>(permissions.length);
        ArrayList<String> listGranted = new ArrayList<>(permissions.length);

        for (String permission : permissions) {
            if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(activity, permission)) {
                listGranted.add(permission);
            } else {
                listDenied.add(permission);
            }
        }

        if (listGranted.size() > 0) {
            String[] strGranted = listGranted.toArray(new String[listGranted.size()]);
            if (null != permissionCallBack) {
                permissionCallBack.onPermissionGrant(strGranted);
            }
        }

        if (listDenied.size() > 0) {
            String[] strDenied = listDenied.toArray(new String[listDenied.size()]);
            ActivityCompat.requestPermissions(activity, strDenied, REQUEST_PERMISSION_CODE);
        }
    }

    /**
     * 权限回调处理
     *
     * @return void
     * @Date 2018-11-08
     */
    public void handleRequestPermissionsResult(int requestCode, @NonNull final String[] permissions,
                                              @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE: {
                ArrayList<String> listGranted = new ArrayList<>(permissions.length);
                ArrayList<String> listDenied = new ArrayList<>(permissions.length);
                ArrayList<String> listNoTips = new ArrayList<>(permissions.length);
                for (String permission : permissions) {
                    if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(activity, permission)) {
                        listGranted.add(permission);
                    } else {
                        listDenied.add(permission);
                        boolean showPermissionRationale = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
                        if (!showPermissionRationale) {
                            listNoTips.add(permission);
                        }
                    }
                }
                if (listGranted.size() > 0) {
                    String[] strGranted = listGranted.toArray(new String[listGranted.size()]);
                    if (null != permissionCallBack) {
                        permissionCallBack.onPermissionGrant(strGranted);
                    }
                }

                if (listNoTips.size() > 0) {
                    final String[] strDenied = listDenied.toArray(new String[listDenied.size()]);
                    //用户选择了不在提示按钮，或者系统默认不在提示，需要引导用户到设置页手动授权
                    new AlertDialog.Builder(activity)
                            .setMessage("【用户选择了不在提示按钮，或者系统默认不在提示（如MIUI）】\r\n" +
                                    "获取相关权限失败将导致部分功能无法正常使用，需要到设置页面手动授权")
                            .setPositiveButton("去授权", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //引导用户至设置页手动授权
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", activity.getApplicationContext().getPackageName(), null);
                                    intent.setData(uri);
                                    activity.startActivity(intent);
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //引导用户手动授权，权限请求失败
                                    if (permissionCallBack != null) {
                                        permissionCallBack.onPermissionDenied(strDenied);
                                    }
                                }
                            }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            //引导用户手动授权，权限请求失败
                            if (permissionCallBack != null) {
                                permissionCallBack.onPermissionDenied(strDenied);
                            }
                        }
                    }).show();
                } else {
                    String[] strDenied = listDenied.toArray(new String[listDenied.size()]);
                    if (listDenied.size() > 0) {
                        if (null != permissionCallBack) {
                            permissionCallBack.onPermissionDenied(strDenied);
                        }
                    }
                }
                break;

            }

        }
    }


    /**
     * 权限请求回调
     *
     * @Date 2018-11-08
     * @return
     */
    public interface PermissionCallBack {

        void onPermissionGrant(String... permission);

        void onPermissionDenied(String... permission);
    }


}
