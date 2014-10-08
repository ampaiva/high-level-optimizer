package com.ampaiva.hlo.cm;

import static org.junit.Assert.assertEquals;
import japa.parser.ParseException;

import org.junit.Test;

public class MetricsColectorTest {

    @Test
    public void testGetMetrics() throws ParseException {
        MetricsColector colector = new MetricsColector().addFolder("src/test/resources/com/ampaiva/in/cm");
        ConcernMetricsTable concernMetricsTable = colector.getMetrics();
        assertEquals(5, concernMetricsTable.getHash().size());
    }

    @Test
    public void testGetMetricsOfSpecific() throws ParseException {
        MetricsColector colector = new MetricsColector().addFiles("src/test/resources/com/ampaiva/in/cm",
                "AddressRepositoryRDB");
        ConcernMetricsTable concernMetricsTable = colector.getMetrics();
        assertEquals(1, concernMetricsTable.getHash().size());
        assertEquals(1, concernMetricsTable.getHash().entrySet().iterator().next().getValue().getValue());
    }

}
