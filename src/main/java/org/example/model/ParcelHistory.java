package org.example.model;

import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "parcel_history")
public class ParcelHistory {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "parcel_id")
  private Long parcelId;

  @Column(name = "post_office_id")
  private Long postOfficeId;

  @Column(name = "date_time")
  private LocalDateTime dateTime;

  private String status;
}
