package com.infy.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.infy.dto.NovelDTO;
import com.infy.entity.Novel;
import com.infy.repository.NovelRepository;

@ExtendWith(MockitoExtension.class)
class NovelServiceImplTest {

    @Mock
    private NovelRepository novelRepository;

    @InjectMocks
    private NovelServiceImpl novelService;

    private Novel createNovel(Integer id, String title, Integer year, Integer authId) {
        Novel n = new Novel();
        n.setId(id);
        n.setTitle(title);
        n.setYear(year);
        n.setAuthId(authId);
        return n;
    }

    @Test
    void addNovel_validNovel_returnsSavedNovelDTO() throws Exception {
        // arrange
        NovelDTO inputDTO = NovelDTO.from(createNovel(999, "Bleak House", 1853, 1001));
        when(novelRepository.save(any(Novel.class))).thenAnswer(invocation -> {
            Novel saved = invocation.getArgument(0);
            saved.setId(50); // simulate auto-generated id
            return saved;
        });
        // act
        NovelDTO result = novelService.addNovel(inputDTO);
        // assert
        assertNotNull(result);
        assertEquals(50, result.getId()); // id came from repo, not from input
        assertEquals("Bleak House", result.getTitle());
        assertEquals(1853, result.getYear());
        assertEquals(1001, result.getAuthId());
    }

    @Test
    void updateNovel_existingId_returnsUpdatedNovelDTO() throws Exception {
        // arrange
        Novel existing = createNovel(14, "Old Title", 2020, 1001);
        NovelDTO updateDTO = NovelDTO.from(createNovel(14, "New Title", 2021, 1001));
        when(novelRepository.findById(14)).thenReturn(Optional.of(existing));
        // act
        NovelDTO result = novelService.updateNovel(updateDTO);
        // assert
        assertNotNull(result);
        assertEquals(14, result.getId());
        assertEquals("New Title", result.getTitle());
        assertEquals(2021, result.getYear());
    }

    @Test
    void deleteNovel_existingId_returnsDeletedNovelDTO() throws Exception {
        // arrange
        Novel existing = createNovel(14, "Bleak House", 1853, 1001);
        when(novelRepository.findById(14)).thenReturn(Optional.of(existing));
        // act
        NovelDTO result = novelService.deleteNovel(14);
        // assert
        assertNotNull(result);
        assertEquals(14, result.getId());
        assertEquals("Bleak House", result.getTitle());
        verify(novelRepository).deleteById(14);
    }

    @Test
    void updateNovel_nonExistentId_throwsException() {
        // arrange
        NovelDTO updateDTO = NovelDTO.from(createNovel(9999, "Nobody", 2020, 1001));
        when(novelRepository.findById(9999)).thenReturn(Optional.empty());
        // act + assert
        Exception exception = assertThrows(Exception.class, () -> novelService.updateNovel(updateDTO));
        assertEquals("No Novel for Id", exception.getMessage());
    }

    @Test
    void deleteNovel_nonExistentId_throwsException() {
        // arrange
        when(novelRepository.findById(9999)).thenReturn(Optional.empty());
        // act + assert
        Exception exception = assertThrows(Exception.class, () -> novelService.deleteNovel(9999));
        assertEquals("Bad novel ID", exception.getMessage());
        verify(novelRepository, never()).deleteById(anyInt());
    }
}