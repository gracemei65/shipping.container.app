package com.railinc.shipping.container.controller;


import java.util.List;

import com.railinc.shipping.container.model.ContainerStatus;
import com.railinc.shipping.container.service.ContainerStatusRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * Rest APIs endpoints to perform CRUD operation against H2 Inmemory CONTAINER_STATUS table
 *
 * @author  Grace Gong
 * @version 1.0
 * @since   2020-07-30
 */
@RestController
@RequestMapping(value = "/shipping")
public class ContainerStatusRestController {

    @Autowired
    private ContainerStatusRestService service;

    /**
     * Return a list of "AVAILABLE" containers based on containerOwnerId
     */
    @GetMapping("/container/{containerOwnerId}")
    public List<ContainerStatus> getContainersByOwner(@PathVariable Integer containerOwnerId) {
        return service.getContainersByOwner(containerOwnerId);

    }

    /**
     * Add a new container by supplying containerOwnerId
     * (customerId should default to 0, status should default to AVAILABLE)
     */
    @PostMapping("/container/{containerOwnerId}")
    public ContainerStatus createContainer(@PathVariable Integer containerOwnerId) {

        return service.createContainer(containerOwnerId);

    }

    /**
     * Update the status of an existing container
     */
    @PutMapping("/container/{containerId}")
    public ContainerStatus updateContainer(@PathVariable Integer containerId, @RequestParam String status) {

        return service.updateContainer(containerId, status);

    }

    /**
     * Delete a container by container id
     */
    @DeleteMapping("/container/{containerId}")
    public ResponseEntity<String> deleteContainer(@PathVariable Integer containerId) {

        return service.deleteContainer(containerId);

    }
}