package com.it51buy.mojian;

import android.util.Log;

import com.it51buy.mojian.exception.NoSdCardException;
import com.it51buy.mojian.utils.DateUtil;
import com.it51buy.mojian.utils.FileUtil;
import com.it51buy.mojian.utils.StringUtil;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class FileLog {
	private final static StringBuilder builder_ = new StringBuilder();
	private final static String SPACE = " ";
	private final static String LINE_BREAK = "\n";
	private final static String LOG_SUFFIX = "txt";

	public static final String VERBOSE = "VERBOSE";
	public static final String DEBUG = "DEBUG";
	public static final String INFO = "INFO";
	public static final String WARN = "WARN";
	public static final String ERROR = "ERROR";
	public static final String ASSERT = "ASSERT";

	private static final int LOG_DAY_LENGTH = 7;

	public static String VERSION_INFO = "";

	public static void deleteExpiredLog() {
		try {
			deleteLog(FileUtil.getLogFile());
		} catch (NoSdCardException e) {
			e.printStackTrace();
		}
	}

	private static void deleteLog(String logFilePath) {
		Date lastDate = DateUtil.getDateByDays(-LOG_DAY_LENGTH);
		if (null != logFilePath && logFilePath.length() > 0) {
			File dir = new File(logFilePath);
			if (dir.exists() && dir.isDirectory()) {
				File[] files = dir.listFiles();
				if (files != null) {
					for (File file : files) {
						if (LOG_SUFFIX.equalsIgnoreCase(StringUtil.pathExtension(file.getName()))) {
							Date date = DateUtil.parseDate(StringUtil.stringByDeletingPathExtension(file.getName()), DateUtil.dateFormatDay);
							if (null != date) {
								if (date.before(lastDate)) {
									file.delete();
								}
							}
						}
					}
				}
			}
		}
	}

	private static String getNowData() {
		return DateUtil.formatDate(new Date());
	}

	private static String createLogContent(String priority, String tag, String msg) {
		return createLogContent(priority, tag, msg, null);
	}

	/**
	 * Log Format: 2 2011-12-27 12:06:22 tag msg
	 * 
	 * @param priority
	 * @param tag
	 * @param msg
	 * @return
	 */
	private static String createLogContent(String priority, String tag, String msg, String stackTraceString) {
		builder_.setLength(0);
		builder_.append(priority);
		builder_.append(SPACE);
		builder_.append(getNowData());
		builder_.append(SPACE);
		builder_.append(tag);
		builder_.append(SPACE);
		builder_.append(msg);
		builder_.append(LINE_BREAK);
		if (null != stackTraceString && stackTraceString.length() > 0) {
			builder_.append(stackTraceString);
			builder_.append(LINE_BREAK);
		}
		return builder_.toString();
	}

	private static void writeToFile(String content) {
		try {
			doWriteToFile(FileUtil.getLogFile(), content);
		} catch (NoSdCardException e) {
			e.printStackTrace();
		}
	}

	private static void doWriteToFile(String file, String content) {
		if (null != file && file.length() > 0) {
			final File f = new File(file);
			if (!f.exists()) {
				try {
					FileUtil.newFile(file);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			FileUtil.writeToFile(file, VERSION_INFO + "\n" + content, true);
		}
	}

	/**
	 * Send a {@link #VERBOSE} log message.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 */
	public static void v(String tag, String msg) {
		writeToFile(createLogContent(VERBOSE, tag, msg));
	}

	/**
	 * Send a {@link #VERBOSE} log message and log the exception.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 * @param tr
	 *            An exception to log
	 */
	public static void v(String tag, String msg, Throwable tr) {
		writeToFile(createLogContent(VERBOSE, tag, msg, Log.getStackTraceString(tr)));
	}

	/**
	 * Send a {@link #DEBUG} log message.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 */
	public static void d(String tag, String msg) {
		writeToFile(createLogContent(DEBUG, tag, msg));
	}

	/**
	 * Send a {@link #DEBUG} log message and log the exception.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 * @param tr
	 *            An exception to log
	 */
	public static void d(String tag, String msg, Throwable tr) {
		writeToFile(createLogContent(DEBUG, tag, msg, Log.getStackTraceString(tr)));
	}

	/**
	 * Send an {@link #INFO} log message.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 */
	public static void i(String tag, String msg) {
		writeToFile(createLogContent(INFO, tag, msg));
	}

	/**
	 * Send a {@link #INFO} log message and log the exception.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 * @param tr
	 *            An exception to log
	 */
	public static void i(String tag, String msg, Throwable tr) {
		writeToFile(createLogContent(DEBUG, tag, msg, Log.getStackTraceString(tr)));
	}

	/**
	 * Send a {@link #WARN} log message.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 */
	public static void w(String tag, String msg) {
		writeToFile(createLogContent(WARN, tag, msg));
	}

	/**
	 * Send a {@link #WARN} log message and log the exception.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 * @param tr
	 *            An exception to log
	 */
	public static void w(String tag, String msg, Throwable tr) {
		writeToFile(createLogContent(WARN, tag, msg, Log.getStackTraceString(tr)));
	}

	/**
	 * Send a {@link #WARN} log message and log the exception.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param tr
	 *            An exception to log
	 */
	public static void w(String tag, Throwable tr) {
		writeToFile(createLogContent(WARN, tag, "", Log.getStackTraceString(tr)));
	}

	/**
	 * Send an {@link #ERROR} log message.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 */
	public static void e(String tag, String msg) {
		writeToFile(createLogContent(ERROR, tag, msg));
	}

	/**
	 * Send a {@link #ERROR} log message and log the exception.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 * @param tr
	 *            An exception to log
	 */
	public static void e(String tag, String msg, Throwable tr) {
		writeToFile(createLogContent(ERROR, tag, msg, Log.getStackTraceString(tr)));
	}

}
