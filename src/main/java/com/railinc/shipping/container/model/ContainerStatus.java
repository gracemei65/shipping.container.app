package com.railinc.shipping.container.model;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;


@Entity
@Table(name = "CONTAINER_STATUS")
public class ContainerStatus  {
    @Id
    //@GeneratedValue
    @Column(name = "ID", unique=true)
    private Integer containerId;
    @Column(name = "OWNER_ID")
    private Integer containerOwnerId;
    @Column(name = "CUSTOMER_ID")
    private Integer customerId;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "EVENT_TIMESTAMP")
    private Long eventTimestampEpoch;
    @Transient
    private String eventTimestamp;

    public Integer getContainerId() {
        return containerId;
    }

    public void setContainerId(Integer containerId) {
        this.containerId = containerId;
    }

    public Integer getContainerOwnerId() {
        return containerOwnerId;
    }

    public void setContainerOwnerId(Integer containerOwnerId) {
        this.containerOwnerId = containerOwnerId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getEventTimestampEpoch() {
        return eventTimestampEpoch;
    }

    public void setEventTimestampEpoch(Long eventTimestampEpoch) {
        this.eventTimestampEpoch = eventTimestampEpoch;
    }

    public String getEventTimestamp() {
        return eventTimestamp;
    }

    public void setEventTimestamp(String eventTimestamp) {
        this.eventTimestamp = eventTimestamp;
    }

    @Override
    public String toString() {
        return "ContainerStatus{" +
                "containerId=" + containerId +
                ", containerOwnerId=" + containerOwnerId +
                ", customerId=" + customerId +
                ", status='" + status + '\'' +
                ", eventTimestampEpoch=" + eventTimestampEpoch +
                ", eventTimestamp='" + eventTimestamp + '\'' +
                '}';
    }
}
