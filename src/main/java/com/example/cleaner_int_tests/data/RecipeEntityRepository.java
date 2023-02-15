package com.example.cleaner_int_tests.data;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface RecipeEntityRepository extends PagingAndSortingRepository<RecipeEntity, String> {
}
