package it.polito.tdp.crimes.db;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.crimes.model.Event;



public class EventsDao {
	
	public List<Event> listAllEvents(){
		String sql = "SELECT * FROM events" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Event> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	/**
	 * Anno presente nel db
	 * @return
	 */
	public List<Integer> getYears(){
		String sql="SELECT DISTINCT YEAR(reported_date) AS year " + 
				"FROM EVENTS " + 
				"ORDER BY YEAR asc ";
		List<Integer> lista= new ArrayList<>(); 
		
		try {
			Connection conn = DBConnect.getConnection() ;
            PreparedStatement st = conn.prepareStatement(sql) ;
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				lista.add(res.getInt("year")); 
			}
			conn.close();
			return lista ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
		
	}
	
	public List<Integer> getVertex(){
		String sql="SELECT DISTINCT district_id AS vertex " + 
				"FROM EVENTS ";
	List<Integer> lista= new ArrayList<>(); 
		
		try {
			Connection conn = DBConnect.getConnection() ;
            PreparedStatement st = conn.prepareStatement(sql) ;
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				lista.add(res.getInt("vertex")); 
			}
			conn.close();
			return lista ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
		
		
	            
	}

	public Double getLatMedia(Integer year, Integer v) {
		String sql="SELECT AVG(geo_lat) as lat " + 
				"FROM EVENTS " + 
				"WHERE district_id=? AND YEAR(reported_date)=?"; 

		Double lat=-1.1; 
		try {
			Connection conn = DBConnect.getConnection() ;
            PreparedStatement st = conn.prepareStatement(sql) ;
            st.setInt(1,  v);
            st.setInt(2,  year);
			ResultSet res = st.executeQuery() ;
			
			if(res.first()) {
				lat= res.getDouble("lat"); 
				
			}
			conn.close();
			return lat ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
		
	}

	public Double getLonMedia(Integer year, Integer v) {
		String sql="SELECT AVG(geo_lon) as lon " + 
				"FROM EVENTS " + 
				"WHERE district_id=? AND YEAR(reported_date)=?"; 
		Double lon=-1.1; 
		try {
			Connection conn = DBConnect.getConnection() ;
            PreparedStatement st = conn.prepareStatement(sql) ;
            st.setInt(1,  v);
            st.setInt(2,  year);
			ResultSet res = st.executeQuery() ;
			
			if(res.next()) {
				lon= res.getDouble("lon"); 
				
			}
			conn.close();
			return lon ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Integer> getMonths(Integer year){
		String sql="SELECT DISTINCT MONTH(reported_date) AS mese " + 
				"FROM EVENTS " + 
				"WHERE YEAR(reported_date)=? " + 
				"ORDER BY mese asc"; 
		List<Integer> lista= new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection() ;
            PreparedStatement st = conn.prepareStatement(sql) ;
            st.setInt(1,  year);
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				lista.add(res.getInt("mese")); 
			}
			conn.close();
			return lista ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}

	
	public List<Integer> getDays(Integer year){
		String sql="SELECT DISTINCT Day(reported_date) AS gg " + 
				"FROM EVENTS " + 
				"WHERE YEAR(reported_date)=? " + 
				"ORDER BY gg asc"; 
		List<Integer> lista= new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection() ;
            PreparedStatement st = conn.prepareStatement(sql) ;
            st.setInt(1,  year);
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				lista.add(res.getInt("gg")); 
			}
			conn.close();
			return lista ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}

	public int getDistrictMinCrime(int year) {
		String sql="SELECT district_id, COUNT(*) AS crimes " + 
				"FROM EVENTS " + 
				"WHERE YEAR(reported_date)=? " + 
				"GROUP BY district_id " + 
				"ORDER BY crimes ASC " + 
				"LIMIT 1";  //limto il risultato ma dopo averlo ordinato
		
		int min= -1; 
		
		try {
			Connection conn = DBConnect.getConnection() ;
            PreparedStatement st = conn.prepareStatement(sql) ;
            st.setInt(1,  year);
			ResultSet res = st.executeQuery() ;
			
			if(res.next()) {
				min=res.getInt("district_id");  
				
			}
			conn.close();
			

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		}
		return min; 
	}
	
	public List<Event> eventsByDate(int year, int month, int day){
		String sql = "SELECT * FROM events "
				+ "WHERE YEAR(reported_date)=? "
				+ "AND MONTH(reported_date)=? "
				+ "AND DAY(reported_date)=? " ;
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, year);
			st.setInt(2, month);
			st.setInt(3, day);
			
			List<Event> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
}
