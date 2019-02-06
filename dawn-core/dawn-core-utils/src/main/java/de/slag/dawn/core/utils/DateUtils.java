package de.slag.dawn.core.utils;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
	
	public static Calendar toCalendar(LocalDate ld) {
		return toCalendar(toDate(ld));
	}

	public static Calendar toCalendar(final Date date) {
		final Calendar instance = Calendar.getInstance();
		instance.setTime(date);
		return instance;
	}

	public static Date toDate(final LocalDateTime localDateTime) {
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}

	public static Date toDate(final LocalDate localDate) {
		return toDate(LocalDateTime.of(localDate, LocalTime.MIDNIGHT));
	}

	public static LocalDate toLocalDate(final Date date) {
		if (date == null) {
			return null;
		}
		return toLocalDateTime(date).toLocalDate();
	}

	public static LocalDateTime toLocalDateTime(final Date date) {
		if (date == null) {
			return null;
		}
		return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
	}

}
