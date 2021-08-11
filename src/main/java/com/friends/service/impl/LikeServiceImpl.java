package com.friends.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.friends.dao.DynamicLikeMapper;
import com.friends.entity.DynamicLike;
import com.friends.service.ILikeService;

/**
 * @author Evan
 * @date 2019/4
 */
@Service("likeServiceImpl")
public class LikeServiceImpl implements ILikeService {
	
	
	@Autowired
    private DynamicLikeMapper momentLikeDAO;

	@Override
	public void giveDynamicLike(DynamicLike dynamicLike) {
		Long now = System.currentTimeMillis();
		dynamicLike.setAddtime(now);
		momentLikeDAO.insert(dynamicLike);
	}

	@Override
	public void cancelDynamicLike(DynamicLike dynamicLike) {
		Long now = System.currentTimeMillis();
		dynamicLike.setDelFlag(1l);
		dynamicLike.setDelTime(now);
		momentLikeDAO.deleteDynamicLikeById(dynamicLike);
	}

	@Override
	public DynamicLike findDynamicLikeById(Long id) {
		return momentLikeDAO.findDynamiclikeById(id);
	}

	@Override
	public List<DynamicLike> findDynamicLikeList(Long dynaId) {
		DynamicLike dynamicLike = new DynamicLike();
		dynamicLike.setDynaId(dynaId);
		return momentLikeDAO.findDynamicLikeList(dynamicLike);
	}

	@Override
	public List<DynamicLike> findDynamicLikeBy(Long userId,Long dynaId) {
		DynamicLike dynamicLike = new DynamicLike();
		dynamicLike.setDynaId(dynaId);
		dynamicLike.setUserId(userId);
		return momentLikeDAO.findDynamicLikeList(dynamicLike);
	}

	@Override
	public List<DynamicLike> findDynamicLikeList(DynamicLike dynamicLike) {
		return momentLikeDAO.findDynamicLikeList(dynamicLike);
	}



}
