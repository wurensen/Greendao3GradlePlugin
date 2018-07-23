package com.lancewu.greendao3gradleplugin.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Property;

/**
 * Created by wrs on 2018/7/23.
 */
@Entity
public class H {

    @Property
    private long _id;

    @Generated(hash = 907652822)
    public H(long _id) {
        this._id = _id;
    }

    @Generated(hash = 1114724152)
    public H() {
    }

    public long get_id() {
        return this._id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

}
