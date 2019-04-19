CREATE TABLE IF NOT EXISTS `User` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`username` VARCHAR(255) NOT NULL UNIQUE,
	`password` VARCHAR(255) NOT NULL,
	`totp_secret` BINARY(80) DEFAULT NULL,
	`email` VARCHAR(255) DEFAULT NULL UNIQUE,
	`verified` BOOLEAN DEFAULT FALSE NOT NULL,
	`locked` BOOLEAN DEFAULT FALSE NOT NULL,
	`creation_time` DATETIME NOT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `LoginRecord` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`time` DATETIME NOT NULL,
	`username` VARCHAR(255) NOT NULL,
	`user_ip` VARCHAR(255) DEFAULT NULL,
	`user_agent` VARCHAR(255) DEFAULT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `UserAuthenticationToken` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`user_id` INT NOT NULL,
	`value` VARCHAR(255) NOT NULL,
	`issue_time` DATETIME NOT NULL,
	`expiration_time` DATETIME NOT NULL,
	`issued_ip` VARCHAR(255) DEFAULT NULL,
	`issued_user_agent` VARCHAR(255) DEFAULT NULL,
	PRIMARY KEY (`id`),
    FOREIGN KEY (`user_id`) REFERENCES User(`id`)
);

CREATE TABLE IF NOT EXISTS `UserVerificationToken` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`user_id` INT NOT NULL,
	`value` VARCHAR(255) NOT NULL,
	`issue_time` DATETIME NOT NULL,
	`expiration_time` DATETIME NOT NULL,
	PRIMARY KEY (`id`),
    FOREIGN KEY (`user_id`) REFERENCES User(`id`)
);

CREATE TABLE IF NOT EXISTS `PasswordResetToken` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`user_id` INT NOT NULL,
	`value` VARCHAR(255) NOT NULL,
	`issue_time` DATETIME NOT NULL,
	`expiration_time` DATETIME NOT NULL,
	PRIMARY KEY (`id`),
    FOREIGN KEY (`user_id`) REFERENCES User(`id`)
);

CREATE TABLE IF NOT EXISTS `PasswordlessLoginToken` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`user_id` INT NOT NULL,
	`value` VARCHAR(255) NOT NULL,
	`issue_time` DATETIME NOT NULL,
	`expiration_time` DATETIME NOT NULL,
	PRIMARY KEY (`id`),
    FOREIGN KEY (`user_id`) REFERENCES User(`id`)
);

CREATE TABLE IF NOT EXISTS `TentativeTOTPConfiguration` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`user_id` INT NOT NULL,
	`tentative_totp_secret` BINARY(80) NOT NULL,
	`expiration_time` DATETIME NOT NULL,
	UNIQUE (user_id),
	PRIMARY KEY (`id`),
	FOREIGN KEY (`user_id`) REFERENCES User(`id`)
);