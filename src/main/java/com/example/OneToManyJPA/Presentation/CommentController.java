package com.example.OneToManyJPA.Presentation;

import java.util.List;

import com.example.OneToManyJPA.Business.Comment;
import com.example.OneToManyJPA.Persistence.CommentRepository;
import com.example.OneToManyJPA.Persistence.TutorialRepository;

import org.elasticsearch.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CommentController {
    
    @Autowired
    TutorialRepository tutorialRepository;
    
    @Autowired
    CommentRepository commentRepository;

    @GetMapping("/tutorials/{tutorialId}/comments")
    public ResponseEntity<List<Comment>> getAllCommentsByTutorialId(@PathVariable(value = "tutorialId") Long tutorialId) {
        if (!tutorialRepository.existsById(tutorialId)) {
            throw new ResourceNotFoundException("tutorial not found with id : " + tutorialId);
        }

        return new ResponseEntity<>(commentRepository.findByTutorialId(tutorialId), HttpStatus.OK);
    }

    @GetMapping("/tutorials/{id}")
    public ResponseEntity<Comment> getCommentsByTutorialId(@PathVariable(value = "id") Long id) {
        Comment comment = commentRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("comment not found with id : " + id));
        
        return new ResponseEntity<>(comment, HttpStatus.OK);
    } 

    @PostMapping("/tutorials/{tutorrialId}/comments")
    public ResponseEntity<Comment> createComment(@PathVariable(value = "tutorialId") Long tutorialId, @RequestBody Comment comment) {
        Comment newComment = tutorialRepository.findById(tutorialId).map(tutorial -> {
            comment.setTutorial(tutorial);
            return commentRepository.save(comment);
        }).orElseThrow(() -> new ResourceNotFoundException("Not found with tutorial Id: " + tutorialId));

        return new ResponseEntity<>(newComment, HttpStatus.CREATED);
    }

    @PutMapping("/comments/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable("id") Long id, @RequestBody Comment comment) {
        Comment newComment = commentRepository.findById(id)
                                    .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id:" + id));
        newComment.setContent(comment.getContent());
        return new ResponseEntity<>(commentRepository.save(newComment), HttpStatus.OK);
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<HttpStatus> deleteComment(@PathVariable("id") Long id) {
        commentRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/tutorials/{tutorialId}/comments")
    public ResponseEntity<List<Comment>> deleteAllCommentsOfTutorial(@PathVariable("tutorialId") Long tutorialId) {
        if (!tutorialRepository.existsById(tutorialId)) {
            throw new ResourceNotFoundException("tutorial not found with id : " + tutorialId);
        }
        commentRepository.deleteByTutorialId(tutorialId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }
}
