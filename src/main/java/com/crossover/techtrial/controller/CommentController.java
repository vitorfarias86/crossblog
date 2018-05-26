package com.crossover.techtrial.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.crossover.techtrial.exceptions.ArticleNotFoundException;
import com.crossover.techtrial.model.Comment;
import com.crossover.techtrial.service.ArticleService;
import com.crossover.techtrial.service.CommentService;

@RestController
public class CommentController {
  @Autowired
  private CommentService commentService;

  @Autowired
  private ArticleService articleService;

  @PostMapping(path = "articles/{article-id}/comments")
  public ResponseEntity<Comment> createComment(@PathVariable(value = "article-id") Long articleId,
      @Valid @RequestBody Comment comment) throws ArticleNotFoundException {
	  
    comment.setArticle(articleService.findById(articleId));
    
    return new ResponseEntity<>(commentService.save(comment), HttpStatus.CREATED);
  }

  @GetMapping(path = "articles/{article-id}/comments")
  public ResponseEntity<List<Comment>> getComments(@PathVariable("article-id") Long articleId) {
    return new ResponseEntity<>(commentService.findAll(articleId), HttpStatus.OK);
  }
  
  @GetMapping(path = "articles/{article-id}/comments-page")
  public ResponseEntity<Page<Comment>> getCommentsPageable(@PathVariable("article-id") Long articleId, Pageable page) {
    return new ResponseEntity<>(commentService.findAll(articleId, page), HttpStatus.OK);
  }
}
