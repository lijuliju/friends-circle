package com.friends.service;

import java.util.List;

import com.friends.entity.Dynamic;

public interface IDynamicService {
	
  // 查看动态
  public List<Dynamic> getMomentMessageList(int page,int pageNumber,Boolean isHot,Dynamic momenstMessage);
  
  // 查看动态详情
  public Dynamic getMomentDetailById(Dynamic momenstMessage);
  
  // 发表动态
  public void publishMomentsMessage(Dynamic momenstMessage);
  
  // 删除动态
  public void deleteMomentsMessage(Dynamic momenstMessage);
  
  // 更新评论数
  public void updateCommentCount(Long commentCount,Long dynaId);
  
  // 更新点赞数
  public void updateLikeCount(Long likeCount,Long weekLikeCount,Long dynaId);
  
}
