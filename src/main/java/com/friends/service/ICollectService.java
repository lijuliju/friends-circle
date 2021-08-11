package com.friends.service;



import java.util.List;

import com.friends.entity.DynamicCollect;

public interface ICollectService {
	
	// 保存收藏功能
	public void saveCollect(Long currentUserId,Long dynaId);
  
	// 取消收藏功能
	public void cancelCollect(DynamicCollect dynamicCollect);
	
	public DynamicCollect getCollectBy(Long currentUserId,Long dynaId);
	
	public List<Long> getDynaIdList(Long currentUserId);
  
}
