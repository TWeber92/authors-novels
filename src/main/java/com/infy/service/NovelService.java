package com.infy.service;

import com.infy.dto.NovelDTO;

public interface NovelService {
	NovelDTO addNovel(NovelDTO novelDTO);  /// adding a new novel without adding a new author 
	NovelDTO updateNovel(NovelDTO novelDTO) throws Exception;                        /// edit a novel 
	NovelDTO deleteNovel(Integer novelId) throws Exception;                       /// delete a novel 
}
