package kh.farrukh.quickclient;

import java.util.Scanner;
import kh.farrukh.quickclient.client.QuickClient;
import kh.farrukh.quickclient.config.QuickConfig;

public class QuickClientApp {

  public static void main(String[] args) {
    var config = new QuickConfig();

    try (var client = new QuickClient(config.getClient())) {
      System.out.print("Enter your username to start quiz: ");
      var username = new Scanner(System.in).nextLine();

      var question = client.getQuestion(username);
      System.out.println("Question: " + question);
    }
  }

}
