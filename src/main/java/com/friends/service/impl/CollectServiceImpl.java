package com.friends.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.friends.dao.DynamicCollectMapper;
import com.friends.entity.DynamicCollect;
import com.friends.service.ICollectService;

/**
 * @author Evan
 * @date 2019/4
 */
@Service("collectServiceImpl")
public class CollectServiceImpl implements ICollectService {
	
	@Autowired
    private DynamicCollectMapper collectDao;

	@Override
	public void saveCollect(Long currentUserId, Long dynaId) {
		DynamicCollect dynamicCollect = new DynamicCollect();
		dynamicCollect.setUserId(currentUserId);
		dynamicCollect.setDynaId(dynaId);
		dynamicCollect.setCollectTime(System.currentTimeMillis());
		collectDao.insert(dynamicCollect);
	}

	@Override
	public void cancelCollect(DynamicCollect dynamicCollect) {
		dynamicCollect.setDelFlag(1l);
		dynamicCollect.setDelTime(System.currentTimeMillis());
		collectDao.deleteDynamicCollectById(dynamicCollect);
	}

	@Override
	public DynamicCollect getCollectBy(Long currentUserId, Long dynaId) {
		DynamicCollect dynamicCollect = new DynamicCollect();
		dynamicCollect.setUserId(currentUserId);
		dynamicCollect.setDynaId(dynaId);
		return collectDao.findCollectBy(dynamicCollect);
	}

	@Override
	public List<Long> getDynaIdList(Long currentUserId) {
		return collectDao.findDynaIdByUser(currentUserId);
	}




}
