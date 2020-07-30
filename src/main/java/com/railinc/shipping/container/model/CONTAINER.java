package com.railinc.shipping.container.model;

public class CONTAINER {
    private int ID;
    private int OWNER_ID;
    private int CUSTOMER_ID;
    private String STATUS;
    private String STATUS_TIMESTAMP;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getOWNER_ID() {
        return OWNER_ID;
    }

    public void setOWNER_ID(int OWNER_ID) {
        this.OWNER_ID = OWNER_ID;
    }

    public int getCUSTOMER_ID() {
        return CUSTOMER_ID;
    }

    public void setCUSTOMER_ID(int CUSTOMER_ID) {
        this.CUSTOMER_ID = CUSTOMER_ID;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getSTATUS_TIMESTAMP() {
        return STATUS_TIMESTAMP;
    }

    public void setSTATUS_TIMESTAMP(String STATUS_TIMESTAMP) {
        this.STATUS_TIMESTAMP = STATUS_TIMESTAMP;
    }
}

