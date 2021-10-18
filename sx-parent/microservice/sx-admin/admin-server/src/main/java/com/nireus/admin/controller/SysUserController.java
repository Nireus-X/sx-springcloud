package com.nireus.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nireus.admin.dto.UserDTO;
import com.nireus.admin.entity.SysUser;
import com.nireus.admin.service.SysUserService;
import com.nireus.common.result.Result;
import com.nireus.common.result.ResultCode;

import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * <p>
 * 用户信息控制器
 * </p>
 * 
 * @author wushixiong
 * @since 2021-05-28 17:42:21
 */
@RestController
@RequestMapping("/user")
public class SysUserController {

	@Autowired
	private SysUserService sysUserService;

	@ApiOperation(value = "根据用户名获取用户信息")
	@GetMapping("/username/{username}")
	public Result<?> getUserByUsername(@PathVariable String username) {
		SysUser user = sysUserService.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username));

		// 用户不存在，返回自定义异常，让调用端处理后续逻辑
		if (user == null) {
			return Result.failed(ResultCode.USER_NOT_EXIST);
		}
		// Entity->DTO
		UserDTO userDTO = new UserDTO();
		BeanUtil.copyProperties(user, userDTO);

		// 获取用户的角色ID集合
//        List<Long> roleIds = iSysUserRoleService.list(new LambdaQueryWrapper<SysUserRole>()
//                .eq(SysUserRole::getUserId, user.getId())
//        ).stream().map(item -> item.getRoleId()).collect(Collectors.toList());
//        userDTO.setRoleIds(roleIds);

		return Result.success(userDTO);
	}
}
