package software.optimised.toyrobot;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import software.optimised.toyrobot.Robot;
import software.optimised.toyrobot.Robot.Orientation;

@RunWith(SpringRunner.class)
public class RobotControllerTest {

  // If I had more time, I would write tests for invalid requests and a few more workflows,
  // however, the RobotController class only forwards requests to the Robot class, which is completely tested
  // so it's not absolutely necessary in this case

  @Test
  public void workflowTest() {
    RobotController controller = new RobotController();

    String uuid1 = controller.createRobot().getId().toString();
    assertNotNull(uuid1);

    Robot robotParams = new Robot();
    robotParams.place(1, 2, Orientation.SOUTH);
    controller.place(uuid1, robotParams);
    Robot robot1 = controller.getRobot(uuid1);
    assertEquals(1, robot1.getLocationX());
    assertEquals(2, robot1.getLocationY());
    assertEquals(Orientation.SOUTH, robot1.getOrientation());

    String uuid2 = controller.createRobot().getId().toString();

    assertEquals(controller.getRobots().size(), 2);

    controller.left(uuid1);
    controller.left(uuid1);
    controller.move(uuid1);
    controller.right(uuid1);
    controller.move(uuid1);

    robot1 = controller.getRobot(uuid1);
    assertEquals(2, robot1.getLocationX());
    assertEquals(3, robot1.getLocationY());
    assertEquals(Orientation.EAST, robot1.getOrientation());

    Robot robot2 = controller.getRobot(uuid2);
    assertEquals(0, robot2.getLocationX());
    assertEquals(0, robot2.getLocationY());
    assertNull(robot2.getOrientation());
  }

}
