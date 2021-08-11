package com.friends.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.friends.dao.DynamicLikeMapper;
import com.friends.dao.DynamicMapper;
import com.friends.entity.Dynamic;
import com.friends.entity.DynamicLike;
import com.friends.service.IDynamicService;
import com.github.pagehelper.PageHelper;

import java.util.List;

/**
 * @author Evan
 * @date 2019/4
 */
@Service("dynamicServiceImpl")
public class DynamicServiceImpl implements IDynamicService {
	
	@Autowired
    private DynamicMapper dynamicMapper;
	
	@Autowired
    private DynamicLikeMapper dynamicLikeMapper;
	
	@Override
	public List<Dynamic> getMomentMessageList(int page,int pageNumber,Boolean isHot,Dynamic dynamic) {
//		int page = dynamic.getPage();
//		int pageNumber = dynamic.getPageNumber();
		String orderBy = isHot?"week_like_count desc,addtime desc":"addtime desc";
		PageHelper.startPage(page, pageNumber,orderBy);
		List<Dynamic> dynamicList = dynamicMapper.findMomentMessageList(dynamic);
		return dynamicList;
	}

	@Override
	public void publishMomentsMessage(Dynamic momenstMessage) {
		Long now = System.currentTimeMillis();
		momenstMessage.setAddtime(now);
		momenstMessage.setLikeCount(0l);
		momenstMessage.setCommentCount(0l);
		momenstMessage.setEnable(0l);
		momenstMessage.setWeekLikeCount(0l);
		momenstMessage.setShareCount(0l);
		dynamicMapper.insert(momenstMessage);
	}

	@Override
	public void deleteMomentsMessage(Dynamic momenstMessage) {
		Long delTime = System.currentTimeMillis();
		momenstMessage.setEnable(1l);
		momenstMessage.setDelTime(delTime);
		dynamicMapper.deleteById(momenstMessage);
	}

	@Override
	public Dynamic getMomentDetailById(Dynamic dynamic) {
		return dynamicMapper.findMomentMessagetById(dynamic);
	}

	@Override
	public void updateCommentCount(Long commentCount,Long dynaId) {
		Dynamic dynamic = new Dynamic();
		dynamic.setId(dynaId);
		dynamic.setCommentCount(commentCount);
		dynamicMapper.updateDynamicById(dynamic);
	}

	@Override
	public void updateLikeCount(Long likeCount,Long weekLikeCount,Long dynaId) {
		Dynamic dynamic = new Dynamic();
		dynamic.setId(dynaId);
		dynamic.setLikeCount(likeCount);
		dynamic.setWeekLikeCount(weekLikeCount);
		dynamicMapper.updateDynamicById(dynamic);
	}

	@Override
	public void updateShareCount(Dynamic dynamicParam,Dynamic dynamicExist) {
//		Dynamic dynamic = new Dynamic();
//		dynamic.setId(dynaId);
//		
//		Dynamic dynamicExist = dynamicMapper.findMomentMessagetById(dynamic);
		Long shareCount = dynamicExist.getShareCount();
		shareCount = null!= shareCount?shareCount:0l;
		shareCount = shareCount+1;
		
		dynamicParam.setShareCount(shareCount);
		dynamicMapper.updateDynamicById(dynamicParam);
	}

	@Override
	public void updateCollectVisible(Boolean collectVisible, Dynamic dynamicExist) {
		dynamicExist.setCollectVisible(collectVisible);;
		dynamicMapper.updateDynamicById(dynamicExist);
	}

	
}
