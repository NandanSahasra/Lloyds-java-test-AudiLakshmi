package com.example.boardroombooking.controller;

import com.example.boardroombooking.meeting.BoardRoomBooking;
import com.example.boardroombooking.meeting.schduler.MeetingScheduler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BoardRoomController {
    BoardRoomBooking boardRoomBooking;
    @PostMapping("/bookmeetings")
    String batchMeeting(@RequestBody  String request){
        boardRoomBooking =  new BoardRoomBooking(new MeetingScheduler());
        return boardRoomBooking.bookBatchMeeting(request);
    }
}
