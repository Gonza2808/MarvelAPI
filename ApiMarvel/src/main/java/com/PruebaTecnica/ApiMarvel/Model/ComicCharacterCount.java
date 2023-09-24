package com.PruebaTecnica.ApiMarvel.Model;

public class ComicCharacterCount {
	private Long comicId;
	private String comicName;
	private Long characterId;
	private String characterName;
	private Long characterCount;

	public Long getComicId() {
		return comicId;
	}

	public void setComicId(Long comicId) {
		this.comicId = comicId;
	}

	public String getComicName() {
		return comicName;
	}

	public void setComicName(String comicName) {
		this.comicName = comicName;
	}

	public Long getCharacterId() {
		return characterId;
	}

	public void setCharacterId(Long characterId) {
		this.characterId = characterId;
	}

	public String getCharacterName() {
		return characterName;
	}

	public void setCharacterName(String characterName) {
		this.characterName = characterName;
	}

	public Long getCharacterCount() {
		return characterCount;
	}

	public void setCharacterCount(Long characterCount) {
		this.characterCount = characterCount;
	}

}
