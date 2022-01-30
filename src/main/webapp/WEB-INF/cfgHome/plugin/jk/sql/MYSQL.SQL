/*==============================================================*/
/* DBMS name:      MYSQL                                        */
/* Created on:     2022/1/30 20:41:02                            */
/*==============================================================*/

/*==============================================================*/
/* Table: JK_JOB(任务表)                                         */
/*==============================================================*/
CREATE TABLE JK_JOB (
  	ID BIGINT(20)                     NOT NULL                   COMMENT '主键id',
    JK_JOB_NAME VARCHAR(255)          NOT NULL                   COMMENT '',
    JK_JOB_TYPE SMALLINT(4)           NOT NULL                   COMMENT '',
    JOB_NAME VARCHAR(255)             DEFAULT NULL               COMMENT '',
    JOB_GROUP VARCHAR(255)            DEFAULT NULL               COMMENT '',
    TRIGGER_NAME VARCHAR(255)         DEFAULT NULL               COMMENT '',
    TRIGGER_GROUP VARCHAR(255)        DEFAULT NULL               COMMENT '',
    MEMBERID BIGINT(20)               DEFAULT NULL               COMMENT '',
    CREATE_DATE DATETIME              DEFAULT NULL               COMMENT '',
    MODIFYID BIGINT(20)               DEFAULT NULL               COMMENT '',
    MODIFY_DATE DATETIME              DEFAULT NULL               COMMENT '',
    DELETED BOOLEAN                   DEFAULT NULL               COMMENT '',
  	PRIMARY KEY (ID)
);
/*==============================================================*/
/* Table: JK_JOB_LOG(任务日志表)                                  */
/*==============================================================*/
CREATE TABLE JK_JOB_LOG(
   ID BIGINT(20)                     NOT NULL                   COMMENT '主键id',
   JOB_ID BIGINT(20)                 NOT NULL                   COMMENT '',
   START_TIME DATETIME               DEFAULT NULL               COMMENT '',
   END_TIME DATETIME                 DEFAULT NULL               COMMENT '',
   MANUAL BOOLEAN                    DEFAULT NULL               COMMENT '',
   EXECUTE_STATUS SMALLINT(4)        DEFAULT NULL               COMMENT '',
   DELETED BOOLEAN                   DEFAULT NULL               COMMENT '',
   PRIMARY KEY (ID)
);