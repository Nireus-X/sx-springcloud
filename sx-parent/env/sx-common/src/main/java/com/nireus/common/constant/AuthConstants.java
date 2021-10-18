package com.nireus.common.constant;

/**
 * 
 * <p>
 * 认证常量
 * </p>
 * 
 * @author wushixiong
 * @since 2021-05-26 16:28:16
 */
public class AuthConstants {

	/**
	 * 认证请求头key
	 */
	public static final String AUTHORIZATION_KEY = "Authorization";

	/**
	 * JWT令牌前缀
	 */
	public static final String AUTHORIZATION_PREFIX = "bearer ";

	/**
	 * Basic认证前缀
	 */
	public static final String BASIC_PREFIX = "Basic ";

	/**
	 * JWT载体key
	 */
	public static final String JWT_PAYLOAD_KEY = "payload";

	/**
	 * JWT存储权限前缀
	 */
	public static final String AUTHORITY_PREFIX = "ROLE_";

	/**
	 * JWT存储权限属性
	 */
	public static final String JWT_AUTHORITIES_KEY = "authorities";

	/**
	 * 后台管理接口路径匹配
	 */
	public static final String ADMIN_URL_PATTERN = "**/api.admin/**";

	/**
	 * Redis缓存权限规则key
	 */
	public static final String PERMISSION_ROLES_KEY = "auth:permission:roles";

	public static final String CLIENT_DETAILS_FIELDS = "client_id, CONCAT('{bcrypt}',client_secret) as client_secret, resource_ids, scope, "
			+ "authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, "
			+ "refresh_token_validity, additional_information, autoapprove";

	public static final String BASE_CLIENT_DETAILS_SQL = "select " + CLIENT_DETAILS_FIELDS
			+ " from oauth_client_details";

	public static final String FIND_CLIENT_DETAILS_SQL = BASE_CLIENT_DETAILS_SQL + " order by client_id";

	public static final String SELECT_CLIENT_DETAILS_SQL = BASE_CLIENT_DETAILS_SQL + " where client_id = ?";

	/**
	 * 密码加密方式
	 */
	public static final String BCRYPT = "{bcrypt}";

	public static final String USER_ID_KEY = "userId";

	public static final String USER_NAME_KEY = "userName";

	public static final String CLIENT_ID_KEY = "clientId";
}
