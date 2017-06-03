package com.it51buy.mojian.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil
{

	private final static String		DATE_FORMAT_DAY		= "yyyyMMdd";

	private final static String		DATE_FORMAT_DAY_1	= "yyyy-MM-dd";

	private final static String		DATE_FORMAT_VERBOSE	= "yyyy-MM-dd HH:mm:ss";
	
	private final static String		DATE_FORMAT_VERBOSE_WITHOUT_SECOND	= "yyyy-MM-dd HH:mm";
	
	private final static String		DATE_FORMAT_DAY_TIME   = "HH:mm";

	private final static String		DATE_FORMAT_DAY_USA   = "MM/dd/yyyy HH:mm";
	
	private final static String		DATE_FORMAT_DAY_UK_RUSSIA   = "dd/MM/yyyy HH:mm";

	private final static String DATE_FORMAT_DAY_GREAT_CHINA = "yyyy-MM-dd HH:mm";
	
	/**
	 * Date formats are not synchronized. It is recommended to create separate
	 * format instances for each thread. If multiple threads access a format
	 * concurrently, it must be synchronized externally.
	 */

	public final static DateFormat	dateFormat			= new SimpleDateFormat(DATE_FORMAT_VERBOSE);
	public final static DateFormat	dateFormatDay		= new SimpleDateFormat(DATE_FORMAT_DAY);
	public final static DateFormat	dateFormatDay1		= new SimpleDateFormat(DATE_FORMAT_DAY_1);
	
	public final static DateFormat	dateFormatWithoutSecond	= new SimpleDateFormat(DATE_FORMAT_VERBOSE_WITHOUT_SECOND);
	public final static DateFormat	dateFormatDayTIME	= new SimpleDateFormat(DATE_FORMAT_DAY_TIME);
	public final static DateFormat	dateFormatDayUSA	= new SimpleDateFormat(DATE_FORMAT_DAY_USA);
	public final static DateFormat	dateFormatDayUK_RUSSIA		= new SimpleDateFormat(DATE_FORMAT_DAY_UK_RUSSIA);
	public final static DateFormat dateFormatDayGC = new SimpleDateFormat(DATE_FORMAT_DAY_GREAT_CHINA);
	
	/**
	 * Convert Date to String
	 * 
	 * @param date
	 * @return String
	 */
	public synchronized static String formatDate(Date date)
	{
		return formatDate(date, dateFormat);
	}

	public synchronized static String formatDate(Date date, DateFormat format)
	{
		return format.format(date);
	}

	public static Date getDateByDays(int days)
	{
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, days);
		return c.getTime();
	}

	public static long getDaysBetween(Date startDate, Date endDate)
	{
		Calendar fromCalendar = Calendar.getInstance();
		fromCalendar.setTime(startDate);
		fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
		fromCalendar.set(Calendar.MINUTE, 0);
		fromCalendar.set(Calendar.SECOND, 0);
		fromCalendar.set(Calendar.MILLISECOND, 0);

		Calendar toCalendar = Calendar.getInstance();
		toCalendar.setTime(endDate);
		toCalendar.set(Calendar.HOUR_OF_DAY, 0);
		toCalendar.set(Calendar.MINUTE, 0);
		toCalendar.set(Calendar.SECOND, 0);
		toCalendar.set(Calendar.MILLISECOND, 0);

		return (toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24);
	}

	public static boolean isSameDay(Date date1, Date date2)
	{
		String DATE_FORMAT = "yyyy-MM-dd";
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(DATE_FORMAT);
		String date1Str = sdf.format(date1);
		String date2Str = sdf.format(date2);
		return date1Str.equals(date2Str);
	}

	/**
	 * Convert String to Date
	 * 
	 * @param str
	 * @return Date
	 */
	public synchronized static Date parseDate(String str)
	{
		return parseDate(str, dateFormat);
	}

	/**
	 * Convert String to Date
	 * 
	 * @param str
	 * @return Date
	 */
	public synchronized static Date parseDate(String str, DateFormat format)
	{
		try
		{
			return format.parse(str);
		}
		catch (ParseException e)
		{
			return null;
		}
	}
	
}
