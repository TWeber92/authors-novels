package com.infy.dto;

import java.util.List;

import com.infy.entity.Author;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


public class AuthorDTO {
	@NotNull
	@Max(value = 9999, message = "{Author.id.4DIGITSMAX}")
	private Integer id;
	
	@NotNull()
	@Pattern(regexp = "[A-Za-z][A-Za-z ]*", message = "{Author.name.ONLYLETTERS}")
	@Size(max = 50, min = 2)
	private String name;
	@Valid
	private List<NovelDTO> novelDTOs;

	public AuthorDTO(Integer id, String name){
		this.id = id;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<NovelDTO> getNovelDTOs() {
		return novelDTOs;
	}

	public void setNovelDTOs(List<NovelDTO> novelDTOs) {
		this.novelDTOs = novelDTOs;
	}

	static public AuthorDTO from(Author entity){
		return new AuthorDTO(entity.getId(), entity.getName());
	}
	/// Since the fields are private, the only way outside classes can find fields is through getter setters
	
	
}
