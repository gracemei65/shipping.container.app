package com.railinc.shipping.container.controller;

import com.railinc.shipping.container.config.ContainerStatusConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;

import javax.jms.Queue;

@RestController
@RequestMapping(value = "/shipping")
public class ContainerStatusJmsController {

    @Autowired
    private JmsTemplate jmsTemplate;

    @PostMapping("/container/message")
    public void readAndSend(@RequestBody String message) throws Exception {
        jmsTemplate.convertAndSend(ContainerStatusConstants.STATUS_INBOUND_QUEUE, message);
        System.out.println(" message sent Successfully !");
    }
}
