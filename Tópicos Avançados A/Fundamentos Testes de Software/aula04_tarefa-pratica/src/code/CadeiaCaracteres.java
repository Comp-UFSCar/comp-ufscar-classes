package code;

import com.sun.corba.se.spi.orbutil.fsm.Input;

import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Created by Thales on 3/28/2015.
 *
 * O programa solicita do usu�rio um inteiro positivo no
 * intervalo entre 1 e 20 e ent�o solicita uma cadeia de
 * caracteres desse comprimento. Ap�s isso, o programa
 * solicita um caracter e retorna a posi��o na cadeia em que
 * o caracter � encontrado pela primeira vez ou uma
 * mensagem indicando que o caracter n�o est� presente na
 * cadeia. O usu�rio tem a op��o de procurar por v�rios
 * caracteres.
 *
 */

public class CadeiaCaracteres {

    public static String cadeia = new String();

    public static void defineCadeia(Scanner in){
        int t;

        do {
            System.out.print("Tamanho da cadeia: ");
            t = in.nextInt();
        } while (!(t >= 1 && t <= 20));

        System.out.print("Cadeia de "+ t +" caracteres: ");
        do{
            cadeia = in.nextLine();
        } while(!(cadeia.length() == t));
    }

    public static void buscaCaractere(Scanner in, PrintWriter output){
        String op = "sim";
        CharSequence cc;

        // string not defined before
        if(cadeia.isEmpty())
            defineCadeia(in);

        // main loop to find character
        do{
            // define the character
            do {
                System.out.print("Cactere a ser encontrado: ");
                cc = in.nextLine();
            } while(!(cc.length() == 1));

            // check if it exists in current string
            if(cadeia.contains(cc)){
                int i = 0;
                while(cadeia.charAt(i) != cc.charAt(0))
                    i++;
                //System.out.println("Caracter aparece na posicao " + i + " da cadeia");
                output.println("Caracter aparece na posicao " + i + " da cadeia");
            }
            else
                //System.out.println("Caracter nao pertence a cadeia");
                output.println("Caracter nao pertence a cadeia");

            // if wants to check for another character
            do {
                System.out.print("Outro caractere? [sim] ou [nao]: ");
                op = in.nextLine();
            } while (!(op.compareToIgnoreCase("nao") == 0 || op.compareToIgnoreCase("sim") == 0));
        }while(!(op.compareToIgnoreCase("nao") == 0));
    }
}
