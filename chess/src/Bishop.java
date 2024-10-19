//Bishop class, here the legal moves are depicted for both white and black
import java.util.ArrayList;
public class Bishop extends Piece{

    Bishop(boolean white, int x, int y){
        super(2,white);
        this.x = x;
        this.y = y;
    }
    
    //Method to check if the Bishop can move left forward diagonally
    public ArrayList<int[]> checkleftforward(Piece[][] board){
        ArrayList<int[]> list = new ArrayList<int[]>();
        int j = y;
        if (white) {
            for (int i = x-1; i >= 0; i--) {
                    j--;
                    if(j<0)break;
                    if (board[i][j].isempty) {
                        list.add(new int[]{i,j});
                    }else if (!board[i][j].white){
                        list.add(new int[]{i,j});
                        return list;
                    } else if (board[i][j].white) {
                        return list;
                    }
            }
        } else{
            for (int i = x+1; i < 8; i++) {
                    j++;
                    if(j>7)break;
                    if (board[i][j].isempty) {
                        list.add(new int[]{i,j});
                    }else if (board[i][j].white){
                        list.add(new int[]{i,j});
                        return list;
                    }else if (!board[i][j].white) {
                        return list;
                    }
            }
        }
        //Return the list of possible moves found
        return list;
    }
    //Method to check if the Bishop can move right forward diagonally
    public ArrayList<int[]> checkrightforward(Piece[][] board){
        ArrayList<int[]> list = new ArrayList<int[]>();
        int j = y;
        if (white) {
            for (int i = x-1; i >= 0; i--) {
                    j++;
                    if(j>7)break;
                    if (board[i][j].isempty) {
                        list.add(new int[]{i,j});
                    }else if (!board[i][j].white){
                        list.add(new int[]{i,j});
                        return list;
                    }else if (board[i][j].white) {
                        return list;
                    }
            }
        } else {
            for (int i = x+1; i < 8; i++) {
                    j--;
                    if(j<0)break;
                    if (board[i][j].isempty) {
                        list.add(new int[]{i,j});
                    }else if (board[i][j].white){
                        list.add(new int[]{i,j});
                        return list;
                    }else if (!board[i][j].white) {
                        return list;
                    }
                }
            }
            //Return the list of possible moves found
        return list;
    }
    //Method to check if the Bishop can move left backward diagonally
    public ArrayList<int[]> checkleftbackward(Piece[][] board){
        ArrayList<int[]> list = new ArrayList<int[]>();
        int j = y;
        if (white) {
            for (int i = x+1; i < 8; i++) {
                    j--;
                    if(j<0)break;
                    if (board[i][j].isempty) {
                        list.add(new int[]{i,j});
                    } else if (!board[i][j].white){
                        list.add(new int[]{i,j});
                        return list;
                    } else if (board[i][j].white) {
                        return list;
                    }
            }
        } else{
            for (int i = x-1; i >= 0; i--) {
                    j++;
                    if(j>7)break;
                    if (board[i][j].isempty) {
                        list.add(new int[]{i,j});
                    }else if (board[i][j].white){
                        list.add(new int[]{i,j});
                        return list;
                    }else if (!board[i][j].white) {
                        return list;
                    }
                
            }
        }
        //Return the list of possible moves found
        return list;   
    }
    //Method to check if the Bishop can move right backward diagonally
    public ArrayList<int[]> checkrightbackward(Piece[][] board){
        ArrayList<int[]> list = new ArrayList<int[]>();
        int j = y;
        if (white) {
            for (int i = x+1; i < 8; i++) {
                    j++;
                    if(j>7)break;
                    if (board[i][j].isempty) {
                        list.add(new int[]{i,j});
                    } else if (!board[i][j].white){
                        list.add(new int[]{i,j});
                        return list;
                    } else if (board[i][j].white) {
                        return list;
                    }
                
            }
        } else{
            for (int i = x-1; i >= 0; i--) {
                    j--;
                    if(j<0)break;
                    if(board[i][j].isempty) {
                        list.add(new int[]{i,j});
                    }else if (board[i][j].white){
                        list.add(new int[]{i,j});
                        return list;
                    }else if (!board[i][j].white) {
                        return list;
                    }
                
            }
        }
        //Return the list of possible moves found
        return list;
    }
    //Method to check all possible moves for the Bishop and return them in an ArrayList
    public ArrayList<int[]> checkallmoves(Piece[][] board){
            ArrayList<int[]> list = new ArrayList<int[]>();

            for (int[] pos : checkleftforward(board)) {
                list.add(pos);
            }
            for (int[] pos : checkrightforward(board)) {
                list.add(pos);
            }
            for (int[] pos : checkleftbackward(board)) {
                list.add(pos);
            }
            for (int[] pos : checkrightbackward(board)) {
                list.add(pos);
           }
        return list;
    }
    //Method to move the Bishop to a new position passing a few arguments relevant to the new position
    public void movebishop(int i, int j, Piece[][] currentboard){
        currentboard[x][y] = new Piece(0,white);
        currentboard[i][j] = new Bishop(white,i,j);
    }
    
}