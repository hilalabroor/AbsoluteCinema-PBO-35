package com.absolut.view;

import com.absolut.model.Film;
import com.absolut.model.User;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MainFrame extends JFrame {
	private static MainFrame instance;
	private JPanel cards;
	private CardLayout cardLayout;
	private Map<String, JPanel> panels = new HashMap<>();

	private MainFrame() {
		setTitle("Absolute Cinema");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setLocationRelativeTo(null);

		cardLayout = new CardLayout();
		cards = new JPanel(cardLayout);
		cards.setBackground(new Color(18, 18, 18));
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(cards, BorderLayout.CENTER);

		LandingPage landing = new LandingPage();
		panels.put("landing", landing);
		cards.add(landing, "landing");

		LoginPage login = new LoginPage();
		panels.put("login", login);
		cards.add(login, "login");

		RegisterPage register = new RegisterPage();
		panels.put("register", register);
		cards.add(register, "register");

		cardLayout.show(cards, "landing");
	}

	public static synchronized MainFrame getInstance() {
		if (instance == null) {
			instance = new MainFrame();
		}
		return instance;
	}

	public void showLanding() {
		cardLayout.show(cards, "landing");
	}

	public void showLogin() {
		cardLayout.show(cards, "login");
	}

	public void showRegister() {
		cardLayout.show(cards, "register");
	}

	public void showDashboard(User user) {
        String key = "dashboard";
        DashboardPage panel = new DashboardPage(user);
        panels.put(key, panel);
        cards.add(panel, key);
        cardLayout.show(cards, key);
    }

	public void showLibrary(User user) {
        String key = "library"; 
        
        // SELALU BUAT BARU
        LibraryPage panel = new LibraryPage(user);
        panels.put(key, panel);
        cards.add(panel, key);
        
        cardLayout.show(cards, key);
    }

	// show detail panel
	public void showDetail(Film film, User user) {
			// Gunakan key statis saja biar hemat memori (kita timpa terus)
			String key = "detail"; 
			
			// SELALU BUAT BARU
			DetailPage panel = new DetailPage(film, user);
			
			panels.put(key, panel);
			cards.add(panel, key);
			
			cardLayout.show(cards, key);
		}

	// show movie player panel
    public void showMovie(Film film, User user) {
        String key = "movie"; 
        
        // SELALU BUAT BARU
        MoviePlayerPage panel = new MoviePlayerPage(film, user);
        
        panels.put(key, panel);
        cards.add(panel, key);
        
        cardLayout.show(cards, key);
        panel.startMovie(); 
    }

	// show Payment panel
	public void showPayment(String username, String password) {
        String key = "payment";
        
        // SELALU BUAT BARU
        PaymentPage panel = new PaymentPage(username, password);
        panels.put(key, panel);
        cards.add(panel, key);
        
        cardLayout.show(cards, key);
    }

    public void showPayment(User user) {
        String key = "payment";
        
        // SELALU BUAT BARU
        PaymentPage panel = new PaymentPage(user);
        panels.put(key, panel);
        cards.add(panel, key);
        
        cardLayout.show(cards, key);
    }

	// expose visibility
	public void open() {
		setVisible(true);
	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(() -> {
			MainFrame mf = MainFrame.getInstance();
			mf.open();
		});
	}
}
