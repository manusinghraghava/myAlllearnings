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

--How many Programmers Don’t know PASCAL and Cselect count(pname) from programmer as p where (prof1 not in ('PASCAL','C') and prof2 not in ('PASCAL','C'))--Display the details of those who don’t know Clipper, COBOL or PASCALSELECT * FROM PROGRAMMER WHERE PROF1 NOT IN ('CLIPPER','COBOL','PASCAL') AND PROF2 NOT IN ('CLIPPER','COBOL','PASCAL');--Display each language name with AVG Development Cost, AVG Selling Cost and AVG Price per CopySELECT DEVELOPIN,AVG(DCOST),AVG(SCOST),AVG(SCOST) FROM SOFTWARE GROUP BY DEVELOPIN;--. List the programmer names (from the programmer table) and No. Of Packages each has developed.select p.pname as name,count(s.title) as noOfPackageDeveloped  from programmer as p left join software as s on s.pname=p.pname  group by p.pname--List each PROFIT with the number of Programmers having that PROF and the number of the packages in that PROF.select count(*),sum(scost*sold-dcost) "PROFIT"
from software
where developin in (select prof1
from programmer) group by developin;--How many packages are developed by the most experienced programmer form BDPS.select sw.title,p.pname from programmer as p join studies as s on s.pname=p.pname join software as sw on sw.pname=s.pname where s.institute='BDPS' and DATEDIFF(day, p.doj,convert(date,getdate())) =(select max(DATEDIFF(day, doj,convert(date,getdate()))) from programmer where s.institute='BDPS')--How many packages were developed by the female programmers earning more than the highest paid male programmer?select count(*) from programmer as p join software as sw on sw.pname=p.pname where p.gender='F' and p.salary> (select max(salary) from programmer where gender='M')--How much does the person who developed the highest selling package earn and what course did HE/SHE undergo.select p.salary,s.course from programmer as p join software as sw on sw.pname=p.pname join studies as s on s.pname=p.pname and sw.scost=(select max(scost) from software)--In which institute did the person who developed the costliest package study?select s.institute from programmer as p join software as sw on sw.pname=p.pname join studies as s on s.pname=p.pname and sw.scost=(select max(scost) from software)--Display the names of the programmers who have not developed any packages.select p.pname  from programmer as p left join software as sw on sw.pname=p.pname where sw.pname is null--Display the details of the software that has developed in the language which is neither the first nor the second proficiencyselect * from software where developin not in (select prof1 from programmer) and developin not in (select prof2 from programmer)--Display the details of the software Developed by the male programmers Born before 1965 and female programmers born after 1975select * from software as sw where sw.pname in (select p.pname from programmer as p where ((p.gender='M' and year(p.dob)<1965) or (p.gender='F' and year(p.dob)>1975)) and p.pname=sw.pname)--Display the number of packages, No. of Copies Sold and sales value of each programmer institute wise.Select studies.institute, count(software.developin), count(software.sold), sum(software.sold*software.scost) from software,studies where software.pname=studies.pname group by studies.institute; --Display the details of the Software Developed by the Male Programmers Earning More than 3000select * from software as sw where sw.pname in (select p.pname from programmer as p where (p.gender='M' and p.salary>3000) and p.pname=sw.pname)--Who are the Female Programmers earning more than the Highest Paid male?select pname from programmer where gender ='F' and salary > (select max(salary) from programmer where gender ='M')--Who are the male programmers earning below the AVG salary of Female Programmers?select pname from programmer where gender ='M' and salary <(select avg(salary) from programmer where gender ='F')--Display the language used by each programmer to develop the Highest Selling and Lowest-selling package.
SELECT PNAME, DEVELOPIN FROM SOFTWARE WHERE SOLD IN (SELECT MAX(SOLD) FROM SOFTWARE GROUP BY PNAME) or SOLD IN (SELECT MIN(SOLD) FROM SOFTWARE GROUP BY PNAME);

-- Display the names of the packages, which have sold less than the AVG number of copies
select title from software where sold < (select avg(sold) from software)

--Which is the costliest package developed in PASCAL.
select title from software as s where developin ='PASCAL' and s.dcost = (select max(dcost) from software where developin ='PASCAL')

--How many copies of the package that has the least difference between development and selling cost were sold.
select sold,title from software where (dcost-scost) = (select min (dcost-scost) from software)

--Which language has been used to develop the package, which has the highest sales amount?select developin from software where scost=(select max(scost) from software)--Who Developed the Package that has sold the least number of copies?select pname from software where sold = (select min(sold) from software)--Display the names of the courses whose fees are within 1000 (+ or -) of the Average Feeselect sw.course from studies as sw where sw.course_fee between (select avg(course_fee)-1000 from studies) and (select avg(course_fee)+1000 from studies)--Display the name of the Institute and Course, which has below AVG course fee.select sw.course,sw.institute from studies as sw where course_fee < (select avg(course_fee) from studies)--Which Institute conducts costliest course.select institute from studies where course_fee = (select max(course_fee) from studies)--What is the Costliest course?select max(course_fee) from studies