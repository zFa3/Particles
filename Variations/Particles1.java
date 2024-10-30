import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

class Particle {
    private double gravityStrength = 0.45;
    private int x, y;
    private double vx, vy;
    private int size = 5;
    public int colorMultiplier = 15;
    // private Color color;
    private int transparency = 15; // 0 - 255

    public Particle(int x, int y) {
        this.x = x;
        this.y = y;
        
        this.x = x + (int)(new Random().nextDouble()-0.5);
        this.y = y + (int)(new Random().nextDouble()-0.5);
        
        this.vx = (new Random().nextDouble()-0.5) * 0.01; 
        this.vy = (new Random().nextDouble()-0.5) * 0.01; 
        
        if (x > 500)
            this.vx = -5;
        else
            this.vx = 5;
        if (y > 500)
            this.vy = -5; 
        else 
            this.vy = 5;
        
        // this.vx = this.vy = 0;
        // this.color = new Color(new Random().nextInt(256), new Random().nextInt(256), new Random().nextInt(256), transparency);
    }

    public void move(int width, int height, int targetX, int targetY) {
        
        chDir(targetX, targetY);
        
        x += vx;
        y += vy;
        
        if (x < 0 || x > width) vx = -vx;
        if (y < 0 || y > height) vy = -vy;
    }

    private void chDir(int targetX, int targetY) {
        
        double deltaX = targetX - x;
        double deltaY = targetY - y;
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        
        if (distance > 0) {
            vx += (deltaX / distance) * gravityStrength;
            vy += (deltaY / distance) * gravityStrength;
        }
    }
    
    public int setBounds(int x){
        return Math.min(255, Math.max(0, x));
    }

    public void draw(Graphics g, Particle particle) {
        g.setColor(new Color(setBounds((int)(particle.vx + 4) * colorMultiplier), setBounds((int)(particle.vy + 4) * colorMultiplier), 255 - (colorMultiplier * 5), transparency));
        g.fillOval(x, y, size, size);
    }
}

class ParticleSimulation extends JPanel implements ActionListener {
    /*
    Some good presets
    ––––––––––––––––––
    int numParticles = 150;
    private int delay = 1; // milliseconds
    int spacing = 3;
    
    int numParticles = 150;
    private int delay = 25;
    int spacing = 10;
    
    int numParticles = 250;
    private int delay = 25;
    int spacing = 5;
    
    int numParticles = 500;
    private int delay = 25;
    int spacing = 2;
    
    int numParticles = 200;
    private int delay = 1;
    int spacing = 3;
    */
    
    int particleCount = 1;
    int numParticles = 300;
    // total particles == particleCount * numParticles
    // and iff the coords are within bounds

    private int delay = 1;
    double spacing = 2;
    
    public static int window_size = 1000;
    private ArrayList<Particle> particles;
    private Timer timer;
    private int targetX, targetY;

    public ParticleSimulation() {
        particles = new ArrayList<>();
        targetY = targetX = window_size / 2;

        // for (int i = 0; i < numParticles; i++) particles.add(new Particle(i/10, (numParticles - i)/10));
        for (int i = 0; i < numParticles; i++){
            for (int j = 0; j < numParticles; j++){
                double dist = Math.pow(Math.pow((i * spacing) - (window_size/2), 2) + Math.pow((j * spacing)-(window_size/2), 2), 0.5);
                if (dist < 350 && dist % 3 != 0 && dist > 250/* && (i * spacing) < 500 && (int)(j * spacing) < 500 */){
                    for (int k = 0; k < particleCount; k++){
                        particles.add(new Particle((int)(i * spacing), (int)(j * spacing)));
                    }
                }
            }
        }
        timer = new Timer(delay, this);
        timer.start();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Particle particle : particles) {
            particle.draw(g, particle);
        }
    }

    public void actionPerformed(ActionEvent e) {
        for (Particle particle : particles) {
            particle.move(getWidth(), getHeight(), targetX, targetY);
        }
        repaint();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Particle Simulation");
        ParticleSimulation simulation = new ParticleSimulation();
        frame.add(simulation);
        frame.setSize(window_size, window_size);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
