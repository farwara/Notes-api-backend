package com.farwa.notesapi.repository;

import com.farwa.notesapi.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findByUserId(Long userId);
    Optional<Tag> findByNameIgnoreCaseAndUserId(String name, Long userId);
    boolean existsByNameIgnoreCaseAndUserId(String name, Long userId);
}