package org.example.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.example.DTO.ParcelDTO;
import org.example.exception.ParcelNotFoundException;
import org.example.exception.PostOfficeNotFoundException;
import org.example.model.Parcel;
import org.example.model.ParcelHistory;
import org.example.model.PostOffice;
import org.example.repository.ParcelHistoryRepository;
import org.example.repository.ParcelRepository;
import org.example.repository.PostOfficeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ParcelServiceTest {

  @Mock private ParcelRepository parcelRepository;

  @Mock private ParcelHistoryRepository parcelHistoryRepository;

  @Mock private PostOfficeRepository postOfficeRepository;

  @InjectMocks private ParcelService parcelService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testRegisterParcel() throws PostOfficeNotFoundException {
    ParcelDTO parcelDTO = new ParcelDTO();
    parcelDTO.setRecipientName("Recipient");
    parcelDTO.setRecipientIndex(123456L);

    PostOffice postOffice = new PostOffice();
    postOffice.setIndex(123456L);

    when(postOfficeRepository.findByIndex(123456L)).thenReturn(Optional.of(postOffice));

    parcelService.registerParcel(parcelDTO);

    verify(parcelRepository, times(1)).save(any(Parcel.class));
    verify(parcelHistoryRepository, times(1)).save(any(ParcelHistory.class));
  }

  @Test
  void testRegisterParcelNotFound() {
    ParcelDTO parcelDTO = new ParcelDTO();
    parcelDTO.setRecipientIndex(12345L);

    when(postOfficeRepository.findByIndex(123456L)).thenReturn(Optional.empty());
    assertThrows(PostOfficeNotFoundException.class, () -> parcelService.registerParcel(parcelDTO));
  }

  @Test
  void testGetParcelById() throws ParcelNotFoundException {
    Long parcelId = 1L;
    Parcel parcel = new Parcel();
    parcel.setId(parcelId);
    parcel.setRecipientName("Recipient");

    when(parcelRepository.findById(parcelId)).thenReturn(Optional.of(parcel));

    Parcel result = parcelService.getParcelById(parcelId);

    assertEquals(parcelId, result.getId());
    assertEquals("Recipient", result.getRecipientName());
  }

  @Test
  void testGetParcelByIdParcelNotFoundException() {
    Long parcelId = 1L;
    when(parcelRepository.findById(parcelId)).thenReturn(Optional.empty());
    assertThrows(
        ParcelNotFoundException.class,
        () -> {
          parcelService.getParcelById(parcelId);
        });
  }

  @Test
  void testGetParcelStatus() throws ParcelNotFoundException {
    Long parcelId = 1L;
    Parcel parcel = new Parcel();
    parcel.setId(parcelId);
    parcel.setStatus("REGISTERED");

    when(parcelRepository.findById(parcelId)).thenReturn(Optional.of(parcel));

    String result = parcelService.getParcelStatus(parcelId);

    assertEquals("REGISTERED", result);
  }

  @Test
  void testGetParcelStatusParcelNotFoundException() throws ParcelNotFoundException {
    when(parcelRepository.findById(anyLong())).thenReturn(Optional.empty());
    assertThrows(ParcelNotFoundException.class, () -> parcelService.getParcelStatus(1L));
  }

  @Test
  void testGetAllParcels() {
    List<Parcel> parcels = Arrays.asList(new Parcel(), new Parcel());

    when(parcelRepository.findAll()).thenReturn(parcels);

    List<Parcel> result = parcelService.getAllParcels();

    assertEquals(2, result.size());
  }

  @Test
  void testUpdateStatus() throws ParcelNotFoundException, PostOfficeNotFoundException {
    Long parcelId = 1L;
    Long postOfficeId = 2L;
    Parcel parcel = new Parcel();
    parcel.setId(parcelId);
    parcel.setStatus("REGISTERED");

    PostOffice postOffice = new PostOffice();
    postOffice.setId(postOfficeId);

    when(parcelRepository.findById(parcelId)).thenReturn(Optional.of(parcel));
    when(postOfficeRepository.findById(postOfficeId)).thenReturn(Optional.of(postOffice));

    parcelService.updateStatus(parcelId, "ARRIVED", postOfficeId);

    verify(parcelRepository, times(1)).save(parcel);
    verify(parcelHistoryRepository, times(1)).save(any(ParcelHistory.class));
  }

  @Test
  void testUpdateStatusParcelException() {
    when(parcelRepository.findById(anyLong())).thenReturn(Optional.empty());
    assertThrows(
        ParcelNotFoundException.class, () -> parcelService.updateStatus(1L, "ARRIVED", 1L));
  }

  @Test
  void testUpdateStatusPostOfficeException() {
    Parcel parcel = new Parcel();
    when(parcelRepository.findById(anyLong())).thenReturn(Optional.of(parcel));
    when(postOfficeRepository.findById(anyLong())).thenReturn(Optional.empty());
    assertThrows(
        PostOfficeNotFoundException.class, () -> parcelService.updateStatus(1L, "ARRIVED", 1L));
  }

  @Test
  void testDeliverParcel() throws ParcelNotFoundException {
    Long parcelId = 1L;
    Parcel parcel = new Parcel();
    parcel.setId(parcelId);
    parcel.setRecipientIndex(123456L);
    PostOffice postOffice = new PostOffice();
    postOffice.setIndex(123456L);
    when(parcelRepository.findById(parcelId)).thenReturn(Optional.of(parcel));
    when(postOfficeRepository.findByIndex(123456L)).thenReturn(Optional.of(postOffice));
    parcelService.deliverParcel(parcelId);
    assertEquals("DELIVERED", parcel.getStatus());
    verify(parcelRepository, times(1)).save(parcel);
    verify(parcelHistoryRepository, times(1)).save(any(ParcelHistory.class));
  }

  @Test
  void testDeliverParcelNotFoundException() {
    when(parcelRepository.findById(anyLong())).thenReturn(Optional.empty());
    assertThrows(ParcelNotFoundException.class, () -> parcelService.deliverParcel(1L));
  }

  @Test
  void testReceiveParcel() throws ParcelNotFoundException {
    Parcel parcel = new Parcel();
    when(parcelRepository.findById(anyLong())).thenReturn(Optional.of(parcel));

    parcelService.receiveParcel(1L);

    assertEquals("RECEIVED", parcel.getStatus());
    verify(parcelRepository, times(1)).save(parcel);
    verify(parcelHistoryRepository, times(1)).save(any(ParcelHistory.class));
  }

  @Test
  void testReceiveParcelNotFoundException() {
    when(parcelRepository.findById(anyLong())).thenReturn(Optional.empty());

    assertThrows(ParcelNotFoundException.class, () -> parcelService.receiveParcel(1L));
  }

  @Test
  void testParcelHistory() throws ParcelNotFoundException {
    List<ParcelHistory> history = new ArrayList<>();
    when(parcelRepository.existsById(1L)).thenReturn(true);
    when(parcelHistoryRepository.findByParcelId(1L)).thenReturn(history);
    List<ParcelHistory> parcelHistory = parcelService.getParcelHistory(1L);
    assertEquals(history, parcelHistory);
  }

  @Test
  void testDeleteParcel() throws ParcelNotFoundException {
    when(parcelRepository.existsById(anyLong())).thenReturn(true);
    parcelService.deleteParcel(1L);
    verify(parcelRepository, times(1)).deleteById(1L);
  }

  @Test
  void testDeleteParcelNotFoundException() {
    when(parcelRepository.existsById(anyLong())).thenReturn(false);

    assertThrows(ParcelNotFoundException.class, () -> parcelService.deleteParcel(1L));
  }
}
