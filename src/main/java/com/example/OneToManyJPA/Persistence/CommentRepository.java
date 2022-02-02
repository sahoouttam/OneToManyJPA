package com.example.OneToManyJPA.Persistence;

import java.util.List;

import javax.transaction.Transactional;

import com.example.OneToManyJPA.Business.Comment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByTutorialId(Long id);

    @Transactional
    void deleteByTutorialId(Long id);
    
}
