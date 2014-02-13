package com.dunglv.calendar.dao;

import java.util.List;
import com.dunglv.calendar.dao.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table ROTA.
 */
public class Rota {

    private Long id;
    private String name;
    private Long dateStarted;
    private String color;
    private Integer weekReapeat;
    private Boolean timeRepeat;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient RotaDao myDao;

    private List<WeekTime> weekTimeList;

    public Rota() {
    }

    public Rota(Long id) {
        this.id = id;
    }

    public Rota(Long id, String name, Long dateStarted, String color, Integer weekReapeat, Boolean timeRepeat) {
        this.id = id;
        this.name = name;
        this.dateStarted = dateStarted;
        this.color = color;
        this.weekReapeat = weekReapeat;
        this.timeRepeat = timeRepeat;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getRotaDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getDateStarted() {
        return dateStarted;
    }

    public void setDateStarted(Long dateStarted) {
        this.dateStarted = dateStarted;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getWeekReapeat() {
        return weekReapeat;
    }

    public void setWeekReapeat(Integer weekReapeat) {
        this.weekReapeat = weekReapeat;
    }

    public Boolean getTimeRepeat() {
        return timeRepeat;
    }

    public void setTimeRepeat(Boolean timeRepeat) {
        this.timeRepeat = timeRepeat;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<WeekTime> getWeekTimeList() {
        if (weekTimeList == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            WeekTimeDao targetDao = daoSession.getWeekTimeDao();
            List<WeekTime> weekTimeListNew = targetDao._queryRota_WeekTimeList(id);
            synchronized (this) {
                if(weekTimeList == null) {
                    weekTimeList = weekTimeListNew;
                }
            }
        }
        return weekTimeList;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetWeekTimeList() {
        weekTimeList = null;
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
