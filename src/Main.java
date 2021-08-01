import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class Main extends JFrame {
    public static Main m = null;

    private static JLabel countDownLabel = null;
    private static JLabel timerLabel = null;
    private static JLabel charLabel = null;
    private static JLabel backgroundLabel = null;

    private static JButton startButton = null;

    private static int mm = 0;
    private static int ss = 0;
    private static int dot = 0;

    private static boolean gameStart = false;

    private static boolean stateGod = false;

    private static HashSet<Integer> pressedKeys = null;

    private static ArrayList<Timer> dropTimerList = null;

    private static Timer kt = null;

    private static int speed = 0;

    public Main() {
        pressedKeys = new HashSet<Integer>();
        dropTimerList = new ArrayList<>();
        gameStart = false;
        stateGod = false;
        mm = 0;
        ss = 0;
        dot = 0;
        speed = 1;

        timerLabel = new JLabel("00:00.0", JLabel.RIGHT);
        timerLabel.setFont(new Font("맑은 고딕", Font.BOLD, 32));
        timerLabel.setForeground(Color.WHITE);
        timerLabel.setBounds(0, 10, 770, 30);
        add(timerLabel);

        startButton = new JButton("Game Start");
        startButton.setFont(new Font("맑은 고딕", Font.BOLD, 28));
        startButton.setBounds(300, 200, 200, 100);
        add(startButton);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                remove(startButton);
                repaint();
                countDownLabel.setText("게임이 곧 시작됩니다.");

                Timer countDown = new Timer(1000, new ActionListener() {
                    int count = 3;

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(count == 0) {
                            ((Timer) e.getSource()).stop();
                            remove(countDownLabel);
                            repaint();
                            backgroundLabel.requestFocus();
                            //50 % speed
                            kt = new Timer(5, new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent arg0)
                                {
                                    if(!pressedKeys.isEmpty()){
                                        Iterator<Integer> i = pressedKeys.iterator();
                                        int n = 0;
                                        while(i.hasNext()){
                                            n = i.next();
                                            if(n == KeyEvent.VK_LEFT) {
                                                if(charLabel.getX() > 10)
                                                    charLabel.setLocation(charLabel.getX() - 2, charLabel.getY());
                                            } else if (n == KeyEvent.VK_RIGHT) {
                                                if(charLabel.getX() < 730)
                                                    charLabel.setLocation(charLabel.getX() + 2, charLabel.getY());
                                            } else if(n == KeyEvent.VK_SHIFT) {
                                                System.out.println("CHEAT ON");
                                                stateGod = true;
                                            } else if(n == KeyEvent.VK_CONTROL) {
                                                System.out.println("CHEAT OFF");
                                                stateGod = false;
                                            }
                                        }
                                    } else kt.stop();
                                }
                            });

                            Timer timer = new Timer(100, new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    dot ++;

                                    if(dot == 10) {
                                        dot = 0;
                                        ss ++;

                                        if(ss == 60) {
                                            ss = 0;
                                            mm ++;
                                        }
                                    }

                                    DecimalFormat mmdf = new DecimalFormat("00");
                                    DecimalFormat ssdf = new DecimalFormat("00");
                                    timerLabel.setText(mmdf.format(mm) + ":" + ssdf.format(ss) + "." + dot);
                                }
                            });

                            timer.setRepeats(true);
                            timer.start();

                            Timer t = new Timer(300, new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    JLabel drop = new JLabel(getImageIcon("meteor.png"));
                                    int ranX = (int) (Math.random() * 750);
                                    drop.setBounds(ranX, -25, 50, 50);
                                    remove(backgroundLabel);
                                    add(drop);
                                    add(backgroundLabel);
                                    validate();
                                    repaint();
                                    backgroundLabel.requestFocus();

                                    Timer dt = new Timer(5, new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent ex) {
                                            drop.setLocation(drop.getX(), drop.getY() + 1);

                                            if(!stateGod) {
                                                if((drop.getX() < (charLabel.getX() + 40)) && (drop.getX() + 50) > charLabel.getX() &&
                                                    drop.getY() + 50 > 480) {
                                                    if (drop.getY() > 415) {
                                                        for (int i = 0; i < dropTimerList.size(); i++)
                                                            dropTimerList.get(i).stop();
                                                        dropTimerList.clear();
                                                        timer.stop();
                                                        kt.stop();
                                                        ((Timer) e.getSource()).stop();
                                                        int result = JOptionPane.showConfirmDialog(null, "다시 플레이하시겠습니까?", "사망", JOptionPane.YES_NO_OPTION);

                                                        if (result == JOptionPane.CLOSED_OPTION || result == JOptionPane.NO_OPTION)
                                                            System.exit(0);
                                                        else if (result == JOptionPane.YES_OPTION) {
                                                            m = new Main();
                                                            dispose();
                                                        }
                                                    }
                                                }
                                            }

                                            if (drop.getY() + 50 >= 548) {
                                                remove(drop);
                                                repaint();
                                                ((Timer) ex.getSource()).stop();
                                            }
                                        }
                                    });

                                    dt.setRepeats(true);
                                    dt.start();

                                    dropTimerList.add(dt);
                                }
                            });

                            t.setRepeats(true);
                            t.start();

                            gameStart = true;
                        }
                        countDownLabel.setText(":: " + count + " ::");
                        count --;
                    }
                });
                countDown.setRepeats(true);
                countDown.start();
            }
        });

        countDownLabel = new JLabel("", JLabel.CENTER);
        countDownLabel.setFont(new Font("맑은 고딕", Font.BOLD, 28));
        countDownLabel.setForeground(Color.WHITE);
        countDownLabel.setBounds(0, 10, 800, 100);
        add(countDownLabel);

        charLabel = new JLabel(getImageIcon("char.png"));
        charLabel.setBounds(380, 480, 40, 68);
        add(charLabel);

        backgroundLabel = new JLabel(getImageIcon("background.png"));
        backgroundLabel.setBounds(0, 0, 800, 600);
        add(backgroundLabel);

        backgroundLabel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                pressedKeys.add(keyCode);
                if(!kt.isRunning()) kt.start();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int keyCode = e.getKeyCode();
                pressedKeys.remove(keyCode);
            }
        });

        backgroundLabel.requestFocus();

        // 프레임 설정
        setTitle("운석피하기");
        setIconImage(getImageIcon("meteor.png").getImage());
        setLayout(null);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        m = new Main();
    }

    public ImageIcon getImageIcon(String path) {
        ImageIcon image = new ImageIcon(Main.class.getResource(path));
        return image;
    }
}