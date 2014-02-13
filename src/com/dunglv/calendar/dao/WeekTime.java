package com.dunglv.calendar.dao;

import com.dunglv.calendar.dao.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table WEEK_TIME.
 */
public class WeekTime {

    private Long id;
    private Integer weekId;
    private long rotaId;
    private String monday;
    private String tuesday;
    private String wednesday;
    private String thursday;
    private String friday;
    private String saturday;
    private String sunday;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient WeekTimeDao myDao;

    private Rota rota;
    private Long rota__resolvedKey;


    public WeekTime() {
    }

    public WeekTime(Long id) {
        this.id = id;
    }

    public WeekTime(Long id, Integer weekId, long rotaId, String monday, String tuesday, String wednesday, String thursday, String friday, String saturday, String sunday) {
        this.id = id;
        this.weekId = weekId;
        this.rotaId = rotaId;
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getWeekTimeDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getWeekId() {
        return weekId;
    }

    public void setWeekId(Integer weekId) {
        this.weekId = weekId;
    }

    public long getRotaId() {
        return rotaId;
    }

    public void setRotaId(long rotaId) {
        this.rotaId = rotaId;
    }

    public String getMonday() {
        return monday;
    }

    public void setMonday(String monday) {
        this.monday = monday;
    }

    public String getTuesday() {
        return tuesday;
    }

    public void setTuesday(String tuesday) {
        this.tuesday = tuesday;
    }

    public String getWednesday() {
        return wednesday;
    }

    public void setWednesday(String wednesday) {
        this.wednesday = wednesday;
    }

    public String getThursday() {
        return thursday;
    }

    public void setThursday(String thursday) {
        this.thursday = thursday;
    }

    public String getFriday() {
        return friday;
    }

    public void setFriday(String friday) {
        this.friday = friday;
    }

    public String getSaturday() {
        return saturday;
    }

    public void setSaturday(String saturday) {
        this.saturday = saturday;
    }

    public String getSunday() {
        return sunday;
    }

    public void setSunday(String sunday) {
        this.sunday = sunday;
    }

    /** To-one relationship, resolved on first access. */
    public Rota getRota() {
        long __key = this.rotaId;
        if (rota__resolvedKey == null || !rota__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            RotaDao targetDao = daoSession.getRotaDao();
            Rota rotaNew = targetDao.load(__key);
            synchronized (this) {
                rota = rotaNew;
            	rota__resolvedKey = __key;
            }
        }
        return rota;
    }

    public void setRota(Rota rota) {
        if (rota == null) {
            throw new DaoException("To-one property 'rotaId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.rota = rota;
            rotaId = rota.getId();
            rota__resolvedKey = rotaId;
        }
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

}
