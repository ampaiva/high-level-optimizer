package com.ampaiva.hlo;

import japa.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import com.ampaiva.hlo.cm.ConcernMetric;
import com.ampaiva.hlo.cm.MetricsColector;
import com.ampaiva.hlo.util.Helper;

public class Main {
    public MetricsColector getMetrics(Map<String, String> sources) throws ParseException, FileNotFoundException,
            IOException {
        MetricsColector metricsColector = new MetricsColector(sources);
        printMetrics(metricsColector);
        return metricsColector;
    }

    private void printMetrics(MetricsColector metricsColector) throws ParseException {
        TreeSet<String> set = new TreeSet<String>();
        for (Entry<String, List<ConcernMetric>> entry : metricsColector.getMetrics().getHash().entrySet()) {
            String key = entry.getKey().split("\\.")[entry.getKey().split("\\.").length - 1];
            for (ConcernMetric concernMetric : entry.getValue()) {
                int value = concernMetric.getMetric();
                set.add(key + "=" + value);
            }
        }
        for (String string : set) {
            System.out.println(string);
        }
    }

    public List<String> getGitHubFiles(String repoName) throws IOException {
        List<String> sources = new ArrayList<String>();
        GitHub gitHub = GitHub.connectAnonymously();
        GHRepository repository = gitHub.getRepository(repoName);
        listFiles(sources, repository, "/");
        return sources;
    }

    private void listFiles(List<String> sources, final GHRepository repository, String path) throws IOException {
        List<GHContent> folderList = repository.getDirectoryContent(path);
        for (GHContent ghContent : folderList) {
            if (ghContent.isDirectory()) {
                listFiles(sources, repository, ghContent.getPath());
            } else if (ghContent.getName().toLowerCase().endsWith(".java")) {
                System.out.println(ghContent.getPath());
                sources.add(ghContent.getContent());
            }
        }
    }

    private Map<String, String> getFilesInZip(String zipFilePath) throws IOException {
        Map<String, String> sources = new HashMap<String, String>();
        ZipFile zipFile = new ZipFile(zipFilePath);
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            if (entry.isDirectory() || !entry.getName().toLowerCase().endsWith(".java")) {
                continue;
            }
            System.out.println(entry.getName());
            sources.put(entry.getName(), Helper.convertInputStream2String(zipFile.getInputStream(entry)));
        }
        zipFile.close();
        return sources;
    }

    public void getMetricsofAllFiles(String folder) throws Exception {
        File[] files = new File(folder).listFiles(new FilenameFilter() {

            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".zip");
            }
        });
        for (File zipFile : files) {
            MetricsColector metricsColector = getMetrics(getFilesInZip(zipFile.getAbsolutePath()));
            printMetrics(metricsColector);
        }
    }

    public static void main(String[] args) throws Exception {
        Main main = new Main();
        // final String PROJECT_SRC = "../HW/src";
        // main.getMetrics(new SourceColector().addFiles(PROJECT_SRC, ".*\\.java").getSources());
        // String url = "https://github.com/junit-team/junit/archive/master.zip";
        String folder = "C:/opt/tools/target-projects";
        main.getMetricsofAllFiles(folder);
    }
}
