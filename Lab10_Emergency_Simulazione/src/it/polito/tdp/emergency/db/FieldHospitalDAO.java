////////////////////////////////////////////////////////////////////////////////
//             //                                                             //
//   #####     // Field hospital simulator                                    //
//  ######     // (!) 2013 Giovanni Squillero <giovanni.squillero@polito.it>  //
//  ###   \    //                                                             //
//   ##G  c\   // Field Hospital DAO                                          //
//   #     _\  // Test with MariaDB 10 on win                                 //
//   |   _/    //                                                             //
//   |  _/     //                                                             //
//             // 03FYZ - Tecniche di programmazione 2012-13                  //
////////////////////////////////////////////////////////////////////////////////

package it.polito.tdp.emergency.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import it.polito.tdp.emergency.simulation.Core;
import it.polito.tdp.emergency.simulation.Evento;
import it.polito.tdp.emergency.simulation.Evento.TipoEvento;
import it.polito.tdp.emergency.simulation.Paziente;
import it.polito.tdp.emergency.simulation.Paziente.StatoPaziente;

public class FieldHospitalDAO {
	
	Core core=new Core();
	
	DBConnect connection= DBConnect.getInstance();

	public void getPazienti(){

		Connection conn= connection.getConnection();
				
		String sql= "SELECT id,triage FROM arrivals,patients;";
		
		Statement st;
		
		try {
			st=conn.createStatement();
			
			ResultSet res=st.executeQuery(sql);
			
			while(res.next()){
				
			int id=res.getInt("id");
            String en = res.getString("triage");
            
            switch(en){
            
            case "Red":
            	Paziente p = new Paziente (id, StatoPaziente.ROSSO);
            	core.aggiungiPaziente(p);
            	break;
            	
            case "White":
            	Paziente p1 = new Paziente (id, StatoPaziente.BIANCO);
            	core.aggiungiPaziente(p1);

            	break;
            	
            case "Green":
            	Paziente p2 = new Paziente (id, StatoPaziente.VERDE);
            	core.aggiungiPaziente(p2);

            	break;
            	
            case "Yellow":
            	Paziente p3 = new Paziente (id, StatoPaziente.GIALLO);
            	core.aggiungiPaziente(p3);
            	break;
            	
            default:
    	 ;
    	 }
			}
			
			conn.close();
			
			}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void getArrivi(){
		
		Connection conn= connection.getConnection();
		
		String sql= "SELECT timestamp,patient FROM arrivals;";
		
		Statement st;
		try {
			st=conn.createStatement();
			
			ResultSet res=st.executeQuery(sql);
			
			while(res.next()){
				
			long timestamp=(long )res.getTimestamp("timestamp").getTime()/(long) (Math.pow(10, -5)*1.67);
            int id = res.getInt("patient");
            
            Evento e=new Evento(timestamp,TipoEvento.PAZIENTE_ARRIVA,id);
            core.aggiungiEvento(e);
			}
			
			conn.close();
			
			}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
		
	

}
