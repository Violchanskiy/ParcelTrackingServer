package org.example.controller;

import org.example.exception.PostOfficeNotFoundException;
import org.example.model.PostOffice;
import org.example.service.PostOfficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/postOffice")
public class PostOfficeController {
  private final PostOfficeService postOfficeService;

  @Autowired
  public PostOfficeController(PostOfficeService postOfficeService) {
    this.postOfficeService = postOfficeService;
  }

  @GetMapping("/all")
  public ResponseEntity<?> getAll() {
    return ResponseEntity.ok(postOfficeService.getAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getById(@PathVariable Long id) throws PostOfficeNotFoundException {
    return ResponseEntity.ok(postOfficeService.getById(id));
  }

  @PostMapping("/add")
  public ResponseEntity<?> addPostOffice(@RequestBody PostOffice postOffice) {
    postOfficeService.addPostOffice(postOffice);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @DeleteMapping("/delete/{id}")
  public void deletePostOffice(@PathVariable Long id) throws PostOfficeNotFoundException {
    postOfficeService.deletePostOffice(id);
  }
}
