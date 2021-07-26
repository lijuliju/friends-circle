package com.friends.service;


import java.util.List;

import com.friends.entity.DynamicComment;
import com.friends.entity.DynamicLike;

public interface ILikeService {
  
  // 点赞动态
  public void giveDynamicLike(DynamicLike momentsLike);
  
  // 取消点赞动态
  public void cancelDynamicLike(DynamicLike momentsLike);
  
  public DynamicLike findDynamicLikeById(Long id);
  
  //查看评论信息
  public List<DynamicLike> findDynamicLikeList(Long dynaId);
  
}
