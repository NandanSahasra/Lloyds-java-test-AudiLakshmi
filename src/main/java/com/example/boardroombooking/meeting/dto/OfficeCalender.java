package com.example.boardroombooking.meeting.dto;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.Map;
import java.util.Set;

public class OfficeCalender {

	private LocalTime officeStartTime;

	private LocalTime officeCloseTime;

	private Map<LocalDate, Set<Meeting>> meetings;

	public OfficeCalender(LocalTime officeStartTime,
						  LocalTime officeCloseTime, Map<LocalDate, Set<Meeting>> meetings) {
		this.officeStartTime = officeStartTime;
		this.officeCloseTime = officeCloseTime;
		this.meetings = meetings;
	}

	public LocalTime getOfficeStartTime() {
		return officeStartTime;
	}

	public LocalTime getOfficeCloseTime() {
		return officeCloseTime;
	}

	public Map<LocalDate, Set<Meeting>> getMeetings() {
		return meetings;
	}

}
