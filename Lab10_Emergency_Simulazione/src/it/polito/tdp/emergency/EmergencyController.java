package it.polito.tdp.emergency;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.emergency.db.FieldHospitalDAO;
import it.polito.tdp.emergency.simulation.Core;
import it.polito.tdp.emergency.simulation.Dottore;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class EmergencyController {
	
	Core core=new Core();
	FieldHospitalDAO dao= new FieldHospitalDAO();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField TxtD;

    @FXML
    private TextField txtO;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnSim;

    @FXML
    private TextArea txtResult;

    @FXML
    void doAdd(ActionEvent event) {
    	
    	int id=0;
    	
    	Dottore d=new Dottore(id,TxtD.getText(),((Integer.parseInt(txtO.getText())/60)));
    	d.setStato(Dottore.StatoDottore.IN_PAUSA);
    	core.aggiungiDottore(id, d);
  
    	txtResult.appendText("Dottore aggiunto");
    	
    	id++;
    }

    @FXML
    void doSim(ActionEvent event) {
    	dao.getPazienti();
    	dao.getArrivi();
    	core.simula();
    	txtResult.appendText(core.getPazientiPersi()+"\n"+core.getPazientiSalvati());
    }

    @FXML
    void initialize() {
        assert TxtD != null : "fx:id=\"TxtD\" was not injected: check your FXML file 'Emergency.fxml'.";
        assert txtO != null : "fx:id=\"txtO\" was not injected: check your FXML file 'Emergency.fxml'.";
        assert btnAdd != null : "fx:id=\"btnAdd\" was not injected: check your FXML file 'Emergency.fxml'.";
        assert btnSim != null : "fx:id=\"btnSim\" was not injected: check your FXML file 'Emergency.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Emergency.fxml'.";

    }
}

