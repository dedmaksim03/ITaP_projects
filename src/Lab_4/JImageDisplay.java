package Lab_4;

import java.awt.*;
import javax.swing.*;
import java.awt.image.BufferedImage;

public class JImageDisplay extends JComponent{
    private BufferedImage image; // Управляет изображением
    private int width;
    private int height;

    public JImageDisplay(int width, int height){  // Задается ширина и высота изображения
        super();
        this.width = width;
        this.height = height;

        /* Создаем объект по заданным параметрам и типом изображения RGB - красные, зеленые и синие
        компоненты имеют по 8 битов */
        image = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);

        // Передаем параметры ширины и высоты для отображения на экране изображения
        Dimension dimension = new Dimension(this.width, this.height);
        super.setPreferredSize(dimension);
    }

    // Отрисовка изображения
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(image,0,0,image.getWidth(),image.getHeight(),null);
    }

    // Устанавливает все пиксели изображения в черный цвет
    public void clearImage(){
        for (int i=0; i< width; i++){
            for (int j=0; j< height; j++){
                image.setRGB(i,j,0);
            }
        }
    }

    // Устанавливает пиксель в определенный цвет
    public void drawPixel(int x, int y, int rgbColor){
        image.setRGB(x, y, rgbColor);
    }
}
