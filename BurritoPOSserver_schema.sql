USE neatoburrito $$

delimiter $$

CREATE TABLE `User` (
  `id` int(11) DEFAULT NULL,
  `groupid` int(11) DEFAULT NULL,
  `username` varchar(128) DEFAULT NULL,
  `password` varchar(128) DEFAULT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (groupid) REFERENCES `Group`(id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1$$

delimiter $$

CREATE TABLE `Group` (
  `id` int(11) DEFAULT NULL,
  `name` varchar(128) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1$$

