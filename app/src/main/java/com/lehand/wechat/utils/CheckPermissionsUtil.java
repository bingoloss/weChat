package com.lehand.wechat.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by bingo on 2018/1/17.
 * 针对危险权限，需要用户手动授权
 * 使用该工具类，前提也需要在Manifest 中添加所需的权限
 * 这里需要在Activity 中添加onRequestPermissionsResult方法，用户授权回调监听
 */

public class CheckPermissionsUtil {

    private final String TAG = CheckPermissionsUtil.class.getCanonicalName();
    public static int REQUEST_CODE = 11111;
    public static int REQUEST_CODE_FROM_SETTING = 11112;
    private Activity activity;
    //需要请求的权限数组
    private String[] allPermissions;
    //冬天权限的中文名称对应关系
    private Map<String, String> permissionMaps = new HashMap<>();

    private CheckPermissionsUtil() {
        initPermissionMaps();
    }

    //初始化常用的动态权限的中文对应的关系，可扩展
    private void initPermissionMaps() {
        permissionMaps.clear();
        permissionMaps.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, "存储");
        permissionMaps.put(Manifest.permission.READ_EXTERNAL_STORAGE, "存储");
        permissionMaps.put(Manifest.permission.CALL_PHONE, "电话");
        permissionMaps.put(Manifest.permission.READ_PHONE_STATE, "电话");
        permissionMaps.put(Manifest.permission.CAMERA, "相机");
        permissionMaps.put(Manifest.permission.WRITE_CALENDAR, "日历");
        permissionMaps.put(Manifest.permission.READ_CALENDAR, "日历");
        permissionMaps.put(Manifest.permission.WRITE_CONTACTS, "通讯录");
        permissionMaps.put(Manifest.permission.READ_CONTACTS, "通讯录");
        permissionMaps.put(Manifest.permission.GET_ACCOUNTS, "通讯录");
        permissionMaps.put(Manifest.permission.READ_SMS, "短信");
        permissionMaps.put(Manifest.permission.SEND_SMS, "短信");
        permissionMaps.put(Manifest.permission.RECEIVE_SMS, "短信");
        permissionMaps.put(Manifest.permission.VIBRATE, "振动");
        permissionMaps.put(Manifest.permission.RECORD_AUDIO, "麦克风");
        permissionMaps.put(Manifest.permission.ACCESS_FINE_LOCATION, "位置");
        permissionMaps.put(Manifest.permission.ACCESS_COARSE_LOCATION, "位置");
    }

    private static CheckPermissionsUtil util = new CheckPermissionsUtil();

    public static CheckPermissionsUtil getInstance() {
        return util;
    }

    /**
     * 权限请求
     *
     * @param activity
     * @param permissions
     */
    public void requestPermission(Activity activity, String[] permissions) {
        this.activity = activity;
        this.allPermissions = permissions;
        Log.e(TAG, "allPermissions.length = " + allPermissions.length);
        //检查权限是否授权
        if (checkPermissions(permissions)) {
            Log.e(TAG, "权限通过");
        } else {
            List<String> needPermissions = getPermissions(permissions);
            ActivityCompat.requestPermissions(activity, needPermissions.toArray(new String[needPermissions.size()]), REQUEST_CODE);
        }
    }

    /**
     * 检查单个权限
     *
     * @param activity
     * @param permission
     */
    public void requestPermission(Activity activity, String permission) {
        requestPermission(activity, new String[]{permission});
    }

    /**
     * 将权限数组转换成list
     *
     * @param permissions
     * @return
     */
    private List<String> getPermissions(String[] permissions) {
        List<String> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) !=
                    PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                permissionList.add(permission);
            }
        }
        return permissionList;
    }

    /**
     * 验证权限是否通过
     *
     * @param results
     * @return
     */
    public boolean verifyPermissions(int[] results) {
        for (int result : results) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检测所有的权限是否都已授权
     *
     * @param permissions
     * @return
     */
    public boolean checkPermissions(String[] permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取未通过的权限
     */
    public String[] getDeniedList() {
        List<String> list = new ArrayList<>();
        for (String permission : allPermissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_DENIED) {
                list.add(permission);
            }
        }
        return list.toArray(new String[0]);
    }

    /**
     * 获取未通过的权限
     *
     * @param permissions
     */
    public String[] getDeniedList(String[] permissions) {
        List<String> list = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED) {
                list.add(permission);
            }
        }
        return list.toArray(new String[0]);
    }

    /**
     * 输出未获取权限的信息
     *
     * @return
     */
    private String printLogs(String[] permissions) {
        String head = "";
        String line = "";
        String data = "";
        //未过滤的数据
        List<String> unfilteredList = new ArrayList<>();
        if (permissions != null) {
            //现将数据装换成中文
            for (String str : permissions) {
                for (Map.Entry<String, String> maps : permissionMaps.entrySet()) {
                    if (str.equals(maps.getKey())) {
                        unfilteredList.add(maps.getValue());
                    }
                }
            }
            //过滤数据
            if (unfilteredList.size() > 1) {
                removeDuplicateWithOrder(unfilteredList);
            }
            //拼接数据：相机、电话、通讯录等权限
            if (unfilteredList.size() >= 1 && unfilteredList.size() <= 3) {
                for (int i = 0; i < unfilteredList.size(); i++) {
                    if (i == 0) {
                        head = unfilteredList.get(0);
                    } else {
                        line += ("、" + unfilteredList.get(i));
                    }
                }
                line += "权限";
            } else {
                for (int i = 0; i < 3; i++) {
                    if (i == 0) {
                        head = unfilteredList.get(0);
                    } else {
                        line += ("、" + unfilteredList.get(i));
                    }
                }
                line += "等权限";
            }
            data = head + line;
        }
        return data;
    }

    /**
     * 权限禁止后弹出对话框，要求用户授权，此处的dialog可自定义
     */
    public void showFaiingDialog() {
        new AlertDialog.Builder(activity)
                .setCancelable(false)
                .setTitle("消息")
                .setMessage("当前应用未完成对使用" + printLogs(getDeniedList()) + "的授权，该程序的某些功能将无法使用。如若需要，请单击确定按钮进行权限授权！")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //退出程序或者退出当前页面
                        Toast.makeText(activity, "你已否定授权", Toast.LENGTH_SHORT).show();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startSettings();
                    }
                }).show();

    }

    /**
     * 跳转到程序设置页面，手动打开相应的权限
     */
    public void startSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + activity.getPackageName()));
        activity.startActivityForResult(intent, REQUEST_CODE_FROM_SETTING);
    }

    // 删除ArrayList中重复元素，保持顺序
    public void removeDuplicateWithOrder(List<String> list) {
        Set set = new HashSet();
        List newList = new ArrayList();
        for (Iterator iter = list.iterator(); iter.hasNext(); ) {
            Object element = iter.next();
            if (set.add(element)) {
                newList.add(element);
            }
        }
        list.clear();
        list.addAll(newList);
        for (String s : list) {
            Log.e("TAG", s);
        }
    }

    //通过HashSet踢除重复元素
    public List removeDuplicate(List<String> list) {
        HashSet h = new HashSet(list);
        list.clear();
        list.addAll(h);
        return list;
    }
}
