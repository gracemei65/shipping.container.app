package com.railinc.shipping.container.service;

import com.railinc.shipping.container.exception.EntityNotFoundException;
import com.railinc.shipping.container.model.ContainerStatus;
import com.railinc.shipping.container.repository.ContainerStatusRepository;
import com.railinc.shipping.container.util.DateFormatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ContainerStatusRestService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ContainerStatusRepository repository;

    public List<ContainerStatus> getContainersByOwner(@PathVariable Integer ownerId) {
        List<ContainerStatus> statuses = repository.findByContainerOwnerId(ownerId);

        return statuses = statuses.stream()
                .filter(status -> "AVAILABLE".equals(status.getStatus()))
                .peek(status -> status.setEventTimestamp(DateFormatUtil.formatTime(status.getEventTimestampEpoch())))
                .collect(Collectors.toList());
    }

    public ContainerStatus createContainer(@PathVariable Integer ownerId) {
        ContainerStatus status = new ContainerStatus();
        //status.setContainerId(1);
        status.setContainerOwnerId(ownerId);
        status.setCustomerId(0);
        status.setStatus("AVAILABLE");
        status.setEventTimestampEpoch(System.currentTimeMillis());
        return repository.save(status);

    }

    public ContainerStatus updateContainer(Integer containerId, String status) {
        Optional<ContainerStatus> optional = repository.findById(containerId); // returns java8 optional
        if (!optional.isPresent()) {
            logger.error("Container " + containerId + " is not found  ");
            throw new EntityNotFoundException("Container " + containerId + " is not found  ");
        }

        ContainerStatus container = optional.get();
        container.setStatus(status);
        return repository.save(container);
    }

    public String deleteContainer(Integer containerId) {
        Optional<ContainerStatus> optional = repository.findById(containerId); // returns java8 optional
        if (!optional.isPresent()) {
            logger.info("Container " + containerId + " is not found  ");
            throw new EntityNotFoundException("Container " + containerId + " is not found  ");
        }
        repository.deleteById(containerId);
        String msg ="container deleted successfully!";
        logger.info(msg);
        return msg;
    }
}
