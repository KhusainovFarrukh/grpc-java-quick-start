package kh.farrukh.quickclient.rpc;

import io.grpc.Channel;
import java.util.logging.Logger;
import kh.farrukh.quickclient.quizzer.GetQuestionRequest;
import kh.farrukh.quickclient.quizzer.QuizzerGrpc;

public class QuizzerImpl {

  private static final Logger logger = Logger.getLogger(QuizzerImpl.class.getName());

  private final QuizzerGrpc.QuizzerBlockingStub quizzerStub;

  public QuizzerImpl(Channel channel) {
    quizzerStub = QuizzerGrpc.newBlockingStub(channel);
  }

  public String getQuestion(String username) {
    var request = GetQuestionRequest.newBuilder().setUsername(username).build();
    try {
      var response = quizzerStub.getQuestion(request);
      return response.getQuestion();
    } catch (Exception e) {
      logger.info("Error on getting question: " + e.getMessage());
      throw new RuntimeException(e);
    }
  }

}
