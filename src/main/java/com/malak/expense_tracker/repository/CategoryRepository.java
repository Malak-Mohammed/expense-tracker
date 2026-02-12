package com.malak.expense_tracker.repository;

import com.malak.expense_tracker.model.Category;
import com.malak.expense_tracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {


    Optional<Category> findByCategoryName(String categoryName);


    boolean existsByCategoryName(String categoryName);


    List<Category> findByOwner(User owner);


    Optional<Category> findByCategoryNameAndOwner(String categoryName, User owner);

    boolean existsByCategoryNameAndOwner(String categoryName, User owner);
}