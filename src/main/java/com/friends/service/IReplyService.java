package com.friends.service;


import java.util.List;

import com.friends.entity.DynamicComment;

public interface IReplyService {
	// 查看评论下的回复
	public List<DynamicComment> getReplyList(Long commentId);
	
	// 查看评论下的回复
	public List<DynamicComment> getReplyPage(Long commentId,int page,int pageNumber);
	
  // 回复评论动态
  public void replyComment(DynamicComment dynamicComment);
  
  //查看指定评论的评论
  public DynamicComment findReplyCommentDetailById(Long replyId);
  
  //更新评论的评论点赞数
  public void updateReplyCommentLikeCount(DynamicComment dynamicCommentExist,String type);
  
  
}
