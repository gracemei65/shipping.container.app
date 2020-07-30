package com.railinc.shipping.container.service;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.railinc.shipping.container.config.ContainerStatusConstants;
import com.railinc.shipping.container.model.ContainerStatus;
import com.railinc.shipping.container.repository.ContainerStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ContainerStatusJmsService {

    @Autowired
    private Gson gson;
    @Autowired
    private ContainerStatusRepository repository;

    @JmsListener(destination = ContainerStatusConstants.STATUS_INBOUND_QUEUE)
    public void receiveMessage(String message) {

        System.out.println("receive  message  : " + message);
        processMessage(message);
    }

    public void processMessage(String message)
    {
        try{
            JsonParser parser = new JsonParser();
            parser.parse(message);
        }
        catch(JsonSyntaxException jse){
            System.out.println("Not a valid Json String:"+jse.getMessage());
            return;
        }

        ContainerStatus containerStatus = gson.fromJson(message, ContainerStatus.class);
        Integer containerId =containerStatus.getContainerId();
        Long  timestampEpoch     =containerStatus.getEventTimestampEpoch();
        if(containerId !=null && timestampEpoch !=null)
        {
            Optional<ContainerStatus> optional = repository.findById(containerId);
            if (optional.isPresent())
            {
                ContainerStatus existingOne =optional.get();
                if(timestampEpoch>existingOne.getEventTimestampEpoch())
                {
                    repository.save(containerStatus);

                }
                else
                {
                    System.out.println(" ignore older message ");
                }
            }


        }




        System.out.println("process  message  : " + containerStatus);


    }
}
