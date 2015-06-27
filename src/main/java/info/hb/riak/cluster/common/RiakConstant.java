package info.hb.riak.cluster.common;

/**
 * 应用于视频图片的Riak集群常量
 *
 * @author wanggang
 *
 */
public class RiakConstant {

	// 需要在Riak服务端创建，默认只有default
	public static final String IMAGE_BUCKET_TYPE = "image_bucket_type";

	// 默认Raik访问端口
	public static final int RIAK_PORT = 8087;

	// 默认对外HTTP访问端口
	public static final int HTTP_PORT = 8098;

}
