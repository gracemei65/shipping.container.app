package com.railinc.shipping.container.controller;


import java.util.List;

import com.railinc.shipping.container.model.ContainerStatus;
import com.railinc.shipping.container.service.ContainerStatusRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/shipping")
public class ContainerStatusRestController {

    @Autowired
    private ContainerStatusRestService service;

    @GetMapping("/container/{ownerId}")
    public List<ContainerStatus> getContainersByOwner(@PathVariable Integer ownerId) {
        return service.getContainersByOwner(ownerId);

    }

    @PostMapping("/container/{ownerId}")
    public ContainerStatus createContainer(@PathVariable Integer ownerId) {

        return service.createContainer(ownerId);

    }

    @PutMapping("/container/{containerId}")
    public void updateContainer(@PathVariable Integer containerId, @RequestParam String status) {
        service.updateContainer(containerId, status);

    }

    @DeleteMapping("/container/{containerId}")
    public void deleteContainer(@PathVariable Integer containerId) {

        service.deleteContainer(containerId);

    }


}