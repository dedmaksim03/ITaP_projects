package Lab_4_5_6;

import Lab_4_5_6.Fractals.BurningShip;
import Lab_4_5_6.Fractals.Mandelbrot;
import Lab_4_5_6.Fractals.Tricorn;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.Objects;

public class FractalExplorer {

    public static void main(String[] args){
        FractalExplorer fractalExplorer = new FractalExplorer(700);
        fractalExplorer.createAndShowGUI();
        fractalExplorer.drawFractal();
    }

    // Вспомогательный класс для обработки нажатия на кнопку
    private class ListenerButton implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent event){
            switch (event.getActionCommand()) {
                case ("Reset") -> {  // Для кнопки сброса
                    nowFractal.getInitialRange(rectangle);  // Обновляем область фрактала
                    drawFractal();  // Рисуем фрактал
                }
                case ("Save") -> { // Для кнопки сохранения
                    JFileChooser jFileChooser = new JFileChooser();  // Объект вспомогательного окна
                    FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG Images", ".png");
                    jFileChooser.setFileFilter(filter); // Устанавливаем фильтр - только на png
                    jFileChooser.setAcceptAllFileFilterUsed(false);  // Запрещаем выбирать другой тип файла
                    int result = jFileChooser.showSaveDialog(display); // Возвращает значение окна сохранения
                    if (result == JFileChooser.APPROVE_OPTION) {  // Если окно не закрыто
                        File file = new File((jFileChooser.getSelectedFile()).toString() + ".png");  // Создаем файл
                        try {
                            ImageIO.write(display.image, "png", file);  // Записываем его
                        } catch (Exception e) {  // Обрабатываем возможное исключение
                            JOptionPane.showMessageDialog(display, e.getMessage(), "Нельзя сохранить",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        }
    }

    // Вспомогательный класс для обработчика выбора фракталов
    private class ListenerBox implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent event){
            switch ((String) Objects.requireNonNull(jComboBox.getSelectedItem())) {
                case ("Tricorn") -> nowFractal = new Tricorn();
                case ("Mandelbrot") -> nowFractal = new Mandelbrot();
                case ("BurningShip") -> nowFractal = new BurningShip();
            }
            nowFractal.getInitialRange(rectangle);
            drawFractal();
        }
    }


    // Вспомогательный класс для обработки нажатия мыши по экрану и увеличении изображения в точке клика
    private class ListenerMouse extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent event) {
            double x = FractalGenerator.getCoord(rectangle.x, rectangle.x + rectangle.width,size, event.getX());
            double y = FractalGenerator.getCoord(rectangle.y, rectangle.y + rectangle.height,size, event.getY());
            nowFractal.recenterAndZoomRange(rectangle, x, y, 0.5);
            drawFractal();
        }
    }

    // Класс для вычислений в другом потоке
    private class FractalWorker extends SwingWorker<Object, Object>{
        int y;  // у-координата
        int[] colors;  // Список цветов на строке под номером y
        public FractalWorker(int y){
            this.y = y;
        }

        @Override
        public Object doInBackground(){  // Метод работающий в фоновом потоке, вычисляет цвет пикселей и записывает в массив
            colors = new int[size];
            /* Перебираем все пиксели изображения, переводим каждый в комплексную форму,
             * подаем в фрактал. Если точка не выходит за границы фрактала - красим пиксель в черный,
             * если выходит - красим по формуле*/
            for (int x=0; x<size; x++){
                double xCoord = FractalGenerator.getCoord(rectangle.x, rectangle.x + rectangle.width, size, x);
                double yCoord = FractalGenerator.getCoord(rectangle.y, rectangle.y + rectangle.height, size, y);
                if (nowFractal.numIterations(xCoord,yCoord) == -1){
                    colors[x] = 0;
                }
                else{
                    float hue = 0.7f + (float) nowFractal.numIterations(xCoord,yCoord)/ 200f;
                    int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
                    colors[x] = rgbColor;
                }
            }
            return null;
        }

        @Override
        public void done(){  // Работает после фонового процесса, перебирает массив цветов, и рисует пиксели
            rowsRemaining -= 1;
            if (rowsRemaining == 0) enableUI(true);
            for (int x=0; x<size; x++){
                display.drawPixel(x,y,colors[x]);
            }
            display.repaint(0,0,y,size,1);  // Обновляем строку y
        }
    }

    private int size;  // Размер окна
    private JImageDisplay display;  // Переменная для отображения изображения
    private FractalGenerator nowFractal;  // Отображаемый фрактал
    private Rectangle2D.Double rectangle; // Область, в которой будет рисоваться фрактал
    private JComboBox<String> jComboBox;  // Блок выбора фрактала
    private int rowsRemaining = 0;  // Количество не прорисованных строк
    JButton buttonReset; // Кнопка сброса
    JButton buttonSave;  // Кнопка сохранения картинки

    public FractalExplorer(int size){
        this.size = size;
        display = new JImageDisplay(size, size);  // Создаем новый объект отображения
        rectangle = new Rectangle2D.Double();  // Инициализируем начальную область фрактала
        nowFractal = new Mandelbrot();  // Инициализируем начальный фрактал (потом мы будем менять ссылку)
        nowFractal.getInitialRange(rectangle);  // Задаем область фрактала
        jComboBox = new JComboBox<>(); // Выбор фрактала
    }

    // Создает и выводит графический интерфейс
    public void createAndShowGUI(){
        JFrame frame = new JFrame();  // Графический интерфейс

        jComboBox.addItem(new Mandelbrot().toString());  // Добавляем имеющиеся фракталы в блок выбора фрактала
        jComboBox.addItem(new Tricorn().toString());
        jComboBox.addItem(new BurningShip().toString());
        ListenerBox listenerBox = new ListenerBox(); // Создаем слушатель
        jComboBox.addActionListener(listenerBox);  // Вешаем слушатель на блок выбора фракатал

        buttonReset = new JButton("Reset");
        buttonSave = new JButton("Save image");
        buttonReset.setActionCommand("Reset");  // Устанавливаем команды, для различия выполняемых кнопками действий
        buttonSave.setActionCommand("Save");
        ListenerButton listenerButton = new ListenerButton();  // Слушатель кнопок
        buttonReset.addActionListener(listenerButton); // Добавляем слушатели для кнопок
        buttonSave.addActionListener(listenerButton);

        ListenerMouse listenerMouse = new ListenerMouse();
        frame.addMouseListener(listenerMouse);  // Добавляем слушатель для нажатия мыши

        JLabel jLabel = new JLabel("Fractal"); // Текст на экране
        JPanel jPanelNorth = new JPanel();  // Верхняя панель с элементами
        JPanel jPanelSouth = new JPanel();  // Нижняя панель с элементами

        jPanelNorth.add(jLabel);  // Добавляем текст
        jPanelNorth.add(jComboBox);  // Добавляем блок выбора фрактала
        jPanelSouth.add(buttonReset);  // Добавляем кнопку сброса
        jPanelSouth.add(buttonSave);  // Добавляем кнопку сохранения
        frame.add(jPanelNorth, BorderLayout.NORTH);  // Добавляем панели на экран
        frame.add(jPanelSouth, BorderLayout.SOUTH);
        frame.add(display, BorderLayout.CENTER);  // Добавляем к графическому интерфейсу изображение по центру

        // Стандартные операции
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);  // При закрытии окна останавливаем программу
        frame.setTitle("Мое первое приложение на Java");
    }

    // Рисует фрактал в фоновом потоке
    private void drawFractal(){
        enableUI(false);
        rowsRemaining = size;
        for (int y=0; y<size; y++){
            FractalWorker fractalWorker = new FractalWorker(y);
            fractalWorker.execute();
        }
    }

    // Включает и отключает UI
    private void enableUI(boolean val){
        buttonReset.setEnabled(val);
        buttonSave.setEnabled(val);
        jComboBox.setEnabled(val);
    }
}
