drop database if exists sql_assignment
create database sql_assignment
use sql_assignment
--creating table for assignment
drop table if exists programmer 
drop table if exists software
drop table if exists studies
create table programmer
(
pname varchar(30),
dob date,
doj date,
gender varchar(1),
prof1 varchar(20),
prof2 varchar(20),
salary int,
constraint pk_programmer primary key(pname)
)

create table studies
(
pname varchar(30),
institute varchar(20),
course varchar(20),
course_fee int,
constraint fk_studies_refrences_programmer foreign key(pname) references programmer(pname)
)

create table software
(
pname varchar(30),
title varchar(30),
developin varchar(30),
scost int,
dcost int,
sold int,
constraint fk_software_refrences_programmer foreign key(pname) references programmer(pname)
)

--inserting value in table

insert into programmer
values
('ANAND','1966-04-12','1992-04-21','M','PASCAL','BASIC',3200        ),
('ALTAF','1964-07-02','1990-11-13','M','CLIPPER','COBOL',2800       ),
('JULIANA','1960-01-31','1990-04-21','F','COBOL','DBASE',3000          ),
('KAMALA', '1968-10-30','1992-01-02','F','C','DBASE',2900              ),
('MARY', '1970-06-24','1991-02-01','F','CPP','ORACLE',4500             ),
('NELSON', '1985-09-11','1989-10-11','M','COBOL','DBASE',2500          ),
('PATTRICK','1965-11-10','1990-04-21','M','PASCAL','CLIPPER',2800      ),
('QADIR', '1965-08-31','1991-04-21','M','ASSEMBLY','C',3000            ),
('RAMESH', '1967-05-03','1991-02-28','M','PASCAL','DBASE',3200         ),
('REBECCA', '1967-01-01','1990-12-01','F','BASIC','COBOL',2500         ),
('REMITHA', '1970-04-19','1993-04-20','F','C','ASSEMBLY',3600          ),
('REVATHI', '1969-12-02','1992-01-02','F','PASCAL','BASIC',3700        ),
('VIJAYA', '1965-12-14','1992-05-02','F','FOXPRO','C',3500             )

insert into studies
values
('ANAND','SABHARI','PGDCA',4500),
('ALTAF','COIT','DCA',7200),
('JULIANA','BDPS','MCA',22000),
('KAMALA','PRAGATHI','DCA',5000),
('MARY','SABHARI','PGDCA',4500),
('NELSON','PRAGATHI','DAP',6200),
('PATTRICK','PRAGATHI','DCAP',5200),
('QADIR','APPLE','HDCA',14000),
('RAMESH','SABHARI','PGDCA',4500),
('REBECCA','BRILLIANT','DCAP',11000),
('REMITHA','BDPS','DCS',6000),
('REVATHI','SABHARI','DAP',5000),
('VIJAYA','BDPS','DCA',48000)

insert into software
values
('MARY','README','CPP',300,1200,84),
('ANAND','PARACHUTES','BASIC',399.95,6000,43),
('ANAND','VIDEO TITLING','PASCAL',7500,16000,9),
('JULIANA','INVENTORY','COBOL',3000,3500,0),
('KAMALA','PAYROLL PKG.','DBASE',9000,20000,7),
('MARY','FINANCIAL ACCT.','ORACLE',18000,85000,4),
('MARY','CODE GENERATOR','C',4500,20000,23),
('PATTRICK','README','CPP',300,1200,84),
('QADIR','BOMBS AWAY','ASSEMBLY',750,3000,11),
('QADIR','VACCINES','C',1900,3100,21),
('RAMESH','HOTEL MGMT.','DBASE',13000,35000,4),
('RAMESH','DEAD LEE','PASCAL',599.95,4500,73),
('REMITHA','PC UTILITIES','C',725,5000,51),
('REMITHA','TSR HELP PKG.','ASSEMBLY',2500,6000,7),
('REVATHI','HOSPITAL MGMT.','PASCAL',1100,75000,2),
('VIJAYA','TSR EDITOR','C',900,700,6)


/*
all the answers begin
*/
--Display the names of the highest paid programmers for each Language
select * from programmer
;with cte_highest_paid_programmer
as
(
SELECT DISTINCT PNAME, SALARY, PROF1 FROM PROGRAMMER WHERE (SALARY) IN (SELECT MAX(SALARY) FROM PROGRAMMER GROUP BY PROF1)
union
SELECT DISTINCT PNAME, SALARY, PROF2 FROM PROGRAMMER WHERE (SALARY) IN (SELECT MAX(SALARY)FROM PROGRAMMER GROUP BY PROF2)
)
select DISTINCT(PNAME) from cte_highest_paid_programmer

--Display the details of those who are drawing the same salary
select * from programmer where salary in (select salary from programmer group by salary having count(salary)>1) order by salary asc

-- Who are the programmers who joined on the same day?select pname,DATENAME(weekday,doj) as day from programmer where DATENAME(weekday,doj) in (select DATENAME(weekday,doj) from programmer group by DATENAME(weekday,doj) having count(DATENAME(weekday,doj))>1)--Who are the programmers who have the same Prof2?select pname,prof2 from programmer where prof2 in (select prof2 from programmer group by prof2 having count(prof2)>1) order by prof2 asc--How many packages were developed by the person who developed the cheapest package,where did he/she study?select count(*) from  software as sw join studies as s on sw.pname=s.pname where sw.dcost=(select min(dcost) from software) 
