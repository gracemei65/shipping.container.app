package com.railinc.shipping.container.repository;

import com.railinc.shipping.container.model.ContainerStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;


public interface ContainerStatusRepository extends JpaRepository<ContainerStatus, Integer> {


    List<ContainerStatus> findByContainerOwnerId(Integer ownerId);
    Optional<ContainerStatus> findById(Integer id);

}
