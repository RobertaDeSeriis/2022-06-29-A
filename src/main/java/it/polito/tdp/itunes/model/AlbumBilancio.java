package it.polito.tdp.itunes.model;

public class AlbumBilancio implements Comparable<AlbumBilancio>{
	
	Album a;
	Double bilancio;
	
	
	public AlbumBilancio(Album a, Double bilancio) {
		super();
		this.a = a;
		this.bilancio = bilancio;
	}
	
	
	public Album getA() {
		return a;
	}
	public void setA(Album a) {
		this.a = a;
	}
	public Double getBilancio() {
		return bilancio;
	}
	public void setBilancio(Double bilancio) {
		this.bilancio = bilancio;
	}


	@Override
	public String toString() {
		return  a + ", bilancio=" + bilancio;
	}


	@Override
	public int compareTo(AlbumBilancio o) {
		return (int) -(this.bilancio-o.bilancio);
	}
	
	
	

}
