package com.friends.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.friends.util.AbstractEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.sql.Timestamp;
import java.util.List;

/**
 * 点赞表
 *
 * @author Evan
 * @date 2019/4
 */
@Data
public class DynamicLike{

	@TableId(value = "id", type = IdType.UUID)
    private long id;
	
	@NotNull(message = "动态唯一标识不能为空")
	private Long dynaId;
    
	@NotNull(message = "点赞用户唯一标识不能为空")
    private Long userId;
    
    private Long addtime;
    
    private Long delFlag;
    
    private Long delTime;

    
}

