import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDialog;
import javax.swing.JTextArea;

/**
 * Страница об авторе
 */
public class AboutAuthor extends JDialog {
    JTextArea textArea = new JTextArea(5, 10);
    Button buttonBack = new Button("Вернуться назад");

    AboutAuthor() {
        setTitle("Об авторе");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        textArea.setEditable(false);

        textArea.setFont(new Font("Arial", Font.BOLD, 16));
        textArea.setText("Группа ИТС-10\n"
                + "Вариант 22\n"
                + "Стрелковский Илья Евгеньевич");

        setLayout(new BorderLayout());
        add(textArea, BorderLayout.CENTER);
        add(buttonBack, BorderLayout.SOUTH);

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

