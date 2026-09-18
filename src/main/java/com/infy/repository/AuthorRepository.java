package com.infy.repository;

// import java.util.List;

// import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
// import org.springframework.data.repository.query.Param;

import com.infy.entity.Author;
// import com.infy.entity.Novel;

// We want to go from AUTHOR to NOVELS => Author is source entity
public interface AuthorRepository extends CrudRepository<Author, Integer> {
	
	// if we had the ID, we do not need to create any method here coz .findById is already in CrudRepository
	
	// Method name approach for query
	// need to find an author by name and since cascade is all, we will get that authors novels as well
	public Author findByName(String name);
	
	// @Query approach for query
//	@Query("select a.novels from Author a where a.name = :authorName and a.novels.year = :year")
//	public List<Novel> novelByYear(@Param("authorName") String authorName, @Param("year") Integer year);

}
