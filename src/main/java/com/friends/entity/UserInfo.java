package com.friends.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import java.sql.Timestamp;

/**
 * User entity.
 *
 * @author Evan
 * @date 2019/4
 */
@Data
public class UserInfo{

	@TableId(value = "user_id", type = IdType.UUID)
    private long userId;	//  用户ID
    
    private long addtime;	// 添加时间
    
    private long pushCount;		// 发布总数
    
    private long praisedCount;		// 获赞总数

}

