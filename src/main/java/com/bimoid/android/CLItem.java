package com.bimoid.android;

/**
 * Created by alex_xpert on 22.05.2014.
 */
public class CLItem {
    private String cName;
    private String cStatus;
    //private byte[] cAvatar;

    public CLItem(String cName, String cStatus, byte[] cAvatar) {
        this.cName = cName;
        this.cStatus = cStatus;
        //this.cAvatar = cAvatar;
    }

    public CLItem() {
    }

    public String getName() {
        return cName;
    }

    public void setName(String cName) {
        this.cName = cName;
    }

    public String getStatus() {
        return cStatus;
    }

    public void setStatus(String cStatus) {
        this.cStatus = cStatus;
    }

    //public byte[] getAvatar() {
        //return cAvatar;
    //}

    //public void setAvatar(byte[] cAvatar) {
        //this.cAvatar = cAvatar;
    //}
}
