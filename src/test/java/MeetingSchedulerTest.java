import com.example.boardroombooking.meeting.BoardRoomBooking;
import com.example.boardroombooking.meeting.dto.Meeting;
import com.example.boardroombooking.meeting.dto.OfficeCalender;
import com.example.boardroombooking.meeting.schduler.MeetingScheduler;
import org.joda.time.LocalDate;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MeetingSchedulerTest {
    String meetingRequest;

    MeetingScheduler meetingScheduler = new MeetingScheduler();
    BoardRoomBooking
            boardRoomBooking = new BoardRoomBooking(new MeetingScheduler());


    @Test
     public void shouldTestSingleEmployeeMeetingRequestForBoardRoom() {

         meetingRequest = "0900 1800\n"
                         + "2016-07-18 10:17:06 EMP001\n"
                         + "2016-07-21 09:00 2\n";
         OfficeCalender bookings = meetingScheduler.createBookingMeetingRequests(meetingRequest);
         LocalDate meetingDate = new LocalDate(2016, 07, 21);
         assertEquals(1, bookings.getMeetings().get(meetingDate).size());
         Meeting meeting = bookings.getMeetings().get(meetingDate)
                 .toArray(new Meeting[0])[0];
         assertAll(()-> assertEquals("EMP001", meeting.getRequestedMeetingDetails().getEmpId()),
                   ()-> assertEquals(9, meeting.getMeetingStartTime().getHourOfDay()),
                   ()-> assertEquals(0, meeting.getMeetingStartTime().getMinuteOfHour()),
                   ()-> assertEquals(11, meeting.getMeetingEndTime().getHourOfDay()),
                   ()-> assertEquals(0, meeting.getMeetingEndTime().getMinuteOfHour()));
     }
    @Test
     public void shouldTestOutOfOfficeHoursMeetingRequest(){

         meetingRequest = "0900 1800\n"
                          + "2016-07-15 18:29:12 EMP005\n"
                          + "2016-07-21 16:00 3\n";
         OfficeCalender bookings = meetingScheduler.createBookingMeetingRequests(meetingRequest);

         assertEquals(0, bookings.getMeetings().size());

     }
    @Test
    public void shouldProcessMeetingsInChronologicalOrderOfSubmission() {
        meetingRequest = "0900 1800\n"
                         + "2016-07-18 10:17:06 EMP001\n"
                         + "2016-07-21 09:00 2\n"
                         + "2016-07-18 12:34:56 EMP002\n"
                         + "2016-07-21 09:00 2\n";

        OfficeCalender bookings = meetingScheduler.createBookingMeetingRequests(meetingRequest);

        LocalDate meetingDate = new LocalDate(2016, 07, 21);

        assertEquals(1, bookings.getMeetings().get(meetingDate).size());
        Meeting meeting = bookings.getMeetings().get(meetingDate)
                .toArray(new Meeting[0])[0];
        assertAll(()-> assertEquals("EMP001", meeting.getRequestedMeetingDetails().getEmpId()),
                  ()->assertEquals(9, meeting.getMeetingStartTime().getHourOfDay()),
                  ()->assertEquals(0, meeting.getMeetingStartTime().getMinuteOfHour()),
                  ()->assertEquals(11, meeting.getMeetingEndTime().getHourOfDay()),
                  ()->assertEquals(0, meeting.getMeetingEndTime().getMinuteOfHour())
                 );
            }
    @Test
    public void shouldGroupMeetingsChronologically() {
        meetingRequest = "0900 1800\n"
                         + "2016-07-18 09:28:23 EMP003\n"
                         + "2016-07-22 14:00 2\n"
                         + "2016-07-18 11:23:45 EMP004\n"
                         + "2016-07-22 16:00 1\n";


        OfficeCalender bookings = meetingScheduler.createBookingMeetingRequests(meetingRequest);
        LocalDate meetingDate = new LocalDate(2016, 07, 22);

        assertEquals(1, bookings.getMeetings().size());
        assertEquals(2, bookings.getMeetings().get(meetingDate).size());
        Meeting[] meetings = bookings.getMeetings().get(meetingDate)
                .toArray(new Meeting[0]);

        assertAll(()-> assertEquals("EMP003", meetings[1].getRequestedMeetingDetails().getEmpId()),
                  ()->assertEquals(14, meetings[1].getMeetingStartTime().getHourOfDay()),
                  ()->assertEquals(0, meetings[1].getMeetingStartTime().getMinuteOfHour()),
                  ()->assertEquals(16, meetings[1].getMeetingEndTime().getHourOfDay()),
                  ()->assertEquals(0, meetings[1].getMeetingEndTime().getMinuteOfHour())
                 );
        assertAll(()-> assertEquals("EMP004", meetings[0].getRequestedMeetingDetails().getEmpId()),
                  ()->assertEquals(16, meetings[0].getMeetingStartTime().getHourOfDay()),
                  ()->assertEquals(0, meetings[0].getMeetingStartTime().getMinuteOfHour()),
                  ()->assertEquals(17, meetings[0].getMeetingEndTime().getHourOfDay()),
                  ()->assertEquals(0, meetings[0].getMeetingEndTime().getMinuteOfHour())
                  );
            }

    @Test
    public void shoudTestMeetingsShouldNotOverlap() {
        meetingRequest = "0900 1800\n"
                         + "2016-07-18 10:17:06 EMP001\n"
                         + "2016-07-21 09:00 2\n"
                         + "2016-07-18 12:34:56 EMP002\n"
                         + "2016-07-21 10:00 1\n";

        OfficeCalender bookings = meetingScheduler.createBookingMeetingRequests(meetingRequest);
        LocalDate meetingDate = new LocalDate(2016, 07, 21);

        assertEquals(1, bookings.getMeetings().size());
        assertEquals(1, bookings.getMeetings().get(meetingDate).size());
        Meeting[] meetings = bookings.getMeetings().get(meetingDate)
                .toArray(new Meeting[0]);
        assertAll(()-> assertEquals("EMP001", meetings[0].getRequestedMeetingDetails().getEmpId()),
                ()->assertEquals(9, meetings[0].getMeetingStartTime().getHourOfDay()),
                ()->assertEquals(0, meetings[0].getMeetingStartTime().getMinuteOfHour()),
                ()->assertEquals(11, meetings[0].getMeetingEndTime().getHourOfDay()),
                ()->assertEquals(0, meetings[0].getMeetingEndTime().getMinuteOfHour())
                );
           }

    @Test
    public void emptyInputDataShouldEndWithNull() {
        meetingRequest = null;
        OfficeCalender bookings = meetingScheduler.createBookingMeetingRequests(meetingRequest);
        assertEquals(null, bookings);
    }
    @Test
    public void invalidInputDataShouldEndWithNull() {
        meetingRequest = "0900 1800\n"
                        + "2016-07-18 10:17:06 EMP001\n";
        OfficeCalender bookings = meetingScheduler.createBookingMeetingRequests(meetingRequest);
        assertEquals(null, bookings);
    }
    @Test
    public void shouldPrintMeetingSchedule() {
        String meetingRequest = "0900 1800\n"
                              + "2016-07-18 10:17:06 EMP001\n"
                              + "2016-07-21 09:00 2\n"
                              + "2016-07-18 12:34:56 EMP002\n"
                              + "2016-07-21 09:00 2\n"
                              + "2016-07-18 09:28:23 EMP003\n"
                              + "2016-07-22 14:00 2\n"
                              + "2016-07-18 10:17:06 EMP004\n"
                              + "2016-07-22 16:00 1\n"
                              + "2016-07-15 18:29:12 EMP005\n"
                              + "2016-07-21 16:00 3\n";

        String actualoutput = boardRoomBooking.bookBatchMeeting(meetingRequest);

        String expectedOutput = "2016-07-22\n"
                                + "16:00 17:00 EMP004\n"
                                + "14:00 16:00 EMP003\n"
                                + "2016-07-21\n"
                                + "09:00 11:00 EMP001\n";
         assertEquals(actualoutput,expectedOutput);


    }

    @Test
    public void shouldTestOfficeHoursReadFromRequest() {
        meetingRequest = "0900 1730\n";
        OfficeCalender bookings = meetingScheduler.createBookingMeetingRequests(meetingRequest);
        assertAll(()->assertEquals(bookings.getOfficeStartTime().getHourOfDay(), 9),
                ()-> assertEquals(bookings.getOfficeStartTime().getMinuteOfHour(), 0),
                ()->assertEquals(bookings.getOfficeCloseTime().getHourOfDay(), 17),
                ()->assertEquals(bookings.getOfficeCloseTime().getMinuteOfHour(), 30)
                );
       }


}
