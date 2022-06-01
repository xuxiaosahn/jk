CREATE TABLE JK_JOB (
	ID				bigint			NOT NULL,
	JK_JOB_NAME		nvarchar(255)	NOT NULL,
	JK_JOB_TYPE		int	        	NOT NULL,
	SCHED_NAME		nvarchar(255)	NULL,
	JOB_NAME		nvarchar(255)	NULL,
	JOB_GROUP		nvarchar(255)	NULL,
	TRIGGER_NAME	nvarchar(255)	NULL,
	TRIGGER_GROUP	nvarchar(255)	NULL,
	MEMBERID		bigint			NULL,
	CREATE_DATE		datetime		NULL,
	MODIFYID		bigint			NULL,
	MODIFY_DATE		datetime		NULL,
	DELETED			bit				NULL
);
ALTER TABLE JK_JOB ADD PRIMARY KEY NONCLUSTERED (id);

CREATE TABLE JK_JOB_LOG(
	ID				bigint			NOT NULL,
	JOB_ID			bigint			NOT NULL,
	START_TIME		datetime		NULL,
	END_TIME		datetime		NULL,
	MANUAL			bit				NULL,
	EXECUTE_STATUS	int		        NULL,
	DELETED			bit				NULL
);
ALTER TABLE JK_JOB_LOG ADD PRIMARY KEY NONCLUSTERED (id);