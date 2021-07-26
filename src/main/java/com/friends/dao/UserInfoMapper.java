package com.friends.dao;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.friends.entity.UserInfo;

@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {
	
	// 新增用户信息
	public int insertUserInfo(UserInfo userInfo);
	
	// 更新用户信息
	public int updateUserInfoById(UserInfo userInfo);
	
	// 查询详情
	public UserInfo getUserDetailById(long userId);
	
}
