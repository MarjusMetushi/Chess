//Knight class here the legal moves are depicted for both white and black
import java.util.ArrayList;
public class Knight extends Piece {
    
    public Knight(boolean white,int x, int y) {
        super(4,white);
        this.x = x;
        this.y = y;
    }
    //Method to check if the Knight can move in the nearest L-shaped squares around as per the rules of chess
    public ArrayList<int[]> checkallmoves(Piece[][] board){
        ArrayList<int[]> list = new ArrayList<int[]>();
        if (white) {
            if(x>1 && y>0 && (board[x-2][y-1].isempty || !board[x-2][y-1].white)){
                list.add(new int[]{x-2,y-1});
            }
            if((x>1 && y<7) && (board[x-2][y+1].isempty || !board[x-2][y+1].white)){
                list.add(new int[]{x-2,y+1});
            }
            if((x<6 && y>0) && (board[x+2][y-1].isempty || !board[x+2][y-1].white)){
                list.add(new int[]{x+2,y-1});
            }
            if( (x<6 && y<7) && (board[x+2][y+1].isempty || !board[x+2][y+1].white)){
                list.add(new int[]{x+2,y+1});
            }
            if ( (x>0 && y>1) && (board[x-1][y-2].isempty || !board[x-1][y-2].white)){
                list.add(new int[]{x-1,y-2});
            }
            if ( (x<7 && y>1) && (board[x+1][y-2].isempty || !board[x+1][y-2].white)){
                list.add(new int[]{x+1,y-2});
            }
            if ( (x>0 && y<6) && (board[x-1][y+2].isempty || !board[x-1][y+2].white)){
                list.add(new int[]{x-1,y+2});
            }
            if ( (x<7 && y<6) && (board[x+1][y+2].isempty || !board[x+1][y+2].white)){
                list.add(new int[]{x+1,y+2});
            }
        }else{
            if(x>1 && y>0 && (board[x-2][y-1].isempty || board[x-2][y-1].white)){
                list.add(new int[]{x-2,y-1});
            }
            if((x>1 && y<7) && (board[x-2][y+1].isempty || board[x-2][y+1].white)){
                list.add(new int[]{x-2,y+1});
            }
            if((x<6 && y>0) && (board[x+2][y-1].isempty || board[x+2][y-1].white)){
                list.add(new int[]{x+2,y-1});
            }
            if( (x<6 && y<7) && (board[x+2][y+1].isempty || board[x+2][y+1].white)){
                list.add(new int[]{x+2,y+1});
            }
            if ( (x>0 && y>1) && (board[x-1][y-2].isempty || board[x-1][y-2].white)){
                list.add(new int[]{x-1,y-2});
            }
            if ( (x<7 && y>1) && (board[x+1][y-2].isempty || board[x+1][y-2].white)){
                list.add(new int[]{x+1,y-2});
            }
            if ( (x>0 && y<6) && (board[x-1][y+2].isempty || board[x-1][y+2].white)){
                list.add(new int[]{x-1,y+2});
            }
            if ( (x<7 && y<6) && (board[x+1][y+2].isempty || board[x+1][y+2].white)){
                list.add(new int[]{x+1,y+2});
            }
        }
        //After finding all the possible moves, return the list
        return list;
    }
    //Method to move the Knight to a new position passing a few arguments relevant to the new position
    public void moveknight(int i, int j,Piece[][] currentboard){
        currentboard[x][y] = new Piece(0,white);
        currentboard[i][j] = new Knight(white,i,j);
    }

}