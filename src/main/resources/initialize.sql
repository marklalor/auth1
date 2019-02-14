CREATE TABLE IF NOT EXISTS `User` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`username` VARCHAR(255) NOT NULL,
	`password` VARCHAR(255) NOT NULL,
	`totp_secret` BINARY(128) DEFAULT NULL,
	`email` VARCHAR(255) DEFAULT NULL,
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
	`client_ip` VARCHAR(255) DEFAULT NULL,
	`client_id` VARCHAR(255) DEFAULT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `LoginToken` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`user_id` INT NOT NULL,
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
	`issue_time` DATETIME NOT NULL,
	`expiration_time` DATETIME NOT NULL,
	PRIMARY KEY (`id`),
    FOREIGN KEY (`user_id`) REFERENCES User(`id`)
);

CREATE TABLE IF NOT EXISTS `PasswordResetToken` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`user_id` INT NOT NULL,
	`issue_time` DATETIME NOT NULL,
	`expiration_time` DATETIME NOT NULL,
	PRIMARY KEY (`id`),
    FOREIGN KEY (`user_id`) REFERENCES User(`id`)
);

CREATE TABLE IF NOT EXISTS `PasswordlessLoginToken` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`user_id` INT NOT NULL,
	`issue_time` DATETIME NOT NULL,
	`expiration_time` DATETIME NOT NULL,
	PRIMARY KEY (`id`),
    FOREIGN KEY (`user_id`) REFERENCES User(`id`)
);