package com.infy.repository;

import org.springframework.data.repository.CrudRepository;

import com.infy.entity.Novel;

public interface NovelRepository extends CrudRepository<Novel, Integer> {

}
