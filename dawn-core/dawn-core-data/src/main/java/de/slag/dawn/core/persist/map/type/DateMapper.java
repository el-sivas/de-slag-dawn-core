package de.slag.dawn.core.persist.map.type;

import java.time.LocalDate;
import java.util.Date;
import java.util.function.Function;

import de.slag.root.base.DateUtils;

public class DateMapper {

	public static final Function<Date, LocalDate> DATE_TO_LOCAL_DATE = date -> DateUtils.toLocalDate(date);
	
	public static final Function<LocalDate, Date> LOCAL_DATE_TO_DATE = localDate -> DateUtils.toDate(localDate);

}
