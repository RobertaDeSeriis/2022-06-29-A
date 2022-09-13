package it.polito.tdp.itunes.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.traverse.DepthFirstIterator;

import it.polito.tdp.itunes.db.ItunesDAO;

public class Model {
	ItunesDAO dao; 
	List<Album> vertici;
	Graph<Album, DefaultWeightedEdge> grafo;
	List<Album> migliore;
	double bilancioSorgente;
	Set<Album> visitati;
	
	public Model() {
		this.dao= new ItunesDAO();
	}
	
	public String creaaGrafo(int n) {
		this.grafo= new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		vertici= dao.getAllAlbums(n);
		Graphs.addAllVertices(this.grafo, vertici);
		double peso=0; 
		
		for(Album a: vertici) {
			for (Album a1: vertici) {
				if(a!=a1) {
					 peso=a.getnCanz()-a1.getnCanz();
				 
				 if(peso>0) {
					 Graphs.addEdgeWithVertices(this.grafo, a1, a, peso);
				 }//questo è inutile serve solo se dice, se il peso è negativo metti l'arco al contrario
				 /*if(peso<0) {
					 Graphs.addEdgeWithVertices(this.grafo, a, a1, (-1)*peso);
				 }*/
			}
		}
	}
		
		return "Grafo creato!\n# Vertici:"+grafo.vertexSet().size()+ "\n# Archi: "+grafo.edgeSet().size();	
	}
	
	
	public List<AlbumBilancio> getBilancio(Album a1){
		List<AlbumBilancio> bilancio= new LinkedList<>(); 
		List<Album> vicini= Graphs.successorListOf(this.grafo, a1);
		
		for (Album a: vicini) {
			double bil= this.calcolaBilancio(a);
			bilancio.add(new AlbumBilancio(a,bil));	
		}
		Collections.sort(bilancio);
		return bilancio;
	}
	
	public double calcolaBilancio(Album a1) {
			 double peso=0;
			
			for(Album al: Graphs.predecessorListOf(grafo, a1) ) {
				peso+= grafo.getEdgeWeight(this.grafo.getEdge(al, a1));
				
			}
			for(Album al: Graphs.successorListOf(grafo, a1) ) {
				peso-= grafo.getEdgeWeight(this.grafo.getEdge(a1, al));
			
		}
			return peso;
	}

	public List<Album> getVertici() {
		return vertici;
	}

	
	public boolean esisteGrafo() {
		if(this.grafo!=null)
			return true;
		return false;
	}
	

public List<Album> calcolaPercorso(Album sorg, Album dest, double x)
{
	migliore = new LinkedList<Album>();
	List<Album> parziale = new LinkedList<>();
	parziale.add(sorg);
	bilancioSorgente=this.calcolaBilancio(sorg);
	cercaRicorsiva(parziale, dest, x);
	// this.pesoMigliore = this.pesoTot(migliore); fin qui tutto copiato
	return migliore;
}

private void cercaRicorsiva(List<Album> parziale, Album dest, double x) {
	
			//condizione di terminazione
			if(parziale.get(parziale.size()-1).equals(dest)) //finisce quando in parziale c'è la destinazione
			{
				int verticiMax= calcolaVerticiMax(parziale);
				if(verticiMax > calcolaVerticiMax(migliore))
				{
					migliore = new LinkedList<>(parziale);
				}
				return;
			}
			Album ultimo=parziale.get(parziale.size()-1);
			for(Album v:Graphs.successorListOf(this.grafo, parziale.get(parziale.size()-1))) //scorro sui vicini dell'ultimo nodo sulla lista
			{
				if(!parziale.contains(v))
				{
					if(grafo.getEdgeWeight(grafo.getEdge(ultimo,v))>=x)
					{
						parziale.add(v);
						cercaRicorsiva(parziale, dest, x);
						parziale.remove(parziale.size()-1);
					}
					
				}
				
			}
	
}

private int calcolaVerticiMax(List<Album> parziale) {
	int n=0;
	for(Album a: parziale) {
		if(this.calcolaBilancio(a)>bilancioSorgente) {
			n++; 
		}
	}
	return n;
}
public boolean sonoConnessi(Album p, Album a) {
	visitati= new HashSet<>(); 
	DepthFirstIterator<Album, DefaultWeightedEdge> it= new DepthFirstIterator<>(this.grafo, p); 
	while(it.hasNext()) {
		visitati.add(it.next()); 
		}
	
	if(visitati.contains(a)) {
	return true; 
	}
	return false;
}
	
}
