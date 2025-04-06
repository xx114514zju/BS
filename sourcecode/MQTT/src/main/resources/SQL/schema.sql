use mqtt;
DROP TABLE IF EXISTS message;
DROP TABLE IF EXISTS device;
DROP TABLE IF EXISTS user;
CREATE TABLE user (
 id INT AUTO_INCREMENT PRIMARY KEY,
 username VARCHAR(128) UNIQUE,
 password VARCHAR(128) CHECK (LENGTH(password) >= 6),
 email VARCHAR(128) UNIQUE
);
CREATE TABLE device (
 id INT PRIMARY KEY AUTO_INCREMENT,
 name VARCHAR(128),
 description VARCHAR(128),
 userid INT,
 kind INT NOT NULL CHECK (kind IN (1, 2, 3, 4, 5)),
 activate VARCHAR(128) DEFAULT '',
 FOREIGN KEY (userid) REFERENCES user(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE message (
id INT PRIMARY KEY AUTO_INCREMENT,
device_id INT,
device_name VARCHAR(128),
alert INT,
info VARCHAR(128),
lat NUMERIC(8,2),
lng NUMERIC(8,2),
stamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
value INT,
FOREIGN KEY (device_id) REFERENCES device(id) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO user (id, username, password, email) VALUES
  (1, 'user1', 'zxcvbn', 'user1@example.com'),
  (2, 'user2', 'password2', 'sada'),
  (3, 'user3', 'zxcvbn', 'ss'),
  (4, 'user4', 'password4', 'user4@example.com'),
  (5, 'user5', 'password5', 'user5@example.com'),
  (6, 'user6', 'password6', 'user6@example.com'),
  (7, 'user7', 'password7', 'user7@example.com'),
  (8, 'user8', 'password8', 'user8@example.com'),
  (9, 'user9', 'password9', 'user9@example.com'),
  (10, 'user10', 'password10', 'user10@example.com'),
  (11, '98', '589744', '2'),
  (15, '94988', '649864', '25'),
  (16, 'zhangsan', '1234568', 'zhangsan.com'),
  (18, 'zhangsan1', '1234568', 'zhangsan.com1'),
  (20, 'xxjtcsm', 'xlsdb212', '1564549374@qq.com'),
  (29, 'ssh', '114514', '1919810@abc.com'),
  (30, 'nmsl', 'qwerty', 'sunxiaochuan@114514.com'),
  (31, 'asd', 'asdadsd', 'sadas@qq.com'),
  (32, 'sadas', 'cfdwfwdf', 'sdasd@aqw.sd'),
  (33, 'sdgdfb', 'fdsfdgfegfd', 'fdvdfa@ddsfd.vs'),
  (34, 'cxy', '123456', '123456@qq.com'),
  (35, 'aced', 'qqwerwqe', 'aced@qq.com');


INSERT INTO device (name, description, userid, kind, activate)
VALUES
  ('Device 1', 'Description 1', 20, 1, ''),
  ('Device 2', 'Description 2', 2, 1, ''),
  ('Device 3', 'Description 3', 3, 1, ''),
  ('Device 4', 'Description 4', 4, 1, ''),
  ('Device 5', 'Description 5', 5, 1, ''),
  ('Device 6', 'Description 6', 6, 1, ''),
  ('Device 7', 'Description 7', 7, 1, ''),
  ('Device 8', 'Description 8', 8, 1, ''),
  ('Device 9', 'Description 9', 9, 1, ''),
  ('Device 10', 'Description 10', 10, 1, ''),
  ('Device 11', 'Description 11', 11, 1, ''),
  ('Device 12', 'Description 12', 15, 1, ''),
  ('Device 13', 'Description 13', 16, 1, ''),
  ('Device 14', 'Description 14', 18, 1, ''),
  ('de15', 'Description 15', 20, 2, 'now'),
  ('Device 16', 'Description 16', 29, 1, ''),
  ('Device 17', 'Description 17', 30, 1, ''),
  ('Device 18', 'Description 18', 30, 1, ''),
  ('Device 19', 'Description 19', 20, 1, 'now'),
  ('Device 20', 'Description 20', 20, 1, 'now'),
  ('Device 21', 'Description 21', 20, 1, 'now'),
  ('Device 22', 'Description 22', 20, 1, 'now'),
  ('Device23', 'Description for Device23', 20, 2, 'Activation code for Device23'),
  ('Device24', 'Description for Device24', 20, 2, 'now'),
  ('Device25', 'Description for Device24', 20, 2, 'now'),
  ('Device26', 'Description for Device26', 20, 2, 'Activation code for Device26'),
  ('Device27', 'Description for Device27', 20, 2, 'Activation code for Device27'),
  ('Device28', 'Description for Device28', 20, 3, 'now'),
  ('Device29', 'Description for Device29', 20, 3, 'Activation code for Device29'),
  ('Device30', 'Description for Device30', 20, 3, 'now'),
  ('Device31', 'Description for Device31', 20, 3, 'Activation code for Device31'),
  ('Device32', 'Description for Device32', 20, 3, 'Activation code for Device32'),
  ('Device33', 'Description for Device33', 20, 4, 'now'),
  ('Device34', 'Description for Device34', 20, 4, 'now'),
  ('Device35', 'Description for Device35', 20, 4, 'Activation code for Device35'),
  ('Device36', 'Description for Device36', 20, 4, 'now'),
  ('Device37', 'Description for Device37', 20, 4, 'Activation code for Device37'),
  ('dv-38', 'Description for Device38', 20, 1, 'Activation code for Device38'),
  ('Device39', 'Description for Device39', 20, 5, 'now'),
  ('Device40', 'Description for Device40', 20, 5, 'now'),
  ('Device41', 'Description for Device41', 20, 5, 'Activation code for Device41'),
  ('Device42', 'Des 42', 20, 1, 'now'),
  ('Device43', 'Des 43', 20, 1, ''),
  ('Device44', 'Des 44', 20, 1, 'now'),
  ('sadas', 'das', 20, 1, 'now'),
  ('sadqaa', 'das', 20, 1, 'now'),
  ('dessaq', 'no des', 20, 2, 'notnow'),
  ('sofa', 'this is a sofa', 20, 2, 'notnow');



INSERT INTO message (id, device_id, device_name,alert , info, lat, lng, stamp, value)
VALUES (3, 39, 'device39', 1,'Info for device 39', 77.00, 170.00, '2021-07-14 23:30:34', 5),
       (4, 39, 'device39',0, 'info for device 39', 43.00, 40.00, '2023-01-08 19:00:20', 70),
       (5, 39, 'device39', 1,'info for device 39', 33.00, 86.00, '2023-07-11 16:35:34', 25),
       (6, 39, 'device39', 0,'information', 33.50, 86.20, '2023-08-18 00:00:00', 37),
       (7, 39, 'device39', 0,'information', 34.00, 86.70, '2023-08-18 00:00:01', 37),
       (8, 39, 'device39', 1,'information', 34.50, 87.20, '2023-08-18 00:00:02', 37),
       (9, 39, 'device39', 0,'information', 35.00, 87.70, '2023-08-18 00:00:03', 37),
       (10, 39, 'device39',0, 'information', 35.50, 88.20, '2023-08-18 00:00:04', 37),
       (11, 39, 'device39', 0,'information', 36.00, 88.70, '2023-08-18 00:00:05', 37),
       (12, 39, 'device39', 0,'information', 36.50, 89.20, '2023-08-18 00:00:06', 37),
       (13, 39, 'device39', 0,'information', 37.00, 89.70, '2023-08-18 00:00:07', 37),
       (14, 39, 'device39', 0,'information', 37.50, 90.20, '2023-08-18 00:00:08', 37),
       (15, 39, 'device39', 0,'information', 38.00, 90.70, '2023-08-18 00:00:09', 37),
       (16, 38, 'dv-38', 0,'information', 56.00, 120.00, '2023-11-06 18:08:00', 44),
       (17, 38, 'dv-38', 0,'information', 56.50, 120.50, '2023-11-06 18:08:01', 44),
       (18, 38, 'dv-38', 0,'information', 57.00, 121.00, '2023-11-06 18:08:02', 44),
       (19, 38, 'dv-38', 0,'information', 57.50, 121.50, '2023-11-06 18:08:03', 44),
       (20, 38, 'dv-38', 0,'information', 58.00, 122.00, '2023-11-06 18:08:04', 44),
       (21, 38, 'dv-38', 0,'information', 58.50, 122.50, '2023-11-06 18:08:05', 44),
       (22, 38, 'dv-38', 0,'information', 59.00, 123.00, '2023-11-06 18:08:06', 44),
       (23, 38, 'dv-38', 0,'information', 59.50, 123.50, '2023-11-06 18:08:07', 44),
       (24, 38, 'dv-38', 0,'information', 60.00, 124.00, '2023-11-06 18:08:08', 44),
       (25, 38, 'dv-38', 0,'information', 60.50, 124.50, '2023-11-06 18:08:09', 44),
       (26, 38, 'dv-38', 0,'information', 61.00, 125.00, '2023-11-06 18:08:10', 44),
       (27, 38, 'dv-38', 0,'information', 61.50, 125.50, '2023-11-06 18:08:11', 44),
       (28, 38, 'dv-38', 0,'information', 62.00, 126.00, '2023-11-06 18:08:12', 44),
       (29, 38, 'dv-38', 0,'information', 62.50, 126.50, '2023-11-06 18:08:13', 44),
       (30, 38, 'dv-38', 0,'information', 63.00, 127.00, '2023-11-06 18:08:14', 44),
       (31, 38, 'dv-38', 0,'information', 63.50, 127.50, '2023-11-06 18:08:15', 44),
       (33, 1, 'Device 1', 1,'info1', 80.50, 71.60, '2023-11-08 14:28:00', 50);

