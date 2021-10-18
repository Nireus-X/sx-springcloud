package com.nireus.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.nireus.common.base.BaseEntity;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class SysUser extends BaseEntity {

	@TableId(type = IdType.AUTO)
	private Long id;

	private String username;

	private String nickname;

	private String mobile;

	private Integer gender;

	private String avatar;

	private String password;

	private Integer status;

	private Long deptId;

	@ApiModelProperty("逻辑删除标识 0-未删除 1-已删除")
	@TableLogic(value = "0", delval = "1")
	private Integer deleted;

	@TableField(exist = false)
	private String deptName;

	@TableField(exist = false)
	private List<Long> roleIds;

	@TableField(exist = false)
	private String roleNames;

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

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
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

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public List<Long> getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(List<Long> roleIds) {
		this.roleIds = roleIds;
	}

	public String getRoleNames() {
		return roleNames;
	}

	public void setRoleNames(String roleNames) {
		this.roleNames = roleNames;
	}

	@Override
	public String toString() {
		return "SysUser [id=" + id + ", username=" + username + ", nickname=" + nickname + ", mobile=" + mobile
				+ ", gender=" + gender + ", avatar=" + avatar + ", password=" + password + ", status=" + status
				+ ", deptId=" + deptId + ", deleted=" + deleted + ", deptName=" + deptName + ", roleIds=" + roleIds
				+ ", roleNames=" + roleNames + "]";
	}

}
