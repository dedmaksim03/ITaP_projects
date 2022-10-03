// Класс определяет и выводит простые числа
public class Primes {
    // Метод перебирает числа от 2 до 100 и выводит простые из них
    public static void main(String[] args){
        for (int i=2; i<100; i++){
            if (isPrime(i)){
                System.out.print(i + " ");
            }

        }
    }

    // Метод получает на вход число и определяет, является ли оно простым
    public static boolean isPrime(int n){
        for (int i=2; i<n; i++){
            if (n % i == 0){
                return false;
            }
        }
        return true;
    }
}