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
--. What is the cost of the costliest software development in Basicselect dcost from software where dcost=(select max(dcost) from software where developin='BASIC') and developin='BASIC'--Display details of Packages whose sales crossed the 2000 Mark.SELECT * FROM SOFTWARE WHERE (SOLD*SCOST)>20000;--Who are the Programmers who celebrate their Birthdays during the Current Month?select pname from programmer where month(dob)=month(convert(date,GETDATE()))--Display the Cost of Package Developed By each Programmer.SELECT PNAME,SUM(DCOST) AS TOTAL_COST FROM SOFTWARE GROUP BY PNAME;--. Display the sales values of the Packages Developed by each ProgrammerSELECT PNAME, SUM(SCOST*SOLD) FROM SOFTWARE GROUP BY PNAME--Display the Number of Packages sold by Each Programmer.SELECT pname as name,COUNT(TITLE) as NO_of_PACK FROM SOFTWARE GROUP BY pname--Display each programmer’s name, costliest and cheapest Packages Developed by him or her.select pname,max(dcost) as costlies,min(dcost) as cheapest from software group by pname --Display each institute name with the number of Courses, Average Cost per Courseselect institute,count(course) as total_course ,avg(course_fee) as avg_fee from studies group by institute--Display each institute Name with Number of Students.select count(pname) as no_of_students , institute from studies group by institute--. List the programmers (form the software table) and the institutes they studied.select distinct sw.pname,s.institute from software as sw left join studies as s on sw.pname=s.pname--How many packages were developed by students, who studied in institute that charge the lowest course fee?select count(*) from software where pname in (select pname from studies where course_fee=(select min(course_fee) from studies))--What is the AVG salary for those whose software sales is more than 50,000/-.select avg(salary) from programmer where pname in (select pname name from software group by pname having sum(sold*scost)>50000)--Which language listed in prof1, prof2 has not been used to develop any package.select prof1 from programmer where prof1 not in (select developin from software)unionselect prof2 from programmer where prof2 not in (select developin from software)--Display the total sales value of the software, institute wise.select s.institute ,sum(scost*sold) as sold from software as sw join studies as s on sw.pname=s.pname group by s.institute -- Display the details of the Software Developed in C By female programmers of Pragathi.select * from programmer as p join software as sw on sw.pname=p.pname join studies as s on s.pname=sw.pname where developin='C' and institute='PRAGATHI' and gender='F'-- Display the details of the packages developed in Pascal by the Female Programmersselect * from programmer as p join software as sw on sw.pname=p.pname where developin='PASCAL'  and gender='F'--Which language has been stated as the proficiency by most of the Programmers?SELECT PROF1 FROM PROGRAMMER GROUP BY PROF1 HAVING PROF1 = (SELECT MAX(PROF1) FROM PROGRAMMER)UNIONSELECT PROF2 FROM PROGRAMMER GROUP BY PROF2 HAVING PROF2 = (SELECT MAX(PROF2) FROM PROGRAMMER);--Who is the Author of the Costliest Package?select pname from software where dcost=(select max(dcost) from software)--Which package has the Highest Development cost?select title from software where dcost=(select max(dcost) from software)
--Who is the Highest Paid Female COBOL Programmer?
SELECT * FROM PROGRAMMER WHERE SALARY=(SELECT MAX(SALARY) FROM PROGRAMMER WHERE (PROF1 LIKE 'COBOL' OR PROF2 LIKE 'COBOL')) AND GENDER LIKE 'F'

--Display the Name of Programmers and Their Packages.
select p.pname,sw.title as package from programmer as p left join software as sw on sw.pname=p.pname

--Display the Number of Packages in Each Language Except C and C++.select count(*) as total_package,developin as no_of_packages from software group by developin having developin not in ('C','CPP')

--Display AVG Difference between SCOST, DCOST for Each Package.select Abs(avg(scost)-avg(dcost)) from software group by title

--Display the total SCOST, DCOST and amount to Be Recovered for each Programmer for Those Whose Cost has not yet been Recovered.select scost , dcost , abs(dcost -(scost*sold)) as amount_to_be_recoverd from software where dcost -(scost*sold) <0

--Who is the Highest Paid C Programmers?select pname from programmer where salary = (select max(salary) from programmer where prof1 ='C' or prof2='C')

--Who is the Highest Paid Female COBOL Programmer?
select pname from programmer where salary = (select max(salary) from programmer where prof1 ='COBOL' or prof2='COBOL' and gender='F')and gender='F'


