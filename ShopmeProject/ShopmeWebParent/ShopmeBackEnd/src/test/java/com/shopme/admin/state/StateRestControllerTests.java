package com.shopme.admin.state;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopme.admin.repository.CountryRepository;
import com.shopme.admin.repository.StateRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;


@SpringBootTest
@AutoConfigureMockMvc
public class StateRestControllerTests {

	MockMvc mockMvc;

	ObjectMapper objectMapper;

	StateRepository repo;
	
	CountryRepository countryRepo;

	@Autowired
	public StateRestControllerTests(MockMvc mockMvc, ObjectMapper objectMapper, StateRepository repo, CountryRepository countryRepo) {
		super();
		this.mockMvc = mockMvc;
		this.objectMapper = objectMapper;
		this.repo = repo;
		this.countryRepo = countryRepo;
	}
	
	@Test
	@WithMockUser(username = "thong@gmail.com", password = "thongbem", roles = "ADMIN")
	public void testListByCountries() throws Exception {
		Integer id = 1;
		String url = "/states/list_by_country/" + id;

		MvcResult result = mockMvc.perform(get(url)) // `mockMvc.perform()`: Phương thức này được sử dụng để bắt đầu một bài kiểm tra
			.andExpect(status().isOk()) // hàm này có nhiệm vụ kiểm tra kết quả của request với các giá trị được mong đợi tương ứng  
			.andDo(print()) // hàm này cho phép thực thi một lượng lệnh nào đó trong quá trình kiểm tra và ghi lại kết quả cho mục đích kiểm tra sau này
			.andReturn();

		String jsonResponse = result.getResponse().getContentAsString();		
		State[] states = objectMapper.readValue(jsonResponse, State[].class);

		assertThat(states).hasSizeGreaterThan(0);
	}
	

	@Test
	@WithMockUser(username = "thong.it@gmail.com", password = "thongbem", roles = "ADMIN")
	public void testCreateState() throws JsonProcessingException, Exception {
		String url = "/states/save";
        
		Integer id = 2;
		Country country = countryRepo.findById(id).get();
		 
		State state = new State("DongNai", country);

		MvcResult result = mockMvc.perform(post(url).contentType("application/json")
				.content(objectMapper.writeValueAsString(state))
				.with(csrf()))
			.andDo(print())
			.andExpect(status().isOk())
			.andReturn();

		String response = result.getResponse().getContentAsString();
		Integer stateId = Integer.parseInt(response);

		Optional<State> findById = repo.findById(stateId);
		assertThat(findById.isPresent());

	}	
	
	@Test
	@WithMockUser(username = "thong.it@gmail.com", password = "thongbem", roles = "ADMIN")
	public void testUpdateState() throws JsonProcessingException, Exception {
		String url = "/states/save";

		Integer stateId = 3;
		String stateUpdateName = "Nghe An";
		
		State state = repo.findById(stateId).get();
		state.setName(stateUpdateName);

		mockMvc.perform(post(url).contentType("application/json")
				.content(objectMapper.writeValueAsString(state))
				.with(csrf()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().string(String.valueOf(stateId)));

		Optional<State> findById = repo.findById(stateId);
		assertThat(findById.isPresent());
//
//		Country savedCountry = findById.get();
//
//		assertThat(savedCountry.getName()).isEqualTo(countryName);
	}

	
	@Test
	@WithMockUser(username = "thong.it@gmail.com", password = "thongbem", roles = "ADMIN")
	public void testDeleteState() throws Exception {
		Integer stateId = 3;
		String url = "/states/delete/" + stateId;

		mockMvc.perform(get(url)).andExpect(status().isOk());

		Optional<State> findById = repo.findById(stateId);

		assertThat(findById).isNotPresent();
	}
}
