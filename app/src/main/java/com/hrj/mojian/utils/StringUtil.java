package com.hrj.mojian.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.Assert;

public class StringUtil {
	public static boolean isEmpty(String s) {
		return s == null || s.trim().length() == 0;
	}

	private static final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();
	private static final String ROOT_PATH = "/";

	/**
	 * 判断String数组包含某一string
	 *
	 * @param strs
	 * @param str
	 * @return
	 */
	public static boolean isHave(String[] strs, String str) {
		if (strs == null || str == null) {
			return false;
		}
		for (int i = 0; i < strs.length; i++) {
			if (str.equals(strs[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断str以string数组某一项开头
	 *
	 * @param strs
	 * @param str
	 * @return
	 */
	public static boolean isStartWith(String[] strs, String str) {
		if (str == null || strs == null) {
			return false;
		}
		for (int i = 0; i < strs.length; i++) {
			if (str.startsWith(strs[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 解析一个带 token 分隔符的字符串，这个方法的效率比直接调用String的split()方法快大约1倍。
	 *
	 * @param tokenedStr
	 * @param token
	 * @return String[]
	 */
	public static String[] splitString(String tokenedStr, String token) {
		String[] ids = null;
		if (tokenedStr != null) {
			StringTokenizer st = new StringTokenizer(tokenedStr, token);
			final int arraySize = st.countTokens();
			if (arraySize > 0) {
				ids = new String[arraySize];
				int counter = 0;
				while (st.hasMoreTokens()) {
					ids[counter++] = st.nextToken();
				}
			}
		}
		return ids;
	}

	/**
	 * 把字符串数组组合成一个以指定分隔符分隔的字符串，并追加到给定的<code>StringBuilder</code>
	 *
	 * @param strs
	 *            字符串数组
	 * @param seperator
	 *            分隔符
	 * @return
	 */
	public static void mergeString(String[] strs, String seperator, StringBuilder sb) {
		if (strs == null || strs.length == 0)
			return;
		for (int i = 0; i < strs.length; i++) {
			if (i != 0) {
				sb.append(seperator);
			}
			sb.append(strs[i]);
		}
	}

	public static String stringWithFormat(String str, Object... args) {
		str = String.format(str, args);
		return str;
	}

	/**
	 * 把字符串数组组合成一个以指定分隔符分隔的字符串。
	 *
	 * @param strs
	 *            字符串数组
	 * @param seperator
	 *            分隔符
	 * @return
	 */
	public static String mergeString(String[] strs, String seperator) {
		StringBuilder sb = new StringBuilder();
		mergeString(strs, seperator, sb);
		return sb.toString();
	}

	/**
	 * MessageEncrypt 文件后缀名小写
	 *
	 * @param strFileName
	 * @return
	 */
	public static String lowerCaseExtension(String strFileName) {
		// 去掉文件后缀
		String strFileNameBody = stringByDeletingPathExtension(strFileName);
		// 得到文件后缀
		String strExt = pathExtension(strFileName).toLowerCase();
		strFileName = strFileNameBody + "." + strExt;
		return strFileName;
	}

	/**
	 * 得到文件的类型。 实际上就是得到文件名中最后一个“.”后面的部分。
	 *
	 * @param fileName
	 *            文件名
	 * @return 文件名中的类型部分
	 */
	public static String pathExtension(String fileName) {
		int point = fileName.lastIndexOf('.');
		int length = fileName.length();
		if (point == -1 || point == length - 1) {
			return "";
		} else {
			return fileName.substring(point + 1, length);
		}
	}

	/**
	 * 将文件名中的类型部分去掉。不含点
	 *
	 * @param filename
	 *            文件名
	 * @return 去掉类型部分的结果
	 */
	public static String stringByDeletingPathExtension(String filename) {
		int index = filename.lastIndexOf(".");
		if (index != -1) {
			return filename.substring(0, index);
		} else {
			return filename;
		}
	}

	/**
	 * strFolder/filename
	 *
	 * @param strFolder
	 * @param filename
	 * @return
	 */
	public static String stringByAppendingPathComponent(String strFolder, String filename) {
		return strFolder + File.separator + filename;
	}

	/**
	 * trimExt.ext
	 *
	 * @param trimExt
	 * @param ext
	 * @return
	 */
	public static String stringByAppendingPathExtension(String trimExt, String ext) {
		return trimExt + "." + ext;
	}

	/**
	 * 得到文件的名字部分。 实际上就是路径中的最后一个路径分隔符后的部分。
	 *
	 * @param fileName
	 *            文件名
	 * @return 文件名中的名字部分
	 * @since 0.5
	 */
	public static String getNamePart(String fileName) {
		int point = getPathLastIndex(fileName);
		int length = fileName.length();
		if (point == -1) {
			return fileName;
		} else if (point == length - 1) {
			int secondPoint = getPathLastIndex(fileName, point - 1);
			if (secondPoint == -1) {
				if (length == 1) {
					return fileName;
				} else {
					return fileName.substring(0, point);
				}
			} else {
				return fileName.substring(secondPoint + 1, point);
			}
		} else {
			return fileName.substring(point + 1);
		}
	}

	/**
	 * 得到除去文件名部分的路径 实际上就是路径中的最后一个路径分隔符前的部分。
	 *
	 * @param fileName
	 * @return
	 */
	public static String getNameDelLastPath(String fileName) {
		if (fileName == null)
			return fileName;
		int point = getPathLastIndex(fileName);
		if (point == -1) {
			return fileName;
		} else {
			return fileName.substring(0, point);
		}
	}

	/**
	 * 得到路径分隔符在文件路径中最后出现的位置。 对于DOS或者UNIX风格的分隔符都可以。
	 *
	 * @param fileName
	 *            文件路径
	 * @return 路径分隔符在路径中最后出现的位置，没有出现时返回-1。
	 * @since 0.5
	 */
	public static int getPathLastIndex(String fileName) {
		int point = fileName.lastIndexOf('/');
		if (point == -1) {
			point = fileName.lastIndexOf('\\');
		}
		return point;
	}

	/**
	 * 得到路径分隔符在文件路径中指定位置前最后出现的位置。 对于DOS或者UNIX风格的分隔符都可以。
	 *
	 * @param fileName
	 *            文件路径
	 * @param fromIndex
	 *            开始查找的位置
	 * @return 路径分隔符在路径中指定位置前最后出现的位置，没有出现时返回-1。
	 * @since 0.5
	 */
	public static int getPathLastIndex(String fileName, int fromIndex) {
		int point = fileName.lastIndexOf('/', fromIndex);
		if (point == -1) {
			point = fileName.lastIndexOf('\\', fromIndex);
		}
		return point;
	}

	public static final String toString(byte[] ba) {
		return toString(ba, 0, ba.length);
	}

	public static final String toString(byte[] ba, int offset, int length) {
		char[] buf = new char[length * 2];
		for (int i = 0, j = 0, k; i < length;) {
			k = ba[offset + i++];
			buf[j++] = HEX_DIGITS[(k >>> 4) & 0x0F];
			buf[j++] = HEX_DIGITS[k & 0x0F];
		}
		return new String(buf);
	}

	/**
	 * 过滤特殊字符
	 *
	 * @param str
	 * @return
	 */
	public static String removeInvalidChar(String str) {
		// 清除掉所有特殊字符
		String regEx = "[`~!@#$%^&*()+=|{}':;＇,\\[\\].<>/?～！＠＃￥％……＆＊（）——＋｜｛｝【】‘；：”“’。，、？]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}

	public static class StringComparator implements Comparator<String> {
		@Override
		public int compare(String object1, String object2) {
			return object1.compareTo(object2);
		}
	}

	/**
	 * 生成size位随机数
	 *
	 * @param size
	 * @return
	 */
	public static String createRandomKey(int size) {
		char[] c = { '1', '2', '3', '4', '5', '6', '7', '8', '9', '0' };
		Random random = new Random(); // 初始化随机数产生器
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < size; i++) {
			sb.append(c[Math.abs(random.nextInt()) % c.length]);
		}
		return sb.toString();
	}

	/**
	 * 根据文档大小生成文档大小的字符
	 *
	 * @param size
	 *            文档大小
	 * @return 文档大小的字符
	 */
	public static String stringFromSize(double size) {
		if (size < 0)
			return "-";

		double B = size;
		if (B < 1.0 && B > 0.0)
			B = 1.0;
		if (B < 1024.0)
			return StringUtil.stringWithFormat("%.0f B", B);

		double K = size / 1024.0;
		if (K < 1024.0)
			return StringUtil.stringWithFormat("%.1f KB", K);

		double M = K / 1024.0;
		if (M < 1024.0)
			return StringUtil.stringWithFormat("%.1f MB", M);

		double G = M / 1024.0;
		return StringUtil.stringWithFormat("%.1f GB", G);
	}

	/**
	 * 得到文件的父文件夹。 例如/mnt/sdcard/a.doc,得到sdcard。特殊情况：根目录/下的文件得到的是/
	 *
	 * @param 文件绝对路径
	 * @return 文件的父文件夹名字
	 */
	public static String getFileNameSubStr(String path) {
		path = path.substring(0, path.lastIndexOf('/') + 1);
		if (ROOT_PATH.equals(path)) {
			return path;
		}
		if (path.charAt(path.length() - 1) == '/')
			path = path.substring(0, path.length() - 1);
		return path.substring(path.lastIndexOf('/') + 1);
		// File f = new File(path);
		// String parentPath = f.getParent();
		// return getNamePart(parentPath);
	}

	/**
	 * 获取字符串tag第N次在字符串text出现的下标
	 *
	 * @param text
	 *            字符串
	 * @param tag
	 *            待查询字符串
	 * @param flag
	 *            字符串第N次出现
	 * @author longzihang
	 */
	public static int search(String text, String tag, int flag) {
		int tagLength = tag.length();// 搜索的字符或者字符串长度
		String[] temp = text.split(tag);// 拆分text
		int l = temp.length;// text拆分后得到的数组长度
		// 如果text不是以tag结束，将l的值-1
		if (!text.endsWith(tag)) {
			l = l - 1;
		}
		// 其实位置
		int position = 0;
		int[] positionarr = new int[l];
		for (int i = 0; i < l; i++) {
			String s = temp[i];
			position += s.length();
			positionarr[i] = position;
			position += tagLength;
		}
		return positionarr[flag - 1];
	}

	/**
	 * Normalize a path, e.g. resolve ".", "..", "//"
	 */
	public static String normalizePath(String path) {
		if (!isEmpty(path)) {
			Assert.assertFalse(path.contains("//"));
			if (path.contains("//")) // 暂时仅处理多出 '/'的问题
			{
				path = path.replaceAll("/+/", "/");
			}// else do nothing.
		}

		return path;
	}

	/**
	 * All strings will be encoded as UTF-8.
	 */
	public static final String DEFAULT_CHARSET = "UTF-8";

	/**
	 * One-way hashing function used for providing a checksum of data
	 */
	private static final String HASH_ALGORITHM = "MD5";

	/**
	 * A runtime exception that will be thrown when we hit an error that should
	 * "never" occur ... e.g. if the JVM doesn't know about UTF-8 or MD5.
	 */
	private static final class StringUtilException extends RuntimeException {
		private static final long serialVersionUID = -8099786694856724498L;

		public StringUtilException(String message, Throwable cause) {
			super(message, cause);
		}
	}

	/**
	 * Takes the provided byte array and converts it into a hexidecimal string
	 * with two characters per byte.
	 */
	public static String bytesToHex(byte[] bytes) {
		return bytesToHex(bytes, false);
	}

	/**
	 * Takes the provided byte array and converts it into a hexidecimal string
	 * with two characters per byte.
	 *
	 * @param withSpaces
	 *            if true, this will put a space character between each
	 *            hex-rendered byte, for readability.
	 */
	public static String bytesToHex(byte[] bytes, boolean withSpaces) {
		StringBuilder sb = new StringBuilder();
		for (byte hashByte : bytes) {
			int intVal = 0xff & hashByte;
			if (intVal < 0x10) {
				sb.append('0');
			}
			sb.append(Integer.toHexString(intVal));
			if (withSpaces) {
				sb.append(' ');
			}
		}
		return sb.toString();
	}

	/**
	 * Takes a string in hexidecimal format and converts it to a binary byte
	 * array. This does no checking of the format of the input, so this should
	 * only be used after confirming the format or origin of the string. The
	 * input string should only contain the hex data, two characters per byte.
	 */
	public static byte[] hexToBytes(String hexString) {
		byte[] result = new byte[hexString.length() / 2];
		for (int i = 0; i < result.length; ++i) {
			int offset = i * 2;
			result[i] = (byte) Integer.parseInt(hexString.substring(offset, offset + 2), 16);
		}
		return result;
	}

	/**
	 * Encodes a string as a byte array using the default encoding.
	 */
	public static byte[] stringToBytes(String string) {
		try {
			return stringToBytes(string, DEFAULT_CHARSET);
		} catch (UnsupportedEncodingException e) {
			throw new StringUtilException(DEFAULT_CHARSET + " not supported", e);
		}
	}

	/**
	 * Encodes a string as a byte array with a specified character set encoding.
	 */
	public static byte[] stringToBytes(String string, String charSet) throws UnsupportedEncodingException {
		if (string == null) {
			return null;
		}
		return string.getBytes(charSet);
	}

	/**
	 * Decodes a byte array as a string using the default encoding (UTF-8)
	 */
	public static String bytesToString(byte[] bytes) {
		try {
			return bytesToString(bytes, DEFAULT_CHARSET);
		} catch (UnsupportedEncodingException e) {
			throw new StringUtilException(DEFAULT_CHARSET + " not supported", e);
		}
	}

	/**
	 * Decodes a byte array as a string using the specified character set
	 * encoding.
	 */
	public static String bytesToString(byte[] bytes, String charSet) throws UnsupportedEncodingException {
		if (bytes == null) {
			return null;
		}
		return new String(bytes, charSet);
	}

	/**
	 * Returns an MD5 checksum of the provided array of bytes.
	 */
	public static byte[] hash(byte[] body) {
		try {
			return MessageDigest.getInstance(HASH_ALGORITHM).digest(body);
		} catch (NoSuchAlgorithmException e) {
			throw new StringUtilException(HASH_ALGORITHM + " not supported", e);
		}
	}

	/**
	 * Returns an MD5 checksum of the data read from the specified InputStream.
	 */
	public static byte[] hash(InputStream data) throws IOException {
		try {
			MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
			byte[] buf = new byte[4096];
			int len;
			while ((len = data.read(buf)) != -1) {
				digest.update(buf, 0, len);
			}
			return digest.digest();
		} catch (NoSuchAlgorithmException e) {
			throw new StringUtilException(HASH_ALGORITHM + " not supported", e);
		}
	}

	/**
	 * Returns an MD5 checksum of the provided string, which is encoded into
	 * UTF-8 format first for unambiguous hashing.
	 */
	public static byte[] hash(String content) {
		return hash(stringToBytes(content));
	}

	/**
	 * Return the suffix of the passed file name.
	 *
	 * @param fileName
	 *            File name to retrieve suffix for.
	 *
	 * @return Suffix for <TT>fileName</TT> or an empty string if unable to get
	 *         the suffix.
	 *
	 */
	public static String getFileNameSuffix(String fileName) {
		if (fileName == null)
			return "";

		final int pos = fileName.lastIndexOf('.');
		if (pos > 0 && pos < fileName.length() - 1) {
			return fileName.substring(pos + 1);
		}
		return "";
	}
}
