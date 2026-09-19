package com.infy.service;

import java.util.List;

import com.infy.dto.AuthorDTO;
import com.infy.dto.NovelDTO;

public interface AuthorService {
	AuthorDTO addAuthor(AuthorDTO authorDTO) throws Exception;
	List<NovelDTO> getNovelsByAuthorName(String authorName);
	List<NovelDTO> getNovelsByNameAndYear(String name, Integer year);
	
	List<AuthorDTO> allAuthors(); /// getting all authors also fetches all novels ; 
	                              ///Cascade all and Author-Novel 1 to many
	AuthorDTO deleteAuthor(Integer authorId) throws Exception;
	AuthorDTO updateAuthor(Integer authorId,String authorName) throws Exception;
	
}
