// Robot class is responsible for the placement and navigation of a robot on a grid (table)
// all actions return `this` to allow method chaining e.g. robot.left().move().move()

package software.optimised.toyrobot;

import java.util.UUID;

public class Robot {

  enum Orientation {
    NORTH, EAST, SOUTH, WEST;

    private static Orientation[] orientations = values();
    public Orientation right() {
      return orientations[(this.ordinal() + 1) % orientations.length];
    }
    public Orientation left() {
      return orientations[(orientations.length + this.ordinal() - 1) % orientations.length];
    }
  }

  private UUID id = UUID.randomUUID();
  private int locationX;
  private int locationY;
  private Orientation orientation;

  public UUID getId() {
    return id;
  }

  public int getLocationX() {
    return locationX;
  }

  public int getLocationY() {
    return locationY;
  }

  public Orientation getOrientation() {
    return orientation;
  }

  public Robot right() {
    if (orientation != null) orientation = orientation.right();

    return this;
  }

  public Robot left() {
    if (orientation != null) orientation = orientation.left();

    return this;
  }

  public Robot place(int locationX, int locationY, Orientation orientation) {
    if (locationX < 0 || locationY < 0 || locationX >= Application.gridSizeX || locationY >= Application.gridSizeY) {
      return this;
    }

    this.locationX = locationX;
    this.locationY = locationY;
    this.orientation = orientation;

    return this;
  }

  // move the robot if it's already placed, in the direction it's facing, while preventing falls
  public Robot move() {
    if (orientation == Orientation.NORTH && locationY + 1 < Application.gridSizeY) locationY += 1;
    else if (orientation == Orientation.SOUTH && locationY > 0) locationY -= 1;
    else if (orientation == Orientation.EAST && locationX + 1 < Application.gridSizeX) locationX += 1;
    else if (orientation == Orientation.WEST && locationX > 0) locationX -= 1;

    return this;
  }

  public void report() {
    System.out.printf("Output: %d, %d, %s\n", locationX, locationY, orientation);
  }

}
