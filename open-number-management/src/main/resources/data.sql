use openNM;
-- create admin user
set @adm_login = 'admin';
INSERT INTO `opennm`.`users` (`login`, `first_name`, `last_name`, `password`, `row_added_user`, `row_update_user`) 
		VALUES 
			(@adm_login, 'Administrator', 'Admin', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', 'admin', 'admin');
			
-- create ADMIN role
INSERT INTO `opennm`.`roles` (`name`, `descr`, `row_added_user`, `row_added_dttm`, `row_updated_user`) 
		VALUES ('ADMIN', 'Administrator', 'admin', '2018-02-10 16:17:05', 'admin');
		
-- update admin user by adding reference to ADMIN role
UPDATE opennm.users SET role_id = (select id from opennm.roles where name = 'ADMIN') WHERE login = 'admin';

-- create ADMIN_PERM permission
INSERT INTO `opennm`.`permissions` (`name`, `descr`, `row_added_user`, `row_added_dttm`, `row_updated_user`) 
		VALUES ('ADMIN_PERM', 'Administrator privileges', 'admin', '2018-02-10 16:20:11', 'admin');
		
-- assign ADMIN_PERM permission to ADMIN role
INSERT INTO opennm.roles2permissions (`role_id`, `perm_id`, `row_added_user`) 
		VALUES ((select id from roles where name = 'ADMIN'), (select id from permissions where name = 'ADMIN_PERM'), 'admin');

-- create Resource Status NEW
INSERT INTO opennm.resource_status (name, descr, row_added_user, row_updated_user)
		VALUES ('NEW', 'New', 'admin', 'admin');
		
-- create Resource Status RESERVED
INSERT INTO opennm.resource_status (name, descr, row_added_user, row_updated_user)
		VALUES ('RESERVED', 'Reserved', 'admin', 'admin');

-- create Resource Status QUARANTINE
INSERT INTO opennm.resource_status (name, descr, row_added_user, row_updated_user)
		VALUES ('QUARANTINE', 'Quarantine', 'admin', 'admin');
		
-- create Resource Status ACTIVE
INSERT INTO opennm.resource_status (name, descr, row_added_user, row_updated_user)
		VALUES ('ACTIVE', 'Active', 'admin', 'admin');
		
-- create Resource Lifecycle <EMPTY> -> NEW
INSERT INTO opennm.resource_lifecycle (source_status_id, target_status_id, row_added_user)
		VALUES (NULL, (SELECT id  FROM opennm.resource_status WHERE name = 'NEW'), 'admin');
		
-- create Resource Lifecycle NEW -> RESERVED
INSERT INTO opennm.resource_lifecycle (source_status_id, target_status_id, row_added_user)
		VALUES ((SELECT id  FROM opennm.resource_status WHERE name = 'NEW'), (SELECT id  FROM opennm.resource_status WHERE name = 'RESERVED'), 'admin');

-- create Resource Lifecycle RESERVED -> ACTIVE
INSERT INTO opennm.resource_lifecycle (source_status_id, target_status_id, row_added_user)
		VALUES ((SELECT id  FROM opennm.resource_status WHERE name = 'RESERVED'), (SELECT id  FROM opennm.resource_status WHERE name = 'ACTIVE'), 'admin');
		
-- create Resource Lifecycle ACTIVE -> QUARANTINE
INSERT INTO opennm.resource_lifecycle (source_status_id, target_status_id, row_added_user)
		VALUES ((SELECT id  FROM opennm.resource_status WHERE name = 'ACTIVE'), (SELECT id  FROM opennm.resource_status WHERE name = 'QUARANTINE'), 'admin');
		
-- create Resource Lifecycle RESERVED -> NEW
INSERT INTO opennm.resource_lifecycle (source_status_id, target_status_id, row_added_user)
		VALUES ((SELECT id  FROM opennm.resource_status WHERE name = 'RESERVED'), (SELECT id  FROM opennm.resource_status WHERE name = 'NEW'), 'admin');
		
-- create Resource Lifecycle QUARANTINE -> NEW
INSERT INTO opennm.resource_lifecycle (source_status_id, target_status_id, row_added_user)
		VALUES ((SELECT id  FROM opennm.resource_status WHERE name = 'QUARANTINE'), (SELECT id  FROM opennm.resource_status WHERE name = 'NEW'), 'admin');