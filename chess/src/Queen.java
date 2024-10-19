//Queen class, here the legal moves are depicted for both white and black
import java.util.ArrayList;
public class Queen extends Piece {
    
    public Queen(boolean white, int x, int y) {
        super(5,white);
        this.x = x;
        this.y = y;
    }
    //Method to check if the Queen can move left forward diagonally as if it was a bishop
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
    //Method to check if the Queen can move right forward diagonally as if it was a bishop
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
    //Method to check if the Queen can move left backward diagonally as if it was a bishop
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
    //Method to check if the Queen can move right backward diagonally as if it was a bishop
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
    //Method to check if the Queen can move forward just like checking as if it was a rook
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
        //Return the list of possible moves found
        return list;
    }
    //Method to check if the Queen can move backward just like checking as if it was a rook
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
        //Return the list of possible moves found
        return list;
    }
    //Method to check if the Queen can move right just like checking as if it was a rook
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
        //Return the list of possible moves found
        return list;
    }
    //Method to check if the Queen can move left just like checking as if it was a rook
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
        //Return the list of possible moves found
        return list;
   }
   //Method to check all possible moves for the Queen and return them in an ArrayList
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
       for (int[] pos : checkleftbackward(board)) {
        list.add(pos);
       }
       for (int[] pos : checkleftforward(board)) {
        list.add(pos);
       }
       for (int[] pos : checkright(board)) {
        list.add(pos);
       }
       for (int[] pos : checkrightbackward(board)) {
        list.add(pos);
       }
       for (int[] pos : checkrightforward(board)) {
        list.add(pos);
       }
       //Return the list of possible moves found
       return list;
   }
   //Method to move the Queen to a new position passing a few arguments relevant to the new position
   public void moveQueen(int i,int j, Piece[][] currentboard){
       currentboard[x][y] = new Piece(0,white);
       currentboard[i][j] = new Queen(white,i,j);
    }
}