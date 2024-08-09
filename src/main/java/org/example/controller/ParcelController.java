package org.example.controller;

import java.util.List;
import org.example.DTO.ParcelDTO;
import org.example.exception.ParcelNotFoundException;
import org.example.exception.PostOfficeNotFoundException;
import org.example.model.ParcelHistory;
import org.example.service.ParcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/parcel")
public class ParcelController {
  private final ParcelService parcelService;

  @Autowired
  public ParcelController(ParcelService service) {
    this.parcelService = service;
  }

  @PostMapping("/register")
  public ResponseEntity<?> registerParcel(@RequestBody ParcelDTO parcelDTO)
      throws PostOfficeNotFoundException {
    parcelService.registerParcel(parcelDTO);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getParcel(@PathVariable Long id) throws ParcelNotFoundException {
    return ResponseEntity.ok(parcelService.getParcelById(id));
  }

  @GetMapping("/status/{id}")
  public ResponseEntity<?> getParcelStatus(@PathVariable Long id) throws ParcelNotFoundException {
    return ResponseEntity.ok(parcelService.getParcelStatus(id));
  }

  @GetMapping("/all")
  public ResponseEntity<?> getAllParcels() {
    return ResponseEntity.ok(parcelService.getAllParcels());
  }

  @PostMapping(path = {"/arrival/{id}"})
  public ResponseEntity<?> parcelArrival(@PathVariable Long id, @RequestParam Long postOfficeId)
      throws ParcelNotFoundException, PostOfficeNotFoundException {
    parcelService.updateStatus(id, "ARRIVED", postOfficeId);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/departure/{id}")
  public ResponseEntity<?> parcelDeparture(@PathVariable Long id, @RequestParam Long postOfficeId)
      throws ParcelNotFoundException, PostOfficeNotFoundException {
    parcelService.updateStatus(id, "DEPARTED", postOfficeId);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/deliver/{id}")
  public ResponseEntity<?> parcelDelivered(@PathVariable Long id) throws ParcelNotFoundException {
    parcelService.deliverParcel(id);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/receive/{id}")
  public ResponseEntity<?> parcelReceived(@PathVariable Long id) throws ParcelNotFoundException {
    parcelService.receiveParcel(id);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/history/{id}")
  public List<ParcelHistory> getParcelHistory(@PathVariable Long id)
      throws ParcelNotFoundException {
    return parcelService.getParcelHistory(id);
  }

  @DeleteMapping("/delete/{id}")
  public void deleteParcel(@PathVariable Long id) throws ParcelNotFoundException {
    parcelService.deleteParcel(id);
  }
}
