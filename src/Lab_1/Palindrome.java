package Lab_1;

// Класс определяет, является ли заданная строка палиндромом
public class Palindrome {
    public static void main(String[] args){
        for (int i=0; i<args.length; i++){  // Прием строки из консоли
            String s = args[i];
            if (isPalindrome(s)) {
                System.out.print("is palindrome ");
            }
            else{
                System.out.print("is not palindrome ");
            }
        }
    }

    // Переворачивает строку, введенную на вход
    public static String reserveString(String s){
        String finalString = "";
        for (int i=s.length(); i>0; i--){
            finalString += s.charAt(i-1);
        }
        return finalString;
    }

    // Возвращает true, если поданная на вход строка является палиндромом
    public static Boolean isPalindrome(String s){
        String reserveS = reserveString(s);
        return s.equals(reserveS);
    }
}
