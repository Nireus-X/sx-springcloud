package com.nireus.gateway.util;

import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nireus.common.result.Result;
import com.nireus.common.result.ResultCode;
import com.nireus.common.util.JsonUtils;

import reactor.core.publisher.Mono;

/**
 * 
 * <p>
 * web服务工具类
 * </p>
 * 
 * @author wushixiong
 * @since 2021-05-26 16:42:44
 */
public class WebUtils {

	private static final Logger log = LoggerFactory.getLogger(WebUtils.class);

	public static Mono writeFailedToResponse(ServerHttpResponse response, ResultCode resultCode) {
		response.setStatusCode(HttpStatus.OK);
		response.getHeaders().set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		response.getHeaders().set("Access-Control-Allow-Origin", "*");
		response.getHeaders().set("Cache-Control", "no-cache");
		String body = null;
		try {
			body = JsonUtils.toJson(Result.failed(resultCode));
		} catch (JsonProcessingException e) {
			log.error(e.getMessage(), e);
		}
		DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(Charset.forName("UTF-8")));
		return response.writeWith(Mono.just(buffer)).doOnError(error -> DataBufferUtils.release(buffer));
	}
}
