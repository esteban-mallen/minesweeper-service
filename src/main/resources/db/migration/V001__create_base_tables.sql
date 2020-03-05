CREATE TABLE `games`
(
    `id`            bigint(11) unsigned NOT NULL AUTO_INCREMENT,
    `created_on`    datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `last_modified` datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `column_count`  bigint(11) unsigned NOT NULL,
    `row_count`     bigint(11) unsigned NOT NULL,
    `mine_count`    bigint(11) unsigned NOT NULL,
    `status`        varchar(20)         NOT NULL DEFAULT 'NEW',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `cells`
(
    `id`              bigint(11) unsigned NOT NULL AUTO_INCREMENT,
    `created_on`      datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `last_modified`   datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `game_id`         bigint(11) unsigned,
    `row_position`    bigint(11) unsigned NOT NULL,
    `column_position` bigint(11) unsigned NOT NULL,
    `is_mine`         bit(1)              NOT NULL DEFAULT 0,
    `status`          varchar(20)         NOT NULL DEFAULT 'HIDDEN',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
