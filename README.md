Processing Batches of booking requests for meeting

Problem Statement
=================
Booking rules
- No part of a meeting may fall outside of office hours (9:00 to 18:00).
- Meetings may not overlap.
- The booking submission system only allows one submission at a time, so submission
  times are guaranteed to be unique.
- Booking must be processed in the chronological order in which they were submitted.
- The ordering of booking submissions in the supplied input is not guaranteed.

Developer Notes
===============

This project can test in two ways 

Run the MeetingSchedulerTest.java file with Junit it covers all the business scenarios 

This functionality I planned to keep it into one service so we can start application as Spring Boot Application and 

Run request with POST method with the following URL

http://localhost:8080/bookmeetings

with sample request as bellow 

Note : Please make sure that no spaces in request before sending. 

![img_1.png](img_1.png)


Input
============

0900 1800



2016-07-18 10:17:06 EMP001



2016-07-21 09:00 2



2016-07-18 12:34:56 EMP002



2016-07-21 09:00 2



2016-07-18 09:28:23 EMP003



2016-07-22 14:00 2



2016-07-18 10:17:06 EMP004



2016-07-22 16:00 1


2016-07-15 17:29:12 EMP005



2016-07-21 16:00 3

Output
============

2016-07-22



16:00 17:00 EMP004


14:00 16:00 EMP003


2016-07-21


09:00 11:00 EMP001

