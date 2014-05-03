package s3.experimenter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import s3.base.S3App;

public class Experimenter {

    static final int SPEED = 1;
    static String ais[] = {"ai-footmen-rush", "ai-archers-rush",
                           "ai-catapults-rush", "ai-knights-rush"};
    static int repetitions = 1; // number of times a single experiment is run

    public static void main(String args[]) {

        int MAX_CYCLES = 200000;
        List<String> maps = new LinkedList<String>();
        String outputFolder = "Experimenter/results/S3--" + repetitions;

        maps.add("NWTR1.xml");
        maps.add("NWTR7.xml");
        maps.add("NWTR9.xml");
        maps.add("NWTR11.xml");
        maps.add("GOW-small-64x32.xml");
        maps.add("GOW-128x128.xml");
        maps.add("CP1.xml");

        S3App.REDRAWING_PERIOD = SPEED;
        S3App.MAX_FRAMESKIP = 50;

        try {
            {
                double score[][][] = new double[maps.size()][ais.length][ais.length];
                int nMap = 0, k = 0;
                new File(outputFolder).mkdirs();
                File results = new File(outputFolder + "/results-formatted.txt");
                results.createNewFile();
                FileWriter w = new FileWriter(results);

                for (String map : maps) {
                    int ai1i = 0, ai2i = 0;
                    for (String ai1 : ais) {
                        ai2i = 0;
                        for (String ai2 : ais) {
                            score[nMap][ai1i][ai2i] = 0.0;
                            for (k = 0; k < repetitions; k++) {
                                String id = "M" + nMap + "-" + ai1 + "-vs-" + ai2 + "-" + k;
                                String winner = runExperiment(id, map, ai1, ai2, outputFolder, MAX_CYCLES);

                                if (winner == null) {
                                    // TIE:
                                    score[nMap][ai1i][ai2i] += 0.5;
                                } else {
                                    // D2 wins:
                                    if (winner.equals(ai1)) {
                                        score[nMap][ai1i][ai2i] += 1.0;
                                    }
                                }
                            }
                            w.write(score[nMap][ai1i][ai2i] + ",");
                            w.flush();
                            ai2i++;
                        }
                        ai1i++;
                        w.write("\n");
                        w.flush();
                    }
                    nMap++;
                    w.write("\n\n");
                    w.flush();
                }
                w.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static String runExperiment(String ID, String map, String ai1, String ai2,
            String outputFolder, int maxCycles) throws IOException {
        String winner = s3.base.Main.internalMain(
                new String[]{"-i", "50",
                    "-m", "maps/" + map,
                    "-u", "experimenter",
                    "-p", "1|player1|" + ai1,
                    "-p", "1|player2|" + ai2}, maxCycles);
        if (winner == null) {
            return null;
        }
        if (winner.equals("player1")) {
            return ai1;
        }
        if (winner.equals("player2")) {
            return ai2;
        }
        return null;
    }
}
