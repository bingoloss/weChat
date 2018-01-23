package com.lehand.wechat.chat.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by bingo on 2018/1/22.
 */
@Entity
public class User {
    @Id
    private String hxid;//主键(环信id作为唯一主键)

    private String uesrName;
    private String password;
    private String orgNo;
    private String orgName;
    private String cardNo;
    private String qqNo;
    private String weixinNo;
    private String nick;
    private String avatar;
    private String phone;
    private String sex;
    private String email;
    private String postName;//职位
    private String ext;//扩展属性
    private boolean online;//是否在线
    private int mFlag;//管理员标志(0 普通用户，1 管理员，2 群主)
    public int getMFlag() {
        return this.mFlag;
    }
    public void setMFlag(int mFlag) {
        this.mFlag = mFlag;
    }
    public boolean getOnline() {
        return this.online;
    }
    public void setOnline(boolean online) {
        this.online = online;
    }
    public String getExt() {
        return this.ext;
    }
    public void setExt(String ext) {
        this.ext = ext;
    }
    public String getPostName() {
        return this.postName;
    }
    public void setPostName(String postName) {
        this.postName = postName;
    }
    public String getEmail() {
        return this.email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getSex() {
        return this.sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    public String getPhone() {
        return this.phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getAvatar() {
        return this.avatar;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public String getNick() {
        return this.nick;
    }
    public void setNick(String nick) {
        this.nick = nick;
    }
    public String getWeixinNo() {
        return this.weixinNo;
    }
    public void setWeixinNo(String weixinNo) {
        this.weixinNo = weixinNo;
    }
    public String getQqNo() {
        return this.qqNo;
    }
    public void setQqNo(String qqNo) {
        this.qqNo = qqNo;
    }
    public String getCardNo() {
        return this.cardNo;
    }
    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }
    public String getOrgName() {
        return this.orgName;
    }
    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
    public String getOrgNo() {
        return this.orgNo;
    }
    public void setOrgNo(String orgNo) {
        this.orgNo = orgNo;
    }
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getUesrName() {
        return this.uesrName;
    }
    public void setUesrName(String uesrName) {
        this.uesrName = uesrName;
    }
    public String getHxid() {
        return this.hxid;
    }
    public void setHxid(String hxid) {
        this.hxid = hxid;
    }
    @Generated(hash = 1323095706)
    public User(String hxid, String uesrName, String password, String orgNo,
            String orgName, String cardNo, String qqNo, String weixinNo,
            String nick, String avatar, String phone, String sex, String email,
            String postName, String ext, boolean online, int mFlag) {
        this.hxid = hxid;
        this.uesrName = uesrName;
        this.password = password;
        this.orgNo = orgNo;
        this.orgName = orgName;
        this.cardNo = cardNo;
        this.qqNo = qqNo;
        this.weixinNo = weixinNo;
        this.nick = nick;
        this.avatar = avatar;
        this.phone = phone;
        this.sex = sex;
        this.email = email;
        this.postName = postName;
        this.ext = ext;
        this.online = online;
        this.mFlag = mFlag;
    }
    @Generated(hash = 586692638)
    public User() {
    }
}
