CREATE TABLE kickr_user (
  id bigint NOT NULL,
  created datetime NOT NULL,
  email varchar(255) NOT NULL,
  name varchar(255) not null,
  password varchar(255) NOT NULL,
  permissions integer NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY UNIQUE_kickr_user_name (name),
  UNIQUE KEY UNIQUE_kickr_user_email (email)
) ENGINE=InnoDB;

CREATE TABLE kickr_access_token (
  id bigint NOT NULL,
  created datetime NOT NULL,
  valid_until datetime,
  value varchar(255) NOT NULL,
  user_id bigint,
  PRIMARY KEY (id),
  UNIQUE KEY UNIQUE_kickr_access_token_value (value),
  CONSTRAINT FK_kickr_access_token_user_id FOREIGN KEY (user_id) REFERENCES kickr_user (id) ON DELETE CASCADE
) ENGINE=InnoDB;


INSERT INTO kickr_user (id, created, email, name, password, permissions) VALUES (1, NOW(), 'admin@kickr', 'admin', 'nLpk/3e0jLn/3njOjwL+Vy4TtjXSx6iNKpbUZuYiRv/2m/hpfZKWj9WdDmK8nb4u', 6);