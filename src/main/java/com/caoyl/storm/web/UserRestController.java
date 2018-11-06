package com.caoyl.storm.web;

import java.util.List;

import com.caoyl.storm.kafka.KafkaProducerUtil;
import com.caoyl.storm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.caoyl.storm.config.ApplicationConfiguration;
import com.caoyl.storm.pojo.User;

/**
 * Title: UserRestController
 * Description:
 * 用户数据操作接口
 * Version:1.0.0
 *
 * @author caoyl
 * @date 2018年1月9日
 */
@RestController
@RequestMapping(value = "/api")
public class UserRestController {

    @Autowired
    private UserService userService;

    @Autowired
    ApplicationConfiguration app;

    @GetMapping("/user")
    public List<User> findByUser(User user) {
        System.out.println("开始查询...");
        return userService.findByUser(user);
    }

    @PostMapping("/user")
    public boolean sendKafka(@RequestBody User user) {
        return KafkaProducerUtil.sendMessage(user.toString(), app.getServers(), app.getTopicName());
    }

    @PostMapping("/users")
    public boolean sendKafka(@RequestBody List<User> users) {
        return KafkaProducerUtil.sendMessage(users, app.getServers(), app.getTopicName());
    }
}