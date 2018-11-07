package com.caoyl.storm.storm.topology;

import com.caoyl.storm.config.ApplicationConfiguration;
import com.caoyl.storm.constant.Constants;
import com.caoyl.storm.storm.TopologyApp;
import com.caoyl.storm.storm.bolt.InsertBolt;
import com.caoyl.storm.storm.spout.KafkaInsertDataSpout;
import com.caoyl.storm.util.GetSpringBean;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.topology.TopologyBuilder;
import org.elasticsearch.storm.EsBolt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author caoyl
 * @Title: TopologyApp
 * @Description: TopologyApp
 * @Version:1.0.0
 * @date 2018年5月8日
 */
@Component
public class ESInsertTopology {

    private final Logger logger = LoggerFactory.getLogger(ESInsertTopology.class);

    private ApplicationConfiguration app;

    public void runStorm(String[] args) {

        app = GetSpringBean.getBean(ApplicationConfiguration.class);

        // 定义一个拓扑
        TopologyBuilder builder = new TopologyBuilder();

        // 设置1个Executeor(线程)，默认一个
        builder.setSpout(Constants.KAFKA_SPOUT, new KafkaInsertDataSpout(), 1);

        // 组装连接 elasticsearch 的 bolt
        Map<Object, Object> boltConf = new HashMap<Object, Object>(16);
        boltConf.put("es.input.json", "false");
        EsBolt esBolt = new EsBolt(app.getEsIndexType(), boltConf);

        // shuffleGrouping:表示是随机分组
        // 设置1个Executeor(线程)，和两个task
        builder.setBolt(Constants.ES_INSERT_BOLT, esBolt, 1).setNumTasks(1).shuffleGrouping(Constants.KAFKA_SPOUT);
        Config conf = new Config();

        conf.setDebug(true);
        conf.put("es.nodes", "172.16.101.111");
        conf.put("es.port", 9201);
        conf.put("es.index.auto.create", "true");
        conf.put("es.storm.bolt.write.ack", "true");
        conf.put("es.storm.bolt.flush.entries.size", 1);

        //设置一个应答者
        conf.setNumAckers(1);

        //设置一个worker
        conf.setNumWorkers(1);
        TopologyApp.submitTopology(args, builder, conf, logger);
    }
}
