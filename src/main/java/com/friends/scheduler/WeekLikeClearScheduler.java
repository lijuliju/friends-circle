package com.friends.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.friends.dao.DynamicMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class WeekLikeClearScheduler {
	
	@Autowired
    private DynamicMapper dynamicMapper;
	
	//每天3：05执行
    @Scheduled(cron = "${friends.scheduler.cron}")
    public void testTasks() {
    	int clearCount = dynamicMapper.clearWeekLikeCount();
    	if(clearCount>0) {
    		log.info("============清空一周内点赞数完成=============");
    	}
    }
}
