package com.railinc.shipping.container.controller;

import com.railinc.shipping.container.model.ContainerStatus;
import com.railinc.shipping.container.service.ContainerStatusRestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Rest APIs endpoints to perform CRUD operation against H2 Inmemory CONTAINER_STATUS table
 *
 * @author  Grace Gong
 * @version 1.0
 * @since   2020-07-30
 */
@RestController
@RequestMapping(value = "/shipping")
@CrossOrigin(origins = "*")
public class ContainerStatusRestController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ContainerStatusRestService service;

    /**
     * Return All containers
     */
    @GetMapping("/containers")
    public List<ContainerStatus> getAllContainers() {
        return service.getAllContainers();

    }
    /**
     * Return a list of "AVAILABLE" containers based on containerOwnerId
     */
    @GetMapping("/containers/{containerOwnerId}")
    public List<ContainerStatus> getContainersByOwner(@PathVariable Integer containerOwnerId) {

        return service.getContainersByOwner(containerOwnerId);

    }

    /**
     * Add a new container by supplying containerOwnerId
     * (customerId should default to 0, status should default to AVAILABLE)
     */
    @PostMapping("/containers/{containerOwnerId}")
    public ContainerStatus createContainer(@PathVariable Integer containerOwnerId) {
        logger.info("createContainer for ownerId : "+containerOwnerId);
        return service.createContainer(containerOwnerId);
    }

    /**
     * Update the status of an existing container
     */
    @PutMapping("/containers/{containerId}")
    public ContainerStatus updateContainer(@PathVariable Integer containerId, @RequestParam String status) {

        return service.updateContainer(containerId, status);
    }

    /**
     * Delete a container by container id
     */
    @DeleteMapping("/containers/{containerId}")
    public String deleteContainer(@PathVariable Integer containerId) {

        return service.deleteContainer(containerId);

    }
}