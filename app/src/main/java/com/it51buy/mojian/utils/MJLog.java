package com.it51buy.mojian.utils;

import android.util.Log;

public class MJLog {
	/**
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 */
	public static void v(String tag, String msg) {
		if (Config.DEBUG)
			Log.v(tag, msg);
	}

	/**
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 * @param tr
	 *            An exception to log
	 */
	public static void v(String tag, String msg, Throwable tr) {
		if (Config.DEBUG)
			Log.v(tag, msg, tr);
	}

	/**
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 */
	public static void d(String tag, String msg) {
		if (Config.DEBUG)
			Log.d(tag, msg);
	}

	/**
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 * @param tr
	 *            An exception to log
	 */
	public static void d(String tag, String msg, Throwable tr) {
		if (Config.DEBUG)
			Log.d(tag, msg, tr);
	}

	/**
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 */
	public static void i(String tag, String msg) {
		if (Config.DEBUG)
			Log.i(tag, msg);
	}

	/**
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 * @param tr
	 *            An exception to log
	 */
	public static void i(String tag, String msg, Throwable tr) {
		if (Config.DEBUG)
			Log.i(tag, msg, tr);
	}

	/**
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 */
	public static void w(String tag, String msg) {
		if (Config.DEBUG)
			Log.w(tag, msg);
	}

	/**
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 * @param tr
	 *            An exception to log
	 */
	public static void w(String tag, String msg, Throwable tr) {
		if (Config.DEBUG)
			Log.w(tag, msg, tr);
	}

	/**
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param tr
	 *            An exception to log
	 */
	public static void w(String tag, Throwable tr) {
		if (Config.DEBUG)
			Log.w(tag, "", tr);
	}

	/**
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 */
	public static void e(String tag, String msg) {
		if (Config.DEBUG)
			Log.e(tag, msg);
	}

	/**
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 * @param tr
	 *            An exception to log
	 */
	public static void e(String tag, String msg, Throwable tr) {
		if (Config.DEBUG)
			Log.e(tag, msg, tr);
	}
}
