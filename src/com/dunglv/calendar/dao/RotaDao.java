package com.dunglv.calendar.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.dunglv.calendar.dao.Rota;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table ROTA.
*/
public class RotaDao extends AbstractDao<Rota, Long> {

    public static final String TABLENAME = "ROTA";

    /**
     * Properties of entity Rota.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property DateStarted = new Property(2, Long.class, "dateStarted", false, "DATE_STARTED");
        public final static Property Color = new Property(3, String.class, "color", false, "COLOR");
        public final static Property WeekReapeat = new Property(4, Integer.class, "weekReapeat", false, "WEEK_REAPEAT");
        public final static Property TimeRepeat = new Property(5, String.class, "timeRepeat", false, "TIME_REPEAT");
        public final static Property ReminderTime = new Property(6, Integer.class, "reminderTime", false, "REMINDER_TIME");
        public final static Property IsGoogleSync = new Property(7, Boolean.class, "isGoogleSync", false, "IS_GOOGLE_SYNC");
        public final static Property CalendarUri = new Property(8, String.class, "calendarUri", false, "CALENDAR_URI");
    };

    private DaoSession daoSession;


    public RotaDao(DaoConfig config) {
        super(config);
    }
    
    public RotaDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'ROTA' (" + //
                "'_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "'NAME' TEXT," + // 1: name
                "'DATE_STARTED' INTEGER," + // 2: dateStarted
                "'COLOR' TEXT," + // 3: color
                "'WEEK_REAPEAT' INTEGER," + // 4: weekReapeat
                "'TIME_REPEAT' TEXT," + // 5: timeRepeat
                "'REMINDER_TIME' INTEGER," + // 6: reminderTime
                "'IS_GOOGLE_SYNC' INTEGER," + // 7: isGoogleSync
                "'CALENDAR_URI' TEXT);"); // 8: calendarUri
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'ROTA'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Rota entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        Long dateStarted = entity.getDateStarted();
        if (dateStarted != null) {
            stmt.bindLong(3, dateStarted);
        }
 
        String color = entity.getColor();
        if (color != null) {
            stmt.bindString(4, color);
        }
 
        Integer weekReapeat = entity.getWeekReapeat();
        if (weekReapeat != null) {
            stmt.bindLong(5, weekReapeat);
        }
 
        String timeRepeat = entity.getTimeRepeat();
        if (timeRepeat != null) {
            stmt.bindString(6, timeRepeat);
        }
 
        Integer reminderTime = entity.getReminderTime();
        if (reminderTime != null) {
            stmt.bindLong(7, reminderTime);
        }
 
        Boolean isGoogleSync = entity.getIsGoogleSync();
        if (isGoogleSync != null) {
            stmt.bindLong(8, isGoogleSync ? 1l: 0l);
        }
 
        String calendarUri = entity.getCalendarUri();
        if (calendarUri != null) {
            stmt.bindString(9, calendarUri);
        }
    }

    @Override
    protected void attachEntity(Rota entity) {
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
    public Rota readEntity(Cursor cursor, int offset) {
        Rota entity = new Rota( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // name
            cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2), // dateStarted
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // color
            cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4), // weekReapeat
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // timeRepeat
            cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6), // reminderTime
            cursor.isNull(offset + 7) ? null : cursor.getShort(offset + 7) != 0, // isGoogleSync
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8) // calendarUri
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Rota entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setDateStarted(cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2));
        entity.setColor(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setWeekReapeat(cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4));
        entity.setTimeRepeat(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setReminderTime(cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6));
        entity.setIsGoogleSync(cursor.isNull(offset + 7) ? null : cursor.getShort(offset + 7) != 0);
        entity.setCalendarUri(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Rota entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Rota entity) {
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
    
}
