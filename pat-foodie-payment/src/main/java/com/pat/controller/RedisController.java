package com.pat.controller;

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

	@GetMapping("/set")
	@ResponseBody
	public Object set(String key, String value) {
		redisTemplate.opsForValue().set(key, value);
		return "OK";
	}

	@GetMapping("/get")
	@ResponseBody
	public String get(String key) {
		return (String) redisTemplate.opsForValue().get(key);
	}

	@GetMapping("/delete")
	@ResponseBody
	public Object delete(String key) {
		redisTemplate.delete(key);
		return "OK";
	}
	
}
