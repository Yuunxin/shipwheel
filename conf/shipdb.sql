USE shipdb;

DROP TABLE IF EXISTS `ship_user` CASCADE;
DROP TABLE IF EXISTS `ship_node_nic` CASCADE;
DROP TABLE IF EXISTS `ship_node_addr` CASCADE;
DROP TABLE IF EXISTS `ship_route` CASCADE;

CREATE TABLE `ship_user` (
  `id`       INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name`     VARCHAR(36)  NOT NULL UNIQUE,
  `password` VARCHAR(128) NOT NULL, -- value is sha256sum
  `is_admin` BOOL         NOT NULL
);
INSERT INTO `ship_user` (`name`, `password`, `is_admin`)
VALUES ('admin', 'fc9a0fbd8bcb2e5a50e629163886ce78b45372bd6e3bfe22b6b051a44e3d6c25', TRUE);

CREATE TABLE `ship_node_nic` (
  `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `node_id` INT NOT NULL,
  `name` VARCHAR(32) NOT NULL
);

CREATE TABLE `ship_node_addr` (
  `id`     INT         NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `nic_id` INT         NOT NULL REFERENCES ship_node_nic(id),
  `ip`     VARCHAR(20) NOT NULL,
  `mask`   VARCHAR(20) NOT NULL
);

CREATE TABLE `ship_route` (
  `id`      INT         NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `subnet`  VARCHAR(20) NOT NULL,
  `mask`    VARCHAR(20) NOT NULL,
  `nic_id`  INT         NOT NULL REFERENCES ship_node_nic(id),
  `gateway` VARCHAR(20)
);

CREATE TABLE `ship_man_addr` (
  `id`      INT         NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `nic`     VARCHAR(20) NOT NULL,
  `ip`      VARCHAR(20) NOT NULL,
  `mask`    VARCHAR(20) NOT NULL,
  `gateway` VARCHAR(20) NOT NULL DEFAULT ''
);
