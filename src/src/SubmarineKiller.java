
package src;

import java.awt.*;        
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JOptionPane;

/**
 * The SubmarineKiller Class implements a simple arcade game in which the user tries 
 * to blow up a submarine by dropping depth charges from a battleship.  The user moves
 * the battleship with the left and right arrow keys, and drops the depth charge with
 * the down arrow key.  The submarine moves erratically left and right along the 
 * bottom of the panel.  The number of hits and misses are shown at the top of the 
 * panel.  Also, in the main method this class is run as an application.
 * @author:  MAbdurrahman
 * @date:  October 25, 2014
 * @version: 1.0.0
 * @revised:  7 August 2016
 * @version: 1.0.1
 */
public class SubmarineKiller extends JPanel {
    //Instance Variables
    /*
     * The fonts of the game are font1 and font2, which respectively keeps the score and instructs
     * the user to click the frame to begin the game.
     */
    protected Font font1 = new Font("Bookman Old Sytle", Font.BOLD, 16);
    protected Font font2 = new Font("Bookman Old Sytle", Font.BOLD, 40);
    
    /** The timer drives this animation */
    private Timer timer;
    /*
     * The width and height of the panel is set the first time the paintComponent method is
     * is called.  The screenWidth and screenHeight is set in the main method, when the
     * default toolkit gets the screen dimension.  Changes in the size of the frame cannot
     * be altered.
     */
    private int screenWidth, screenHeight;
    /*
     * The Battleship, DepthCharge, Submarine, Waves, Sun, Cloud1, and Cloud2 classes are defined
     * with nested classes, which are described later in this class.  All these objects, except
     * the sun are animated in the game.
     */
    private Battleship battleship; 
    private DepthCharge depthCharge;      
    private Submarine submarine;      
    private Wave waves; 
    private Sun sun;         
    private CloudOne cloudOne;            
    private CloudTwo cloudTwo;
    /*
     * The hits are the number times the user hit the submarine, and the misses are the number of
     * times the user miss the submarine.  The numberOfCharges is set to 25 in the SubmarineKiller
     * Constructor.
     */
    private int hits;          
    private int misses;
    private final int numberOfCharges;

    /**
     * SubmarineKiller Constructor - Creates an instance of the SubmarineKiller.  That is, it
     * sets the background color of the panel, creates the timer, and adds a KeyListener,
     * FocusListener, and MouseListener to the panel.  These listeners, as well as the
     * ActionListener for the timer are defined by Anonymous Inner Classes.  The timer will 
     * run only when the panel has the input focus.
     */
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public SubmarineKiller() {
    	screenWidth = (int) getWidth();
    	screenHeight = (int) getHeight(); 
    	numberOfCharges = 25;
    	/*
    	 * actionListener - Responds to and defines the action taken each time the timer fires
    	 */
        @SuppressWarnings("Convert2Lambda")
        ActionListener actionListener = new ActionListener() {
        	/**
        	 * actionPerformed Method - Overrides the actionPerformed method of the ActionListener
                 * Interface, and redefines it for the action taken each time the timer fires, which
        	 * is updating the different objects of the game and repainting them.
        	 * @param ActionEvent - the action event of the timer firing
        	 */
                @Override
        	synchronized public void actionPerformed(ActionEvent ae) {
                    if (battleship != null) {
                        battleship.updateNewFrame();
                        depthCharge.updateNewFrame();
                        submarine.updateNewFrame();
                        waves.updateNewFrame();
                        cloudTwo.updateNewFrame();
                        sun.updateNewFrame();
                        cloudOne.updateNewFrame();
                    }
                    repaint();
                }
        };
        timer = new Timer(25, actionListener);//Fires every 25 milliseconds.
        /**
         * Anonymous MouseAdapter Class responds to the mouse event of the game.  It has only 
         * one method, mousePressed.
         */
        addMouseListener(new MouseAdapter() {
        	/**
        	 * mousePressed Method - Requests focus, stops the Battle Time Theme, begins
        	 * the submarine sonar, and ocean waves sounds
        	 * @param MouseEvent - the event of pressing the mouse
        	 */  
                @Override
        	synchronized public void mousePressed(MouseEvent me) {
                    requestFocus();
               
            }//end of the mousePressed Method
        } );//end of the Anonymous MouseAdapter Class
        /**
         * Anonymous FocusListener Class responds to the focus events of the game. It has two
         * methods, focusGained and focusLost.
         */
        addFocusListener(new FocusListener() {
            /**
             * focusGained Method - Starts the timer when the panel gains input focus and repaints
             * the panel.
             * @param FocusEvent - the event of gaining focus
             */
            @Override
            public void focusGained(FocusEvent fe) {
                timer.start();
                repaint();
            }//end of the focusGained Method
            /**
             * focusLost Method - Stops the timer when the panel looses input focus and repaints
             * the panel.
             * @param FocusEvent - the event of loosing focus
             */
            @Override
            public void focusLost(FocusEvent fe) {
                timer.stop();
                repaint();
            }//end of the focusLost Method
        });//end of the Anonymous FocusListener Class
        /**
         * Anonymous KeyAdapter Class responds to the key events of the game.  It has only one
         * method, keyPressed.
         */
        addKeyListener(new KeyAdapter() {
        	/**
        	 * keyPressed Method - Responds to the key pressed events of the panel.  Only the left,
        	 * right, and down arrow keys have any effect.  The left and right arrow keys move the
        	 * battleship, while the down arrow key releases the depthCharge.
        	 * @param KeyEvent - the event of pressing the key
        	 */
            @Override
            public void keyPressed(KeyEvent ke) {
                int code = ke.getKeyCode();//Determine which key was pressed
                if (code == KeyEvent.VK_LEFT) {//If it is the left arrow key,
                   /*Move the battleship left.  (If this moves the battleship out of the frame, its
                    * position will be adjusted in the battleship.updateNewFrame method
                    */
                    battleship.centerX -= 15;
                } else if (code == KeyEvent.VK_RIGHT) {//If it is the right arrow key,  
                	/*Move the battleship right.  (If this moves battleship out of the frame, its
                	 * position will be adjusted in the battleship.updateNewFrame method.
                	 */
                	battleship.centerX += 15;
                } else if (code == KeyEvent.VK_DOWN) {//If it is the down arrow key
                	//Start the depthCharge falling, if it is not already falling.
                    if (depthCharge.isFalling == false)
                    	depthCharge.isFalling = true;
                }
                
            }//end of the keyPressed Method
        } );//end of the Anonymous KeyAdaper Class
    }//end of the SubmarineKiller Constructor
    /**
     * paintComponent Method - Draws the current state of the game.  It draws the battleship, 
     * submarine, and DepthCharge by calling their respective draw methods.
     * @param Graphics - the graphic context
     */
    @Override
    synchronized public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
	g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
	/**Beginning of drawing the gradient colors of the background */
        Color color1 = new Color(252, 252, 164);//the yellowish color of the sky
        Color color2 = new Color(6, 52, 69);//the bluish color of the water
        GradientPaint gp = new GradientPaint(0, 150, color1, 0, getHeight(), color2);//
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, getWidth(), getHeight());//Fill the rectangle the dimension of the screen
        
        if (battleship == null) {
            /** The first time that paintComponent is called, it assigns values to the instance variables. */
            screenWidth = getWidth();
            screenHeight = getHeight();
            sun = new Sun();
            cloudOne = new CloudOne();
            cloudTwo = new CloudTwo();
            battleship = new Battleship();
            depthCharge = new DepthCharge();
            waves = new Wave();
            submarine = new Submarine();
            
        }
        if (hasFocus()) {
            cloudTwo.draw(g);
            sun.draw(g);
            cloudOne.draw(g);
       	 
        } else if (!hasFocus() && (hits + misses) == 10) {
                   cloudTwo.draw(g);
                   sun.draw(g);
                   cloudOne.draw(g);
           	 
    	} else {
                cloudTwo.draw(g);
                sun.draw(g);
                cloudOne.draw(g);
                g.setColor(new Color(112, 3, 34));
                g.setFont(font2);
                g.drawString("CLICK TO BEGIN", ((screenWidth/2) - 140), screenHeight/10);
            
        }

        battleship.draw(g);
        submarine.draw(g);
        depthCharge.draw(g);
        waves.draw(g);
        /**Display the score number of the hits and number of misses of the submarine */
        g2d.setColor(new Color(6, 52, 69));
        g2d.setFont(font1);
        g2d.drawString("SCORE:  " + hits, 15, 22);
        g2d.drawString("MISSES: " + misses, 15, 40);

    } //end paintComponent Method
    /**
     * Battleship Class - This nested class defines the Battleship.  Note that its constructor 
     * cannot be called until the screenWidth of the panel is known!
     */
    private class Battleship {
        //Instance Variables
        int centerX, centerY;  // Current position of the center of the Battleship.
        
        /**
         * Battleship Constructor - Creates an instance of the Battleship.  The constructor centers
         * the Battleship horizontally, 80 pixels from top.
         */
        Battleship() {
            centerX = screenWidth/2;
            centerY = 200;
            
        }//end of the Battleship Constructor
        /**
         * updateNewFrame Method - Updates the Battleship frame, and makes sure it does not move
         * off the screen.
         * @param Void
         */
        synchronized void updateNewFrame() {
            if (centerX < 130) {
                centerX = 130;
                
            } else if (centerX > screenWidth - 145) {
                centerX = screenWidth - 145;
            } 
        }//end of the updateNewFrame Method for the Battleship
        /**
         * draw Method - Draws the Battleship at its location
         * @param Graphics - the graphics context
         */
        synchronized void draw(Graphics g) {
            Graphics2D g2d = (Graphics2D)g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(new Color(107, 112, 120));//Beginning of the battleship
            g2d.fillArc(centerX-130, centerY -40, 275, 90, 0, -180);
            g2d.fillRoundRect(centerX, centerY -15, 50, 30, 10, 5);//left rectangle
            g2d.fillRoundRect(centerX - 40, centerY-5, 43, 25, 10, 20);//top rectangle
            g2d.fillRect(centerX +50, centerY, 40, 12);//right rectangle
            g2d.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2d.drawLine(centerX , centerY, centerX - 80, centerY - 10);
            
        }//end of the draw Method for the Battleship
    } //end the nested Battleship Class
    /**
     * DepthCharge Class - This nested class defines the DepthCharge. 
     */
    private class DepthCharge {
    	//Instance Variables
        int centerX, centerY;//The current position of the center of the DepthCharge.
        boolean isFalling;//If true, the DepthCharge is falling; if false, it is attached to the battleship
    	protected final int depthChargeWidth = 30;//the width in pixels
    	protected final int depthChargeHeight = 15;//the height in pixels
    	
    	/**
    	 * DepthCharge Constructor - Creates an object of the DepthCharge.  Initially, it is attached to
    	 * the Battleship
    	 */
        DepthCharge() {
        	isFalling = false;
                
        }//end of the DepthCharge Constructor
        /**
         * updateNewFrame Method - Updates the DepthCharge frame
         * @param Void
         */
        synchronized void updateNewFrame() { 

            if (((hits + misses) == numberOfCharges) && !submarine.isExploding)
            	doGameOver();
            if (isFalling) {// If DepthCharge is falling, take appropriate action.
                if (centerY > screenHeight) {//If the depthCharge's centerY position is greater than screenHeight,
                	//the DepthCharge has missed the submarine.  It is returned to its initial state
                    isFalling = false;
                    misses++;//The DepthCharge has missed.  Increment the number of misses.

                } else if (getCollision()) {//If depthCharge rectangle has intersected with submarine rectangle,
                	//there is an explosion occurring
                	submarine.isExploding = true;
                	submarine.explosionFrameNumber = 1;//The submarine enters the "isExploding" state
                	//Sound.DEPTH_CHARGE_EXPLOSION.play();
                	isFalling = false;//Return the DepthCharge's position back to the battleship
                	hits++;//The DepthCharge has hit the submarine.  Increment the number of hits
   
                } else {//If the DepthCharge has not fallen from the Battleship or hit the submarine,
                        //it is moved down 10 pixels.
                    centerY += 10;

                }
            }
        }//end of the updateNewFrame Method for the DepthCharge Class
    	/**
    	 * drawDepthCharge Method - Draws the depth charge under the center of the battleship, if the
    	 * depth charge is NOT falling.  If the depth charge is falling, it moves the depth charge
    	 * downwards.  And then, it verifies whether it has either hit or missed the submarine.  In
    	 * either case, its state changes to not falling and reappears back under the battleshipCenterX
    	 * position in the next frame.
    	 * @param Graphics - the graphics context
    	 * @return Void
    	 */
        synchronized void draw(Graphics g) {
            if (!isFalling) {// If not falling,
            	//set the centerX and centerY position to show the DepthCharge on the bottom of the battleship.
                centerX = battleship.centerX;
                centerY = battleship.centerY + 23;
            }
            /** Begin drawing the depth charge */
    		Graphics2D g2d = (Graphics2D) g;
    		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Color.BLACK);
                g2d.fillOval(centerX - 8, centerY - 8, depthChargeWidth, depthChargeHeight);

        }//end of the draw Method for the DepthCharge Class
        /**
         * getBounds Method - Gets the rectangular bounds of the depthCharge
         * @param Void
         * @return Rectangle - Returns the rectangular dimension of the depthCharge at the centerX and
         * centerY position.
         */
        Rectangle getBounds() {
        	return new Rectangle(centerX, centerY, depthChargeWidth, depthChargeHeight);
        }//end of the getBounds Method
        /**
         * getCollision Method - Determines whether or not the has collided with the submarine.
         * @param Void
         * @return Boolean - Returns true, if there is a collision with the submarine; otherwise, it returns
         * false.
         */
        boolean getCollision() {
            return (submarine.getBounds().intersects(getBounds()));
        	
        }//end of the getCollision Method
    }//end of the nested class DepthCharge
    /**
     * Submarine Class - This nested class defines the submarine.  Note that its constructor is not
     * be called until the screenWidth of the panel is known!
     */
    private class Submarine {
    	//Instance Variables
        int centerX, centerY; //Current position of the center of the submarine.
        boolean isMovingLeft; //Tells whether the submarine is moving left or right
        boolean isExploding;  //Set to true when the submarine is hit by the DepthCharge.
        //If the submarine is exploding, this is the number of frames since the explosion started
        int explosionFrameNumber;                
        int submarineWidth = 264;
        int submarineHeight = 72;
        
        /**
         * Submarine Constructor - Creates an object of the Submarine at a random location 72
         * pixels from the bottom of the frame.
         */
        Submarine() {
            centerX = (int)(screenWidth * Math.random());
            centerY = screenHeight - 72;
            isExploding = false;
            isMovingLeft = (Math.random() < 0.5);
            
        }//end of the Submarine Constructor
        /**
         * updateNewFrame Method - Updates the position of the submarine.  For example, this method
         * moves the submarine; or when exploding, it increases the explosionFrameNumber.
         * @param Void
         */
        synchronized void updateNewFrame() { 
            if (isExploding) {
                /** 
                 * If the submarine is exploding, add 1 to explosionFrameNumber.  When the number 
                 * reaches 15, the explosion ends and the submarine reappears in a random position.
                 */
                explosionFrameNumber++;
                if (explosionFrameNumber == 15) { 
                    centerX = (int)(screenWidth * Math.random());
                    centerY = screenHeight - 72;
                    isExploding = false;
                    isMovingLeft = (Math.random() < 0.5);
                }
            } else { //Move the submarine.
                if (Math.random() < 0.04) {  
                   /** 
                    *  In one frame out of every 25, on average, the submarine reverses its direction 
                    *  of motion.
                    */ 
                    isMovingLeft = ! isMovingLeft; 
                }
                if (isMovingLeft) { 
                    /** Move the submarine 5 pixels to the left.  If it moves of the left edge of
                     * the panel, move it back to the left edge and start it moving to the right.
                     */
                    centerX -= 5;  
                    if (centerX <= 40) {  
                        centerX = 40; 
                        isMovingLeft = false; 
                    }
                } else {
                    /**
                     * Move the submarine 5 pixels to the right.  If it moves off
                     * the right edge of the panel, move it back to the right 
                     * edge and start it moving to the left.
                     */
                    centerX += 5;         
                    if (centerX > (screenWidth - 200)) {  
                        centerX = screenWidth - 200;   
                        isMovingLeft = true; 
                    }
                }
            }
        }//end of the updateNewFrame Method for the Battleship
        /**
         * draw Method - Draws the submarine using its centerX and centerY positions as references.  If
         * the submarine is exploding, it draws the explosion with the graphics context fillOval
         * method, setColor method to yellow and red.
         * @param Graphics - the graphics context
         */
        synchronized void draw(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            /** Draw the black oval and rectangle that represents the main body of the submarine */
            g2d.setColor(Color.BLACK);
            g2d.fillOval(centerX - 30, centerY - 15, submarineWidth, submarineHeight);
            g2d.fillRect(centerX + 70, centerY - 30, 50, 25);
            
            /** Increase the size of the basic stroke to draw the periscope of the submarine */
            g2d.setStroke(new BasicStroke(8, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
            g2d.drawLine(centerX +90, centerY-80  , centerX+90, centerY-25);
            g2d.drawLine(centerX +100, centerY-80, centerX+90, centerY - 80);

            /** Set the color to gray to draw the three ovals on the side of the submarine */
            g2d.setColor(Color.GRAY);
            g2d.fillOval(centerX + 30, centerY , 20, 20);
            g2d.fillOval(centerX + 80, centerY, 20, 20);
            g2d.fillOval(centerX + 130, centerY, 20, 20);
            if (isExploding) {
                /** Draw an explosion that grows in size as it increases */
                g.setColor(Color.YELLOW);
                g.fillOval((centerX + 80) - 8 * explosionFrameNumber,
                        centerY - 4 * explosionFrameNumber,
                        13 * explosionFrameNumber,
                        5 * explosionFrameNumber);
                g.setColor(Color.RED);
                g.fillOval((centerX + 80) - 6 * explosionFrameNumber,
                        centerY - 3 * explosionFrameNumber,
                        8 * explosionFrameNumber,
                        2 * explosionFrameNumber);
            }
        }//end of the draw Method for the Submarine Class
        /**
         * getBounds Method - Gets the rectangular bounds of the submarine
         * @param Void
         * @return Rectangle - Returns the rectangular dimension of the submarine at the centerX and
         * centerY position.
         */
        Rectangle getBounds() {
            return new Rectangle(centerX, centerY, submarineWidth, submarineHeight);
        	
        }//end of the getBounds Method
    } // end of the Submarine Nested Class 
    /**
     * Wave Class - The nested class that defines the waves.  Note that its constructor is not called
     * until the screenWidth of the panel is known!
     */
    private class Wave {
    	//Instance Variable
    	int centerX;
    	/**
    	 * Default Wave Constructor - Creates an instance of the Wave.
    	 */
    	public Wave() {
    		centerX = screenWidth / 1;
                
    	}//end of the Default Wave Constructor
    	/**
    	 * updateNewFrame Method - Updates the centerX position of the drawn arcs that moves them across 
         * the panel.
    	 * @param Void
    	 */
    	synchronized void updateNewFrame() {
    		if (centerX < 0) {
                    centerX = screenWidth / 1;
                } else {
                    centerX -= 1;
                    if (centerX < screenWidth)
                        centerX += 20;
    		}		
    	}//end of the updateNewFrame Method
    	/**
    	 * draw Method - Draws the two roll of waves across the panel.
    	 * @param Graphics - the graphic context
    	 */
    	synchronized void draw(Graphics g) {
            Graphics2D g2d = (Graphics2D)g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            /** Begin drawing the wave by setting the color to blue and increasing the basic stroke */
            g2d.setColor(new Color(6, 52, 166));
            g2d.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            for (int i = 0; i < (screenWidth + 120); i += 20) {
                /**Drawing the first roll of waves across the panel */
                g2d.drawArc((centerX - 40)- i + 20, 220, 20, 20, 0, -180);
            }
            for (int i = 0; i < (screenWidth + 120); i += 20) {
                /**Drawing the second roll of waves across the panel */
                g2d.drawArc((centerX - 40) - i + 20, 250, 20, 20, 0, -180);
            }	
    	}//end of the draw Method
    }//end of the Wave Class
    /**
     * CloudOne Class - The nested class that defines the group of clouds in the foreground.  Note 
     * that its constructor is not call until the screenWidth and screenHeight are known!
     */
    private class CloudOne {
    	//Instance Variables
    	double centerX, centerY;
	Color cloudColor1;

    	/**
    	 * Default Cloud Constructor - Creates an instance of CloudOne class
    	 */
    	public CloudOne() {
            centerX = screenWidth / 1.3091;
            centerY = screenHeight / 18;
            cloudColor1 = new Color(252, 252, 252);
    
    	}//end of the CloudOne Constructor
    	/**
    	 * updateNewFrame Method - Updates the centerX position of CloudOne that moves it across
         * the panel.
    	 * @param Void
    	 */
    	synchronized void updateNewFrame() {
            if (centerX < 0) {
                centerX = screenWidth + 700;

            } else {
                centerX -= 2;

            }		
    	}//end of the updateNewFrame Method for the CloudOne
    	/**
    	 * draw Method - Draws CloudOne by using the graphics context fillOval and setColor methods 
         * to fill ovals using the CloudOne's centerX and centerY positions.
    	 * @param Graphics - the graphics context
    	 */
    	synchronized void draw(Graphics g) {
            Graphics2D g2d = (Graphics2D)g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            //Begin drawing medium cloud
            g2d.setColor(cloudColor1);
            g2d.fillOval((int)centerX - 290, (int)centerY + 25, 130, 20);
            g2d.fillOval((int)centerX - 282, (int)centerY + 22, 15, 15);
            g2d.fillOval((int)centerX - 272, (int)centerY + 15, 35, 20);
            g2d.fillOval((int)centerX - 250, (int)centerY, 50, 35);
            g2d.fillOval((int)centerX - 210, (int)centerY + 15, 40, 26);

            //Begin drawing large cloud
            g2d.fillOval((int)centerX - 495, (int)centerY + 5, 170, 30);
            g2d.fillOval((int)centerX - 465, (int)centerY - 20, 80, 40);
            g2d.fillOval((int)centerX - 485, (int)centerY, 40, 20);
            g2d.fillOval((int)centerX - 445, (int)centerY - 30, 40, 40);
            g2d.fillOval((int)centerX - 465, (int)centerY - 10, 100, 30);

            //Begin drawing small cloud
            g2d.fillOval((int)centerX - 700, (int)centerY + 40, 95, 20);
            g2d.fillOval((int)centerX - 690, (int)centerY + 37, 20, 20);
            g2d.fillOval((int)centerX - 675, (int)centerY + 25, 30, 25);
            g2d.fillOval((int)centerX - 650, (int)centerY + 35, 25, 15);
    		
    	}//end of the draw Method for the CloudOne
    }//end of the CloudOne Class
    /**
     * CloudTwo Class - The nested class that defines the group of clouds behind the sun.  Note 
     * that its constructor is not call until the screenWidth and screenHeight are known!
     */
    private class CloudTwo {
    	//Instance Variables
    	double centerX, centerY;
    	Color cloudColor2;
    	
    	/**
    	 * Default CloudTwo Constructor - Creates an instance Cloud2 class.
    	 */
    	public CloudTwo() {
            centerX = screenWidth / 1.1520;
            centerY = screenHeight / 23.0769;
            cloudColor2 = new Color(222, 220, 240);
    		
    	}//end of the Default CloudTwo Constructor
    	/**
    	 * updateNewFrame Method - Updates the centerX position of CloudTwo that moves it across the
         * panel.
    	 * @param Void
    	 */
    	synchronized void updateNewFrame() {
            if (centerX < 0) {
                centerX = screenWidth + 180;
            } else {
                centerX -= 3;
            }
    	}//end of the updateNewFrame Method for CloudTwo
    	/**
    	 * draw Method - Draws CloudTwo by using the graphics context fillOval and setColor methods 
         * to fill ovals using the CloudTwo's centerX and centerY positions.
    	 * @param Graphics - the graphics context
    	 */
    	synchronized void draw(Graphics g) {
            Graphics2D g2d = (Graphics2D)g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            /**Begin drawing the CloudTwo's smaller cloud */
            g2d.setStroke(new BasicStroke(16, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2d.setColor(cloudColor2);
            g2d.fillOval((int)centerX-180,(int)centerY+1, 23, 13);
            g2d.fillOval((int)centerX-175,(int)centerY+12, 25, 13);
            g2d.fillOval((int)centerX-170,(int)centerY-5, 23, 19);
            g2d.fillOval((int)centerX-155,(int)centerY+2, 22, 16);
            g2d.fillOval((int)centerX-155,(int)centerY+15, 20, 15);
            g2d.fillOval((int)centerX-145,(int)centerY+13, 20, 16);

            /**Begin drawing the CloudTwo's larger cloud */
            g2d.fillOval((int)centerX-60,(int)centerY+10, 40, 20);
            g2d.fillOval((int)centerX-40,(int)centerY+25, 35, 30);
            g2d.fillOval((int)centerX-38,(int)centerY+5, 35, 40);
            g2d.fillOval((int)centerX-10,(int)centerY+12, 40, 30);
            g2d.fillOval((int)centerX-10,(int)centerY+30, 30, 30);
            g2d.fillOval((int)centerX+5,(int)centerY+30, 45, 25);
    		
    	}//end of the draw Method for the CloudTwo
    }//end of the CloudTwo Class
    /**
     * Sun Class - The nested class that defines the Sun.
     */
    private class Sun {
    	//Instance Variables
    	double centerX;
    	double centerY;
    	int sunDiameter;
    	Color sunColor;
    	/**
    	 * Default Sun Constructor - Creates an instance of the Sun Class.
    	 */
    	public Sun() {
    		centerX = screenWidth/1.200;
    		centerY = screenHeight/45.0;
    		sunDiameter = 50;
    		sunColor = new Color(227, 197, 27);
                
    	}//end of the Default Sun Constructor
    	/**
    	 * updateNewFrame Method - Updates the sun by slightly changing its color
    	 * @param Void
    	 */
    	synchronized void updateNewFrame() {
    		if (sunColor == new Color(227, 197, 27)) {
                    sunColor = new Color(240, 223, 141);
                } else {
                    sunColor = new Color(227, 197, 27);
                }
    	}//end of the updateNewFrame Method for the Sun
    	/**
    	 * drawSun Method - Draws the sun using the graphics context setColor and fillOval methods,
    	 * @param Graphics - the graphic context
    	 */
    	synchronized void draw(Graphics g) {
    		Graphics2D g2d = (Graphics2D)g;
    		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    		
    		/**Begin drawing the Sun */
    		g2d.setStroke(new BasicStroke(8, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
    		g2d.setColor(sunColor);
    		g2d.fillOval((int)centerX, (int)centerY, sunDiameter, sunDiameter);
    		
    	}//end of the draw Method for the Sun
    }//end of the Sun Class
    /**
     * getScore - Get the number of the times the depth charge has hit the submarine.
     * @param Void
     * @return Int - Return an Integer for the number of times the depth charge has hit the 
     * submarine.
     */
    public int getScore() {return hits;}
    /**
     * getMisses Method - Get the number of times the depth charge has missed the submarine.
     * @param Void
     * @return Int - Return an Integer for the number of times the depth charge has missed the
     * submarine.
     */
    public int getMisses() {return misses;}
    /**
     * getPercentage Method - Get the percentage for the number of time the depth charge has hit
     * the submarine (i.e., hits divided by the sum of hits and and misses).
     * @param Void
     * @return double - Return a Double for the percentage of hits.
     */
    public double getPercentage() {return (hits / ((double)hits + (double)misses));}
    /**
     * doGameOver Method - Displays the game over dialog. It JDialog to displays the results of
     * the game, and uses JOptionPane to determine whether to repeat the game or not.
     * @param Void
     */
    public void doGameOver() {
        JOptionPane.showMessageDialog(this, "                        Your score is "+ getScore() +
                        "\n              and your Percentage is " + (int)(getPercentage()* 100) + "%",
                        "                         G A M E  O V E R", JOptionPane.PLAIN_MESSAGE);

        String message = "Do you want to play again?";
        int response = JOptionPane.showConfirmDialog(this, message, "", JOptionPane.YES_NO_OPTION);

        switch (response) {
            case JOptionPane.YES_OPTION:
                    doNewGame();
                    break;
            case JOptionPane.NO_OPTION:
                    System.exit(0);
                    break;
            default:
                    System.exit(0);
        }	
    }//end of the gameOver Method
    /**
     * doNewGame Method - Gets the default Toolkit, sets the screen size to full, creates the frame container and
     * the object of SubmarineKiller.  Adds the object of SubmarineKiller to the frame, sets it to the frame to be
     * visible, and set its default close operation.  In short, this method resets the game.
     * @param Void
     */
    public void doNewGame() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();	
        screenWidth = (int)screenSize.getWidth();
        screenHeight = (int)screenSize.getHeight();
		
        JFrame frame = new JFrame("Submarine Killer");
        SubmarineKiller game = new SubmarineKiller();
        frame.setIconImage(Toolkit.getDefaultToolkit().
                getImage(SubmarineKiller.class.getResource("/img/Submarine.png")));
        frame.setContentPane(game);
        frame.setBounds(0, 0, screenWidth, screenHeight);
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setResizable(false);//User cannot change the size of the frame
        frame.setVisible(true);
       
    }//end of the doNewGame Method
    /**
     * main Method - Gets the default Toolkit, sets the screen size to full, creates the frame container and
     * the object of SubmarineKiller.  Adds the object of SubmarineKiller to the frame, sets it the frame to
     * visible, and set its default close operation.
     * @param String[] - the command line arguments
     */
    public static void main(String[] args) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();	
        int screenWidth = (int)screenSize.getWidth();
        int screenHeight = (int)screenSize.getHeight();
		
        JFrame frame = new JFrame("Submarine Killer");
        SubmarineKiller game = new SubmarineKiller();
        frame.setIconImage(Toolkit.getDefaultToolkit().
                getImage(SubmarineKiller.class.getResource("/img/Submarine.png")));
        frame.setContentPane(game);
        frame.setBounds(0, 0, screenWidth, screenHeight);
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setResizable(false);//User cannot change the size of the frame
        frame.setVisible(true);
    }//end of the main Method
} // end of the SubmarineKiller Class