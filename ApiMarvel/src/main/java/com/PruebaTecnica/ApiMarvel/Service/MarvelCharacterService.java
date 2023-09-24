package com.PruebaTecnica.ApiMarvel.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.PruebaTecnica.ApiMarvel.Interface.IMarvelCharacterRepository;
import com.PruebaTecnica.ApiMarvel.Model.ComicCharacterCount;
import com.PruebaTecnica.ApiMarvel.Model.MarvelCharacter;
import com.PruebaTecnica.ApiMarvel.Model.MarvelComic;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;

@Service
public class MarvelCharacterService {

	@Autowired
	private IMarvelCharacterRepository iMarvelCharacter;
	@Autowired
	private EntityManager entityManager;
	
	// llena la bdd de la api de marvel
	public List<MarvelCharacter> loadingBDD(List<MarvelCharacter> marvelcharacters) {
		return iMarvelCharacter.saveAll(marvelcharacters);
	}
	// Modificación de los datos del personaje. Nombre y/o descripción.
	public MarvelCharacter updateCharacterInfo(int characterId, String newName, String newDescription) {
		Optional<MarvelCharacter> characterOptional = iMarvelCharacter.findById(characterId);

		if (characterOptional.isPresent()) {
			MarvelCharacter character = characterOptional.get();
			if (newName != null) {
				character.setName(newName);
			}
			if (newDescription != null) {
				character.setDescription(newDescription);
			}
			return iMarvelCharacter.save(character);
		} else {
			throw new EntityNotFoundException("Character with ID " + characterId + " not found");
		}
	}
	// Creación de nuevo personaje.
	public MarvelCharacter createCharacter(String name, String description, String imageUrl) {
		MarvelCharacter character = new MarvelCharacter();
		character.setName(name);
		character.setDescription(description);
		character.setPath(imageUrl);

		return iMarvelCharacter.save(character);
	}
	// Borrado de un personaje.
	public void deleteCharacter(int characterId) {
		Optional<MarvelCharacter> characterOptional = iMarvelCharacter.findById(characterId);

		if (characterOptional.isPresent()) {
			iMarvelCharacter.delete(characterOptional.get());
		} else {
			throw new EntityNotFoundException("Character with ID " + characterId + " not found");
		}
	}

	public List<Map<String, Object>> findAllCharactersWithSelectedFields() {
        List<MarvelCharacter> characters = iMarvelCharacter.findAll();
        return characters.stream()
                .map(this::mapToCharacterFields)
                .collect(Collectors.toList());
    }

    private Map<String, Object> mapToCharacterFields(MarvelCharacter character) {
        Map<String, Object> characterFields = new HashMap<>();
        characterFields.put("id", character.getId());
        characterFields.put("name", character.getName());
        characterFields.put("description", character.getDescription());
        characterFields.put("path", character.getPath());
        return characterFields;
    }
	//Consulta del detalle de un personaje que incluya los comics en los que aparece. Recibirá el id del personaje.
	public MarvelCharacter findCharacterByIdWithComics(int characterId) {
		Optional<MarvelCharacter> characterOptional = iMarvelCharacter.findById(characterId);

		if (characterOptional.isPresent()) {
			MarvelCharacter character = characterOptional.get();
			character.getComics().size();
			return character;
		} else {
			throw new EntityNotFoundException("Character with ID " + characterId + " not found");
		}
	}
	
	public List<ComicCharacterCount> findTopNComicsWithMostCharacters(int n) {
	    String sql = "SELECT MC.ID AS comic_id, MC.NAME AS comic_name, C.NAME AS character_name, C.ID AS character_id, COUNT(MC.CHARACTER_ID) AS character_count " +
	                 "FROM MARVELCOMIC MC " +
	                 "LEFT JOIN MARVELCHARACTERS C ON MC.CHARACTER_ID = C.ID " +
	                 "GROUP BY MC.ID, MC.NAME, C.NAME, C.ID " +
	                 "ORDER BY character_count DESC " +
	                 "LIMIT :n";

	    List<Object[]> resultList = entityManager.createNativeQuery(sql)
	            .setParameter("n", n)
	            .getResultList();

	    List<ComicCharacterCount> result = new ArrayList<>();

	    for (Object[] rowResult : resultList) {
	        ComicCharacterCount comicCharacterCount = new ComicCharacterCount();
	        comicCharacterCount.setComicId(((Number) rowResult[0]).longValue());
	        comicCharacterCount.setComicName((String) rowResult[1]);

	        comicCharacterCount.setCharacterId(((Long) rowResult[3]).longValue());
	        comicCharacterCount.setCharacterName((String) rowResult[2]);
	        comicCharacterCount.setCharacterCount(((Number) rowResult[4]).longValue());

	        result.add(comicCharacterCount);
	    }

	    return result;
	}


	public StringBuilder init() {
		StringBuilder response = new StringBuilder();

		String baseUrl = "https://gateway.marvel.com:443/v1/public/characters";
		String publicKey = "6f94289cb6f4d6a570a923ce6b7e0151";
		int ts = 1;
		String hash = "d088957184503aaf3e25b17be87c4c05";

		try {
			String apiUrl = baseUrl + "?ts=" + ts + "&apikey=" + publicKey + "&hash=" + hash;
			URL url = new URL(apiUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			connection.setRequestMethod("GET");
			int responseCode = connection.getResponseCode();

			if (responseCode == HttpURLConnection.HTTP_OK) {
				try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
					String inputLine;
					while ((inputLine = reader.readLine()) != null) {
						response.append(inputLine);
					}
				}
			} else {
				System.out.println("Error en la solicitud HTTP. Código de respuesta: " + responseCode);
			}

			connection.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return response;
	}

	public List<MarvelCharacter> crearCharacters(StringBuilder srt) {
		Gson gson = new Gson();
		JsonObject jsonObject = gson.fromJson(srt.toString(), JsonObject.class);
		JsonObject dataObject = jsonObject.getAsJsonObject("data");
		JsonArray resultsArray = dataObject.getAsJsonArray("results");

		List<MarvelCharacter> characters = new ArrayList<>(resultsArray.size());

		for (JsonElement resultElement : resultsArray) {
			JsonObject characterObject = resultElement.getAsJsonObject();
			MarvelCharacter character = new MarvelCharacter();

			character.setName(getStringOrNull(characterObject, "name"));
			character.setDescription(getStringOrNull(characterObject, "description"));
			character.setPath(getThumbnailPath(characterObject));

			JsonArray comicsArray = characterObject.getAsJsonObject("comics").getAsJsonArray("items");

			List<MarvelComic> comics = new ArrayList<>(comicsArray.size());

			for (int j = 0; j < comicsArray.size(); j++) {
				JsonObject comicObject = comicsArray.get(j).getAsJsonObject();
				MarvelComic aux = new MarvelComic();
				aux.setName(getStringOrNull(comicObject, "name"));
				aux.setId(j + 1);
				aux.setMarvelCharacter(character);
				comics.add(aux);
			}

			character.setComics(comics);
			characters.add(character);
		}

		return characters;
	}

	private String getStringOrNull(JsonObject jsonObject, String key) {
		JsonElement element = jsonObject.get(key);
		return (element != null && !element.isJsonNull()) ? element.getAsString() : null;
	}

	private String getThumbnailPath(JsonObject characterObject) {
		JsonObject thumbnailObject = characterObject.getAsJsonObject("thumbnail");
		String path = thumbnailObject.get("path").getAsString();
		String extension = thumbnailObject.get("extension").getAsString();
		return path + "." + extension;
	}

}
