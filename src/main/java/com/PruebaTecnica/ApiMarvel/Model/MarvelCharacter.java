package com.PruebaTecnica.ApiMarvel.Model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="marvelcharacters")
public class MarvelCharacter {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	
	@Column(length = 1000)
	private String description;

	private String path;

	@OneToMany(mappedBy = "marvelCharacter", cascade = CascadeType.ALL)
	private List<MarvelComic> comics = new ArrayList<MarvelComic>();

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public List<MarvelComic> getComics() {
		return comics;
	}

	public void setComics(List<MarvelComic> comics) {
		this.comics = comics;
	}

	@Override
	public String toString() {
		return "MarvelCharacter{" + "id=" + id + ", name='" + name + '\'' + ", description='" + description + '\''
				+ ", path='" + path + '\'' + ", comics=" + comics + '}';
	}

}
