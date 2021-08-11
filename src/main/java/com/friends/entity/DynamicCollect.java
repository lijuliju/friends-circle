package com.friends.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Data;

import java.util.List;

import javax.validation.constraints.NotNull;

/**
 * 	动态详情表
 *
 * @author Evan
 * @date 2019/4
 */
@Data
public class DynamicCollect{
	
	@TableId(value = "id", type = IdType.UUID)
    private long id;
	
	@NotNull(message = "用户不能为空")
    private Long userId;

    private Long dynaId;// 动态详情

    private Long collectTime;
    
    private Long delTime;
    
    private Long delFlag = 0l;	// 是否删除
    
}

