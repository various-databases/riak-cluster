package info.hb.riak.cluster.client;

import static org.junit.Assert.assertNotNull;

import java.util.Properties;

import org.junit.Test;

import zx.soft.utils.config.ConfigUtil;

public class HBRiakClientimplTest {

	@Test
	public void testRiakCluster() {
		Properties props = ConfigUtil.getProps("riak.properties");
		assertNotNull(props.getProperty("riak.cluster"));
	}

}
