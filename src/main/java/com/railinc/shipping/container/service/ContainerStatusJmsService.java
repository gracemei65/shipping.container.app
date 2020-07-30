package com.railinc.shipping.container.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.railinc.shipping.container.config.ContainerStatusConstants;
import com.railinc.shipping.container.model.CONTAINER;
import com.railinc.shipping.container.model.ContainerStatus;
import com.railinc.shipping.container.repository.ContainerStatusRepository;
import com.railinc.shipping.container.util.DateFormatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.Map;
import java.util.Optional;

/**
 * JMS message listener and processor including
 * 1. Read a container status message from queue, STATUS.INBOUND.QUEUE
 * 2. If the timestamp of the message is after the most recent in the table for the containerId,
 * then add the record to the table and
 * 3. If the timestamp is before that most recent table for the container id or the message
 * does not meet the validations for the JSON, place the record on the dead-letter queue STATUS.INBOUND.QUEUE.DLQ
 *
 * @author  Grace Gong
 * @version 1.0
 * @since   2020-07-30
 */
@Service
public class ContainerStatusJmsService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Gson gson;
    @Autowired
    private ContainerStatusRepository repository;

    @Autowired
    private JmsTemplate jmsTemplate;

    /**
     * listen STATUS.INBOUND.QUEUE message
     */
    @JmsListener(destination = ContainerStatusConstants.STATUS_INBOUND_QUEUE, containerFactory = "myFactory")
    public void receiveMessage(String message) {

        logger.info("receive  message  : " + message);
        processMessage(message);
    }

    /**
     * Process STATUS.INBOUND.QUEUE message
     */
    public void processMessage(String message) {

        try {
            JsonParser parser = new JsonParser();
            parser.parse(message);
        } catch (JsonSyntaxException jse) {
            //If the message is not valid JSON, place the record on the dead-letter queue STATUS.INBOUND.QUEUE.DLQ
            logger.error(" invalid json message and place it to dead-letter queue STATUS.INBOUND.QUEUE.DLQ " + ContainerStatusConstants.STATUS_INBOUND_QUEUE_DLQ);
            jmsTemplate.convertAndSend(ContainerStatusConstants.STATUS_INBOUND_QUEUE_DLQ, message);
            return;
        }

        ContainerStatus containerStatus = gson.fromJson(message, ContainerStatus.class);
        Integer containerId = containerStatus.getContainerId();
        Long timestampEpoch = containerStatus.getEventTimestampEpoch();
        if (containerId != null && timestampEpoch != null) {
            Optional<ContainerStatus> optional = repository.findById(containerId);
            if (optional.isPresent()) {
                ContainerStatus existingOne = optional.get();
                //If the timestamp of the message is after the most recent in the table for the containerId, then add the record to the table and
                //send a message to the STATUS.OUTBOUND.QUEUE in an XML format similar to the following ...
                if (timestampEpoch > existingOne.getEventTimestampEpoch()) {
                    //update the record in db
                    repository.save(containerStatus);
                    //send a message to STATUS.OUTBOUND.QUEUE
                    XmlMapper xmlMapper = new XmlMapper();
                    try {
                        String eventTimeStamp = DateFormatUtil.formatTime(containerStatus.getEventTimestampEpoch());
                        CONTAINER container = new CONTAINER();
                        container.setID(containerStatus.getContainerId());
                        container.setOWNER_ID(containerStatus.getContainerOwnerId());
                        container.setSTATUS(containerStatus.getStatus());
                        container.setSTATUS_TIMESTAMP(eventTimeStamp);
                        container.setCUSTOMER_ID(containerStatus.getCustomerId());

                        String xmlMessage = xmlMapper.writeValueAsString(container).toUpperCase();
                        logger.info("send xmlMessage to STATUS.OUTBOUND.QUEUE  " + xmlMessage);
                        jmsTemplate.convertAndSend(ContainerStatusConstants.STATUS_OUTBOUND_QUEUE, message);
                    } catch (JsonProcessingException e) {
                        logger.error(" JsonProcessingException throw and put it to dead-letter queue STATUS.INBOUND.QUEUE.DLQ " + ContainerStatusConstants.STATUS_INBOUND_QUEUE_DLQ);
                    }
                } else {
                    //If the timestamp is before that most recent table for the container place the record on the dead-letter queue STATUS.INBOUND.QUEUE.DLQ
                    logger.warn(" sending stale message to " + ContainerStatusConstants.STATUS_INBOUND_QUEUE_DLQ);
                    jmsTemplate.convertAndSend(ContainerStatusConstants.STATUS_INBOUND_QUEUE_DLQ, message);
                }
            } else {
                logger.warn(" container " + containerId + " does not exist in db yet and treat it as invalid one and  send to " + ContainerStatusConstants.STATUS_INBOUND_QUEUE_DLQ);
                jmsTemplate.convertAndSend(ContainerStatusConstants.STATUS_INBOUND_QUEUE_DLQ, message);
            }
        }

    }
}
