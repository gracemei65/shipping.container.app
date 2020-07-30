package com.railinc.shipping.container.controller;

import com.railinc.shipping.container.config.ContainerStatusConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;

import javax.jms.Queue;

/**
 * sent container message to JMS queue STATUS.INBOUND.QUEUE
 * for JMS listerner to read and process
 *
 * @author  Grace Gong
 * @version 1.0
 * @since   2020-07-30
 */
@RestController
@RequestMapping(value = "/shipping")
public class ContainerStatusJmsController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private JmsTemplate jmsTemplate;

    /**
     * send message to STATUS.INBOUND.QUEUE
     */
    @PostMapping("/container/message")
    public ResponseEntity<String> readAndSend(@RequestBody String message) throws Exception {
        jmsTemplate.convertAndSend(ContainerStatusConstants.STATUS_INBOUND_QUEUE, message);
        String msg =" message sent Successfully ! ";
        logger.error(msg);
        return new ResponseEntity<String>(msg, HttpStatus.OK);
    }
}
