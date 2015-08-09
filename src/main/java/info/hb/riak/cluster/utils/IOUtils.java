package info.hb.riak.cluster.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * 文件操作工具类
 *
 * @author wanggang
 *
 */
public class IOUtils {

	public static byte[] getFile2Bytes(String fileName) throws IOException {
		return getFile2Bytes(new File(fileName));
	}

	public static byte[] getFile2Bytes(File file) throws IOException {
		try (FileInputStream fis = new FileInputStream(file);) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] buf = new byte[(int) file.length()];
			for (int readNum; (readNum = fis.read(buf)) != -1;) {
				bos.write(buf, 0, readNum);
			}
			return bos.toByteArray();
		}
	}

}
