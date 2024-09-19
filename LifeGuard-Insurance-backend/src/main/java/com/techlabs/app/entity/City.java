package com.techlabs.app.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Set;

@Data
@Entity
@Table(name = "cities")
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String city_name;


    @ManyToOne
    @JoinColumn(name = "state_id", nullable = false)    
    private State state;
    
    private boolean isActive;

	public City(Long id, String city_name, State state, boolean isActive) {
		super();
		this.id = id;
		this.city_name = city_name;
		this.state = state;
		this.isActive = isActive;
	}
	

	public City() {
		
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCity_name() {
		return city_name;
	}

	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}


  
  

   
}
