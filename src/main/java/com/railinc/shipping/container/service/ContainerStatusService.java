package com.railinc.shipping.container.service;

import com.railinc.shipping.container.model.ContainerStatus;
import com.railinc.shipping.container.repository.ContainerStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ContainerStatusService {

    @Autowired
    private ContainerStatusRepository repository;

    public List<ContainerStatus> getContainersByOwner(@PathVariable Integer ownerId) {
        List<ContainerStatus> statuses = repository.findByOwnerId(ownerId);

        return statuses = statuses.stream()
                    .filter(status ->"AVAILABLE".equals(status.getStatus()))
                    .collect(Collectors.toList());
    }

    public ContainerStatus createContainer(@PathVariable Integer ownerId) {
        ContainerStatus status =new ContainerStatus();
        status.setOwnerId(ownerId);
        status.setCustomerId(0);
        status.setStatus("AVAILABLE");
        status.setEventTimestampEpoch(System.currentTimeMillis());
        return repository.save(status);
    }

    public ContainerStatus updateContainer(Integer containerId, String status){
        Optional<ContainerStatus> optional = repository.findById(containerId); // returns java8 optional
//        if (!optional.isPresent())
//            throw new EntityNotFoundException("Container  is not found with containerId  " + containerId);
        ContainerStatus container = optional.get();
        container.setStatus(status);
       return  repository.save(container);

    }

    public void deleteContainer(Integer containerId){
          repository.deleteById(containerId);
    }
}
