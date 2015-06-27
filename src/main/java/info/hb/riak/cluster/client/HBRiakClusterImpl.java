package info.hb.riak.cluster.client;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import zx.soft.utils.log.LogbackUtil;

import com.basho.riak.client.api.RiakClient;
import com.basho.riak.client.core.RiakCluster;
import com.basho.riak.client.core.RiakNode;

/**
 * 图片存储Riak集群客户端
 *
 * @author wanggang
 *
 */
public class HBRiakClusterImpl extends HBRiakClientimpl {

	private static Logger logger = LoggerFactory.getLogger(HBRiakClusterImpl.class);

	private RiakCluster cluster;

	public HBRiakClusterImpl() {
		super();
	}

	@Override
	void init(Properties props) {
		try {
			RiakNode.Builder builder = new RiakNode.Builder()
					.withMinConnections(Integer.parseInt(props.getProperty("riak.min.connections")))
					.withMaxConnections(Integer.parseInt(props.getProperty("riak.max.connections")))
					.withRemotePort(Integer.parseInt(props.getProperty("riak.port")));
			//			builder.withAuth(props.getProperty("riak.username"), props.getProperty("riak.password"), null);
			List<RiakNode> nodes = RiakNode.Builder.buildNodes(builder, Arrays.asList(ips));
			cluster = new RiakCluster.Builder(nodes).build();
			cluster.start();
			client = new RiakClient(cluster);
		} catch (Exception e) {
			logger.error("Exception:{}", LogbackUtil.expection2Str(e));
			throw new RuntimeException(e);
		}
	}

	@Override
	public void close() {
		client.shutdown();
		// 不能关闭
		//		cluster.shutdown();
	}

}
