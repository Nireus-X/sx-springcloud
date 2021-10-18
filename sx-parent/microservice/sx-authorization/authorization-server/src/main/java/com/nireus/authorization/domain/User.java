package com.nireus.authorization.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.nireus.admin.dto.UserDTO;
import com.nireus.common.constant.AuthConstants;

import cn.hutool.core.collection.CollectionUtil;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 
 * <p>
 * 登录用户信息
 * </p>
 * 
 * @author wushixiong
 * @since 2021-05-26 18:28:19
 */
public class User implements UserDetails {

	private static final long serialVersionUID = 1L;

	private Long id;

	private String username;

	private String password;

	private Boolean enabled;

	private String clientId;

	private Collection<SimpleGrantedAuthority> authorities;

	public User(UserDTO user) {
		this.setId(user.getId());
		this.setUsername(user.getUsername());
		this.setPassword(AuthConstants.BCRYPT + user.getPassword());
		this.setEnabled(Integer.valueOf(1).equals(user.getStatus()));
		if (CollectionUtil.isNotEmpty(user.getRoleIds())) {
			authorities = new ArrayList<>();
			user.getRoleIds().forEach(roleId -> authorities.add(new SimpleGrantedAuthority(String.valueOf(roleId))));
		}
	}
//
//    public User(AuthMemberDTO member){
//        this.setId(member.getId());
//        this.setUsername(member.getUsername());
//        this.setPassword(AuthConstants.BCRYPT + member.getPassword());
//        this.setEnabled( Integer.valueOf(1).equals(member.getStatus()));
//    }

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setAuthorities(Collection<SimpleGrantedAuthority> authorities) {
		this.authorities = authorities;
	}
}
