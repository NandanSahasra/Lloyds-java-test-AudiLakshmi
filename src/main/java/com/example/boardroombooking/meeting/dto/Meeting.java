package com.example.boardroombooking.meeting.dto;

import org.joda.time.Interval;
import org.joda.time.LocalTime;

import java.util.Objects;


public class Meeting implements Comparable<Meeting> {

	private RequestedMeetingDetails requestedMeetingDetails;

	private LocalTime meetingStartTime;

	private LocalTime meetingEndTime;

	public Meeting(RequestedMeetingDetails requestedMeetingDetails, LocalTime meetingStartTime, LocalTime meetingEndTime) {
		this.requestedMeetingDetails = requestedMeetingDetails;
		this.meetingStartTime = meetingStartTime;
		this.meetingEndTime = meetingEndTime;
	}

	public RequestedMeetingDetails getRequestedMeetingDetails() {
		return requestedMeetingDetails;
	}

	public LocalTime getMeetingStartTime() {
		return meetingStartTime;
	}

	public LocalTime getMeetingEndTime() {
		return meetingEndTime;
	}

	public int compareTo(Meeting that) {
		Interval meetingInterval = new Interval(meetingStartTime.toDateTimeToday(),
				meetingEndTime.toDateTimeToday());
		Interval toCompareMeetingInterval = new Interval(that.getMeetingStartTime()
				.toDateTimeToday(), that.getMeetingEndTime().toDateTimeToday());
		if (meetingInterval.overlaps(toCompareMeetingInterval)) {
			return 0;
		} else {
			return  -1;
		}

	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Meeting meeting = (Meeting) o;
		return Objects.equals(requestedMeetingDetails, meeting.requestedMeetingDetails) && Objects.equals(meetingStartTime, meeting.meetingStartTime) && Objects.equals(meetingEndTime, meeting.meetingEndTime);
	}

	@Override
	public int hashCode() {
		return Objects.hash(requestedMeetingDetails, meetingStartTime, meetingEndTime);
	}
}
