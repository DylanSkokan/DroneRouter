/* Author: Ralph A. Foy
 * Class : ICS340 Spring 2022
 * 
 *       Copyright (c) 2022 
 *       Authorization is given to students enrolled in the course to reproduce 
 *       this material exclusively for their own personal use.
 */
package demo;

import static org.junit.jupiter.api.Assertions.*;

import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;

class GraphDemo {

private static final Logger LOGGER = Logger.getLogger(GraphDemo.class.getName());
	
	@Test
	void testSimpleGraph() {
		MutableValueGraph<String, Integer> g = ValueGraphBuilder.undirected().build();
		g.putEdgeValue("AA", "BB", 42);
		LOGGER.info(g.toString());
		g.putEdgeValue("AA", "CC", 12);
		LOGGER.info(g.toString());
		assertEquals(42, g.edgeValue("AA", "BB").get());
		assertEquals(g.edgeValue("AA", "BB"), g.edgeValue("BB", "AA"));

		assertTrue(g.edgeValue("AA", "CC").isPresent());
		assertFalse(g.edgeValue("BB", "CC").isPresent());
		assertEquals(2, g.adjacentNodes("AA").size());
	}

}
