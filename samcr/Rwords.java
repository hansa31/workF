import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;


public class Rwords {
    public static void main(String[] args) throws FileNotFoundException{
        Scanner scan = new Scanner(new File(args[0]));      //get the input from the text file

        //create the linked list for reserved words
        LinkedList<String> RW = new LinkedList<String>();

        //create the linked list for identifiers
        LinkedList<Node> ID = new LinkedList<Node>();

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
        //System.out.println(" ");

        Iterator<String> itr = RW.iterator();
        while(itr.hasNext()){
            System.out.println(itr.next());
        }

        //get the java file from cli args
        Scanner scan2 = new Scanner(new File(args[1]));

        //get the count
        int count = 1;

        //array to hold words
        String[] words;

        while(scan2.hasNext()){

            //read line by line
            String line = scan2.nextLine();     //nextLine() for line by line
            
            //System.out.print(count+"    ");
            //System.out.println(line);

            //get the words from a line
            words = getWords(line);

            //System.out.println(Arrays.toString(words));
            //System.out.println(words.length);

            //continue if words is empty
            if(words.length == 0 || words.length ==1){
                continue;
            }

            //check whether it is an identifier and add it to the linked list
            
            
            for (String s : words){
                if (s.equals(null)){
                    continue;
                }
                //System.out.println(s.charAt(0));
                if(isID(s)){
                    if(isInRW(s,RW)){
                        Node n = new Node(s,count);
                        
                        //check whether it is a duplicate node
                         
                        if(!(ID.contains(n))){
                            //n.printNode();
                            ID.add(n);
                        }
                        
                        

                        
                    }
                }
            }
            
            

            count++;
            // add the strings to the linked list
            //RW.add(s);
        }

        //sort the linked list
        Collections.sort(ID,Comparator.comparing(Node::getid));

        Iterator<Node> itr2 = ID.iterator();
        while(itr2.hasNext()){
            itr2.next().printNode();
        }


    }

    //method to check whether a string is a reserved keyword or not
    public static boolean isInRW(String s, LinkedList<String> RW){
        if(RW.contains(s)){
            return false;       //returns false if RW linked list has it
        }else{
            return true;        //returns true if it is an identifier
        }
    }

    //method to check whether the syntax is correct for the identifier
    public static boolean isID(String str){
        //check whether it starts with A-Z or a-z or $ or _
        /* 
        char first = s.charAt(0);
        if(s.isJavaIdentifierStart())
        */

        if(str.equals(null)){
            return false;
        }




        // If first character is invalid
        if (!((str.charAt(0) >= 'a' && str.charAt(0) <= 'z')
            || (str.charAt(0)>= 'A' && str.charAt(0) <= 'Z')
            || str.charAt(0) == '_' || str.charAt(0)=='$'))
            return false;

        // Traverse the string for the rest of the characters
        for (int i = 1; i < str.length(); i++)
        {
            if (!((str.charAt(i) >= 'a' && str.charAt(i) <= 'z')
                || (str.charAt(i) >= 'A' && str.charAt(i) <= 'Z')
                || (str.charAt(i) >= '0' && str.charAt(i) <= '9')
                || str.charAt(i) == '_' || str.charAt(i) =='$'))
            return false;
        }

        // String is a valid identifier
        return true;
    }

    //get the words from the given line
    public static String[] getWords(String str){
        StringBuilder sbuild = new StringBuilder(str);

        // Traverse the string
        for (int i = 0; i < str.length(); i++) {

            //check whether it starts with A-Z or a-z or $ or _ if not put a space
            if (!((str.charAt(i) >= 'a' && str.charAt(i) <= 'z')
                || (str.charAt(i) >= 'A' && str.charAt(i) <= 'Z')
                || (str.charAt(i) >= '0' && str.charAt(i) <= '9')
                || str.charAt(i) == '_' || str.charAt(i) =='$')){

                
                
                sbuild.setCharAt(i, ' ');  

            }
            
            
        }

        String str2 = sbuild.toString();

        //System.out.println(sbuild.toString());

        //convert the line to an array of words
        String[] words = str2.split("\\W+");

        //System.out.println(Arrays.toString(words));
        for (int i =0 ; i< words.length;i++) {
            String s = words[i];

            if(s.length()==0){
                words[i]=s.replace("","#");
                //System.out.println("XXX"+s+"XXXX");
            }
        }

        return words;
        
        

    }

}
