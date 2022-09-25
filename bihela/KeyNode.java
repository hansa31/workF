class KeyNode {

    //define the key and it's x,y coordinates
    private char key;
    private int x;
    private int y;

    public KeyNode(char k, int X, int Y){
        key = k;
        x = X;
        y = Y;
    }

    //setters
    public void setKey(char key){
        this.key = key;
    }

    public void setX(int x){
        this.x = x;
    }

    public void setY(int y){
        this.y = y;
    }

    //getters
    public char getKey(){
        return key;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

}