package com.friends.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.friends.dao.UserInfoMapper;
import com.friends.entity.UserInfo;
import com.friends.service.IUserInfoService;

/**
 * @author Evan
 * @date 2019/4
 */
@Service("userInfoServiceImpl")
public class UserInfoServiceImpl implements IUserInfoService {
	
	@Autowired
    private UserInfoMapper userInfoMapper;

	@Override
	public void saveUserInfo(long userId,String type) {
		UserInfo userInfoExist = userInfoMapper.getUserDetailById(userId);
		if(null != userInfoExist) {
			// 更新发布个数
			long pushCount = userInfoExist.getPushCount();
			long praisedCount = userInfoExist.getPraisedCount();
			switch (type) {
			case "push":
				pushCount = pushCount + 1;
				break;
			case "unpush":
				pushCount = pushCount - 1;
				break;
			case "like":
				praisedCount = praisedCount + 1;
				break;
			case "dislike":
				praisedCount = praisedCount - 1;
				break;
			default:
				break;
			}
			userInfoExist.setPushCount(pushCount);
			userInfoExist.setPraisedCount(praisedCount);
			userInfoMapper.updateUserInfoById(userInfoExist);
		}else {
			// 直接新增
			UserInfo userInfo = new UserInfo();
			userInfo.setUserId(userId);
			userInfo.setAddtime(System.currentTimeMillis());
			userInfo.setPushCount(1l);
			userInfo.setPraisedCount(0l);
			userInfoMapper.insertUserInfo(userInfo);
		}
	}

	@Override
	public UserInfo getUserDetailById(long userId) {
		return userInfoMapper.getUserDetailById(userId);
	}



}
