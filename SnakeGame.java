import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;
import java.awt.*;
import java.awt.event.*;

public class SnakeGame  extends JPanel implements ActionListener,KeyListener{
    private class Title {
        int x;
        int y;

        Title(int x,int y){
            this.x=x;
            this.y=y;
        }
        
    }
    int boardHeight;
    int boardWidth;
    int titleSize=25;
    //snake
    Title snakeHead;
    ArrayList<Title> snakebody;
    //food
    Title food;
    Random random;

    //logic
    Timer gameloop;
    int velocityx;
    int velocityy;
    boolean gameover=false;

    SnakeGame(int boardHeight,int boardWidth){
        this.boardWidth=boardWidth;
        this.boardHeight=boardHeight;
        setPreferredSize(new Dimension(this.boardHeight,this.boardWidth));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

        snakeHead=new Title(5,5);
        snakebody=new ArrayList<Title>();

        food=new Title(10, 10);

        random =new Random();
        placeFood();

        velocityx=0;
        velocityy=0;

        gameloop =new Timer(100, this);
        gameloop.start();
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){

        for(int i=0;i<boardWidth/titleSize;i++){
            g.drawLine(i*titleSize, 0, i*titleSize, boardHeight);//vertical 
            g.drawLine(0, i*titleSize, boardWidth, i*titleSize);//horizondal
        }

        //food
        g.setColor(Color.red);
        g.fillRect(food.x*titleSize,food.y*titleSize, titleSize, titleSize);

        //snake
        g.setColor(Color.green);
        g.fillRect(snakeHead.x*titleSize, snakeHead.y*titleSize, titleSize, titleSize);

        //body
        for(int i=0;i<snakebody.size();i++){
            Title snakePart=snakebody.get(i);
            g.fillRect(snakePart.x *titleSize,snakePart.y * titleSize, titleSize, titleSize);
        }

        g.setFont(new Font("Arial",Font.PLAIN,16));
        if(gameover){
            g.setColor(Color.red);
            g.drawString("Game over: "+String.valueOf(snakebody.size()),titleSize-16,titleSize);
        }else{
            g.drawString("Score: "+String.valueOf(snakebody.size()),titleSize-16,titleSize);
        }

    }
    public void placeFood(){
        food.x= random.nextInt(boardWidth/titleSize);//600/25=24
        food.y=random.nextInt(boardHeight/titleSize);
    }
    public boolean collision(Title title1,Title title2){
        return title1.x ==title2.x && title1.y == title2.y;
    }
    public void move(){
        if(collision(snakeHead, food)){
            snakebody.add(new Title(food.x,food.y));
            placeFood();
        }

        //snake body
        for(int i=snakebody.size()-1;i>=0;i--){
            Title snakePart =snakebody.get(i);
            if(i == 0){
                snakePart.x=snakeHead.x;
                snakePart.y=snakeHead.y;
            }else{
                Title prevSnakePart =snakebody.get(i-1);
                snakePart.x=prevSnakePart.x;
                snakePart.y=prevSnakePart.y;
            }
        }

        snakeHead.x+=velocityx;
        snakeHead.y+=velocityy;
        for(int i=0;i<snakebody.size();i++){
            Title snakePart=snakebody.get(i);

            if(collision(snakeHead,snakePart)){
                gameover=true;
            }
        }
        if(snakeHead.x*titleSize <0 || snakeHead.x*titleSize > boardWidth || snakeHead.y*titleSize < 0 ||snakeHead.y*titleSize > boardHeight){
            gameover=true;
        }
    }
    public void actionPerformed(ActionEvent e){
        move();
        repaint();
        if(gameover){
            gameloop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_UP&& velocityy != 1){
            velocityx=0;
            velocityy=-1;
        }else if(e.getKeyCode() == KeyEvent.VK_DOWN && velocityy != -1){
            velocityx=0;
            velocityy=1;
        }else if(e.getKeyCode() == KeyEvent.VK_LEFT && velocityx !=1 ){
            velocityx=-1;
            velocityy=0;
        }else if(e.getKeyCode() == KeyEvent.VK_RIGHT && velocityx !=-1){
            velocityx=1;
            velocityy=0;
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {}
    
    @Override
    public void keyReleased(KeyEvent e) {}
}
