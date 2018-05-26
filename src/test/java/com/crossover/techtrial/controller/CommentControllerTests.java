package com.crossover.techtrial.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.crossover.techtrial.exceptions.ArticleNotFoundException;
import com.crossover.techtrial.model.Article;
import com.crossover.techtrial.model.Comment;
import com.crossover.techtrial.service.ArticleService;
import com.crossover.techtrial.service.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CommentControllerTests {

	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	private MockMvc mockMvc;

	@InjectMocks
	private CommentController controller;

	@Mock
	private CommentService service;

	@Mock
	private ArticleService articleService;

	@Mock
	private Comment comment;

	@Before
	public void setup() throws Exception {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	@Test
	public void contextLoads() {
		Assert.assertNotNull(mockMvc);
		Assert.assertNotNull(controller);
		Assert.assertNotNull(service);
		Assert.assertNotNull(articleService);
	}

	@Test
	public void testCommentShouldThrowNotFoundWhenArticleNotFound() throws Exception {
		Article article = new Article();
		article.setId(1l);

		comment = new Comment();
		comment.setArticle(article);
		comment.setEmail("user1@gmail.com");
		comment.setMessage("test message");
		
		when(articleService.findById(article.getId())).thenThrow(ArticleNotFoundException.class);

		mockMvc.perform(post("/articles/1/comments").contentType(contentType).content(asJsonString(comment)))
				.andExpect(status().isNotFound());
	}
	
	@Test
	public void testCommentShouldCreated() throws Exception {
		Article article = new Article();
		article.setId(1l);

		comment = new Comment();
		comment.setArticle(article);
		comment.setEmail("user1@gmail.com");
		comment.setMessage("test message");

		when(articleService.findById(article.getId())).thenReturn(article);

		mockMvc.perform(post("/articles/1/comments").contentType(contentType).content(asJsonString(comment)))
				.andExpect(status().isCreated());
	}
	
	@Test
	public void testCommentShouldThrowBadRequestWhenFieldErrorsOccurs() throws Exception {
		Article article = new Article();
		article.setId(1l);
		comment = new Comment();
		comment.setArticle(article);
		comment.setEmail("user1gmailcom");
		comment.setMessage("test message");
		
		when(articleService.findById(article.getId())).thenReturn(article);
		when(service.save(comment)).thenReturn(comment);
		
		
		mockMvc.perform(post("/articles/1/comments").contentType(contentType).content(asJsonString(comment)))
				.andExpect(status().isBadRequest());
		}

	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
