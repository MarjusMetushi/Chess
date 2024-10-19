import java.util.ArrayList;

public class Rook extends Piece{

    Rook(boolean white, int x, int y){
        super(3,white);
        this.x = x;
        this.y = y;
    }

    public ArrayList<int[]> checkforwards(Piece[][] board){
        ArrayList<int[]> list = new ArrayList<int[]>();
        if (white) {
            for (int i = x-1; i >= 0; i--) {
                if (board[i][y].isempty) {
                    list.add(new int[]{i,y});
                }else if (board[i][y].white){
                    return list;
                }else if (!board[i][y].white) {
                    list.add(new int[]{i,y});
                    return list;
                }
            }
        }else{
            for (int i = x+1; i < 8; i++) {
                if (board[i][y].isempty) {
                    list.add(new int[]{i,y});
                }else if (board[i][y].white){
                    list.add(new int[]{i,y});
                    return list;
                }else if (!board[i][y].white) {
                    return list;
                }
            }
        }


        return list;
    }

    public ArrayList<int[]> checkbackwards(Piece[][] board){
        ArrayList<int[]> list = new ArrayList<int[]>();
        
        if (white) {
            for (int i = x+1; i <8; i++) {
                if (board[i][y].isempty) {
                    list.add(new int[]{i,y});
                }else if (board[i][y].white){
                    return list;
                }else if (!board[i][y].white) {
                    list.add(new int[]{i,y});
                    return list;
                }
            }
        }else{
            for (int i = x-1; i >=0; i--) {
                if (board[i][y].isempty) {
                    list.add(new int[]{i,y});
                }else if (board[i][y].white){
                    list.add(new int[]{i,y});
                    return list;
                }else if (!board[i][y].white) {
                    return list;
                }
            }
        }

        return list;
    }
    public ArrayList<int[]> checkright(Piece[][] board){
        ArrayList<int[]> list = new ArrayList<int[]>();

        if (white) {
            for (int i = y+1; i < 8; i++) {
                if (board[x][i].isempty) {
                    list.add(new int[]{x,i});
                }else if (board[x][i].white){
                    return list;
                }else if (!board[x][i].white) {
                    list.add(new int[]{x,i});
                    return list;
                }
            }
        }else{
            for (int i = y-1; i >= 0; i--) {
                if (board[x][i].isempty) {
                    list.add(new int[]{x,i});
                }else if (board[x][i].white){
                    list.add(new int[]{x,i});
                    return list;
                }else if (!board[x][i].white) {
                    return list;
                }
            }
        }

        return list;
    }
    public ArrayList<int[]> checkleft(Piece[][] board){
        ArrayList<int[]> list = new ArrayList<int[]>();

        if (white) {
            for (int i = y-1; i >= 0; i--) {
                if (board[x][i].isempty) {
                    list.add(new int[]{x,i});
                }else if (board[x][i].white){
                    return list;
                }else if (!board[x][i].white) {
                    list.add(new int[]{x,i});
                    return list;
                }
            }
        }else{
            for (int i = y+1; i < 8; i++) {
                if (board[x][i].isempty) {
                    list.add(new int[]{x,i});
                }else if (board[x][i].white){
                    list.add(new int[]{x,i});
                    return list;
                }else if (!board[x][i].white) {
                    return list;
                }
            }
        }
        return list;
   }

   public ArrayList<int[]> checkallmoves(Piece[][] board){
        ArrayList<int[]> list = new ArrayList<int[]>();

        for (int[] pos : checkbackwards(board)) {
            list.add(pos);
        }
        for (int[] pos : checkforwards(board)) {
            list.add(pos);
        }
        for (int[] pos : checkleft(board)) {
            list.add(pos);
        }
        for (int[] pos : checkright(board)) {
            list.add(pos);
       }
      return list;
    }

   public void moveRook(int i,int j, Piece[][] currentboard){
       currentboard[x][y] = new Piece(0,white);
       currentboard[i][j] = new Rook(white,i,j);
   }
}