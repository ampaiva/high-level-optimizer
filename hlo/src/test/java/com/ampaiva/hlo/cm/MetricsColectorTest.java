package com.ampaiva.hlo.cm;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map.Entry;

import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ampaiva.hlo.util.SourceColector;
import com.github.javaparser.ParseException;

public class MetricsColectorTest extends EasyMockSupport {

    private MetricsColector colector;
    private IMetricsSource metricsSource;
    private ICodeSource codeSource;
    private IConcernMetric concernMetric;

    /**
     * Setup mocks before each test.
     * 
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        metricsSource = createMock(IMetricsSource.class);
        codeSource = createMock(ICodeSource.class);
        concernMetric = createMock(IConcernMetric.class);
        colector = new MetricsColector(metricsSource);
    }

    /**
     * Verifies all mocks after each test.
     */
    @After()
    public void tearDown() {
        verifyAll();
    }

    @Test
    public void testGetMetrics() throws ParseException, FileNotFoundException, IOException {
        SourceColector sources = new SourceColector().addFolder("src/test/resources/com/ampaiva/in/cm");
        expect(codeSource.getCodeSource()).andReturn(sources.getSources());
        for (Entry<String, String> entry : sources.getSources().entrySet()) {
            expect(metricsSource.getConcernMetrics()).andReturn(Arrays.asList(concernMetric));
            concernMetric.parse(entry.getValue());
        }

        replayAll();

        ConcernMetricsTable concernMetricsTable = colector.getMetrics(codeSource.getCodeSource());
        assertEquals(5, concernMetricsTable.getHash().size());
    }

    @Test
    public void testGetMetricsOfSpecific() throws ParseException, FileNotFoundException, IOException {
        SourceColector sources = new SourceColector().addFiles("src/test/resources/com/ampaiva/in/cm",
                "AddressRepositoryRDB");
        expect(codeSource.getCodeSource()).andReturn(sources.getSources());
        expect(metricsSource.getConcernMetrics()).andReturn(Arrays.asList(concernMetric));
        for (Entry<String, String> entry : sources.getSources().entrySet()) {
            concernMetric.parse(entry.getValue());
        }

        replayAll();

        ConcernMetricsTable concernMetricsTable = colector.getMetrics(codeSource.getCodeSource());
        assertEquals(1, concernMetricsTable.getHash().size());
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
        String source = sb.toString();
        expect(codeSource.getCodeSource()).andReturn(
                new SourceColector().addInputStream("", new ByteArrayInputStream(source.getBytes())).getSources());

        expect(metricsSource.getConcernMetrics()).andReturn(Arrays.asList(concernMetric));
        concernMetric.parse(source);
        replayAll();

        ConcernMetricsTable concernMetricsTable = colector.getMetrics(codeSource.getCodeSource());
        assertEquals(1, concernMetricsTable.getHash().size());
    }
}
