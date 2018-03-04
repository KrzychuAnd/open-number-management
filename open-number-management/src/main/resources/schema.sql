create database openNM;
use openNM;
CREATE TABLE `Users` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`login` varchar(50) NOT NULL UNIQUE,
	`first_name` varchar(50) NOT NULL,
	`last_name` varchar(50) NOT NULL,
	`email` varchar(50),
	`password` varchar(256) NOT NULL,
	`role_id` INT,
	`locked` char(1) NOT NULL DEFAULT 'N',
	`row_added_user` varchar(50) NOT NULL,
	`row_added_dttm` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`row_update_user` varchar(50) NOT NULL,
	`row_update_dttm` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`)
);

CREATE TABLE `Roles` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`name` varchar(50) NOT NULL UNIQUE,
	`descr` varchar(200) NOT NULL,
	`row_added_user` varchar(50) NOT NULL,
	`row_added_dttm` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`row_updated_user` varchar(50) NOT NULL,
	`row_updated_dttm` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`)
);

CREATE TABLE `Permissions` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`name` varchar(50) NOT NULL UNIQUE,
	`descr` varchar(200) NOT NULL,
	`row_added_user` varchar(50) NOT NULL,
	`row_added_dttm` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`row_updated_user` varchar(50) NOT NULL,
	`row_updated_dttm` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`)
);

CREATE TABLE `Roles2Permissions` (
	`role_id` int NOT NULL,
	`perm_id` int NOT NULL,
	`row_added_user` varchar(50) NOT NULL,
	`row_added_dttm` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (`role_id`,`perm_id`)
);

CREATE TABLE `Resource` (
	`id` int NOT NULL AUTO_INCREMENT,
	`name` varchar(50) NOT NULL UNIQUE,
	`res_type_id` int NOT NULL,
	`res_status_id` int NOT NULL,
	`descr` varchar(200),
	`rel_res_id` int,
	`row_added_user` varchar(50) NOT NULL,
	`row_added_dttm` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`row_updated_user` varchar(50) NOT NULL,
	`row_updated_dttm` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`)
);

CREATE TABLE `Resource_Type` (
	`id` int NOT NULL AUTO_INCREMENT,
	`name` varchar(50) NOT NULL UNIQUE,
	`descr` varchar(200) NOT NULL,
	`length` int NOT NULL,
	`prefix` int NOT NULL,
	`reservation_time` int NOT NULL DEFAULT '60',
	`row_added_user` varchar(50) NOT NULL,
	`row_added_dttm` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`row_updated_user` varchar(50) NOT NULL,
	`row_updated_dttm` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`),
	UNIQUE KEY (`length`, `prefix`)
);

CREATE TABLE `Resource_Status` (
	`id` int NOT NULL AUTO_INCREMENT,
	`name` varchar(50) NOT NULL UNIQUE,
	`descr` varchar(200) NOT NULL,
	`row_added_user` varchar(50) NOT NULL,
	`row_added_dttm` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`row_updated_user` varchar(50) NOT NULL,
	`row_updated_dttm` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`)
);

CREATE TABLE `Resource_lifecycle` (
	`id` int NOT NULL AUTO_INCREMENT,
	`source_status_id` int NOT NULL DEFAULT 0,
	`target_status_id` int NOT NULL,
	`row_added_user` varchar(50) NOT NULL,
	`row_added_dttm` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`),
	UNIQUE KEY (`source_status_id`, `target_status_id`)
);

CREATE TABLE `Permissions2ResourceType` (
	`perm_id` int NOT NULL,
	`res_type_id` int NOT NULL,
	`row_added_user` varchar(50) NOT NULL,
	`row_added_dttm` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (`perm_id`,`res_type_id`)
);

CREATE TABLE `Resource_History` (
	`id` int NOT NULL AUTO_INCREMENT,
	`res_id` int NOT NULL,
	`source_status_id` int NOT NULL DEFAULT '0',
	`target_status_id` int NOT NULL,
	`old_rel_res_id` int,
	`new_rel_res_id` int,
	`row_added_user` varchar(50) NOT NULL,
	`row_added_dttm` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`)
);

ALTER TABLE `Users` ADD CONSTRAINT `Users_fk0` FOREIGN KEY (`role_id`) REFERENCES `Roles`(`id`);

ALTER TABLE `Roles` ADD CONSTRAINT `Roles_fk0` FOREIGN KEY (`row_added_user`) REFERENCES `Users`(`login`);

ALTER TABLE `Roles` ADD CONSTRAINT `Roles_fk1` FOREIGN KEY (`row_updated_user`) REFERENCES `Users`(`login`);

ALTER TABLE `Permissions` ADD CONSTRAINT `Permissions_fk0` FOREIGN KEY (`row_added_user`) REFERENCES `Users`(`login`);

ALTER TABLE `Permissions` ADD CONSTRAINT `Permissions_fk1` FOREIGN KEY (`row_updated_user`) REFERENCES `Users`(`login`);

ALTER TABLE `Roles2Permissions` ADD CONSTRAINT `Roles2Permissions_fk0` FOREIGN KEY (`role_id`) REFERENCES `Roles`(`id`);

ALTER TABLE `Roles2Permissions` ADD CONSTRAINT `Roles2Permissions_fk1` FOREIGN KEY (`perm_id`) REFERENCES `Permissions`(`id`);

ALTER TABLE `Roles2Permissions` ADD CONSTRAINT `Roles2Permissions_fk2` FOREIGN KEY (`row_added_user`) REFERENCES `Users`(`login`);

ALTER TABLE `Resource` ADD CONSTRAINT `Resource_fk0` FOREIGN KEY (`res_type_id`) REFERENCES `Resource_Type`(`id`);

ALTER TABLE `Resource` ADD CONSTRAINT `Resource_fk1` FOREIGN KEY (`res_status_id`) REFERENCES `Resource_Status`(`id`);

ALTER TABLE `Resource` ADD CONSTRAINT `Resource_fk2` FOREIGN KEY (`rel_res_id`) REFERENCES `Resource`(`id`);

ALTER TABLE `Resource` ADD CONSTRAINT `Resource_fk3` FOREIGN KEY (`row_added_user`) REFERENCES `Users`(`login`);

ALTER TABLE `Resource` ADD CONSTRAINT `Resource_fk4` FOREIGN KEY (`row_updated_user`) REFERENCES `Users`(`login`);

ALTER TABLE `Resource_Type` ADD CONSTRAINT `Resource_Type_fk0` FOREIGN KEY (`row_added_user`) REFERENCES `Users`(`login`);

ALTER TABLE `Resource_Type` ADD CONSTRAINT `Resource_Type_fk1` FOREIGN KEY (`row_updated_user`) REFERENCES `Users`(`login`);

ALTER TABLE `Resource_Status` ADD CONSTRAINT `Resource_Status_fk0` FOREIGN KEY (`row_added_user`) REFERENCES `Users`(`login`);

ALTER TABLE `Resource_Status` ADD CONSTRAINT `Resource_Status_fk1` FOREIGN KEY (`row_updated_user`) REFERENCES `Users`(`login`);

ALTER TABLE `Resource_lifecycle` ADD CONSTRAINT `Resource_lifecycle_fk0` FOREIGN KEY (`source_status_id`) REFERENCES `Resource_Status`(`id`);

ALTER TABLE `Resource_lifecycle` ADD CONSTRAINT `Resource_lifecycle_fk1` FOREIGN KEY (`target_status_id`) REFERENCES `Resource_Status`(`id`);

ALTER TABLE `Resource_lifecycle` ADD CONSTRAINT `Resource_lifecycle_fk2` FOREIGN KEY (`row_added_user`) REFERENCES `Users`(`login`);

ALTER TABLE `Permissions2ResourceType` ADD CONSTRAINT `Permissions2ResourceType_fk0` FOREIGN KEY (`perm_id`) REFERENCES `Permissions`(`id`);

ALTER TABLE `Permissions2ResourceType` ADD CONSTRAINT `Permissions2ResourceType_fk1` FOREIGN KEY (`res_type_id`) REFERENCES `Resource_Type`(`id`);

ALTER TABLE `Permissions2ResourceType` ADD CONSTRAINT `Permissions2ResourceType_fk2` FOREIGN KEY (`row_added_user`) REFERENCES `Users`(`login`);

ALTER TABLE `Resource_History` ADD CONSTRAINT `Resource_History_fk0` FOREIGN KEY (`res_id`) REFERENCES `Resource`(`id`);

ALTER TABLE `Resource_History` ADD CONSTRAINT `Resource_History_fk1` FOREIGN KEY (`source_status_id`) REFERENCES `Resource_Status`(`id`);

ALTER TABLE `Resource_History` ADD CONSTRAINT `Resource_History_fk2` FOREIGN KEY (`target_status_id`) REFERENCES `Resource_Status`(`id`);

ALTER TABLE `Resource_History` ADD CONSTRAINT `Resource_History_fk3` FOREIGN KEY (`old_rel_res_id`) REFERENCES `Resource`(`id`);

ALTER TABLE `Resource_History` ADD CONSTRAINT `Resource_History_fk4` FOREIGN KEY (`new_rel_res_id`) REFERENCES `Resource`(`id`);

ALTER TABLE `Resource_History` ADD CONSTRAINT `Resource_History_fk5` FOREIGN KEY (`row_added_user`) REFERENCES `Users`(`login`);

