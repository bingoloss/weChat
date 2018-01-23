package com.lehand.wechat.chat;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.lehand.wechat.chat.gen.DaoMaster;
import com.lehand.wechat.chat.gen.DaoSession;
import com.lehand.wechat.chat.gen.UserDao;
import com.lehand.wechat.chat.model.User;

import java.util.List;

/**
 * Created by bingo on 2018/1/22.
 */

public class GreenDaoManager {
    private static GreenDaoManager manager;
    private Context mContext;
    private DaoMaster.DevOpenHelper mHelper;
    private SQLiteDatabase db;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    private GreenDaoManager() {
    }

    public static GreenDaoManager getInstance() {
        if (manager == null) {
            synchronized (GreenDaoManager.class) {
                if (manager == null) {
                    manager = new GreenDaoManager();
                }
            }
        }
        return manager;
    }

    /**
     * 初始化数据库链接
     *
     * @param context
     */
    public void init(Context context) {
        this.mContext = context;
        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
        mHelper = new DaoMaster.DevOpenHelper(context, "chat-db", null);
        db = mHelper.getWritableDatabase();
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
    }

    /**
     * 新增一个用户信息数据
     * 默认注册的环信id就是用户名
     *
     * @param hxid
     */
    public void addUser(String hxid) {
        if (mDaoSession != null) {
            UserDao userDao = mDaoSession.getUserDao();
            User user = new User(hxid, hxid, "", "", "", "", "", "", "", "", "", "", "", "", "", true, 0);
            userDao.insert(user);
        }
    }

    /**
     * 删除用户
     *
     * @param user
     */
    public boolean removeUser(User user) {
        if (user == null) {
            return false;
        }
        if (mDaoSession != null) {
            UserDao userDao = mDaoSession.getUserDao();
            userDao.delete(user);
        }
        return true;
    }

    /**
     * 刷新数据
     *
     * @param user
     */
    public boolean updataUser(User user) {
        if (user == null) {
            return false;
        }
        if (mDaoSession != null) {
            UserDao userDao = mDaoSession.getUserDao();
            userDao.update(user);
        }
        return true;
    }

    /**
     * 根据hxid查找用户
     * @param hxid
     */
    public User findUser(String hxid){
        if (mDaoSession != null) {
            UserDao userDao = mDaoSession.getUserDao();
            User user = userDao.load(hxid);
            return user;
        }
        return null;
    }
    /**
     * 查找所有用户
     */
    public List<User> findAllUser(){
        if (mDaoSession != null) {
            UserDao userDao = mDaoSession.getUserDao();
            List<User> users = userDao.loadAll();
            return users;
        }
        return null;
    }
}
