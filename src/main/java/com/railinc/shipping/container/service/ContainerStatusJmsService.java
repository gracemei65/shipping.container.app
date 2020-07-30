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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.Map;
import java.util.Optional;

@Service
public class ContainerStatusJmsService {

    @Autowired
    private Gson gson;
    @Autowired
    private ContainerStatusRepository repository;

    @Autowired
    private JmsTemplate jmsTemplate;

    @JmsListener(destination = ContainerStatusConstants.STATUS_INBOUND_QUEUE, containerFactory = "myFactory")
    public void receiveMessage(String message) {

        System.out.println("receive  message  : " + message);
        processMessage(message);
    }

    public void processMessage(String message) {
        try {
            JsonParser parser = new JsonParser();
            parser.parse(message);
        } catch (JsonSyntaxException jse) {
            //If the message is not valid JSON, place the record on the dead-letter queue STATUS.INBOUND.QUEUE.DLQ

            System.out.println(" sending invalid json message to " + ContainerStatusConstants.STATUS_INBOUND_QUEUE_DLQ);
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
                        System.out.println("send xmlMessage to STATUS.OUTBOUND.QUEUE  " + xmlMessage);
                        jmsTemplate.convertAndSend(ContainerStatusConstants.STATUS_OUTBOUND_QUEUE, message);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                } else {
                    //If the timestamp is before that most recent table for the container place the record on the dead-letter queue STATUS.INBOUND.QUEUE.DLQ
                    System.out.println(" sending stale message to " + ContainerStatusConstants.STATUS_INBOUND_QUEUE_DLQ);
                    jmsTemplate.convertAndSend(ContainerStatusConstants.STATUS_INBOUND_QUEUE_DLQ, message);
                }
            } else {
                System.out.println(" container " + containerId + " does not existing yet and treat it as invalid one and  send to " + ContainerStatusConstants.STATUS_INBOUND_QUEUE_DLQ);
                jmsTemplate.convertAndSend(ContainerStatusConstants.STATUS_INBOUND_QUEUE_DLQ, message);
            }


        }


        System.out.println("process  message  : " + containerStatus);


    }
}
