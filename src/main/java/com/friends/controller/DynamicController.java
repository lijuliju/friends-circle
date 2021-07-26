package com.friends.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSONObject;
import com.friends.entity.*;
import com.friends.result.Result;
import com.friends.result.ResultFactory;
import com.friends.service.*;
import com.github.pagehelper.PageInfo;

import java.util.List;

import javax.validation.Valid;

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
    public ILikeService likeServiceImpl;
    
    @Autowired
    public IUserInfoService userInfoServiceImpl;

    /**
     * 查询动态
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
    		return ResultFactory.buildFailResult("分页信息传递有误");
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
    	List<Dynamic> list = dynamicServiceImpl.getMomentMessageList(page,pageNumber,isHot,dynamic);
    	PageInfo<Dynamic> pageInfo = new PageInfo<Dynamic>(list);
        return ResultFactory.buildSuccessResult(pageInfo);
    }
    
    /**
     * 查询动态详情
     * @Transactional 添加事务
     * @param requestUser
     * @return
     */
    @RequestMapping(value = "/getDynamicDetail", method = {RequestMethod.POST})
    public Result getDynamicDetail(@RequestBody Dynamic dynamic) {
    	Dynamic dynamicDetail = dynamicServiceImpl.getMomentDetailById(dynamic);
    	Long dynaId = dynamicDetail.getId();
    	Long commentCount = dynamicDetail.getCommentCount();
    	Long likeCount = dynamicDetail.getLikeCount();
    	if(commentCount>0) {
    		List<DynamicComment> commentList = commentServiceImpl.findDynamicCommentList(dynaId);
    		dynamicDetail.setDynamicCommentList(commentList);
    	}
    	if(likeCount>0) {
    		List<DynamicLike> likeList = likeServiceImpl.findDynamicLikeList(dynaId);
    		dynamicDetail.setDynamicLikeList(likeList);
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
    public Result deleteMomentMessage(@RequestBody Dynamic dynamic) {
    	Long userId = dynamic.getUserId();
		if(null != userId) {
			Dynamic selfMoment = dynamicServiceImpl.getMomentDetailById(dynamic);
			if( null!= selfMoment) {
				Long selfUserId = selfMoment.getUserId();
				if(userId == selfUserId) {
					dynamicServiceImpl.deleteMomentsMessage(dynamic);
					userInfoServiceImpl.saveUserInfo(userId, "unpush");
					return ResultFactory.buildSuccessResult("删除成功！");
				}else {
					return ResultFactory.buildSuccessResult("待删除的动态不属于该用户！");
				}
			}else {
				return ResultFactory.buildFailResult("根据动态ID查询为空！");
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
//    @Transactional
    public Result commentDynamic(@RequestBody @Valid DynamicComment dynamicComment) {
    	Long dynaId = dynamicComment.getDynaId();
    	if(null != dynaId) {
    		// 保存评论信息
    		commentServiceImpl.commentDynamic(dynamicComment);
    		
    		// 更新动态评论个数
    		Dynamic dynamic = new Dynamic();
    		dynamic.setId(dynaId);
    		Dynamic existDynamic = dynamicServiceImpl.getMomentDetailById(dynamic);
    		Long commentCount = existDynamic.getCommentCount();
    		commentCount = commentCount + 1;
    		dynamicServiceImpl.updateCommentCount(commentCount, dynaId);
    		
    		return ResultFactory.buildSuccessResult("评论完成！");
    	}else {
    		return ResultFactory.buildFailResult("请传输动态唯一标识");
    	}
    	
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
    	if(null != comId) {
    		commentServiceImpl.replyComment(dynamicComment);
    		
    		Dynamic dynamic = new Dynamic();
    		Long dynaId = dynamicComment.getDynaId();
    		dynamic.setId(dynaId);
    		Dynamic existDynamic = dynamicServiceImpl.getMomentDetailById(dynamic);
    		Long commentCount = existDynamic.getCommentCount();
    		commentCount = commentCount + 1;
    		dynamicServiceImpl.updateCommentCount(commentCount, dynaId);
    		
    		return ResultFactory.buildSuccessResult("回复评论完成！");
    	}else {
    		return ResultFactory.buildFailResult("被评论信息唯一标识不能为空");
    	}
    	
    }
    
    /**
     * 删除评论
     * @param requestUser
     * @return
     */
    @RequestMapping(value = "/deleteComment", method = {RequestMethod.POST})
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
    	
    	// 保存点赞信息
    	likeServiceImpl.giveDynamicLike(dynamicLike);
    	
    	// 查询点赞对应详情
    	Dynamic dynamic = new Dynamic();
		Long dynaId = dynamicLike.getDynaId();
		dynamic.setId(dynaId);
		Dynamic existDynamic = dynamicServiceImpl.getMomentDetailById(dynamic);
		
		// 更新动态点赞数
		Long likeCount = existDynamic.getLikeCount();
		likeCount = likeCount + 1;
		
		Long weekLikeCount = existDynamic.getWeekLikeCount();
		weekLikeCount = weekLikeCount + 1;
		dynamicServiceImpl.updateLikeCount(likeCount,weekLikeCount, dynaId);
		
		userInfoServiceImpl.saveUserInfo(existDynamic.getUserId(), "like");
		
        return ResultFactory.buildSuccessResult("点赞完成！");
    }
    
    /**
     * 取消点赞动态
     * @param requestUser
     * @return
     */
    @RequestMapping(value = "/cancelFabulousDynamic", method = {RequestMethod.POST})
    public Result cancelFabulousDynamic(@RequestBody @Valid DynamicLike dynamicLike) {
    	DynamicLike dynamicDetail = likeServiceImpl.findDynamicLikeById(dynamicLike.getId());
    	if(null != dynamicDetail) {
    		Long accessUserId = dynamicLike.getUserId();
    		Long likeUserId = dynamicDetail.getUserId();
    		if(accessUserId == likeUserId) {
    			likeServiceImpl.cancelDynamicLike(dynamicLike);
    			
    			Dynamic dynamic = new Dynamic();
    			Long dynaId = dynamicLike.getDynaId();
    			dynamic.setId(dynaId);
    			Dynamic existDynamic = dynamicServiceImpl.getMomentDetailById(dynamic);
    			
    			Long likeCount = existDynamic.getLikeCount();
    			likeCount = likeCount - 1;
    			
    			Long weekLikeCount = existDynamic.getWeekLikeCount();
    			weekLikeCount = weekLikeCount - 1;
    			dynamicServiceImpl.updateLikeCount(likeCount,weekLikeCount,dynaId);
    			
    			userInfoServiceImpl.saveUserInfo(dynamic.getUserId(), "dislike");
    			return ResultFactory.buildSuccessResult("取消点赞成功！");
    			
    		}else {
    			return ResultFactory.buildFailResult("该点赞记录不属于本用户！不可进行取消操作！");
    		}
    		
    	}
		return null;
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
    		return ResultFactory.buildFailResult("用户ID有误");
    	}
    	UserInfo userInfo = userInfoServiceImpl.getUserDetailById(userId);
    	if(null == userInfo) {
    		return ResultFactory.buildSuccessResult("没有该用户信息！");
    	}
		return ResultFactory.buildSuccessResult(userInfo);
    }
    
    public static void main(String[] args) {
		String msg = "{\"address\":\"北京市北京市大兴区林肯公园A区\",\"area\":\"大兴区\",\"areaCode\":0,\"city\":\"北京市\",\"content\":\"发布一个描述\",\"detailAddr\":\"林肯公园A区\",\"pictureList\":[{\"filePixels\":{\"fileUrl\":\"http:\\/\\/images.xlttsports.com\\/android_img_20210726093822456\"}},{\"filePixels\":{\"fileUrl\":\"http:\\/\\/images.xlttsports.com\\/android_img_20210726093823235\"}},{\"filePixels\":{\"fileUrl\":\"http:\\/\\/images.xlttsports.com\\/android_img_20210726093823145\"}},{\"filePixels\":{\"fileUrl\":\"http:\\/\\/images.xlttsports.com\\/android_img_20210726093824023\"}},{\"filePixels\":{\"fileUrl\":\"http:\\/\\/images.xlttsports.com\\/android_img_20210726093824125\"}},{\"filePixels\":{\"fileUrl\":\"http:\\/\\/images.xlttsports.com\\/android_img_20210726093825012\"}},{\"filePixels\":{\"fileUrl\":\"http:\\/\\/images.xlttsports.com\\/android_img_20210726093825279\"}},{\"filePixels\":{\"fileUrl\":\"http:\\/\\/images.xlttsports.com\\/android_img_20210726093826347\"}},{\"filePixels\":{\"fileUrl\":\"http:\\/\\/images.xlttsports.com\\/android_img_20210726093826146\"}}],\"province\":\"北京市\",\"type\":2,\"userId\":555730791365171840}";
		JSONObject json = JSONObject.parseObject(msg);
		System.out.println(json);
	}

}
