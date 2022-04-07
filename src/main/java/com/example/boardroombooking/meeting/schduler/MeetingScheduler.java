package com.example.boardroombooking.meeting.schduler;

import com.example.boardroombooking.meeting.dto.Meeting;
import com.example.boardroombooking.meeting.dto.OfficeCalender;
import com.example.boardroombooking.meeting.dto.RequestedMeetingDetails;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.joda.time.*;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.*;

import static java.lang.Integer.parseInt;

public class MeetingScheduler {

    private DateTimeFormatter dateFormatter = DateTimeFormat
            .forPattern("yyyy-MM-dd");
    private DateTimeFormatter separatedTimeFormatter = DateTimeFormat
            .forPattern("HH:mm");
    private DateTimeFormatter dateTimeFormatter = DateTimeFormat
            .forPattern("yyyy-MM-dd HH:mm:ss");
    private static Logger logger = LogManager.getLogger(MeetingScheduler.class);


    public OfficeCalender createBookingMeetingRequests(String meetingRequest) {

        if (meetingRequest == null || meetingRequest.isEmpty()) {
            logger.error("Employee booking request is not a valid input");
            return null;
        }
        try {
            String[] requestLines = meetingRequest.split("\n");
            String[] officeHoursTokens = requestLines[0].split(" ");

            LocalTime officeStartTime = new LocalTime(
                    parseInt(officeHoursTokens[0].substring(0, 2)),
                    parseInt(officeHoursTokens[0].substring(2, 4)));
            LocalTime officeCloseTime = new LocalTime(
                    parseInt(officeHoursTokens[1].substring(0, 2)),
                    parseInt(officeHoursTokens[1].substring(2, 4)));

            Map<LocalDate, Set<Meeting>> meetings = new HashMap<LocalDate, Set<Meeting>>();

            for (int i = 1; i < requestLines.length; i = i + 2) {
                if (requestLines.length == 2) {
                    return null;
                }
                String[] meetingSlotRequest = requestLines[i + 1].split(" ");
                LocalDate meetingDate = dateFormatter
                        .parseLocalDate(meetingSlotRequest[0]);

                Meeting meeting = createMeeting(requestLines[i],
                        officeStartTime, officeCloseTime, meetingSlotRequest);
                if (meeting != null) {
                    if (meetings.containsKey(meetingDate)) {
                        RequestedMeetingDetails requestedMeetingDetails = meeting.getRequestedMeetingDetails();
                        DateTime requestDateTime = requestedMeetingDetails.getRequestDate();
                        Set<Meeting> createdMeetings = meetings.get(meetingDate);
                        DateTimeComparator dateTimeComparator = DateTimeComparator.getInstance();
                        Interval currentMeetingInterval = new Interval(meeting.getMeetingStartTime().toDateTimeToday(),
                                (meeting.getMeetingEndTime().toDateTimeToday()));
                        for(Meeting m : createdMeetings){
                            Interval existingMeetingInterval = new Interval(m.getMeetingStartTime()
                                    .toDateTimeToday(), m.getMeetingEndTime().toDateTimeToday());
                            if(!currentMeetingInterval.overlaps(existingMeetingInterval)){
                                continue;
                            }
                            int compareResult = dateTimeComparator.compare(m.getRequestedMeetingDetails().getRequestDate(), requestDateTime);
                            if(compareResult>0){
                                meetings.get(meetingDate).remove(meeting);
                                meetings.get(meetingDate).add(meeting);
                            }
                        }
                        createdMeetings.add(meeting);
                        meetings.put(meetingDate,createdMeetings);


                    } else {
                        Set<Meeting> meetingsForDay = new TreeSet<Meeting>();
                        meetingsForDay.add(meeting);
                        meetings.put(meetingDate, meetingsForDay);
                    }
                }
            }
            return new OfficeCalender(officeStartTime, officeCloseTime,
                    meetings);
        } catch (Exception e) {
            logger.info("Exception:::" + e.getMessage());
            return null;
        }

    }

    private Meeting createMeeting(String requestLine,
                                  LocalTime officeStartTime, LocalTime officeFinishTime,
                                  String[] meetingSlotRequest) {
        String[] meetingRequestDetails = requestLine.split(" ");
        RequestedMeetingDetails requestedMeetingDetails = new RequestedMeetingDetails();
        DateTime requestedDateTime = dateTimeFormatter
                .parseDateTime(meetingRequestDetails[0]+" "+meetingRequestDetails[1]);
        requestedMeetingDetails.setRequestDate(requestedDateTime);
        requestedMeetingDetails.setEmpId(meetingRequestDetails[2]);

        LocalTime meetingStartTime = separatedTimeFormatter
                .parseLocalTime(meetingSlotRequest[1]);
        LocalTime meetingFinishTime = new LocalTime(
                meetingStartTime.getHourOfDay(),
                meetingStartTime.getMinuteOfHour())
                .plusHours(Integer.parseInt(meetingSlotRequest[2]));

        if (meetingTimeOutsideOfficeHours(officeStartTime, officeFinishTime,
                meetingStartTime, meetingFinishTime)) {
            logger.info("EmployeeId:: " + requestedMeetingDetails.getEmpId()
                    + " has requested booking which is outside office hour.");
            return null;
        } else {
            return new Meeting(requestedMeetingDetails, meetingStartTime, meetingFinishTime);

        }
    }

    private boolean meetingTimeOutsideOfficeHours(LocalTime officeStartTime,
                                                  LocalTime officeFinishTime, LocalTime meetingStartTime,
                                                  LocalTime meetingFinishTime) {
        return meetingStartTime.isBefore(officeStartTime)
                || meetingStartTime.isAfter(officeFinishTime)
                || meetingFinishTime.isAfter(officeFinishTime)
                || meetingFinishTime.isBefore(officeStartTime);
    }
}
