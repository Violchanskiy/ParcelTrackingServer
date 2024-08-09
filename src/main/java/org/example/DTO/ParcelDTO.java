package org.example.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParcelDTO {
  private String recipientName;
  private String type;
  private Long recipientIndex;
  private String recipientAddress;
}
