package kh.farrukh.quickserver.server;

import io.grpc.BindableService;
import io.grpc.Grpc;
import io.grpc.InsecureServerCredentials;
import io.grpc.Server;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import kh.farrukh.quickserver.config.QuickServerConfig;

public class QuickServer {

  private static final Logger logger = Logger.getLogger(QuickServer.class.getName());

  private Server server;
  private final QuickServerConfig serverConfig;
  private final List<? extends BindableService> services;

  public QuickServer(
      QuickServerConfig serverConfig,
      List<? extends BindableService> services
  ) {
    Objects.requireNonNull(serverConfig, "Server config must not be null");
    Objects.requireNonNull(services, "Services must not be null");

    this.serverConfig = serverConfig;
    this.services = services;
  }

  public void start() {
    logger.info("Starting server...");

    var port = serverConfig.getPort();
    var builder = Grpc.newServerBuilderForPort(port, InsecureServerCredentials.create());
    services.forEach(builder::addService);
    server = builder.build();

    try {
      server.start();
    } catch (IOException e) {
      logger.severe("Server failed to start");
      return;
    }

    logger.info("Server started, listening on " + port);

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      System.err.println("*** shutting down gRPC server since JVM is shutting down");
      try {
        stop();
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

  public void blockUntilShutdown() {
    if (server != null) {
      try {
        server.awaitTermination();
      } catch (InterruptedException e) {
        logger.severe("Server interrupted");
      }
    }
  }

}
