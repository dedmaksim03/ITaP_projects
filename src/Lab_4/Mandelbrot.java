package Lab_4;

import java.awt.geom.Rectangle2D;

public class Mandelbrot extends FractalGenerator{
    public static final int MAX_ITERATIONS = 2000; // Предельное количество итераций

    @Override
    public void getInitialRange(Rectangle2D.Double range){  // Определеяет размер фрактала и его координаты
        range.x = -2;
        range.y = -1.5;
        range.width = 3;
        range.height = 3;
    }

    @Override
    public int numIterations(double real, double im){  // реализует итеративную функцию для фрактала Мандельброта
        Complex z = new Complex(0, 0);
        Complex c = new Complex(real, im);
        int i = 0;
        while (z.module_sq() <= 4 && i < MAX_ITERATIONS){
            z = z.square().add(c);  // Функция фрактала - z = z^2 + c
            i++;
        }
        if (i == MAX_ITERATIONS) return -1;
        return i;
    }
}
