package info.hb.riak.cluster.client;

import java.awt.image.BufferedImage;

import com.basho.riak.client.api.commands.kv.UpdateValue.Update;

/**
 * Riak客户端接口
 *
 * @author wanggang
 *
 */
public interface HBRiakClient {

	String getIpsStr();

	String[] getIps();

	/**
	 * 存储、读取和删除对象：FetchValue/MultiFetch/StoreValue/UpdateValue/DeleteValue
	 */
	// 写数据
	void writeText(String bucketType, String bucketName, String key, String data);

	void writeObject(String bucketType, String bucketName, String key, Object object);

	void writeImage(String bucketType, String bucketName, String key, BufferedImage bi, String format);

	// 读数据
	<T> T readObject(String bucketType, String bucketName, String key, Class<T> type);

	// 更新数据
	void updateObject(String bucketType, String bucketName, String key, Update<Object> data);

	// 删除数据
	void deleteObject(String bucketType, String bucketName, String key);

	/**
	 * 列出Namespace中的所有key：ListKeys
	 */

	/**
	 * 二次索引(2i)命令：RawIndexQuery/BinIndexQuery/IntIndexQuery/BigIntIndexQuery
	 */

	/**
	 * 提取和存储数据类型(CRDTs)：FetchCounter/FetchSet/FetchMap/UpdateCounter/UpdateSet/UpdateMap
	 */

	/**
	 * 查询和修改buckets：FetchBucketProperties/StoreBucketProperties/ListBuckets
	 */

	/**
	 * 搜索命令：Search/FetchIndex/StoreIndex/DeleteIndex/FetchSchema/StoreSchema
	 */

	/**
	 * MapReduce命令：BucketMapReduce/BucketKeyMapReduce/IndexMapReduce/SearchMapReduce
	 */

	void close();

}
