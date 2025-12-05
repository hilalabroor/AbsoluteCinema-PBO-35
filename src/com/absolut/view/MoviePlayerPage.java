package com.absolut.view;

import com.absolut.model.Film;
import com.absolut.model.User;

import javax.swing.*;
import javax.sound.sampled.*;
import java.awt.*;
import java.io.File;

public class MoviePlayerPage extends JPanel {

    private Film film;
    private boolean isPlaying = false;
    private Clip audioClip;

    // UI
    private Image gifImage;
    private JPanel screenPanel;
    private JProgressBar timelineBar;
    private JButton btnPlayPause, btnClose;
    private JLabel lblCurrentTime, lblTotalTime;

    public MoviePlayerPage(Film film, User user) {
        this.film = film;
        
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        try {
            gifImage = new ImageIcon("resources/gifs/" + film.getTrailerGifPath()).getImage();
        } catch (Exception e) {
            gifImage = null;
        }

        // LAYAR FILM
        screenPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                // Haluskan render
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

                int w = getWidth();
                int h = getHeight();
                
                g2.setColor(Color.BLACK);
                g2.fillRect(0, 0, w, h);
                
                if (gifImage != null) {
                    int iw = gifImage.getWidth(this);
                    int ih = gifImage.getHeight(this);
                    
                    if (iw > 0 && ih > 0) {
                        double scale = Math.min((double) w / iw, (double) h / ih);
                        
                        int dw = (int) (iw * scale);
                        int dh = (int) (ih * scale);
                        int x = (w - dw) / 2;
                        int y = (h - dh) / 2;
                        
                        g2.drawImage(gifImage, x, y, dw, dh, this);
                    }
                } else {
                    g2.setColor(Color.WHITE);
                    g2.drawString("Video Source Not Found", w/2 - 60, h/2);
                }
            }
        };
        screenPanel.setBackground(Color.BLACK);
        add(screenPanel, BorderLayout.CENTER);

        // CONTROL BAR
        JPanel control = new JPanel(new BorderLayout());
        control.setBackground(new Color(20, 20, 20));
        control.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        btnPlayPause = new JButton("Pause");
        btnClose = new JButton("Close Player");
        styleBtn(btnPlayPause);
        styleBtn(btnClose);
        btnClose.setBackground(new Color(200, 50, 50)); // Merah

        lblCurrentTime = new JLabel("00:00");
        lblCurrentTime.setForeground(Color.WHITE);
        lblTotalTime = new JLabel("02:15:00"); // Durasi dummy
        lblTotalTime.setForeground(Color.WHITE);

        timelineBar = new JProgressBar(0, 100);
        timelineBar.setValue(0);
        timelineBar.setPreferredSize(new Dimension(400, 8));
        timelineBar.setForeground(new Color(229, 9, 20));
        timelineBar.setBackground(Color.DARK_GRAY);

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        left.setOpaque(false);
        left.add(btnPlayPause);
        left.add(lblCurrentTime);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        right.setOpaque(false);
        right.add(lblTotalTime);
        right.add(btnClose);

        control.add(left, BorderLayout.WEST);
        control.add(timelineBar, BorderLayout.CENTER);
        control.add(right, BorderLayout.EAST);

        add(control, BorderLayout.SOUTH);

        // ACTIONS
        btnPlayPause.addActionListener(e -> togglePlay());
        
        btnClose.addActionListener(e -> {
            stopMovie();
            MainFrame.getInstance().showDashboard(user);
        });
    }

    private void styleBtn(JButton btn) {
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setBackground(new Color(50, 50, 50));
        btn.setForeground(Color.WHITE);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setPreferredSize(new Dimension(100, 35));
    }

    // LOGIC UTAMA
    
    public void startMovie() {
        if (isPlaying) return;
        isPlaying = true;
        btnPlayPause.setText("Pause");

        // Logic untuk refresh GIF
        try {
            if (gifImage != null) gifImage.flush(); // Buang cache lama
            gifImage = new ImageIcon("resources/gifs/" + film.getTrailerGifPath()).getImage();
        } catch (Exception e) {
            // ignore
        }
        screenPanel.repaint();

        playAudio(film.getAudioPath());

        // Thread Timer Simulasi
        new Thread(() -> {
            int p = 0;
            while (isPlaying && p <= 100) {
                try {
                    Thread.sleep(1000); // 1 Detik
                    p++;
                    final int v = p;
                    SwingUtilities.invokeLater(() -> {
                        timelineBar.setValue(v);
                        // Update Label Waktu (Menit:Detik)
                        int mins = v / 60;
                        int secs = v % 60;
                        lblCurrentTime.setText(String.format("%02d:%02d", mins, secs));
                    });
                } catch (InterruptedException ex) {
                    break;
                }
            }
            // Jika film selesai (100%), stop otomatis
            if (p >= 100) {
                 SwingUtilities.invokeLater(this::stopMovie);
            }
        }).start();
    }

    public void stopMovie() {
        isPlaying = false;
        if (audioClip != null) {
            if (audioClip.isRunning()) audioClip.stop();
            audioClip.close();
            audioClip = null;
        }
        btnPlayPause.setText("Play");
    }

    private void togglePlay() {
        if (isPlaying) {
            // PAUSE
            isPlaying = false;
            if (audioClip != null) audioClip.stop();
            btnPlayPause.setText("Play");
        } else {
            if (timelineBar.getValue() >= 100) timelineBar.setValue(0);
            
            isPlaying = true;
            btnPlayPause.setText("Pause");
            
            // Lanjutkan Audio
            if (audioClip != null) audioClip.start();
            else playAudio(film.getAudioPath());
            
            // Lanjutkan Thread Timer
            new Thread(() -> {
                int p = timelineBar.getValue();
                while (isPlaying && p <= 100) {
                    try {
                        Thread.sleep(1000);
                        p++;
                        final int v = p;
                        SwingUtilities.invokeLater(() -> {
                            timelineBar.setValue(v);
                            int mins = v / 60;
                            int secs = v % 60;
                            lblCurrentTime.setText(String.format("%02d:%02d", mins, secs));
                        });
                    } catch (Exception ex) { break; }
                }
            }).start();
        }
    }

    private void playAudio(String filename) {
        try {
            File f = new File("resources/audio/" + filename);
            if (f.exists()) {
                AudioInputStream ais = AudioSystem.getAudioInputStream(f);
                audioClip = AudioSystem.getClip();
                audioClip.open(ais);
                audioClip.loop(Clip.LOOP_CONTINUOUSLY); // Loop film
                audioClip.start();
            }
        } catch (Exception e) {}
    }
}