package com.friends.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.friends.entity.Dynamic;
import com.friends.entity.DynamicCollect;
import com.friends.entity.DynamicComment;
import com.friends.entity.DynamicLike;
import com.friends.result.Result;
import com.friends.result.ResultFactory;
import com.friends.service.ICollectService;
import com.friends.service.ICommentService;
import com.friends.service.IDynamicService;
import com.friends.service.ILikeService;
import com.friends.service.IReplyService;
import com.friends.service.IUserInfoService;
import com.github.pagehelper.PageInfo;

/**
 * User controller.
 *
 * @author Evan
 * @date 2019/11
 */

@RestController
@RequestMapping("/reply")
public class ReplyController {
	
    @Autowired
    public IDynamicService dynamicServiceImpl;
    
    @Autowired
    public ICollectService collectServiceImpl;
    
    @Autowired
    public ICommentService commentServiceImpl;
    
    @Autowired
    public IReplyService replyServiceImpl;
    
    @Autowired
    public ILikeService likeServiceImpl;
    
    @Autowired
    public IUserInfoService userInfoServiceImpl;
    
    /**
	     *  收藏动态
	* @param requestUser
	* @return
	*/
	@RequestMapping(value = "/collectDynamic", method = {RequestMethod.POST})
	@Transactional
	public Result collectDynamic(@RequestBody String reqStr) {
		JSONObject dynamicJson = JSONObject.parseObject(reqStr);
		Long dynaId = dynamicJson.getLong("dynaId");
		if(null == dynaId) {
			return ResultFactory.buildFailResult("参数dynaId["+dynaId+"]有误！");
		}
		Long currentUserId = dynamicJson.getLong("currentUserId");
		if(null == currentUserId) {
			return ResultFactory.buildFailResult("登录用户信息为空！");
		}
		Dynamic dynamic = new Dynamic();
		dynamic.setId(dynaId);
		Dynamic dynamicExist = dynamicServiceImpl.getMomentDetailById(dynamic);
		if(null == dynamicExist) {
			return ResultFactory.buildFailResult("根据参数dynaId["+dynaId+"]未查询到动态信息！");
		}
		DynamicCollect dynamicCollect = collectServiceImpl.getCollectBy(currentUserId, dynaId);
		if(null != dynamicCollect) {
			return ResultFactory.buildFailResult("根据参数dynaId["+dynaId+"]和currentUserId["+currentUserId+"]查询到该动态已被该用户收藏！");
		}
		collectServiceImpl.saveCollect(currentUserId, dynaId);
		return ResultFactory.buildSuccessResult("收藏成功！");
	}
	
	/**
	     *  取消收藏动态
	* @param requestUser
	* @return
	*/
	@RequestMapping(value = "/cancelCollectDynamic", method = {RequestMethod.POST})
	@Transactional
	public Result cancelCollectDynamic(@RequestBody String reqStr) {
		JSONObject dynamicJson = JSONObject.parseObject(reqStr);
		Long dynaId = dynamicJson.getLong("dynaId");
		if(null == dynaId) {
			return ResultFactory.buildFailResult("参数dynaId["+dynaId+"]有误！");
		}
		Long currentUserId = dynamicJson.getLong("currentUserId");
		if(null == currentUserId) {
			return ResultFactory.buildFailResult("登录用户信息为空！");
		}
		Dynamic dynamic = new Dynamic();
		dynamic.setId(dynaId);
		Dynamic dynamicExist = dynamicServiceImpl.getMomentDetailById(dynamic);
		if(null == dynamicExist) {
			return ResultFactory.buildFailResult("根据参数dynaId["+dynaId+"]未查询到动态信息！");
		}
		DynamicCollect dynamicCollect = collectServiceImpl.getCollectBy(currentUserId, dynaId);
		if(null == dynamicCollect) {
			return ResultFactory.buildFailResult("根据参数dynaId["+dynaId+"]和currentUserId["+currentUserId+"]查询出该动态没有被该用户收藏！");
		}
		collectServiceImpl.cancelCollect(dynamicCollect);
		return ResultFactory.buildSuccessResult("取消收藏成功！");
	}
	
	/**
	     *  查看评论详情
	* @param requestUser
	* @return
	*/
	@RequestMapping(value = "/getReplyList", method = {RequestMethod.POST})
	@Transactional
	public Result getReplyList(@RequestBody String reqStr) {
		JSONObject dynamicJson = JSONObject.parseObject(reqStr);
		Long dynaId = dynamicJson.getLong("dynaId");
		if(null == dynaId) {
			return ResultFactory.buildFailResult("参数dynaId["+dynaId+"]有误！");
		}
		Long commentId = dynamicJson.getLong("commentId");
		if(null == commentId) {
			return ResultFactory.buildFailResult("参数commentId["+commentId+"]有误！");
		}
		Long currentUserId = dynamicJson.getLong("currentUserId");
		if(null == currentUserId) {
			return ResultFactory.buildFailResult("登录用户信息为空！");
		}
		Dynamic dynamic = new Dynamic();
		dynamic.setId(dynaId);
		Dynamic dynamicExist = dynamicServiceImpl.getMomentDetailById(dynamic);
		if(null == dynamicExist) {
			return ResultFactory.buildFailResult("根据参数dynaId["+dynaId+"]未查询到动态信息！");
		}
//		List<DynamicComment> list = commentServiceImpl.findDynamicCommentList(dynaId, currentUserId);
		DynamicComment dynamicComment = commentServiceImpl.findDynamicCommentDetailById(commentId);
		// 查找回复评论详情
//		List<DynamicComment> replyList = replyServiceImpl.getReplyList(commentId);
		List<DynamicComment> replyList = replyServiceImpl.getReplyPage(commentId,1,10);
		if(!replyList.isEmpty()) {
			for (DynamicComment reply : replyList) {
				Long replyId = reply.getId();
				Long replyPid = reply.getReplyPid();
				Long likeCount = reply.getLikeCount();
				if(null != replyPid) {
					DynamicComment replyParent = replyServiceImpl.findReplyCommentDetailById(replyPid);
					reply.setHasParent(true);
					reply.setReplyParent(replyParent);
				}
				
				// 查找品论的回复是否存在点赞记录
				if(likeCount>0) {
					DynamicLike dynamicLike = new DynamicLike();
					dynamicLike.setReplyId(replyId);
					dynamicLike.setUserId(currentUserId);
					List<DynamicLike> likeList = likeServiceImpl.findDynamicLikeList(dynamicLike);
					if(!likeList.isEmpty()) {
						for (DynamicLike replyLike : likeList) {
							replyLike.getUserId();
							reply.setLikeFlag(1l);
							reply.setCurrentUserLike(replyLike);
						}
					}
				}
			}
		}
		PageInfo<DynamicComment> replyPage = new PageInfo<DynamicComment>(replyList);
		dynamicComment.setReplyPage(replyPage);
//		dynamicComment.setReplyList(replyList);
		
		// 查找评论的点赞详情
		Long likeCount = dynamicExist.getLikeCount();
		if(likeCount>0) {
    		// 当前用户点赞信息
			DynamicLike commentLike = new DynamicLike();
			commentLike.setUserId(currentUserId);
			commentLike.setCommentId(commentId);
    		List<DynamicLike> currentLikeList = likeServiceImpl.findDynamicLikeList(commentLike);
    		for (DynamicLike userDynamicLike : currentLikeList) {
    			Long likeCommentId = userDynamicLike.getCommentId();
    			Long replyId = userDynamicLike.getReplyId();
    			Long likeUserId = userDynamicLike.getUserId();
    			boolean userFlag = currentUserId.longValue() == likeUserId.longValue();
    			boolean commentIdFlag = commentId.longValue()==likeCommentId.longValue();
    			if(userFlag && commentIdFlag && null==replyId) {
    				dynamicComment.setCurrentUserLike(userDynamicLike);
    				dynamicComment.setLikeFlag(1l);
    			}
			}
		}
		return ResultFactory.buildSuccessResult(dynamicComment);
	}
	
	@RequestMapping(value = "/getReplyPage", method = {RequestMethod.POST})
	@Transactional
	public Result getReplyPage(@RequestBody String reqStr) {
		JSONObject dynamicJson = JSONObject.parseObject(reqStr);
		Object pageObj = dynamicJson.get("page");
		Object pageNumberObj = dynamicJson.get("pageNumber");
    	if(null == pageObj|| null == pageNumberObj) {
    		return ResultFactory.buildFailResult("分页信息传递有误！");
    	}
    	int page = dynamicJson.getInteger("page");
    	int pageNumber = dynamicJson.getInteger("pageNumber");
		Long commentId = dynamicJson.getLong("commentId");
		if(null == commentId) {
			return ResultFactory.buildFailResult("参数commentId["+commentId+"]有误！");
		}
		Long currentUserId = dynamicJson.getLong("currentUserId");
		if(null == currentUserId) {
			return ResultFactory.buildFailResult("登录用户信息为空！");
		}
		// 查找回复评论详情
		List<DynamicComment> replyList = replyServiceImpl.getReplyPage(commentId,page,pageNumber);
		if(!replyList.isEmpty()) {
			for (DynamicComment reply : replyList) {
				Long replyId = reply.getId();
				Long replyPid = reply.getReplyPid();
				Long likeCount = reply.getLikeCount();
				if(null != replyPid) {
					DynamicComment replyParent = replyServiceImpl.findReplyCommentDetailById(replyPid);
					reply.setHasParent(true);
					reply.setReplyParent(replyParent);
				}
				
				// 查找品论的回复是否存在点赞记录
				if(likeCount>0) {
					DynamicLike dynamicLike = new DynamicLike();
					dynamicLike.setReplyId(replyId);
					dynamicLike.setUserId(currentUserId);
					List<DynamicLike> likeList = likeServiceImpl.findDynamicLikeList(dynamicLike);
					if(!likeList.isEmpty()) {
						for (DynamicLike replyLike : likeList) {
							replyLike.getUserId();
							reply.setLikeFlag(1l);
							reply.setCurrentUserLike(replyLike);
						}
					}
				}
			}
		}
		PageInfo<DynamicComment> pageInfo = new PageInfo<DynamicComment>(replyList);
		return ResultFactory.buildSuccessResult(pageInfo);
	}
	
	/**
     *  查询动态详情
     * @Transactional 添加事务
     * @param requestUser
     * @return
     */
    @RequestMapping(value = "/getCommentPage", method = {RequestMethod.POST})
//    public Result getDynamicDetail(@RequestBody Dynamic dynamic) {
    public Result getDynamicDetail(@RequestBody String dynamicStr) {
        JSONObject dynamicJson = JSONObject.parseObject(dynamicStr);
        Object pageObj = dynamicJson.get("page");
		Object pageNumberObj = dynamicJson.get("pageNumber");
    	if(null == pageObj|| null == pageNumberObj) {
    		return ResultFactory.buildFailResult("分页信息传递有误！");
    	}
        int page = dynamicJson.getInteger("page");
    	int pageNumber = dynamicJson.getInteger("pageNumber");
    	Long currentUserId = dynamicJson.getLong("currentUserId");
    	if(null == currentUserId) {
    		return ResultFactory.buildFailResult("传输参数currentUserId["+currentUserId+"]有误！");
    	}
    	
    	Object dynaIdObj = dynamicJson.get("dynaId");
    	if(null == dynaIdObj) {
    		return ResultFactory.buildFailResult("传输参数dynaId["+dynaIdObj+"]有误！");
    	}
    	
    	Long dynaId = dynamicJson.getLong("dynaId");
    	Dynamic dynamic = new Dynamic();
    	dynamic.setId(dynaId);
    	dynamic.setUserId(currentUserId);
    	Dynamic dynamicDetail = dynamicServiceImpl.getMomentDetailById(dynamic);
    	if(null == dynamicDetail) {
    		return ResultFactory.buildFailResult("根据参数dynaId["+dynaId+"]查询动态信息不存在！");
    	}
    	
    	List<DynamicComment> commentList = commentServiceImpl.findDynamicCommentPage(dynaId, currentUserId, page, pageNumber);
    	PageInfo<DynamicComment> commentPage = new PageInfo<DynamicComment>(commentList);
        return ResultFactory.buildSuccessResult(commentPage);
    }


}
