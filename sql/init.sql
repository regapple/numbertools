
create database torder  DEFAULT CHARACTER SET utf8 ;

CREATE USER torder@'%' IDENTIFIED BY 'torder';

GRANT ALL PRIVILEGES ON torder.* TO torder;

use torder;

create table trader_order(
	id 		int  not null primary key  AUTO_INCREMENT,
	name 	varchar(200),
	owner 	varchar(200),
	tid 	varchar(200),
	mobile  varchar(50),
	phone 	varchar(50),
	shop_name varchar(500),
	shop_link varchar(500),
	category varchar(200),
	shop_rank varchar(200),
	address  varchar(500),
	email  	varchar(100),
	epay_id  varchar(100),
	qq      varchar(50),
	source  varchar(100),
	shop_type varchar(100),
	sex     	varchar(10),
	import_date varchar(30)
	
) default charset=utf8;