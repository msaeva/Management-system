CREATE SEQUENCE SEQ_PRIMARY_COMMENT_ID
    START 1
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

CREATE TABLE COMMENTS (
                          ID              BIGINT PRIMARY KEY,
                          USER_ID         BIGINT,
                          TASK_ID         BIGINT,
                          COMMENT         VARCHAR(255),
                          CREATED_DATE    TIMESTAMP,
                          FOREIGN KEY (USER_ID) REFERENCES USERS (ID),
                          FOREIGN KEY (TASK_ID) REFERENCES TASKS (ID)
);