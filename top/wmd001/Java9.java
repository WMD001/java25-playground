package wmd001;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Java 9引入了许多新特性和改进，以下是一些主要的特性：
 * 1. 模块系统（Project Jigsaw）：Java 9引入了模块系统，使得开发者可以更好地组织和管理代码。模块系统允许开发者将代码分成不同的模块，并定义模块之间的依赖关系。这有助于提高代码的可维护性和安全性。
 * 2. JShell：Java 9引入了JShell，这是一个交互式的命令行工具，允许开发者在不编写完整的类和方法的情况下测试和运行Java代码。JShell非常适合用于学习、实验和快速原型开发。
 * 3. 改进的Stream API：Java 9对Stream API进行了改进，增加了新的方法，如takeWhile、dropWhile和iterate。这些方法使得开发者可以更方便地处理有序的Stream，并进行条件过滤和分割操作。
 * 4. 私有接口方法：Java 9允许在接口中定义私有方法，这些方法只能在接口内部使用。这有助于减少代码重复，并提高接口的封装性。
 * 5. 改进的集合工厂方法：Java 9引入了新的集合工厂方法，如List.of、Set.of和Map.of，这些方法提供了一种更简洁的方式来创建不可变的集合。这些方法可以接受任意数量的元素，并返回一个包含这些元素的不可变集合。
 * 6. 改进的Process API：Java 9对Process API进行了改进，增加了新的方法，如ProcessHandle和ProcessBuilder。这些方法使得开发者可以更方便地管理和控制外部进程，并获取有关进程的信息。
 * 7. 改进的HTTP客户端：Java 9引入了新的HTTP客户端API，提供了更现代化和功能丰富的方式来处理HTTP请求和响应。新的HTTP客户端支持HTTP/2协议，并提供了更好的异步编程模型。
 * 8. 改进的垃圾回收器：Java 9将G1设为默认垃圾回收器（替代Parallel GC），并引入了新的垃圾回收器接口，为后续ZGC等新GC奠定了基础。
 * 9. 改进的编译器和工具：Java 9对编译器和工具进行了改进，增加了新的功能和优化。例如，javac编译器现在支持模块系统，并且可以更好地处理模块之间的依赖关系。此外，Java 9还引入了新的工具，如jdeps和jlink，用于分析和优化模块依赖关系。
 * 总的来说，Java 9引入了许多新特性和改进，使得开发者可以更好地组织代码、提高性能，并提供了更现代化的编程模型。这些特性使得Java 9成为一个重要的版本，为开发者提供了更多的工具和功能来构建高质量的应用程序。
 * @author WYQ
 * @since 2026/6/3
 */
public class Java9 {

    /**
     * 集合工厂方法是Java 9引入的便捷API，用于快速创建不可变集合。
     * List.of()、Set.of()、Map.of() 提供了简洁的不可变集合创建方式，
     * 替代了之前的 Collections.unmodifiableList(Arrays.asList(...)) 写法。
     * 创建的集合是不可变的，不能添加、删除或修改元素。
     */
    public static void testCollectionFactories() {
        // List.of() - 创建不可变List
        List<String> names = List.of("Alice", "Bob", "Charlie");
        System.out.println("List.of: " + names);
        // names.add("Dave"); // 抛出 UnsupportedOperationException

        // Set.of() - 创建不可变Set
        Set<Integer> numbers = Set.of(1, 2, 3, 4, 5);
        System.out.println("Set.of: " + numbers);
        // Set.of() 不允许重复元素，否则抛出 IllegalArgumentException

        // Map.of() - 创建不可变Map（最多10个键值对）
        Map<String, Integer> scores = Map.of("Alice", 95, "Bob", 87, "Charlie", 92);
        System.out.println("Map.of: " + scores);

        // Map.ofEntries() - 创建不可变Map（不限键值对数量）
        Map<String, Integer> moreScores = Map.ofEntries(
                Map.entry("Dave", 78),
                Map.entry("Eve", 88),
                Map.entry("Frank", 95)
        );
        System.out.println("Map.ofEntries: " + moreScores);
    }

    /**
     * takeWhile方法是Java 9引入的Stream API中的一个方法，用于从Stream的开头开始，返回满足给定条件的连续元素，直到遇到第一个不满足条件的元素为止。dropWhile方法则相反，它会丢弃满足条件的连续元素，直到遇到第一个不满足条件的元素为止。
     * 这两个方法常用于处理有序的Stream，可以帮助我们更方便地进行条件过滤和分割操作。
     */
    public static void testWhile() {
        Stream.of(1, 2, 3, 4, 5, 4, 3, 2, 1).filter(i -> i < 4).forEach(System.out::print);
        System.out.println();
        Stream.of(1, 2, 3, 4, 5, 4, 3, 2, 1).takeWhile(i -> i < 4).forEach(System.out::print);
        System.out.println();
        Stream.of(1, 2, 3, 4, 5, 4, 3, 2, 1).dropWhile(i -> i < 4).forEach(System.out::print);
    }

    public interface MyInterface {
        default void method1() {
            System.out.println("Default implementation of method1");
        }
        private void method2() {
            System.out.println("Private method2");
        }
        static void method3() {
            System.out.println("Static method3");
        }
    }

    /**
     * Stream API改进：Java 9为Stream新增了多个实用方法。
     * Stream.ofNullable() - 安全地创建可能为null的Stream，避免NPE。
     * Stream.iterate() 新增重载 - 支持谓词条件的迭代，替代传统的for循环。
     */
    public static void testStreamImprovements() {
        // Stream.ofNullable() - null安全的Stream创建
        Stream<String> nonNull = Stream.ofNullable("Hello");
        Stream<String> nullStream = Stream.ofNullable(null); // 返回空Stream
        System.out.println("ofNullable非空元素数: " + nonNull.count()); // 1
        System.out.println("ofNullable空元素数: " + nullStream.count()); // 0

        // Stream.iterate() 新重载 - 带谓词的迭代
        // Java 8: iterate需要配合limit使用
        // Java 9: iterate新增第二个参数（谓词），满足条件才继续迭代
        System.out.print("iterate with predicate: ");
        Stream.iterate(1, i -> i <= 10, i -> i + 1)
              .forEach(i -> System.out.print(i + " "));
        System.out.println();
        // 等价于 for (int i = 1; i <= 10; i++)
    }

    public static void testHttpClient() {
        // Java 9引入了新的HTTP客户端API（孵化模块jdk.incubator.httpclient），
        // Java 11将其正式标准化到java.net.http包中。
        // 支持HTTP/2协议和异步编程模型。
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.example.com/data"))
                .build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(System.out::println)
                .join();
    }

    public static void main(String[] args) {
        System.out.println("=== takeWhile / dropWhile ===");
        testWhile();

        System.out.println("\n=== 集合工厂方法 ===");
        testCollectionFactories();

        System.out.println("\n=== Stream 改进 ===");
        testStreamImprovements();

        System.out.println("\n=== 接口私有方法 ===");
        MyInterface.method3();
        System.out.println("接口私有方法method2()只能在接口内部调用，外部无法访问");

        System.out.println("\n=== HTTP Client (孵化模块) ===");
        System.out.println("HTTP Client在Java 9为孵化模块，Java 11正式标准化");
    }

}
