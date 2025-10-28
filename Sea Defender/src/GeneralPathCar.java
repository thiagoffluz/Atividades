
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.color.ColorSpace;
import java.util.ArrayList;
import javax.sound.sampled.*;
import javax.swing.*;

public class GeneralPathCar extends JFrame {

    private GamePanel gamePanel;

    // Construtor
    GeneralPathCar() {
        setTitle("Sea Defender");
        setSize(1000, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        gamePanel = new GamePanel();
        add(gamePanel);
        setVisible(true);

        gamePanel.startPlaneMovement();
    }

    public static void main(String[] argv) {
        new GeneralPathCar();
    }
}

class GamePanel extends JPanel implements KeyListener {

    // Coordenadas do barco
    private int boatX = 90;
    private int boatY = 600; // Posição ajustada para ficar mais alto acima do mar

    // Coordenadas do avião
    private int planeX = 100;
    private int planeY = 210;
    private int planeDirection = 1; // 1 para a direita, -1 para a esquerda

    // Lista para armazenar os projéteis
    private ArrayList<Projectile> projectiles = new ArrayList<>();

    // Parâmetros das ondas do mar
    private int waveOffset = 0;

    // Coordenadas das nuvens
    private int[] cloudX = {150, 400, 700};
    private int[] cloudY = {100, 80, 120};

    // Score
    private int score = 0;
    private boolean showMessage = false;
    private boolean isPaused = false;

    // Timer para movimentação do avião
    private Timer timer;
    private Timer fireDelayTimer;
    private boolean canFire = true;
    private JButton pauseButton;
    private JDialog pauseDialog;

    public GamePanel() {
        setDoubleBuffered(true); // Habilitar buffer duplo para suavizar a animação

        // Botão de pausa
        pauseButton = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Desenhar círculo externo
                g2d.setColor(Color.BLACK);
                g2d.fillOval(0, 0, getWidth(), getHeight());

                // Desenhar linhas internas de pausa
                g2d.setColor(Color.WHITE);
                int lineWidth = getWidth() / 5;
                int lineHeight = getHeight() / 2;
                int gap = lineWidth / 2;

                int x1 = (getWidth() / 2) - gap - lineWidth;
                int x2 = (getWidth() / 2) + gap;
                int y = (getHeight() - lineHeight) / 2;

                g2d.fillRect(x1, y, lineWidth, lineHeight);
                g2d.fillRect(x2, y, lineWidth, lineHeight);
            }
        };
        pauseButton.setBounds(850, 20, 30, 30); // Posição do botão de pausa
        pauseButton.setBorderPainted(false); // Remover borda do botão
        pauseButton.setContentAreaFilled(false); // Remover fundo do botão
        pauseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pauseGame();
            }
        });
        pauseButton.setFocusable(false);
        setLayout(null);
        add(pauseButton);

        pauseDialog = new JDialog((Frame) null, "Paused", Dialog.ModalityType.APPLICATION_MODAL);
        pauseDialog.setSize(400, 300);
        pauseDialog.setUndecorated(true); // Remover a barra de título e botão de fechar
        pauseDialog.setLayout(new GridBagLayout());
        pauseDialog.setBackground(new Color(0, 0, 0, 0)); // Fundo transparente

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10));
        buttonPanel.setBackground(new Color(0, 0, 0, 0)); // Fundo transparente

        // Botão de retomar o jogo
        JButton resumeButton = createButton("Voltar ao Jogo", Color.GREEN);
        resumeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resumeGame();
            }
        });

        // Botão de reiniciar o jogo
        JButton restartButton = createButton("Reiniciar Jogo", Color.ORANGE);
        restartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                restartGame();
            }
        });

        // Botão de sair do jogo
        JButton exitButton = createButton("Sair do Jogo", Color.RED);
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        buttonPanel.add(resumeButton);
        buttonPanel.add(restartButton);
        buttonPanel.add(exitButton);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        pauseDialog.add(buttonPanel, gbc);

        addKeyListener(this);
        setFocusable(true);
    }

    // Método auxiliar para criar um botão com texto e cor específicos
    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 18)); // Diminuir tamanho da fonte para botões menores
        button.setBackground(color);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 50)); // Diminuir tamanho para botões menores
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true)); // Borda arredondada preta
        return button;
    }

    // Métodos para mover o barco
    public void moveLeft() {
        if (!isPaused) {
            boatX -= 10;
            repaint();
        }
    }

    public void moveRight() {
        if (!isPaused) {
            boatX += 10;
            repaint();
        }
    }

    // Método para disparar um projétil
    public void fireProjectile() {
        if (!isPaused && canFire) {
            projectiles.add(new Projectile(boatX + 20, boatY - 60));
            playSound("disparo.wav"); // Reproduzir som de disparo
            canFire = false;
            fireDelayTimer = new Timer(250, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    canFire = true;
                    fireDelayTimer.stop();
                }
            });
            fireDelayTimer.start();
            repaint();
        }
    }

    // Método para reproduzir som
    private void playSound(String soundFile) {
        try {
            // Use getResourceAsStream para carregar o arquivo de som do diretório raiz do projeto
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getClassLoader().getResourceAsStream(soundFile));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Métodos para mover o avião
    private void movePlane() {
        if (!isPaused) {
            // Mover o avião horizontalmente
            planeX += planeDirection * 10;
            // Mudar direção ao atingir os quadrantes especificados
            if (planeX >= 800 || planeX <= 100) {
                planeDirection *= -1;
            }
            repaint();
        }
    }

    // Iniciar a movimentação do avião
    public void startPlaneMovement() {
        // Criar um timer para mover o avião a cada 100 milissegundos
        timer = new Timer(100, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                movePlane();
                updateProjectiles();
                updateWaveOffset();
                updateCloudOffset();
            }
        });
        timer.start();
    }

    // Atualizar projéteis
    private void updateProjectiles() {
        if (!isPaused) {
            ArrayList<Projectile> toRemove = new ArrayList<>();
            for (Projectile p : projectiles) {
                p.move();
                if (p.getY() < 0 || checkCollision(p)) {
                    toRemove.add(p);
                }
            }
            projectiles.removeAll(toRemove);
            repaint();
        }
    }

    // Atualizar a posição das ondas do mar
    private void updateWaveOffset() {
        if (!isPaused) {
            waveOffset = (waveOffset + 1) % 50;
            repaint();
        }
    }

    // Atualizar a posição das nuvens
    private void updateCloudOffset() {
        if (!isPaused) {
            for (int i = 0; i < cloudX.length; i++) {
                cloudX[i] -= 1;
                if (cloudX[i] < -60) { // Reposicionar a nuvem quando sair da tela
                    cloudX[i] = getWidth();
                }
            }
            repaint();
        }
    }

    // Verificar colisão entre projétil e avião
    private boolean checkCollision(Projectile p) {
        if (p.getX() >= planeX && p.getX() <= planeX + 170
                && p.getY() >= planeY - 120 && p.getY() <= planeY + 120) {
            score++;
            if (score >= 50) {
                showMessage = true;
            }
            return true;
        }
        return false;
    }

    // Pausar o jogo
    private void pauseGame() {
        isPaused = true;
        timer.stop();
        pauseDialog.setLocationRelativeTo(this);
        repaint(); // Repaint para aplicar a dessaturação
        pauseDialog.setVisible(true);
    }

    // Retomar o jogo
    private void resumeGame() {
        isPaused = false;
        timer.start();
        pauseDialog.setVisible(false);
        requestFocusInWindow();
        repaint(); // Repaint para remover a dessaturação
    }

    // Reiniciar o jogo
    private void restartGame() {
        isPaused = false;
        score = 0;
        boatX = 90;
        boatY = 600;
        planeX = 100;
        planeY = 210;
        planeDirection = 1;
        projectiles.clear();
        showMessage = false;
        timer.start();
        pauseDialog.setVisible(false);
        requestFocusInWindow();
        repaint(); // Repaint para remover a dessaturação
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Desenhar o céu
        g2d.setColor(new Color(135, 206, 250)); // Cor azul clara do céu
        g2d.fillRect(0, 0, getWidth(), getHeight() - 150);

        // Desenhar o mar com ondas
        drawWaves(g2d, getHeight() - 150, getWidth(), 150, waveOffset);

        // Desenhar nuvens
        g2d.setColor(Color.WHITE);
        for (int i = 0; i < cloudX.length; i++) {
            drawCloud(g2d, cloudX[i], cloudY[i]);
        }

        // Usar antialiasing para ter linhas mais suaves
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Aplicar dessaturação se estiver pausado
        if (isPaused) {
            BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D ig2d = image.createGraphics();
            super.paintComponent(ig2d);
            ig2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            ig2d.dispose();

            // Converter para escala de cinza
            ColorConvertOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
            op.filter(image, image);

            // Desenhar a imagem em escala de cinza
            g2d.drawImage(image, 0, 0, null);
            // Desenhar uma sobreposição transparente para escurecer a tela
            g2d.setColor(new Color(0, 0, 0, 100)); // Sobreposição mais escura
            g2d.fillRect(0, 0, getWidth(), getHeight());

            // Desenhar apenas a pontuação e o botão de pausa sobre a imagem em escala de cinza
            drawScoreAndPauseButton(g2d);
        } else {
            drawGame(g2d);
        }
    }

    // Método para desenhar ondas
    private void drawWaves(Graphics2D g2d, int yStart, int width, int height, int offset) {
        g2d.setColor(new Color(0, 191, 255)); // Cor azul claro para o mar
        g2d.fillRect(0, yStart, width, height);

        g2d.setColor(new Color(0, 150, 200)); // Cor mais escura para as ondas

        int waveHeight = 10;
        for (int x = -offset; x < width; x += 50) {
            for (int y = yStart; y < yStart + height; y += 2 * waveHeight) {
                g2d.fillArc(x, y, 50, waveHeight, 0, 180);
            }
        }
    }

    // Desenhar o jogo
    private void drawGame(Graphics2D g2d) {
        BasicStroke bs = new BasicStroke(3.0f);
        g2d.setStroke(bs);

        GeneralPath gp3 = new GeneralPath();
        GeneralPath gp5 = new GeneralPath();

        // BARCO
        gp5.moveTo(boatX, boatY);
        gp5.lineTo(boatX + 40, boatY);
        gp5.lineTo(boatX + 60, boatY - 20);
        gp5.lineTo(boatX - 20, boatY - 20);
        gp5.moveTo(boatX - 20, boatY - 20);
        gp5.lineTo(boatX, boatY);
        gp5.moveTo(boatX + 20, boatY - 20);
        gp5.lineTo(boatX + 20, boatY - 60);
        gp5.moveTo(boatX + 20, boatY - 60);
        gp5.lineTo(boatX + 40, boatY - 50);
        gp5.lineTo(boatX + 20, boatY - 40);

        // AVIÃO
        gp3.moveTo(planeX, planeY);
        gp3.lineTo(planeX - 20, planeY - 80);
        gp3.lineTo(planeX + 30, planeY - 20);
        gp3.lineTo(planeX + 100, planeY - 20);
        gp3.lineTo(planeX + 50, planeY - 120);
        gp3.lineTo(planeX + 150, planeY - 20);
        gp3.lineTo(planeX + 170, planeY - 20);
        gp3.quadTo(planeX + 230, planeY, planeX + 170, planeY + 20);
        gp3.moveTo(planeX, planeY);
        gp3.lineTo(planeX - 20, planeY + 80);
        gp3.lineTo(planeX + 30, planeY + 20);
        gp3.lineTo(planeX + 100, planeY + 20);
        gp3.lineTo(planeX + 50, planeY + 120);
        gp3.lineTo(planeX + 150, planeY + 20);
        gp3.lineTo(planeX + 170, planeY + 20);

        // Se o avião estiver se movendo para a esquerda, inverta horizontalmente
        if (planeDirection == -1) {
            AffineTransform flip = AffineTransform.getScaleInstance(-1, 1);
            flip.translate(-2 * planeX - 100, 0); // Deslocar para manter a posição
            gp3.transform(flip);
        }

        // Definir a cor para branco para o barco e avião
        g2d.setColor(Color.WHITE);
        g2d.fill(gp3);
        g2d.fill(gp5);

        // Definir a cor de volta para preto para o contorno
        g2d.setColor(Color.BLACK);
        g2d.draw(gp3);
        g2d.draw(gp5);

        // Desenhar projéteis
        g2d.setColor(Color.RED);
        for (Projectile p : projectiles) {
            p.draw(g2d);
        }

        // Desenhar a pontuação
        g2d.setFont(new Font("sansserif", Font.BOLD, 20));
        g2d.setColor(Color.BLACK);
        g2d.drawString("Score: " + score, 700, 40); // Posição ajustada do texto de pontuação

        // Mostrar mensagem se a pontuação for 50 ou mais
        /*  
        if (showMessage) {
            g2d.setFont(new Font("sansserif", Font.BOLD, 40));
            g2d.setColor(Color.GREEN);
            g2d.drawString("Parabéns, continue assim!", 250, 500);
        }
         */
    }

    // Desenhar a pontuação e o botão de pausa
    private void drawScoreAndPauseButton(Graphics2D g2d) {
        // Desenhar a pontuação
        g2d.setFont(new Font("sansserif", Font.BOLD, 20));
        g2d.setColor(Color.BLACK);
        g2d.drawString("Score: " + score, 700, 40);
    }

    // Desenhar nuvens
    private void drawCloud(Graphics2D g2d, int x, int y) {
        g2d.fillOval(x, y, 60, 40);
        g2d.fillOval(x + 20, y - 20, 60, 40);
        g2d.fillOval(x + 40, y, 60, 40);
    }

    // Manipular eventos de teclado
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            moveLeft();
        } else if (key == KeyEvent.VK_RIGHT) {
            moveRight();
        } else if (key == KeyEvent.VK_SPACE) {
            fireProjectile();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}

class Projectile {

    private int x, y;

    public Projectile(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Mover o projétil
    public void move() {
        y -= 10;
    }

    // Obter a coordenada x do projétil
    public int getX() {
        return x;
    }

    // Obter a coordenada y do projétil
    public int getY() {
        return y;
    }

    // Desenhar o projétil
    public void draw(Graphics2D g2d) {
        g2d.setStroke(new BasicStroke(2.0f));
        g2d.setColor(Color.RED);
        g2d.drawLine(x, y, x, y - 10);
    }
}

class MyFinishWindow extends WindowAdapter {

    public void windowClosing(WindowEvent e) {
        System.exit(0);
    }
}
