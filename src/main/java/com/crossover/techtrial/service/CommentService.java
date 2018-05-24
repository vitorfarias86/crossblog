package com.crossover.techtrial.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.crossover.techtrial.model.Comment;

public interface CommentService {

  /*
   * Returns all the Comments related to article along with Pagination information.
   */
	Page<Comment> findAll(Long articleId, Pageable pageable);
	List<Comment> findAll(Long articleId);

  /*
   * Save the default article.
   */
  Comment save(Comment comment);

}
