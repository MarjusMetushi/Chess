//Piece class, here the distinction between the different types of pieces is made and for the color of the pieces
public class Piece {
    int x,y;
    boolean ispawn = false;
    boolean isempty = false;
    boolean white;
    boolean isRook = false;
    boolean isBishop = false;
    boolean isKnight = false;
    boolean isQueen = false;
    boolean isKing = false;

    Piece(int d, boolean white){
        if(d==1){
            ispawn = true;
        } else if (d==0) {
            isempty = true;
        } else if (d==2) {
            isBishop = true;
        }else if (d==3) {
            isRook = true;
        } else if (d==4) {
            isKnight = true;
        } else if (d==5) {
            isQueen = true;
        } else if (d==6) {
            isKing = true;
        }

        if(white){
            this.white = true;
        } else {
            this.white = false;
        }
    }
}