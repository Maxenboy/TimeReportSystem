use puss1302;
DROP TABLE activities;
DROP TABLE users;
DROP TABLE project_groups;
DROP TABLE time_reports;


--
-- Table structure for table `activities`
--

CREATE TABLE `activities` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `activity_nr` int(11) NOT NULL,
  `activity_type` varchar(1) NOT NULL,
  `time` int(11) NOT NULL,
  `time_report_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=14 ;

--
-- Dumping data for table `activities`
--

INSERT INTO `activities` (`id`, `activity_nr`, `activity_type`, `time`, `time_report_id`) VALUES
(1, 12, 'U', 10, 1),
(2, 12, 'U', 10, 2),
(3, 12, 'U', 300, 3),
(4, 100, 'A', 30, 4),
(5, 100, 'A', 30, 5),
(6, 100, 'A', 30, 6),
(7, 100, 'A', 30, 7),
(8, 100, 'A', 30, 8),
(9, 100, 'A', 30, 9),
(10, 100, 'A', 30, 10),
(11, 100, 'A', 30, 11),
(12, 100, 'A', 30, 12),
(13, 100, 'A', 30, 13);

-- --------------------------------------------------------

--
-- Table structure for table `project_groups`
--

CREATE TABLE `project_groups` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_name` varchar(10) NOT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '1',
  `start_week` int(11) NOT NULL,
  `end_week` int(11) NOT NULL,
  `estimated_time` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `project_name` (`project_name`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=5 ;

--
-- Dumping data for table `project_groups`
--

INSERT INTO `project_groups` (`id`, `project_name`, `active`, `start_week`, `end_week`, `estimated_time`) VALUES
(1, 'Test1', 1, 1, 10, 300),
(2, 'Test2', 1, 1, 10, 300),
(3, 'Test3', 0, 1, 10, 300),
(4, 'Test4', 1, 1, 10, 300);

-- --------------------------------------------------------

--
-- Table structure for table `time_reports`
--

CREATE TABLE `time_reports` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `week` int(11) NOT NULL,
  `signed` tinyint(1) NOT NULL DEFAULT '0',
  `user_id` int(11) NOT NULL,
  `project_group_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `week` (`week`,`user_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=14 ;

--
-- Dumping data for table `time_reports`
--

INSERT INTO `time_reports` (`id`, `week`, `signed`, `user_id`, `project_group_id`) VALUES
(1, 2, 1, 3, 0),
(2, 3, 0, 3, 0),
(3, 4, 0, 5, 0),
(4, 1, 0, 6, 0),
(5, 5, 0, 7, 0),
(6, 1, 0, 8, 0),
(7, 2, 0, 9, 0),
(8, 1, 0, 12, 0),
(9, 1, 0, 13, 0),
(10, 1, 0, 14, 0),
(11, 1, 0, 15, 0),
(12, 1, 0, 16, 0),
(13, 1, 0, 17, 0);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(10) NOT NULL,
  `password` varchar(6) NOT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '1',
  `role` int(11) NOT NULL DEFAULT '3',
  `project_group_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=20 ;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `password`, `active`, `role`, `project_group_id`) VALUES
(1, 'admin', 'adminp', 1, 1, NULL),
(2, 'admin2', 'qmhyoa', 1, 1, NULL),
(3, 'andsve', 'iyxfnn', 1, 2, 1),
(4, 'petlin', 'hnaqqg', 0, 2, 1),
(5, 'hennil', 'dcfpuy', 1, 5, 1),
(6, 'rikols', 'ipwnnt', 1, 4, 1),
(7, 'ulrand', 'nflpkv', 1, 6, 1),
(8, 'andher', 'jprfgj', 1, 8, 1),
(9, 'tomsve', 'mhsxur', 1, 7, 1),
(10, 'nilson', 'atqgls', 1, 3, NULL),
(11, 'perand', 'jjqmhh', 0, 3, NULL),
(12, 'karsve', 'jepwvx', 1, 2, 2),
(13, 'idakor', 'fqxkdk', 1, 5, 2),
(14, 'josref', 'mrobls', 1, 4, 2),
(15, 'hyrpol', 'famkry', 1, 6, 2),
(16, 'underl', 'awumke', 1, 8, 2),
(17, 'svesve', 'pppngo', 1, 7, 2),
(18, 'yvokar', 'ymifts', 0, 2, 3),
(19, 'narema', 'vaoipj', 0, 2, 4);