package com.lancewu.greendao3gradleplugin.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by wrs on 2018/7/23.
 */
@Entity
public class E {

    @Property
    private long _id;

    @Generated(hash = 1230699842)
    public E(long _id) {
        this._id = _id;
    }

    @Generated(hash = 1140581159)
    public E() {
    }

    public long get_id() {
        return this._id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }
}
