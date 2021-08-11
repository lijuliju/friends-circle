package com.friends.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import javax.validation.constraints.NotNull;


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
	
	private Long commentId;
	
	private Long replyId;
    
    private Long addtime;
    
    private Long delFlag;
    
    private Long delTime;

    
}

