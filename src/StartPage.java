import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Страница "Добро пожаловать"
 *
 * @author I.E. Strelkovski
 */
public class StartPage {
    int WIDTH_START_PAGE = 375, HEIGHT_START_PAGE = 275;
    static Frame startFrame = new Frame("Геометрический калькулятор");
    String projectDirectory = System.getProperty("user.dir");
    JLabel titleLabel = new JLabel("Добро пожаловать!");
    JLabel subTitleLabel = new JLabel("Это геометрический калькулятор для усечённого конуса");

    Font titleFont = new Font("Arial", Font.BOLD, 30);
    Button enterButton = new Button("Войти в калькулятор");
    JLabel imageLabel;

    StartPage() {
        titleLabel.setFont(titleFont);

        startFrame.add(titleLabel);
        startFrame.add(subTitleLabel);

        startFrame.setLayout(new FlowLayout());
        startFrame.setVisible(true);

        addFrustumImageToMainPage();
        startFrame.add(enterButton);
        ActionListener enterButtonListener = new EnterListener();
        enterButton.addActionListener(enterButtonListener);

        startFrame.setSize(WIDTH_START_PAGE, HEIGHT_START_PAGE);

        startFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                System.exit(0);
            }
        });
    }

    // Добавляю изображение на главную страницу
    private void addFrustumImageToMainPage() {
        try {
            File frustumFile = new File(projectDirectory + "\\src\\assets\\defaultFrustum.png");
            BufferedImage frustumPicture = ImageIO.read(frustumFile);
            imageLabel = new JLabel(new ImageIcon(frustumPicture));
            startFrame.add(imageLabel);
        } catch(IOException error) {
            System.out.println(error.getMessage());
        }
    }

    // Слушетель клика на кнопку "Войти в калькулятор"
    private static class EnterListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            startFrame.setVisible(false);
            new Calculator();
        }
    }

    public static void main(String[] args) {
        new StartPage();
    }
}
