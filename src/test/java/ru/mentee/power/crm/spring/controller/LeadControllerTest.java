package ru.mentee.power.crm.spring.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class LeadControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void shouldReturn200Ok() throws Exception {

    mockMvc.perform(MockMvcRequestBuilders.get("/leads"))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  void shouldReturnLeadsPage() throws Exception {

    mockMvc.perform(MockMvcRequestBuilders.get("/leads"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.view().name("leads/list"))
        .andExpect(MockMvcResultMatchers.model().attributeExists("leads"));
  }
}