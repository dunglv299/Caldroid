package com.dunglv.calendar.dao;

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

}