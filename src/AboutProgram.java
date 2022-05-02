import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class AboutProgram extends JDialog {
    JTextArea textArea = new JTextArea();
    Button buttonBack = new Button("Вернуться назад");

    JLabel frustumPicture = new JLabel(new javax.swing.ImageIcon(getClass().getResource("./assets/defaultFrustum.png")));

    AboutProgram() {
        setTitle("О программе");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        textArea.setEditable(false);

        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        textArea.setText("Эта программа предназначена для расчёта усечённого конуса.\n\n"
                + "Можно произвести расчёт,\n"
                + "удовлетворяя одному из следующих условий:\n\n"
                + "1) Зная: Радиус и образующую усеченного конуса.\n"
                + "2) Зная: Радиус и высоту усеченного конуса.\n"
                + "3) Зная: Площадь и образующую усеченного конуса.\n"
                + "4) Зная: Площадь и высоту усеченного конуса.");

        JPanel panelButtonBack = new JPanel(new GridLayout());
        panelButtonBack.add(buttonBack);

        String projectDirectory = System.getProperty("user.dir");

        try {
            File frustumFile = new File(projectDirectory + "\\src\\assets\\image.png");
            BufferedImage frustumPicture = ImageIO.read(frustumFile);
            JLabel picLabel = new JLabel(new ImageIcon(frustumPicture));
            textArea.add(picLabel);
        } catch(IOException error) {
            System.out.println(error.getMessage());
        }

        setLayout(new BorderLayout());
        add(textArea, BorderLayout.CENTER);
        add(panelButtonBack, BorderLayout.SOUTH);
        add(frustumPicture, BorderLayout.WEST);

        pack();
        setResizable(false);
        setLocationRelativeTo(null);

        buttonBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setModal(true);
        setVisible(true);
    }
}

