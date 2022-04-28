package roughWork;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class TradeStore {

  private static List<Trade> tradeRepository = new ArrayList<>();

  public static void main(String[] args) {
    TradeStore tradeStore = new TradeStore();

    // tradeStore.manual();
    tradeStore.auto();

    tradeStore.fetchTrades();
  }

  private void manual() {
    Trade trade1 = new Trade();
    trade1.setTradeId("T1");
    trade1.setVersion(1);
    trade1.setCounterPartyId("CP-1");
    trade1.setBookId("B1");
    trade1.setMaturityDate(LocalDate.of(2022, 5, 20));
    trade1.setCreatedDate(LocalDate.now());
    trade1.setExpired(false);

    Trade trade11 = new Trade();
    trade11.setTradeId("T1");
    trade11.setVersion(2);
    trade11.setCounterPartyId("CP-1");
    trade11.setBookId("B1");
    trade11.setMaturityDate(LocalDate.of(2022, 5, 20));
    trade11.setCreatedDate(LocalDate.now());
    trade11.setExpired(false);

    Trade trade111 = new Trade();
    trade111.setTradeId("T1");
    trade111.setVersion(2);
    trade111.setCounterPartyId("CP-3");
    trade111.setBookId("B3");
    trade111.setMaturityDate(LocalDate.of(2022, 5, 20));
    trade111.setCreatedDate(LocalDate.now());
    trade111.setExpired(false);

    Trade trade2 = new Trade();
    trade2.setTradeId("T2");
    trade2.setVersion(2);
    trade2.setCounterPartyId("CP-2");
    trade2.setBookId("B1");
    trade2.setMaturityDate(LocalDate.of(2022, 5, 20));
    trade2.setCreatedDate(LocalDate.now());
    trade2.setExpired(false);

    Trade trade3 = new Trade();
    trade3.setTradeId("T2");
    trade3.setVersion(1);
    trade3.setCounterPartyId("CP-1");
    trade3.setBookId("B1");
    trade3.setMaturityDate(LocalDate.of(2022, 5, 20));
    trade3.setCreatedDate(LocalDate.of(2015, 3, 14));
    trade3.setExpired(false);

    Trade trade4 = new Trade();
    trade4.setTradeId("T3");
    trade4.setVersion(3);
    trade4.setCounterPartyId("CP-3");
    trade4.setBookId("B2");
    trade4.setMaturityDate(LocalDate.of(2022, 5, 20));
    trade4.setCreatedDate(LocalDate.now());
    trade4.setExpired(true);

    asyncTransmitTrade(trade4);
    asyncTransmitTrade(trade3);
    asyncTransmitTrade(trade2);
    asyncTransmitTrade(trade1);
    asyncTransmitTrade(trade11);
    asyncTransmitTrade(trade111);
  }

  private void auto() {
    Random random = new Random();
    for (int i = 0; i < 50; i++) {
      Trade trade = new Trade();
      trade.setTradeId("T" + random.nextInt(10));
      trade.setVersion(random.nextInt(10));
      trade.setCounterPartyId("CP-" + random.nextInt(10));
      trade.setBookId("B" + random.nextInt(10));
      trade.setMaturityDate(LocalDate.of(2022, 5, 20));
      trade.setCreatedDate(LocalDate.now());
      trade.setExpired(false);
      asyncTransmitTrade(trade);
    }
  }

  private void asyncTransmitTrade(Trade trade) {
    new Thread(() -> {
      transmitTrade(trade);
    }).start();
  }

  private void transmitTrade(Trade trade) {
    // Create Operation to DB

    if (trade.getMaturityDate().isBefore(LocalDate.now())) {
      System.out.println("The trade which has less maturity date then today date is not allowed");
      return;
    }

    synchronized (tradeRepository) {
      List<Trade> similarTrade = tradeRepository.stream().filter(t -> trade.getTradeId().equals(t.getTradeId()))
        .collect(Collectors.toList());
      similarTrade.sort(new Comparator<Trade>() {
        @Override
        public int compare(Trade o1, Trade o2) {
          return o2.getVersion() - o1.getVersion();
        }
      });
      if (!similarTrade.isEmpty()) {
        Trade highestVersionTrade = similarTrade.get(0);
        if (trade.getVersion() < highestVersionTrade.getVersion()) {
          System.out.println("Rejecting due to lower version");
          return;
        }
        if (trade.getVersion() == highestVersionTrade.getVersion()) {
          int index = 0;
          for (int i = 0; i < tradeRepository.size(); i++) {
            if (tradeRepository.get(i).equals(highestVersionTrade)) {
              index = i;
              break;
            }
          }
          System.out.println("Version already exist hence overriding");
          tradeRepository.set(index, trade);
          return;
        }
      }
    }

    tradeRepository.add(trade);
  }

  private void fetchTrades() {
    // Update Operation in DB
    // can be optimized by using scheduler
    for (Trade trade : tradeRepository) {
      if (trade.getMaturityDate().isBefore(LocalDate.now()) || trade.getMaturityDate().isEqual(LocalDate.now()))
        trade.setExpired(true);
    }

    // Read Operation from DB
    tradeRepository.forEach(System.out::println);
  }

}
