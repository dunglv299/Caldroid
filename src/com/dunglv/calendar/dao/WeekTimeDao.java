package com.dunglv.calendar.dao;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.SqlUtils;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

import com.dunglv.calendar.dao.WeekTime;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table WEEK_TIME.
*/
public class WeekTimeDao extends AbstractDao<WeekTime, Long> {

    public static final String TABLENAME = "WEEK_TIME";

    /**
     * Properties of entity WeekTime.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property WeekId = new Property(1, Integer.class, "weekId", false, "WEEK_ID");
        public final static Property RotaId = new Property(2, long.class, "rotaId", false, "ROTA_ID");
        public final static Property Monday = new Property(3, String.class, "monday", false, "MONDAY");
        public final static Property Tuesday = new Property(4, String.class, "tuesday", false, "TUESDAY");
        public final static Property Wednesday = new Property(5, String.class, "wednesday", false, "WEDNESDAY");
        public final static Property Thursday = new Property(6, String.class, "thursday", false, "THURSDAY");
        public final static Property Friday = new Property(7, String.class, "friday", false, "FRIDAY");
        public final static Property Saturday = new Property(8, String.class, "saturday", false, "SATURDAY");
        public final static Property Sunday = new Property(9, String.class, "sunday", false, "SUNDAY");
    };

    private DaoSession daoSession;

    private Query<WeekTime> rota_WeekTimeListQuery;

    public WeekTimeDao(DaoConfig config) {
        super(config);
    }
    
    public WeekTimeDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'WEEK_TIME' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'WEEK_ID' INTEGER," + // 1: weekId
                "'ROTA_ID' INTEGER NOT NULL ," + // 2: rotaId
                "'MONDAY' TEXT," + // 3: monday
                "'TUESDAY' TEXT," + // 4: tuesday
                "'WEDNESDAY' TEXT," + // 5: wednesday
                "'THURSDAY' TEXT," + // 6: thursday
                "'FRIDAY' TEXT," + // 7: friday
                "'SATURDAY' TEXT," + // 8: saturday
                "'SUNDAY' TEXT);"); // 9: sunday
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'WEEK_TIME'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, WeekTime entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Integer weekId = entity.getWeekId();
        if (weekId != null) {
            stmt.bindLong(2, weekId);
        }
        stmt.bindLong(3, entity.getRotaId());
 
        String monday = entity.getMonday();
        if (monday != null) {
            stmt.bindString(4, monday);
        }
 
        String tuesday = entity.getTuesday();
        if (tuesday != null) {
            stmt.bindString(5, tuesday);
        }
 
        String wednesday = entity.getWednesday();
        if (wednesday != null) {
            stmt.bindString(6, wednesday);
        }
 
        String thursday = entity.getThursday();
        if (thursday != null) {
            stmt.bindString(7, thursday);
        }
 
        String friday = entity.getFriday();
        if (friday != null) {
            stmt.bindString(8, friday);
        }
 
        String saturday = entity.getSaturday();
        if (saturday != null) {
            stmt.bindString(9, saturday);
        }
 
        String sunday = entity.getSunday();
        if (sunday != null) {
            stmt.bindString(10, sunday);
        }
    }

    @Override
    protected void attachEntity(WeekTime entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public WeekTime readEntity(Cursor cursor, int offset) {
        WeekTime entity = new WeekTime( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1), // weekId
            cursor.getLong(offset + 2), // rotaId
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // monday
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // tuesday
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // wednesday
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // thursday
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // friday
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // saturday
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9) // sunday
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, WeekTime entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setWeekId(cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1));
        entity.setRotaId(cursor.getLong(offset + 2));
        entity.setMonday(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setTuesday(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setWednesday(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setThursday(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setFriday(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setSaturday(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setSunday(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(WeekTime entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(WeekTime entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "weekTimeList" to-many relationship of Rota. */
    public List<WeekTime> _queryRota_WeekTimeList(long rotaId) {
        synchronized (this) {
            if (rota_WeekTimeListQuery == null) {
                QueryBuilder<WeekTime> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.RotaId.eq(null));
                rota_WeekTimeListQuery = queryBuilder.build();
            }
        }
        Query<WeekTime> query = rota_WeekTimeListQuery.forCurrentThread();
        query.setParameter(0, rotaId);
        return query.list();
    }

    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getRotaDao().getAllColumns());
            builder.append(" FROM WEEK_TIME T");
            builder.append(" LEFT JOIN ROTA T0 ON T.'ROTA_ID'=T0.'_id'");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected WeekTime loadCurrentDeep(Cursor cursor, boolean lock) {
        WeekTime entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        Rota rota = loadCurrentOther(daoSession.getRotaDao(), cursor, offset);
         if(rota != null) {
            entity.setRota(rota);
        }

        return entity;    
    }

    public WeekTime loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<WeekTime> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<WeekTime> list = new ArrayList<WeekTime>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<WeekTime> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<WeekTime> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
