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
import com.github.pagehelper.PageHelper;

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
	
	@Autowired
    private DynamicLikeMapper dynamicLikeMapper;
	

	@Override
	public void commentDynamic(DynamicComment dynamicComment) {
		Long now = System.currentTimeMillis();
		dynamicComment.setAddtime(now);
		dynamicComment.setDelFlag(0l);
		dynamicCommentMapper.insertComment(dynamicComment);
	}

	@Override
	public void deleteComment(DynamicComment dynamicComment) {
		Long now = System.currentTimeMillis();
		dynamicComment.setDelTime(now);
		dynamicComment.setDelFlag(1l);
		dynamicCommentMapper.updateDynamicCommentById(dynamicComment);
	}

	@Override
	public List<DynamicComment> findDynamicCommentList(Long dynaId,Long currentUserId) {
		DynamicComment dynamicComment = new DynamicComment();
		dynamicComment.setDynaId(dynaId);
		dynamicComment.setUserId(currentUserId);
		List<DynamicComment> dynamicCommentList = dynamicCommentMapper.findDynamicCommentList(dynamicComment);
		makeDynamicComment(dynaId, currentUserId, dynamicComment, dynamicCommentList);
		return dynamicCommentList;
	}

	@Override
	public DynamicComment findDynamicCommentDetailById(Long commentId) {
		DynamicComment dynamicComment = new DynamicComment();
		dynamicComment.setId(commentId);
		return dynamicCommentMapper.findDynamicCommentById(dynamicComment);
	}

	@Override
	public void updateCommentLikeCount(DynamicComment dynamicCommentExist,String type) {
//		DynamicComment dynamicComment = new DynamicComment();
//		dynamicComment.setId(commentId);
//		// 评论点赞
//		DynamicComment dynamicCommentExist = dynamicCommentMapper.findDynamicCommentById(dynamicComment);
				
		Long commentLikeCount = dynamicCommentExist.getLikeCount();
		commentLikeCount = null==commentLikeCount?0:commentLikeCount;
		if("add".equals(type)) {
			commentLikeCount = commentLikeCount+1;
		}else {
			commentLikeCount = commentLikeCount-1;
		}
		dynamicCommentExist.setLikeCount(commentLikeCount);
		dynamicCommentMapper.updateDynamicCommentById(dynamicCommentExist);
	}

	@Override
	public void updateReplyCount(DynamicComment dynamicCommentExist) {
		dynamicCommentMapper.updateDynamicCommentById(dynamicCommentExist);
		
	}

	@Override
	public List<DynamicComment> findDynamicCommentPage(Long dynaId, Long currentUserId, int page, int pageNumber) {
		DynamicComment dynamicComment = new DynamicComment();
		dynamicComment.setDynaId(dynaId);
		dynamicComment.setUserId(currentUserId);
		PageHelper.startPage(page, pageNumber);
		List<DynamicComment> dynamicCommentList = dynamicCommentMapper.findDynamicCommentList(dynamicComment);
		makeDynamicComment(dynaId, currentUserId, dynamicComment, dynamicCommentList);
		return dynamicCommentList;
	}

	// 组织评论评论信息
	public List<DynamicComment> makeDynamicComment(Long dynaId,Long currentUserId,DynamicComment dynamicCommentParam,List<DynamicComment> dynamicCommentList) {
		// 组织评论的点赞信息
		if(dynamicCommentList.isEmpty()) {
			return dynamicCommentList;
		}
		for (DynamicComment userDynamicComment : dynamicCommentList) {
			Long commentId = userDynamicComment.getId();
			Long commentDynaId = userDynamicComment.getDynaId();

			DynamicLike dynamicLike = new DynamicLike();
			dynamicLike.setCommentId(commentId);
			dynamicLike.setDynaId(dynaId);

			// 查询该评论当前用户的点赞信息
			dynamicLike.setUserId(currentUserId);
			List<DynamicLike> userCommentLikelist = dynamicLikeMapper.findDynamicLikeList(dynamicLike);
			// 该用户无点赞记录时，继续下一个循环
			if(userCommentLikelist.isEmpty()) {
				continue;
			}
			userDynamicLikeFor:for (DynamicLike userDynamicLike : userCommentLikelist) {
				Long userDynaId = userDynamicLike.getDynaId();
				Long userCommentId = userDynamicLike.getCommentId();
				Long replyId = userDynamicLike.getReplyId();
				// 该用户对动态评论的点赞（动态的第一个评论）
				if(commentDynaId.longValue()==userDynaId.longValue() && userCommentId.longValue()==commentId.longValue() && null==replyId) {
					userDynamicComment.setCurrentUserLike(userDynamicLike);
					userDynamicComment.setLikeFlag(1l);
					break userDynamicLikeFor;
				}
			}

			// 查询评论回复个数
			dynamicCommentParam.setComId(commentId);
			List<DynamicComment> replyCommentList = replyCommentMapper.findReplyCommentList(dynamicCommentParam);
			long replyCount = replyCommentList.isEmpty()?0:replyCommentList.size();
			userDynamicComment.setReplyCount(replyCount);
		}
		return dynamicCommentList;
	}

}
