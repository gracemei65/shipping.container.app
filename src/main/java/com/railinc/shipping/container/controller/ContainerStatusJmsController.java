package com.railinc.shipping.container.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;

import javax.jms.Queue;

@RestController
@RequestMapping(value = "/shipping")
public class ContainerStatusJmsController {
    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private Queue queue;

    @PostMapping("/container/send")
    public void readAndSend(@RequestBody String message) throws Exception {
        jmsTemplate.convertAndSend(queue, message);
        System.out.println (" message sent Successfully !");
    }
}
