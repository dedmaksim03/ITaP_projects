package Lab_4;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;

public class FractalExplorer {

    public static void main(String[] args){
        FractalExplorer fractalExplorer = new FractalExplorer(700);
        fractalExplorer.createAndShowGUI();
        fractalExplorer.drawFractal();
    }

    private int size;  // Размер окна
    private JImageDisplay display;  // Переменная для отображения изображения
    private FractalGenerator fractal;  // Фрактал, переменная с типом базового класса
    private Rectangle2D.Double rectangle; // Область, в которой будет рисоваться фрактал

    public FractalExplorer(int size){
        this.size = size;
        this.display = new JImageDisplay(size, size);
        this.rectangle = new Rectangle2D.Double();
        this.fractal = new Mandelbrot();  // Создаем ссылку на объект Mandelbrot
        fractal.getInitialRange(rectangle);  // Задаем область фрактала
    }

    public void createAndShowGUI(){
        JFrame frame = new JFrame();  // Графический интерфейс
        JButton button = new JButton("recreate");  // Кнопка сброса

        frame.add(display, BorderLayout.CENTER);  // Добавляем к графическому интерфейсу изображение по центру
        frame.add(button, BorderLayout.SOUTH);  // Добавляем к графическому интерфейсу кнопку сброса снизу
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);  // При закрытии окна останавливаем программу

        ListenerButton listenerButton = new ListenerButton();  // Работоспособность кнопки
        button.addActionListener(listenerButton); // Добавляем обработчик события для кнопки

        ListenerMouse listenerMouse = new ListenerMouse();
        frame.addMouseListener(listenerMouse);  // Добавляем обработчик события для нажатия мыши

        // Стандартные операции
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }

    /* Рисует фрактал мандельброта. Перебираем все пиксели изображения, переводим каждый в комплексную форму,
    * подаем в fractal. Если точка не выходит за границы фрактала - красим пиксель в черный,
    * если выходит - красим по формуле*/
    private void drawFractal(){
        for (int x=0; x<size; x++){
            for (int y=0; y<size; y++){
                double xCoord = FractalGenerator.getCoord(rectangle.x, rectangle.x + rectangle.width, size, x);
                double yCoord = FractalGenerator.getCoord(rectangle.y, rectangle.y + rectangle.height, size, y);
                if (fractal.numIterations(xCoord,yCoord) == -1){
                    display.drawPixel(x,y,0);
                }
                else{
                    float hue = 0.7f + (float) fractal.numIterations(xCoord,yCoord)/ 200f;
                    int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
                    display.drawPixel(x,y,rgbColor);
                }
            }
        }
        display.repaint();  // Обновляем изображение
    }

    // Вспомогательный класс для обработки нажатия на кнопку
    private class ListenerButton implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent event){
            fractal.getInitialRange(rectangle);
            drawFractal();
        }
    }

    // Вспомогательный класс для обработки нажатия мыши по экрану и увеличении изображения в точке клика
    private class ListenerMouse extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent event) {
            double x = FractalGenerator.getCoord(rectangle.x, rectangle.x + rectangle.width,size, event.getX());
            double y = FractalGenerator.getCoord(rectangle.y, rectangle.y + rectangle.height,size, event.getY());
            fractal.recenterAndZoomRange(rectangle, x, y, 0.5);
            drawFractal();
        }
    }

}
