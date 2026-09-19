package com.infy.service;

import org.springframework.stereotype.Service;

import com.infy.dto.NovelDTO;
import com.infy.entity.Novel;
import com.infy.repository.NovelRepository;

import jakarta.transaction.Transactional;

@Service 
@Transactional 
public class NovelServiceImpl implements NovelService {
    public NovelServiceImpl(NovelRepository novelRepository) {
        this.novelRepository = novelRepository;
    }

    private final NovelRepository novelRepository;
	@Override
	public NovelDTO addNovel(NovelDTO novelDTO) {  // assume that they send the Author ID in there
		Novel novel =  Novel.from(novelDTO);
		novel.setId(null);
		return NovelDTO.from(novelRepository.save(novel));
	}

	@Override
	public NovelDTO updateNovel(NovelDTO novelDTO) throws Exception {
		Novel novel = novelRepository.findById(novelDTO.getId()).orElseThrow(()->new Exception("No Novel for Id"));
		novel.setAuthId(novelDTO.getAuthId());
		novel.setTitle(novelDTO.getTitle());
		novel.setYear(novelDTO.getYear());
		return NovelDTO.from(novel); 
	}

	@Override
	public NovelDTO deleteNovel(Integer novelId) throws Exception {
		NovelDTO novelDTO = NovelDTO.from(novelRepository.findById(novelId).orElseThrow(()->new Exception("Bad novel ID"))) ;
		novelRepository.deleteById(novelId);
		return novelDTO;
	}
}
