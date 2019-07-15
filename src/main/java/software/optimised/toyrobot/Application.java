package software.optimised.toyrobot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import software.optimised.toyrobot.Robot.Orientation;

@SpringBootApplication
public class Application {

  public static int gridSizeX = 5;
  public static int gridSizeY = 5;
  private static Map<String, String> argMap = new HashMap<>();

  public static void main(String[] args) {
    parseArgs(args);

    // get grid size from the command line args
    try{
      if (argMap.get("gridSizeX") != null && argMap.get("gridSizeY") != null) {
        gridSizeX = Integer.parseInt(argMap.get("gridSizeX"));
        gridSizeY = Integer.parseInt(argMap.get("gridSizeY"));
      }
    } catch (NumberFormatException e) {
      System.err.println("Grid size should be defined by whole numbers");
      System.exit(1);
    }

    if (argMap.get("file") != null) {
      parseFile(argMap.get("file"));
    } else {
      SpringApplication.run(Application.class, args);
    }

  }

  private static void parseArgs(String[] args) {
    for (String arg : args) {
      if (arg.startsWith("--")) arg = arg.substring(2);
      if (arg.startsWith("-")) arg = arg.substring(1);

      String[] tokens = arg.split("=");
      if (tokens.length == 2) argMap.put(tokens[0], tokens[1]);
    }
  }

  public static void parseFile(String fileName) {
    Robot robot = new Robot();

    try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
      stream.forEach(action -> {
        String[] tokens = Stream.of(action.split(",| ")).filter(token -> !token.isEmpty()).toArray(String[]::new);

        try {
          if (tokens[0].equals("place")) {
            robot.place(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]), Orientation.valueOf(tokens[3]));
          } else {
            Robot.class.getMethod(tokens[0]).invoke(robot); // try to call a method from string (e.g. move, left)
          }
        } catch (Exception ignore) {} // ignore invalid commands
      });
    } catch (IOException e) {
      System.err.println("Failed to load file " + fileName);
      System.exit(1);
    }
  }

}
