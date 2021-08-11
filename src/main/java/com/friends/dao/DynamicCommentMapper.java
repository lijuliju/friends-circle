package com.friends.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.friends.entity.DynamicComment;

@Mapper
public interface DynamicCommentMapper extends BaseMapper<DynamicComment> {
	
	public List<DynamicComment> findDynamicCommentList(DynamicComment dynamicComment);
	
	public DynamicComment findDynamicCommentById(DynamicComment dynamicComment);
	
	public int insertComment(DynamicComment dynamicComment);
	
	public int updateDynamicCommentById(DynamicComment dynamicComment);
	
//	public int updateLikeCountById(DynamicComment dynamicComment);
	
}
