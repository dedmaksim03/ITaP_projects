package Lab_4_5_6;

// Комплексные числа
public class Complex {
    private double real;  // Реальная часть
    private double im;  // Мнимая

    public Complex(double real, double im){
        this.real = real;
        this.im = im;
    }

    public double module_sq(){
        return (this.real*this.real + this.im*this.im);
    } // Модуль числа в квадрате

    public Complex square(){
        return new Complex(real*real - im*im, 2*real*im);
    } // Квадрат числа

    public Complex add(Complex cm){
        return new Complex(this.real+cm.real, this.im+cm.im);
    } // Сложение двух чисел

    public Complex conjugation(){
        return new Complex(this.real, this.im*(-1));
    }  // Сопряженное числа

    public double getIm() {
        return im;
    }

    public double getReal() {
        return real;
    }
}
