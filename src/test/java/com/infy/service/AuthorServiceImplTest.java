package com.infy.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.infy.dto.AuthorDTO;
import com.infy.dto.NovelDTO;
import com.infy.entity.Author;
import com.infy.entity.Novel;
import com.infy.repository.AuthorRepository;
import com.infy.repository.NovelRepository;

@ExtendWith(MockitoExtension.class)
class AuthorServiceImplTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private NovelRepository novelRepository;

    @InjectMocks
    private AuthorServiceImpl authorService;

    private Novel createNovel(Integer id, String title, Integer year, Integer authId) {
        Novel n = new Novel();
        n.setId(id);
        n.setTitle(title);
        n.setYear(year);
        n.setAuthId(authId);
        return n;
    }

    private Author createAuthor(Integer id, String name) {
        Author a = new Author();
        a.setId(id);
        a.setName(name);
        return a;
    }

    @Test
    void getNovelsByNameAndYear_returnsMatchingNovels() throws Exception {
        // arrange
        Author author = createAuthor(1001, "Charles");
        Novel n1 = createNovel(14, "New Novel", 2020, 1001);
        Novel n2 = createNovel(19, "New Novel From AddNovel Component", 2999, 1001);
        Novel n3 = createNovel(32, "klhfejher2kjf", 2021, 1001);
        author.setNovels(List.of(n1, n2, n3));
        when(authorRepository.findByName("Charles")).thenReturn(author);
        // act
        List<NovelDTO> result = authorService.getNovelsByNameAndYear("Charles", 2020);
        // assert
        assertEquals(1, result.size());
        assertEquals(14, result.get(0).getId());
        assertEquals("New Novel", result.get(0).getTitle());
        assertEquals(2020, result.get(0).getYear());
    }

    @Test
    void getNovelsByNameAndYear_noMatchingYear_returnsEmptyList() throws Exception {
        // arrange
        Author author = createAuthor(1001, "Charles");
        Novel n1 = createNovel(14, "", 2020, 1001);
        author.setNovels(List.of(n1));
        when(authorRepository.findByName("Charles")).thenReturn(author);
        // act
        List<NovelDTO> result = authorService.getNovelsByNameAndYear("Charles", 1999);
        // assert
        assertTrue(result.isEmpty());
    }

    @Test
    void addAuthor_newAuthor_returnsAuthorDTOWithNovels() throws Exception {
        // arrange
        NovelDTO novelDTO = NovelDTO.from(createNovel(50, "Bleak House", 1853, 1001));
        AuthorDTO dto = AuthorDTO.from(createAuthor(1001, "Charles"));
        dto.setNovelDTOs(List.of(novelDTO));
        when(authorRepository.findById(1001)).thenReturn(Optional.empty());
        // novelRepository.save should return the novel with an ID
        when(novelRepository.save(any(Novel.class))).thenAnswer(invocation -> {
            Novel saved = invocation.getArgument(0);
            saved.setId(50);
            return saved;
        });
        when(authorRepository.save(any(Author.class))).thenAnswer(invocation -> invocation.getArgument(0));
        // act
        AuthorDTO result = authorService.addAuthor(dto);
        // assert
        assertNotNull(result);
        assertEquals(1001, result.getId());
        assertEquals("Charles", result.getName());
        assertNotNull(result.getNovelDTOs());
        assertEquals(1, result.getNovelDTOs().size());
        assertEquals(50, result.getNovelDTOs().get(0).getId());
        assertEquals("Bleak House", result.getNovelDTOs().get(0).getTitle());
    }

    @Test
    void addAuthor_duplicateId_throwsException() {
        // arrange
        AuthorDTO dto = AuthorDTO.from(createAuthor(1001, "Charles"));
        Author existing = createAuthor(1001, "Charles");
        when(authorRepository.findById(1001)).thenReturn(Optional.of(existing));
        // act + assert
        Exception exception = assertThrows(Exception.class, () -> authorService.addAuthor(dto));
        assertEquals("Author Already Present", exception.getMessage());
        // verify nothing was saved
        verify(authorRepository, never()).save(any());
    }

    @Test
    void updateAuthor_existingId_returnsUpdatedAuthor() throws Exception {
        // arrange
        Author author = createAuthor(1001, "Charles");
        when(authorRepository.findById(1001)).thenReturn(Optional.of(author));
        // act
        AuthorDTO result = authorService.updateAuthor(1001, "Charles Dickens");
        // assert
        assertNotNull(result);
        assertEquals(1001, result.getId());
        assertEquals("Charles Dickens", result.getName());
    }

    @Test
    void updateAuthor_nonExistentId_throwsException() {
        // arrange
        when(authorRepository.findById(9999)).thenReturn(Optional.empty());
        // act + assert
        Exception exception = assertThrows(Exception.class, () -> authorService.updateAuthor(9999, "Nobody"));
        assertEquals("Bad Author Id", exception.getMessage());
    }

    @Test
    void deleteAuthor_existingId_returnsDeletedAuthorDTO() throws Exception {
        // arrange
        Author author = createAuthor(1001, "Charles");
        when(authorRepository.findById(1001)).thenReturn(Optional.of(author));
        // act
        AuthorDTO result = authorService.deleteAuthor(1001);
        // assert
        assertNotNull(result);
        assertEquals(1001, result.getId());
        assertEquals("Charles", result.getName());
        verify(authorRepository).deleteById(1001);
    }

    @Test
    void deleteAuthor_nonExistentId_throwsException() {
        // arrange
        when(authorRepository.findById(9999)).thenReturn(Optional.empty());
        // act + assert
        Exception exception = assertThrows(Exception.class, () -> authorService.deleteAuthor(9999));
        assertEquals("Bad Author Id", exception.getMessage());
        // also verify nothing was deleted
        verify(authorRepository, never()).deleteById(anyInt());
    }

}