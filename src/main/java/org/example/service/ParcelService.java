package org.example.service;

import java.time.LocalDateTime;
import java.util.List;
import org.example.DTO.ParcelDTO;
import org.example.exception.ParcelNotFoundException;
import org.example.exception.PostOfficeNotFoundException;
import org.example.model.Parcel;
import org.example.model.ParcelHistory;
import org.example.repository.ParcelHistoryRepository;
import org.example.repository.ParcelRepository;
import org.example.repository.PostOfficeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParcelService {

  private final ParcelRepository parcelRepository;
  private final ParcelHistoryRepository parcelHistoryRepository;
  private final PostOfficeRepository postOfficeRepository;

  @Autowired
  public ParcelService(
      ParcelRepository parcelRepository,
      ParcelHistoryRepository parcelHistoryRepository,
      PostOfficeRepository postOfficeRepository) {
    this.parcelRepository = parcelRepository;
    this.parcelHistoryRepository = parcelHistoryRepository;
    this.postOfficeRepository = postOfficeRepository;
  }

  public void registerParcel(ParcelDTO parcelDTO) throws PostOfficeNotFoundException {
    postOfficeRepository
        .findByIndex(parcelDTO.getRecipientIndex())
        .orElseThrow(PostOfficeNotFoundException::new);
    Parcel parcel = new Parcel();
    String status = "REGISTERED";
    parcel.setRecipientName(parcelDTO.getRecipientName());
    parcel.setType(parcelDTO.getType());
    parcel.setRecipientIndex(parcelDTO.getRecipientIndex());
    parcel.setRecipientAddress(parcelDTO.getRecipientAddress());
    parcel.setStatus(status);
    parcelRepository.save(parcel);
    saveParcelHistory(parcel.getId(), status, 0L); // Специальное значение для регистрации
  }

  public Parcel getParcelById(Long id) throws ParcelNotFoundException {
    return parcelRepository.findById(id).orElseThrow(ParcelNotFoundException::new);
  }

  public String getParcelStatus(Long id) throws ParcelNotFoundException {
    return parcelRepository.findById(id).orElseThrow(ParcelNotFoundException::new).getStatus();
  }

  public List<Parcel> getAllParcels() {
    return parcelRepository.findAll();
  }

  public void updateStatus(Long id, String status, Long PostOfficeId)
      throws ParcelNotFoundException, PostOfficeNotFoundException {
    Parcel parcel = getParcelById(id);
    parcel.setStatus(status);
    parcelRepository.save(parcel);
    saveParcelHistory(
        id,
        status,
        postOfficeRepository
            .findById(PostOfficeId)
            .orElseThrow(PostOfficeNotFoundException::new)
            .getId());
  }

  public void deliverParcel(Long id) throws ParcelNotFoundException {
    String status = "DELIVERED";
    Parcel parcel = getParcelById(id);
    parcel.setStatus(status);
    parcelRepository.save(parcel);
    saveParcelHistory(
        id, status, postOfficeRepository.findByIndex(parcel.getRecipientIndex()).get().getId());
  }

  public void receiveParcel(Long id) throws ParcelNotFoundException {
    String status = "RECEIVED";
    Parcel parcel = getParcelById(id);
    parcel.setStatus(status);
    parcelRepository.save(parcel);
    saveParcelHistory(id, status, -1L); // Специальное значение для получения
  }

  private void saveParcelHistory(Long parcelId, String status, Long postOfficeId) {
    if (parcelHistoryRepository.existsByParcelIdAndStatusAndPostOfficeId(
        parcelId, status, postOfficeId)) {
      return;
    }
    ParcelHistory history = new ParcelHistory();
    history.setStatus(status);
    history.setParcelId(parcelId);
    history.setPostOfficeId(postOfficeId);
    history.setDateTime(LocalDateTime.now());
    parcelHistoryRepository.save(history);
  }

  public List<ParcelHistory> getParcelHistory(Long id) throws ParcelNotFoundException {
    if (!parcelRepository.existsById(id)) {
      throw new ParcelNotFoundException();
    }
    return parcelHistoryRepository.findByParcelId(id);
  }

  public void deleteParcel(Long id) throws ParcelNotFoundException {
    if (!parcelRepository.existsById(id)) {
      throw new ParcelNotFoundException();
    }
    parcelRepository.deleteById(id);
  }
}
