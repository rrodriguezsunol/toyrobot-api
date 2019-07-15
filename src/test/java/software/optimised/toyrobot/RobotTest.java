package software.optimised.toyrobot;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import software.optimised.toyrobot.Robot;
import software.optimised.toyrobot.Robot.Orientation;

@RunWith(SpringRunner.class)
public class RobotTest {

  private Robot robot;

  @Before
  public void setUp() {
    robot = new Robot();
  }

  private void assertLocation(int x, int y) {
    assertEquals(x, robot.getLocationX());
    assertEquals(y, robot.getLocationY());
  }

  @Test
  public void placeRobot() {
    robot.place(1, 2, Orientation.WEST);
    assertLocation(1, 2);
    assertEquals(Orientation.WEST, robot.getOrientation());

    robot.place(0, 0, Orientation.SOUTH);
    assertLocation(0, 0);
    assertEquals(Orientation.SOUTH, robot.getOrientation());
  }

  @Test
  public void ignoreInvalidPlacements() {
    robot.place(1, -2, Orientation.WEST);
    assertLocation(0, 0);
    assertNull(robot.getOrientation());

    robot.place(1, 2, Orientation.WEST);

    robot.place(-1, 0, Orientation.EAST);
    assertLocation(1, 2);
    assertEquals(Orientation.WEST, robot.getOrientation());

    robot.place(0, Application.gridSizeY, Orientation.EAST);
    assertLocation(1, 2);
    assertEquals(Orientation.WEST, robot.getOrientation());
  }

  @Test
  public void requirePlaceToMove() {
    robot.move();
    assertNull(robot.getOrientation());

    robot.place(0, 0, Orientation.NORTH).move();
    assertNotNull(robot.getOrientation());
  }


  @Test
  public void moveRobot() {
    robot.place(0, 0, Orientation.NORTH).move();
    assertLocation(0, 1);

    robot.move();
    assertLocation(0, 2);

    robot.place(3, 2, Orientation.SOUTH).move();
    assertLocation(3, 1);

    robot.move();
    assertLocation(3, 0);

    robot.place(1, Application.gridSizeY - 1, Orientation.EAST).move();
    assertLocation(2, Application.gridSizeY - 1);

    robot.place(3, Application.gridSizeY - 1, Orientation.WEST).move();
    assertLocation(2, Application.gridSizeY - 1);
  }

  @Test
  public void dontFallOff() {
    robot.place(0, 0, Orientation.SOUTH).move();
    assertLocation(0, 0);

    robot.place(0, 0, Orientation.WEST).move();
    assertLocation(0, 0);

    robot.place(Application.gridSizeX - 1, 0, Orientation.EAST).move();
    assertLocation(Application.gridSizeX - 1, 0);

    robot.place(0, Application.gridSizeY - 1, Orientation.NORTH).move();
    assertLocation(0, Application.gridSizeY - 1);
  }

  @Test
  public void rotateRight() {
    robot.place(0, 0, Orientation.NORTH).right();
    assertEquals(Orientation.EAST, robot.getOrientation());

    robot.right();
    assertEquals(Orientation.SOUTH, robot.getOrientation());

    robot.right().right();
    assertEquals(Orientation.NORTH, robot.getOrientation());
  }

  @Test
  public void rotateLeft() {
    robot.place(0, 0, Orientation.NORTH).left();
    assertEquals(Orientation.WEST, robot.getOrientation());

    robot.left();
    assertEquals(Orientation.SOUTH, robot.getOrientation());

    robot.left().left();
    assertEquals(Orientation.NORTH, robot.getOrientation());
  }

  @Test
  public void robotReport() {
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(outContent));

    robot.place(1, 2, Orientation.WEST).report();
    assertEquals("Output: 1, 2, WEST\n", outContent.toString());


    outContent.reset();
    robot.place(0, 0, Orientation.NORTH).report();
    assertEquals("Output: 0, 0, NORTH\n", outContent.toString());

    System.setOut(originalOut);
  }

}
