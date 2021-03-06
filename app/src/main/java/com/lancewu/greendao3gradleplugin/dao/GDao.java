package com.lancewu.greendao3gradleplugin.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.lancewu.greendao3gradleplugin.bean.G;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "G".
*/
public class GDao extends AbstractDao<G, Void> {

    public static final String TABLENAME = "G";

    /**
     * Properties of entity G.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property _id = new Property(0, long.class, "_id", false, "_ID");
    }


    public GDao(DaoConfig config) {
        super(config);
    }
    
    public GDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"G\" (" + //
                "\"_ID\" INTEGER NOT NULL );"); // 0: _id
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"G\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, G entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.get_id());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, G entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.get_id());
    }

    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    @Override
    public G readEntity(Cursor cursor, int offset) {
        G entity = new G( //
            cursor.getLong(offset + 0) // _id
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, G entity, int offset) {
        entity.set_id(cursor.getLong(offset + 0));
     }
    
    @Override
    protected final Void updateKeyAfterInsert(G entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    @Override
    public Void getKey(G entity) {
        return null;
    }

    @Override
    public boolean hasKey(G entity) {
        // TODO
        return false;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
