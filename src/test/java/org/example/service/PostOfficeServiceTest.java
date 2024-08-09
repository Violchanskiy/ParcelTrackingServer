package org.example.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.example.exception.PostOfficeNotFoundException;
import org.example.model.PostOffice;
import org.example.repository.PostOfficeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class PostOfficeServiceTest {

  @Mock private PostOfficeRepository postOfficeRepository;

  @InjectMocks private PostOfficeService postOfficeService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testAddPostOffice() {
    PostOffice postOffice = new PostOffice();
    postOffice.setName("Main Office");

    postOfficeService.addPostOffice(postOffice);

    verify(postOfficeRepository, times(1)).save(postOffice);
  }

  @Test
  void testGetAll() {
    List<PostOffice> postOffices = Arrays.asList(new PostOffice(), new PostOffice());

    when(postOfficeRepository.findAll()).thenReturn(postOffices);

    List<PostOffice> result = postOfficeService.getAll();

    assertEquals(2, result.size());
  }

  @Test
  void testGetById() throws PostOfficeNotFoundException {
    Long postOfficeId = 1L;
    PostOffice postOffice = new PostOffice();
    postOffice.setId(postOfficeId);
    postOffice.setName("Main Office");

    when(postOfficeRepository.findById(postOfficeId)).thenReturn(Optional.of(postOffice));

    PostOffice result = postOfficeService.getById(postOfficeId);

    assertEquals(postOfficeId, result.getId());
    assertEquals("Main Office", result.getName());
  }

  @Test
  void testGetByIdNotFound() {
    Long postOfficeId = 1L;

    when(postOfficeRepository.findById(postOfficeId)).thenReturn(Optional.empty());

    assertThrows(
        PostOfficeNotFoundException.class,
        () -> {
          postOfficeService.getById(postOfficeId);
        });
  }

  @Test
  void testDeletePostOffice() throws PostOfficeNotFoundException {
    Long postOfficeId = 1L;

    when(postOfficeRepository.existsById(postOfficeId)).thenReturn(true);

    postOfficeService.deletePostOffice(postOfficeId);

    verify(postOfficeRepository, times(1)).deleteById(postOfficeId);
  }

  @Test
  void testDeletePostOfficeNotFound() {
    Long postOfficeId = 1L;

    when(postOfficeRepository.existsById(postOfficeId)).thenReturn(false);

    assertThrows(
        PostOfficeNotFoundException.class,
        () -> {
          postOfficeService.deletePostOffice(postOfficeId);
        });
  }
}
