package com.nireus.redis.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.nireus.common.constant.RedisConstants;
import com.nireus.common.enums.BusinessTypeEnum;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * 
 * <p>
 * 自动生成业务编号
 * </p>
 * 
 * @author wushixiong
 * @since 2021-05-26 17:47:49
 */
@Component
public class BusinessNoGenerator {

	@Autowired
	private RedisTemplate redisTemplate;

	/**
	 * @param businessType 业务类型枚举
	 * @param digit        业务序号位数
	 * @return
	 */
	public String generate(BusinessTypeEnum businessType, Integer digit) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String date = LocalDateTime.now(ZoneOffset.of("+8")).format(formatter);
		String key = RedisConstants.BUSINESS_NO_PREFIX + businessType.getCode() + ":" + date;
		Long increment = redisTemplate.opsForValue().increment(key);
		return date + businessType.getValue() + String.format("%0" + digit + "d", increment);
	}

	public String generate(BusinessTypeEnum businessType) {
		Integer defaultDigit = 6;
		return generate(businessType, defaultDigit);
	}

}
