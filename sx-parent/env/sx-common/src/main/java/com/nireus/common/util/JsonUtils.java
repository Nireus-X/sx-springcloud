package com.nireus.common.util;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * 
 * <p>
 * json操作工具类
 * </p>
 * 
 * @author wushixiong
 * @since 2021-05-26 16:48:33
 */
public class JsonUtils {

	private final static ObjectMapper mapper;

	static {
		mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
	}

	/**
	 * json转对象
	 *
	 * @param jsonStr   json串
	 * @param classType 对象类型
	 * @return 对象
	 * @throws JsonProcessingException
	 * @throws JsonMappingException
	 */
	public static <T> T toEntity(String jsonStr, Class<T> classType)
			throws JsonMappingException, JsonProcessingException {

		if (StringUtils.isEmpty(jsonStr)) {
			return null;
		}
		return mapper.readValue(jsonStr, classType);
	}

	/**
	 * json转化为带泛型的对象
	 *
	 * @param jsonStr       json字符串
	 * @param typeReference 转化类型
	 * @return 对象
	 * @throws JsonProcessingException
	 * @throws JsonMappingException
	 */
	public static <T> T toEntity(String jsonStr, TypeReference<T> typeReference)
			throws JsonMappingException, JsonProcessingException {
		if (StringUtils.isBlank(jsonStr) || typeReference == null) {
			return null;
		}
		return (T) mapper.readValue(jsonStr, typeReference);
	}

	/**
	 * 对象转json
	 *
	 * @param obj 对象
	 * @return json串
	 * @throws JsonProcessingException
	 */
	public static String toJson(Object obj) throws JsonProcessingException {
		if (obj instanceof String) {
			return (String) obj;
		}
		return mapper.writeValueAsString(obj);
	}

	/**
	 * 对象转json(格式化的json)
	 *
	 * @param obj 对象
	 * @return 格式化的json串
	 * @throws JsonProcessingException
	 */
	public static String toJsonWithFormat(Object obj) throws JsonProcessingException {
		if (obj == null) {
			return null;
		}

		if (obj instanceof String) {
			return (String) obj;
		}
		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
	}
}
