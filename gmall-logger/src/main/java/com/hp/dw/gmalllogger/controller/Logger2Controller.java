package com.hp.dw.gmalllogger.controller;


import com.alibaba.fastjson.JSONObject;

import com.hp.dw.gmalllogger.constants.Constant;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
public class Logger2Controller {
    private final Logger log = LoggerFactory.getLogger(LoggerController.class);

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @RequestMapping("/applog")
    public String appLog(@RequestBody JSONObject jsonObject) {
        String jsonString = jsonObject.toJSONString();
        log.info(jsonString);
        if (jsonObject.getString("start") != null) {
            kafkaTemplate.send(Constant.GMALL_START, jsonString);
        } else {
            kafkaTemplate.send(Constant.GMALL_EVENT, jsonString);
        }
        return "success";
    }
}
