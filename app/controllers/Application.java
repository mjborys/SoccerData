/*  Application code, just handles request from the form and runs the SoccerDB 
*/
package controllers;

import java.util.*;
import play.mvc.*;
import play.data.*;
import play.data.Form;
import utils.SoccerDB;
import models.SoccerRequest;
import views.html.*;
import play.data.validation.ValidationError;

public class Application extends Controller {
	
	public static Result printHighestLowest() {
		// Get date request from form 
		DynamicForm requestData = Form.form().bindFromRequest();
		String startDate = requestData.get("StartDateString");
		String endDate = requestData.get("EndDateString");
		// Validate the dates
		SoccerRequest SR = new SoccerRequest(startDate, endDate);
		List<ValidationError> errors = SR.validate();       
		// Find the data
		SoccerDB soccerDB = new SoccerDB(SR);
		if (!soccerDB.findHighestLowest()){  
			errors.add(new ValidationError("CSVIssue", "Could not find or read CSV"));
		}
		return ok(form.render(soccerDB, errors));
	}
}
