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
 * 评论表
 *
 * @author Evan
 * @date 2019/4
 */
@Data
public class DynamicComment{

	@TableId(value = "id", type = IdType.UUID)
    private long id;
	
	@NotNull(message = "动态唯一标识不能为空")
	private Long dynaId;
    
	@NotNull(message = "评论用户唯一标识不能为空")
    private Long userId;

	@NotNull(message = "评论内容不能为空")
    private String content;
    
    private Long addtime;
    
    private Long delTime;
    
    private Long delFlag;
    
    private Long comId;
    
}

