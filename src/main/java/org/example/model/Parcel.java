package org.example.model;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "parcel")
public class Parcel {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "recipient_name")
  private String recipientName;

  private String type;

  @Column(name = "recipient_index")
  private Long recipientIndex;

  @Column(name = "recipient_address")
  private String recipientAddress;

  private String status;
}
