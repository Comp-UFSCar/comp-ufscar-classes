package code;

import java.util.Scanner;

/**
 * Created by Thales on 3/28/2015.
 */
public class SillyPascal {

    public static boolean identificador(String s){
        if (s.matches("[a-zA-Z][0-9a-zA-Z]{0,5}"))
            return true;
        return false;
    }

    public static boolean identificador(){
        Scanner in = new Scanner(System.in);

        if (in.nextLine().matches("[a-zA-Z][0-9a-zA-Z]{0,5}"))
            return true;
        return false;
    }
}
