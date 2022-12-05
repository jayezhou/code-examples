在源数据库创建需要同步的表（达梦）
CREATE TABLE "TEST_KETTLE"
(
"C1" NVARCHAR2(100 CHAR),
"C2" NVARCHAR2(100 CHAR),
"CREATE_TIME" TIMESTAMP,
"UPDATE_TIME" TIMESTAMP,
"ID" BIGINT NOT NULL, UNIQUE("ID")) ;

在目标数据库创建需要同步的表（MySQL）
CREATE TABLE `test_kettle` (
  `c1` varchar(100) DEFAULT NULL,
  `c2` varchar(100) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

在源数据库创建同步时间记录表（达梦）
CREATE TABLE "LOAD_TIME"
(
"TABLE_NAME" VARCHAR2(80),
"LAST_LOAD" TIMESTAMP,
"CURRENT_LOAD" TIMESTAMP);

执行exe_job.bat或者exe_job.sh运行job


