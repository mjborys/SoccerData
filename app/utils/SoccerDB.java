/*  Represents the soccer database. Keeps a map of countries and their points.
    Public functions:
        SoccerDB - Initialize with a SoccerRequest containing submitted dates
        FindHighestLowest - find the countries with highest and lowest points
*/
package utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import models.SoccerRequest;
import java.text.*;

public class SoccerDB {
	private Map<String, Country> Countries;
	
	private static final int WIN_SCORE = 3;
	private static final int LOSE_SCORE = 0;
	private static final int TIE_SCORE = 1;
	private static final String FILEPATH = "public\\results.csv";
	
	private Set<String> highest;
	private Set<String> lowest;
	private Integer highestPoints;
	private Integer lowestPoints;
	private SoccerRequest SR;
	
	
	public SoccerDB(SoccerRequest SR){
		Countries = new HashMap<String, Country>();
		this.SR = SR;
	}
	
	// Find countries with highest and lowest scores
	public void FindHighestLowest() {
	    highest = new HashSet<String>();
	    lowest = new HashSet<String>();
		highestPoints = new Integer(0);
		lowestPoints = new Integer(Integer.MAX_VALUE);
		Country country;
		
		// Read in data
		readCSV();
		
		// Loop once to discover the highest and lowest values
		for (Map.Entry<String, Country> entry : Countries.entrySet()) {
            country = entry.getValue();
			if (country.GetScore() > highestPoints) {
				highestPoints = country.GetScore();
			}
			if (country.GetScore() < lowestPoints) {
				lowestPoints = country.GetScore();
			}
		}
		
		// Loop again to find the countries with these values
		for (Map.Entry<String, Country> entry : Countries.entrySet()) {
		    country = entry.getValue();
			if (country.GetScore() == highestPoints) {
				highest.add(country.GetName());
			}
			if (country.GetScore() == lowestPoints) {
				lowest.add(country.GetName());
			}
		}
	}
	
	public Integer GetHighestPoints() {
	    return this.highestPoints;
	}
	
	public Integer GetLowestPoints() {
	    return this.lowestPoints;
	}
	
	public Set<String> GetHighestCountries() {
	    return this.highest;
	}
	
	public Set<String> GetLowestCountries() {
	    return this.lowest;
	}
	
	public SoccerRequest GetSoccerRequest() {
	    return this.SR;
	}
	
	// CSV file reading code (mostly copied from www.mkyong.com)
    // Since we don't need to store the data, just compute it as it is read in
	 private void readCSV(){
    	BufferedReader buff = null;
    	String line = "";
    	Boolean FirstLine = true; // Throw out first line since it's just headers (soccer pun!)
    	
    	try {
    		buff = new BufferedReader(new FileReader(FILEPATH));
    		while ((line = buff.readLine()) != null) {
    			if (!(FirstLine)) { 
    				handleGame(line);
    			}
				FirstLine = false;
    		}
    		
    	} catch (FileNotFoundException e) {
    		e.printStackTrace();
    	} catch (IOException e) {
    		e.printStackTrace();
    	} finally {
    		if (buff != null) {
    			try {
    				buff.close();
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
    		}
    	}
    }
	
	// Handles data for one game (one line in the CSV file)
	private void handleGame(String data) {
		String[] splitData;
		String home, away; // country names
		int homePoints, awayPoints;
		Integer homeScore, awayScore; // points awarded for game results
        
		
		// Parse data
		splitData = data.split(",");
		// ignore if the game is not in the date range
		if (SR.CheckDateRange(getDateFromCSV(splitData[0]))) {
    		home = splitData[1].replace("\"","");
    		away = splitData[2].replace("\"","");
    		homeScore = Integer.valueOf(splitData[3]);
    		awayScore = Integer.valueOf(splitData[4]);
    		
    		// Determine points
    		if (homeScore > awayScore) {
    			homePoints = WIN_SCORE;
    			awayPoints = LOSE_SCORE;
    		} 
    		else if (awayScore > homeScore) {
    			awayPoints = WIN_SCORE;
    			homePoints = LOSE_SCORE;
    		}
    		else { // draw
    			homePoints = TIE_SCORE;
    			awayPoints = TIE_SCORE;
    		}
    		
    		AddUpdateCountry(home, new Integer(homePoints));
    		AddUpdateCountry(away, new Integer(awayPoints));
		}
	}

	//updates country with points given. Creates country if not in hashmap yet
	private void AddUpdateCountry(String name, Integer points) {
	    Country country = Countries.get(name);
	    
	    if (country == null) {
	        country = new Country(name);
	    }
	    country.UpdateScore(points);
        Countries.put(name, country);
	}
    
    // Get the date from the "Date Time" input and remove the quotes from the date
    private String getDateFromCSV(String inputDate) {
        return  inputDate.split(" ")[0].replace("\"", "");
    }
}
