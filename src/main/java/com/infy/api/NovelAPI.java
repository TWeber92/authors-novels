package com.infy.api;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.infy.dto.AuthorDTO;
import com.infy.dto.NovelDTO;
import com.infy.service.AuthorService;

@RestController
@RequestMapping("novels")
@Validated    // remember this ONLY helps with PathVariable/RequestParam validation
@CrossOrigin
public class NovelAPI {
	
	@Autowired
	Environment environment;
	
	@GetMapping("hi/{name}")
	String sayHi( @PathVariable 
			      @Size(max=10) 
	              @Pattern(regexp="[A-Za-z]+") 
	              String name) {
		return "Hello "+name;
	}
	
	
	@PostMapping("test")
	AuthorDTO add(@RequestBody @Valid AuthorDTO authorDTO) {
		return authorDTO;
	}
	
	
	
	
	
	
	@DeleteMapping("/book/{bookId}")
	/// this method runs for requested URL like:  /novels/book/23?bookType=shortStory
	String deleteBook(@PathVariable 
			@Max(value = 9999,message = "{bookid.greater.fourdigits}") 
			@Min(value=1000, message = "{bookid.less.fourdigits}")
					Integer bookId, 
			@RequestParam String bookType) {
		return "We received " + "bookId:"+bookId+" bookType:"+bookType;			
	}
	
	
	@PostMapping("create")
	ResponseEntity<NovelDTO> createNovel(@RequestBody @Valid NovelDTO novelDTO) {
		System.out.println(novelDTO); /// just to see what was received in the Request Body
		return new ResponseEntity<NovelDTO>(novelDTO,HttpStatus.CREATED);
		/// returning a status code of 201 Created instead of 200 OK
		
	}
	
	
	
	
	
	@GetMapping("novelDTO")
	/// this method works for http://localhost:5555/novels/novelDTO
	NovelDTO sendNovel() {
		NovelDTO novelDTO = new NovelDTO();
		novelDTO.setId(23);
		novelDTO.setTitle("Great Expectations");
		novelDTO.setYear(1955);
		novelDTO.setAuthId(23);
		return novelDTO;
		// This method returns an OBJECT but ResponseBody can only carry STRINGs
		// @ResponseBody converts the novelDTO object to a Stringified version and puts in the ResponseBody
		// What format does @ResponseBody use to convert from object to String??
		// Ans: JSON format => Javascript Object Notation
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Autowired
	AuthorService authorService;
	
	@PostMapping("")
	public ResponseEntity<NovelDTO> addNovel(@RequestBody @Valid NovelDTO novelDTO) throws Exception{
		return new ResponseEntity<NovelDTO>(authorService.addNovel(novelDTO),HttpStatus.CREATED);
	}
		
	@GetMapping("authors")
	public ResponseEntity<List<AuthorDTO>> allAuthors(){
		return new ResponseEntity<List<AuthorDTO>>(authorService.allAuthors(),HttpStatus.OK);
	}
	
	@PutMapping("")
	public ResponseEntity<NovelDTO> updateNovel(@Valid @RequestBody NovelDTO novelDTO) throws Exception{
		return new ResponseEntity<NovelDTO>(authorService.updateNovel(novelDTO),HttpStatus.OK);
	}
	
	@DeleteMapping("{novelId}")
	public ResponseEntity<NovelDTO> deleteNovel(@PathVariable Integer novelId) throws Exception{
		return new ResponseEntity<NovelDTO>(authorService.deleteNovel(novelId),HttpStatus.OK);
	}
	
	@DeleteMapping("authors/{authorId}")
	public ResponseEntity<AuthorDTO> deleteAuthor(@PathVariable Integer authorId) throws Exception{
		return new ResponseEntity<AuthorDTO>(authorService.deleteAuthor(authorId),HttpStatus.OK);
	}
	
	@PutMapping("authors/{authorId}/{authorName}")
	public ResponseEntity<AuthorDTO> updateAuthor(@PathVariable Integer authorId,@PathVariable String authorName) throws Exception {
		return new ResponseEntity<AuthorDTO>(authorService.updateAuthor(authorId, authorName),HttpStatus.OK);
	}
	
	@PostMapping("authors")
	public ResponseEntity<AuthorDTO> addAuthor(@Valid @RequestBody  AuthorDTO authorDTO) throws Exception{
		return new ResponseEntity<AuthorDTO>(authorService.addAuthor(authorDTO),HttpStatus.CREATED);
	}
	
	@GetMapping("authors/{authorName}")
	public ResponseEntity<List<NovelDTO>> getBooksByauthorName(@PathVariable @Pattern(regexp = "[A-Za-z][A-Za-z ]*",message = "{author.name.isalphabetic}") String authorName){
		return new ResponseEntity<List<NovelDTO>>(authorService.getNovelsByAuthorName(authorName),HttpStatus.OK);
	}
	
	@GetMapping("authors/{authorName}/{year}")
	public ResponseEntity<List<NovelDTO>> getByYear(@PathVariable String authorName, @PathVariable Integer year){
		return new ResponseEntity<List<NovelDTO>>(authorService.getNovelsByNameAndYear(authorName, year),HttpStatus.OK);
	}

	
	
	/// basic sanity check
//	@GetMapping("")
//	public String hi() {
//		return "Hello";
//	}

}
