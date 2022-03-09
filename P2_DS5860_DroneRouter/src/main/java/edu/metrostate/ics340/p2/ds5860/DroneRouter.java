package edu.metrostate.ics340.p2.ds5860;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;

/**
 * <h2>DroneRouter</h2> The DroneRouter program finds the shortest path between
 * a source waypoint and a destination waypoint by constructing a graph from a
 * given text file and using a Dijkstra's algorithm implementation.
 * 
 * @author Dylan Skokan
 * @since 2/23/22
 * 
 * <p>
 * Sources used:
 * </p>
 * - https://www.baeldung.com/java-dijkstra <br>
 * - https://www.youtube.com/watch?v=k1kLCB7AZbM <br>
 * - https://stackoverflow.com/questions/4042434/converting-arrayliststring-to-string-in-java
 */
public class DroneRouter implements edu.metrostate.ics340.p2.Router {
	private MutableValueGraph<String, Integer> graph = ValueGraphBuilder.directed().allowsSelfLoops(true).build();
	private HashMap<String, Integer> costScores = new HashMap<String, Integer>();
	private HashMap<String, Boolean> searched = new HashMap<String, Boolean>();
	private HashMap<String, String> prevWaypoints = new HashMap<String, String>();
	private String startWaypoint;

	DroneRouter() {}

	public void loadRoutes(String routesFilePath, String source) {
		Path fileName = null;
		String fileText = "";

		try {
			fileName = Path.of(routesFilePath);
			fileText = Files.readString(fileName);
		} catch (IllegalArgumentException | IOException e) {
			throw new IllegalArgumentException("File is either inaccessible or does not exist.");
		}

		if (!graph.nodes().isEmpty()) {
			costScores.clear();
			searched.clear();
			prevWaypoints.clear();
		}
		
		readFileText(fileText);

		if (!graph.nodes().contains(source)) {
			throw new IllegalArgumentException("Source location does not exist.");
		}

		startWaypoint = source;
	}

	private void readFileText(String fileText) {
		String fromDest = "", toDest = "", routeCost = "";
		int stage = 0, i;
		for (i = 0; i < fileText.length(); i++) {
			char currChar = fileText.charAt(i);
			if (stage == 0) {
				if ((Character.isWhitespace(currChar)) && !fromDest.equals("")) {
					stage = 1;
				} else if (Character.isAlphabetic(currChar)) {
					fromDest += currChar;
				}
			} else if (stage == 1) {
				if ((Character.isWhitespace(currChar)) && !toDest.equals("")) {
					stage = 2;
				} else if (Character.isAlphabetic(currChar)) {
					toDest += currChar;
				}
			} else if (stage == 2) {
				if (currChar == '\n' || i == fileText.length() - 1) {
					stage = 3;
				}
				if (Character.isDigit(currChar)) {
					routeCost += currChar;
				}
			}
			if (stage == 3) {
				graph.putEdgeValue(fromDest, toDest, Integer.parseInt(routeCost));
				fromDest = "";
				toDest = "";
				routeCost = "";
				stage = 0;
			}
		}
	}

	private void dijkstraSearch(String destination) {
		Set<String> allWaypoints = graph.nodes();
		String currWaypoint = startWaypoint, nextWaypoint = "";

		for (String tempWaypoint : allWaypoints) {
			int tempInt = (tempWaypoint.equals(currWaypoint)) ? 0 : Integer.MAX_VALUE;
			costScores.put(tempWaypoint, tempInt);
			searched.put(tempWaypoint, false);
		}

		while (currWaypoint != null) {
			var successNodes = graph.successors(currWaypoint);
			int minCostScore = Integer.MAX_VALUE;
			int validPathCounter = 0;

			for (String currSuccessNode : successNodes) {

				int pathCost = (costScores.get(currWaypoint) == Integer.MAX_VALUE) ? costScores.get(currWaypoint)
						: costScores.get(currWaypoint) + graph.edgeValue(currWaypoint, currSuccessNode).get();

				if (pathCost < costScores.get(currSuccessNode)) {
					costScores.put(currSuccessNode, pathCost);
					prevWaypoints.put(currSuccessNode, currWaypoint);
				}

				if (searched.get(currSuccessNode) == false) {
					validPathCounter++;
					if (pathCost < minCostScore) {
						minCostScore = pathCost;
						nextWaypoint = currSuccessNode;
					}
				}
			}
			searched.put(currWaypoint, true);
			currWaypoint = (validPathCounter == 0) ? prevWaypoints.get(currWaypoint) : nextWaypoint;
		}
	}

	public String[] getRoute(String destination) {
		if (!graph.nodes().contains(destination)) {
			throw new IllegalArgumentException("Destination does not exist.");
		}
		if (destination.equals(startWaypoint)) {
			return new String[] { startWaypoint };
		}

		var shortestPath = new ArrayList<String>();
		String currWaypoint = destination;

		dijkstraSearch(destination);

		shortestPath.add(currWaypoint);
		while (prevWaypoints.containsKey(currWaypoint)) {
			currWaypoint = prevWaypoints.get(currWaypoint);
			shortestPath.add(0, currWaypoint);
		}
		
		if(shortestPath.size() == 1) {
			shortestPath.clear();
		}

		return shortestPath.toArray(String[]::new);
	}

	public int getPathCost(String destination) {
		if (destination == null) {
			throw new NullPointerException("Null cannot be destination.");
		}
		if (!graph.nodes().contains(destination)) {
			throw new IllegalArgumentException("Destination does not exist.");
		}
		if (destination.equals(startWaypoint)) {
			return 0;
		}

		dijkstraSearch(destination);

		int costScore = (costScores.get(destination) == Integer.MAX_VALUE) ? NO_ROUTE : costScores.get(destination);

		return costScore;
	}
}