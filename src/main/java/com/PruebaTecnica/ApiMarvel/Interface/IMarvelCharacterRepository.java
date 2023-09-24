package com.PruebaTecnica.ApiMarvel.Interface;

import org.springframework.data.jpa.repository.JpaRepository;

import com.PruebaTecnica.ApiMarvel.Model.MarvelCharacter;



public interface IMarvelCharacterRepository extends JpaRepository<MarvelCharacter, Integer> {

//	public List<Object[]> findTopNComicsWithMostCharacters(@Param("n") int n);

}
