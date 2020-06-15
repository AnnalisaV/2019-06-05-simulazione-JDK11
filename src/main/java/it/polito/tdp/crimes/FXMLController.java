/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.crimes;

import java.net.URL;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.zip.DataFormatException;

import it.polito.tdp.crimes.model.DistrictVicino;
import it.polito.tdp.crimes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxAnno"
    private ComboBox<Integer> boxAnno; // Value injected by FXMLLoader

    @FXML // fx:id="boxMese"
    private ComboBox<Integer> boxMese; // Value injected by FXMLLoader

    @FXML // fx:id="boxGiorno"
    private ComboBox<Integer> boxGiorno; // Value injected by FXMLLoader

    @FXML // fx:id="btnCreaReteCittadina"
    private Button btnCreaReteCittadina; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="txtN"
    private TextField txtN; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaReteCittadina(ActionEvent event) {

    	txtResult.clear();
    	
    	if (this.boxAnno.getValue()==null) {
    		txtResult.appendText("ERRORE : selezionare un anno\n");
    		return; 
    	}
    	model.creaGrafo(this.boxAnno.getValue());
    	txtResult.appendText("Grafo creato!\n");
    	
    	txtResult.appendText("District collegati : \n");
    	for (Integer i : model.getVertex()) {
    		txtResult.appendText("\n Vicini del District : "+i+"\n");
    		for (DistrictVicino v : model.getVicini(i)) {
    			txtResult.appendText(v.toString()+"\n");
    		}
    	}
    	
    	//pulizia e riempimento boxes 
    	this.boxMese.getItems().removeAll(this.boxMese.getItems()); 
    	this.boxMese.getItems().addAll(model.getMonth(this.boxAnno.getValue())); 
    	this.boxGiorno.getItems().removeAll(this.boxGiorno.getItems()); 
    	this.boxGiorno.getItems().addAll(model.getDays(this.boxAnno.getValue())); 
    	
    	this.btnSimula.setDisable(false);
    }

    @FXML
    void doSimula(ActionEvent event) {

    	txtResult.clear();
    	int n= -1; 
    	if (txtN.getText().length()==0 ) {
    		txtResult.appendText("ERRORE inserire un numero di agenti compreso fra 1-10!\n");
    		return; 
    	}
    	try {
    		n= Integer.parseInt(this.txtN.getText());  
    	}catch(NumberFormatException nfe) {
    		txtResult.appendText("ERRORE inserire un numero di agenti compreso fra 1-10!\n");
    		return; 
    	}
    	
    	if (n<1 || n>10) {
    		txtResult.appendText("ERRORE inserire un numero di agenti compreso fra 1-10!\n");
    		return; 
    	}
    	
    	if (this.boxAnno.getValue()==null ||this.boxMese.getValue()==null || this.boxGiorno.getValue()==null) {
    		txtResult.appendText("ERRORE : selezionare la data\n");
    		return; 
    	}
    	
    	//ulteriore controllo sul fatto che la data sia corretta, p.ex 30 Feb non andrebbe bene 
    	try {
    		LocalDate.of(this.boxAnno.getValue(), this.boxMese.getValue(), this.boxGiorno.getValue()); 
    	}catch(DateTimeException dte) {
    		txtResult.appendText("ERRORE : La data inserita non e' corretta");
    		return; 
    	}
    	txtResult.appendText("Simulazione per "+txtN.getText()+" agenti\n\n");
    	txtResult.appendText("Crimes mal gestiti "+model.simula(this.boxAnno.getValue(), this.boxMese.getValue(), this.boxGiorno.getValue(), n)); 
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxGiorno != null : "fx:id=\"boxGiorno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaReteCittadina != null : "fx:id=\"btnCreaReteCittadina\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtN != null : "fx:id=\"txtN\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	this.boxAnno.getItems().addAll(model.getYears());
    	this.btnSimula.setDisable(true);
    }
}
