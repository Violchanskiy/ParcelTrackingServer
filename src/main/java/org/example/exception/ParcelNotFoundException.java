package org.example.exception;

public class ParcelNotFoundException extends Exception {
  public ParcelNotFoundException() {
    super("Parcel not found");
  }
}
