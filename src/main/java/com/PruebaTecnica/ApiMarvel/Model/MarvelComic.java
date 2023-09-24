package com.PruebaTecnica.ApiMarvel.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "marvelcomic")
public class MarvelComic {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private long id_comic;

	private String name;

	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "character_id",nullable = false)
	private MarvelCharacter marvelCharacter;

	public long getId() {
		return id_comic;
	}

	public void setId(long id) {
		this.id_comic = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MarvelCharacter getMarvelCharacter() {
		return marvelCharacter;
	}

	public void setMarvelCharacter(MarvelCharacter marvelCharacter) {
		this.marvelCharacter = marvelCharacter;
	}
	
	

}
