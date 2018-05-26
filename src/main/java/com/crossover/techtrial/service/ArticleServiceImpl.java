package com.crossover.techtrial.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.crossover.techtrial.exceptions.ArticleNotFoundException;
import com.crossover.techtrial.model.Article;
import com.crossover.techtrial.repository.ArticleRepository;

@Service
@CacheConfig(cacheNames = "search" )
public class ArticleServiceImpl implements ArticleService {

	@Autowired
	ArticleRepository articleRepository;

	public Article save(Article article) {
		return articleRepository.save(article);
	}

	public Article findById(Long id) throws ArticleNotFoundException {
		return articleRepository.findById(id).orElseThrow(() -> new ArticleNotFoundException(id));
	}

	public void delete(Long id) {
		articleRepository.deleteById(id);
	}
	@Cacheable
	public List<Article> search(String search) {

		return articleRepository.findTop10ByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(search, search);
	}

}