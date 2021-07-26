package com.friends.service;


import com.friends.entity.UserInfo;

public interface IUserInfoService {
	
	// 新增用户
	public void saveUserInfo(long userId,String type);
	
	// 根据用户ID查询用户详情
	public UserInfo getUserDetailById(long userId);
  
}
