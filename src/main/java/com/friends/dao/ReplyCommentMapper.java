package com.friends.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.friends.entity.DynamicComment;

@Mapper
public interface ReplyCommentMapper extends BaseMapper<DynamicComment> {
	
	public List<DynamicComment> findReplyCommentList(DynamicComment momentsComment);
	
	public int insert(DynamicComment momentsComment);
	
	public int deleteReplyById(DynamicComment momentsComment);
	
	public DynamicComment findReplyCommentById(DynamicComment dynamicComment);
	
	public int updateLikeCountById(DynamicComment dynamicComment);
	
}
