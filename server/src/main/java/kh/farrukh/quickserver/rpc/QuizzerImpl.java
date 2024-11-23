package kh.farrukh.quickserver.rpc;

import io.grpc.stub.StreamObserver;
import java.util.Random;
import java.util.logging.Logger;
import kh.farrukh.quickserver.quizzer.GetQuestionRequest;
import kh.farrukh.quickserver.quizzer.GetQuestionResponse;
import kh.farrukh.quickserver.quizzer.QuizzerGrpc.QuizzerImplBase;

public class QuizzerImpl extends QuizzerImplBase {

  private static final Logger logger = Logger.getLogger(QuizzerImpl.class.getName());

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
