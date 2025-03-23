--creating table for assignment
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

--What is the Highest Number of copies sold by a Package?select max(sold) from software--Display lowest course Fee.select min(course_fee) from studies--How old is the Oldest Male Programmer.select 'oldest male programmer age is :',year(GETDATE()) - year(min(dob)) from programmer where gender='M'--What is the AVG age of Female Programmers?select 'average age of female programmer is : ',AVG(year(GETDATE()) - year(dob)) from programmer where gender='F'--Calculate the Experience in Years for each Programmer and Display with their names in Descending order.select pname as name , 'experience for '+pname+' is : '+cast((year(GETDATE()) - year(doj)) as varchar(20)) from programmer-- How many programmers have done the PGDCA Course?select count(*) from programmer as p join studies as s on s.pname=p.pname where course ='PGDCA'--How much revenue has been earned thru sales of Packages Developed in C.select sum(scost*sold) from software where developin='c'--How many Programmers Studied at Sabhari?select count(*) from programmer as p join studies as s on s.pname=p.pname where institute ='SABHARI'--How many Packages Developed in DBASE?select count(*) from software where developin='DBASE'--How many programmers studied in Pragathi?select count(*) from programmer as p join studies as s on s.pname=p.pname where institute ='PRAGATHI'--How many Programmers Paid 5000 to 10000 for their course?select count(*) from programmer as p join studies as s on s.pname=p.pname where course_fee between 5000 and 10000-- How many Programmers know either COBOL or PASCAL?select count(*) from programmer where prof1 in ('COBOL','PASCAL') or prof2 in ('COBOL','PASCAL')--How many Female Programmers are there?select count(*) from programmer where gender='F' --What is the AVG Salary?select avg(salary) from programmer--How many people draw salary 2000 to 4000?select count(*) from programmer where salary between 2000 and 4000--Display the sales cost of the packages Developed by each Programmer Language wiseselect pname,scost,developin from software order by developin asc--Display the details of the software developed by the male students of Sabhari.select * from programmer as p join studies as s on s.pname=p.pname join software as sw on sw.pname=p.pname where p.gender='M' and s.institute='SABHARI'--Who is the oldest Female Programmer who joined in 1992?select min(dob) from programmer where gender='F' and year(doj)=1992--Who is the youngest male Programmer born in 1965?select pname from programmer where gender='M' and dob =(select max(dob) from programmer)--Which Package has the lowest selling cost?select * from software where scost=(select min(scost) from software)--Which Female Programmer earning more than 3000 does not know C, C++,ORACLE or DBASE?select pname from programmer where salary >3000 and gender='F' and (prof1 not in ('C','CPP','ORACLE','DBASE') or prof2 not in ('C','CPP','ORACLE','DBASE') )--Who is the Youngest Programmer knowing DBASE?select * from programmer where prof1='DBASE' or prof2='DBASE' and dob=(select max(dob) from programmer)--Which Language is known by only one Programmer?select prof1 from programmer group by prof1 having count(prof1)=1 and prof1 not in (select prof2 from programmer group by prof2)unionselect prof2 from programmer group by prof2 having count(prof2)=1 and prof2 not in (select prof1 from programmer group by prof1)--Who is the least experienced Programmer? select * from programmer where doj=(select max(doj) from programmer)--Display the Number of Packages in Each Language for which Development Cost is less than 1000.select count(title),developin from software  where dcost<10000 group by developin--Display Highest, Lowest and Average Salaries for those earning more than 2000.select max(salary) as [max],min(salary) as [min] , avg(salary) as [avg] from programmer where salary>2000