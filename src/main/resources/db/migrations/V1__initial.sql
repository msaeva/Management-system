CREATE SEQUENCE SEQ_PRIMARY_ROLE_ID
    START 1
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;


CREATE TABLE ROLES
(
    ID   BIGINT PRIMARY KEY DEFAULT nextval('SEQ_PRIMARY_ROLE_ID'),
    ROLE VARCHAR(255)
);

CREATE SEQUENCE SEQ_PRIMARY_USER_ID
    START 1
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;


CREATE TABLE USERS
(
    ID        BIGINT PRIMARY KEY DEFAULT nextval('SEQ_PRIMARY_USER_ID'),
    FIRSTNAME VARCHAR(255) NOT NULL,
    LASTNAME  VARCHAR(255) NOT NULL,
    EMAIL     VARCHAR(255) NOT NULL,
    PASSWORD  VARCHAR(255) NOT NULL,
    ROLE_ID   BIGINT       NOT NULL,
    FOREIGN KEY (ROLE_ID) REFERENCES ROLES (ID)

);


CREATE SEQUENCE SEQ_PRIMARY_TEAM_ID
    START 1
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

CREATE TABLE TEAMS
(
    ID   BIGINT PRIMARY KEY DEFAULT nextval('SEQ_PRIMARY_TEAM_ID'),
    NAME VARCHAR(255)
);


CREATE TABLE USERS_TEAMS
(
    USER_ID BIGINT,
    TEAM_ID BIGINT,
    PRIMARY KEY (USER_ID, TEAM_ID),
    FOREIGN KEY (USER_ID) REFERENCES USERS (ID),
    FOREIGN KEY (TEAM_ID) REFERENCES TEAMS (ID)
);

CREATE SEQUENCE SEQ_PRIMARY_PROJECT_ID
    START 1
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;



CREATE TABLE PROJECTS
(
    ID           BIGINT PRIMARY KEY DEFAULT nextval('SEQ_PRIMARY_PROJECT_ID'),
    TITLE        VARCHAR(255) NOT NULL,
    DESCRIPTION        VARCHAR(255) NOT NULL,
    STATUS       VARCHAR(255) NOT NULL,
    CREATED_DATE TIMESTAMP    NOT NULL
);

CREATE TABLE ADMIN_PROJECTS
(
    USER_ID    BIGINT,
    PROJECT_ID BIGINT,
    PRIMARY KEY (USER_ID, PROJECT_ID),
    FOREIGN KEY (USER_ID) REFERENCES USERS (ID),
    FOREIGN KEY (PROJECT_ID) REFERENCES PROJECTS (ID)
);

CREATE SEQUENCE SEQ_PRIMARY_TASK_ID
    START 1
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

CREATE TABLE TASKS
(
    ID          BIGINT PRIMARY KEY DEFAULT nextval('SEQ_PRIMARY_TASK_ID'),
    TITLE       VARCHAR(255),
    DESCRIPTION VARCHAR(255),
    STATUS      VARCHAR(255),
    USER_ID     BIGINT NOT NULL,
    PROJECT_ID  BIGINT NOT NULL,
    FOREIGN KEY (USER_ID) REFERENCES USERS (ID),
    FOREIGN KEY (PROJECT_ID) REFERENCES PROJECTS (ID)
);

CREATE SEQUENCE SEQ_PRIMARY_MEETING_ID
    START 1
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;


CREATE TABLE MEETINGS
(
    ID               BIGINT PRIMARY KEY DEFAULT nextval('SEQ_PRIMARY_MEETING_ID'),
    STATUS           VARCHAR(255),
    MEETING_DATETIME TIMESTAMP,
    PROJECT_ID       BIGINT NOT NULL,
    FOREIGN KEY (PROJECT_ID) REFERENCES PROJECTS (ID)
);

CREATE TABLE MEETINGS_TEAMS
(
    MEETING_ID BIGINT,
    TEAM_ID    BIGINT,
    PRIMARY KEY (MEETING_ID, TEAM_ID),
    FOREIGN KEY (TEAM_ID) REFERENCES TEAMS (ID),
    FOREIGN KEY (MEETING_ID) REFERENCES MEETINGS (ID)
);

CREATE TABLE PROJECTS_TEAMS
(
    PROJECT_ID BIGINT,
    TEAM_ID    BIGINT,
    PRIMARY KEY (PROJECT_ID, TEAM_ID),
    FOREIGN KEY (TEAM_ID) REFERENCES TEAMS (ID),
    FOREIGN KEY (PROJECT_ID) REFERENCES PROJECTS (ID)
);



INSERT INTO ROLES (ROLE)
VALUES ('USER'),
       ('ADMIN');