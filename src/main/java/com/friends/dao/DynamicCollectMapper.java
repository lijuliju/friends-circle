package com.friends.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.friends.entity.DynamicCollect;
import com.friends.entity.DynamicLike;

@Mapper
public interface DynamicCollectMapper extends BaseMapper<DynamicLike> {
	
	public DynamicCollect findCollectBy(DynamicCollect dynamicCollect);
	
	public List<DynamicCollect> findDynamicCollectList(DynamicCollect dynamicCollect);
	
	public int insert(DynamicCollect dynamicCollect);
	
	public int deleteDynamicCollectById(DynamicCollect dynamicCollect);
	
	public List<Long> findDynaIdByUser(Long userId);
	
}
