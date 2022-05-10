import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

/**
 * Главная страница приложения
 */
public class Calculator {
    final int WIDTH_MAIN_PAGE = 800, HEIGHT_MAIN_PAGE = 500;
    final String TITLE_MAIN_PAGE = "Геометрический калькулятор. Усеченный конус";
    final int DEFAULT_SIZE_TEXT_FIELD = 5;
    Frame mainFrame;
    Button aboutProgramButton, aboutAuthorButton, calculateButton, clearButton, exitButton;
    static Panel picturePanel, calculatePanel, buttonPanel;
    static JLabel resultCalculateFields = new JLabel("");
    String[] optionsToChoose = {
            "Не выбранно",
            "1) Дано: Радиус и образующая",
            "2) Дано: Радиус и высота",
            "3) Дано: Площадь и образующая",
            "4) Дано: Площадь и высота"
    };
    static JComboBox<String> operationsBox;
    String projectDirectory = System.getProperty("user.dir");
    Color BACKGROUND_MAIN_PAGE_COLOR = Color.LIGHT_GRAY;
    Color BACKGROUND_PICTURE_PANEL_COLOR = Color.WHITE;
    static JTextField firstTextField, secondTextField, thirdTextField;
    JLabel pictureLabel;
    static JLabel calculateResult = new JLabel("");
    static int currentOperationIndex;

    // Конструктор в котором происходят основные настройки
    Calculator(){
        mainFrame = new Frame(TITLE_MAIN_PAGE);
        mainFrame.setSize(WIDTH_MAIN_PAGE, HEIGHT_MAIN_PAGE);
        mainFrame.setLayout(new GridLayout());
        picturePanel = new Panel();
        calculatePanel = new Panel();
        buttonPanel = new Panel(new GridLayout(5, 1));
        mainFrame.setBackground(BACKGROUND_MAIN_PAGE_COLOR);
        picturePanel.setBackground(BACKGROUND_PICTURE_PANEL_COLOR);

        addMainPropertiesToPicturePanel(picturePanel);
        addFrustumImageToPicturePanel("\\src\\assets\\defaultFrustum.png", picturePanel);

        createCalculateUI(calculatePanel);

        aboutProgramButton = new Button("О программе");
        aboutAuthorButton = new Button("Об авторе");
        calculateButton = new Button("Вычислить");
        clearButton = new Button("Отчистить форму");
        exitButton = new Button("Выйти");
        Button[] buttons = {aboutProgramButton, aboutAuthorButton, calculateButton, clearButton, exitButton};

        ActionListener aboutProgramButtonListener = new AboutProgramListener();
        ActionListener aboutAuthorButtonListener = new AboutAuthorListener();
        ActionListener calculateButtonListener = new CalculateListener();
        ActionListener clearButtonListener = new ClearListener();
        ActionListener exitButtonListener = new ExitListener();
        ActionListener[] buttonListeners = {
                aboutProgramButtonListener,
                aboutAuthorButtonListener,
                calculateButtonListener,
                clearButtonListener,
                exitButtonListener
        };

        createButtonsForButtonPanel(buttons, buttonListeners, buttonPanel);

        Panel[] panels = {picturePanel, calculatePanel, buttonPanel};
        addPanelsToFrame(mainFrame, panels);

        mainFrame.setVisible(true);
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                System.exit(0);
            }
        });
    }

    // Добавление 3-ёх основных панелей на главный кадр
    private void addPanelsToFrame(Frame baseFrame, Panel[] panels) {
        for (Panel panel : panels) {
            baseFrame.add(panel);
        }
    }

    // Добавление картинки на PicturePanel
    private void addFrustumImageToPicturePanel(String localPathToFile, Panel currentPanel) {
        try {
            File frustumFile = new File(projectDirectory + localPathToFile);
            BufferedImage frustumPicture = ImageIO.read(frustumFile);
            pictureLabel = new JLabel(new ImageIcon(frustumPicture));
            currentPanel.add(pictureLabel);
        } catch(IOException error) {
            System.out.println(error.getMessage());
        }
    }

    // Изменение текущей картинки на PicturePanel
    private void changeCurrentFrustumImageFromPicturePanel(String localPathToFile, Panel currentPanel) {
        currentPanel.remove(pictureLabel);
        addFrustumImageToPicturePanel(localPathToFile, currentPanel);
        currentPanel.repaint();
    }

    // Добавление основных свойств к картинке, в PicturePanel
    private void addMainPropertiesToPicturePanel(Panel currentPanel) {
        String[] properties = {
                "Свойства:",
                "r - радиус верхнего основания",
                "R - радиус нижнего основания",
                "l - образующая",
                "h - высота",
                "α - тупой угол",
                "β - острый угол",
                "V - объем",
                "S - площадь",
                "<br/>",
        };
        String propertiesWithSpace = String.join("<br/>", properties);
        JLabel propertiesLabel = new JLabel("<html>" + propertiesWithSpace + "</html>");
        currentPanel.add(propertiesLabel);
    }

    // Создание кнопок для ButtonPanel
    private void createButtonsForButtonPanel(Button[] buttons, ActionListener[] listeners, Panel currentPanel) {
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].addActionListener(listeners[i]);
            currentPanel.add(buttons[i]);
        }
    }

    // Создание пользовательского интерфейса калькулятора
    private void createCalculateUI(Panel currentPanel) {
        JLabel titleLabel = new JLabel("Выберите из предложенных данных:");
        currentPanel.add(titleLabel);

        operationsBox = new JComboBox<>(optionsToChoose);
        currentPanel.add(operationsBox);

        JLabel firstLabel = new JLabel();
        JLabel secondLabel = new JLabel();
        JLabel thirdLabel = new JLabel();

        firstTextField = new JTextField(DEFAULT_SIZE_TEXT_FIELD);
        secondTextField = new JTextField(DEFAULT_SIZE_TEXT_FIELD);
        thirdTextField = new JTextField(DEFAULT_SIZE_TEXT_FIELD);

        firstTextField.setVisible(false);
        secondTextField.setVisible(false);
        thirdTextField.setVisible(false);

        firstTextField.setBorder(BorderFactory.createLineBorder(Color.black));
        secondTextField.setBorder(BorderFactory.createLineBorder(Color.black));
        thirdTextField.setBorder(BorderFactory.createLineBorder(Color.black));

        currentPanel.add(firstLabel);
        currentPanel.add(firstTextField);
        currentPanel.add(secondLabel);
        currentPanel.add(secondTextField);
        currentPanel.add(thirdLabel);
        currentPanel.add(thirdTextField);

        currentPanel.add(calculateResult);

        operationsBox.addActionListener(event -> {
            currentOperationIndex = operationsBox.getSelectedIndex();

            JTextField[] textFields = {firstTextField, secondTextField, thirdTextField};
            JLabel[] labels = {firstLabel, secondLabel, thirdLabel};
            setVisibleLabelAndTextField(textFields, labels, true);
            clearTextFields(textFields);
            calculateResult.setText("");
            resultCalculateFields.setText("");
            calculatePanel.repaint();

            switch (currentOperationIndex) {
                case 0 -> {
                    setVisibleLabelAndTextField(textFields, labels, false);
                    changeCurrentFrustumImageFromPicturePanel(
                            "\\src\\assets\\defaultFrustum.png",
                            picturePanel
                    );
                }
                case 1 -> {
                    firstLabel.setText("Радиус нижнего основания R");
                    secondLabel.setText("Радиус верхнего основания r");
                    thirdLabel.setText("Образующая усеченного конуса l");
                    changeCurrentFrustumImageFromPicturePanel(
                            "\\src\\assets\\radiusFormingFrustum.png",
                            picturePanel
                    );
                }
                case 2 -> {
                    firstLabel.setText("Радиус нижнего основания R");
                    secondLabel.setText("Радиус верхнего основания r");
                    thirdLabel.setText("Высота усеченного конуса h");
                    changeCurrentFrustumImageFromPicturePanel(
                            "\\src\\assets\\radiusHeightFrustum.png",
                            picturePanel
                    );
                }
                case 3 -> {
                    firstLabel.setText("Площадь нижнего основания S");
                    secondLabel.setText("Площадь верхнего основания s");
                    thirdLabel.setText("Образующая усеченного конуса l");
                    changeCurrentFrustumImageFromPicturePanel(
                            "\\src\\assets\\areaFormingFrustum.png",
                            picturePanel
                    );
                }
                case 4 -> {
                    firstLabel.setText("Площадь нижнего основания S");
                    secondLabel.setText("Площадь верхнего основания s");
                    thirdLabel.setText("Высота усеченного конуса h");
                    changeCurrentFrustumImageFromPicturePanel(
                            "\\src\\assets\\areaHeightFrustum.png",
                            picturePanel
                    );
                }
            }
        });
    }

    // Отчистка текстовых полей в форме
    private void clearTextFields(JTextField[] fields) {
        for (JTextField field : fields) {
            field.setText("");
        }
    }

    // Установка видимости текстовых полей и заголовков
    private void setVisibleLabelAndTextField(JTextField[] textFields, JLabel[] labels, boolean isVisible) {
        for (JTextField textField : textFields) {
            textField.setVisible(isVisible);
        }
        for (JLabel label : labels) {
            label.setVisible(isVisible);
        }
    }

    // Слушатель на кнопку "О программе"
    private static class AboutProgramListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            new AboutProgram();
        }
    }

    // Слушатель на кнопку "Об авторе"
    private static class AboutAuthorListener implements  ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            new AboutAuthor();
        }
    }

    // Слушатель на кнопку "Отчистить форму"
    private static class ClearListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            firstTextField.setText("");
            secondTextField.setText("");
            thirdTextField.setText("");
            calculateResult.setText("");
            resultCalculateFields.setText("");
            operationsBox.setSelectedIndex(0);
        }
    }

    // Слушатель на кнопку "Выйти"
    private static class ExitListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            System.exit(0);
        }
    }

    /**
     * Класс отвечающий за логику вычислений калькулятора
     */
    private static class CalculateListener implements ActionListener {
        int firstValue, secondValue, thirdValue;

        CalculateListener() {
            JTextField[] fields = {firstTextField, secondTextField, thirdTextField};
            parseTextFields(fields);

            boolean isIncorrectFields = checkToCorrectTextFields();
            if (!isIncorrectFields) return;

            calculateResult.setText("Результат вычислений:");
            inferCalculation();
        }

        // Вызывает нужный метод для вычислений, в зависимости от выбранной операции
        private void inferCalculation() {
            switch (currentOperationIndex) {
                case 1 -> calculateRadiusAndForming();
                case 2 -> calculateRadiusAndHeight();
                case 3 -> calculateAreaAndForming();
                case 4 -> calculateAreaAndHeight();
            }
        }

        // Вычисление исходя из данных радиуса и образующей
        private void calculateRadiusAndForming() {
            if (hasCorrectValue()) return;

            double height = Math.sqrt(Math.pow(thirdValue, 2) - Math.pow((secondValue - firstValue), 2));
            double volume = (1.0 / 3.0) * Math.PI * height *
                    (Math.pow(firstValue, 2) + firstValue * secondValue + Math.pow(secondValue, 2));
            double allSquare = Math.PI * (Math.pow(secondValue, 2) +
                    (firstValue + secondValue) * thirdValue + Math.pow(firstValue, 2));
            double sideSquare = Math.PI * (firstValue + secondValue) * thirdValue;
            double underSideSquare = Math.PI * Math.pow(firstValue, 2);
            double overSideSquare = Math.PI * Math.pow(secondValue, 2);
            double underDiagonal = firstValue * 2;
            double overDiagonal = secondValue * 2;
            double axisSquare = (double)(firstValue + firstValue + secondValue + secondValue) / 2 * height;
            double betaAngle = Math.acos((double)(firstValue - secondValue) / thirdValue) * 180 / Math.PI;
            double alfaAngle = 180 - betaAngle;

            formattingCalculationResult(
                    true,
                    false,
                    height,
                    volume,
                    allSquare,
                    sideSquare,
                    underSideSquare,
                    overSideSquare,
                    axisSquare,
                    betaAngle,
                    alfaAngle,
                    underDiagonal,
                    overDiagonal
            );
        }

        // Вычисление исходя из данных радиуса и высоты
        private void calculateRadiusAndHeight() {
            if (hasCorrectValue()) return;

            double forming = Math.sqrt(Math.pow(thirdValue, 2) + Math.pow((firstValue - secondValue), 2));
            double volume = (1.0 / 3.0) * Math.PI * thirdValue *
                    (Math.pow(firstValue, 2) + firstValue * secondValue + Math.pow(secondValue, 2));
            double allSquare = Math.PI * (Math.pow(secondValue, 2) +
                    (firstValue + secondValue) * forming + Math.pow(firstValue, 2));
            double sideSquare = Math.PI * (firstValue + secondValue) * forming;
            double underSideSquare = Math.PI * Math.pow(firstValue, 2);
            double overSideSquare = Math.PI * Math.pow(secondValue, 2);
            double underDiagonal = firstValue * 2;
            double overDiagonal = secondValue * 2;
            double axisSquare = (double)(firstValue + firstValue + secondValue + secondValue) / 2 * thirdValue;
            double betaAngle = Math.acos((double)(firstValue - secondValue) / forming) * 180 / Math.PI;
            double alfaAngle = 180 - betaAngle;

            formattingCalculationResult(
                    false,
                    false,
                    forming,
                    volume,
                    allSquare,
                    sideSquare,
                    underSideSquare,
                    overSideSquare,
                    axisSquare,
                    betaAngle,
                    alfaAngle,
                    underDiagonal,
                    overDiagonal
            );
        }

        // Вычисление исходя из данных площади оснований и образующей
        private void calculateAreaAndForming() {
            if (hasCorrectValue()) return;

            double underRadius = Math.sqrt(firstValue / Math.PI);
            double overRadius = Math.sqrt(secondValue / Math.PI);
            double height = Math.sqrt(Math.pow(thirdValue, 2) - Math.pow((underRadius - overRadius), 2));
            double volume = height / 3 * (firstValue + Math.sqrt(firstValue * secondValue) + secondValue);
            double sideSquare = ((2 * Math.PI * (underRadius + overRadius)) / 2) * thirdValue;
            double allSquare = sideSquare + firstValue + secondValue;
            double underDiagonal = underRadius * 2;
            double overDiagonal = overRadius * 2;
            double axisSquare = (underDiagonal + overDiagonal) / 2 * height;
            double betaAngle = Math.acos((underRadius - overRadius) / thirdValue) * 180 / Math.PI;
            double alfaAngle = 180 - betaAngle;

            formattingCalculationResult(
                    true,
                    true,
                    height,
                    volume,
                    allSquare,
                    sideSquare,
                    underRadius,
                    overRadius,
                    axisSquare,
                    betaAngle,
                    alfaAngle,
                    underDiagonal,
                    overDiagonal
            );
        }

        // Вычисление исходя из данных площади оснований и высоты
        private void calculateAreaAndHeight() {
            if (hasCorrectValue()) return;

            double underRadius = Math.sqrt(firstValue / Math.PI);
            double overRadius = Math.sqrt(secondValue / Math.PI);
            double forming = Math.sqrt(Math.pow(thirdValue, 2) + Math.pow((underRadius - overRadius), 2));
            double volume = (double)1 / 3 * Math.PI * thirdValue
                    * (Math.pow(underRadius, 2) + underRadius * overRadius + Math.pow(overRadius, 2));
            double sideSquare = Math.PI * (underRadius + overRadius) * forming;
            double allSquare =  firstValue + secondValue + sideSquare;
            double underDiagonal = underRadius * 2;
            double overDiagonal = overRadius * 2;
            double axisSquare = (underDiagonal + overDiagonal) / 2 * thirdValue;
            double betaAngle = Math.acos((underRadius - overRadius) / forming) * 180 / Math.PI;
            double alfaAngle = 180 - betaAngle;

            formattingCalculationResult(
                    false,
                    true,
                    forming,
                    volume,
                    allSquare,
                    sideSquare,
                    underRadius,
                    overRadius,
                    axisSquare,
                    betaAngle,
                    alfaAngle,
                    underDiagonal,
                    overDiagonal
            );
        }

        // Простая проверка на заполненность текстовых полей
        private boolean checkToCorrectTextFields() {
            return firstValue != 0 && secondValue != 0 && thirdValue != 0 && currentOperationIndex != 0;
        }

        // Проверка на то, что площадь или радиус нижнего основания больше верхнего
        private boolean hasCorrectValue() {
            if (secondValue >= firstValue) {
                calculateResult.setText("Ошибка. Неверные данные.");
                resultCalculateFields.setText("");
                return true;
            }
            return false;
        }

        // Преобразование текстовых палей в числа
        private void parseTextFields(JTextField[] fields) {
            try {
                firstValue = Integer.parseInt(fields[0].getText());
                secondValue = Integer.parseInt(fields[1].getText());
                thirdValue = Integer.parseInt(fields[2].getText());
            } catch(NumberFormatException error) {
                calculateResult.setText("Введите корректные данные.");
                resultCalculateFields.setText("");
                System.out.println(error.getMessage());
            }
        }
        @Override
        public void actionPerformed(ActionEvent event) {
            new CalculateListener();
        }

        // Метод отвечающий за вывод результата
        private void formattingCalculationResult(boolean isHeight,
                                                 boolean isRadius,
                                                 double heightOrForming,
                                                 double volume,
                                                 double allSquare,
                                                 double sideSquare,
                                                 double underSideSquareOrUnderRadius,
                                                 double overSideSquareOrOverRadius,
                                                 double axisSquare,
                                                 double betaAngle,
                                                 double alfaAngle,
                                                 double underDiagonal,
                                                 double overDiagonal
        ) {
            DecimalFormat df = new DecimalFormat("###.###");

            String heightOrFormattingString = isHeight ?
                    "Высота усеченного конуса h: " + df.format(heightOrForming) :
                    "Образующая усеченного конуса l: " + df.format(heightOrForming);

            String underRadiusOrUnderSquareString = isRadius ?
                    "Радиус нижнего оснонвания R: " + df.format(underSideSquareOrUnderRadius) :
                    "Площадь нижнего основания S: " + df.format(underSideSquareOrUnderRadius);

            String overRadiusOrOverSquareString = isRadius ?
                    "Радиус верхнего основания r : " + df.format(overSideSquareOrOverRadius) :
                    "Площадь верхнего основания S: " + df.format(overSideSquareOrOverRadius);

            String[] resultFields = {
                    heightOrFormattingString,
                    "Объем усеченного конуса V: " + df.format(volume),
                    "Площадь усеченного конуса S: " + df.format(allSquare),
                    "Площадь боковой поверхности S: " + df.format(sideSquare),
                    underRadiusOrUnderSquareString,
                    overRadiusOrOverSquareString,
                    "Площадь осевого сечения S: " + df.format(axisSquare),
                    "Угол наклона образующей α: " + df.format(alfaAngle),
                    "Угол наклона образующей β: " + df.format(betaAngle),
                    "Диагональ нижнего основания d: " + df.format(underDiagonal),
                    "Диагональ верхнего основания d: " + df.format(overDiagonal)
            };
            String resultFieldsWithSpace = String.join("<br/>", resultFields);
            resultCalculateFields.setText("<html>" + resultFieldsWithSpace + "</html>");
            calculatePanel.add(resultCalculateFields);
        }
    }
}