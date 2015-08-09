package info.hb.riak.cluster.sample;

import info.hb.riak.cluster.client.HBRiakClient;
import info.hb.riak.cluster.client.HBRiakClusterImpl;
import info.hb.riak.cluster.utils.IOUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class VideoIOExample {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		File file = new File("/home/wanggang/develop/deeplearning/test-videos/test10.mp4");
		byte[] bytes = IOUtils.getFile2Bytes(file);

		HBRiakClient client = new HBRiakClusterImpl();
		// 写数据
		client.writeVideo("default", "videos", "test13", bytes);
		// 关闭资源
		client.close();
		System.out.println(bytes.length);
	}

}
