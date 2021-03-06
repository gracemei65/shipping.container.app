DROP TABLE IF EXISTS CONTAINER_STATUS;

CREATE TABLE CONTAINER_STATUS (
  ID INT PRIMARY KEY,
  OWNER_ID int NOT NULL,
  CUSTOMER_ID int NOT NULL,
  STATUS VARCHAR(250) DEFAULT NULL,
  EVENT_TIMESTAMP BIGINT DEFAULT NULL
);

--INSERT INTO CONTAINER_STATUS (OWNER_ID, CUSTOMER_ID, STATUS, EVENT_TIMESTAMP) VALUES
--  (1, 1, 'AVAILABLE', 1596061422449),
--  (1, 2, 'WAITING_FOR_PICKUP', 1596061436664),
--  (1, 3, 'IN_TRANSIT', 1596061595484),
--  (1, 4, 'READY_TO_RETURN',1596061617099),
--  (1, 5, 'AVAILABLE', 1596061632420),
--  (1, 6, 'IN_TRANSIT', 1596061436664),
--  (2, 7, 'AVAILABLE', 1596061436664),
--  (2, 8, 'AVAILABLE', 1596061436664),
--  (3, 9, 'AVAILABLE', 1596061436664),
--  (3, 10, 'AVAILABLE', 1596061436664);