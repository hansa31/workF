import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

public class Rwords {
    public static void main(String[] args) throws FileNotFoundException{
        Scanner scan = new Scanner(new File(args[0]));      //get the input from the text file

        //create the linked list for reserved words
        LinkedList<String> RW = new LinkedList<String>();

        while(scan.hasNext()){
            //read word by word
            String s = scan.next();     //nextLine() for line by line
            //System.out.println(s);

            // add the strings to the linked list
            RW.add(s);
        }

        //sort the linked list
        Collections.sort(RW);

        //printing the linked list for debugging (comment out this part)
        System.out.println(" ");

        Iterator<String> itr = RW.iterator();
        while(itr.hasNext()){
            System.out.println(itr.next());
        }


    }
}
