DROP SCHEMA IF EXISTS db;
CREATE SCHEMA db;
USE db;

-- mind nullable columns!
CREATE TABLE `users` (
  `id` INT(10) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  -- merge firstname and lastname
  `firstname` varchar(255),
  `lastname` varchar(255),
  `description` mediumtext,
  -- guest: map to enum Role (USER; GUEST)
  -- the column type tinyint(1) can be easily interpreted as an boolean: rs.getBoolean("guest")
  `guest` tinyint(1) NOT NULL DEFAULT '0',
  -- platform: contains a mess! eu, na, EU, NA, europe, northamerica, null => map to enum Platform(EU,NA)!
  `platform` varchar(100),
  `date_created` timestamp NOT NULL DEFAULT '1970-01-03 00:00:00',
  -- state: map to enum State
  `state` enum('DELETED','DEACTIVATED','ACTIVATED') DEFAULT 'ACTIVATED',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `users` (email, firstname, lastname, description, guest, platform, date_created, state) VALUES
('peter@kilometer.de', 'Peter', 'Kilometer', 'Ein super Typ', 0, 'eu', '2017-09-06 14:00:00', 'ACTIVATED')
,('peter@gmail.de', null, null, null, 1, 'na', '2016-09-06 14:00:00', 'ACTIVATED')
,('bla@gmail.de', null, null, null, 1, 'NA', '2015-09-06 14:00:00', 'DEACTIVATED')
,('bla2@gmail.de', null, null, null, 0, 'europe', '2015-09-06 14:00:00', 'DELETED')
,('bla2@gmail.de', 'Anne', null, 'Ganz ok!', 0, null, '2015-09-06 14:00:00', 'DELETED')
;
