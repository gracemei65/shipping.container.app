package com.railinc.shipping.container.repository;

import com.railinc.shipping.container.model.ContainerStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface ContainerStatusRepository extends JpaRepository<ContainerStatus, Integer> {

    //http://localhost:9090/h2-console
    List<ContainerStatus> findByOwnerId(Integer ownerId);

}
