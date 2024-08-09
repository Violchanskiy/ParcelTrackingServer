package org.example.repository;

import java.util.Optional;
import org.example.model.PostOffice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostOfficeRepository extends JpaRepository<PostOffice, Long> {
  Optional<PostOffice> findByIndex(Long index);
}
