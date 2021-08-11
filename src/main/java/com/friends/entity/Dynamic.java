package com.friends.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.github.pagehelper.PageInfo;

import lombok.Data;

import java.util.List;

import javax.validation.constraints.NotNull;

/**
 * 	动态详情表
 *
 * @author Evan
 * @date 2019/4
 */
@Data
public class Dynamic{
	
	@TableId(value = "id", type = IdType.UUID)
    private long id;
	
	@NotNull(message = "用户不能为空")
    private Long userId;

    private String content;// 动态详情

    private Long areaCode; // 同城标识，用区号区分
    
    private Long addtime;
    
    private Long delTime;
    
    private Long likeCount;	// 点赞数量
    
    private Long weekLikeCount;	// 一周内点赞数量
    
    private Long commentCount;   //评论数量
    
    private Long enable;// 是否启用
    
    @NotNull(message = "动态类型不能为空")
//    @DecimalMin(value="1", message = "动态类型必须在1和3之间")
//    @DecimalMax(value="3", message = "动态类型必须在1和3之间")
    private Long type;// 动态类型，1 表示只有文字动态；2 表示带有图片动态；3 表示带有视频动态
    
    private String pictureList;
    
    private String videoList;
    
    private List<DynamicComment> dynamicCommentList;	// 评论信息
    
    private PageInfo<DynamicComment> dynamicCommentPage;	// 评论信息
    
    private List<DynamicLike> dynamicLikeList;	// 点赞信息
    
    private String userIds;
    
    @NotNull(message = "省份不能为空")
    private String province;// 省份
    
    @NotNull(message = "城市不能为空")
    private String city;	// 城市
    
    @NotNull(message = "区县不能为空")
    private String area;	// 区县
    
    private String detailAddr;	// 详细地址
    
    @NotNull(message = "地址不能为空")
    private String address;	// 地址
    
    private Long likeFlag = 0l;	// 登陆者是否点赞，默认为0 未点赞；1 已点赞
    
    private Long shareCount = 0l;	// 动态分享次数
    
    private DynamicLike currentUserLike;	// 当前登录用户点赞记录
    
    private Boolean collectVisible = false;	// 是否收藏
    
    private List<Long> dynaIdList;

}

