package br.com.luizcurado.provaconceito.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.core.IsNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
//@WebMvcTest(AutenticacaoComGeracaoTokenController.class)
public class AutenticacaoComGeracaoTokenControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void deveriaDevolver400CasoDadosDeAutenticacaoEstejamIncorretos() throws Exception {
		String json = "{\"email\":\"invalido@email.com\",\"senha\":\"123456\"}";
		
		mockMvc
			.perform(post("/auth")
			.content(json)
			.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest()); // 400
	}
	
	@Test
	public void deveriaAutenticar() throws Exception {
		String json = "{\"email\":\"lrcurado@email.com\",\"senha\":\"123456\"}";
		
		mockMvc
			.perform(post("/auth")
			.content(json)
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.token").value(IsNull.notNullValue()))
			.andExpect(jsonPath("$.tipo").value("Bearer"));
	}

}
