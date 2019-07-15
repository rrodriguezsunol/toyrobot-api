package software.optimised.toyrobot;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class ApplicationTest {

  public static final PrintStream originalOut = System.out;
  public static ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  public String fileName;

  @BeforeClass
  public static void setUpClass() {
    System.setOut(new PrintStream(outContent));
  }

  @AfterClass
  public static void cleanUpClass() {
    System.setOut(originalOut);
  }

  @Before
  public void before() {
    fileName = UUID.randomUUID().toString();
    outContent.reset();
  }

  @After
  public void after() throws IOException {
    Files.delete(Paths.get(fileName));
  }

  private void createFile(String[] instructions) throws IOException {
    String content = String.join("\n", instructions);
    Files.write(Paths.get(fileName), content.getBytes());
  }

  @Test
  public void parseValidFile() throws IOException {
    createFile(new String[] {
      "place 0,0,NORTH",
      "move",
      "move",
      "report",
    });

    Application.parseFile(fileName);

    assertEquals("Output: 0, 2, NORTH\n", outContent.toString());
  }

  @Test
  public void parseValidFileWithoutReport() throws IOException {
    createFile(new String[] {
      "place 3,2,SOUTH",
      "left",
      "move",
    });

    Application.parseFile(fileName);

    assertEquals("", outContent.toString());
  }

  @Test
  public void parseValidFile2() throws IOException {
    createFile(new String[] {
      "place 0,0,NORTH",
      "move",
      "report",
      "place 0, 0, NORTH",
      "left",
      "report",
      "place 1,2,EAST",
      "move",
      "move",
      "left",
      "move",
      "report",
    });

    Application.parseFile(fileName);

    assertEquals(
      "Output: 0, 1, NORTH\n" +
      "Output: 0, 0, WEST\n" +
      "Output: 3, 3, NORTH\n"
    , outContent.toString());
  }

  @Test
  public void invalidOrientation() throws IOException {
    createFile(new String[] {
      "place 3,2,SOUTD",
      "left",
      "move",
      "report",
    });

    Application.parseFile(fileName);

    assertEquals("Output: 0, 0, null\n", outContent.toString());
  }

  @Test
  public void locationOutOfBounds() throws IOException {
    createFile(new String[] {
      "place " + Application.gridSizeX + ",2,SOUTH",
      "move",
      "report",
    });

    Application.parseFile(fileName);

    assertEquals("Output: 0, 0, null\n", outContent.toString());
  }

  @Test
  public void emptyFile() throws IOException {
    createFile(new String[] {});

    Application.parseFile(fileName);

    assertEquals("", outContent.toString());
  }

}
