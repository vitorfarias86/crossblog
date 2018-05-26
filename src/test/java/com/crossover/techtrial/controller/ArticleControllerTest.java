package com.crossover.techtrial.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.util.ArrayList;

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
import com.crossover.techtrial.service.ArticleService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ArticleControllerTest {

	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	private MockMvc mockMvc;

	@InjectMocks
	private ArticleController controller;

	@Mock
	private ArticleService service;

	@Mock
	private Article article;
	
	@Before
	public void setup() throws Exception {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	@Test
	public void testArticleShouldThrowBadRequestWhenFieldErrorsOccurs() throws Exception {
		article = new Article();
		article.setTitle("hello");
		article.setEmail("user1gmailcom"); // invalid e-mail
		article.setContent("10");
		article.setPublished(Boolean.TRUE);

		when(service.save(article)).thenReturn(article);

		mockMvc.perform(post("/articles/").contentType(contentType).content(asJsonString(article)))
				.andExpect(status().isBadRequest())
				.andDo(print());
		}

	@Test
	public void testArticleShouldBeCreated() throws Exception {
		article = new Article();
		article.setTitle("hello");
		article.setEmail("user1@gmail.com");
		article.setContent("10");
		article.setPublished(Boolean.TRUE);

		when(service.save(article)).thenReturn(article);

		mockMvc.perform(post("/articles/").contentType(contentType).content(asJsonString(article)))
				.andExpect(status().isCreated());
	}
	
	@Test
	public void testArticleShouldBeUpdated() throws Exception {
		article = new Article();
		article.setId(1l);
		article.setTitle("teste update");
		article.setEmail("user1@gmail.com");
		article.setContent(" ");

		when(service.save(article)).thenReturn(article);
		mockMvc.perform(
				put("/articles/{article-id}", article.getId()).contentType(contentType).content(asJsonString(article)))
				.andExpect(status().isOk()).andExpect(jsonPath("$.email").value("user1@gmail.com"))
				.andExpect(jsonPath("$.title").value("teste update"));

		verify(service, times(1)).save(article);
		verifyNoMoreInteractions(service);

	}

	@Test
	public void testArticleFindByIdShouldReturnNotFound() throws Exception {
		when(service.findById(1L)).thenThrow(ArticleNotFoundException.class);
		mockMvc.perform(get("/articles/{article-id}", 1)).andExpect(status().isNotFound());
		verify(service, times(1)).findById(1L);
		verifyNoMoreInteractions(service);
	}

	@Test
	public void testArticleFindByIdShouldReturnArticle() throws Exception {
		article = new Article();
		article.setId(1l);
		article.setTitle("teste update");
		article.setEmail("user1@gmail.com");
		article.setContent(" ");

		when(service.findById(1L)).thenReturn(article);
		mockMvc.perform(get("/articles/{article-id}", 1).contentType(contentType).content(asJsonString(article)))
				.andExpect(status().isOk()).andExpect(jsonPath("$.email").value("user1@gmail.com"))
				.andExpect(jsonPath("$.title").value("teste update"));
		verify(service, times(1)).findById(1L);
		verifyNoMoreInteractions(service);
	}

	@Test
	public void testArticleShouldBeDelted() throws Exception {
		doNothing().when(service).delete(1l);
		mockMvc.perform(delete("/articles/{article-id}", 1))
				.andExpect(status().isOk());
		verify(service, times(1)).delete(1L);
		verifyNoMoreInteractions(service);
	}
	
	@Test
	public void testArticleSearchShouldReturnOk() throws Exception {
		when(service.search("A")).thenReturn(new ArrayList<>());
		mockMvc.perform(get("/articles/search")
				.param("text", "A"))
				.andExpect(status().isOk());
		
		verify(service, times(1)).search("A");
		verifyNoMoreInteractions(service);
	}

	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
