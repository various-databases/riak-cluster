package info.hb.riak.cluster.client;

import info.hb.riak.cluster.utils.ImageUtils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import zx.soft.utils.config.ConfigUtil;
import zx.soft.utils.log.LogbackUtil;

import com.basho.riak.client.api.RiakClient;
import com.basho.riak.client.api.cap.Quorum;
import com.basho.riak.client.api.commands.kv.DeleteValue;
import com.basho.riak.client.api.commands.kv.FetchValue;
import com.basho.riak.client.api.commands.kv.StoreValue;
import com.basho.riak.client.api.commands.kv.UpdateValue;
import com.basho.riak.client.api.commands.kv.UpdateValue.Update;
import com.basho.riak.client.core.query.Location;
import com.basho.riak.client.core.query.Namespace;
import com.basho.riak.client.core.query.RiakObject;
import com.basho.riak.client.core.util.BinaryValue;

public class HBRiakClientimpl implements HBRiakClient {

	static Logger logger = LoggerFactory.getLogger(HBRiakClientimpl.class);

	RiakClient client;

	// IP地址列表
	String ipsStr;
	String[] ips;

	// 根据需求设置，确认是否是副本数
	final int QUORUM_SIZE;

	public HBRiakClientimpl() {
		Properties props = ConfigUtil.getProps("riak.properties");
		ipsStr = props.getProperty("riak.cluster");
		ips = ipsStr.split(",");
		// 法定数要多于一半的节点
		QUORUM_SIZE = ips.length / 2 + 1;
		init(props);
	}

	void init(Properties props) {
		try {
			client = RiakClient.newClient(ips);
		} catch (Exception e) {
			logger.error("Exception:{}", LogbackUtil.expection2Str(e));
			throw new RuntimeException(e);
		}
	}

	public String getIpsStr() {
		return ipsStr;
	}

	public String[] getIps() {
		return ips;
	}

	@Override
	public void writeText(String bucketType, String bucketName, String key, String data) {
		try {
			RiakObject riakObject = new RiakObject().setContentType("text/plain").setValue(BinaryValue.create(data));
			writeObject(bucketType, bucketName, key, riakObject);
		} catch (Exception e) {
			logger.error("Exception:{}", LogbackUtil.expection2Str(e));
			// 注意：项目稳定时，需要把抛出异常去掉，防止因个别异常线程停止
			throw new RuntimeException(e);
		}
	}

	@Override
	public void writeImage(String bucketType, String bucketName, String key, BufferedImage bi, String format) {
		try {
			RiakObject riakObject = new RiakObject().setContentType("image/" + format).setValue(
					BinaryValue.create(ImageUtils.transBI2BytesBytes(bi, format)));
			writeObject(bucketType, bucketName, key, riakObject);
		} catch (IOException e) {
			logger.error("Exception:{}", LogbackUtil.expection2Str(e));
			// 注意：项目稳定时，需要把抛出异常去掉，防止因个别异常线程停止
			throw new RuntimeException(e);
		}
	}

	@Override
	public void writeObject(String bucketType, String bucketName, String key, Object object) {
		try {
			Location location = new Location(new Namespace(bucketType, bucketName), key);
			// 参考配置文件：http://riak.com.cn/riak/latest/ops/advanced/configs/configuration-files/
			/*
			n_val - 保存的副本数。注意：详细讨论参见“CAP 控制”一文。
			读、写和删除请求的法定值。可选值包括数字（例如，{r, 2}），以及下面列出的值：
			quorum： 大多数副本要响应，等同于 n_val / 2 + 1
			all： 所有副本都要响应
			r - 读请求的法定值（成功的 GET 请求必须返回结果的 Riak 节点数）默认值：quorum
			pr - 主读取请求的法定值（成功的 GET 请求必须返回结果的 Riak 主节点（非备用节点）数）默认值：0 注意：关于主节点的说明请阅读“最终一致性”一文
			w - 写请求的法定值（必须接受 PUT 请求的 Riak 节点数）默认值：quorum
			dw - 持续写请求的法定值（接受从存储后台发出的写请求的 Riak 节点数）默认值：quorum
			pw - 主写入请求的法定值（必须接受 PUT 请求的 Riak 主节点（非备用节点）数）默认值：0
			rw - 删除请求的法定值。默认值：quorum
			*/
			StoreValue store = new StoreValue.Builder(object).withLocation(location)
			//					.withOption(Option.RETURN_BODY, true)
					.withOption(StoreValue.Option.W, new Quorum(QUORUM_SIZE)).build();
			client.execute(store);
		} catch (ExecutionException | InterruptedException e) {
			logger.error("Exception:{}", LogbackUtil.expection2Str(e));
			// 注意：项目稳定时，需要把抛出异常去掉，防止因个别异常线程停止
			throw new RuntimeException(e);
		}
	}

	@Override
	public <T> T readObject(String bucketType, String bucketName, String key, Class<T> type) {
		try {
			Location location = new Location(new Namespace(bucketType, bucketName), key);
			FetchValue fv = new FetchValue.Builder(location).build();
			return client.execute(fv).getValue(type);
		} catch (ExecutionException | InterruptedException e) {
			logger.error("Exception:{}", LogbackUtil.expection2Str(e));
			// 注意：项目稳定时，需要把抛出异常去掉，防止因个别异常线程停止
			throw new RuntimeException(e);
		}
	}

	@Override
	public void updateObject(String bucketType, String bucketName, String key, Update<Object> object) {
		try {
			Location location = new Location(new Namespace(bucketType, bucketName), key);
			UpdateValue uv = new UpdateValue.Builder(location).withFetchOption(FetchValue.Option.DELETED_VCLOCK, true)
					.withUpdate(object).build();
			client.execute(uv);
		} catch (ExecutionException | InterruptedException e) {
			logger.error("Exception:{}", LogbackUtil.expection2Str(e));
			// 注意：项目稳定时，需要把抛出异常去掉，防止因个别异常线程停止
			throw new RuntimeException(e);
		}
	}

	@Override
	public void deleteObject(String bucketType, String bucketName, String key) {
		try {
			Location location = new Location(new Namespace(bucketType, bucketName), key);
			DeleteValue dv = new DeleteValue.Builder(location).build();
			client.execute(dv);
		} catch (ExecutionException | InterruptedException e) {
			logger.error("Exception:{}", LogbackUtil.expection2Str(e));
			// 注意：项目稳定时，需要把抛出异常去掉，防止因个别异常线程停止
			throw new RuntimeException(e);
		}
	}

	public static class UpdateObject extends Update<Object> {

		private Object text;

		public UpdateObject(Object text) {
			this.text = text;
		}

		@Override
		public Object apply(Object object) {
			return text;
		}

	}

	@Override
	public void close() {
		client.shutdown();
	}

}
