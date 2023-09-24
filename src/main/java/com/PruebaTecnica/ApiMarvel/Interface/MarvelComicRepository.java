package com.PruebaTecnica.ApiMarvel.Interface;

import org.springframework.data.jpa.repository.JpaRepository;

import com.PruebaTecnica.ApiMarvel.Model.MarvelComic;

public interface MarvelComicRepository extends JpaRepository<MarvelComic, Long> {

}
