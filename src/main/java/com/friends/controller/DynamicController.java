package com.friends.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

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
import com.friends.entity.UserInfo;
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
@RequestMapping("/dynamic")
public class DynamicController {
	
    @Autowired
    public IDynamicService dynamicServiceImpl;
    
    @Autowired
    public ICommentService commentServiceImpl;
    
    @Autowired
    public IReplyService replyServiceImpl;
    
    @Autowired
    public ICollectService collectServiceImpl;
    
    @Autowired
    public ILikeService likeServiceImpl;
    
    @Autowired
    public IUserInfoService userInfoServiceImpl;

    /**
     *  @de查询动态
     * @Transactional 添加事务
     * @param requestUser
     * @return
     */
    @RequestMapping(value = "/getDynamicList", method = {RequestMethod.POST})
//    public Result getMomentList(@RequestBody Dynamic momentsMessage) {
    public Result getMomentList(@RequestBody String dynamicStr) {
    	JSONObject dynamicJson = JSONObject.parseObject(dynamicStr);
    	int page = dynamicJson.getInteger("page");
    	int pageNumber = dynamicJson.getInteger("pageNumber");
    	if(page==0 || pageNumber==0) {
    		return ResultFactory.buildFailResult("分页信息传递有误！");
    	}
    	Long currentUserId = dynamicJson.getLong("currentUserId");
    	if(null == currentUserId) {
    		return ResultFactory.buildFailResult("登录用户信息为空！");
    	}
    	String userIds = dynamicJson.getString("userIds");
    	
    	// 热门潮汛标识
    	Object isHotObj = dynamicJson.get("isHot");
    	Boolean isHot = false;
    	if(null != isHotObj ) {
    		long isHotL = dynamicJson.getLong("isHot");
    		if(isHotL==1) {
    			isHot = true;
    		}
    	}
    	
    	Long type = dynamicJson.getLong("type");
    	String province = dynamicJson.getString("province");
    	String city = dynamicJson.getString("city");
    	String area = dynamicJson.getString("area");
    	Dynamic dynamic = new Dynamic();
    	dynamic.setUserIds(userIds);
    	dynamic.setType(type);
    	dynamic.setProvince(province);
    	dynamic.setCity(city);
    	dynamic.setArea(area);
    	dynamic.setUserId(currentUserId);
    	
    	// 是否查询收藏列表
    	Object collectVisibleObj = dynamicJson.get("collectVisible");
    	if(null!=collectVisibleObj) {
    		Boolean collectVisible = Boolean.valueOf(collectVisibleObj+"");
    		if(collectVisible) {
    			// 查找已收藏列表
    			List<Long> dynaIdList = collectServiceImpl.getDynaIdList(currentUserId);
    			if(dynaIdList.isEmpty()) {
    				List<Dynamic> dynamicList = new ArrayList<Dynamic>();
    				PageInfo<Dynamic> pageInfo = new PageInfo<Dynamic>(dynamicList);
    				return ResultFactory.buildSuccessResult(pageInfo);
    			}
    			dynamic.setDynaIdList(dynaIdList);
    		}
    	}
    	List<Dynamic> dynamicList = dynamicServiceImpl.getMomentMessageList(page,pageNumber,isHot,dynamic);
		if(!dynamicList.isEmpty()) {
			for (Dynamic allDynamic : dynamicList) {
				Long dynaId = allDynamic.getId();
				Long commentCount = allDynamic.getCommentCount();
		    	Long likeCount = allDynamic.getLikeCount();
//		    	if(commentCount>0) {
//		    		List<DynamicComment> commentList = commentServiceImpl.findDynamicCommentList(dynaId,currentUserId);
//		    		allDynamic.setDynamicCommentList(commentList);
//		    	}
		    	if(likeCount>0) {
		    		// 当前用户点赞信息
		    		List<DynamicLike> currentLikeList = likeServiceImpl.findDynamicLikeBy(currentUserId, dynaId);
		    		for (DynamicLike userDynamicLike : currentLikeList) {
//		    			Long userDynaId = userDynamicLike.getDynaId();
		    			Long commentId = userDynamicLike.getCommentId();
		    			Long replyId = userDynamicLike.getReplyId();
		    			Long likeUserId = userDynamicLike.getUserId();
		    			if(currentUserId.longValue() == likeUserId.longValue() && null==commentId && null==replyId) {
		    				allDynamic.setCurrentUserLike(userDynamicLike);
		    			}
					}
		    	}
			}
		}
    	PageInfo<Dynamic> pageInfo = new PageInfo<Dynamic>(dynamicList);
        return ResultFactory.buildSuccessResult(pageInfo);
    }
    
    /**
     *  查询动态详情
     * @Transactional 添加事务
     * @param requestUser
     * @return
     */
    @RequestMapping(value = "/getDynamicDetail", method = {RequestMethod.POST})
//    public Result getDynamicDetail(@RequestBody Dynamic dynamic) {
    public Result getDynamicDetail(@RequestBody String dynamicStr) {
        JSONObject dynamicJson = JSONObject.parseObject(dynamicStr);
    	Long currentUserId = dynamicJson.getLong("currentUserId");
    	if(null == currentUserId) {
    		return ResultFactory.buildFailResult("传输参数currentUserId["+currentUserId+"]有误！");
    	}
    	
    	Long id = dynamicJson.getLong("id");
    	Dynamic dynamic = new Dynamic();
    	dynamic.setId(id);
    	dynamic.setUserId(currentUserId);
    	Dynamic dynamicDetail = dynamicServiceImpl.getMomentDetailById(dynamic);
    	if(null == dynamicDetail) {
    		return ResultFactory.buildFailResult("根据参数id["+id+"]查询动态信息不存在！");
    	}
    	
    	Long dynaId = dynamicDetail.getId();
    	Long commentCount = dynamicDetail.getCommentCount();
    	Long likeCount = dynamicDetail.getLikeCount();
    	if(commentCount>0) {
    		List<DynamicComment> commentList = commentServiceImpl.findDynamicCommentPage(dynaId, currentUserId, 1, 10);
    		PageInfo<DynamicComment> commentPage = new PageInfo<DynamicComment>(commentList);
    		dynamicDetail.setDynamicCommentPage(commentPage);
//    		List<DynamicComment> commentList = commentServiceImpl.findDynamicCommentList(dynaId,currentUserId);
//    		dynamicDetail.setDynamicCommentList(commentList);
    	}
    	if(likeCount>0) {
    		// 当前用户点赞信息
    		List<DynamicLike> currentLikeList = likeServiceImpl.findDynamicLikeBy(currentUserId, dynaId);
    		for (DynamicLike userDynamicLike : currentLikeList) {
    			Long commentId = userDynamicLike.getCommentId();
    			Long replyId = userDynamicLike.getReplyId();
    			Long likeUserId = userDynamicLike.getUserId();
    			if(currentUserId.longValue() == likeUserId.longValue() && null==commentId && null==replyId) {
    				dynamicDetail.setCurrentUserLike(userDynamicLike);
    			}
//    			if(null!=userDynaId && null==commentId && null==replyId) {
//    				dynamicDetail.setCurrentUserLike(userDynamicLike);
//    			}
			}
    	}
    	// 判断该用户是否对该动态收藏
    	DynamicCollect collect = collectServiceImpl.getCollectBy(currentUserId, dynaId);
    	if(null != collect) {
    		dynamicDetail.setCollectVisible(true);
    	}
        return ResultFactory.buildSuccessResult(dynamicDetail);
    }
    
    
    /**
               *  发表动态
     * @param requestUser
     * @return
     */
    @RequestMapping(value = "/pushDynamic", method = {RequestMethod.POST})
    @Transactional
    public Result pushMomentMessage(@RequestBody @Valid Dynamic dynamic) {
//    public Result pushMomentMessage(@RequestBody String dynamicStr) {
//    	JSONObject dynamicJson = JSONObject.parseObject(dynamicStr);
//    	Dynamic dynamic = (Dynamic) JSONObject.parseArray(dynamicStr, Dynamic.class);
    	dynamicServiceImpl.publishMomentsMessage(dynamic);
    	userInfoServiceImpl.saveUserInfo(dynamic.getUserId(), "push");
        return ResultFactory.buildSuccessResult("发表成功！");
    }
    
    /**
     	* 删除动态
     * @Transactional 添加事务
     * @param requestUser
     * @return
     */
    @RequestMapping(value = "/deleteDynamic", method = {RequestMethod.POST})
    @Transactional
    public Result deleteMomentMessage(@RequestBody Dynamic dynamic) {
    	Long userId = dynamic.getUserId();
		if(null != userId) {
			Dynamic selfMoment = dynamicServiceImpl.getMomentDetailById(dynamic);
			if( null!= selfMoment) {
				Long selfUserId = selfMoment.getUserId();
				if(userId.longValue() == selfUserId.longValue()) {
					dynamicServiceImpl.deleteMomentsMessage(dynamic);
					userInfoServiceImpl.saveUserInfo(userId, "unpush");
					return ResultFactory.buildSuccessResult("删除成功！");
				}else {
					return ResultFactory.buildFailResult("待删除的动态不属于该用户！");
				}
			}else {
				return ResultFactory.buildFailResult("该动态ID为【"+dynamic.getId()+"】信息不存在！");
			}
		}else {
			return ResultFactory.buildFailResult("传输参数有误！用户ID为空！");
		}
    }
    
    /**
     * 评论动态
     * @param requestUser
     * @return
     */
    @RequestMapping(value = "/commentDynamic", method = {RequestMethod.POST})
    @Transactional
    public Result commentDynamic(@RequestBody @Valid DynamicComment dynamicComment) {
    	Long dynaId = dynamicComment.getDynaId();
    	if(null == dynaId) {
    		return ResultFactory.buildFailResult("参数dynaId["+dynaId+"]传输有误！");
    	}
    	// 判断该动态是否存在
    	Dynamic dynamic = new Dynamic();
    	dynamic.setId(dynaId);
    	Dynamic existDynamic = dynamicServiceImpl.getMomentDetailById(dynamic);
    	if(null == existDynamic) {
    		return ResultFactory.buildFailResult("根据参数dynaId["+dynaId+"]未查询到动态信息！");
    	}
    	
    	// 保存评论信息
    	commentServiceImpl.commentDynamic(dynamicComment);

    	// 更新动态评论个数
    	Long commentCount = existDynamic.getCommentCount();
    	commentCount = commentCount + 1;
    	dynamicServiceImpl.updateCommentCount(commentCount, dynaId);

    	return ResultFactory.buildSuccessResult("评论完成！");

    }
    
    /**
     	* 回复评论
     * @param requestUser
     * @return
     */
    @RequestMapping(value = "/replyDynamic", method = {RequestMethod.POST})
    @Transactional
    public Result replyDynamic(@RequestBody @Valid DynamicComment dynamicComment) {
    	Long comId = dynamicComment.getComId();
    	Long replyPid = dynamicComment.getReplyPid();
    	if(null != replyPid) {
    		// 校验回复父ID是否存在
    		DynamicComment replyExist = replyServiceImpl.findReplyCommentDetailById(replyPid);
    		if(null == replyExist) {
    			return ResultFactory.buildFailResult("根据传递参数replyPid["+replyPid+"]未查询到该评论！");
    		}
    	}
    	if(null == comId) {
    		return ResultFactory.buildFailResult("传递参数comId["+comId+"]错误！");
    	}
    	Long dynaId = dynamicComment.getDynaId();
    	Dynamic dynamic = new Dynamic();
    	dynamic.setId(dynaId);
    	Dynamic existDynamic = dynamicServiceImpl.getMomentDetailById(dynamic);
    	if(null == existDynamic) {
    		return ResultFactory.buildFailResult("根据传递的参数dynaId["+dynaId+"]未找到动态信息！");
    	}
    	
    	DynamicComment dynamicCommentExist = commentServiceImpl.findDynamicCommentDetailById(comId);
    	if(null == dynamicCommentExist) {
    		return ResultFactory.buildFailResult("根据传递的参数comId["+comId+"]未找到评论信息！");
    	}
    	// 新增回复评论记录
    	replyServiceImpl.replyComment(dynamicComment);
    	
    	if(null != dynamicCommentExist) {
    		// 更新动态的直接评论
    		Long replyCount = dynamicCommentExist.getReplyCount();
    		if(null == replyCount) {
    			replyCount = 0l;
    		}
    		replyCount = replyCount + 1;
    		dynamicCommentExist.setReplyCount(replyCount);
    		commentServiceImpl.updateReplyCount(dynamicCommentExist);
    	}

    	// 更动态新评论数量
    	Long commentCount = existDynamic.getCommentCount();
    	commentCount = commentCount + 1;
    	dynamicServiceImpl.updateCommentCount(commentCount, dynaId);
    	
    	return ResultFactory.buildSuccessResult("回复评论完成！");
    }
    
    /**
     	* 删除评论
     * @param requestUser
     * @return
     */
    @RequestMapping(value = "/deleteComment", method = {RequestMethod.POST})
    @Transactional
    public Result deleteComment(@RequestBody DynamicComment dynamicComment) {
    	
    	Long comId = dynamicComment.getComId();
    	if(null != comId) {
    		commentServiceImpl.deleteComment(dynamicComment);
    		
    		Dynamic dynamic = new Dynamic();
    		Long dynaId = dynamicComment.getDynaId();
    		dynamic.setId(dynaId);
    		Dynamic existDynamic = dynamicServiceImpl.getMomentDetailById(dynamic);
    		Long commentCount = existDynamic.getCommentCount();
    		commentCount = commentCount - 1;
    		dynamicServiceImpl.updateCommentCount(commentCount, dynaId);
    		
    		return ResultFactory.buildSuccessResult("回复评论完成！");
    	}else {
    		return ResultFactory.buildFailResult("被评论信息唯一标识不能为空");
    	}
    }
    
    /**
     	* 点赞动态
     * @param requestUser
     * @return
     */
    @RequestMapping(value = "/fabulousDynamic", method = {RequestMethod.POST})
    @Transactional
    public Result fabulousDynamic(@RequestBody @Valid DynamicLike dynamicLike) {
    	
    	// 判断是哪种点赞，并判断是否已点赞
    	Long dynaId = dynamicLike.getDynaId();
    	Long commId = dynamicLike.getCommentId();
    	Long replyId = dynamicLike.getReplyId();
    	String type="";
    	if(null ==commId && null == replyId) {
    		type="dynamic";
    	}else if(null!=commId && null==replyId) {
			// 评论的点赞
    		type="comment";
		}else if(null!=commId && null!=replyId) {
			// 评论的评论的点赞
			type="reply";
		}else if(null == commId && null != replyId) {
			return ResultFactory.buildFailResult("参数commentId["+commId+"]未传输！");
		}
		
    	// 动态的点赞
    	List<DynamicLike> likeList = likeServiceImpl.findDynamicLikeList(dynamicLike);
    	if(!likeList.isEmpty()) {
    		for (DynamicLike existLike : likeList) {
    			Long existCommentId = existLike.getCommentId();
    			Long existReplyId = existLike.getReplyId();
    			switch (type) {
				case "dynamic":
					if(null == existCommentId && null == existReplyId) {
	    				return ResultFactory.buildFailResult("该动态已被该用户点赞过，不可二次点赞！");
	    			}
					break;
				case "comment":
					if(null!=existCommentId && null==existReplyId) {
	    				// 评论的点赞
	    				return ResultFactory.buildFailResult("该评论已被该用户点赞过，不可二次点赞！");
	    			}
					break;
				case "reply":
					if(null!=existCommentId && null!=existReplyId) {
	    				// 评论的评论的点赞
	    				return ResultFactory.buildFailResult("该评论（回复）已被该用户点赞过，不可二次点赞！");
	    			}
					break;
				default:
					break;
				}
			}
    	}
    	// 查询点赞对应动态详情
    	Dynamic dynamic = new Dynamic();
    	dynamic.setId(dynaId);
    	Dynamic existDynamic = dynamicServiceImpl.getMomentDetailById(dynamic);
    	if(null == existDynamic ) {
    		return ResultFactory.buildFailResult("根据参数dynaId["+dynaId+"]未查询到动态信息！");
    	}
		
		// 判断点赞对象----点赞评论
    	switch (type) {
		case "comment":
			// 评论点赞
			DynamicComment dynamicCommentExist = commentServiceImpl.findDynamicCommentDetailById(commId);
			if(null == dynamicCommentExist ) {
				return ResultFactory.buildFailResult("根据参数commentId["+commId+"]未查询到评论信息！");
			}
			commentServiceImpl.updateCommentLikeCount(dynamicCommentExist,"add");
			break;
		case "reply":
			// 评论点赞
			DynamicComment dynamicCommentExist2 = commentServiceImpl.findDynamicCommentDetailById(commId);
			if(null == dynamicCommentExist2 ) {
				return ResultFactory.buildFailResult("根据参数commentId["+commId+"]未查询到评论信息！");
			}
			commentServiceImpl.updateCommentLikeCount(dynamicCommentExist2,"add");
						
			// 点赞回复评论
			DynamicComment replyCommentExist = replyServiceImpl.findReplyCommentDetailById(replyId);
			if(null == replyCommentExist ) {
	    		return ResultFactory.buildFailResult("根据参数replyId["+replyId+"]未查询到评论信息！");
	    	}
			// 评论的评论点赞
			replyServiceImpl.updateReplyCommentLikeCount(replyCommentExist,"add");
			break;
		default:
			break;
		}
		
		// insert保存点赞信息
		likeServiceImpl.giveDynamicLike(dynamicLike);
		
		// 更新动态点赞数
		Long likeCount = existDynamic.getLikeCount();
		likeCount = likeCount + 1;
		
		Long weekLikeCount = existDynamic.getWeekLikeCount();
		weekLikeCount = weekLikeCount + 1;
		dynamicServiceImpl.updateLikeCount(likeCount,weekLikeCount, dynaId);
		
		// 保存用户的点赞数
		userInfoServiceImpl.saveUserInfo(existDynamic.getUserId(), "like");
        return ResultFactory.buildSuccessResult("点赞完成！");
    }
    
    /**
     * 取消点赞动态
     * @param requestUser
     * @return
     */
    @RequestMapping(value = "/cancelFabulousDynamic", method = {RequestMethod.POST})
    @Transactional
    public Result cancelFabulousDynamic(@RequestBody @Valid DynamicLike dynamicLike) {
    	Dynamic dynamic = new Dynamic();
    	Long dynaId = dynamicLike.getDynaId();
    	dynamic.setId(dynaId);
    	Dynamic existDynamic = dynamicServiceImpl.getMomentDetailById(dynamic);
    	if(null == existDynamic) {
    		return ResultFactory.buildFailResult("根据参数dynaId["+dynaId+"]未查询动态信息！");
    	}
    	Long likeId = dynamicLike.getId();
    	DynamicLike dynamicDetail = likeServiceImpl.findDynamicLikeById(likeId);
    	if(null == dynamicDetail) {
    		return ResultFactory.buildFailResult("根据参数id["+likeId+"]未查询到点赞信息");
    	}

    	Long accessUserId = dynamicLike.getUserId();
    	Long likeUserId = dynamicDetail.getUserId();
    	if(accessUserId.longValue() != likeUserId.longValue()) {
    		return ResultFactory.buildFailResult("用户["+accessUserId+"]无取消点赞的权利，或因该点赞记录不属于本用户！");
    	}
    	
    	// 判断点赞对象
    	Long commentId = dynamicDetail.getCommentId();
    	Long replyId = dynamicDetail.getReplyId();
    	// 取消评论的点赞
    	if(null != commentId) {
    		DynamicComment dynamicComment = new DynamicComment();
    		dynamicComment.setId(commentId);
    		// 评论点赞
    		DynamicComment dynamicCommentExist = commentServiceImpl.findDynamicCommentDetailById(commentId);
    		if(null == dynamicCommentExist ) {
    			return ResultFactory.buildFailResult("根据参数commentId["+commentId+"]未查询到评论信息！");
    		}
    		commentServiceImpl.updateCommentLikeCount(dynamicCommentExist,"reduce");
    		
    		// 取消回复评论的点赞
    		if(null != replyId) {
    			DynamicComment replyCommentExist = replyServiceImpl.findReplyCommentDetailById(replyId);
        		if(null == replyCommentExist ) {
        			return ResultFactory.buildFailResult("根据参数replyId["+replyId+"]未查询到评论信息！");
        		}
        		// 评论的评论点赞
        		replyServiceImpl.updateReplyCommentLikeCount(replyCommentExist,"reduce");
    		}
    	}
//    	if(null != commentId && null == replyId) {
//    	}else if(null != commentId && null != replyId){
//    		DynamicComment dynamicComment = new DynamicComment();
//    		dynamicComment.setId(commentId);
//    		// 评论点赞
//    		DynamicComment dynamicCommentExist = commentServiceImpl.findDynamicCommentDetailById(commentId);
//    		if(null == dynamicCommentExist ) {
//    			return ResultFactory.buildFailResult("根据参数commentId["+commentId+"]未查询到评论信息！");
//    		}
//    		commentServiceImpl.updateCommentLikeCount(dynamicCommentExist,"reduce");
//    		
//    		DynamicComment replyCommentExist = replyServiceImpl.findReplyCommentDetailById(replyId);
//    		if(null == replyCommentExist ) {
//    			return ResultFactory.buildFailResult("根据参数replyId["+replyId+"]未查询到评论信息！");
//    		}
//    		// 评论的评论点赞
//    		replyServiceImpl.updateReplyCommentLikeCount(replyCommentExist,"reduce");
//    	}
    	
    	// 取消点赞【删除点赞记录】
    	likeServiceImpl.cancelDynamicLike(dynamicLike);
    	
    	Long likeCount = existDynamic.getLikeCount();
    	likeCount = likeCount - 1;

    	Long weekLikeCount = existDynamic.getWeekLikeCount();
    	weekLikeCount = weekLikeCount - 1;
    	dynamicServiceImpl.updateLikeCount(likeCount,weekLikeCount,dynaId);

    	// 保存用户点赞个数
    	userInfoServiceImpl.saveUserInfo(existDynamic.getUserId(), "dislike");
    	
    	return ResultFactory.buildSuccessResult("取消点赞成功！");

    }
    
    /**
     * 获取用户详情
     * @param requestUser
     * @return
     */
    @RequestMapping(value = "/getUserDetail", method = {RequestMethod.POST})
    public Result getUserDetail(@RequestBody Dynamic dynamic) {
    	Long userId = dynamic.getUserId();
    	if(null == userId) {
    		return ResultFactory.buildFailResult("参数suerId["+userId+"]有误！");
    	}
    	UserInfo userInfo = userInfoServiceImpl.getUserDetailById(userId);
//    	if(null == userInfo) {
//    		return ResultFactory.buildFailResult("没有该用户信息！");
//    	}
		return ResultFactory.buildSuccessResult(userInfo);
    }
    
    /**
     	* 分享动态
     * @param requestUser
     * @return
     */
    @RequestMapping(value = "/shareDynamic", method = {RequestMethod.POST})
    public Result shareDynamic(@RequestBody String reqStr) {
    	JSONObject reqJson = JSONObject.parseObject(reqStr);
    	Object dynaIdObj = reqJson.get("dynaId");
    	if(null == dynaIdObj) {
    		return ResultFactory.buildFailResult("参数dynaId["+dynaIdObj+"]传输有误！");
    	}
    	Long dynaId = Long.valueOf(dynaIdObj+"");
    	
    	Dynamic dynamicParam = new Dynamic();
    	dynamicParam.setId(dynaId);
		
		Dynamic dynamicExist = dynamicServiceImpl.getMomentDetailById(dynamicParam);
		if(null == dynamicExist) {
    		return ResultFactory.buildFailResult("根据参数dynaId["+dynaIdObj+"]未查询到动态信息！");
    	}
    	dynamicServiceImpl.updateShareCount(dynamicParam, dynamicExist);
		return ResultFactory.buildSuccessResult("分享成功！");
    }
    
    public static void main(String[] args) {
		String msg = "{\"address\":\"北京市北京市大兴区林肯公园A区\",\"area\":\"大兴区\",\"areaCode\":0,\"city\":\"北京市\",\"content\":\"发布一个描述\",\"detailAddr\":\"林肯公园A区\",\"pictureList\":[{\"filePixels\":{\"fileUrl\":\"http:\\/\\/images.xlttsports.com\\/android_img_20210726093822456\"}},{\"filePixels\":{\"fileUrl\":\"http:\\/\\/images.xlttsports.com\\/android_img_20210726093823235\"}},{\"filePixels\":{\"fileUrl\":\"http:\\/\\/images.xlttsports.com\\/android_img_20210726093823145\"}},{\"filePixels\":{\"fileUrl\":\"http:\\/\\/images.xlttsports.com\\/android_img_20210726093824023\"}},{\"filePixels\":{\"fileUrl\":\"http:\\/\\/images.xlttsports.com\\/android_img_20210726093824125\"}},{\"filePixels\":{\"fileUrl\":\"http:\\/\\/images.xlttsports.com\\/android_img_20210726093825012\"}},{\"filePixels\":{\"fileUrl\":\"http:\\/\\/images.xlttsports.com\\/android_img_20210726093825279\"}},{\"filePixels\":{\"fileUrl\":\"http:\\/\\/images.xlttsports.com\\/android_img_20210726093826347\"}},{\"filePixels\":{\"fileUrl\":\"http:\\/\\/images.xlttsports.com\\/android_img_20210726093826146\"}}],\"province\":\"北京市\",\"type\":2,\"userId\":555730791365171840}";
		JSONObject json = JSONObject.parseObject(msg);
		System.out.println(json);
	}

}
