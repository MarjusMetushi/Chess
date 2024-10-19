import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.*;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.Border;
//Game class, here the game is initialized and the main game loop is run. As well as further rules are implemented
public class Game extends JFrame implements ActionListener{
    JPanel gamepanel = new JPanel();
    Piece[][] board = new Piece[8][8];
    JButton[][] boardbuttons = new JButton[8][8];
    ImageIcon blackpawn = new ImageIcon("img\\black-pawn.png");
    ImageIcon whitepawn = new ImageIcon("img\\white-pawn.png");
    ImageIcon blackbishop = new ImageIcon("img\\black-bishop.png");
    ImageIcon whitebishop = new ImageIcon("img\\white-bishop.png");
    ImageIcon blackrook = new ImageIcon("img\\black-rook.png");
    ImageIcon whiterook = new ImageIcon("img\\white-rook.png");
    ImageIcon blackknight = new ImageIcon("img\\black-knight.png");
    ImageIcon whiteknight = new ImageIcon("img\\white-knight.png");
    ImageIcon blackqueen = new ImageIcon("img\\black-queen.png");
    ImageIcon whitequeen = new ImageIcon("img\\white-queen.png");
    ImageIcon blackking = new ImageIcon("img\\black-king.png");
    ImageIcon whiteking = new ImageIcon("img\\white-king.png");
    ArrayList<JButton> possiblemoves = new ArrayList<JButton>();
    boolean isSelected = false;
    Piece selected;
    Border blackborder = BorderFactory.createLineBorder(Color.BLACK , 3,true);
    Timer timer1;
    Timer timer2;
    int[] time1 = new int[]{0,10};
    int[] time2 = new int[]{0,10};
    JLabel playertimer1;
    JLabel playertimer2;
    boolean whitekingMoved = false;
    boolean blackkingMoved = false;
    boolean whiterook1 = false;
    boolean whiterook2 = false;
    boolean blackrook1 = false;
    boolean blackrook2 = false;
    JLayeredPane layeredPane = new JLayeredPane();
    int movetracker = 0;
    int fiftymoverule = 0;
    ArrayList<position> savedpositions = new ArrayList<>();
    boolean player1draw = false;
    boolean player2draw = false;
    JPanel buttonspanel = new JPanel();
    JLabel drawLabel;
    JPanel sidepanel = new JPanel();
    JTextArea movespanel = new JTextArea(40,17);
    JScrollPane scroll = new JScrollPane(movespanel);
    Piece[][] var = null;
    //Constructor to create a new Game object and make the frame 
    Game(){
        //Setting up the frame with the title and size and making it unresizable
        super("Chess Game");
        setSize(920,815);
        setLayout(new FlowLayout());
        getContentPane().setBackground(Color.lightGray);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        //Initializing the players, board and sidepanel
        Player(2);
        addboard();
        addsidepanel();
        add(gamepanel);
        //Initializing the starting position of the pieces
        ArrayList<JButton> start = new ArrayList<JButton>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if(i==7 || i==6)start.add(boardbuttons[i][j]);
            }
        }
        //Enabling the buttons and playing the start sound
        enablebuttons(start);
        play("C:\\Users\\mariu\\Desktop\\Java Projects\\Chess\\chess\\soundfx\\game-start.wav");
        //Initializing the player and adding the pieces, as well as drawing and resigning options
        Player(1);
        addpieces();
        DrawAndResign();
        //Initializing the position and adding it to the list of saved positions
        position p = new position(board, findAllPossibleMoves());
        savedpositions.add(p);
    }
    //Method to play a sound file
    public void play(String filePath){
        try {
            File file = new File(filePath);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
            clip.addLineListener(event -> {
            if (event.getType() == LineEvent.Type.STOP) {
                clip.close();  
            }
        });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //Method to draw and resign
    private void DrawAndResign(){
        //Making the front end for the draw button
        JButton draw = new JButton("Draw");
        draw.setFocusable(false);
        draw.setBackground(Color.LIGHT_GRAY);
        drawLabel = new JLabel();
        drawLabel.setVisible(false);
        draw.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Allowing the players to request draws
                if (timer1.isRunning()) {
                    player1draw = true;
                    drawLabel.setVisible(true);
                    drawLabel.setText("White has requested a draw");
                }else if(timer2.isRunning()){
                    player2draw = true;
                    drawLabel.setVisible(true);
                    drawLabel.setText("Black has requested a draw");
                }
                //Adding functionality to the buttons by calling the endgame method in case both players agree to a draw
                if(player1draw && player2draw){
                    endgame(false,true);
                }
            }
        });
        //Making the front end for resigning as a button
        JButton resign = new JButton("Resign");
        resign.setFocusable(false);
        resign.setBackground(Color.LIGHT_GRAY);
        resign.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Allowing the players to resign
                if (timer1.isRunning()) {
                    endgame(false, false);
                }
                if (timer2.isRunning()) {
                    endgame(true, false);
                }
            }
        });
        buttonspanel.add(drawLabel);
        buttonspanel.add(draw);
        buttonspanel.add(resign);
    }

    private JPanel Playerbar(){
        //Creating the panel for the players
        JPanel panel = new JPanel();
        //Making the front end of the panel
        panel.setLayout(new FlowLayout());
        panel.setPreferredSize(new Dimension(950,50));
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK , 3,true));
        panel.setBackground(Color.decode("#deb887"));
        
        return panel;
    }

    //Method to add more functionality to the players
    private void Player(int n){
        //Editing the playerbar
        JPanel panel = Playerbar();
        //Making the front end for the player's name, in this case player 1 or player 2
        JLabel playername = new JLabel("Player "+n);
        playername.setFont(new Font("Times New Roman", Font.BOLD, 20));
        playername.setPreferredSize(new Dimension(200, 30));
        panel.add(playername);
        //Adding some space to the panel to make everything look better
        JLabel space = new JLabel();
        space.setBackground(Color.decode("#deb887"));
        space.setPreferredSize(new Dimension(300, 40));
        panel.add(space);
        //Making the front end for the player's timer
        if(n==1){ 
        playertimer1 = new JLabel("10:00");
        playertimer1.setFont(new Font("Times New Roman", Font.BOLD, 20));
        playertimer1.setPreferredSize(new Dimension(60, 40));
        panel.add(playertimer1);
        //Making the timer for player 1 work
        timer1 = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                if(time1[0]==0){
                    if (time1[1]>0) {
                        time1[1]--;
                        time1[0] = 59;
                    }
                }
                //Adding functionality to the timer by calling the endgame method 
                //in case the player runs out of time and deciding whether to draw or not based on material
                if(time1[1]==0 && time1[0]==0){
                    if (hasinsufficientmaterial(false)) {
                        endgame(false,true);
                    }
                    endgame(false,false);
                }
                updatetime();
                if (time1[0]>0) {
                    time1[0]--;
                }
                //Playing a sound once the timer reaches 10 seconds to indicate the player is low on time
                if ((time1[0]==10 && time1[1]==0) || (time2[0]==10 && time2[1]==0)) {
                    play("C:\\Users\\mariu\\Desktop\\Java Projects\\Chess\\chess\\soundfx\\tenseconds.wav");
                }
            }
        });
        //Starting the timer
        timer1.start();
        } else {
            //Making the front end for the player 2 timer
            playertimer2 = new JLabel("10:00");
            playertimer2.setFont(new Font("Times New Roman", Font.BOLD, 20));
            playertimer2.setPreferredSize(new Dimension(60, 40));
            panel.add(playertimer2);
            //Making the timer for player 2 work
            timer2 = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(time2[0]==0){
                        if (time2[1]>0) {
                            time2[1]--;
                            time2[0] = 59;
                        }
                    }
                    if(time2[1]==0 && time2[0]==0){
                        if (hasinsufficientmaterial(true)) {
                            endgame(true,true);
                        }
                        endgame(true,false);
                    }
                    updatetime();
                    if (time2[0]>0) {
                        time2[0]--;
                    }
                    //Playing a sound once the timer reaches 10 seconds to indicate the player is low on time
                    if ((time1[0]==10 && time1[1]==0) || (time2[0]==10 && time2[1]==0)) {
                        play("C:\\Users\\mariu\\Desktop\\Java Projects\\Chess\\chess\\soundfx\\tenseconds.wav");
                    }
                }
            });
        }
        add(panel);
    }
    //Method to update the timer of player 1 and player 2
    private void updatetime(){
        playertimer1.setText(String.format("%02d:%02d", time1[1], time1[0]));
        playertimer2.setText(String.format("%02d:%02d", time2[1], time2[0]));
    }
    //Method to add the board to the game
    private void addboard(){
        //Making the board panel, setting the size of the board and adding it to the game panel
        JPanel boardpanel = new JPanel();
        boardpanel.setLayout(new GridLayout(8,8));
        boardpanel.setBounds(0,0,650,650);

        layeredPane.setPreferredSize(new Dimension(670, 650));
        layeredPane.add(boardpanel, JLayeredPane.DEFAULT_LAYER);

        gamepanel.add(layeredPane);
        
        //Making the buttons for the board and adding them to the board panel
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                JButton b = new JButton();
                boardpanel.add(b);
                b.setBorder(null);
                boardbuttons[i][j] = b;
                boardbuttons[i][j].setFocusable(false);
                b.addActionListener(this);
                //the buttons are added with the respective colors
                if((i+j)%2==0){ 
                    b.setBackground(Color.white);
                } else {
                    b.setBackground(Color.decode("#769656"));
                }
            }
        }
    }
    //Method for creating the side panel for displaying the moves and buttons for resignation and drawing
    private void addsidepanel(){
        //Setting up the front end for the side panel, moves panel and buttonspanel 
        sidepanel.setPreferredSize(new Dimension(180, 650));
        gamepanel.add(sidepanel);

        movespanel.setLayout(new BoxLayout(movespanel, BoxLayout.Y_AXIS));
        movespanel.setLineWrap(true);
        movespanel.setWrapStyleWord(true);
        movespanel.setEditable(false);
        //Making the scrollpane for the moves panel in case the text of the moves exceeds the size of the panel
        JScrollPane scroll = new JScrollPane(movespanel);

        buttonspanel.setPreferredSize(new Dimension(195,80));
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        movespanel.setFont(new Font("Times New Roman", Font.BOLD, 11));

        sidepanel.add(scroll);
        sidepanel.add(buttonspanel);
    }
    //Method for ending the game with a win or a draw
    public void endgame(boolean white, boolean draw){
        play("C:\\Users\\mariu\\Desktop\\Java Projects\\Chess\\chess\\soundfx\\game-end.wav");
        if(draw){
            JOptionPane.showMessageDialog(null, "Draw");
        } else {
            JOptionPane.showMessageDialog(null, white ? "White wins" : "Black wins");
        }
        //Exiting the game
        System.exit(0);
    }
    //Method for adding the pieces to the board
    private void addpieces(){
        //Adding the pieces to the board based on rows and columns and the color of the piece on the board
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if(i==1){
                    board[i][j] = new Pawn(false,i,j, 0, false, false, false ,false);
                } else if(i==6){
                    board[i][j] = new Pawn(true,i,j, 0, false ,false, false ,false);
                } else if(i==0 && (j==2 || j==5)){
                    board[i][j] = new Bishop(false,i,j);
                } else if(i==7 && (j==2 || j==5)){
                    board[i][j] = new Bishop(true,i,j);
                } else if (i==0 && (j==0 || j==7)){
                    board[i][j] = new Rook(false,i,j);
                }else if (i==7 && (j==0 || j==7)){
                    board[i][j] = new Rook(true,i,j);
                } else if(i==0 && (j==1 || j==6)){
                    board[i][j] = new Knight(false,i,j);
                } else if(i==7 && (j==1 || j==6)){ 
                    board[i][j] = new Knight(true,i,j);
                }else if (i==0 && j==3) {
                    board[i][j] = new Queen(false,i,j);
                }else if (i==7 && j==3) {
                    board[i][j] = new Queen(true,i,j);
                } else if(i==7 && j==4){
                    board[i][j] = new King(true,i,j);
                } else if(i==0 && j==4){
                    board[i][j] = new King(false,i,j);
                }
                //otherwise the piece is a empty square
                else{
                    board[i][j] = new Piece(0,false);
                }
            }
        }
        updatedisplay();
        findAllPossibleMoves();
    }
    //Method for updating the display of the board for each piece after moving a piece
    private void updatedisplay(){
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                boardbuttons[i][j].setIcon(null);
                boardbuttons[i][j].setBorder(null);
                if(board[i][j].ispawn && board[i][j].white){
                    boardbuttons[i][j].setIcon(whitepawn);
                    boardbuttons[i][j].setDisabledIcon(whitepawn);
                } else if(board[i][j].ispawn && !board[i][j].white){
                    boardbuttons[i][j].setIcon(blackpawn);
                    boardbuttons[i][j].setDisabledIcon(blackpawn);
                } else if(board[i][j].isBishop && board[i][j].white){
                    boardbuttons[i][j].setIcon(whitebishop);
                    boardbuttons[i][j].setDisabledIcon(whitebishop);
                } else if(board[i][j].isBishop && !board[i][j].white){
                    boardbuttons[i][j].setIcon(blackbishop);
                    boardbuttons[i][j].setDisabledIcon(blackbishop);
                } else if (board[i][j].isRook && board[i][j].white){
                    boardbuttons[i][j].setIcon(whiterook);
                    boardbuttons[i][j].setDisabledIcon(whiterook);
                }else if (board[i][j].isRook && !board[i][j].white){
                    boardbuttons[i][j].setIcon(blackrook);
                    boardbuttons[i][j].setDisabledIcon(blackrook);
                } else if(board[i][j].isKnight && !board[i][j].white){
                    boardbuttons[i][j].setIcon(blackknight);
                    boardbuttons[i][j].setDisabledIcon(blackknight);
                } else if(board[i][j].isKnight && board[i][j].white){
                    boardbuttons[i][j].setIcon(whiteknight);
                    boardbuttons[i][j].setDisabledIcon(whiteknight);
                } else if (board[i][j].isQueen && board[i][j].white) {
                    boardbuttons[i][j].setIcon(whitequeen);
                    boardbuttons[i][j].setDisabledIcon(whitequeen);
                } else if (board[i][j].isQueen && !board[i][j].white) {
                    boardbuttons[i][j].setIcon(blackqueen);
                    boardbuttons[i][j].setDisabledIcon(blackqueen);
                } else if (board[i][j].isKing && !board[i][j].white) {
                    boardbuttons[i][j].setIcon(blackking);
                    boardbuttons[i][j].setDisabledIcon(blackking);
                } else if (board[i][j].isKing && board[i][j].white) {
                    boardbuttons[i][j].setIcon(whiteking);
                    boardbuttons[i][j].setDisabledIcon(whiteking);
                }
            }
        }
    }
    //Method for getting the position of a button in the board
    public int[] getPosition(JButton b){
        int[] pos = new int[2];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (boardbuttons[i][j] == b) {
                    pos[0] = i;
                    pos[1] = j;
                }
            }
        }
        return pos;
    }
    //Method for getting the position of a piece in the board
    public int[] getPosition(Piece piece){
        int[] pos = new int[2];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == piece) {
                    pos[0] = i;
                    pos[1] = j;
                }
            }
        }
        return pos;
    }
    //Method for making the actions when a button is clicked 
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton sourcebutton = (JButton) e.getSource();
            int[] pos = getPosition(sourcebutton);
            int x = pos[0];
            int y = pos[1];
            Piece sourcepiece = (Piece) board[x][y];
            //Allowing the user to deselect a piece by clicking it again
            if(isSelected && sourcepiece==selected){
                isSelected = false;
                sourcebutton.setBorder(null);
                for (JButton b : possiblemoves) {
                    b.setBorder(null);
                }
                possiblemoves.clear();
                ArrayList<JButton> enabledbuttons = new ArrayList<JButton>();
                findenabledbuttons(enabledbuttons);
                enablebuttons(enabledbuttons);
                return;
            }
            //Allows the user to select a piece by clicking it
            if(!isSelected){
                checkMove(sourcepiece, sourcebutton , x, y);
                possiblemoves.add(sourcebutton);
                enablebuttons(possiblemoves);
            }
            //Allows the user to make a move by clicking the preferred button on the board
            else if(isSelected){
                makeMove(sourcebutton,x,y,false);
                ArrayList<JButton> enabledbuttons = new ArrayList<JButton>();
                findenabledbuttons(enabledbuttons);
                enablebuttons(enabledbuttons);
            }
        }
        //Method for checking and showing all the legal moves the selected piece can make
        public void checkMove(Piece sourcepiece ,JButton sourceButton, int x, int y){
            if(!sourcepiece.isempty){
                isSelected = true;
                //letting the user know which piece is selected
                sourceButton.setBorder(blackborder);
                //checking if the selected piece is a pawn
                if(sourcepiece.ispawn){
                    //relevant to know the past moves of the pawn in order to judge for en Passant
                    if (savedpositions.size()>1) {
                        var = savedpositions.get(savedpositions.size()-2).board;
                    }
                    //Creating a new Pawn object for the selected piece
                    Pawn selectedpiece = (Pawn) sourcepiece;
                    selected = sourcepiece;
                    //checking if the selected piece can move based on the list retrieved from the Pawn class
                    if(selectedpiece.checkallmoves(board,var).size()>0){
                        for (int[] pos : selectedpiece.checkallmoves(board,var)) {
                            //checking if the selected piece can move if the king is in check in order to block the check 
                            if(moveincheck(selectedpiece, pos)){
                                possiblemoves.add(boardbuttons[pos[0]][pos[1]]);
                                boardbuttons[pos[0]][pos[1]].setBorder(blackborder);
                            }
                            //checking if the selected piece can perform en Passant right or left
                            if(selectedpiece.enPassant(board,var)!=null){
                                if(selectedpiece.enPassant(board,var).get(0)!=null && !selectedpiece.missedleft){
                                    selectedpiece.enPassantleft = true;
                                }if(selectedpiece.enPassant(board,var).get(1)!=null && !selectedpiece.missedright){
                                    selectedpiece.enPassantright = true;
                                }
                            }
                        }
                    }
                    //Creating a new Bishop object for the selected piece if the selected piece is a Bishop
                } else if(sourcepiece.isBishop){
                    Bishop selectedpiece = (Bishop) sourcepiece;
                    selected = sourcepiece;
                    //checking if the selected piece can move based on the list retrieved from the Bishop class
                    if(selectedpiece.checkallmoves(board).size()>0){
                        for (int[] pos : selectedpiece.checkallmoves(board)) {
                            if(moveincheck(selectedpiece, pos)){
                                possiblemoves.add(boardbuttons[pos[0]][pos[1]]);
                                boardbuttons[pos[0]][pos[1]].setBorder(blackborder);
                            }
                        }
                    }
                    //Creating a new Rook object for the selected piece if the selected piece is a Rook
                } else if (sourcepiece.isRook) {
                    Rook selectedpiece = (Rook) sourcepiece;
                    selected = sourcepiece;
                    //checking if the selected piece can move based on the list retrieved from the Rook class
                    if(selectedpiece.checkallmoves(board).size()>0){
                        for (int[] pos : selectedpiece.checkallmoves(board)) {
                            if(moveincheck(selectedpiece, pos)){
                                possiblemoves.add(boardbuttons[pos[0]][pos[1]]);
                                boardbuttons[pos[0]][pos[1]].setBorder(blackborder);
                            }
                        }
                    }
                    //Creating a new Knight object for the selected piece if the selected piece is a Knight
                } else if (sourcepiece.isKnight) {
                    Knight selectedpiece = (Knight) sourcepiece;
                    selected = sourcepiece;
                    //checking if the selected piece can move based on the list retrieved from the Knight class
                    if (selectedpiece.checkallmoves(board).size()>0){
                        for (int[] pos : selectedpiece.checkallmoves(board)) {
                            if(moveincheck(selectedpiece, pos)){
                                possiblemoves.add(boardbuttons[pos[0]][pos[1]]);
                                boardbuttons[pos[0]][pos[1]].setBorder(blackborder);
                            }
                        }
                    }
                    //Creating a new Queen object for the selected piece if the selected piece is a Queen
                }else if (sourcepiece.isQueen) {
                    Queen selectedpiece = (Queen) sourcepiece;
                    selected = sourcepiece;
                    //Checking if the selected piece can move based on the list retrieved from the Queen class
                    if(selectedpiece.checkallmoves(board).size()>0){
                        for (int[] pos : selectedpiece.checkallmoves(board)) {
                            if(moveincheck(selectedpiece, pos)){
                                possiblemoves.add(boardbuttons[pos[0]][pos[1]]);
                                boardbuttons[pos[0]][pos[1]].setBorder(blackborder);
                            }
                        }
                    }
                    //Creating a new King object for the selected piece if the selected piece is a King
                } else if (sourcepiece.isKing) {
                    King selectedpiece = (King) sourcepiece;
                    selected = sourcepiece;
                    //Checking if the selected piece can move based on the list retrieved from the King class
                    if (selectedpiece.checkallmoves(board).size()>0){
                        for (int[] pos : selectedpiece.checkallmoves(board)) {
                            if(moveincheck(selectedpiece, pos)){
                                possiblemoves.add(boardbuttons[pos[0]][pos[1]]);
                                boardbuttons[pos[0]][pos[1]].setBorder(blackborder);
                            }
                        }
                    }
                    //Checking if the King can perform long castling for white and black
                    if(checkCastleLong()){
                        if(timer1.isRunning()){
                            possiblemoves.add(boardbuttons[7][0]);
                            boardbuttons[7][0].setBorder(blackborder);
                        } else if(timer2.isRunning()){
                            possiblemoves.add(boardbuttons[0][0]);
                            boardbuttons[0][0].setBorder(blackborder);
                        }
                    }
                    //Checking if the King can perform short castling for white and black
                    if(checkCastleShort()){
                        if(timer1.isRunning()){
                            possiblemoves.add(boardbuttons[7][7]);
                            boardbuttons[7][7].setBorder(blackborder);
                        } else if(timer2.isRunning()){
                            possiblemoves.add(boardbuttons[0][7]);
                            boardbuttons[0][7].setBorder(blackborder);
                        }
                    }
                    
                }
            }
        }
        //Method for making the move of the selected piece
        public void makeMove(JButton sourcebutton, int x, int y, boolean recursive){
            play("C:\\Users\\mariu\\Desktop\\Java Projects\\Chess\\chess\\soundfx\\move-self.wav");
            int[] pick = new int[]{0};
            boolean promote = false;
            //if the selected button is a highlighted button where the user can move
            if(possiblemoves.contains(sourcebutton)){
                if(recursive){
                    //if the selected piece is a pawn
                }else if(selected.ispawn){
                    //relevant to know the board of the past moves of the pawn in order to judge for en Passant
                    if (savedpositions.size()>1) {
                        var = savedpositions.get(savedpositions.size()-2).board;
                    }
                    //Creating a new Pawn object for the selected piece
                    Pawn selectedpiece = (Pawn) selected;
                    //checking if there are any moves for the pawn to make
                    if(selectedpiece.checkallmoves(board,var).size()>0){
                        //taking each move from the list
                        for (int[] pos : selectedpiece.checkallmoves(board,var)) {
                            if(boardbuttons[pos[0]][pos[1]]==sourcebutton){
                                //checking if the pawn can perform en Passant right or left
                                if(selectedpiece.enPassant(board,var)!=null && (selectedpiece.enPassantleft || selectedpiece.enPassantright)){
                                    //taking the new position where the pawn can perform en Passant and storing it in the newpos array
                                    int[] newpos = null;  
                                    for (int[] i : selectedpiece.enPassant(board,var)) {
                                        if(i!=null){
                                            newpos = i;
                                        }
                                    }
                                    //Making the move in the board for white and black by removing the piece taken
                                    if(selectedpiece.white && newpos[0]==pos[0] && newpos[1]==pos[1]){
                                        board[pos[0]+1][pos[1]]=new Piece(0,false);
                                    } else if(!selectedpiece.white && newpos[0]==pos[0] && newpos[1]==pos[1]){
                                        board[pos[0]-1][pos[1]]=new Piece(0,true);
                                    }
                                }
                                //if the pawn is at the end of the board the pawn will promote
                                if ((selectedpiece.white && pos[0]==0)||(!selectedpiece.white && pos[0]==7)){
                                    promotion(selectedpiece, pick, pos[0], pos[1]);
                                    promote = true;
                                }
                                //otherwise just move the pawn and display the move
                                else {
                                    if (board[pos[0]][pos[1]].isempty) {
                                        displayMoves(pos[0],pos[1],false,false,-1,-1);
                                    }else{
                                        displayMoves(pos[0],pos[1],true,false,-1,-1);   
                                    }
                                    //making the move
                                    selectedpiece.movepawn(pos[0],pos[1],board);
                                    fiftymoverule = -1;
                                }
                            }
                        }
                    }
                    //Creating a new Bishop object for the selected piece if the selected piece is a Bishop
                } else if (selected.isBishop) {
                    Bishop selectedpiece = (Bishop) selected;
                        //checking if the selected piece can move based on the list retrieved from the checkallmoves method of the Bishop class
                        if (selectedpiece.checkallmoves(board).size()>0){
                            //for each of the positions retrieved from the list find where to move and display the move
                            for (int[] pos : selectedpiece.checkallmoves(board)) {
                                if (boardbuttons[pos[0]][pos[1]]==sourcebutton) {
                                    //updating the 50 move rule
                                    if (!board[pos[0]][pos[1]].isempty && board[pos[0]][pos[1]].white!=selectedpiece.white) {
                                        fiftymoverule = -1;
                                    }
                                    //displaying the move
                                    if (board[pos[0]][pos[1]].isempty) {
                                        displayMoves(pos[0],pos[1],false,false,-1,-1);
                                    }else{
                                        displayMoves(pos[0],pos[1],true,false,-1,-1);
                                    }
                                    //making the move
                                    selectedpiece.movebishop(pos[0],pos[1],board);
                                }
                            }
                        }
                //Creating a new Rook object for the selected piece if the selected piece is a Rook
                } else if (selected.isRook) {
                    Rook selectedpiece = (Rook) selected;
                    if(selectedpiece.checkallmoves(board).size()>0){
                        for (int[] pos : selectedpiece.checkallmoves(board)) {
                            if (boardbuttons[pos[0]][pos[1]]==sourcebutton) {
                                int[] rookpos = getPosition(selectedpiece);
                                if (!board[pos[0]][pos[1]].isempty && board[pos[0]][pos[1]].white!=selectedpiece.white) {
                                    fiftymoverule = -1;
                                }
                                //depicting which rook is where for castling and it is necessary to know whether the rook has moved or not
                                if(rookpos[0]==7 && rookpos[1]==0){
                                    whiterook1 = true;
                                } else if(rookpos[0]==7 && rookpos[1]==7){
                                    whiterook2 = true;
                                } else if(rookpos[0]==0 && rookpos[1]==7){
                                    blackrook2 = true;
                                } else if(rookpos[0]==0 && rookpos[1]==0){
                                    blackrook1 = true;
                                }
                                //displaying the move
                                if (board[pos[0]][pos[1]].isempty) {
                                    displayMoves(pos[0],pos[1],false,false,-1,-1);
                                }else{
                                    displayMoves(pos[0],pos[1],true,false,-1,-1);
                                }
                                //making the move
                                selectedpiece.moveRook(pos[0],pos[1],board);
                            }
                        }
                    }
                    //Creating a new Knight object for the selected piece if the selected piece is a Knight
                }else if (selected.isKnight) {
                    Knight selectedpiece = (Knight) selected;
                    //checking if the selected piece can move based on the list retrieved from the checkallmoves method of the Knight class
                    if (selectedpiece.checkallmoves(board).size()>0){
                        for (int[] pos : selectedpiece.checkallmoves(board)) {
                                if(boardbuttons[pos[0]][pos[1]]==sourcebutton){
                                    //updating the 50 move rule
                                    if (!board[pos[0]][pos[1]].isempty && board[pos[0]][pos[1]].white!=selectedpiece.white) {
                                        fiftymoverule = -1;
                                    }
                                    //displaying the move
                                    if (board[pos[0]][pos[1]].isempty) {
                                        displayMoves(pos[0],pos[1],false,false,-1,-1);
                                    }else{
                                        displayMoves(pos[0],pos[1],true,false,-1,-1);
                                    }
                                    //making the move
                                    selectedpiece.moveknight(pos[0],pos[1],board);
                            }
                        }
                    }
                    //Creating a new Queen object for the selected piece if the selected piece is a Queen
                }else if (selected.isQueen) {
                    Queen selectedpiece = (Queen) selected;
                    //checking if the selected piece can move based on the list retrieved from the checkallmoves method of the Queen class
                    if(selectedpiece.checkallmoves(board).size()>0){
                        for (int[] pos : selectedpiece.checkallmoves(board)) {
                                if (boardbuttons[pos[0]][pos[1]]==sourcebutton) {
                                    //updating the 50 move rule
                                    if (!board[pos[0]][pos[1]].isempty && board[pos[0]][pos[1]].white!=selectedpiece.white) {
                                        fiftymoverule = -1;
                                    }
                                    //displaying the move
                                    if (board[pos[0]][pos[1]].isempty) {
                                        displayMoves(pos[0],pos[1],false,false,-1,-1);
                                    }else{
                                        displayMoves(pos[0],pos[1],true,false,-1,-1);
                                    }
                                    //making the move
                                    selectedpiece.moveQueen(pos[0],pos[1],board);
                                }
                            }
                    }
                    //Creating a new King object for the selected piece if the selected piece is a King
                } else if (selected.isKing) {
                    King selectedpiece = (King) selected;
                    //checking if the selected piece can move based on the list retrieved from the checkallmoves method of the King class
                    if (selectedpiece.checkallmoves(board).size()>0){
                        for (int[] pos : selectedpiece.checkallmoves(board)) {
                            if(boardbuttons[pos[0]][pos[1]]==sourcebutton){
                                //updating the 50 move rule
                                if (!board[pos[0]][pos[1]].isempty && board[pos[0]][pos[1]].white!=selectedpiece.white) {
                                    fiftymoverule = -1;
                                }
                                //displaying the move
                                if (board[pos[0]][pos[1]].isempty) {
                                    displayMoves(pos[0],pos[1],false,false,-1,-1);   
                                }else{
                                    displayMoves(pos[0],pos[1],true,false,-1,-1);
                                }
                                //making the move
                                selectedpiece.moveKing(pos[0],pos[1],board);
                                if(selectedpiece.white){
                                    whitekingMoved = true;
                                } else {
                                    blackkingMoved = true;
                                }
                            }
                        }
                    }
                    //checking for castling short or long and displaying the move
                    if(board[x][y].isRook){
                        if ((x==7 && y==7) || (x==0 && y==7) ){ 
                            //calling the method for castling short
                            castleshort();
                            displayMoves(-1,-1,false,false,0,-1);
                        } else if((x==0 && y==0) || (x==7 && y==0)){
                            //calling the method for castling long
                            castlelong();
                            displayMoves(-1,-1,false,false,1,-1);
                        }
                    }
                }
                //if the pawn is not promoted
                 if(!promote && !recursive){
                    isSelected = false;
                    //resetting the en Passant possibilities
                    resetpassant();
                    //clearing the possible moves
                    possiblemoves.clear();
                    //updating the display
                    updatedisplay();
                    //checks for threefold repetition draw
                    threefold();
                    //finding all the possible moves
                    findAllPossibleMoves();
                    //switching the turn of the players
                    switchturn();
                    //calling this method again recursively to make the code stall until the user has made a decision on promoting
                } else if(pick[0]==0 && promote){
                    makeMove(sourcebutton, x, y, true);
                }
                //if the king is in check setting a red border to let the user know that the king is in check
                if(KingInCheck(board)){
                    for (int i = 0; i < 8; i++) {
                        for (int j = 0; j < 8; j++) {
                            if(timer1.isRunning() && board[i][j].isKing && board[i][j].white){
                                boardbuttons[i][j].setBorder(BorderFactory.createLineBorder(Color.RED , 3,true));
                            } else if(timer2.isRunning() && board[i][j].isKing && !board[i][j].white){
                                boardbuttons[i][j].setBorder(BorderFactory.createLineBorder(Color.RED , 3,true));
                            }
                        }
                    }
                }
                //if there is a checkmate calling the game off
                if(KingInCheck(board)){
                    if(CheckMate()){
                        endgame(timer1.isRunning() ? false : true,false);
                    }
                }
                //otherwise incrementing the 50 move rule and checking if the game can be called off by the 50 move rule
                fiftymoverule++;
                if(fiftymoverule==100){
                    endgame(false,true);
                }
                //if there are no possible moves left and the king is in check calling the game as a win/lose
                if(findAllPossibleMoves().size()==0){
                    if(KingInCheck(board)){
                        endgame(timer1.isRunning() ? false : true, false);
                    } else {
                        //otherwise its a stalemate
                        endgame(true, true);
                    }
                }
                //if there are insufficient material on both sides calling the game as a draw
                if(hasinsufficientmaterial(true) && hasinsufficientmaterial(false)){
                    endgame(true, true);
                }
            }
        }
        //Method for displaying the moves
        public void displayMoves(int x , int y, boolean capture, boolean promotion, int castle, int promotiontype){
            //Creating a stringbuilder for concatenating the moves and forming a notation
            StringBuilder movestr = new StringBuilder();
            //Creating a hashmap for the y axis of the board to name the squares
            HashMap<Integer,Character> mapY = new HashMap<Integer,Character>();
            mapY.put(0, 'a');
            mapY.put(1, 'b');
            mapY.put(2, 'c');
            mapY.put(3, 'd');
            mapY.put(4, 'e');
            mapY.put(5, 'f');
            mapY.put(6, 'g');
            mapY.put(7, 'h');
            //Creating a hashmap for the x axis of the board to name the squares
            HashMap<Integer,Character> mapX = new HashMap<Integer,Character>();
            mapX.put(0, '8');
            mapX.put(1, '7');
            mapX.put(2, '6');
            mapX.put(3, '5');
            mapX.put(4, '4');
            mapX.put(5, '3');
            mapX.put(6, '2');
            mapX.put(7, '1');
            //Appending the notation of the piece to the stringbuilder
            if(!selected.isempty && castle!=1 && castle!=0){
                if(selected.isQueen){
                    movestr.append("Q");
                } else if(selected.isBishop){
                    movestr.append("B");
                } else if(selected.isKnight){
                    movestr.append("N");
                } else if(selected.isRook){
                    movestr.append("R");
                } else if(selected.isKing){
                    movestr.append("K");
                }
                movestr.append(mapY.get(selected.y));
                movestr.append(mapX.get(selected.x));
                //if the move is a capture append x to the stringbuilder and play a sound
                if(capture){
                    play("C:\\Users\\mariu\\Desktop\\Java Projects\\Chess\\chess\\soundfx\\capture.wav");
                    movestr.append("x");
                }
                movestr.append(mapY.get(y));
                movestr.append(mapX.get(x));
                //create a temporary board
                Piece[][] tempboard = new Piece[8][8];
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        tempboard[i][j] = board[i][j];
                    }
                }
                if(selected.ispawn){
                    Pawn selectedpiece = (Pawn) selected;
                    selectedpiece.movepawn(x,y,tempboard);
                } else if(selected.isBishop){
                    Bishop selectedpiece = (Bishop) selected;
                    selectedpiece.movebishop(x,y,tempboard);
                } else if(selected.isRook){
                    Rook selectedpiece = (Rook) selected;
                    selectedpiece.moveRook(x,y,tempboard);
                } else if(selected.isKnight){
                    Knight selectedpiece = (Knight) selected;
                    selectedpiece.moveknight(x,y,tempboard);
                }else if(selected.isQueen){
                    Queen selectedpiece = (Queen) selected;
                    selectedpiece.moveQueen(x,y,tempboard);
                } else if(selected.isKing){
                    King selectedpiece = (King) selected;
                    selectedpiece.moveKing(x,y,tempboard);
                }
                //if the king is in check append # to the stringbuilder otherwise append + to the stringbuilder to indicate a check
                if(KingInCheck(tempboard)){
                    if (CheckMate()) {
                        movestr.append("#");
                    } else {
                        movestr.append("+");
                    }
                }
                //if the user promoted then append = to the stringbuilder and play a sound, as well as append the notation of the promoted piece
                if(promotion){
                    play("C:\\Users\\mariu\\Desktop\\Java Projects\\Chess\\chess\\soundfx\\promote.wav");
                    movestr.append("=");
                    switch (promotiontype) {
                        case 1:
                            movestr.append("Q");
                            break;
                        case 2:
                            movestr.append("R");
                            break;
                        case 3:
                            movestr.append("B");
                            break;
                        case 4:
                            movestr.append("N");
                            break;
                    }
                }
            }
            //if the user is castling then append O-O or O-O-O to the stringbuilder based on the castling type and play a sound
            if(castle==0){
                play("C:\\Users\\mariu\\Desktop\\Java Projects\\Chess\\chess\\soundfx\\castle.wav");
                movestr.append("O-O");
            } else if(castle==1){
                play("C:\\Users\\mariu\\Desktop\\Java Projects\\Chess\\chess\\soundfx\\castle.wav");
                movestr.append("O-O-O");
            }
            if(KingInCheck(board) && (castle==1 || castle==0)){
                play("C:\\Users\\mariu\\Desktop\\Java Projects\\Chess\\chess\\soundfx\\move-check.wav");
                if (CheckMate()) {
                    movestr.append("#");
                } else {
                    movestr.append("+");
                }
            }
            //making the font of the moves panel bold and setting the size of the font
            movespanel.setFont(new Font("Times New Roman", Font.BOLD, 11));
            if (timer1.isRunning()) {
            movetracker++;
            movespanel.append("\n");   
            movespanel.append(" " + movetracker + ". ");
            }
            movespanel.append(movestr.toString());
            movespanel.append("            ");

        }
        //Method for checking if the player has insufficient material as per the rules of chess
        public boolean hasinsufficientmaterial(boolean white){
            ArrayList<Piece> arrayList = new ArrayList<Piece>();
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (!board[i][j].isempty && board[i][j].white==white) {
                        if (board[i][j].isBishop || board[i][j].isKnight) {
                            arrayList.add(board[i][j]);
                        }else if(!board[i][j].isKing){
                            return false;
                        }
                    }
                }
            }
            if (arrayList.size()==0) {
                return true;
            }
            if (arrayList.size()==1) {
                return true;
            }
            return false;
        }
        //Method for checking for threefold repetition draw
        public void threefold(){
            position p = new position(board, findAllPossibleMoves());

            int counter = 0; 
            //keeping tracks of how many times the same position has been played by using "same" method of the class "position"
            if (savedpositions.size()==0) {
                counter+=0;
            }else{
                for (int i = 0; i < savedpositions.size(); i++) {
                    for (int j = i+1; j < savedpositions.size(); j++) {
                        if(savedpositions.get(i).same(savedpositions.get(j))){
                            counter++;
                        }
                    }
                }
            }
            //if the counter is 3 then the game is a draw
            if (counter==3) {
                endgame(false,true);
            }
            //adding the current position to the list of saved positions
            savedpositions.add(p);
        }
        //Class for keeping track of the positions
        public class position{
            Piece[][] board = new Piece[8][8];
            //creating a board to check all the positions made and adding all the pieces as if it was the primary board
            ArrayList<JButton> possiblemoves = new ArrayList<>();
            public position(Piece[][] newboard, ArrayList<JButton> newpossiblemoves){
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        if (newboard[i][j].ispawn) {
                            Pawn pawn = (Pawn) newboard[i][j];
                            board[i][j] = new Pawn(pawn.white,i,j,pawn.movecounter,pawn.missedleft,pawn.missedright,pawn.enPassantleft,pawn.enPassantright);
                        } else if(newboard[i][j].isBishop){
                            Bishop bishop = (Bishop) newboard[i][j];
                            board[i][j] = new Bishop(bishop.white,i,j);
                        } else if(newboard[i][j].isRook){
                            Rook rook = (Rook) newboard[i][j];
                            board[i][j] = new Rook(rook.white,i,j);
                        } else if(newboard[i][j].isKnight){
                            Knight knight = (Knight) newboard[i][j];
                            board[i][j] = new Knight(knight.white,i,j);
                        }else if(newboard[i][j].isQueen){
                            Queen queen = (Queen) newboard[i][j];
                            board[i][j] = new Queen(queen.white,i,j);
                        }else if(newboard[i][j].isKing){
                            King king = (King) newboard[i][j];
                            board[i][j] = new King(king.white,i,j);
                        } else if (newboard[i][j].isempty) {
                            board[i][j] = new Piece(0,newboard[i][j].white);
                        }
                    }
                }
                this.possiblemoves.addAll(newpossiblemoves);
            }
            //Method for checking if the position is the same as the compared position
            public boolean same(position compare){
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        if(this.board[i][j].isempty!=compare.board[i][j].isempty){
                            return false;
                        } else if(this.board[i][j].white!=compare.board[i][j].white){
                            return false;
                        } else if(this.board[i][j].ispawn!=compare.board[i][j].ispawn){
                            return false;
                        } else if(this.board[i][j].isBishop!=compare.board[i][j].isBishop){
                            return false;
                        } else if(this.board[i][j].isKing!=compare.board[i][j].isKing){
                            return false;
                        } else if(this.board[i][j].isKnight!=compare.board[i][j].isKnight){
                            return false;
                        } else if(this.board[i][j].isQueen!=compare.board[i][j].isQueen){
                            return false;
                        } else if(this.board[i][j].isRook!=compare.board[i][j].isRook){
                            return false;
                            }
                        }
                    }
                    //if the number of possible moves is not the same then the positions are not the same
                if (this.possiblemoves.size()!=compare.possiblemoves.size()) {
                    return false;
                }
                //checking if the positions of the possible moves are the exact same
                for (int i = 0; i < this.possiblemoves.size(); i++) {
                    if(getPosition(this.possiblemoves.get(i))[0]!=getPosition(compare.possiblemoves.get(i))[0] || getPosition(this.possiblemoves.get(i))[1]!=getPosition(compare.possiblemoves.get(i))[1]){
                        return false;
                    }
                }
                return true;
            }
        }
        //Method for finding all the possible moves
        public ArrayList<JButton> findAllPossibleMoves(){
            ArrayList<JButton> findAllMoves = new ArrayList<JButton>();
            //finding all the possible moves for each piece
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (!board[i][j].isempty && ((timer1.isRunning() && board[i][j].white) || (timer2.isRunning() && !board[i][j].white))) {
                        if (board[i][j].ispawn) {
                            //creating a new Pawn object for the selected piece
                            Pawn selectedpiece = (Pawn) board[i][j];
                            if(savedpositions.size()>1){
                                var = savedpositions.get(savedpositions.size()-2).board;
                            }
                            //checking if the selected piece can move based on the list retrieved from the Pawn class and adding it to the ArrayList
                            for (int[] pos : selectedpiece.checkallmoves(board,var)) {
                                if(moveincheck(selectedpiece, pos)){
                                    findAllMoves.add(boardbuttons[pos[0]][pos[1]]);
                                }
                            }
                            //creating a new Bishop object for the selected piece if the selected piece is a Bishop
                        }else if(board[i][j].isBishop){
                            Bishop selectedpiece = (Bishop) board[i][j];
                            //checking if the selected piece can move based on the list retrieved from the Bishop class and adding it to the ArrayList
                            for (int[] pos : selectedpiece.checkallmoves(board)) {
                                if(moveincheck(selectedpiece, pos)){
                                    findAllMoves.add(boardbuttons[pos[0]][pos[1]]);
                                }
                            }
                            //creating a new Rook object for the selected piece if the selected piece is a Rook
                        } else if(board[i][j].isRook){
                            Rook selectedpiece = (Rook) board[i][j];
                            //checking if the selected piece can move based on the list retrieved from the Rook class and adding it to the ArrayList
                            for (int[] pos : selectedpiece.checkallmoves(board)) {
                                if(moveincheck(selectedpiece, pos)){
                                    findAllMoves.add(boardbuttons[pos[0]][pos[1]]);
                                }
                            }
                            //creating a new Knight object for the selected piece if the selected piece is a Knight
                        } else if(board[i][j].isKnight){
                            Knight selectedpiece = (Knight) board[i][j];
                            //checking if the selected piece can move based on the list retrieved from the Knight class and adding it to the ArrayList
                            for (int[] pos : selectedpiece.checkallmoves(board)) {
                                if(moveincheck(selectedpiece, pos)){
                                    findAllMoves.add(boardbuttons[pos[0]][pos[1]]);
                                }
                            }
                            //creating a new Queen object for the selected piece if the selected piece is a Queen
                        }else if(board[i][j].isQueen){
                            Queen selectedpiece = (Queen) board[i][j];
                            //checking if the selected piece can move based on the list retrieved from the Queen class and adding it to the ArrayList
                            for (int[] pos : selectedpiece.checkallmoves(board)) {
                                if(moveincheck(selectedpiece, pos)){
                                    findAllMoves.add(boardbuttons[pos[0]][pos[1]]);
                                }
                            }
                            //creating a new King object for the selected piece if the selected piece is a King
                        } else if(board[i][j].isKing){
                            King selectedpiece = (King) board[i][j];
                            //checking if the selected piece can move based on the list retrieved from the King class and adding it to the ArrayList
                            for (int[] pos : selectedpiece.checkallmoves(board)) {
                                if(moveincheck(selectedpiece, pos)){
                                    findAllMoves.add(boardbuttons[pos[0]][pos[1]]);
                                }
                            }
                        }
                    }
                }
            }
            //Returning the ArrayList of possible moves
            return findAllMoves;
        }
        //Method for promoting a pawn
        public void promotion(Piece piece, int[] pick, int x, int y){
            //disabling all the buttons
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    boardbuttons[i][j].setEnabled(false);
                }
            }
            //Creating a panel for the promotion
            JPanel promotionpanel = new JPanel();
            if(piece.white){
                switch (y) {
                    //displaying the promotion panel based on the y axis of the board where the pawn has reached the promotion square for both white and black
                    case 0:
                    promotionpanel.setBounds(2,0,94,469);
                        break;
                    case 1:
                        promotionpanel.setBounds(91,0,94,469);
                        break;
                    case 2:
                        promotionpanel.setBounds(185,0,94,469);
                        break;
                    case 3:
                        promotionpanel.setBounds(279,0,94,469);
                        break;
                    case 4:
                        promotionpanel.setBounds(373,0,94,469);
                        break;
                    case 5:
                        promotionpanel.setBounds(467,0,94,469);
                        break;
                    case 6:
                        promotionpanel.setBounds(561,0,94,469);
                        break;
                    case 7:
                        promotionpanel.setBounds(655,0,94,469);
                        break;
                }
            }
            if (!piece.white) {
                switch (y) {
                    case 0:
                    promotionpanel.setBounds(2,281,94,469);
                        break;
                    case 1:
                        promotionpanel.setBounds(95,281,94,469);
                        break;
                    case 2:
                        promotionpanel.setBounds(189,281,94,469);
                        break;
                    case 3:
                        promotionpanel.setBounds(283,281,94,469);
                        break;
                    case 4:
                        promotionpanel.setBounds(377,281,94,469);
                        break;
                    case 5:
                        promotionpanel.setBounds(471,281,94,469);
                        break;
                    case 6:
                        promotionpanel.setBounds(565,281,94,469);
                        break;
                    case 7:
                        promotionpanel.setBounds(659,281,94,469);
                        break;
                }
            }
            promotionpanel.setLayout(new GridLayout(5,1));
            
            layeredPane.add(promotionpanel, JLayeredPane.PALETTE_LAYER); 
            //creating the buttons for the promotion and adding them to the panel each assigned with a piece to promote
            for (int i = 0; i < 4; i++) {
            int[] temp = new int[1];
            temp[0] = i;
            JButton b = new JButton();
            switch (i) {
                case 3:
                    b.setIcon(piece.white ? whiteknight : blackknight);
                    break;
                case 2:
                    b.setIcon(piece.white ? whitebishop : blackbishop);
                    break;
                case 1:
                    b.setIcon(piece.white ? whiterook : blackrook);
                    break;
                case 0:
                    b.setIcon(piece.white ? whitequeen : blackqueen);
                    break;
            }
            //adding functionality to the buttons of the promotion panel
            b.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fiftymoverule = -1;
                    //depending on the button clicked the promotion type is assigned to the pick array
                    switch (temp[0]) {
                        case 0:
                            pick[0]=1;
                            if(!board[x][y].isempty){
                                displayMoves(x,y,true,true,-1,pick[0]);
                            } else {
                                displayMoves(x,y,false,true,-1,pick[0]);
                            }
                            board[x][y] = new Queen(piece.white,x,y);
                            board[selected.x][selected.y] = new Piece(0,piece.white);
                            break;
                        case 1:
                            pick[0]=2;
                            if(!board[x][y].isempty){
                                displayMoves(x,y,true,true,-1,pick[0]);
                            } else {
                                displayMoves(x,y,false,true,-1,pick[0]);
                            }
                            board[x][y] = new Rook(piece.white,x,y);
                            board[selected.x][selected.y] = new Piece(0,piece.white);
                            break;
                        case 2:
                            pick[0]=3;
                            if(!board[x][y].isempty){
                                displayMoves(x,y,true,true,-1,pick[0]);
                            } else {
                                displayMoves(x,y,false,true,-1,pick[0]);
                            }
                            board[x][y] = new Bishop(piece.white,x,y);
                            board[selected.x][selected.y] = new Piece(0,piece.white);
                            break;
                        case 3:
                            pick[0]=4;
                            if(!board[x][y].isempty){
                                displayMoves(x,y,true,true,-1,pick[0]);
                            } else {
                                displayMoves(x,y,false,true,-1,pick[0]);
                            }
                            board[x][y] = new Knight(piece.white,x,y);
                            board[selected.x][selected.y] = new Piece(0,piece.white);
                            break;
                    }
                    //after the user has decided what to do with the promotion the panel is removed and the buttons are enabled and turns are switched
                    promotionpanel.setVisible(false);
                    layeredPane.remove(promotionpanel);
                    isSelected = false;
                    resetpassant();
                    possiblemoves.clear();
                    updatedisplay();
                    switchturn();
                    ArrayList<JButton> enabledbuttons = new ArrayList<JButton>();
                    findenabledbuttons(enabledbuttons);
                    enablebuttons(enabledbuttons);
                }
            });
            b.setFocusable(false);
            b.setBackground(Color.white);
            promotionpanel.add(b);
            }
            //Creating a button for when the user decides to cancel the promotion and making the front end
            JButton exit = new JButton();
            exit.setText("X");
            exit.setFocusable(false);
            exit.setBackground(Color.white);
            exit.setFont(new Font("Comic Sans MS", Font.BOLD, 40));
            exit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //resetting the pawn to its original position
                    pick[0]=0;
                    promotionpanel.setVisible(false);
                    layeredPane.remove(promotionpanel);
                    isSelected = false;
                    boardbuttons[x][y].setBorder(null);
                    for (JButton b : possiblemoves) {
                        b.setBorder(null);
                    }
                    possiblemoves.clear();
                    ArrayList<JButton> enabledbuttons = new ArrayList<JButton>();
                    findenabledbuttons(enabledbuttons);
                    enablebuttons(enabledbuttons);
                }
            });
            promotionpanel.add(exit);
        }
        //Method for checking if pieces can move to block the check
        public boolean CheckMate(){
            boolean white;

            if(timer1.isRunning()){
                white = true;
            } else {
                white = false;
            }

            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if(board[i][j].ispawn && board[i][j].white==white){
                        //creating a new Pawn object 
                        Pawn access = (Pawn) board[i][j];
                        //checking if the selected piece can move based on the list retrieved from the Pawn class
                        for (int[] move : access.checkallmoves(board,savedpositions.get(savedpositions.size()-2).board)) {
                            //creating a temporary board
                            Piece[][] tempboard = new Piece[8][8];
                            for (int l = 0; l < 8; l++) {
                                for (int k = 0; k < 8;k++) {
                                    tempboard[l][k] = board[l][k];
                                }
                            }
                            //moving the pawn to the new position to check if the king is in check
                            access.movepawn(move[0],move[1],tempboard);
                            if(!KingInCheck(tempboard)){
                                return false;
                            }
                        }
                        //creating a new Bishop object
                    } else if(board[i][j].isBishop && board[i][j].white==white){
                        Bishop access = (Bishop) board[i][j];
                        //checking if the selected piece can move based on the list retrieved from the Bishop class
                        for (int[] move : access.checkallmoves(board)) {
                            Piece[][] tempboard = new Piece[8][8];
                            for (int l = 0; l < 8; l++) {
                                for (int k = 0; k < 8;k++) {
                                    tempboard[l][k] = board[l][k];
                                }
                            }
                            //moving the bishop to the new position to check if the king is in check
                            access.movebishop(move[0],move[1],tempboard);
                            if(!KingInCheck(tempboard)){
                                return false;
                            }
                        }
                        //creating a new Knight object
                    } else if(board[i][j].isKnight && board[i][j].white==white){
                        Knight access = (Knight) board[i][j];
                        //checking if the selected piece can move based on the list retrieved from the Knight class
                        for (int[] move : access.checkallmoves(board)) {
                            Piece[][] tempboard = new Piece[8][8];
                            for (int l = 0; l < 8; l++) {
                                for (int k = 0; k < 8;k++) {
                                    tempboard[l][k] = board[l][k];
                                }
                            }
                            //moving the knight to the new position to check if the king is in check
                            access.moveknight(move[0],move[1],tempboard);
                            if(!KingInCheck(tempboard)){
                                return false;
                            }
                        }
                        //creating a new Rook object
                    } else if(board[i][j].isRook && board[i][j].white==white){
                        Rook access = (Rook) board[i][j];
                        //checking if the selected piece can move based on the list retrieved from the Rook class
                        for (int[] move : access.checkallmoves(board)) {
                            Piece[][] tempboard = new Piece[8][8];
                            for (int l = 0; l < 8; l++) {
                                for (int k = 0; k < 8;k++) {
                                    tempboard[l][k] = board[l][k];
                                }
                            }
                            //moving the rook to the new position to check if the king is in check
                            access.moveRook(move[0],move[1],tempboard);
                            if(!KingInCheck(tempboard)){
                                return false;
                            }
                        }
                        //creating a new Queen object
                    } else if(board[i][j].isQueen && board[i][j].white==white){
                        Queen access = (Queen) board[i][j];
                        //checking if the selected piece can move based on the list retrieved from the Queen class
                        for (int[] move : access.checkallmoves(board)) {
                            Piece[][] tempboard = new Piece[8][8];
                            for (int l = 0; l < 8; l++) {
                                for (int k = 0; k < 8;k++) {
                                    tempboard[l][k] = board[l][k];
                                }
                            }
                            //moving the queen to the new position to check if the king is in check
                            access.moveQueen(move[0],move[1],tempboard);
                            if(!KingInCheck(tempboard)){
                                return false;
                            }
                        }
                        //creating a new King object
                    } else if(board[i][j].isKing && board[i][j].white==white){
                        King access = (King) board[i][j];
                        //checking if the selected piece can move based on the list retrieved from the King class
                        for (int[] move : access.checkallmoves(board)) {
                            Piece[][] tempboard = new Piece[8][8];
                            for (int l = 0; l < 8; l++) {
                                for (int k = 0; k < 8;k++) {
                                    tempboard[l][k] = board[l][k];
                                }
                            }
                            //moving the king to the new position to check if the king is in check
                            access.moveKing(move[0],move[1],tempboard);
                            if(!KingInCheck(tempboard)){
                                return false;
                            }
                        }
                    }
                }
            }
            //otherwise the king is in check and it can't be blocked
            return true;
        }
        //Method for making the move of castling short for white and black
        private void castleshort(){
            if(timer1.isRunning()){
                board[7][4] = new Piece(0,true);
                board[7][5] = new Rook(true,7,5);
                board[7][6] = new King(true,7,6);
                board[7][7] = new Piece(0,true);
            } else if(timer2.isRunning()){
                board[0][4] = new Piece(0,false);
                board[0][5] = new Rook(false,0,5);
                board[0][6] = new King(false,0,6);
                board[0][7] = new Piece(0,false);
            }
        }
        //Method for making the move of castling long for white and black
        private void castlelong(){
            if(timer1.isRunning()){
                board[7][4] = new Piece(0,true);
                board[7][3] = new Rook(true,7,5);
                board[7][2] = new King(true,7,6);
                board[7][0] = new Piece(0,true);
            } else if(timer2.isRunning()){
                board[0][4] = new Piece(0,false);
                board[0][3] = new Rook(false,0,5);
                board[0][2] = new King(false,0,6);
                board[0][0] = new Piece(0,false);
            }
        }
        //Method to check for the opportunity of castling long
        private boolean checkCastleLong(){
            //if the king has not moved and the rook is in the starting position then castling long is possible
            if(timer1.isRunning() && !KingInCheck(board)){
                if (!whitekingMoved && !whiterook1) {
                    Piece[][] tempboard = new Piece[8][8];
                    for (int i = 0; i < 8; i++) {
                        for (int j = 0; j < 8; j++) {
                            tempboard[i][j] = board[i][j];
                        }
                    }
                    King whiteking = (King) board[7][4];
                    if(!KingInCheck(tempboard) && board[7][3].isempty){
                        whiteking.moveKing(7,3,tempboard);
                        if(!KingInCheck(tempboard) && board[7][2].isempty){
                            whiteking.moveKing(7,2,tempboard);
                            if(!KingInCheck(tempboard) && board[7][1].isempty){
                                return true;
                            }
                        }
                    }
                }
            //if the king has not moved and the rook is in the starting position then castling long is possible
            } else if(timer2.isRunning() && !KingInCheck(board)){
                if(!blackkingMoved && !blackrook1){
                    Piece[][] tempboard = new Piece[8][8];
                    for (int i = 0; i < 8; i++) {
                        for (int j = 0; j < 8; j++) {
                            tempboard[i][j] = board[i][j];
                        }
                    }
                    King blackking = (King) board[0][4];
                    if(!KingInCheck(tempboard) && tempboard[0][3].isempty){
                        blackking.moveKing(0,3,tempboard);
                        if(!KingInCheck(tempboard) && tempboard[0][2].isempty){
                            blackking.moveKing(0,2,tempboard);
                            if(!KingInCheck(tempboard) && board[0][1].isempty){
                                return true;
                            }
                        }
                    }
                    
                }
            }
            //otherwise castling is not possible
           return false;
       }
        //Method to check for the opportunity of castling short
        private boolean checkCastleShort(){
        //if the king has not moved and the rook is in the starting position then castling short is possible
        if(timer1.isRunning() && !KingInCheck(board)){
            if (!whitekingMoved && !whiterook2) {
                Piece[][] tempboard = new Piece[8][8];
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        tempboard[i][j] = board[i][j];
                    }
                }
                King whiteking = (King) board[7][4];
                if(board[7][5].isempty){
                    whiteking.moveKing(7,5,tempboard);
                    if(!KingInCheck(tempboard) && board[7][6].isempty){
                        whiteking.moveKing(7,6,tempboard);
                        if(!KingInCheck(tempboard)){
                            return true;
                        }
                    }
                }
            }
            //if the king has not moved and the rook is in the starting position then castling short is possible
        } else if(timer2.isRunning() && !KingInCheck(board)){
            if(!blackkingMoved && !blackrook2){
                Piece[][] tempboard = new Piece[8][8];
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        tempboard[i][j] = board[i][j];
                    }
                }
                King blackking = (King) board[0][4];
                if(tempboard[0][5].isempty){
                    blackking.moveKing(0,5,tempboard);
                    if(!KingInCheck(tempboard) && tempboard[0][6].isempty){
                        blackking.moveKing(0,6,tempboard);
                        if(!KingInCheck(tempboard)){
                            return true;
                        }
                    }
                }
                
            }
        }
        //otherwise castling is not possible
        return false;
       }
        //Method for switching the turn of the players
        private void switchturn(){
        if(timer1.isRunning()){
            timer1.stop();
            updatetime();
            timer2.start();
            player2draw = false;
            if(!player1draw)drawLabel.setVisible(false);
        } else if(timer2.isRunning()){
            timer2.stop();
            updatetime();
            timer1.start();
            player1draw = false;
            if(!player2draw)drawLabel.setVisible(false);
            }
        }
        //Method for enabling the buttons from the enabled arraylist
        private void enablebuttons(ArrayList<JButton> enabled){
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    boardbuttons[i][j].setEnabled(false);
                }
            }
            for (JButton b : enabled) {
                b.setEnabled(true);
            }
        }
        //Method for finding the enabled buttons
        private void findenabledbuttons(ArrayList<JButton> enabled){
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if(timer1.isRunning() && !board[i][j].isempty && board[i][j].white){
                        enabled.add(boardbuttons[i][j]);
                    } else if(timer2.isRunning() && !board[i][j].isempty && !board[i][j].white){
                        enabled.add(boardbuttons[i][j]);
                    }
                }
            }
        }
        //Method for checking if the king is in check
        public boolean KingInCheck(Piece[][] currentboard){
            int[] kingpos = null;
            if (timer1.isRunning()) {
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        //iterating the board to find the white king and storing its position
                        if(currentboard[i][j].isKing && currentboard[i][j].white){
                            kingpos = new int[]{i,j};
                        }
                    }
                }
                //iterating the board to find all the opponents moves and if any of these moves sees the king
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        if(!currentboard[i][j].isempty && !currentboard[i][j].white){
                            if(currentboard[i][j].isQueen){
                                Queen selected = (Queen) currentboard[i][j];
                                for (int[] pos : selected.checkallmoves(currentboard)) {
                                    if(pos[0]==kingpos[0] && pos[1]==kingpos[1]){
                                        return true;
                                    }
                                }
                            } else if(currentboard[i][j].isBishop){
                                Bishop selected = (Bishop) currentboard[i][j];
                                for (int[] pos : selected.checkallmoves(currentboard)) {
                                    if(pos[0]==kingpos[0] && pos[1]==kingpos[1]){
                                        return true;
                                    }
                                }
                            } else if(currentboard[i][j].isKnight){
                                Knight selected = (Knight) currentboard[i][j];
                                for (int[] pos : selected.checkallmoves(currentboard)) {
                                    if(pos[0]==kingpos[0] && pos[1]==kingpos[1]){
                                        return true;
                                    }
                                }
                            } else if(currentboard[i][j].isRook){
                                Rook selected = (Rook) currentboard[i][j];
                                for (int[] pos : selected.checkallmoves(currentboard)) {
                                    if(pos[0]==kingpos[0] && pos[1]==kingpos[1]){
                                        return true;
                                    }
                                }
                            } else if(currentboard[i][j].ispawn){
                                Pawn selected = (Pawn) currentboard[i][j];
                                if(savedpositions.size()>1){
                                var = savedpositions.get(savedpositions.size()-2).board;
                            }
                                for (int[] pos : selected.checkallmoves(currentboard,var)) {
                                    if(pos[0]==kingpos[0] && pos[1]==kingpos[1]){
                                        return true;
                                    }
                                }
                            } else if (currentboard[i][j].isKing) {
                                King selected = (King) currentboard[i][j];
                                for (int[] pos : selected.checkallmoves(currentboard)) {
                                    if(pos[0]==kingpos[0] && pos[1]==kingpos[1]){
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
                //iterating the board to find the black king and storing its position
            }else if (timer2.isRunning()) {
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        if(currentboard[i][j].isKing && !currentboard[i][j].white){
                            kingpos = new int[]{i,j};
                        }
                    }
                }
                //iterating the board to find all the opponents moves and if any of these moves sees the king
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        if(!currentboard[i][j].isempty && currentboard[i][j].white){
                            if(currentboard[i][j].isQueen){
                                Queen selected = (Queen) currentboard[i][j];
                                for (int[] pos : selected.checkallmoves(currentboard)) {
                                    if(pos[0]==kingpos[0] && pos[1]==kingpos[1]){
                                        return true;
                                    }
                                }
                            } else if(currentboard[i][j].isBishop){
                                Bishop selected = (Bishop) currentboard[i][j];
                                for (int[] pos : selected.checkallmoves(currentboard)) {
                                    if(pos[0]==kingpos[0] && pos[1]==kingpos[1]){
                                        return true;
                                    }
                                }
                            } else if(currentboard[i][j].isKnight){
                                Knight selected = (Knight) currentboard[i][j];
                                for (int[] pos : selected.checkallmoves(currentboard)) {
                                    if(pos[0]==kingpos[0] && pos[1]==kingpos[1]){
                                        return true;
                                    }
                                }
                            } else if(currentboard[i][j].isRook){
                                Rook selected = (Rook) currentboard[i][j];
                                for (int[] pos : selected.checkallmoves(currentboard)) {
                                    if(pos[0]==kingpos[0] && pos[1]==kingpos[1]){
                                        return true;
                                    }
                                }
                            } else if(currentboard[i][j].ispawn){
                                Pawn selected = (Pawn) currentboard[i][j];
                                for (int[] pos : selected.checkallmoves(currentboard,savedpositions.get(savedpositions.size()-2).board)) {
                                    if(pos[0]==kingpos[0] && pos[1]==kingpos[1]){
                                        return true;
                                    }
                                }
                            } else if (currentboard[i][j].isKing) {
                                King selected = (King) currentboard[i][j];
                                for (int[] pos : selected.checkallmoves(currentboard)) {
                                    if(pos[0]==kingpos[0] && pos[1]==kingpos[1]){
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            //otherwise the king is not in check
            return false;
        }
        //Method for setting the en Passant to false in case en passant was possible for a specific piece
        private void resetpassant(){
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if(board[i][j].ispawn){
                        Pawn access = (Pawn) board[i][j];
                        if(access.enPassantleft){
                            access.enPassantleft = false;
                            access.missedleft = true;
                        }
                        if(access.enPassantright){
                            access.enPassantright = false;
                            access.missedright = true;
                        }
                    }
                }
            }
        }
        //Method for checking if the move does not lead to a check and determines if the king is pinned
        private boolean moveincheck(Piece selectedpiece, int[] pos){
            //Creating a temporary board
            Piece[][] tempboard = new Piece[8][8];
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        tempboard[i][j] = board[i][j];
                    }
                }
                //Determining the type of the piece and moving it to the new position
                if(selectedpiece.isQueen){
                    Queen a = (Queen) selectedpiece;
                    a.moveQueen(pos[0],pos[1],tempboard);
                } else if(selectedpiece.isBishop){
                    Bishop a = (Bishop) selectedpiece;
                    a.movebishop(pos[0],pos[1],tempboard);
                } else if(selectedpiece.isKnight){
                    Knight a = (Knight) selectedpiece;
                    a.moveknight(pos[0],pos[1],tempboard);
                } else if(selectedpiece.isRook){
                    Rook a = (Rook) selectedpiece;
                    a.moveRook(pos[0],pos[1],tempboard);
                } else if(selectedpiece.ispawn){
                    Pawn a = (Pawn) selectedpiece;
                    a.movepawn(pos[0],pos[1],tempboard);
                    if(savedpositions.size()>1){
                        var = savedpositions.get(savedpositions.size()-2).board;
                    }
                    if(a.enPassant(tempboard,var)!=null){
                        int[] newpos = null;  
                        if(savedpositions.size()>1){
                            var = savedpositions.get(savedpositions.size()-2).board;
                        }
                        for (int[] i : a.enPassant(board,var)) {
                            if(i!=null){
                                newpos = i;
                            }
                        }
                        if((!a.missedleft && a.enPassantleft) ^ (!a.missedright && a.enPassantright) && a.white && newpos[0]==pos[0] && newpos[1]==pos[1]){
                            tempboard[pos[0]+1][pos[1]]=new Piece(0,false);
                        } else if((!a.missedleft && a.enPassantleft) ^ (!a.missedright && a.enPassantright) && !a.white && newpos[0]==pos[0] && newpos[1]==pos[1]){
                            tempboard[pos[0]-1][pos[1]]=new Piece(0,true);
                        }
                    }
                } else if (selectedpiece.isKing) {
                    King a = (King) selectedpiece;
                    a.moveKing(pos[0],pos[1],tempboard);
                }  
                //if the pieces that moved lead to a check then the move is not allowed
                if(!KingInCheck(tempboard)){
                    return true;
                }
                return false;
        }
    }