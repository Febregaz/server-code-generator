/**
 * Program  : FileUtil.java
 * Author   : songkun
 * Create   : 2017年4月21日 下午4:07:04
 *
 * Copyright 2017 songkun. All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of songkun.  
 * You shall not disclose such Confidential Information and shall 
 * use it only in accordance with the terms of the license agreement 
 * you entered into with songkun.
 *
 */

package com.kun.springmvc.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 文件相关工具类
 *
 * @author songkun
 * @version 1.0.0
 * @date 2017年4月21日 下午4:07:04
 */
public class FileUtil {

	private FileUtil() {
	}

	/**
	 * 复制文件夹(包含文件)，同时替换${rootPackage}
	 * 
	 * @author songkun
	 * @create 2017年4月25日 下午1:58:45
	 * @param source
	 * @param dest
	 * @param rootPackage
	 * @param containSubFolder
	 *            是否处理子文件夹
	 * @return void
	 */
	public static final void doCopyFileOrFolder(File source, File dest, Map<String, String> placeHolders, boolean containSubFolder) {
		if (!source.exists()) {
			return;
		}
		if (source.isFile()) {// 文件，直接复制并处理占位符
			doCopyFile(source, dest, placeHolders);
		} else {// 文件夹，同时需要处理子文件夹，则，遍历所有子目录
			File[] children = source.listFiles();
			if (!dest.exists()) {
				dest.mkdirs();
			}
			if (children != null && children.length > 0) {
				for (File file : children) {
					if (!containSubFolder && file.isDirectory()) {
						continue;
					}
					doCopyFileOrFolder(file, new File(dest, file.getName()), placeHolders, containSubFolder);
				}
			}
		}
	}

	/**
	 * 复制文件，同时，替换${rootPackage}为目标包名，写入目标文件中
	 * 
	 * @author songkun
	 * @create 2017年4月25日 下午1:58:03
	 * @param source
	 * @param dest
	 * @param placeHolders
	 *            key=占位符,value=对应的值
	 * @return void
	 */
	public static final void doCopyFile(File source, File dest, Map<String, String> placeHolders) {
		if (!source.exists() || source.isDirectory()) {
			return;
		}
		if (placeHolders != null && !isBinary(source)) {
			String content = FileUtil.readFileToString(source);
			for (Entry<String, String> entry : placeHolders.entrySet()) {
				content = content.replace(entry.getKey(), entry.getValue());
			}
			FileUtil.writeStringToFile(dest, content);
		} else {// 直接拷贝文件，直接二进制拷贝，有的是图片什么的
			copy(source, dest);
		}
	}

	/**
	 * 是否是二进制文件
	 * 
	 * @author http://www.jb51.net/article/69945.htm
	 * @create 2017年5月15日 下午7:05:26
	 * @param file
	 * @return boolean
	 */
	public static boolean isBinary(File file) {
		boolean isBinary = false;
		InputStream stream = null;
		try {
			stream = new FileInputStream(file);
			long len = file.length();
			for (int j = 0; j < (int) len; j++) {
				int t = stream.read();
				if (t < 32 && t != 9 && t != 10 && t != 13) {
					isBinary = true;
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (Exception e2) {
				}
			}
		}
		return isBinary;
	}

	/**
	 * 文件拷贝，二进制拷贝
	 * 
	 * @author songkun
	 * @create 2017年5月15日 下午6:59:21
	 * @param source
	 * @param dest
	 * @return void
	 */
	private static final void copy(File source, File dest) {
		if (!source.exists() || dest.exists()) {
			return;
		}
		if (!dest.getParentFile().exists() && !dest.getParentFile().mkdirs()) {
			return;
		}
		try {
			InputStream inputStream = new FileInputStream(source);
			OutputStream outputStream = new FileOutputStream(dest);
			copy(inputStream, outputStream);
			close(inputStream);
			close(outputStream);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 管理读入流
	 * 
	 * @author songkun
	 * @create 2017年5月15日 下午6:58:32
	 * @param inputStream
	 * @return void
	 */
	private static final void close(InputStream inputStream) {
		if (inputStream == null) {
			return;
		}
		try {
			inputStream.close();
		} catch (Exception e) {
		}
	}

	/**
	 * 关闭写出流
	 * 
	 * @author songkun
	 * @create 2017年5月15日 下午6:58:48
	 * @param outputStream
	 * @return void
	 */
	private static final void close(OutputStream outputStream) {
		if (outputStream == null) {
			return;
		}
		try {
			outputStream.close();
		} catch (Exception e) {
		}
	}

	/**
	 * 根据根目录+包名，转换成文件
	 * 
	 * @author songkun
	 * @create 2017年4月25日 上午11:01:56
	 * @param targetPackage
	 * @param rootDir
	 * @return File
	 */
	public static final File toFile(String targetPackage, String rootDir) {
		String filePath = rootDir == null ? "" : rootDir;
		String[] splits = targetPackage.split("\\.");
		if (!filePath.endsWith("/")) {
			filePath = filePath + "/";
		}
		for (String split : splits) {
			filePath = filePath + split + "/";
		}
		return new File(filePath);
	}

	/**
	 * 构造model文件
	 * 
	 * @author songkun
	 * @create 2017年4月21日 下午6:50:45
	 * @param classFileName
	 * @param targetPackage
	 * @param rootDir
	 * @return File
	 */
	public static final File initJavaFile(String classFileName, String targetPackage, String rootDir, boolean overWrite) {
		String filePath = rootDir == null ? "" : rootDir;
		String[] splits = targetPackage.split("\\.");
		if (!filePath.endsWith("/")) {
			filePath = filePath + "/";
		}
		for (String split : splits) {
			filePath = filePath + split + "/";
		}
		filePath = filePath + classFileName;
		File file = new File(filePath);
		if (file.exists()) {
			if (overWrite) {
				file.renameTo(new File(file.getParent(), file.getName() + "_" + System.currentTimeMillis()));
			} else {
				return null;
			}
		}
		return file;
	}

	/**
	 * 初始化xml文件
	 * 
	 * @author songkun
	 * @create 2017年4月23日 下午2:20:10
	 * @param fileName
	 * @param folderName
	 * @param rootDir
	 * @param overWrite
	 * @return File
	 */
	public static final File initXmlFile(String fileName, String folderName, String rootDir, boolean overWrite) {
		if (fileName == null || fileName.length() <= 0) {
			return null;
		}
		String filePath = rootDir == null ? "" : rootDir;
		if (!filePath.endsWith("/")) {
			filePath = filePath + "/";
		}
		filePath = filePath + (folderName == null ? "" : folderName);
		if (!filePath.endsWith("/")) {
			filePath = filePath + "/";
		}
		filePath = filePath + fileName;
		File file = new File(filePath);
		if (file.exists()) {
			if (overWrite) {
				file.renameTo(new File(file.getParent(), file.getName() + "_" + System.currentTimeMillis()));
			} else {
				return null;
			}
		}
		return file;
	}

	/**
	 * 读文件，并转化成字符串
	 * 
	 * @author songkun
	 * @create 2017年4月21日 下午4:10:03
	 * @param file
	 * @return String
	 */
	public static final String readFileToString(File file) {
		if (file == null || !file.exists() || file.length() <= 0) {
			return null;
		}
		InputStream input = null;
		try {
			input = new FileInputStream(file);
			return toString(input, "utf-8");
		} catch (Exception e) {
		} finally {
			try {
				if (input != null) {
					input.close();
				}
			} catch (Exception e2) {
			}
		}
		return null;
	}

	/**
	 * 将内容写入文件
	 * 
	 * @author songkun
	 * @create 2017年4月21日 下午6:39:51
	 * @param file
	 * @param content
	 * @return void
	 */
	public static final void writeStringToFile(File file, String content) {
		if (file == null || content == null || content.length() <= 0 || file.exists()) {
			return;
		}
		if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
			return;
		}
		OutputStream stream = null;
		Writer writer = null;
		try {
			stream = new FileOutputStream(file);
			writer = new OutputStreamWriter(stream, "utf-8");
			writer.write(content);
		} catch (Exception e) {
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (Exception e) {
			}
		}
	}

	/**
	 * Get the contents of an <code>InputStream</code> as a String using the
	 * specified character encoding.
	 * <p>
	 * Character encoding names can be found at
	 * <a href="http://www.iana.org/assignments/character-sets">IANA</a>.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a
	 * <code>BufferedInputStream</code>.
	 * 
	 * @param input
	 *            the <code>InputStream</code> to read from
	 * @param encoding
	 *            the encoding to use, null means platform default
	 * @return the requested String
	 * @throws NullPointerException
	 *             if the input is null
	 * @throws IOException
	 *             if an I/O error occurs
	 * @since Commons IO 1.1
	 */
	public static String toString(InputStream input, String encoding) throws IOException {
		StringWriter sw = new StringWriter();
		copy(input, sw, encoding);
		return sw.toString();
	}

	/**
	 * 二进制拷贝
	 * 
	 * @author songkun
	 * @create 2017年5月15日 下午7:55:18
	 * @param input
	 * @param output
	 * @param encoding
	 * @throws IOException
	 * @return void
	 */
	public static void copy(InputStream input, OutputStream output) throws IOException {
		byte[] buffer = new byte[1024 * 4];
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
		}
	}

	/**
	 * Copy bytes from an <code>InputStream</code> to chars on a
	 * <code>Writer</code> using the specified character encoding.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a
	 * <code>BufferedInputStream</code>.
	 * <p>
	 * Character encoding names can be found at
	 * <a href="http://www.iana.org/assignments/character-sets">IANA</a>.
	 * <p>
	 * This method uses {@link InputStreamReader}.
	 *
	 * @param input
	 *            the <code>InputStream</code> to read from
	 * @param output
	 *            the <code>Writer</code> to write to
	 * @param encoding
	 *            the encoding to use, null means platform default
	 * @throws NullPointerException
	 *             if the input or output is null
	 * @throws IOException
	 *             if an I/O error occurs
	 * @since Commons IO 1.1
	 */
	public static void copy(InputStream input, Writer output, String encoding) throws IOException {
		if (encoding == null) {
			copy(input, output);
		} else {
			InputStreamReader in = new InputStreamReader(input, encoding);
			copy(in, output);
		}
	}

	/**
	 * Copy bytes from an <code>InputStream</code> to chars on a
	 * <code>Writer</code> using the default character encoding of the platform.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a
	 * <code>BufferedInputStream</code>.
	 * <p>
	 * This method uses {@link InputStreamReader}.
	 *
	 * @param input
	 *            the <code>InputStream</code> to read from
	 * @param output
	 *            the <code>Writer</code> to write to
	 * @throws NullPointerException
	 *             if the input or output is null
	 * @throws IOException
	 *             if an I/O error occurs
	 * @since Commons IO 1.1
	 */
	public static void copy(InputStream input, Writer output) throws IOException {
		InputStreamReader in = new InputStreamReader(input);
		copy(in, output);
	}
	// copy from Reader
	// -----------------------------------------------------------------------
	/**
	 * Copy chars from a <code>Reader</code> to a <code>Writer</code>.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a
	 * <code>BufferedReader</code>.
	 * <p>
	 * Large streams (over 2GB) will return a chars copied value of
	 * <code>-1</code> after the copy has completed since the correct number of
	 * chars cannot be returned as an int. For large streams use the
	 * <code>copyLarge(Reader, Writer)</code> method.
	 *
	 * @param input
	 *            the <code>Reader</code> to read from
	 * @param output
	 *            the <code>Writer</code> to write to
	 * @return the number of characters copied
	 * @throws NullPointerException
	 *             if the input or output is null
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws ArithmeticException
	 *             if the character count is too large
	 * @since Commons IO 1.1
	 */
	public static int copy(Reader input, Writer output) throws IOException {
		long count = copyLarge(input, output);
		if (count > Integer.MAX_VALUE) {
			return -1;
		}
		return (int) count;
	}

	/**
	 * Copy chars from a large (over 2GB) <code>Reader</code> to a
	 * <code>Writer</code>.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a
	 * <code>BufferedReader</code>.
	 *
	 * @param input
	 *            the <code>Reader</code> to read from
	 * @param output
	 *            the <code>Writer</code> to write to
	 * @return the number of characters copied
	 * @throws NullPointerException
	 *             if the input or output is null
	 * @throws IOException
	 *             if an I/O error occurs
	 * @since Commons IO 1.3
	 */
	public static long copyLarge(Reader input, Writer output) throws IOException {
		char[] buffer = new char[1024 * 4];
		long count = 0;
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}
}
