package info.hb.riak.cluster.sample;

import info.hb.riak.cluster.client.HBRiakClient;
import info.hb.riak.cluster.client.HBRiakClusterImpl;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class HBRiakClusterImplExample {

	/**
	 * 测试函数
	 */
	public static void main(String[] args) throws IOException {
		// http://hefei08:8098/riak/text_data/text_key
		HBRiakClient client = new HBRiakClusterImpl();

		/**
		 * Text操作
		 */
		//		// 写数据
		//		client.writeText("default", "text_data", "text_key", "写入数据");
		//		System.out.println(client.readObject("default", "text_data", "text_key", String.class));
		//		// 更新数据
		//		client.writeText("default", "text_data", "text_key", "更新数据");
		//		System.out.println(client.readObject("default", "text_data", "text_key", String.class));
		//		// 删除Text数据
		//		client.deleteObject("default", "text_data", "text_key");

		/**
		 * 对象操作
		 */
		//		 http://hefei08:8098/riak/object_data/object_key
		//		Book book = new Book();
		//		book.setTitle("《大数据实战》");
		//		book.setAuthor("永不止步");
		//		book.setBody("预练此功，必先自宫......");
		//		book.setIsbn("1111979723");
		//		book.setCopiesOwned(3);
		//		// 写数据
		//		client.writeObject("default", "object_data", "object_key", book);
		//		// 读数据
		//		Book rbook = client.readObject("default", "object_data", "object_key", Book.class);
		//		System.out.println(JsonUtils.toJson(rbook));
		//		client.deleteObject("default", "object_data", "object_key");

		/**
		 * Image操作
		 */
		// http://hefei08:8098/riak/videos/test1.jpg
		// 写数据
		client.writeImage("default", "videos", "test1.jpg", ImageIO.read(new File("test-image/test1.jpg")), "jpg");
		// 删除数据
		client.deleteObject("default", "videos", "test1.jpg");

		// 关闭资源
		client.close();
	}

}
