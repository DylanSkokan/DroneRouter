package edu.metrostate.ics340.p2.ds5860;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;

/**
 * <h2>GraphTests</h2> Provides a variety of JUnitTests to demonstrate the
 * functionality of the DroneRouter program.
 * 
 * @author Dylan Skokan
 * @since 2/23/22
 */
public class GraphTests {

	@Test
	void scenario0() {
		Path resourceDirectory = Path.of("src", "test", "resources", "scenario0.txt");
		String absolutePath = resourceDirectory.toFile().getAbsolutePath();

		var router = new DroneRouter();
		router.loadRoutes(absolutePath, "Ironfall");

		String[] expectedShortestPath = { "Ironfall", "Automahelm", "Cooperward" };

		assertArrayEquals(router.getRoute("Cooperward"), expectedShortestPath);
		assertEquals(router.getPathCost("Cooperward"), 4);

		router.loadRoutes(absolutePath, "Automahelm");

		String[] expectedShortestPath1 = { "Automahelm", "Cooperward", "Titferburn" };

		assertArrayEquals(router.getRoute("Titferburn"), expectedShortestPath1);
		assertEquals(router.getPathCost("Titferburn"), 7);

		router.loadRoutes(absolutePath, "Ironfall");

		String[] expectedShortestPath2 = { "Ironfall", "Automahelm", "Cooperward", "Titferburn" };

		assertArrayEquals(router.getRoute("Titferburn"), expectedShortestPath2);
		assertEquals(router.getPathCost("Titferburn"), 8);
	}

	@Test
	void scenario1() {
		Path resourceDirectory = Path.of("src", "test", "resources", "scenario1.txt");
		String absolutePath = resourceDirectory.toFile().getAbsolutePath();

		var router = new DroneRouter();
		router.loadRoutes(absolutePath, "A");

		String[] expectedShortestPath = { "A", "C", "G", "H", "F" };

		assertArrayEquals(router.getRoute("F"), expectedShortestPath);
		assertEquals(router.getPathCost("F"), 16);
		
		router.loadRoutes(absolutePath, "F");
		
		String[] expectedShortestPath1 = { };

		assertEquals(router.getPathCost("G"), -1);
		assertArrayEquals(router.getRoute("G"), expectedShortestPath1);
	}

	@Test
	void scenario2() {
		Path resourceDirectory = Path.of("src", "test", "resources", "scenario2.txt");
		String absolutePath = resourceDirectory.toFile().getAbsolutePath();

		var router = new DroneRouter();
		router.loadRoutes(absolutePath, "A");

		String[] expectedShortestPath = { "A", "B", "C", "E" };

		assertArrayEquals(expectedShortestPath, router.getRoute("E"));
		assertEquals(router.getPathCost("E"), 4);
		assertEquals(router.getPathCost("M"), -1);
	}

	@Test
	void scenario3() {
		Path resourceDirectory = Path.of("src", "test", "resources", "scenario3.txt");
		String absolutePath = resourceDirectory.toFile().getAbsolutePath();

		var router = new DroneRouter();
		router.loadRoutes(absolutePath, "A");

		String[] expectedShortestPath = { "A", "H", "F" };

		assertArrayEquals(expectedShortestPath, router.getRoute("F"));
		assertEquals(router.getPathCost("A"), 0);
		assertEquals(router.getPathCost("F"), 5);

		router.loadRoutes(absolutePath, "H");

		String[] expectedShortestPath1 = { "H", "F", "A", "D" };

		assertArrayEquals(expectedShortestPath1, router.getRoute("D"));
		assertEquals(router.getPathCost("D"), 8);
	}

	@Test
	void scenario4() {
		Path resourceDirectory = Path.of("src", "test", "resources", "scenario4.txt");
		String absolutePath = resourceDirectory.toFile().getAbsolutePath();

		var router = new DroneRouter();
		router.loadRoutes(absolutePath, "Z");

		assertEquals(router.getPathCost("A"), 2);
		assertEquals(router.getPathCost("F"), -1);
	}

	@Test
	void sameSourceDest() {
		Path resourceDirectory = Path.of("src", "test", "resources", "sameSourceDest.txt");
		String absolutePath = resourceDirectory.toFile().getAbsolutePath();

		var router = new DroneRouter();
		router.loadRoutes(absolutePath, "A");

		String[] expectedShortestPath = { "A" };

		assertArrayEquals(expectedShortestPath, router.getRoute("A"));
		assertEquals(router.getPathCost("A"), 0);
	}
	
	@Test
	void reusingObject() {
		Path resourceDirectory = Path.of("src", "test", "resources", "scenario0.txt");
		String absolutePath = resourceDirectory.toFile().getAbsolutePath();

		var router = new DroneRouter();
		router.loadRoutes(absolutePath, "Ironfall");

		String[] expectedShortestPath = { "Ironfall", "Automahelm", "Cooperward" };

		assertArrayEquals(router.getRoute("Cooperward"), expectedShortestPath);
		assertEquals(router.getPathCost("Cooperward"), 4);
		
		resourceDirectory = Path.of("src", "test", "resources", "scenario1.txt");
		absolutePath = resourceDirectory.toFile().getAbsolutePath();

		router.loadRoutes(absolutePath, "A");

		String[] expectedShortestPath1 = { "A", "C", "G", "H", "F" };

		assertArrayEquals(router.getRoute("F"), expectedShortestPath1);
		assertEquals(router.getPathCost("F"), 16);
	}

	@Test
	void twoWaypoint() {
		Path resourceDirectory = Path.of("src", "test", "resources", "2waypoint.txt");
		String absolutePath = resourceDirectory.toFile().getAbsolutePath();

		var router = new DroneRouter();
		router.loadRoutes(absolutePath, "A");

		assertEquals(router.getPathCost("B"), 2);
	}

	@Test
	void noFile() {
		Path resourceDirectory = Path.of("src", "test", "resources", "doesNotExist.txt");
		String absolutePath = resourceDirectory.toFile().getAbsolutePath();

		var router = new DroneRouter();

		assertThrows(IllegalArgumentException.class, () -> router.loadRoutes(absolutePath, "A"),
				"Did not throw IllegalArgumentException for noFile");
	}

	@Test
	void noSource() {
		Path resourceDirectory = Path.of("src", "test", "resources", "scenario1.txt");
		String absolutePath = resourceDirectory.toFile().getAbsolutePath();

		var router = new DroneRouter();

		assertThrows(IllegalArgumentException.class, () -> router.loadRoutes(absolutePath, "Y"),
				"Did not throw IllegalArgumentException for noSource");
	}

	@Test
	void noDestination() {
		Path resourceDirectory = Path.of("src", "test", "resources", "scenario1.txt");
		String absolutePath = resourceDirectory.toFile().getAbsolutePath();

		var router = new DroneRouter();
		router.loadRoutes(absolutePath, "D");

		assertThrows(IllegalArgumentException.class, () -> router.getRoute("N"),
				"Did not throw IllegalArgumentException for noDestination");
		assertThrows(IllegalArgumentException.class, () -> router.getPathCost("N"),
				"Did not throw IllegalArgumentException for noDestination");
	}

	@Test
	void noPath() {
		Path resourceDirectory = Path.of("src", "test", "resources", "scenario1.txt");
		String absolutePath = resourceDirectory.toFile().getAbsolutePath();

		var router = new DroneRouter();
		router.loadRoutes(absolutePath, "F");
		
		String[] expectedShortestPath = { };

		assertEquals(router.getPathCost("G"), -1);
		assertArrayEquals(router.getRoute("G"), expectedShortestPath);
	}

	@Test
	void nullException() {
		var router = new DroneRouter();

		assertThrows(NullPointerException.class, () -> router.getPathCost(null),
				"Did not throw NullPointerException for nullExceptions");
	}
}