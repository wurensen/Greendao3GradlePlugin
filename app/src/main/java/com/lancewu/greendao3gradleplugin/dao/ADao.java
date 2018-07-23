package com.lancewu.greendao3gradleplugin.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.lancewu.greendao3gradleplugin.bean.A;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "A".
*/
public class ADao extends AbstractDao<A, Void> {

    public static final String TABLENAME = "A";

    /**
     * Properties of entity A.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property _id = new Property(0, long.class, "_id", false, "_ID");
    }


    public ADao(DaoConfig config) {
        super(config);
    }
    
    public ADao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"A\" (" + //
                "\"_ID\" INTEGER NOT NULL );"); // 0: _id
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"A\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, A entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.get_id());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, A entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.get_id());
    }

    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    @Override
    public A readEntity(Cursor cursor, int offset) {
        A entity = new A( //
            cursor.getLong(offset + 0) // _id
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, A entity, int offset) {
        entity.set_id(cursor.getLong(offset + 0));
     }
    
    @Override
    protected final Void updateKeyAfterInsert(A entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    @Override
    public Void getKey(A entity) {
        return null;
    }

    @Override
    public boolean hasKey(A entity) {
        // TODO
        return false;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
