package com.crossover.techtrial.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.crossover.techtrial.exceptions.ArticleNotFoundException;
import com.crossover.techtrial.model.Comment;
import com.crossover.techtrial.repository.CommentRepository;

@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private ArticleService service;

	/*
	 * Returns all the Comments related to article along with Pagination
	 * information.
	 */
	@Override
	public Page<Comment> findAll(Long articleId, Pageable pageable) {
		return commentRepository.findByArticleIdOrderByDate(articleId, pageable);
	}
	public List<Comment> findAll(Long articleId) {
		return commentRepository.findAll();
	}
	/*
	 * Save the default article.
	 */
	public Comment save(Comment comment) throws ArticleNotFoundException {
		comment.setArticle(service.findById(comment.getArticle().getId()));
		return commentRepository.save(comment);
	}
}
