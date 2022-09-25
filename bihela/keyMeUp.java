import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

class keyMeUp{

    //main method
    public static void main(String args[]) throws IOException{

        //just some error handleing
        if (!(args.length == 1 || args.length == 4)){
            System.out.println("Use -i for interactive testing enviromrnt [java keyMeUp -i]\nUse -s for silent mode [java keyMeUp -s keyFile strFile pathFile]");
        } else if (!(args[0].equals("-i") || args[0].equals("-s"))){
            System.out.println("Use -i for interactive testing enviromrnt [java keyMeUp -i]\nUse -s for silent mode [java keyMeUp -s keyFile strFile pathFile]");
        }

        //for the silent mode
        if (args[0].equals("-s")){

            String keyFile = args[1];
            String strFile = args[2];
            String pathFile = args[3];

            String path = "";
            //String str = "APPLE";

            KeyNode [][] matrix = null;

            //creating the keyboard matirx
            try {
                matrix = create2DIntMatrixFromFile(keyFile);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //read the string from the file
            String str = new String(Files.readAllBytes(Paths.get(strFile)));
            System.out.println(str);

            //getting the path
            path = getPath(str, matrix);

            //System.out.println("***********");
            System.out.println(path);

            //writing the path to the path file
            try {
                FileWriter myWriter = new FileWriter(pathFile);
                myWriter.write(path);
                myWriter.close();
                System.out.println("Successfully wrote to the file.");
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }

        //interactive mode
        }else if (args[0].equals("-i")){
            System.out.println("1 - Load keyboard file");
            System.out.println("2 - Node operations (find, insert, delete, update)");
            System.out.println("3 - Edge operations (find, insert, delete, update)");
            System.out.println("4 - Display graph");
            System.out.println("5 - Display graph information");
            System.out.println("6 - Enter string for finding path");
            System.out.println("7 - Generate paths");
            System.out.println("8 - Display path");
            System.out.println("9 - Save keyboard");

            //variables
            String keyFile;
            //String strFile;
            //String pathFile;

            String str = "";
            String path = "";
            KeyNode [][] matrix = null;

            while (true){
                Scanner sc = new Scanner(System.in);
                int start = sc.nextInt();

                if (start == 1){
                    Scanner sc2 = new Scanner(System.in);
                    keyFile = sc2.nextLine();
                    System.out.println(keyFile);
                    
                    //creating the keyboard matirx
                    try {
                        matrix = create2DIntMatrixFromFile(keyFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }else if (start ==2){
                    //TODO
                }else if (start==3){
                    //TODO
                }else if (start==4){
                    //Display Graph
                    System.out.println(Arrays.deepToString(matrix));
                }else if (start==5){
                    //TODO
                }else if (start==6){
                    //enter the string
                    Scanner sc7 = new Scanner(System.in);
                    str = sc7.nextLine();
                }else if (start==7){
                    if(str.equals("")){
                        System.out.println("Please enter a string to calculate the path");
                    }else{
                        //getting the path
                        path = getPath(str, matrix);
                        System.out.println("Path created sucessfully");
                    }
                }else if (start==8){
                    //display path
                    System.out.println(path);
                }else if (start==9){
                    System.out.println("Sucessfully saved the keyboard");
                }else if(start<1 || start>9){
                    System.out.println("Wrong Input. Please reffer the manual again");
                }
            }
            
        }
    }

    //building the keyboard from the given file
    public static KeyNode[][] create2DIntMatrixFromFile(String filename) throws Exception {
        KeyNode[][] matrix = null;

        // If included in an Eclipse project.
        InputStream stream = ClassLoader.getSystemResourceAsStream(filename);
        BufferedReader buffer = new BufferedReader(new InputStreamReader(stream));

        // If in the same directory - Probably in your case...
        // Just comment out the 2 lines above this and uncomment the line
        // that follows.
        //BufferedReader buffer = new BufferedReader(new FileReader(filename));

        String line;
        int row = 0;
        int size = 0;

        while ((line = buffer.readLine()) != null) {
            String[] vals = line.trim().split(" ");

            // Lazy instantiation.
            if (matrix == null) {
                size = vals.length;
                matrix = new KeyNode[10][10];
            }

            for (int col = 0; col < size; col++) {
                //matrix[row][col] = Integer.parseInt(vals[col]);
                //System.out.println(row + "..." + col +"..."+ vals[col]);
                KeyNode entry = new KeyNode(vals[col].charAt(0), row, col);
                matrix[row][col] = entry;
            }

            row++;
        }

        return matrix;
    }

    // method to get the path

    // Find the shortest route in a device to construct the given string
    private static String getPath(String str,KeyNode[][] keyboard)
    {

        // path
        String path = "";

        // start from the top-left corner with coordinates, i.e., (0, 0) cell
        int x = 0, y = 0;
 
        for (char c: str.toCharArray())
        {
            // find coordinates of the next character
            int X=0;
            int Y=0;

            for(int i=0; i<keyboard.length; i++) {
                for(int j=0; j<keyboard[i].length; j++) {
                    KeyNode entry = keyboard[i][j];
                    if(entry == null ){
                        continue;
                    }
                    if(c==entry.getKey()){
                        X = entry.getX();
                        Y = entry.getY();
                        break;
                    }
                }
            }

 
            // if the next character is above the current character
            while (x > X)
            {
                //System.out.print("T");
                path = path.concat("T");
                x--;            // Go up
            }
 
            // if the next character is below the current character
            while (x < X)
            {
                //System.out.print("B");
                path = path.concat("B");
                x++;            // Go down
            }
 
            // if the next character is to the left of the current character
            while (y > Y)
            {
                //System.out.print("L");
                path = path.concat("L");
                y--;            // Go left
            }
 
            // if the next character is to the right of the current character
            while (y < Y)
            {
                //System.out.print("R");
                path = path.concat("R");
                y++;            // Go right
            }
 
            // next character is found
            //System.out.print("M");
            path = path.concat("M");
        }

        return path;
    }
    



}