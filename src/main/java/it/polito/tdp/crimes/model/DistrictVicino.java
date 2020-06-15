package it.polito.tdp.crimes.model;

public class DistrictVicino implements Comparable<DistrictVicino>{

	private Integer district; 
	private Double distanza;
	
	/**
	 * @param district
	 * @param distanza
	 */
	public DistrictVicino(Integer district, Double distanza) {
		super();
		this.district = district;
		this.distanza = distanza;
	}

	public Integer getDistrict() {
		return district;
	}

	public Double getDistanza() {
		return distanza;
	}

	@Override
	public String toString() {
		return this.district +" a "+this.distanza+" km";
	}

	//distanza crescente
	@Override
	public int compareTo(DistrictVicino o) {
		
		return this.distanza.compareTo(o.distanza);
	} 
	
	
	
}
