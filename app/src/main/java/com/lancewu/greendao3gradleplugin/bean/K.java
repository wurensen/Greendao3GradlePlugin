package com.lancewu.greendao3gradleplugin.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by wrs on 2018/7/23.
 */
@Entity
public class K {

    @Property
    private long _id;

    @Generated(hash = 1988579801)
    public K(long _id) {
        this._id = _id;
    }

    @Generated(hash = 1629073097)
    public K() {
    }

    public long get_id() {
        return this._id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

}
