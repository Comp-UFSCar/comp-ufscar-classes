/**
 *  Referências:
 *  Java 7 SE API
 *      http://docs.oracle.com/javase/7/docs/api/
 *  Leitura de Arquivos
 *      http://java-tips.org/java-se-tips/java.io/how-to-read-file-in-java.html
 *  Parsing de String
 *      http://pages.cs.wisc.edu/~hasti/cs302/examples/Parsing/parseString.html
 *  Estrutura FIFO
 *      http://stackoverflow.com/questions/9580457/fifo-class-in-java
 */
package com.company;

import java.io.*;
import java.net.URL;

public class Main {

    public static void main(String[] args)  {

        problemaB.inicializarProblemaB();
        URL main = Main.class.getResource("/data/B/input/B_0");

        File file = new File(main.getPath());

        FileInputStream fis = null;
        BufferedInputStream bis = null;
        DataInputStream dis = null;

        try {
            fis = new FileInputStream(file);

            // Here BufferedInputStream is added for fast reading.
            bis = new BufferedInputStream(fis);
            dis = new DataInputStream(bis);

            problemaB.lerNumOfertas(dis.readLine());

            // dis.available() returns 0 if the file does not have more lines.
            while (dis.available() != 0)
                problemaB.lerDisciplina(dis.readLine());

            // dispose all the resources after using them.
            fis.close();
            bis.close();
            dis.close();

            //System.out.println("Entrada lida com sucesso!");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //problemaB.imprimirEntrada();
        System.out.println(problemaB.maiorCMAcumulado());
        //problemaB.imprimirSelecionadas();
    }
}
