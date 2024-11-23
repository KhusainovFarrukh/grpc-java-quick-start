package kh.farrukh.quickserver;

import java.util.List;
import java.util.logging.Logger;
import kh.farrukh.quickserver.config.QuickConfig;
import kh.farrukh.quickserver.rpc.QuizzerImpl;
import kh.farrukh.quickserver.server.QuickServer;

public class QuickServerApp {

  private static final Logger logger = Logger.getLogger(QuickServerApp.class.getName());

  public static void main(String[] args) {
    logger.info("Starting GrpcQuickServerApp...");
    var config = new QuickConfig();
    var services = List.of(new QuizzerImpl());

    var server = new QuickServer(config.getServer(), services);
    server.start();
    server.blockUntilShutdown();
  }

}
