package org.springframework.samples.mvc.messageconverters;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.samples.mvc.AbstractContextControllerTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
public class MessageConvertersControllerTests extends AbstractContextControllerTests {

	private static String URI = "/messageconverters/{action}";

	private MockMvc mockMvc;

	@Before
	public void setup() {
		this.mockMvc = webAppContextSetup(this.wac).alwaysExpect(status().isOk()).build();
	}

	@Test
	public void readString() throws Exception {
		this.mockMvc.perform(post(URI, "string").content("foo".getBytes()))
				.andExpect(content().string("Read string 'foo'"));
	}

	@Test
	public void writeString() throws Exception {
		this.mockMvc.perform(get(URI, "string"))
			.andExpect(content().string("Wrote a string"));
	}

	@Test
	public void readJson() throws Exception {
		this.mockMvc.perform(
				post(URI, "json")
					.contentType(MediaType.APPLICATION_JSON)
					.content("{ \"foo\": \"bar\", \"fruit\": \"apple\" }".getBytes()))
				.andExpect(content().string("Read from JSON: JavaBean {foo=[bar], fruit=[apple]}"));
	}

	@Test
	public void writeJson() throws Exception {
		this.mockMvc.perform(get(URI, "json").accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.foo").value("bar"))
				.andExpect(jsonPath("$.fruit").value("apple"));
	}

	@Test
	public void writeJson2() throws Exception {
		this.mockMvc.perform(get(URI, "json").accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(jsonPath("$.foo").value("bar"))
				.andExpect(jsonPath("$.fruit").value("apple"));
	}

}
