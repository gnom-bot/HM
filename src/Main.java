import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        HashMap<String, Integer> balls = new HashMap<>();
        balls.put("yellow", 5);
        balls.put("red", 4);
        balls.put("blue", 2);
        System.out.println("В корзине красных шаров "+ balls.get("red")); //вызываем get по ключу для вывода значения
        System.out.println(balls.size());//проверяем количество пар ключ/значение
        balls.remove("yellow");//удаляем одну пару
        System.out.println(balls.size());//проверяем удаление

    }
}