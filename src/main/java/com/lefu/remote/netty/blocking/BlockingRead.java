package com.lefu.remote.netty.blocking;

import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.util.AttributeKey;

import java.net.SocketTimeoutException;

/**
 * 阻塞读取接口定义，仅建议在客户端使用！！
 * @author jiang.li
 *
 */
public interface BlockingRead {
	/**
	 * 默认的键名称
	 */
	public static final AttributeKey<BlockingRead> CHANNEL_BLOCKING_READ_KEY = AttributeKey.valueOf("__CHANNEL_BLOCKING_READ_KEY__");
	
	/**
	 * 读取消息，消息是经过 {@link ChannelPipeline} 处理后的消息，由最后的业务Handler传递。
	 * <pre>
	 * 如果有存在的消息未读取，那么当前的操作会一次性读取完毕,并立即返回并不会阻塞，否则会等待消息读取或者读取超时
	 * 注意，此消息的读取先于{@link ChannelInboundHandler#channelRead(io.netty.channel.ChannelHandlerContext, Object)} 完成
	 * 另外，在出现读超时后，会默认关闭掉连接以处理超时带来的逻辑读取问题
	 * </pre>
	 * @return
	 * @throws SocketTimeoutException
	 */
	public Object read() throws SocketTimeoutException;
	
	/**
	 * 清理资源
	 */
	public void clear();
}
