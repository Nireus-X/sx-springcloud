package com.nireus.authorization.service.impl;

import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;

import javax.sql.DataSource;

/**
 * 
 * <p>
 * 	数据库查询客户端详情实现
 * </p>
 * 
 * @author wushixiong
 * @since 2021-05-26 18:12:31
 */
public class JdbcClientDetailsServiceImpl extends JdbcClientDetailsService {

	public JdbcClientDetailsServiceImpl(DataSource dataSource) {
		super(dataSource);
	}

	@Override
	public ClientDetails loadClientByClientId(String clientId) {
		return super.loadClientByClientId(clientId);
	}
}
