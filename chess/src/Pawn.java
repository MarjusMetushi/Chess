//Pawn class, here the legal moves are depicted for both white and black
import java.util.ArrayList;
public class Pawn extends Piece{
    //Variables to keep track of the moves made by the pawn and En Passant possibilities
    int movecounter = 0;
    boolean enPassantleft = false;
    boolean enPassantright = false;
    boolean missedleft = false;
    boolean missedright = false;
    //Constructor to create a new Pawn object
    Pawn(boolean white,int x, int y, int counter, boolean missedleft, boolean missedright, boolean enPassantleft, boolean enPassantright){
        super(1,white);
        if(white){
            this.white = true;
        } else {
            this.white = false;
        }
        this.x = x;
        this.y = y;
        this.movecounter = counter;
        this.missedleft = missedleft;
        this.missedright = missedright;
        this.enPassantleft = enPassantleft;
        this.enPassantright = enPassantright;

    }
    //Method to check if the Pawn can take left diagonally
   public boolean checkleft(Piece[][] board){
        if (white && x>0 && y>0 && !board[x-1][y-1].white && !board[x-1][y-1].isempty) {
            return true;
        }else if (!white && x<7 && y<7 && board[x+1][y+1].white && !board[x+1][y+1].isempty) {
            return true;
        }
        return false;
   }

    //Method to check if the Pawn can take right diagonally
   public boolean checkright(Piece[][] board){
        if (white && x>0 && y<7 && !board[x-1][y+1].white && !board[x-1][y+1].isempty ) { 
            return true;
        }else if (!white && x<7 && y>0 && board[x+1][y-1].white && !board[x+1][y-1].isempty) {
            return true;
        }
        return false;
   }

    //Method to check if the Pawn can move forward by one square
   public boolean checkforward(Piece[][] board){
    if (white && x>0 && board[x-1][y].isempty ) {
        return true;
    }else if (!white && x<7 && board[x+1][y].isempty ) {
        return true;
    }
        return false;
   }
    //Method to check if the Pawn can move forward by two squares
   public boolean checktwoforward(Piece[][] board){
    if(!isstartingposition(board))return false;
       if (white && board[x-1][y].isempty && board[x-2][y].isempty) {  
            return true;
       }else if (!white && board[x+1][y].isempty && board[x+2][y].isempty) { 
            return true;    
       }
       return false;
    }
    //Method to check all possible moves for the Pawn and return them in an ArrayList
    public ArrayList<int[]> checkallmoves(Piece[][] board,Piece[][] lastboard){
        ArrayList<int[]> list = new ArrayList<int[]>();
        //based on the booleans it is allowed the pawn to move
        if(white){
            if(checkforward(board)){
                list.add(new int[]{x-1,y});
            }
            if(checktwoforward(board)){
                list.add(new int[]{x-2,y});
            }
            if(checkleft(board)){
                list.add(new int[]{x-1,y-1});
            }
            if(checkright(board)){
                list.add(new int[]{x-1,y+1});
            }
        } else {
            if(checkforward(board)){
                list.add(new int[]{x+1,y});
            }
            if(checktwoforward(board)){
                list.add(new int[]{x+2,y});
            }
            if(checkleft(board)){
                list.add(new int[]{x+1,y+1});
            }
            if(checkright(board)){
                list.add(new int[]{x+1,y-1});
            }
        }
        //Also here en Passant is allowed if there is a possibility
        if(enPassant(board,lastboard)!=null){
            for (int[] pos : enPassant(board,lastboard)) {
                if(pos!=null){
                    list.add(pos);          
                }
            }       
        }
        //Return the list of possible moves found
        return list;
    }

    //Method to check if en Passant is possible
    public ArrayList<int[]> enPassant(Piece[][] board, Piece[][] lastboard){
        //Checking for en Passant and allowing it if the previous move the pawn has moved to the right position for taking with en Passant
        ArrayList<int[]> list = new ArrayList<int[]>();
        if(white){
            if(x==3 && y>0 && board[x][y-1].ispawn && !board[x][y-1].white && !missedleft){
                Pawn left = (Pawn) board[x][y-1];
                if(left.movecounter==1 && board[x-1][y-1].isempty && lastboard!=null && lastboard[x][y-1].isempty){
                    list.add(new int[]{x-1,y-1});
                }else {
                    list.add(null);
                }
            } else {
                list.add(null);
            }
            if(x==3 && y<7 && board[x][y+1].ispawn && !board[x][y+1].white && !missedright){
                Pawn right = (Pawn) board[x][y+1];
                if(right.movecounter==1 && board[x-1][y+1].isempty && lastboard!=null && lastboard[x][y+1].isempty){
                    list.add(new int[]{x-1,y+1});
                }else {
                    list.add(null);
                }
            } else {
                list.add(null);
            }
        } else { 
            if(x==4 && y<7 && board[x][y+1].ispawn && board[x][y+1].white && !missedleft){
            Pawn right = (Pawn) board[x][y+1];
            if(right.movecounter==1 && board[x+1][y+1].isempty && lastboard!=null && lastboard[x][y+1].isempty){
                list.add(new int[]{x+1,y+1});
            }else {
                list.add(null);
            }
        } else {
            list.add(null);
        }
            if(x==4 && y>0 && board[x][y-1].ispawn && board[x][y-1].white &&  !missedright){
                Pawn left = (Pawn) board[x][y-1];
                if(left.movecounter==1 && board[x+1][y-1].isempty && lastboard!=null && lastboard[x][y-1].isempty){
                    list.add(new int[]{x+1,y-1});
                }else {
                    list.add(null);
                }
            } else {
                list.add(null);
            }
           
        }
        //Then everything is returned by a list
        for (int[] is : list) {
            if(is!=null){
                return list;
            }
        }
        return null;
    }
    //Checking if the pawn is in the starting position
    public boolean isstartingposition(Piece[][] board){
        if(white && x==6){
            return true;
        } else if(!white && x==1){
            return true;
        }
        return false;
    }
    //Method to move the Pawn to a new position passing a few arguments relevant to the new position
    public void movepawn(int i, int j, Piece[][] currentboard){
        currentboard[x][y] = new Piece(0,white);
        currentboard[i][j] = new Pawn(white,i,j,movecounter+1,missedleft,missedright,enPassantleft,enPassantright);
    }
}