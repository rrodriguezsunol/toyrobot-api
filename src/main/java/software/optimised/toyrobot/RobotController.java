package software.optimised.toyrobot;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class RobotController {

  // stores robots in memory until the app is restarted
  private static ConcurrentMap<String, Robot> robots = new ConcurrentHashMap<>();

  @PostMapping("/robot")
  public Robot createRobot() {
    Robot robot = new Robot();
    robots.put(robot.getId().toString(), robot);
    return robot;
  }

  @GetMapping("/robot/{id}")
  public Robot getRobot(@PathVariable String id) {
    return robots.get(id);
  }

  @GetMapping("/robots")
  public ConcurrentMap<String, Robot> getRobots() {
    return robots;
  }

  @PostMapping("/robot/{id}/place")
  public Robot place(@PathVariable String id, @RequestBody Robot newRobot) {
    return robots.get(id).place(newRobot.getLocationX(), newRobot.getLocationY(), newRobot.getOrientation());
  }

  @PostMapping("/robot/{id}/move")
  public Robot move(@PathVariable String id) {
    return robots.get(id).move();
  }

  @PostMapping("/robot/{id}/left")
  public Robot left(@PathVariable String id) {
    return robots.get(id).left();
  }

  @PostMapping("/robot/{id}/right")
  public Robot right(@PathVariable String id) {
    return robots.get(id).right();
  }

  @PostMapping("/robot/{id}/report")
  public Robot report(@PathVariable String id) {
    Robot robot = robots.get(id);
    robot.report();
    return robot;
  }
}
