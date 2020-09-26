package com.pat.controller;

import com.pat.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@ApiIgnore
@RequestMapping("redis")
public class RedisController {

	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private RedisOperator redisOperator;

	@GetMapping("/set")
	@ResponseBody
	public Object set(String key, String value) {
		redisOperator.set(key, value);
		return "OK";
	}

	@GetMapping("/get")
	@ResponseBody
	public String get(String key) {
		return (String) redisOperator.get(key);
	}

	@GetMapping("/delete")
	@ResponseBody
	public Object delete(String key) {
		redisOperator.del(key);
		return "OK";
	}
	
}
