package Lab_4_5_6.Fractals;

import Lab_4_5_6.Complex;
import Lab_4_5_6.FractalGenerator;

import java.awt.geom.Rectangle2D;

public class BurningShip extends FractalGenerator {
    public static final int MAX_ITERATIONS = 2000; // Предельное количество итераций

    @Override
    public void getInitialRange(Rectangle2D.Double range){  // Определеяет размер фрактала и его координаты
        range.x = -2;
        range.y = -2.5;
        range.width = 4;
        range.height = 4;
    }

    @Override
    public int numIterations(double real, double im){  // реализует итеративную функцию для фрактала Мандельброта
        Complex z = new Complex(0, 0);
        Complex c = new Complex(real, im);
        int i = 0;
        while (z.module_sq() <= 4 && i < MAX_ITERATIONS){
            z = new Complex(Math.abs(z.getReal()), Math.abs(z.getIm()));
            z = z.square().add(c);  // Функция фрактала (|Re(z)| + i|Im(z)|)^2 + c
            i++;
        }
        if (i == MAX_ITERATIONS) return -1;
        return i;
    }

    public String toString(){
        return "BurningShip";
    }
}
