package com.splitwise.Activity_Service;

import com.splitwise.Activity_Service.service.ActivityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ActivityServiceApplicationTests {
	@Autowired
	ActivityService activityService;

	@Test
	void contextLoads() {
	}
	@Test
	void test1()
	{
		activityService.getUserNameMap(1L);
	}
}
