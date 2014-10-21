package com.ampaiva.hlo;

import japa.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeSet;

import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import com.ampaiva.hlo.cm.ConcernMetric;
import com.ampaiva.hlo.cm.MetricsColector;

public class Main {
    public void getMetrics(List<String> sources) throws ParseException, FileNotFoundException, IOException {
        MetricsColector metricsColector = new MetricsColector(sources);
        TreeSet<String> set = new TreeSet<String>();
        for (Entry<String, ConcernMetric> entry : metricsColector.getMetrics().getHash().entrySet()) {
            String key = entry.getKey().split("\\.")[entry.getKey().split("\\.").length - 1];
            int value = entry.getValue().getMetric();
            set.add(key + "=" + value);
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

    public void listFiles(List<String> sources, final GHRepository repository, String path) throws IOException {
        List<GHContent> folderList = repository.getDirectoryContent(path);
        for (GHContent ghContent : folderList) {
            if (ghContent.isDirectory()) {
                listFiles(sources, repository, ghContent.getPath());
            } else if (ghContent.getName().toLowerCase().endsWith(".java")) {
                sources.add(ghContent.getContent());
            }
        }
    }

    public static void main(String[] args) throws IOException, ParseException {
        Main main = new Main();
        // final String PROJECT_SRC = "../HW/src";
        // main.getMetrics(new SourceColector().addFiles(PROJECT_SRC, ".*\\.java").getSources());
        main.getMetrics(main.getGitHubFiles("hawstan/PlayerHealthWatcher"));
    }

}
