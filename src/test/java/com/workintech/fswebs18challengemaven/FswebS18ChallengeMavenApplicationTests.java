package com.workintech.fswebs18challengemaven;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.workintech.fswebs18challengemaven.controller.CardController;
import com.workintech.fswebs18challengemaven.entity.Card;
import com.workintech.fswebs18challengemaven.entity.Color;
import com.workintech.fswebs18challengemaven.entity.Type;
import com.workintech.fswebs18challengemaven.exceptions.CardException;
import com.workintech.fswebs18challengemaven.exceptions.GlobalExceptionHandler;
import com.workintech.fswebs18challengemaven.repository.CardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = {CardController.class, GlobalExceptionHandler.class,FswebS18ChallengeMavenApplicationTests.class})
@ExtendWith(ResultAnalyzer.class)
class FswebS18ChallengeMavenApplicationTests {

	@Autowired
	private Environment env;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CardRepository cardRepository;

	private Card card;
	private Card card2;
	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() {
		card = new Card();
		card.setId(1L);
		card.setColor(Color.HEARTH);
		card.setType(Type.ACE);
		card.setValue(null);

		card2 = new Card();
		card2.setId(2L);
		card2.setColor(Color.HEARTH);
		card2.setType(null);
		card2.setValue(10);
	}


	@Test
	@DisplayName("application properties istenilenler eklendi mi?")
	void serverPortIsSetTo8585() {

		String serverPort = env.getProperty("server.port");
		assertThat(serverPort).isEqualTo("9000");


		String datasourceUrl = env.getProperty("spring.datasource.url");
		assertNotNull(datasourceUrl);

		String datasourceUsername = env.getProperty("spring.datasource.username");
		assertNotNull(datasourceUsername);

		String datasourcePassword = env.getProperty("spring.datasource.password");
		assertNotNull(datasourcePassword);

		String hibernateDdlAuto = env.getProperty("spring.jpa.hibernate.ddl-auto");
		assertNotNull(hibernateDdlAuto);

		String hibernateSql = env.getProperty("logging.level.org.hibernate.SQL");
		assertNotNull(hibernateSql);

		String hibernateJdbcBind = env.getProperty("logging.level.org.hibernate.jdbc.bind");
		assertNotNull(hibernateJdbcBind);

	}

	@Test
	@DisplayName("Card not found exception test")
	void testCardNotFoundException() throws Exception {
		// Burada "HEART" gibi geçerli bir renk enum'ı bekleniyor
		given(cardRepository.findByColor(ArgumentMatchers.any(Color.class)))
				.willThrow(new CardException("Card not found", HttpStatus.NOT_FOUND));

		mockMvc.perform(get("/cards/byColor/{color}", "HEART"))  // Geçerli bir enum değeri kullanıyoruz
				.andExpect(status().isNotFound())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message").value("Card not found"));
	}


	@Test
	@DisplayName("Invalid Type test")
	void testInvalidType() throws Exception {
		mockMvc.perform(get("/workintech/cards/byType/{type}", "Hello"))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message").value("Invalid type: Hello"));
	}


	@Test
	@DisplayName("Save card test")
	void testSaveCard() throws Exception {
		given(cardRepository.save(any())).willReturn(card);

		mockMvc.perform(post("/workintech/cards")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(card)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.color", is(card.getColor().toString())));
	}

	@Test
	@DisplayName("Find all cards test")
	void testFindAllCards() throws Exception {
		List<Card> cards = Arrays.asList(card);
		given(cardRepository.findAll()).willReturn(cards);

		mockMvc.perform(get("/workintech/cards"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].color", is(card.getColor().toString())));
	}

	@Test
	@DisplayName("Find card by color test")
	void testGetCardByColor() throws Exception {
		// String -> Color enum'a dönüşüm
		Color colorEnum = Color.valueOf("HEARTH");  // Color enum'ına dönüştürme

		given(cardRepository.findByColor(colorEnum)).willReturn(List.of(card));

		mockMvc.perform(get("/workintech/cards/byColor/{color}", colorEnum.toString()))  // toString ile string olarak gönderiyoruz
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].type", is(card.getType().toString())));
	}


	@Test
	@DisplayName("Update card test")
	void testUpdateCard() throws Exception {
		Card updatedCard = new Card();
		updatedCard.setId(1L);
		updatedCard.setType(Type.KING);
		given(cardRepository.update(any())).willReturn(updatedCard);

		mockMvc.perform(put("/workintech/cards")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(updatedCard)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.type", is(updatedCard.getType().toString())));
	}

	@Test
	@DisplayName("Remove card test")
	void testRemoveCard() throws Exception {
		// Burada return yapmamıza gerek yok çünkü remove void türünde bir metottur.
		doNothing().when(cardRepository).remove(card.getId());

		mockMvc.perform(delete("/workintech/cards/{id}", card.getId()))
				.andExpect(status().isOk());
	}



	@Test
	@DisplayName("Find by type test")
	void testFindByType() throws Exception {
		List<Card> cards = Arrays.asList(card);
		given(cardRepository.findByType(card.getType())).willReturn(cards);  // 'toString()' kullanmadan 'Type' enum'ını geçiyoruz

		mockMvc.perform(get("/workintech/cards/byType/{type}", card.getType().name()))  // 'name()' ile enum adını string olarak geçiyoruz
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].color", is(card.getColor().toString())));
	}

	@Test
	@DisplayName("Find by value test")
	void testFindByValue() throws Exception {
		List<Card> cards = Arrays.asList(card2);
		given(cardRepository.findByValue(card2.getValue().intValue())).willReturn(cards);

		mockMvc.perform(get("/workintech/cards/byValue/{value}", card2.getValue().intValue()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].color", is(card2.getColor().toString())));
	}

}
