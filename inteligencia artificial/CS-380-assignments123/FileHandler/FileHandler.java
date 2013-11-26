package FileHandler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.StringTokenizer;

/**
 *
 * @author ld492
 */
public class FileHandler {

    private String lastFileLoaded;
    
    public FileHandler() {
        lastFileLoaded = "";
    }

    public int[][] load(String _fileName) throws Exception {
        lastFileLoaded = "src/Maps/" + _fileName + ".txt";
        
        BufferedReader br = new BufferedReader(new FileReader(lastFileLoaded));
        StringTokenizer st;
        String line;
        
        // Decoding file header
        line = br.readLine();
        st = new StringTokenizer(line, ",");
        
        int cols = Integer.parseInt(st.nextToken());
        int rows = Integer.parseInt(st.nextToken());
        int gameMatrix[][] = new int[rows][cols];
                
        for (int i = 0; i < rows; i++) {
            line = br.readLine();
            st = new StringTokenizer(line, ",");
            
            for (int j = 0; j < cols; j++) {
                String token = st.nextToken();
                gameMatrix[i][j] = Integer.parseInt(token);
            }
        }
        
        return gameMatrix;
    }
    
    public int[][] reload() throws Exception {
        if (lastFileLoaded.isEmpty()) {
            throw new GameLoadException();
        }
        
        return load(lastFileLoaded);
    }
}
