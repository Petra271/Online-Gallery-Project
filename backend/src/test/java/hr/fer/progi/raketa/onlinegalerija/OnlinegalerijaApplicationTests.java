package hr.fer.progi.raketa.onlinegalerija;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hr.fer.progi.raketa.onlinegalerija.dao.ArtworkDTO;
import hr.fer.progi.raketa.onlinegalerija.dao.CollectionDTO;
import hr.fer.progi.raketa.onlinegalerija.dao.CommentDTO;
import hr.fer.progi.raketa.onlinegalerija.dao.VisitorDTO;
import hr.fer.progi.raketa.onlinegalerija.model.Style;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.print.attribute.standard.Media;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class OnlinegalerijaApplicationTests {

	private String token;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webApplicationContext;

	/*@Test
	public void contextLoads() {

	}*/

	@Test
	public void testVisitorRegistration() throws Exception {
		VisitorDTO visitorDTO = new VisitorDTO();
		visitorDTO.setName("Pero");
		visitorDTO.setSurname("Peric");
		visitorDTO.setEmail("peroperic@gmail.com");
		visitorDTO.setPassword("password123");
		visitorDTO.setPaypalMail("peroperic@gmail.com");
		visitorDTO.setFlag(false);

		String json = new ObjectMapper().writeValueAsString(visitorDTO);
		MockMultipartFile jsonFile = new MockMultipartFile("json", "", "application/json", json.getBytes());
		MockMultipartFile nullFile = new MockMultipartFile("file", "", "Auto", (byte[]) null);


		mockMvc.perform(MockMvcRequestBuilders.multipart("/visitor/registration").file(jsonFile).file(nullFile))
				.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	public void testArtistRegistration() throws Exception {
		VisitorDTO visitorDTO = new VisitorDTO();
		visitorDTO.setName("Pero");
		visitorDTO.setSurname("Umjetnik");
		visitorDTO.setEmail("peroumjetnik@gmail.com");
		visitorDTO.setPassword("password123");
		visitorDTO.setPaypalMail("peroumjetnik@gmail.com");
		visitorDTO.setFlag(true);

		String json = new ObjectMapper().writeValueAsString(visitorDTO);
		MockMultipartFile jsonFile = new MockMultipartFile("json", "", "application/json", json.getBytes());
		MockMultipartFile nullFile = new MockMultipartFile("file", "", "Auto", (byte[]) null);


		mockMvc.perform(MockMvcRequestBuilders.multipart("/visitor/registration").file(jsonFile).file(nullFile))
				.andDo(print())
				.andExpect(status().isOk());
	}

	class LoginForm {
		private String email;
		private String password;

		public LoginForm(String email, String password) {
			this.email = email;
			this.password = password;
		}

		public String getEmail() {
			return email;
		}

		public String getPassword() {
			return password;
		}

	}

	@Test
	public void testVisitorLogin() throws Exception {

		String json = new ObjectMapper().writeValueAsString(new LoginForm("peroperic@gmail.com", "password123"));
		mockMvc.perform(MockMvcRequestBuilders.post("/login").contentType(MediaType.APPLICATION_JSON).content(json))
				.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	@Disabled
	public void testArtistLogin() throws Exception {

		String json = new ObjectMapper().writeValueAsString(new LoginForm("peroumjetnik@gmail.com", "password123"));
		mockMvc.perform(MockMvcRequestBuilders.post("/login").contentType(MediaType.APPLICATION_JSON).content(json))
				.andDo(print())
				.andExpect(status().isOk());

	}

	@Test
	public void registrationWithSameEmailShouldThrow() throws Exception {
		VisitorDTO visitorDTO = new VisitorDTO();
		visitorDTO.setName("Pero");
		visitorDTO.setSurname("Sistimmailom");
		visitorDTO.setEmail("peroperic@gmail.com");
		visitorDTO.setPassword("perinasifra2");
		visitorDTO.setPaypalMail("peroperic@gmail.com");
		visitorDTO.setFlag(false);

		String json = new ObjectMapper().writeValueAsString(visitorDTO);
		MockMultipartFile jsonFile = new MockMultipartFile("json", "", "application/json", json.getBytes());
		MockMultipartFile nullFile = new MockMultipartFile("file", "", "Auto", (byte[]) null);


		mockMvc.perform(MockMvcRequestBuilders.multipart("/visitor/registration").file(jsonFile).file(nullFile))
				.andDo(print())
				.andExpect(status().is4xxClientError());
	}

	@Test
	public void testPostArtwork() throws Exception {
		ArtworkDTO artworkDTO = new ArtworkDTO();
		artworkDTO.setCollectionName("Kolekcija za testiranje 2");
		artworkDTO.setDescription("Ovo predivno djelo za jeftinu cijenu od 100 kuna je koristeno za potrebe unit testiranja.");
		artworkDTO.setName("Unit test djelo");
		artworkDTO.setStyle("OLOVKA");
		artworkDTO.setPrice("100");

		String json = new ObjectMapper().writeValueAsString(new LoginForm("peroumjetnik@gmail.com", "password123"));
		String artworkJson = new ObjectMapper().writeValueAsString(artworkDTO);
		MockMultipartFile jsonFile = new MockMultipartFile("json", "", "application/json", artworkJson.getBytes());
		MockMultipartFile nullFile = new MockMultipartFile("file", "", "Auto", (byte[]) null);

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/login").contentType(MediaType.APPLICATION_JSON).content(json)).andReturn();

		String header = mvcResult.getResponse().getHeader("Authorization");

		mockMvc.perform(MockMvcRequestBuilders.multipart("/artist/addArtwork")
						.file(jsonFile)
						.file(nullFile)
						.header("Authorization", header))
				.andDo(print())
				.andExpect(status().isOk());

	}

	@Test
	@Disabled
	public void testCreateCollection() throws Exception {

		String json = new ObjectMapper().writeValueAsString(new LoginForm("peroumjetnik@gmail.com", "password123"));

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/login").contentType(MediaType.APPLICATION_JSON).content(json)).andReturn();
		String header = mvcResult.getResponse().getHeader("Authorization");
		System.out.println("\n\n Ovo je header " + header);

		CollectionDTO collectionDTO = new CollectionDTO();
		collectionDTO.setName("Kolekcija za testiranje 2");
		collectionDTO.setDescription("Opis kolekcije za testiranje");
		collectionDTO.setStyle("OLOVKA");

		String jsonColl = new ObjectMapper().writeValueAsString(collectionDTO);

		mockMvc.perform(MockMvcRequestBuilders.post("/artist/createCollection").header("Authorization", header)
				.contentType(MediaType.APPLICATION_JSON).content(jsonColl))
				.andDo(print())
				.andExpect(status().isOk());
	}






}
