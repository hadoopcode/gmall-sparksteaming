package com.hp.dw.gmalllogger.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.hp.dw.gmalllogger.constants.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoggerController {
    @PostMapping("/log")
    public String logger(@RequestParam("log") String log) {
        String logWithTs = addTs(log);
        saveToFile(logWithTs);
        sendToKafka(logWithTs);
        return "ok";
    }

    private final Logger logger = LoggerFactory.getLogger(LoggerController.class);

    @Autowired
    private KafkaTemplate kafkaTemplate;

    private void sendToKafka(String log) {
        String topic = Constant.TOPIC_STARTUP;
        if (log.contains("event")) {
            topic = Constant.TOPIC_EVENT;
        }
        kafkaTemplate.send(topic, log);
    }

    private void saveToFile(String log) {
        logger.info(log);
    }

    private String addTs(String log) {
        JSONObject jsonObject = JSON.parseObject(log);
        jsonObject.put("ts", System.currentTimeMillis());
        return jsonObject.toJSONString();
    }
}
