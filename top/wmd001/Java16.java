package wmd001;

import java.util.List;
import java.util.stream.Stream;

/**
 * Java 16 主要语法变更（2021年3月发布）：
 * 1. Records（正式特性）：record关键字从预览转为正式，用于定义不可变数据类，自动生成构造器、equals、hashCode和toString方法。
 * 2. instanceof模式匹配（正式特性）：模式匹配从预览转为正式，可以在instanceof判断的同时进行类型转换和变量绑定。
 * 3. Stream.toList()方法：新增便捷方法，直接将Stream转换为不可变List，替代collect(Collectors.toList())。
 * 4. Stream.mapMulti方法：新增flatMap的替代方法，通过Consumer逐个输出元素，性能更优。
 * @author WYQ
 * @since 2026/6/3
 */
public class Java16 {

    // ==================== 1. Records（正式特性） ====================
    // Record是一种特殊的类，用于定义不可变数据载体
    // 自动生成：全参构造器、getter方法、equals、hashCode、toString
    record Point(int x, int y) {}

    // Record可以包含方法
    record Color(int red, int green, int blue) {
        // 紧凑构造器（用于参数校验）
        Color {
            if (red < 0 || red > 255 || green < 0 || green > 255 || blue < 0 || blue > 255) {
                throw new IllegalArgumentException("颜色值必须在0-255之间");
            }
        }

        // Record中可以定义自定义方法
        String toHex() {
            return String.format("#%02x%02x%02x", red, green, blue);
        }
    }

    // Record可以实现接口
    record Student(String name, int age, double score) implements Comparable<Student> {
        @Override
        public int compareTo(Student other) {
            return Double.compare(this.score, other.score);
        }
    }

    public void demonstrateRecords() {
        // 使用record创建实例
        Point point = new Point(10, 20);
        System.out.println("Point: " + point); // 自动生成的toString: Point[x=10, y=20]
        System.out.println("X: " + point.x()); // 自动生成的getter方法
        System.out.println("Y: " + point.y());

        // Record的equals和hashCode是自动生成的
        Point point2 = new Point(10, 20);
        System.out.println("两个Point是否相等: " + point.equals(point2)); // true

        // 使用带校验的record
        Color red = new Color(255, 0, 0);
        System.out.println("红色的十六进制: " + red.toHex()); // #ff0000

        // 使用实现接口的record
        List<Student> students = List.of(
            new Student("Alice", 20, 95.5),
            new Student("Bob", 21, 88.0),
            new Student("Charlie", 19, 92.3)
        );
        students.stream().sorted().forEach(s ->
            System.out.println(s.name() + ": " + s.score())
        );
    }

    // ==================== 2. instanceof模式匹配（正式特性） ====================
    // 传统instanceof需要先判断再强制转换，模式匹配简化了这一过程
    public void demonstratePatternMatching() {
        Object obj = "Hello, Java 16!";

        // Java 16之前的方式（繁琐）
        if (obj instanceof String) {
            String s = (String) obj; // 需要显式强制转换
            System.out.println("字符串长度: " + s.length());
        }

        // Java 16模式匹配方式（简洁）
        // 在instanceof判断的同时进行类型转换和变量绑定
        if (obj instanceof String s) {
            // 变量s已经自动绑定为String类型，可以直接使用
            System.out.println("字符串长度（模式匹配）: " + s.length());
            System.out.println("转大写: " + s.toUpperCase());
        }

        // 模式变量可以在条件表达式中使用
        Object num = 42;
        if (num instanceof Integer i && i > 0) {
            System.out.println("正整数: " + i);
        }

        // 模式变量的作用域是正确的
        Object data = List.of(1, 2, 3);
        if (data instanceof List<?> list && !list.isEmpty()) {
            System.out.println("列表大小: " + list.size());
        }
    }

    // ==================== 3. Stream.toList()方法 ====================
    // 提供更简洁的Stream到List转换方式
    public void demonstrateStreamToList() {
        List<String> names = List.of("Alice", "Bob", "Charlie", "David");

        // Java 16之前的方式
        List<String> upperBefore = names.stream()
            .map(String::toUpperCase)
            .collect(java.util.stream.Collectors.toList()); // 需要使用Collectors

        // Java 16的新方式 - 更简洁
        List<String> upperAfter = names.stream()
            .map(String::toUpperCase)
            .toList(); // 直接使用toList()方法

        System.out.println("转换前: " + names);
        System.out.println("转换后: " + upperAfter);

        // 注意：toList()返回的是不可变List，不能进行add/remove操作
        // 如果需要可变List，仍需使用collect(Collectors.toList())
        try {
            upperAfter.add("Eve"); // 会抛出UnsupportedOperationException
        } catch (UnsupportedOperationException e) {
            System.out.println("toList()返回的List是不可变的: " + e.getMessage());
        }

        // 数值流也可以使用toList()
        List<Integer> numbers = Stream.of(1, 2, 3, 4, 5)
            .map(n -> n * n)
            .toList();
        System.out.println("平方数: " + numbers);
    }

    // ==================== 4. Stream.mapMulti方法 ====================
    // mapMulti是flatMap的替代方案，通过Consumer逐个输出元素
    // 对于一对一映射，性能优于flatMap
    public void demonstrateMapMulti() {
        List<Integer> numbers = List.of(1, 2, 3, 4, 5);

        // 使用flatMap展开嵌套结构
        List<Integer> flatMapped = numbers.stream()
            .flatMap(n -> Stream.of(n, n * 10)) // 每个数字生成两个值
            .toList();

        // 使用mapMulti实现相同功能
        // mapMulti通过Consumer逐个输出元素，避免创建中间Stream
        List<Integer> mapMultiResult = numbers.stream()
            .<Integer>mapMulti((n, consumer) -> {
                consumer.accept(n);      // 输出原值
                consumer.accept(n * 10); // 输出10倍值
            })
            .toList();

        System.out.println("flatMap结果: " + flatMapped);
        System.out.println("mapMulti结果: " + mapMultiResult);

        // mapMulti在处理Optional时特别有用
        List<String> texts = List.of("1", "abc", "3", "xyz", "5");

        // 使用flatMap处理Optional
        List<Integer> parsedFlatMap = texts.stream()
            .flatMap(text -> {
                try {
                    return Stream.of(Integer.parseInt(text));
                } catch (NumberFormatException e) {
                    return Stream.empty();
                }
            })
            .toList();

        // 使用mapMulti处理Optional（更高效）
        List<Integer> parsedMapMulti = texts.stream()
            .<Integer>mapMulti((text, consumer) -> {
                try {
                    consumer.accept(Integer.parseInt(text));
                } catch (NumberFormatException e) {
                    // 忽略无法解析的值
                }
            })
            .toList();

        System.out.println("解析的数字（flatMap）: " + parsedFlatMap);
        System.out.println("解析的数字（mapMulti）: " + parsedMapMulti);
    }

    public static void main(String[] args) {
        Java16 demo = new Java16();

        System.out.println("=== Records演示 ===");
        demo.demonstrateRecords();

        System.out.println("\n=== instanceof模式匹配演示 ===");
        demo.demonstratePatternMatching();

        System.out.println("\n=== Stream.toList()演示 ===");
        demo.demonstrateStreamToList();

        System.out.println("\n=== Stream.mapMulti演示 ===");
        demo.demonstrateMapMulti();
    }
}
