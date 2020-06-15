package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	
	
	private EventsDao dao; 
	private Graph<Integer,DefaultWeightedEdge> graph; 
	
	public Model() {
		this.dao= new EventsDao(); 
	}
	
	public List<Integer> getYears(){
		return this.dao.getYears(); 
	}
	
	public void creaGrafo(Integer year) {
		this.graph= new SimpleWeightedGraph<>(DefaultWeightedEdge.class); 
		
		//vertex
		Graphs.addAllVertices(this.graph, this.dao.getVertex()); 
		
		//edges 
		// avendo pochi vertex posso fare il doppio ciclo for 
		for (Integer v1 : this.graph.vertexSet()) {
			for(Integer v2 : this.graph.vertexSet()) {
				
				if (!v1.equals(v2)) {
					if (this.graph.getEdge(v1, v2)==null) {
						//aggiungo arco altrimenti c'e' gia' ed essendo non orientato va bene così
						Double latMediaV1= dao.getLatMedia(year, v1); 
						Double latMediaV2= dao.getLatMedia(year, v2);
						
						Double lonMediaV1= dao.getLonMedia(year, v1);
						Double lonMediaV2= dao.getLonMedia(year, v2);
						                                                      //centro V1
						Double distanzaMedia= LatLngTool.distance(new LatLng(latMediaV1, lonMediaV1), 
								new LatLng(latMediaV2, lonMediaV2), LengthUnit.KILOMETER); 
						
						Graphs.addEdge(this.graph, v1, v2, distanzaMedia); 
						
					}
				}
			}
		}
		
		System.out.println("Grafo creato \n"); 
		System.out.println(this.graph.vertexSet().size()+" vertex and "+this.graph.edgeSet().size()+" edges\n"); 
	}
	
	public List<DistrictVicino> getVicini(Integer district){
		List<DistrictVicino> vicini= new ArrayList<>();
		
		for (Integer i : Graphs.neighborListOf(this.graph, district)) {
			
			vicini.add(new DistrictVicino(i,  this.graph.getEdgeWeight(graph.getEdge(district, i)))); 
		}
		
		Collections.sort(vicini);
		return vicini; 
	}
	
	public List<Integer> getVertex(){
		List<Integer> lista= new ArrayList<>(this.graph.vertexSet());
		return lista; 
	}
}
