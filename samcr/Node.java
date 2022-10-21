public class Node {
    //basic class to hold the identifier and line
    private String id;
    private int line;

    //constructor
    public Node(String i, int l){
        id = i;
        line = l;
    }

    //method to print a Node
    public void printNode(){
        System.out.println("identifier: "+id+" "+"line: "+line);
    }

    //getters
    public String getid(){
        return this.id;
    }

    public int getLine(){
        return this.line;
    }

    //setters
    public void setid(String id){
        this.id = id;
    }

    public void setLine(int line){
        this.line = line;
    }


}
