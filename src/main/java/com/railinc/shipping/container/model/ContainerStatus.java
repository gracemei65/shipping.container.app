package com.railinc.shipping.container.model;

import javax.persistence.*;
import java.sql.Timestamp;


@Entity
@Table(name = "CONTAINER_STATUS")
public class ContainerStatus {
    @Id
    @GeneratedValue
    @Column(unique=true)
    private Integer id;
    @Column(name = "OWNER_ID")
    private Integer ownerId;
    @Column(name = "CUSTOMER_ID")
    private Integer customerId;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "EVENT_TIMESTAMP")
    private Long eventTimestampEpoch;
    @Transient
    private String eventTimestamp;

    public ContainerStatus() {
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
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
}
