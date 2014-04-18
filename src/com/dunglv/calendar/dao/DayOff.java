package com.dunglv.calendar.dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table DAY_OFF.
 */
public class DayOff {

    private Long id;
    private Long dayOfTime;
    private Integer offType;

    public DayOff() {
    }

    public DayOff(Long id) {
        this.id = id;
    }

    public DayOff(Long id, Long dayOfTime, Integer offType) {
        this.id = id;
        this.dayOfTime = dayOfTime;
        this.offType = offType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDayOfTime() {
        return dayOfTime;
    }

    public void setDayOfTime(Long dayOfTime) {
        this.dayOfTime = dayOfTime;
    }

    public Integer getOffType() {
        return offType;
    }

    public void setOffType(Integer offType) {
        this.offType = offType;
    }

}
