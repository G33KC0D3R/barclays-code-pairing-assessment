package roughWork;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Trade {

  private String tradeId;
  private int version;
  private String counterPartyId;
  private String bookId;
  private LocalDate maturityDate;
  private LocalDate createdDate;
  private boolean expired;

  public String getTradeId() {
    return tradeId;
  }

  public void setTradeId(String tradeId) {
    this.tradeId = tradeId;
  }

  public int getVersion() {
    return version;
  }

  public void setVersion(int version) {
    this.version = version;
  }

  public String getCounterPartyId() {
    return counterPartyId;
  }

  public void setCounterPartyId(String counterPartyId) {
    this.counterPartyId = counterPartyId;
  }

  public String getBookId() {
    return bookId;
  }

  public void setBookId(String bookId) {
    this.bookId = bookId;
  }

  public LocalDate getMaturityDate() {
    return maturityDate;
  }

  public void setMaturityDate(LocalDate maturityDate) {
    this.maturityDate = maturityDate;
  }

  public LocalDate getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(LocalDate createdDate) {
    this.createdDate = createdDate;
  }

  public boolean isExpired() {
    return expired;
  }

  public void setExpired(boolean expired) {
    this.expired = expired;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    else if (obj instanceof Trade) {
      Trade temp = (Trade) obj;
      if (this.getTradeId().equals(temp.getTradeId()) && this.getVersion() == (temp.getVersion()))
        return true;
      else
        return false;
    } else
      return false;
  }

  @Override
  public String toString() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    return "Trade [tradeId=" + tradeId + ", version=" + version + ", counterPartyId=" + counterPartyId + ", bookId="
      + bookId + ", maturityDate=" + maturityDate.format(formatter) + ", createdDate=" + createdDate.format(formatter)
      + ", expired=" + ((expired) ? "Y" : "N") + "]";
  }

}
