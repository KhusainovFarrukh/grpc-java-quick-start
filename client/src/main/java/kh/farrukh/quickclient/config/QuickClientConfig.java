package kh.farrukh.quickclient.config;

public class QuickClientConfig {

  private String serverHost = "localhost";
  private int serverPort = 9090;

  public String getServerHost() {
    return serverHost;
  }

  public void setServerHost(String serverHost) {
    this.serverHost = serverHost;
  }

  public int getServerPort() {
    return serverPort;
  }

  public void setServerPort(int serverPort) {
    this.serverPort = serverPort;
  }

}
