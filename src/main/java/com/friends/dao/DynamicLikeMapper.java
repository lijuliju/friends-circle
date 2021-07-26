package com.friends.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.friends.entity.DynamicLike;

@Mapper
public interface DynamicLikeMapper extends BaseMapper<DynamicLike> {
	
	public DynamicLike findDynamiclikeById(Long id);
	
	public List<DynamicLike> findDynamicLikeList(DynamicLike momentsLike);
	
	public int insert(DynamicLike momentsLike);
	
	public int deleteDynamicLikeById(DynamicLike dynamicLike);
	
}
