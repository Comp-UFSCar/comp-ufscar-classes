package FileHandler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.util.Date;
import java.util.NoSuchElementException;

/**
 *
 * @author lucasdavid
 */
public class FileHandler {

    public static final int MATRIX_LENGTH = 1000;
    public static final String BASE_PATH = "src/data/";
    final String inputFile;
    final String reportFileName;
    PrintWriter pw;
    BufferedReader br;

    int[][] matrix;
    int length;

    /**
     * Construct a FileHandler instance set with a input file _problem and a report file (new Date()).toString().
     *
     * @param _problem
     */
    public FileHandler(String _problem) {
        br = null;
        pw = null;

        length = MATRIX_LENGTH;

        inputFile = BASE_PATH + _problem;
        reportFileName = "src/report/report_" + (new Date()).toString();
    }

    /**
     * Read a matrix from an input file.
     *
     * @return matrix
     *
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    public int[][] read() throws FileNotFoundException, IOException {
        String token = null;
        String line;
        
        int rows, cols, row;
        
        br = new BufferedReader(new FileReader(inputFile));
        
        line = br.readLine();
        rows = Integer.parseInt(line);
        line = br.readLine();
        //cols = Integer.parseInt(line);
        
        matrix = new int[rows][rows];

        row = 0;
        while ((line = br.readLine()) != null) {
            StringTokenizer st;
            
            st = new StringTokenizer(line, Character.toString((char) 9));
            int tokens = st.countTokens();

            for (int current = 0; current < tokens; current++) {
                token = st.nextToken();
                matrix[row][current] = Integer.parseInt(token);
            }

            row++;
        }

        br.close();
        br = null;

        return matrix;
    }

    public FileHandler writeInWekaFormat(String _file) throws IOException {

        PrintWriter weka = new PrintWriter(new FileWriter(BASE_PATH + _file + ".arff"));

        append("@relation greatmindchallenge\n", weka);

        for (int i = 1; i < matrix.length -1; i++) {
            append("@attribute a" + i + " real", weka);
        }

        append("\n@data", weka);

        for (int[] row : matrix) {
            String line = "";

            for (int i = 1; i < row.length -1; i++) {
                line += row[i] + ",";
            }

            append(line.substring(0, line.length() - 1), weka);
        }

        return commit(weka);
    }

    public FileHandler append(String _line) throws IOException {
        return append(_line, pw);
    }

    /**
     * Add a line to the end of the opened file, if none is opened, start a new file for written.
     *
     * @param _line string that should be added to the end of the file written
     * @param _pw
     * @return this
     * @throws IOException
     */
    FileHandler append(String _line, PrintWriter _pw) throws IOException {
        if (_pw == null) {
            _pw = new PrintWriter(new FileWriter(reportFileName));
        }

        _pw.println(_line);
        _pw.flush();

        return this;
    }

    public FileHandler commit() {
        return commit(pw);
    }

    /**
     * Commit changes made by @append method and closes written file.
     *
     * @return this
     */
    FileHandler commit(PrintWriter _pw) {
        _pw.close();
        _pw = null;

        return this;
    }

    /**
     *
     * @param _length number of lines which should be processed in the file
     * @return this
     */
    public FileHandler length(int _length) {
        length = _length;

        return this;
    }

    public FileHandler print(int _i, int _j) {
        for (int i = 0; i < _i; i++) {
            for (int j = 0; j < _j; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }

        return this;
    }
}
