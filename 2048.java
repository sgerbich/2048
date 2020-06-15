package com.codegym.games.game2048;
import com.codegym.engine.cell.*;

public class Game2048 extends Game {
    private static final int SIDE =4;
    private int[][] gameField= new int[SIDE][SIDE];
    int maxTile;
    private boolean isGameStopped = false;
    private int score;
    
    @Override
    public void initialize(){
        
        setScreenSize(SIDE, SIDE);
        createGame();
        drawScene();
    }
    
    private void createGame(){
        score =0;
        setScore(score);
        for(int i=0;i<SIDE;i++){
            for(int j=0;j<SIDE;j++){
                gameField[i][j]=0;
            }
        }
        createNewNumber();
        createNewNumber();
    }
    
    private void drawScene(){
        
        for(int i=0;i<SIDE;i++){
           for(int j=0;j<SIDE;j++){
            setCellColoredNumber(i,j,gameField[j][i]);
            } 
        }
        
    }
    
    private void createNewNumber(){
        int x;
        int y;
       do {
        x = getRandomNumber(SIDE);     
        y = getRandomNumber(SIDE);
        } while(gameField[x][y] != 0);
        if(getRandomNumber(10)==9 && gameField[x][y]==0){
           gameField[x][y]= 4;
        }else if(gameField[x][y]==0){
            gameField[x][y]= 2;
        }
        if (getMaxTileValue()==2048){
            win();
        }
    }
    
    private Color getColorByValue(int val){
        switch(val){
            case 0: return Color.WHITE;
            case 2: return Color.BLUE;
            case 4: return Color.RED;
            case 8: return Color.GREEN;
            case 16: return Color.CYAN;
            case 32: return Color.GRAY;
            case 64: return Color.ORANGE;
            case 128: return Color.PINK;
            case 256: return Color.BROWN;
            case 512: return Color.MAGENTA;
            case 1024: return Color.PURPLE;
            case 2048: return Color.GOLD;
            default: return Color.WHITE;
            
        }
    }
    
    private void setCellColoredNumber(int x, int y, int val){
        
       if(val!=0){
            setCellValueEx(x,y,getColorByValue(val),Integer.toString(val));
        }
        else{
            setCellValueEx(x,y,getColorByValue(val), "");
        }
    }
    
    private boolean compressRow(int[] row){
        boolean isChanged = false;
        int temp = 0;
        int[] tempArr= row.clone();
        for (int i = 0;i<tempArr.length;i++){
            for(int j = 0; j < tempArr.length-i-1; j++){
               if(tempArr[j] == 0) {
                    temp = tempArr[j];
                    tempArr[j] = tempArr[j+1];
                    tempArr[j+1] = temp;
                    
                }
        }
    }
     for(int i=0;i<row.length;i++){
         if(tempArr[i]!=row[i]){
             row[i]= tempArr[i];
             isChanged=true;
         }
     }
     
     return isChanged;
        
    }
    
    private boolean mergeRow(int[] row){
        boolean moved = false;
         for (int i=0; i< row.length-1;i++)
             if ((row[i] == row[i+1]) && (row[i]!=0)){
            row[i] = 2*row[i];
            row[i+1] = 0;
            moved = true;
            score+=row[i];
            setScore(score);
        }

    return moved;
    }
    
    @Override
    public void onKeyPress(Key key){
        if(canUserMove()==false){
            gameOver();
            
        }else if(isGameStopped=true){
            if(key==Key.SPACE){
                isGameStopped=false;
                
                createGame();
                drawScene();
            }
        }else{
        if (key==Key.LEFT){
            moveLeft();
            drawScene();
        }else if(key==Key.RIGHT){
            moveRight();
            drawScene();
        }else if(key==Key.UP){
            moveUp();
            drawScene();
        }else if(key==Key.DOWN){
            moveDown();
            drawScene();
        }
    }
    }
    
    private void moveLeft() {
        boolean isChanged = false;
        for (int i = 0; i < SIDE; i++) {
            if (compressRow(gameField[i])) {
                isChanged = true;
            }
            if (mergeRow(gameField[i])) {
                isChanged = true;
            }
            if (compressRow(gameField[i])) {
                isChanged = true;
            }
        } 
        if (isChanged) {
            createNewNumber();
        }
    }
    
    private void moveRight(){
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
    }
    
    private void moveUp(){
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
    }
    
    private void moveDown(){
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
    }
    
    private void rotateClockwise(){
        for (int i = 0; i < SIDE / 2; i++)
        {
            for (int j = i; j < SIDE - i - 1; j++)
            {
                int temp = gameField[i][j];
                gameField[i][j] = gameField[SIDE - 1 - j][i];
                gameField[SIDE - 1 - j][i] = gameField[SIDE - 1 - i][SIDE - 1 - j];
                gameField[SIDE - 1 - i][SIDE - 1 - j] = gameField[j][SIDE - 1 - i];
                gameField[j][SIDE - 1 - i] = temp;
            }
        }
    }
    
    private int getMaxTileValue(){
        
        for(int i=0;i<SIDE;i++){
           for(int j=0;j<SIDE;j++){
            if(gameField[i][j]> maxTile){
                maxTile= gameField[i][j];
             }
            } 
        }
        return maxTile;
    }
    
    private void win(){
        isGameStopped=true;
         showMessageDialog(Color.BLACK, "Winner!", Color.BLUE, 100);
    }
    
    private boolean canUserMove(){
        boolean canMove = false;
        
        for(int i=0;i<SIDE;i++){
           for(int j=0;j<SIDE;j++){
            
            if(gameField[i][j]==0){
               canMove=true; 
             }
             
             if (i < SIDE - 1) {
                    if (gameField[i][j] == gameField[i + 1][j]) {
                        canMove = true;
                    }
                }
                if (j < SIDE - 1) {
                    if (gameField[i][j] == gameField[i][j + 1]) {
                        canMove = true;
                    }
                }
            } 
        }
        
        return canMove;
    }
    
    private void gameOver(){
        isGameStopped = true;
        showMessageDialog(Color.RED, "Sorry you lose!", Color.BLUE, 100);
    }
}