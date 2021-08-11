package com.friends.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.github.pagehelper.PageInfo;

import lombok.Data;

import java.util.List;

import javax.validation.constraints.NotNull;


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
    
    private Long likeFlag = 0l;// 登陆者是否点赞，默认为0 未点赞；1 已点赞
    
    private Long likeCount;// 评论的点赞数
    
    private Long replyCount = 0l;// 评论回复总数
    
    private List<DynamicLike> dynamicLikeList;
    
    private List<DynamicComment> replyList;
    
    private PageInfo<DynamicComment> replyPage;
    
    private DynamicLike currentUserLike;
    
    private Long replyPid;
    
    private String picture;
    
    private boolean hasParent = false;
    
    private DynamicComment replyParent;

    
}

