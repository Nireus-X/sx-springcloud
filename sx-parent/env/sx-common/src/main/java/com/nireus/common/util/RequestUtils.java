package com.nireus.common.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.nireus.common.constant.AuthConstants;

import javax.servlet.http.HttpServletRequest;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 
 * <p>
 * 处理请求工具类
 * </p>
 * 
 * @author wushixiong
 * @since 2021-05-28 18:15:49
 */
public class RequestUtils {

	private static final Logger log = LoggerFactory.getLogger(RequestUtils.class);

	/**
	 * 
	 * <p>
	 * 获取HttpServletRequest
	 * </p>
	 * 
	 * @return
	 * @create wushixiong 2021-05-28 18:16:13
	 */
	public static HttpServletRequest getRequest() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		return request;
	}

	/**
	 * 
	 * <p>
	 * 获取jwt
	 * </p>
	 * 
	 * @return
	 * @create wushixiong 2021-05-28 18:16:26
	 */
	public static JSONObject getJwtPayload() {
		String jwtPayload = getRequest().getHeader(AuthConstants.JWT_PAYLOAD_KEY);
		JSONObject jsonObject = JSONUtil.parseObj(jwtPayload);
		return jsonObject;
	}

	/**
	 * 
	 * <p>
	 * 获取userId
	 * </p>
	 * 
	 * @return
	 * @create wushixiong 2021-05-28 18:17:32
	 */
	public static Long getUserId() {
		Long id = getJwtPayload().getLong(AuthConstants.USER_ID_KEY);
		return id;
	}

	/**
	 * 
	 * <p>
	 * 获取用户名称
	 * </p>
	 * 
	 * @return
	 * @create wushixiong 2021-05-28 18:18:15
	 */
	public static String getUsername() {
		String username = getJwtPayload().getStr(AuthConstants.USER_NAME_KEY);
		return username;
	}

	/**
	 * 获取JWT的载体中的clientId
	 *
	 * @return
	 */
	public static String getClientId() {
		String clientId = getJwtPayload().getStr(AuthConstants.CLIENT_ID_KEY);
		return clientId;
	}

	/**
	 * 获取登录认证的客户端ID
	 * <p>
	 * 兼容两种方式获取Oauth2客户端信息（client_id、client_secret）
	 * 方式一：client_id、client_secret放在请求路径中 方式二：放在请求头（Request
	 * Headers）中的Authorization字段，且经过加密，例如 Basic Y2xpZW50OnNlY3JldA== 明文等于
	 * client:secret
	 *
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getAuthClientId() {
		String clientId;

		HttpServletRequest request = getRequest();

		// 从请求路径中获取
		clientId = request.getParameter(AuthConstants.CLIENT_ID_KEY);
		if (StrUtil.isNotBlank(clientId)) {
			return clientId;
		}

		// 从请求头获取
		String basic = request.getHeader(AuthConstants.AUTHORIZATION_KEY);
		if (StrUtil.isNotBlank(basic) && basic.startsWith(AuthConstants.BASIC_PREFIX)) {
			basic = basic.replace(AuthConstants.BASIC_PREFIX, Strings.EMPTY);
			try {
				String basicPlainText = new String(Base64.getMimeDecoder().decode(basic), "UTF-8");
				clientId = basicPlainText.split(":")[0]; // client:secretF
			} catch (UnsupportedEncodingException e) {
				log.error(e.getMessage(), e);
			}
		}
		return clientId;
	}

	public static List<Long> getRoleIds() {
		List<String> list = getJwtPayload().get(AuthConstants.JWT_AUTHORITIES_KEY, List.class);
		List<Long> authorities = list.stream().map(Long::valueOf).collect(Collectors.toList());
		return authorities;
	}
}
