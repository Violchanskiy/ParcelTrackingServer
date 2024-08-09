package org.example.repository;

import java.util.List;
import org.example.model.ParcelHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParcelHistoryRepository extends JpaRepository<ParcelHistory, Long> {
  List<ParcelHistory> findByParcelId(Long id);

  boolean existsByParcelIdAndStatusAndPostOfficeId(Long id, String status, Long postOfficeId);
}
