package com.friends.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.friends.dao.DynamicCommentMapper;
import com.friends.dao.ReplyCommentMapper;
import com.friends.entity.DynamicComment;
import com.friends.service.ICommentService;

/**
 * @author Evan
 * @date 2019/4
 */
@Service("commentServiceImpl")
public class CommentServiceImpl implements ICommentService {
	
	@Autowired
    private DynamicCommentMapper dynamicCommentMapper;
	
	@Autowired
    private ReplyCommentMapper replyCommentMapper;
	

	@Override
	public void commentDynamic(DynamicComment dynamicComment) {
		Long now = System.currentTimeMillis();
		dynamicComment.setAddtime(now);
		dynamicComment.setDelFlag(0l);
		dynamicCommentMapper.insertComment(dynamicComment);
	}

	@Override
	public void replyComment(DynamicComment dynamicComment) {
		Long now = System.currentTimeMillis();
		dynamicComment.setAddtime(now);
		dynamicComment.setDelFlag(0l);
		replyCommentMapper.insert(dynamicComment);
	}

	@Override
	public void deleteComment(DynamicComment dynamicComment) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<DynamicComment> findDynamicCommentList(Long dynaId) {
		DynamicComment dynamicComment = new DynamicComment();
		dynamicComment.setDynaId(dynaId);
		List<DynamicComment> dynamicCommentList = dynamicCommentMapper.findDynamicCommentList(dynamicComment);
		List<DynamicComment> replyCommentList = replyCommentMapper.findReplyCommentList(dynamicComment);
		dynamicCommentList.addAll(replyCommentList);
		return dynamicCommentList;
	}

}
