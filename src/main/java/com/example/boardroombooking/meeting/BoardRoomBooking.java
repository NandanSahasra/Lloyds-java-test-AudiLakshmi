package com.example.boardroombooking.meeting;

import com.example.boardroombooking.meeting.dto.Meeting;
import com.example.boardroombooking.meeting.dto.OfficeCalender;
import com.example.boardroombooking.meeting.schduler.MeetingScheduler;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Map;
import java.util.Set;

public class BoardRoomBooking {

    private DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("yyyy-MM-dd");
    private DateTimeFormatter timeFormatter = DateTimeFormat.forPattern("HH:mm");

    private MeetingScheduler meetingScheduler;

    public BoardRoomBooking(MeetingScheduler meetingScheduler) {
        this.meetingScheduler = meetingScheduler;
    }

    public String bookBatchMeeting(String meetingRequest){
        OfficeCalender meetingsScheduleBooked = meetingScheduler.createBookingMeetingRequests(meetingRequest);

        return buildMeetingScheduleString(meetingsScheduleBooked);

    }

    private String buildMeetingScheduleString(OfficeCalender meetingsScheduleBooked) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<LocalDate, Set<Meeting>> meetingEntry : meetingsScheduleBooked.getMeetings().entrySet()) {
            LocalDate meetingDate = meetingEntry.getKey();
            sb.append(dateFormatter.print(meetingDate)).append("\n");
            Set<Meeting> meetings = meetingEntry.getValue();
            for (Meeting meeting : meetings) {
                sb.append(timeFormatter.print(meeting.getMeetingStartTime())).append(" ");
                sb.append(timeFormatter.print(meeting.getMeetingEndTime())).append(" ");
                sb.append(meeting.getRequestedMeetingDetails().getEmpId()).append("\n");
            }

        }
        return sb.toString();
    }
}
