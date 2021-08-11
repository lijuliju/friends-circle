package com.friends.service;

import java.util.List;

import com.friends.entity.DynamicComment;

public interface ICommentService {
	
  // 评论动态
  public void commentDynamic(DynamicComment dynamicComment);
  
  // 回复评论动态
//  public void replyComment(DynamicComment dynamicComment);
  
  // 删除评论动态
  public void deleteComment(DynamicComment dynamicComment);
  
  // 查看评论信息
  public List<DynamicComment> findDynamicCommentList(Long dynaId,Long currentUserId);
  
//查看评论信息
 public List<DynamicComment> findDynamicCommentPage(Long dynaId,Long currentUserId,int page,int pageNumber);
  

  // 查看指定评论
  public DynamicComment findDynamicCommentDetailById(Long commentId);
  
  //查看指定评论的评论
//  public DynamicComment findReplyCommentDetailById(Long replyId);
  
  // 更新评论点赞数
  public void updateCommentLikeCount(DynamicComment dynamicCommentExist,String type);
  
  // 更新回复数
  public void updateReplyCount(DynamicComment dynamicCommentExist);
  
  //更新评论的评论点赞数
//  public void updateReplyCommentLikeCount(DynamicComment dynamicCommentExist,String type);
}
