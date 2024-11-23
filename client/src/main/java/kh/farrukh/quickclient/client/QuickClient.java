package kh.farrukh.quickclient.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.logging.Logger;
import kh.farrukh.quickclient.config.QuickClientConfig;
import kh.farrukh.quickclient.rpc.QuizzerImpl;

public class QuickClient implements AutoCloseable {

  private static final Logger logger = Logger.getLogger(QuickClient.class.getName());

  private final QuickClientConfig clientConfig;
  private ManagedChannel channel;
  private QuizzerImpl quizzer;

  public QuickClient(QuickClientConfig clientConfig) {
    this.clientConfig = clientConfig;
    connect();
  }

  private void connect() {
    channel = ManagedChannelBuilder
        .forAddress(clientConfig.getServerHost(), clientConfig.getServerPort())
        .usePlaintext()
        .build();

    quizzer = new QuizzerImpl(channel);
  }

  public String getQuestion(String username) {
    return quizzer.getQuestion(username);
  }

  @Override
  public void close() {
    try {
      channel.shutdown().awaitTermination(5, java.util.concurrent.TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      logger.info("Error on closing channel: " + e.getMessage());
    }
  }
}
