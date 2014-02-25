package com.dunglv.calendar.entity;

import java.util.Date;

import com.dunglv.calendar.dao.DayTime;
import com.dunglv.calendar.dao.Rota;

public class RotaDay {
	private Date dateTime;
	private int day;
	private int month;
	private int year;
	private String color;
	private Rota rota;
	private int timeRepeat;
	private DayTime dayTime;

	/**
	 * @return the day
	 */
	public int getDay() {
		return day;
	}

	/**
	 * @param day
	 *            the day to set
	 */
	public void setDay(int day) {
		this.day = day;
	}

	/**
	 * @return the month
	 */
	public int getMonth() {
		return month;
	}

	/**
	 * @param month
	 *            the month to set
	 */
	public void setMonth(int month) {
		this.month = month;
	}

	/**
	 * @return the year
	 */
	public int getYear() {
		return year;
	}

	/**
	 * @param year
	 *            the year to set
	 */
	public void setYear(int year) {
		this.year = year;
	}

	/**
	 * @return the color
	 */
	public String getColor() {
		return color;
	}

	/**
	 * @param color
	 *            the color to set
	 */
	public void setColor(String color) {
		this.color = color;
	}

	/**
	 * @return the dateTime
	 */
	public Date getDateTime() {
		return dateTime;
	}

	/**
	 * @param dateTime
	 *            the dateTime to set
	 */
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	/**
	 * @return the rota
	 */
	public Rota getRota() {
		return rota;
	}

	/**
	 * @param rota
	 *            the rota to set
	 */
	public void setRota(Rota rota) {
		this.rota = rota;
	}

	/**
	 * @return the timeRepeat
	 */
	public int getTimeRepeat() {
		return timeRepeat;
	}

	/**
	 * @param timeRepeat
	 *            the timeRepeat to set
	 */
	public void setTimeRepeat(int timeRepeat) {
		this.timeRepeat = timeRepeat;
	}

	/**
	 * @return the dayTime
	 */
	public DayTime getDayTime() {
		return dayTime;
	}

	/**
	 * @param dayTime
	 *            the dayTime to set
	 */
	public void setDayTime(DayTime dayTime) {
		this.dayTime = dayTime;
	}
}
