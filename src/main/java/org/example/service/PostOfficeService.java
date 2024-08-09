package org.example.service;

import java.util.List;
import org.example.exception.PostOfficeNotFoundException;
import org.example.model.PostOffice;
import org.example.repository.PostOfficeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostOfficeService {
  private final PostOfficeRepository postOfficeRepository;

  @Autowired
  public PostOfficeService(PostOfficeRepository postOfficeRepository) {
    this.postOfficeRepository = postOfficeRepository;
  }

  public void addPostOffice(PostOffice postOffice) {
    postOfficeRepository.save(postOffice);
  }

  public List<PostOffice> getAll() {
    return postOfficeRepository.findAll();
  }

  public PostOffice getById(Long id) throws PostOfficeNotFoundException {
    return postOfficeRepository.findById(id).orElseThrow(PostOfficeNotFoundException::new);
  }

  public void deletePostOffice(Long id) throws PostOfficeNotFoundException {
    if (!postOfficeRepository.existsById(id)) {
      throw new PostOfficeNotFoundException();
    }
    postOfficeRepository.deleteById(id);
  }
}
