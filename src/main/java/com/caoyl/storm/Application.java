package com.caoyl.storm;

import com.caoyl.storm.storm.TopologyApp;
import com.caoyl.storm.storm.topology.ESInsertTopology;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.caoyl.storm.util.GetSpringBean;

/**
 * Title: Application
 * Description:
 * springBoot 主程序
 * Version:1.0.0
 *
 * @author caoyl
 * @date 2018年1月5日
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {

        // 启动嵌入式的 Tomcat 并初始化 Spring 环境及其各 Spring 组件
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        GetSpringBean springBean = new GetSpringBean();
        springBean.setApplicationContext(context);
//        TopologyApp app = context.getBean(TopologyApp.class);
        ESInsertTopology app = context.getBean(ESInsertTopology.class);
        app.runStorm(args);
    }

}
