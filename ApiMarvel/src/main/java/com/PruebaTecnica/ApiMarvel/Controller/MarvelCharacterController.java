package com.PruebaTecnica.ApiMarvel.Controller;

import java.rmi.dgc.VMID;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.PruebaTecnica.ApiMarvel.Model.ComicCharacterCount;
import com.PruebaTecnica.ApiMarvel.Model.MarvelCharacter;
import com.PruebaTecnica.ApiMarvel.Service.MarvelCharacterService;

@RestController
@RequestMapping("/MarvelCharacter")
public class MarvelCharacterController {

	@Autowired
	private MarvelCharacterService marvelservice;

	// Creación de nuevo personaje.
	@PostMapping("/")
	public MarvelCharacter createCharacter(@RequestParam String name, @RequestParam String description,
			@RequestParam String imageUrl) {
		return marvelservice.createCharacter(name, description, imageUrl);
	}

	// Modificación de los datos del personaje. Nombre y/o descripción.
	@PutMapping("/{characterId}")
	public MarvelCharacter updateCharacter(@PathVariable int characterId, @RequestParam(required = false) String name,
			@RequestParam(required = false) String description) {
		return marvelservice.updateCharacterInfo(characterId, name, description);
	}

	// Consulta de los personajes. Sólo su info, id, nombre, descripción y url de la
	// imagen del personaje.
	@GetMapping("/info")
	public List<MarvelCharacter> getAllCharacterInfo() {
		return marvelservice.getAllCharacterInfo();
	}

	// Borrado de un personaje.
	@DeleteMapping("/{characterId}")
	public ResponseEntity<VMID> deleteCharacter(@PathVariable int characterId) {
		marvelservice.deleteCharacter(characterId);
		return ResponseEntity.noContent().build();
	}

	// Consulta del detalle de un personaje que incluya los comics en los que
	// aparece. Recibirá el id del personaje.
	@GetMapping("/{characterId}")
	public MarvelCharacter findCharacterDetail(@PathVariable int characterId) {
		return marvelservice.findCharacterByIdWithComics(characterId);
	}

	@GetMapping("/findTopNComics")
	public List<ComicCharacterCount> findTopNComicsWithMostCharacters(@RequestParam int n) {
		return marvelservice.findTopNComicsWithMostCharacters(n);
	}

	// llena la bdd de la api de marvel
	@GetMapping("/CargarBDD")
	public List<MarvelCharacter> saveData() {
		return marvelservice.loadingBDD(marvelservice.crearCharacters(marvelservice.init()));

	}
}
