package Lab_4;

public class Complex {
    private double real;
    private double im;

    public Complex(double real, double im){
        this.real = real;
        this.im = im;
    }

    public double module_sq(){
        return (this.real*this.real + this.im*this.im);
    }

    public Complex square(){
        return new Complex(real*real - im*im, 2*real*im);
    }

    public Complex add(Complex cm){
        return new Complex(this.real+cm.real, this.im+cm.im);
    }

}
