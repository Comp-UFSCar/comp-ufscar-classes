import java.util.StringTokenizer;

/**
 * Created by Thales on 4/11/2015.
 */
public class Quitanda {

    public static boolean validaItem(String entrada){

        StringTokenizer st = new StringTokenizer(entrada,",");
        String itemName = st.nextToken();

        // check item's name
        if(!validaItemNome(itemName))
            return false;

        // get quantities
        int[] sizes;
        sizes = new int[st.countTokens()];

        for (int i = 0; i < sizes.length; i++) {
            String q = st.nextToken().trim();
            if(!q.matches("[0-9]+"))    // no number or not int
                return false;
            sizes[i] = Integer.parseInt(q);
        }

        // check item's quantities
        if(!validaItemQuantidades(sizes))
            return false;

        return true;
    }

    public static boolean validaItemQuantidades(int[] sizes) {

        if(sizes.length > 5 || sizes.length == 0)
            return false;

        // loop to verify the sizes
        for (int i = 0; i < sizes.length; i++) {

            // size has to be between [30, 2000]
            if (sizes[i] < 30 || sizes[i] > 2000)
                return false;

            // sizes has to be in ascending order
            if (i < sizes.length - 1)
                if (sizes[i] > sizes[i + 1])
                    return false;
        }
        return true;
    }

    public static boolean validaItemNome(String itemName) {
        itemName = itemName.trim(); // ignore whitespaces
        if (itemName.matches("[a-zA-Z]{2,15}"))
            return true;
        return false;
    }

}
