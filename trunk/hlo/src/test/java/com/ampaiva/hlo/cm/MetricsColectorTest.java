package com.ampaiva.hlo.cm;

import static org.junit.Assert.assertEquals;
import japa.parser.ParseException;

import org.junit.Test;

public class MetricsColectorTest {

    @Test
    public void testGetMetrics() throws ParseException {
        MetricsColector colector = new MetricsColector("src/test/resources/com/ampaiva/in/cm");
        ConcernMetricsTable concernMetricsTable = colector.getMetrics();
        assertEquals(4, concernMetricsTable.getHash().size());
    }

}
