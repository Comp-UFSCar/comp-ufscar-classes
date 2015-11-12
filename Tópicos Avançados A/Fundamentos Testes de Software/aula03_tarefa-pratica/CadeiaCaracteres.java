import java.util.Scanner;

/**
 * Created by Thales on 3/28/2015.
 *
 * O programa solicita do usuário um inteiro positivo no
 * intervalo entre 1 e 20 e então solicita uma cadeia de
 * caracteres desse comprimento. Após isso, o programa
 * solicita um caracter e retorna a posição na cadeia em que
 * o caracter é encontrado pela primeira vez ou uma
 * mensagem indicando que o caracter não está presente na
 * cadeia. O usuário tem a opção de procurar por vários
 * caracteres.
 *
 */

public class CadeiaCaracteres {

    public static String cadeia = new String();

    public static void defineCadeia(){
        Scanner in = new Scanner(System.in);
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

    public static void buscaCaractere(){
        Scanner in = new Scanner(System.in);
        String op = "sim";
        CharSequence cc;

        // string not defined before
        if(cadeia.isEmpty())
            defineCadeia();

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
                System.out.println("Caracter aparece na posicao " + i + " da cadeia");
            }
            else
                System.out.println("Caracter nao pertence a cadeia");

            // if wants to check for another character
            do {
                System.out.print("Outro caractere? [sim] ou [nao]: ");
                op = in.nextLine();
            } while (!(op.compareToIgnoreCase("nao") == 0 || op.compareToIgnoreCase("sim") == 0));
        }while(!(op.compareToIgnoreCase("nao") == 0));
    }
}
