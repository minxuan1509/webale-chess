package webale;

import java.awt.event.*;

import javax.swing.JLabel;
import javax.swing.JOptionPane;


public class GameController {
    BoardFrame boardFrame = null;
    HomeFrame homeFrame = null;
    
    public GameController(BoardFrame boardFrame){
        this.boardFrame = boardFrame;
        setListener();
    }

    public GameController(HomeFrame homeFrame){
        this.homeFrame = homeFrame;
        setListener();
    }

    public GameController(HomeFrame homeFrame, BoardFrame boardFrame){
        this.homeFrame = homeFrame;
        this.boardFrame = boardFrame;
        setListener();
    }

    public void setListener(){
        
        homeFrame.getStartButton().addMouseListener(startBtnListener);
        homeFrame.getInstructionButton().addMouseListener(instructionBtnListener);
        homeFrame.getQuitButton().addMouseListener(quitBtnListener);

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 7; x++) {
                boardFrame.getGameBoard().getTileArray()[y][x].addActionListener(chessTileListener);
            }
        }
        
    }

    MouseListener startBtnListener = new MouseAdapter(){
        @Override
        public void mouseClicked(MouseEvent e){
            if(e.getButton()==MouseEvent.BUTTON1){
                new BoardFrame();
            }
        }
    };

    MouseListener instructionBtnListener =  new MouseAdapter(){
        @Override
        public void mouseClicked(MouseEvent e){
            if(e.getButton()==MouseEvent.BUTTON1){
                JLabel instructionLabel = new JLabel(homeFrame.getInstructionImageIcon());
                JOptionPane.showMessageDialog(null, instructionLabel, "Instruction", JOptionPane.PLAIN_MESSAGE, null);
            }
        }
    };

    MouseListener quitBtnListener = new MouseAdapter(){
        @Override
        public void mouseClicked(MouseEvent e){
            if(e.getButton()==MouseEvent.BUTTON1){
                System.exit(0);
            }
        }
    };

    ActionListener chessTileListener = new ActionListener(){
        int timeClicked = 0;
        boolean isValidMove = false;

        @Override
        public void actionPerformed(ActionEvent e){
            System.out.println(timeClicked);
            if(boardFrame.getGameBoard() != null){
                timeClicked++;
                isValidMove = movePiece((ChessTiles)e.getSource(), timeClicked);
                //if chesstile clicked for startpoint is empty
                if(!isValidMove && timeClicked == 1){
                    timeClicked = 0;
                }

                //if chess movement to endpoint is valid
                else if (timeClicked == 2) {
                    timeClicked = 0;
                    if(isValidMove){
                        boardFrame.getGameBoard().rotateBoard();
                        togglePlayerTurn();
                    }
                    
                }
            }
        }
    };

    Coordinate startPoint = null;
    Coordinate endPoint = null;
    boolean isRedPlayer = true;

    public boolean movePiece(ChessTiles chessTileClicked, int timeClicked){
        Coordinate[][] coordinate = boardFrame.getGameBoard().getCoordinateArray();
        
        //check if the chesstile selected as startPoint is valid first
        if(timeClicked % 2 != 0){
            //if chesstile clicked as startPoint is empty
            if(coordinate[chessTileClicked.getCoorY()][chessTileClicked.getCoorX()].getChessPiece() == null)
                return false;
            //if piece on chesstile clicked as startPoint is not the same colour as the player's piece
            else if(coordinate[chessTileClicked.getCoorY()][chessTileClicked.getCoorX()].getChessPiece().getIsRedColor() != isRedPlayer){
                return false;
            }
        }

        if(timeClicked % 2 == 0){
            endPoint = coordinate[chessTileClicked.getCoorY()][chessTileClicked.getCoorX()];
            //if moving to endPoint is valid
            if(startPoint != null && startPoint.getChessPiece().canMove(coordinate, startPoint, endPoint)){
                endPoint.setChessPiece(startPoint.getChessPiece());
                startPoint.setChessPiece(null);
                boardFrame.getGameBoard().revalidate();
                boardFrame.getGameBoard().repaint();
                //if successfully moved return true, if not return false
                return true;
            } else{
                return false;
            }
        }
        else {
            startPoint = coordinate[chessTileClicked.getCoorY()][chessTileClicked.getCoorX()];
            return true;
        }
    }

    public void togglePlayerTurn(){
        isRedPlayer = !isRedPlayer;
        if(isRedPlayer){
            boardFrame.getToolbar().setPlayerToMove("Red");
            boardFrame.getToolbar().repaint();
        } else{
            boardFrame.getToolbar().setPlayerToMove("Blue");
            boardFrame.getToolbar().repaint();
        }

    }

}