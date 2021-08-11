package com.friends.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.friends.dao.DynamicCommentMapper;
import com.friends.dao.DynamicLikeMapper;
import com.friends.dao.ReplyCommentMapper;
import com.friends.entity.DynamicComment;
import com.friends.entity.DynamicLike;
import com.friends.service.ICommentService;
import com.friends.service.IReplyService;
import com.github.pagehelper.PageHelper;

/**
 * @author Evan
 * @date 2019/4
 */
@Service("replyServiceImpl")
public class ReplyServiceImpl implements IReplyService {
	
	@Autowired
    private ReplyCommentMapper replyCommentMapper;
	
	@Override
	public void replyComment(DynamicComment dynamicComment) {
		Long now = System.currentTimeMillis();
		dynamicComment.setAddtime(now);
		dynamicComment.setDelFlag(0l);
		replyCommentMapper.insert(dynamicComment);
	}

	@Override
	public void updateReplyCommentLikeCount(DynamicComment dynamicCommentExist,String type) {
		Long commentLikeCount = dynamicCommentExist.getLikeCount();
		commentLikeCount = null==commentLikeCount?0:commentLikeCount;
		if("add".equals(type)) {
			commentLikeCount = commentLikeCount+1;
		}else {
			commentLikeCount = commentLikeCount-1;
		}
		dynamicCommentExist.setLikeCount(commentLikeCount);
		
		replyCommentMapper.updateLikeCountById(dynamicCommentExist);
	}

	@Override
	public DynamicComment findReplyCommentDetailById(Long replyId) {
		DynamicComment dynamicComment = new DynamicComment();
		dynamicComment.setId(replyId);
		return replyCommentMapper.findReplyCommentById(dynamicComment);
	}

	@Override
	public List<DynamicComment> getReplyList(Long commentId) {
		DynamicComment dynamicComment = new DynamicComment();
		dynamicComment.setComId(commentId);
		return replyCommentMapper.findReplyCommentList(dynamicComment);
	}

	@Override
	public List<DynamicComment> getReplyPage(Long commentId, int page, int pageNumber) {
		DynamicComment dynamicComment = new DynamicComment();
		dynamicComment.setComId(commentId);
		PageHelper.startPage(page, pageNumber);
		return replyCommentMapper.findReplyCommentList(dynamicComment);
	}

}
