
package frogger;

import java.io.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;
 
public class Frogger extends JFrame implements Runnable {

    static Window w = new Window();
    boolean animateFirstTime = true;
    Image image;
    Graphics2D g;

    Frog frog;
    Log LogRow3;
    Log LogRow2;
    Log LogRow1;
    Log SpecialLog;
    
    int numCarsRow3 = 3;
    Car carRow3[] = new Car[numCarsRow3];
    int numCarsRow2 = 2;
    Car carRow2[] = new Car[numCarsRow3];
    int numCarsRow1 = 2;
    Car carRow1[] = new Car[numCarsRow3];
     
    boolean gameOver;
    
    Image frogImage;
   
    int score;
    int highScore;
    int logTime;
    int logTime2;

    
    int roadLength;
    int roadTop;
   
    int riverLength;
    int riverTop;
    int timeCount;
    
    
    boolean youWin;
    
    static Frogger frame1;

    public static void main(String[] args) {
        frame1 = new Frogger();
        frame1.setSize(w.WINDOW_WIDTH, w.WINDOW_HEIGHT);
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame1.setVisible(true);
    }

    public Frogger() {

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.BUTTON1 == e.getButton()) {
                    //left button
                }
                if (e.BUTTON3 == e.getButton()) {
                    //right button
                    reset();
                }
                repaint();
            }
        });

    addMouseMotionListener(new MouseMotionAdapter() {
      public void mouseDragged(MouseEvent e) {
        repaint();
      }
    });

    addMouseMotionListener(new MouseMotionAdapter() {
      public void mouseMoved(MouseEvent e) {
        repaint();
      }
    });

        addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                 
        if(gameOver)
            return;
        if(youWin)
            return;
    
                if (e.VK_RIGHT == e.getKeyCode())
                {
                    frog.moveRight();
                }
                if (e.VK_LEFT == e.getKeyCode())
                {
                    frog.moveLeft();
                }
                if (e.VK_UP == e.getKeyCode())
                {
                    frog.moveUp();
                }
                if (e.VK_DOWN == e.getKeyCode())
                {
                    frog.moveDown();
                }
         
                repaint();
            }
        });
        init();
        start();
    }




    Thread relaxer;
////////////////////////////////////////////////////////////////////////////
    public void init() {
        requestFocus();
    }
////////////////////////////////////////////////////////////////////////////
    public void destroy() {
    }
////////////////////////////////////////////////////////////////////////////
    public void paint(Graphics gOld) {
        if (image == null || w.xsize != getSize().width || w.ysize != getSize().height) {
            w.xsize = getSize().width;
            w.ysize = getSize().height;
            image = createImage(w.xsize, w.ysize);
            g = (Graphics2D) image.getGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
        }

//fill background
        g.setColor(Color.cyan);

        g.fillRect(0, 0, w.xsize, w.ysize);

        int x[] = {w.getX(0), w.getX(w.getWidth2()), w.getX(w.getWidth2()), w.getX(0), w.getX(0)};
        int y[] = {w.getY(0), w.getY(0), w.getY(w.getHeight2()), w.getY(w.getHeight2()), w.getY(0)};
//fill border
        g.setColor(Color.white);
        g.fillPolygon(x, y, 4);
// draw border
        g.setColor(Color.red);
        g.drawPolyline(x, y, 5);

        if (animateFirstTime) {
            gOld.drawImage(image, 0, 0, null);
            return;
        }
       
       drawRiver();
       int width = image.getWidth(this);
       int height = image.getWidth(this);
       g.setColor(Color.gray);
       LogRow3.draw(g);
       LogRow2.draw(g);
       LogRow1.draw(g);
       g.setColor(Color.pink);
      if(SpecialLog.getVisible())
      {
       SpecialLog.draw(g);
      }
       drawRoad();
       frog.draw(g,w,frogImage,width,height,this);
      
      for(int index = 0;index < numCarsRow3; index++)
       {
        g.setColor(carRow3[index].color);
           carRow3[index].draw(g);
       }
      for(int index = 0;index < numCarsRow2; index++)
       {
        carRow2[index].draw(g);
        g.setColor(carRow2[index].color);
    
       }
      for(int index = 0;index < numCarsRow1; index++)
       {
        carRow1[index].draw(g);
        g.setColor(carRow1[index].color);
    
       }
      
      g.setColor(Color.black);
      g.setFont(new Font("Comic Sans MS",Font.PLAIN,30));
      g.drawString("Score: " + score, 10, 60);
      g.drawString("High score: " + highScore, 180, 60);
      
      if (gameOver)
        {
            g.setColor(Color.black);
            g.setFont(new Font("Comic Sans MS",Font.PLAIN,30));
            g.drawString("WASTED",w.getWidth2()/2-(w.getWidth2()/4),w.getYNormal(riverTop));
        } 
      
      if(youWin)
      {
          g.setColor(Color.black);
          g.setFont(new Font("Comic Sans MS",Font.PLAIN,30));
          g.drawString("You Win",w.getWidth2()/2-(w.getWidth2()/4),w.getYNormal(riverTop));
      }
      
       g.drawString("" + SpecialLog.getTimeVal(),w.getX(SpecialLog.getXPos()),w.getYNormal(SpecialLog.getYPos()));    
       g.drawString("" + LogRow3.getTimeVal(),w.getX(LogRow3.getXPos()),w.getYNormal(LogRow3.getYPos()));    
       g.drawString("" + LogRow2.getTimeVal(),w.getX(LogRow2.getXPos()),w.getYNormal(LogRow2.getYPos()));    
       g.drawString("" + LogRow1.getTimeVal(),w.getX(LogRow1.getXPos()),w.getYNormal(LogRow1.getYPos()));    
       
       
       
       
       
       
       
       
       
      
        gOld.drawImage(image, 0, 0, null);
    }

////////////////////////////////////////////////////////////////////////////
    public void drawRoad()
    {
        g.setColor(Color.black);
        g.fillRect(w.getX(0),w.getYNormal(roadTop),w.getWidth2(),roadLength);
        

        for(int index = 0;index<w.getWidth2();index+=25)
       {
        g.setColor(Color.yellow);
        g.fillRect(w.getX(index),w.getYNormal(roadTop - 1*roadLength/3) ,10 ,3);
        g.fillRect(w.getX(index),w.getYNormal(roadTop - 2*roadLength/3) ,10 ,3);
       }
    }
////////////////////////////////////////////////////////////////////////////
    public void drawRiver()
    {
        g.setColor(Color.blue);
        g.fillRect(w.getX(0),w.getYNormal(riverTop),w.getWidth2(),riverLength);
    }    
      
////////////////////////////////////////////////////////////////////////////
// needed for     implement runnable
    public void run() {
        while (true) {
            animate();
            repaint();
            double seconds = 0.04;    //time that 1 frame takes.
            int miliseconds = (int) (1000.0 * seconds);
            try {
                Thread.sleep(miliseconds);
            } catch (InterruptedException e) {
            }
        }
    }
/////////////////////////////////////////////////////////////////////////
    public void reset() {
        
        logTime2 = 100;
        logTime = (int) (Math.random()*5+5);
        timeCount = 0;
        score = 0;
        youWin = false;
        riverLength = 200;
        riverTop = 350;
        roadLength = 100;
        roadTop = 120;
      
        SpecialLog = new Log(w,1);
        LogRow3 = new Log(w,1);
        LogRow2 = new Log(w,-1);
        LogRow1 = new Log(w,1);
          
        SpecialLog.setYPos(riverTop - (1*riverLength/6)); 
        SpecialLog.setXPos(w.getX(100));
        SpecialLog.setHeight(riverLength/3);
        SpecialLog.setWidth(riverLength/3);
        SpecialLog.setVisible(true);
        
        LogRow3.setYPos(riverTop - (1*riverLength/6)); 
        LogRow3.setXPos(w.getX(300));
        LogRow3.setHeight(riverLength/3);
        LogRow3.setWidth(riverLength/3);
        
        LogRow2.setYPos(riverTop - (1*riverLength/2)); 
        LogRow2.setXPos(w.getX(50));
        LogRow2.setHeight(riverLength/3);
        LogRow2.setWidth(riverLength/3);

        LogRow1.setYPos(riverTop - (5*riverLength/6)); 
        LogRow1.setXPos(w.getX(150));
        LogRow1.setHeight(riverLength/3);
        LogRow1.setWidth(riverLength/3);
      

        
      
        
        
        gameOver = false;
        frog = new Frog(w,6);
        
     for (int index=0;index<numCarsRow3;index++)       
       {
        carRow3[index] = new Car(w,6);
        carRow3[index].setYPos(roadTop - (1*roadLength/6)); 
        carRow3[index].setXPos(w.getX(140*index-50));
        carRow3[index].setHeight(roadLength/5);
       
        int zred = (int) (Math.random()* 255);
        int zgreen = (int) (Math.random()* 255);
        int zblue = (int) (Math.random()* 255); 
        Color newColor = new Color(zred,zgreen,zblue);
        carRow3[index].color = newColor;
       }
     
    
     for (int index=0;index<numCarsRow2;index++)       
       {
        carRow2[index] = new Car(w,-10);
        carRow2[index].setYPos(roadTop - (roadLength/2)); 
        carRow2[index].setXPos(w.getX(140*index-50));
        carRow2[index].setHeight(roadLength/5);
       
        int zred = (int) (Math.random()* 255);
        int zgreen = (int) (Math.random()* 255);
        int zblue = (int) (Math.random()* 255);
        Color newColor = new Color(zred,zgreen,zblue);
        carRow2[index].color = newColor;
       }
      
        
     for (int index=0;index<numCarsRow1;index++)       
       {
        carRow1[index] = new Car(w,3);
        carRow1[index].setYPos(roadTop - (16*roadLength/19)); 
        carRow1[index].setXPos(w.getX(140*index-50));
        carRow1[index].setHeight(roadLength/5);
        int zred = (int) (Math.random()* 255);
        int zgreen = (int) (Math.random()* 255);
        int zblue = (int) (Math.random()* 255);
        
        Color newColor = new Color(zred,zgreen,zblue);
        carRow1[index].color = newColor;
       }
      
       
    }

/////////////////////////////////////////////////////////////////////////
    public void animate() {

        if (animateFirstTime) {
            animateFirstTime = false;
            if (w.xsize != getSize().width || w.ysize != getSize().height) {
                w.xsize = getSize().width;
                w.ysize = getSize().height;
            }
            reset();
            frogImage=Toolkit.getDefaultToolkit().getImage("./frog.GIF");
        }
      
        if(gameOver)
            return;
        if(youWin)
            return;
    
        
        for(int index = 0;index < numCarsRow3; index++)
        {
        if(carRow3[index].getXPos()> w.getWidth2()+50)
        {
           carRow3[index].setXPos(-50);
        }
           carRow3[index].moveRight();
        } 
  
        for(int index = 0;index < numCarsRow2; index++)
        {
        if(carRow2[index].getXPos()< w.getX(0)-50)
        {
           carRow2[index].setXPos(w.getWidth2()+50);
        }
           carRow2[index].moveRight();
        } 
    
         for(int index = 0;index < numCarsRow1; index++)
        {
        if(carRow1[index].getXPos()> w.getWidth2()+50)
        {
           carRow1[index].setXPos(-50);
        }
           carRow1[index].moveRight();
        } 
    
    for(int index = 0;index<numCarsRow3;index++)
    {
         if(frog.intersect(carRow3[index]))
         {
             gameOver = true;
         }
    }    
    for(int index = 0;index<numCarsRow2;index++)
    {
         if(frog.intersect(carRow2[index]))
         {
             gameOver = true;
         }
    
    } 
    for(int index = 0;index<numCarsRow1;index++)
    {
         if(frog.intersect(carRow1[index]))
         {
             gameOver = true;
         }
    
    } 

    if(frog.getXPos()+frog.getWidth()/2<w.getX(0))
    {
        gameOver = true;
    }
    else if(frog.getXPos()-frog.getWidth()/2>w.getWidth2())
    {
        gameOver = true;
    }
    else if(frog.getYPos()<riverTop-frog.getHeight()/2&&frog.getYPos()>w.getY(riverTop)-w.getY(riverLength)+frog.getHeight()/2 
         && !frog.intersect(LogRow3) && !frog.intersect(LogRow2) && !frog.intersect(LogRow1) && !frog.intersect(SpecialLog))
        {
        gameOver = true;
        }
  
    LogRow3.moveRight();
    LogRow2.moveRight();
    LogRow1.moveRight();
    SpecialLog.moveRight();
    
    if(frog.intersect(SpecialLog) && SpecialLog.getVisible() == true)
       frog.setMoveRight(SpecialLog.getSpeed());
    
    if(SpecialLog.getXPos()> w.getWidth2()+50)
        {
           SpecialLog.setXPos(w.getX(-50));
        }
     else if(SpecialLog.getXPos()< w.getX(0)-50)
        {
           SpecialLog.setXPos(w.getWidth2()+50);
        }
    
     if(LogRow3.getXPos()> w.getWidth2()+50)
        {
           LogRow3.setXPos(w.getX(-50));
        }
     else if(LogRow3.getXPos()< w.getX(0)-50)
        {
           LogRow3.setXPos(w.getWidth2()+50);
        }
     
      if(LogRow2.getXPos()> w.getWidth2()+50)
        {
           LogRow2.setXPos(-50);
        }
     else if(LogRow2.getXPos()< w.getX(0)-50)
        {
           LogRow2.setXPos(w.getWidth2()+50);
        }
      
       if(LogRow1.getXPos()> w.getWidth2()+50)
        {
           LogRow1.setXPos(w.getX(-50));
        }
     else if(LogRow1.getXPos()< w.getX(0)-50)
        {
           LogRow1.setXPos(w.getWidth2()+50);
        }
       
       if(frog.intersect(LogRow3))
       {
        frog.setMoveRight(LogRow3.getSpeed());
       }
       else if(frog.intersect(LogRow2))
       {
        frog.setMoveRight(LogRow2.getSpeed());  
       } 
       else if(frog.intersect(LogRow1))
       {
          frog.setMoveRight(LogRow1.getSpeed());   
       }

       
       if(frog.getYPos()> riverTop + frog.getHeight())
       {
           score+=1;
           frog.setYPos(10);
       }
       
       if(score > highScore)
       {
           highScore = score;
       }
      if(frog.intersect(SpecialLog)) 
      {
          frog.setSpeed(13);
      }
      
      
     if(timeCount % 25 == 24)
     {
        if(logTime>=2)
        logTime--;
        else 
        {
        logTime = (int) (Math.random()*5+5);
        SpecialLog.setXPos(w.getWidth2()+50);
        }
     }
           
       
      if(frog.intersect(SpecialLog) && SpecialLog.getVisible() == false)
       {
           gameOver = true;
       }
  
  SpecialLog.decrementTime();
  LogRow3.decrementTime();
  LogRow2.decrementTime();
  LogRow1.decrementTime();
  Log.addTimeCount(); 
}

////////////////////////////////////////////////////////////////////////////
    public void start() {
        if (relaxer == null) {
            relaxer = new Thread(this);
            relaxer.start();
        }
    }
////////////////////////////////////////////////////////////////////////////
    public void stop() {
        if (relaxer.isAlive()) {
            relaxer.stop();
        }
        relaxer = null;
    }
}


class Window {
    int xsize = -1;
    int ysize = -1;
    
    static final int WINDOW_WIDTH = 412;
    static final int WINDOW_HEIGHT = 451;

    final int TOP_BORDER = 40;
    final int SIDE_BORDER = 8;
    final int BOTTOM_BORDER = 8;
    final int YTITLE = 25;
    
    public int getX(int x) {
        return (x+SIDE_BORDER);
    }
    public int getY(int y) {
        return (y + TOP_BORDER + YTITLE);
    }
    
    public int getYNormal(int y) {
        return (-y + TOP_BORDER + YTITLE + getHeight2());
    }
        
    public int getWidth2() {
        return (xsize - SIDE_BORDER*2);
    }
    public int getHeight2() {
        return (ysize - (TOP_BORDER + YTITLE) - BOTTOM_BORDER);
    }    
}


class Frog {
    private int xpos;
    private int ypos;
    private int speed;
    private int width;
    private int height;
    private Window w;
   
    Frog(Window _w,int _speed)
    {
        w = _w;
        speed = _speed;
        
    xpos = w.getWidth2()/2;
    ypos = 10;
    width = 20;
    height =  20;
    }
    int getHeight()
    {
     return height;   
    }
    int getWidth()
    {
     return width;   
    }
    void setSpeed(int _speed)
    {
      speed = _speed;   
    }
      void setXPos(int _xpos)
    {
      xpos = _xpos;   
    }
       void setYPos(int _ypos)
    {
      ypos = _ypos;   
    }
    int getXPos()
    {
     return xpos;   
    }
    int getYPos()
    {
     return ypos;     
    }
   void setMoveRight(int _speed)
    {
        xpos+=_speed;
    }
    void moveRight()
    {
        xpos+=speed;
    }
    void moveLeft()
    {
        xpos-=speed;
    }
    void moveUp()
    {
         ypos+=speed;
    }
    void moveDown()
    {
         ypos-=speed;
    }
    
    public void draw(Graphics2D g,Window w , Image image,
       int width,int height, Frogger obj)
    {
       
        g.translate(w.getX(xpos),w.getYNormal(ypos));
        g.scale(.05,.05);
        g.drawImage(image, -width/2, -height/2,width,height,obj);
        g.scale(20,20);
        g.translate(-w.getX(xpos),-w.getYNormal(ypos));
    }
    public boolean intersect(Car car)
    {
//lower left car vertex  
        int carXPos = car.getXPos() - car.getWidth()/2;
        int carYPos = car.getYPos() - car.getHeight()/2;
        if(carXPos >= xpos-width/2 &&
         carXPos <= xpos+width/2 &&
         carYPos <= ypos+height/2 &&
         carYPos >= ypos-height/2)
             return true;
        
//upper right car vertex
         carXPos = car.getXPos() + car.getWidth()/2;
         carYPos = car.getYPos() + car.getHeight()/2;
     
      if(carXPos >= xpos-width/2 &&
         carXPos <= xpos+width/2 &&
         carYPos <= ypos+height/2 &&
         carYPos >= ypos-height/2 )
           return true;
        
 //lower right car vertex  
         carXPos = car.getXPos() + car.getWidth()/2;
         carYPos = car.getYPos() - car.getHeight()/2;
        if(carXPos >= xpos-width/2 &&
         carXPos <= xpos+width/2 &&
         carYPos <= ypos+height/2 &&
         carYPos >= ypos-height/2)
             return true;
        
//upper left car vertex
         carXPos = car.getXPos() - car.getWidth()/2;
         carYPos = car.getYPos() + car.getHeight()/2;
     
      if(carXPos >= xpos-width/2 &&
         carXPos <= xpos+width/2 &&
         carYPos <= ypos+height/2 &&
         carYPos >= ypos-height/2 )
           return true;     
      
 //frog     
      
//lower left car vertex  
        int frogXPos = getXPos() - getWidth()/2;
        int frogYPos = getYPos() - getHeight()/2;
        if(frogXPos >= car.getXPos()-car.getWidth()/2 &&
         frogXPos <= car.getXPos()+car.getWidth()/2 &&
         frogYPos <= car.getYPos()+car.getHeight()/2 &&
         frogYPos >= car.getYPos()-car.getHeight()/2)
             return true;
        
//upper right car vertex
         frogXPos = getXPos() + getWidth()/2;
         frogYPos = getYPos() + getHeight()/2;
        if(frogXPos >= car.getXPos()-car.getWidth()/2 &&
         frogXPos <= car.getXPos()+car.getWidth()/2 &&
         frogYPos <= car.getYPos()+car.getHeight()/2 &&
         frogYPos >= car.getYPos()-car.getHeight()/2)
             return true;
        
 //lower right car vertex  
         frogXPos = getXPos() + getWidth()/2;
         frogYPos = getYPos() - getHeight()/2;
        if(frogXPos >= car.getXPos()-car.getWidth()/2 &&
         frogXPos <= car.getXPos()+car.getWidth()/2 &&
         frogYPos <= car.getYPos()+car.getHeight()/2 &&
         frogYPos >= car.getYPos()-car.getHeight()/2)
             return true;
        
//upper left car vertex
         frogXPos = getXPos() - getWidth()/2;
         frogYPos = getYPos() + getHeight()/2;
        if(frogXPos >= car.getXPos()-car.getWidth()/2 &&
         frogXPos <= car.getXPos()+car.getWidth()/2 &&
         frogYPos <= car.getYPos()+car.getHeight()/2 &&
         frogYPos >= car.getYPos()-car.getHeight()/2)
             return true;     
      
      
      
      
        return(false); 
    }
        public boolean intersect(Log log)
    {
//lower left car vertex  
        int logXPos = log.getXPos() - log.getWidth()/2;
        int logYPos = log.getYPos() - log.getHeight()/2;
        if(logXPos >= xpos-width/2 &&
         logXPos <= xpos+width/2 &&
         logYPos <= ypos+height/2 &&
         logYPos >= ypos-height/2)
             return true;
        
//upper right car vertex
         logXPos = log.getXPos() + log.getWidth()/2;
         logYPos = log.getYPos() + log.getHeight()/2;
     
      if(logXPos >= xpos-width/2 &&
         logXPos <= xpos+width/2 &&
         logYPos <= ypos+height/2 &&
         logYPos >= ypos-height/2 )
           return true;
        
 //lower right car vertex  
         logXPos = log.getXPos() + log.getWidth()/2;
         logYPos = log.getYPos() - log.getHeight()/2;
        if(logXPos >= xpos-width/2 &&
         logXPos <= xpos+width/2 &&
         logYPos <= ypos+height/2 &&
         logYPos >= ypos-height/2)
             return true;
        
//upper left car vertex
         logXPos = log.getXPos() - log.getWidth()/2;
         logYPos = log.getYPos() + log.getHeight()/2;
     
      if(logXPos >= xpos-width/2 &&
         logXPos <= xpos+width/2 &&
         logYPos <= ypos+height/2 &&
         logYPos >= ypos-height/2 )
           return true;     
      
 //frog checks if its inbetween the logs  /////////////////////////////////////////////////////////////////////////////////////////////    
      
//lower left car vertex  
        int frogXPos = getXPos() - getWidth()/2;
        int frogYPos = getYPos() - getHeight()/2;
        if(frogXPos >= log.getXPos()-log.getWidth()/2 &&
         frogXPos <= log.getXPos()+log.getWidth()/2 &&
         frogYPos <= log.getYPos()+log.getHeight()/2 &&
         frogYPos >= log.getYPos()-log.getHeight()/2)
             return true;
        
//upper right car vertex
         frogXPos = getXPos() + getWidth()/2;
         frogYPos = getYPos() + getHeight()/2;
        if(frogXPos >= log.getXPos()-log.getWidth()/2 &&
         frogXPos <= log.getXPos()+log.getWidth()/2 &&
         frogYPos <= log.getYPos()+log.getHeight()/2 &&
         frogYPos >= log.getYPos()-log.getHeight()/2)
             return true;
        
 //lower right car vertex  
         frogXPos = getXPos() + getWidth()/2;
         frogYPos = getYPos() - getHeight()/2;
        if(frogXPos >= log.getXPos()-log.getWidth()/2 &&
         frogXPos <= log.getXPos()+log.getWidth()/2 &&
         frogYPos <= log.getYPos()+log.getHeight()/2 &&
         frogYPos >= log.getYPos()-log.getHeight()/2)
             return true;
        
//upper left car vertex
         frogXPos = getXPos() - getWidth()/2;
         frogYPos = getYPos() + getHeight()/2;
        if(frogXPos >= log.getXPos()-log.getWidth()/2 &&
         frogXPos <= log.getXPos()+log.getWidth()/2 &&
         frogYPos <= log.getYPos()+log.getHeight()/2 &&
         frogYPos >= log.getYPos()-log.getHeight()/2)
             return true;     
      
      
      
      
        return(false); 
    }
    
    
}

class Car {
    
    private int xpos;
    private int ypos;
    private int speed;
    private int width;
    private int height;
    private Window w;
    Color color;
    
    Car (Window _w,int _speed)
    {
        w = _w;
        speed = _speed;
        
    xpos = -30;
    ypos = w.getHeight2()/2-60;
    width = 30;
    height = 20;
    }
    int getXPos()
    {
     return xpos;   
    }
     void setWidth(int _width)
    {
      width = _width;   
    }
    void setHeight(int _height)
    {
      height = _height;   
    }
    int getWidth()
    {
     return width;   
    }
      int getHeight()
    {
     return height;   
    }
    void setXPos(int _xpos)
    {
      xpos = _xpos;   
    }
    void setYPos(int _ypos)
    {
      ypos = _ypos;   
    }
    int getYPos()
    {
     return ypos;     
    }
    void moveRight()
    {
        xpos+=speed;
    }
    void moveLeft()
    {
        xpos-=speed;
    }
    public void draw(Graphics2D g)
    {
        g.translate(w.getX(xpos),w.getYNormal(ypos));
        g.fillRect(-width/2,-height/2,width,height);
        g.translate(-w.getX(xpos),-w.getYNormal(ypos));
    }
      
}

class Log {
    private int xpos;
    private int ypos;
    private int speed;
    private int width;
    private int height;
    private Window w;
    private boolean visible;
    Color color;
    private int timeVal;
    private static int timeCount;
    
    
    Log (Window _w,int _speed)
    {
     w = _w;
     speed = _speed;
        
     xpos = -30;
     ypos = w.getHeight2()/2-60;
     width = 30;
     height = 20;
     timeVal = (int)(Math.random()*10+5);
    }
    static void addTimeCount()
    {
       timeCount++; 
    }
    void decrementTime()
    {
       if(timeCount%25==24) 
       {
           if(timeVal>=1)
            timeVal--; 
           else if (timeVal == 0)
           {
               xpos = w.getX(-50);
               timeVal = (int)(Math.random()*10+5);
           }
       }
    }
     int getTimeVal()
    {
     return timeVal;   
    }
     boolean getVisible()
    {
     return visible;   
    }
      void setVisible(boolean  _visible)
    {
      visible = _visible;   
    }
    int getSpeed()
    {
     return speed;   
    }
    int getXPos()
    {
     return xpos;   
    }
     void setWidth(int _width)
    {
      width = _width;   
    }
    void setHeight(int _height)
    {
      height = _height;   
    }
    int getWidth()
    {
     return width;   
    }
      int getHeight()
    {
     return height;   
    }
    void setXPos(int _xpos)
    {
      xpos = _xpos;   
    }
    void setYPos(int _ypos)
    {
      ypos = _ypos;   
    }
    int getYPos()
    {
     return ypos;     
    }
    void moveRight()
    {
        xpos+=speed;
    }
    void moveLeft()
    {
        xpos-=speed;
    }
    public void draw(Graphics2D g)
    {
        g.translate(w.getX(xpos),w.getYNormal(ypos));
        g.fillRect(-width/2,-height/2,width,height);
        g.translate(-w.getX(xpos),-w.getYNormal(ypos));
    }
      
}

