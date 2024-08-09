package org.example.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import org.example.model.PostOffice;
import org.example.service.PostOfficeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(PostOfficeController.class)
public class PostOfficeControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private PostOfficeService postOfficeService;

  @Test
  public void testGetAllPostOffices() throws Exception {
    List<PostOffice> postOffices = Arrays.asList(new PostOffice(), new PostOffice());

    when(postOfficeService.getAll()).thenReturn(postOffices);

    mockMvc
        .perform(MockMvcRequestBuilders.get("/postOffice/all"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(postOffices.size()));
  }

  @Test
  public void testGetPostOfficeById() throws Exception {
    Long postOfficeId = 1L;
    PostOffice postOffice = new PostOffice();
    postOffice.setId(postOfficeId);
    postOffice.setName("Main Office");

    when(postOfficeService.getById(postOfficeId)).thenReturn(postOffice);

    mockMvc
        .perform(MockMvcRequestBuilders.get("/postOffice/{id}", postOfficeId))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(postOfficeId))
        .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Main Office"));
  }

  @Test
  public void testAddPostOffice() throws Exception {
    PostOffice postOffice = new PostOffice();
    postOffice.setName("New Office");

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/postOffice/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(postOffice)))
        .andExpect(MockMvcResultMatchers.status().isCreated());

    verify(postOfficeService, times(1)).addPostOffice(any(PostOffice.class));
  }

  @Test
  public void testDeletePostOffice() throws Exception {
    Long postOfficeId = 1L;

    mockMvc
        .perform(MockMvcRequestBuilders.delete("/postOffice/delete/{id}", postOfficeId))
        .andExpect(MockMvcResultMatchers.status().isOk());

    verify(postOfficeService, times(1)).deletePostOffice(postOfficeId);
  }
}
