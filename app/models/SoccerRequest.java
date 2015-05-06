/*  Represents a request from the user to specify dates for the DB to search.
    Public functions:
        SoccerRequest - Initialize with a start and end date from form
        CheckDateRange - Check that date given is between start and end date
        Validate - Checks input dates against min, max, and checks for proper order
        GetStartDisplayString - Get formatted start date to display on form
        GetEndDisplayString - Get formatted end date to display on form
*/

package models;

import java.text.*;
import java.util.*;

import play.data.validation.ValidationError;

import play.data.validation.Constraints.*;
import play.db.ebean.Model;


// Defines a request containing optional start/end dates to define a range
public class SoccerRequest {
	private Calendar startDate; // actual date objects
	private Calendar endDate;
    private String DEFAULT_START_DATE ="2008-01-01";
	private String DEFAULT_END_DATE = "2014-06-01";
	
	
	public SoccerRequest(String startStringDate, String endStringDate) {
	    // Add defaults if left blank
	    if (startStringDate == null || startStringDate.isEmpty()) {
    	    startStringDate = DEFAULT_START_DATE;
    	}
    	if (endStringDate == null || endStringDate.isEmpty()) {
    	    endStringDate = DEFAULT_END_DATE;
    	}
	    startDate = resolveDate(startStringDate);
	    endDate = resolveDate(endStringDate);
	}
	
	// checks if input date is within the range given
	public boolean CheckDateRange(String inputDate) {
	    Calendar cal = resolveDate(inputDate);
	    return checkValidRange(cal, startDate, endDate);
	}
	
	// Validates that dates are between max and min dates, and that start date is not before end date.
	// Returns a list of ValidationErrors to be displayed on the form.
	public List<ValidationError> Validate() {
	    List<ValidationError> errors = new ArrayList<ValidationError>();
	    Calendar minDate = resolveDate(DEFAULT_START_DATE);
	    Calendar maxDate = resolveDate(DEFAULT_END_DATE);
	    
	    if (!checkValidRange(startDate, minDate, maxDate)) {
	        errors.add(new ValidationError("startDate","Start date must be between 1/1/2008 and 6/1/2014"));
	    }
	    
	    if (!checkValidRange(endDate, minDate, maxDate)) {
	        errors.add(new ValidationError("endDate","End date must be between 1/1/2008 and 6/1/2014"));
	    }
        
        if (startDate.after(endDate)) {
            errors.add(new ValidationError("dateOrder","Start date must come before the end date"));
        }
        
	    return errors;
	}
	
	// Format start date
	public String GetStartDisplayString() {
        return getFormattedDate(startDate);
	}
	
	
	// Format end date
	public String GetEndDisplayString() {
	    return getFormattedDate(endDate);
	}
	
	// Expects date as "YYYY-MM-DD".
	// If there is a parse error (we control the data so low risk of this) it will return
	// today's date, which is fine since it won't be counted as a valid game.
	private Calendar resolveDate(String stringDate) {
	    if (stringDate != null && !stringDate.isEmpty()) {
    	    GregorianCalendar cal = new GregorianCalendar();
    	    String[] datePieces = stringDate.split("-", 0);
    	    cal.set(Integer.parseInt(datePieces[0]), Integer.parseInt(datePieces[1])-1, Integer.parseInt(datePieces[2]));
    	    return cal;
	    }
	    return new GregorianCalendar();
	}
	
	// Checks that inputDate is between minDate and maxDate, inclusive
	private boolean checkValidRange(Calendar inputDate, Calendar minDate, Calendar maxDate) {
	    if (minDate.after(inputDate)) {
	        return false;
	    }
	    if (maxDate.before(inputDate)) {
	        return false;
	    }
	    return true;
	}
	
	// Formats the calendar object into MM/DD/YYYY format
	private String getFormattedDate(Calendar date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	    sdf.setCalendar(date);
	    return sdf.format(date.getTime());
	}
	
}
