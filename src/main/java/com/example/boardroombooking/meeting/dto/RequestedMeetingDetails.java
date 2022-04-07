package com.example.boardroombooking.meeting.dto;

import org.joda.time.DateTime;

public class RequestedMeetingDetails {
    private String empId;
    private DateTime requestDate;

    public String getEmpId() {
        return empId;
    }
    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public DateTime getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(DateTime requestDate) {
        this.requestDate = requestDate;
    }


}
