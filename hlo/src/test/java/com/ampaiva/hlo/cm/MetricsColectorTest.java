package com.ampaiva.hlo.cm;

import static org.junit.Assert.assertEquals;
import japa.parser.ParseException;

import java.io.ByteArrayInputStream;

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
        assertEquals(32, concernMetricsTable.getHash().entrySet().iterator().next().getValue().getMetric());
    }

    @Test
    public void testGetMetricsOfInputStream() throws ParseException {
        StringBuilder sb = new StringBuilder();
        sb.append("package com.ampaiva;\n");
        sb.append("class C {");
        sb.append("{try{\n");
        sb.append("\n");
        sb.append("}catch (Exception e){}}");
        sb.append("}");
        MetricsColector colector = new MetricsColector().addInputStream(new ByteArrayInputStream(sb.toString()
                .getBytes()));
        ConcernMetricsTable concernMetricsTable = colector.getMetrics();
        assertEquals(1, concernMetricsTable.getHash().size());
    }
}
