package org.slingerxv.limitart.net.http.server.event;

import org.slingerxv.limitart.collections.ConstraintMap;
import org.slingerxv.limitart.net.http.message.UrlMessage;
import org.slingerxv.limitart.net.listener.NettyEventListener;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.HttpMessage;

/**
 * 服务器事件
 * 
 * @author Hank
 *
 */
public interface HttpServerEventListener extends NettyEventListener {
	void dispatchMessage(UrlMessage message, ConstraintMap<String> map);

	/**
	 * 当消息超标时会调用此函数
	 * 
	 * @param ctx
	 * @param oversized
	 */
	void onMessageOverSize(Channel channel, HttpMessage oversized);

	void onServerBind(Channel channel);
}
