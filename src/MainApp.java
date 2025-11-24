import com.absolut.view.LandingPage;
import javax.swing.SwingUtilities;

public class MainApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LandingPage().setVisible(true);
        });
    }
}