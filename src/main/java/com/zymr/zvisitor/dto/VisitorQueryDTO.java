package com.zymr.zvisitor.dto;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class VisitorQueryDTO {

	private Map<String, List<String>> findByInParams;
	
	private Date findByGte = new Date(0); 
	
	private Date findByLte = new Date(System.currentTimeMillis()); 

	public VisitorQueryDTO(Map<String, List<String>> findByInParams) {
		this.findByInParams = findByInParams;
	}
	
	public Map<String, List<String>> getFindByInParams() {
		return findByInParams;
	}

	public Date getFindByGte() {
		return findByGte;
	}

	public void setFindByGte(Date findByGte) {
		this.findByGte = findByGte;
	}

	public Date getFindByLte() {
		return findByLte;
	}

	public void setFindByLte(Date findByLte) {
		this.findByLte = findByLte;
	}

	@Override
	public String toString() {
		return "VisitorQueryDTO [findByInParams=" + findByInParams + ", findByGte=" + findByGte + ", findByLte="
				+ findByLte + "]";
	}
	

}
