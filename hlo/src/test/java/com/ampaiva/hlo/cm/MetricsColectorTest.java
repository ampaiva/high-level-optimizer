package com.ampaiva.hlo.cm;

import static org.junit.Assert.assertEquals;
import japa.parser.ParseException;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

import com.ampaiva.hlo.util.SourceColector;

public class MetricsColectorTest {

    @Test
    public void testGetMetrics() throws ParseException, FileNotFoundException, IOException {
        MetricsColector colector = new MetricsColector(new SourceColector().addFolder(
                "src/test/resources/com/ampaiva/in/cm").getSources());
        ConcernMetricsTable concernMetricsTable = colector.getMetrics();
        assertEquals(5, concernMetricsTable.getHash().size());
    }

    @Test
    public void testGetMetricsOfSpecific() throws ParseException, FileNotFoundException, IOException {
        MetricsColector colector = new MetricsColector(new SourceColector().addFiles(
                "src/test/resources/com/ampaiva/in/cm", "AddressRepositoryRDB").getSources());
        ConcernMetricsTable concernMetricsTable = colector.getMetrics();
        assertEquals(1, concernMetricsTable.getHash().size());
        assertEquals(32, concernMetricsTable.getHash().entrySet().iterator().next().getValue().getMetric());
    }

    @Test
    public void testGetMetricsOfInputStream() throws ParseException, IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("package com.ampaiva;\n");
        sb.append("class C {");
        sb.append("{try{\n");
        sb.append("\n");
        sb.append("}catch (Exception e){}}");
        sb.append("}");
        MetricsColector colector = new MetricsColector(new SourceColector().addInputStream("",
                new ByteArrayInputStream(sb.toString().getBytes())).getSources());
        ConcernMetricsTable concernMetricsTable = colector.getMetrics();
        assertEquals(1, concernMetricsTable.getHash().size());
    }
}
