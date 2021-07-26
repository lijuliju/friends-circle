package com.friends.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.friends.entity.Dynamic;

public interface DynamicMapper extends BaseMapper<Dynamic> {
	
	public List<Dynamic> findMomentMessageList(Dynamic momentMessage);
	
	public Dynamic findMomentMessagetById(Dynamic momentMessage);
	
	public int insert(Dynamic momentMessage);
	
	public int deleteById(Dynamic momentMessage);
	
	public int updateDynamicById(Dynamic dynamic);
	
	public int clearWeekLikeCount();
}
