package com.friends.service;

import java.util.List;

import com.friends.entity.DynamicComment;

public interface ICommentService {
	
  // 评论动态
  public void commentDynamic(DynamicComment dynamicComment);
  
  // 回复评论动态
  public void replyComment(DynamicComment dynamicComment);
  
  // 删除评论动态
  public void deleteComment(DynamicComment dynamicComment);
  
  // 查看评论信息
  public List<DynamicComment> findDynamicCommentList(Long dynaId);
}
