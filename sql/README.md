# Introduction
In this project, I created a database called ```exercises```. Then, I created a scheme called ```cd```. This scheme has 3 tables: ```cd members```, ```cd.bookings```, and ```cd.facilities```. Then I exercised different sql queries to select, insert, delete, or update data fields, covering all CRUD operations.

# SQL Queries
I practiced different SQL queries falling under the following categories: Modifying Data, Basics, Join, Aggregation, and String.

### Modifying Data

##### Question 1: Insert a row into cd.facilities

```sql
INSERT INTO cd.facilities
            (facid,
             NAME,
             membercost,
             guestcost,
             initialoutlay,
             monthlymaintenance)
VALUES      (9,
             'Spa',
             20,
             30,
             100000,
             800)
```

####  Question 2: Insert using auto increment of facid.

```sql
INSERT INTO cd.facilities
            (facid,
             NAME,
             membercost,
             guestcost,
             initialoutlay,
             monthlymaintenance)
SELECT (SELECT Max (facid)
        FROM   cd.facilities)
       + 1,
       'Spa',
       20,
       30,
       100000,
       800; 
```

#### Question 3: Update the data in cd.facilities for 'Tennis Court 2'

```sql
UPDATE cd.facilities
SET    initialoutlay = 10000
WHERE  initialoutlay = 8000; 
```

#### Questionn 4: Update the data of 'Tennis Court 2' based on the data of 'Tennis Court 1'

```sql
UPDATE cd.facilities
SET    guestcost = 1.1 * (SELECT guestcost
                          FROM   cd.facilities
                          WHERE  facid = 0),
       membercost = 1.1 * (SELECT membercost
                           FROM   cd.facilities
                           WHERE  facid = 0)
WHERE  facid = 1; 
```

#### Question 5: Delete all bookings from cd.bookings

```sql
DELETE FROM cd.bookings;
```

#### Question 6: Delete a member from cd.members

```sql
DELETE FROM cd.members 
WHERE memid = 37;
```


### Basics

#### Question 1: Control which tables are  retrieved from cd.facilities

```sql
SELECT facid,
       NAME,
       membercost,
       monthlymaintenance
FROM   cd.facilities
WHERE  membercost = 35; 
``` 

#### Question 2: Show a list of facilities with the word 'Tennis'

```sql
SELECT facid,
       NAME,
       membercost,
       guestcost,
       initialoutlay,
       monthlymaintenance
FROM   cd.facilities
WHERE  NAME LIKE '%Tennis%'; 
```

#### Question 3: Retrieve the details of facilities with ID 1 and 5 from cd.facilities

```sql
SELECT *
FROM   cd.facilities
WHERE  facid IN ( 1, 5 ); 
```

#### Question 4: Produce a list of members who joined after the start of September 2012

```sql
SELECT memid,
       surname,
       firstname,
       joindate
FROM   cd.members
WHERE  joindate >= '2012-09-01'; 
```

#### Question 5: Produced a combied listt of all surnames and all facility names

```sql
SELECT surname
FROM   cd.members
UNION
SELECT NAME
FROM   cd.facilities;
```

### Join

#### Question 1: Retrieve the start times of members' bookings

```sql
SELECT bks.starttime
FROM   cd.bookings bks
       INNER JOIN cd.members mems
               ON mems.memid = bks.memid
WHERE  mems.firstname = 'David'
       AND mems.surname = 'Farrell'; 
```

#### Question 2: View all of the start times for bookings for tennis coutts for the date '2012-09-21'
```sql
SELECT bks.starttime AS start,
       facs.NAME
FROM   cd.facilities facs
       INNER JOIN cd.bookings bks
               ON facs.facid = bks.facid
WHERE  facs.NAME LIKE '%Tennis Court%'
       AND bks.starttime >= '2012-09-21'
       AND bks.starttime < '2012-09-22'
ORDER  BY bks.starttime; 
```

#### Question 3: Produce a list of all members with their recommender

```sql
SELECT mems.firstname AS memfname,
       mems.surname   AS memsname,
       recs.firstname AS recfname,
       recs.surname   AS recsname
FROM   cd.members mems
       LEFT OUTER JOIN cd.members recs
                    ON mems.recommendedby = recs.memid
ORDER  BY memsname,
          memfname;
```

#### Question 4: Produce a list of all members who have recommended another member

```sql
SELECT DISTINCT recs.firstname,
                recs.surname
FROM   cd.members mems
       INNER JOIN cd.members recs
               ON recs.memid = mems.recommendedby
ORDER  BY recs.surname,
          recs.firstname; 
```

#### Question 5: Produce a list of all members, along with their recommender, using no joins

```sql
SELECT DISTINCT mems.firstname
                || ' '
                || mems.surname AS member,
                (SELECT recs.firstname
                        || ' '
                        || recs.surname AS recommender
                 FROM   cd.members recs
                 WHERE  recs.memid = mems.recommendedby)
FROM   cd.members mems
ORDER  BY member; 
```

### Aggregation

#### Question 1: Count the number of recommendations each member makes

```sql
SELECT recommendedby,
       Count(*)
FROM   cd.members
WHERE  recommendedby IS NOT NULL
GROUP  BY recommendedby
ORDER  BY recommendedby; 
```

#### Question 2: List the total slots booked per facility

```sql
SELECT facid,
       Sum(slots) AS "Total Slots"
FROM   cd.bookings
GROUP  BY facid
ORDER  BY facid; 
```

#### Question 3: List the total slots booked per facility in a given month

```sql

SELECT facid,
       SUM(slots) AS "Total Slots"
FROM   cd.bookings
WHERE  starttime >= '2012-09-01'
       AND starttime < '2012-10-01'
GROUP  BY facid
ORDER  BY "Total Slots"; 
```

#### Question 4: List the total slots booked per facility per month

```sql
SELECT facid,
       Extract(month FROM starttime) AS month,
       Sum(slots)                    AS "Total Slots"
FROM   cd.bookings
WHERE  Extract(year FROM starttime) = 2012
GROUP  BY facid,
          month
ORDER  BY facid,
          month; 
```

#### Question 5: Find the count of members who have made at least one booking

```sql
SELECT Count(DISTINCT memid)
FROM  cd.bookings;
```

#### Question 6: List each member's first booking after September 1st 2012

```sql
SELECT mems.surname,
       mems.firstname,
       mems.memid,
       Min (bks.starttime) AS starttime
FROM   cd.members mems,
       cd.bookings bks
WHERE  bks.starttime >= '2012-09-01'
       AND mems.memid = bks.memid
GROUP  BY mems.memid
ORDER  BY memid; 
```

#### Question 7: Produce a list of member names, with each row containing the total member count

```sql
SELECT Count(*)
         OVER(),
       firstname,
       surname
FROM   cd.members
ORDER  BY joindate; 
```

#### Question 8: Produce a numbered list of members

```sql
SELECT Row_number()
         OVER(
           ORDER BY joindate ),
       firstname,
       surname
FROM   cd.members
ORDER  BY joindate; 
```

#### Question 9: Output the facility id that has the highest number of slots booked, again

```sql
SELECT facid,
       total
FROM   (SELECT facid,
               Sum(slots)                     total,
               Rank()
                 OVER (
                   ORDER BY Sum(slots) DESC ) rank
        FROM   cd.bookings
        GROUP  BY facid) AS ranked
WHERE  rank = 1; 
```

### String

#### Question 1: Format the names of members

```sql
SELECT surname
       || ', '
       || firstname AS NAME
FROM   cd.members; 
```

#### Question 2: Find telephone numbers with parentheses

```sql
SELECT memid,
       telephone
FROM   cd.members
WHERE  telephone ~ '[()]'
ORDER  BY memid; 
```

#### Question 3: Count the number of members whose surname starts with each letter of the alphabet


```sql
SELECT Substr (surname, 1, 1) AS letter,
       Count(*)               AS count
FROM   cd.members
GROUP  BY letter
ORDER  BY letter; 
```
