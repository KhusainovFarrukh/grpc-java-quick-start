package kh.farrukh.quickserver;

import io.grpc.Grpc;
import io.grpc.InsecureServerCredentials;
import io.grpc.Server;
import io.grpc.stub.StreamObserver;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Logger;
import kh.farrukh.quickserver.quizzer.GetQuestionRequest;
import kh.farrukh.quickserver.quizzer.GetQuestionResponse;
import kh.farrukh.quickserver.quizzer.QuizzerGrpc.QuizzerImplBase;

public class GrpcQuickServerApp {

  private static final Logger logger = Logger.getLogger(GrpcQuickServerApp.class.getName());

  private Server server;

  private void start() throws InterruptedException, IOException {
    var port = 9090;
    server = Grpc.newServerBuilderForPort(port, InsecureServerCredentials.create())
        .addService(new QuizzerImpl())
        .build()
        .start();
    logger.info("Server started, listening on " + port);

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      System.err.println("*** shutting down gRPC server since JVM is shutting down");
      try {
        GrpcQuickServerApp.this.stop();
      } catch (InterruptedException e) {
        e.printStackTrace(System.err);
      }
      System.err.println("*** server shut down");
    }));
  }

  private void stop() throws InterruptedException {
    if (server != null) {
      logger.info("Stopping server...");
      server.shutdown().awaitTermination();
      logger.info("Server stopped");
    }
  }

  private void blockUntilShutdown() throws InterruptedException {
    if (server != null) {
      server.awaitTermination();
    }
  }

  public static void main(String[] args) {
    var server = new GrpcQuickServerApp();
    try {
      server.start();
      server.blockUntilShutdown();
    } catch (InterruptedException e) {
      logger.severe("Server interrupted");
    } catch (IOException e) {
      logger.severe("Server failed to start");
    }
  }

  static class QuizzerImpl extends QuizzerImplBase {

    @Override
    public void getQuestion(
        GetQuestionRequest request,
        StreamObserver<GetQuestionResponse> responseObserver
    ) {
      logger.info("Received request from " + request.getUsername());
      var response = GetQuestionResponse.newBuilder()
          .setId(new Random().nextInt(1000))
          .setUsername(request.getUsername())
          .setQuestion("How software engineer names his/her first project?")
          .build();

      responseObserver.onNext(response);
      responseObserver.onCompleted();

      logger.info(
          "Response sent to " + request.getUsername() + " with question id " + response.getId()
      );
    }
  }

}
