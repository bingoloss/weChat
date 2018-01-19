package com.lehand.wechat.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by bingo on 2017/11/2.
 * <p>
 * description : 单例模式的异常捕捉类
 */

public class ExceptionCrashHandler implements Thread.UncaughtExceptionHandler {

    private static final String TAG = ExceptionCrashHandler.class.getCanonicalName();
    //默认放在根目录路径
    private String CAHCE_CRASH_LOG = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "wechat";
    //允许最大日志文件的数量
    private int LIMIT_LOG_COUNT = 5;
    //简单日期格式
    private SimpleDateFormat formate = null;
    //保存异常日志信息集合
    private LinkedHashMap<String, String> crashAppLog = new LinkedHashMap<String, String>();

    //单例获取异常捕捉类实例
    private static ExceptionCrashHandler mExceptionCrashHandler;

    //系统默认的异常
    private Thread.UncaughtExceptionHandler mDefaultUncaughtExceptionHandler;

    private ExceptionCrashHandler() {
    }

    public static ExceptionCrashHandler getInstance() {

        if (mExceptionCrashHandler == null) {
            synchronized (ExceptionCrashHandler.class) {
                if (mExceptionCrashHandler == null) {
                    mExceptionCrashHandler = new ExceptionCrashHandler();
                }
            }
        }

        return mExceptionCrashHandler;
    }

    private Context mContext;

    public void init(Context context) {
        this.mContext = context;
        //设置全局的异常类为本类
        Thread.currentThread().setUncaughtExceptionHandler(this);

        mDefaultUncaughtExceptionHandler = Thread.currentThread().getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {

        Log.e("ExceptionCrashHandler", "异常了~" + e.getMessage());

        if (!hanlderException(e) && mDefaultUncaughtExceptionHandler != null) {
            //如果此异常不处理,让系统默认的异常去处理
            this.mDefaultUncaughtExceptionHandler.uncaughtException(t, e);
        } else {
            //可以延迟一秒钟在退出
            // Thread.sleep(1000);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    /**
     * 用户处理异常日志
     *
     * @param throwable
     * @return
     */
    private boolean hanlderException(Throwable throwable) {

        try {
            if (throwable == null) {
                return false;
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Looper.prepare();
                    Toast.makeText(mContext, "程序崩溃", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }).start();
            //收集应用信息
            collectCrashLogInfo(mContext);
            //将日志写入文件
            writerCrashLogToFile(throwable);
            //限制日子志文件的数量
            limitAppLogCount(LIMIT_LOG_COUNT);
        } catch (Exception e) {
            Log.e(TAG, "hanlderException - " + e.getMessage());
        }
        return false;
    }

    /**
     * 获取应用信息
     *
     * @param mContext
     */
    private void collectCrashLogInfo(Context mContext) {
        try {
            if (mContext == null) {
                return;
            }
            PackageManager packageManager = mContext.getPackageManager();
            if (packageManager != null) {
                PackageInfo packageInfo = packageManager.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
                if (packageInfo != null) {
                    String versionName = packageInfo.versionName;
                    String versionCode = "" + packageInfo.versionCode;
                    String packName = packageInfo.packageName;
                    crashAppLog.put("versionName", versionName);
                    crashAppLog.put("versionCode", versionCode);
                    crashAppLog.put("packName", packName);
                }
            }

            // 获取手机型号，系统版本，以及SDK版本
            crashAppLog.put("手机型号:", Build.MODEL);
            crashAppLog.put("系统版本", "" + Build.VERSION.SDK);
            crashAppLog.put("Android版本", Build.VERSION.RELEASE);
            Field[] fields = Build.class.getFields();
            if (fields != null && fields.length > 0) {
                for (Field field : fields) {
                    if (field != null) {
                        field.setAccessible(true);
                        crashAppLog.put(field.getName(), field.get(null).toString());
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "collectDeviceInfo - " + e.getMessage());
        }
    }


    /**
     * 写入文件中
     *
     * @param ex
     */
    private void writerCrashLogToFile(Throwable ex) {
        try {
            StringBuffer buffer = new StringBuffer();
            if (crashAppLog != null && crashAppLog.size() > 0) {
                for (Map.Entry<String, String> entry : crashAppLog.entrySet()) {
                    buffer.append(entry.getKey() + ":" + entry.getValue() + "\n");
                }
            }
            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            ex.printStackTrace(printWriter);
            Throwable cause = ex.getCause();

            while (cause != null) {
                cause.printStackTrace(printWriter);
                cause = cause.getCause();
            }
            printWriter.flush();
            printWriter.close();
            String result = writer.toString();
            buffer.append("Exception:+\n");
            buffer.append(result);
            writerToFile(buffer.toString());
            Log.e(TAG, "CrashLog " + buffer.toString());
        } catch (Exception e) {
            Log.e(TAG, "writerCrashLogToFile - " + e.getMessage());
        } finally {

        }
    }

    private void writerToFile(String s) {
        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;
        try {
            //创建日志文件名称
            String curtTimer = "" + System.currentTimeMillis();
            if (formate == null) {
                formate = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
            }
            String timer = formate.format(new Date());
            String fileName = "crash-" + timer + "-" + curtTimer + ".log";
            //创建文件夹
            File folder = new File(CAHCE_CRASH_LOG);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            //创建日志文件
            File file = new File(folder.getAbsolutePath() + File.separator + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            fileWriter = new FileWriter(file);
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(s);

        } catch (Exception e) {
            Log.e(TAG, "writerToFile - " + e.getMessage());
        } finally {
            try {
                bufferedWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 最大文件数量
     *
     * @param limitLogCount
     */
    private void limitAppLogCount(int limitLogCount) {
        try {
            File file = new File(CAHCE_CRASH_LOG);
            if (file != null && file.isDirectory()) {
                File[] files = file.listFiles(new CrashLogFliter());
                if (files != null && files.length > 0) {
                    //倒序排列
                    Arrays.sort(files, comparator);
                    if (files.length > LIMIT_LOG_COUNT) {
                        for (int i = 0; i < files.length - LIMIT_LOG_COUNT; i++) {
                            files[i].delete();
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "limitAppLogCount - " + e.getMessage());
        }
    }

    /**
     * 日志文件按日期大小排序（从大到小排序）
     */
    private Comparator<File> comparator = new Comparator<File>() {
        @Override
        public int compare(File l, File r) {

            if (l.lastModified() > r.lastModified())
                return 1;
            if (l.lastModified() < r.lastModified())
                return -1;

            return 0;
        }
    };

    /**
     * 过滤.log的文件
     */
    public class CrashLogFliter implements FileFilter {

        @Override
        public boolean accept(File file) {
            if (file.getName().endsWith(".log"))
                return true;
            return false;
        }
    }

    /**
     * 获取最新的崩溃日志file
     */

    public File getLatestFile() {
        try {
            File file = new File(CAHCE_CRASH_LOG);
            if (file != null && file.isDirectory()) {
                File[] files = file.listFiles(new CrashLogFliter());
                if (files != null && files.length > 0) {
                    //倒序排列
                    Arrays.sort(files, comparator);
                    return files[0];
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "limitAppLogCount - " + e.getMessage());
        }
        return null;
    }
}
