package interfaz;

import model.Marine;
import model.Sprite;
import sprites.MarineSprites;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;

public class PanelJuego extends JPanel implements MouseListener, MouseMotionListener,Runnable, ComponentListener {
	private ArrayList<Sprite> marines;
	private final static int MAX_WIDTH = 40;
	private final static int INVULNERABILITY = 20;
	private double seconds;
	private JLabel jlPoints;
	private BufferedImage asteroid;
	private Timer time;
	private BufferedImage background;
	private int cristal;
	private MarineSprites spritesVector;
	private boolean resizeBackground;
	private final static int COST_MARINE = 10;
	private int refreshTime;
	public PanelJuego() {
		this.spritesVector = new MarineSprites(MAX_WIDTH,MAX_WIDTH);
		Sprite.setMarineSprites(this.spritesVector);
		this.marines = new ArrayList<>();
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		chargeImages();
		this.cristal = 10;
		this.seconds =0;
		 this.time  = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				seconds +=1;
				if (seconds %3 == 0){
					cristal++;
				}
			}
		});
		this.refreshTime = 0;
		this.resizeBackground = false;
		setLayout(new GridBagLayout());
		genComponents();
		this.addComponentListener(this);
		Thread th = new Thread(this);
		th.start();

	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (!this.resizeBackground){
			this.background = resizeImage(this.getWidth(),this.getHeight(),this.background);
			time.start();
			this.resizeBackground = true;
		}
		paintBackground(g);
		paintSpraits(g);

	}


	public void paintBackground(Graphics g) {
		g.drawImage(this.background,0,0,null);
	}
	public void paintSpraits(Graphics g){
		if (this.refreshTime>=INVULNERABILITY){
			Sprite.comprobarSprites(this.marines);
		}
		for (int i = 0; i <this.marines.size() ; i++) {
			g.drawImage(this.marines.get(i).getCanvas(), this.marines.get(i).getX(), this.marines.get(i).getY(), this.marines.get(i).getWidth(), this.marines.get(i).getHeight(), null);
			this.marines.get(i).move(getWidth(),getHeight());
		}
		this.jlPoints.setText("CRISTAL: "+this.cristal);
		this.refreshTime++;
	}



	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (cristal >= COST_MARINE){
			this.marines.add(new Marine());
			cristal-=COST_MARINE;
		}

	}


	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void run() {
		while (true){
			repaint();
			try {
				Thread.sleep(25);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Toolkit.getDefaultToolkit().sync();
		}

	}

	public BufferedImage chargeSprite(String resource){
		BufferedImage bf = null;
		try {
			File f = new File(resource);
			if (f.exists()){
				bf	= ImageIO.read(f);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bf;
	}
	public void chargeImages(){
		this.background = chargeSprite("resource/background.jpg");
		this.asteroid = this.spritesVector.getmDp();


	}
	public void genComponents(){
		GridBagConstraints settingsObject = new GridBagConstraints();
		settingsObject.gridx= 1;
		settingsObject.gridy=1;
		settingsObject.weightx=1;
		settingsObject.weighty =1;
		settingsObject.fill = GridBagConstraints.BOTH;
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		add(panel,settingsObject);
		settingsObject = new GridBagConstraints();
		settingsObject.ipadx = 20;
		settingsObject.gridx=0;
		settingsObject.gridy=0;
		settingsObject.weightx=0;
		settingsObject.weighty =0;
		settingsObject.insets = new Insets(20,20,0,0);
		settingsObject.anchor =GridBagConstraints.NORTHEAST;
		this.jlPoints = new JLabel();
		this.jlPoints.setText(Double.toString(seconds));
		this.jlPoints.setOpaque(false);
		this.jlPoints.setForeground(Color.GREEN);
		this.add(this.jlPoints,settingsObject);
	}
	public BufferedImage resizeImage(int width,int height,BufferedImage img){
		Image image  = img.getScaledInstance(width,height,0);
		BufferedImage bf = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = bf.createGraphics();
		g2d.drawImage(image,0,0,null);
		g2d.dispose();
		return bf;
	}


	@Override
	public void componentResized(ComponentEvent e) {
		this.background = resizeImage(this.getWidth(),this.getHeight(),this.background);
	}

	@Override
	public void componentMoved(ComponentEvent e) {

	}

	@Override
	public void componentShown(ComponentEvent e) {

	}

	@Override
	public void componentHidden(ComponentEvent e) {

	}

	@Override
	public void mouseDragged(MouseEvent e) {

	}

	@Override
	public void mouseMoved(MouseEvent e) {

	}

}