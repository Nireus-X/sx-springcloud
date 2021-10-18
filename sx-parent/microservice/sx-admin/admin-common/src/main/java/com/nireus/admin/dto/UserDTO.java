package com.nireus.admin.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * <p>
 * 用户信息参数封装
 * </p>
 * 
 * @author wushixiong
 * @since 2021-05-28 17:48:33
 */
public class UserDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String username;
	private String password;
	private Integer status;
	private List<Long> roleIds;

	public UserDTO() {
		super();
	}

	public UserDTO(Long id, String username, String password, Integer status, List<Long> roleIds) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.status = status;
		this.roleIds = roleIds;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public List<Long> getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(List<Long> roleIds) {
		this.roleIds = roleIds;
	}

	@Override
	public String toString() {
		return "UserDTO [id=" + id + ", username=" + username + ", password=" + password + ", status=" + status
				+ ", roleIds=" + roleIds + "]";
	}

}
