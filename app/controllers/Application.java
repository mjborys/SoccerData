/*  Application code, just handles request from the form and runs the SoccerDB 
*/
package controllers;

import java.util.*;
import play.*;
import play.mvc.*;
import play.api.Routes;
import play.data.*;
import play.data.Form;
import utils.SoccerDB;
import models.SoccerRequest;
import views.html.*;
import views.html.form.*;
import play.data.validation.ValidationError;

import java.lang.reflect.InvocationTargetException;

public class Application extends Controller {
    
    public static Result PrintHighestLowest() {
        // Get date request from form 
    	DynamicForm requestData = Form.form().bindFromRequest();
    	String startDate = requestData.get("StartDateString");
    	String endDate = requestData.get("EndDateString");
    	
    	// Validate the dates
    	SoccerRequest SR = new SoccerRequest(startDate, endDate);
    	List<ValidationError> errors = SR.Validate();
        
        // Find the data
    	SoccerDB soccerDB = new SoccerDB(SR);
        soccerDB.FindHighestLowest();
    	return ok(form.render(soccerDB, errors));
    }
}
