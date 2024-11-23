package kh.farrukh.quickclient;

import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import java.util.Scanner;
import java.util.logging.Logger;
import kh.farrukh.quickclient.quizzer.GetQuestionRequest;
import kh.farrukh.quickclient.quizzer.QuizzerGrpc;

public class QuickClientApp {

  private static final Logger logger = Logger.getLogger(QuickClientApp.class.getName());
  private static final String SERVER_ADDRESS = "localhost:9090";

  private final QuizzerGrpc.QuizzerBlockingStub quizzerStub;

  public QuickClientApp(Channel channel) {
    this.quizzerStub = QuizzerGrpc.newBlockingStub(channel);
  }

  private String getQuestion(String username) {
    var request = GetQuestionRequest.newBuilder().setUsername(username).build();
    try {
      var response = quizzerStub.getQuestion(request);
      return response.getQuestion();
    } catch (Exception e) {
      logger.info("Error on getting question: " + e.getMessage());
      return "ERROR";
    }
  }

  public static void main(String[] args) {
    var channel = ManagedChannelBuilder.forTarget(SERVER_ADDRESS)
        .usePlaintext()
        .build();
    var client = new QuickClientApp(channel);

    System.out.print("Enter your username to start quiz: ");
    var username = new Scanner(System.in).nextLine();

    var question = client.getQuestion(username);
    System.out.println("Question: " + question);

    try {
      channel.shutdown().awaitTermination(5, java.util.concurrent.TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      logger.info("Error on shutting down channel: " + e.getMessage());
    }
  }

}
