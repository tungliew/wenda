DROP TABLE IF EXISTS wenda.user;
CREATE TABLE `wenda`.`user` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(64) NOT NULL DEFAULT '',
  `password` VARCHAR(128) NOT NULL DEFAULT ' ',
  `salt` VARCHAR(32) NOT NULL DEFAULT '',
  `head_url` VARCHAR(256) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = '这是一个用户表';

DROP TABLE IF EXISTS wenda.question;
CREATE TABLE `wenda`.`question` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(255) NOT NULL,
  `content` TEXT(1000) NULL,
  `user_id` INT NOT NULL,
  `created_date` DATETIME NOT NULL,
  `comment_count` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `data_index` (`created_date` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = 'question table';

DROP TABLE IF EXISTS wenda.login_ticket;
CREATE TABLE `wenda`.`login_ticket` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `ticket` VARCHAR(45) NOT NULL,
  `expired` DATETIME NOT NULL,
  `status` INT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `ticket_UNIQUE` (`ticket` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = 'login-ticket table';


DROP TABLE IF EXISTS wenda.comment;
CREATE TABLE `wenda`.`comment` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `content` TEXT(1000) NOT NULL,
  `user_id` INT NOT NULL,
  `entity_id` INT NOT NULL,
  `entity_type` INT NOT NULL,
  `created_date` DATETIME NOT NULL,
  `status` INT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  INDEX `entity_index` (`entity_id` ASC, `entity_type` ASC))
COMMENT = 'comment table';


DROP TABLE IF EXISTS wenda.message;
CREATE TABLE `wenda`.`message` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `from_id` INT NULL,
  `to_id` INT NULL,
  `content` TEXT(1000) NULL,
  `created_date` DATETIME NULL,
  `has_read` INT NULL,
  `conversation_id` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `conversation_index` (`conversation_id` ASC),
  INDEX `created_date` (`created_date` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = 'message table';


DROP TABLE IF EXISTS wenda.seed;
CREATE TABLE `wenda`.`seed` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `created_date` DATETIME NULL,
  `user_id` INT NULL,
  `data` TINYTEXT NULL,
  `type` INT NULL,
  PRIMARY KEY (`id`),
  INDEX `user_index` (`user_id` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = 'seed table';




