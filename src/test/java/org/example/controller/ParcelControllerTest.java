package org.example.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import org.example.DTO.ParcelDTO;
import org.example.model.Parcel;
import org.example.model.ParcelHistory;
import org.example.service.ParcelService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(ParcelController.class)
public class ParcelControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private ParcelService parcelService;

  @Test
  public void testRegisterParcel() throws Exception {
    ParcelDTO parcelDTO = new ParcelDTO();
    parcelDTO.setRecipientName("Recipient");
    parcelDTO.setRecipientIndex(111111L);
    parcelDTO.setRecipientAddress("Street");
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/parcel/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(parcelDTO)))
        .andExpect(MockMvcResultMatchers.status().isCreated());

    verify(parcelService, times(1)).registerParcel(any(ParcelDTO.class));
  }

  @Test
  public void testGetParcel() throws Exception {
    Long parcelId = 1L;
    Parcel parcel = new Parcel();
    parcel.setId(parcelId);
    parcel.setRecipientName("Recipient");

    when(parcelService.getParcelById(parcelId)).thenReturn(parcel);

    mockMvc
        .perform(MockMvcRequestBuilders.get("/parcel/{id}", parcelId))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(parcelId))
        .andExpect(MockMvcResultMatchers.jsonPath("$.recipientName").value("Recipient"));
  }

  @Test
  public void testGetParcelStatus() throws Exception {
    Long parcelId = 1L;
    String status = "REGISTERED";
    when(parcelService.getParcelStatus(parcelId)).thenReturn(status);
    mockMvc
        .perform(MockMvcRequestBuilders.get("/parcel/status/{id}", parcelId))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string(status));
  }

  @Test
  public void testGetAllParcels() throws Exception {
    List<Parcel> parcels = Arrays.asList(new Parcel(), new Parcel());

    when(parcelService.getAllParcels()).thenReturn(parcels);

    mockMvc
        .perform(MockMvcRequestBuilders.get("/parcel/all"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(parcels.size()));
  }

  @Test
  public void testParcelArrival() throws Exception {
    Long parcelId = 1L;
    Long postOfficeId = 2L;

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/parcel/arrival/{id}", parcelId)
                .param("postOfficeId", postOfficeId.toString()))
        .andExpect(MockMvcResultMatchers.status().isOk());

    verify(parcelService, times(1)).updateStatus(parcelId, "ARRIVED", postOfficeId);
  }

  @Test
  public void testParcelDeparture() throws Exception {
    Long parcelId = 1L;
    Long postOfficeId = 2L;

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/parcel/departure/{id}", parcelId)
                .param("postOfficeId", postOfficeId.toString()))
        .andExpect(MockMvcResultMatchers.status().isOk());

    verify(parcelService, times(1)).updateStatus(parcelId, "DEPARTED", postOfficeId);
  }

  @Test
  public void testParcelDelivered() throws Exception {
    Long parcelId = 1L;

    mockMvc
        .perform(MockMvcRequestBuilders.post("/parcel/deliver/{id}", parcelId))
        .andExpect(MockMvcResultMatchers.status().isOk());

    verify(parcelService, times(1)).deliverParcel(parcelId);
  }

  @Test
  public void testParcelReceived() throws Exception {
    Long parcelId = 1L;

    mockMvc
        .perform(MockMvcRequestBuilders.post("/parcel/receive/{id}", parcelId))
        .andExpect(MockMvcResultMatchers.status().isOk());

    verify(parcelService, times(1)).receiveParcel(parcelId);
  }

  @Test
  public void testGetParcelHistory() throws Exception {
    Long parcelId = 1L;
    List<ParcelHistory> history = Arrays.asList(new ParcelHistory(), new ParcelHistory());

    when(parcelService.getParcelHistory(parcelId)).thenReturn(history);

    mockMvc
        .perform(MockMvcRequestBuilders.get("/parcel/history/{id}", parcelId))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(history.size()));
  }

  @Test
  public void testDeleteParcel() throws Exception {
    Long parcelId = 1L;

    mockMvc
        .perform(MockMvcRequestBuilders.delete("/parcel/delete/{id}", parcelId))
        .andExpect(MockMvcResultMatchers.status().isOk());

    verify(parcelService, times(1)).deleteParcel(parcelId);
  }
}
