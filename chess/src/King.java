//King class, here the legal moves are depicted for both white and black
import java.util.ArrayList;
public class King extends Piece {

    King(boolean white, int x, int y){
        super(6,white);
        this.x = x;
        this.y = y;
    }
    //Method to check if the King can move in the nearest squares around as per the rules of chess
    public ArrayList<int[]> checkallmoves(Piece[][] board){
        ArrayList<int[]> list = new ArrayList<int[]>();
        if(x+1<8 && (board[x+1][y].isempty || board[x+1][y].white!=white)){
            list.add(new int[]{x+1,y});
        }
        if(x-1>=0 && (board[x-1][y].isempty || board[x-1][y].white!=white)){
            list.add(new int[]{x-1,y});
        }
        if(y+1<8 && (board[x][y+1].isempty || board[x][y+1].white!=white)){
            list.add(new int[]{x,y+1});
        }
        if(y-1>=0 && (board[x][y-1].isempty || board[x][y-1].white!=white)){
            list.add(new int[]{x,y-1});
        }
        if(x+1<8 && y+1<8 && (board[x+1][y+1].isempty || board[x+1][y+1].white!=white)){
            list.add(new int[]{x+1,y+1});
        }
        if(x+1<8 && y-1>=0 && (board[x+1][y-1].isempty || board[x+1][y-1].white!=white)){
            list.add(new int[]{x+1,y-1});
        }
        if(x-1>=0 && y+1<8 && (board[x-1][y+1].isempty || board[x-1][y+1].white!=white)){
            list.add(new int[]{x-1,y+1});
        }
        if(x-1>=0 && y-1>=0 && (board[x-1][y-1].isempty || board[x-1][y-1].white!=white)){
            list.add(new int[]{x-1,y-1});
        }
        //After finding all the possible moves, return the list
        return list;
    }
    
    //Method to move the King to a new position passing a few arguments relevant to the new position
    public void moveKing(int i, int j, Piece[][] currentboard){
        currentboard[x][y] = new Piece(0,white);
        currentboard[i][j] = new King(white,i,j);
    }
}