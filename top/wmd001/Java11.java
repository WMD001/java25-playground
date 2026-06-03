package wmd001;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Java 11 主要语法变更（2018 LTS）：
 * 1. var 可用于 lambda 表达式参数
 * 2. String 新增实用方法：isBlank、strip、repeat、lines
 * 3. HTTP Client API 正式标准化（java.net.http）
 * 4. 文件读写便捷方法（Files.readString / writeString）
 * 5. Collection.toArray(IntFunction) 新方法
 * 6. Predicate.not() 便捷否定方法
 * 注意：Java 11 的语法变更主要是 var 在 lambda 参数中的使用，其余为 API 增强
 *
 * @author WYQ
 * @since 2026/6/3
 */
public class Java11 {

    // ==================== 1. var 用于 lambda 参数 ====================

    /**
     * Java 10 引入了局部变量类型推断（var），但不允许在 lambda 参数中使用。
     * Java 11 放开了这一限制，允许用 var 声明 lambda 参数类型。
     *
     * 使用场景：当需要对 lambda 参数添加注解时，必须显式声明类型，
     * 而 var 让我们在不重复写类型的情况下依然可以添加注解。
     *
     * 规则：所有参数要么全部使用 var，要么全部不使用，不能混用。
     */
    public void varInLambda() {
        // Java 10 只能这样写：要么全写类型，要么全不写
        BiFunction<Integer, Integer, Integer> add = (Integer x, Integer y) -> x + y;

        // Java 11：可以用 var 声明 lambda 参数
        BiFunction<Integer, Integer, Integer> addWithVar = (var x, var y) -> x + y;

        // 注意：(var x, y) -> x + y 是非法的，要么全部用 var，要么全部不用
        System.out.println("var lambda: " + addWithVar.apply(3, 5));

        // var 在 lambda 中的实际价值：可以添加注解而无需写完整类型
        // 例如：(@NonNull var x, @NonNull var y) -> x + y
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
        names.stream()
                .map((var name) -> name.toUpperCase())
                .forEach(System.out::println);
    }

    // ==================== 2. String 新方法 ====================

    /**
     * Java 11 为 String 类新增了多个实用方法。
     * 注意：这些是 API 变更而非语法变更，但在实际开发中极为常用。
     */
    public void stringNewMethods() {
        // isBlank() - 判断字符串是否为空或只包含空白字符（比 isEmpty 更宽松）
        String empty = "";
        String blank = "   \t\n";
        System.out.println("\"\".isBlank() = " + empty.isBlank());       // true
        System.out.println("\"   \\t\\n\".isBlank() = " + blank.isBlank()); // true

        // strip() - 去除首尾空白字符（支持 Unicode 空白，比 trim() 更准确）
        // 另外还有 stripLeading() 和 stripTrailing()
        String padded = "  Hello World  ";
        System.out.println("strip: [" + padded.strip() + "]");               // [Hello World]
        System.out.println("stripLeading: [" + padded.stripLeading() + "]"); // [Hello World  ]
        System.out.println("stripTrailing: [" + padded.stripTrailing() + "]"); // [  Hello World]

        // repeat(int) - 将字符串重复 n 次
        String ha = "Ha";
        System.out.println("repeat: " + ha.repeat(3)); // HaHaHa
        String separator = "-".repeat(40);
        System.out.println(separator); // ----------------------------------------

        // lines() - 按行分割字符串，返回 Stream<String>
        String multiline = "Line 1\nLine 2\nLine 3";
        List<String> lineList = multiline.lines()
                .map(String::trim)
                .collect(Collectors.toList());
        System.out.println("lines: " + lineList); // [Line 1, Line 2, Line 3]
    }

    // ==================== 3. HTTP Client API 标准化 ====================

    /**
     * Java 9 引入了 HTTP Client（孵化模块 jdk.incubator.httpclient），
     * Java 11 将其正式标准化到 java.net.http 包中。
     *
     * 新的 HttpClient 替代了老旧的 HttpURLConnection，支持：
     * - 同步和异步请求
     * - HTTP/2 协议
     * - WebSocket
     * - 流式响应体处理
     */
    public void httpClientApi() {
        // 创建 HttpClient
        HttpClient client = HttpClient.newHttpClient();

        // 构建 GET 请求
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://httpbin.org/get"))
                .header("Accept", "application/json")
                .GET()
                .build();

        // 同步发送请求
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Status: " + response.statusCode());
            System.out.println("Body (first 200 chars): " + response.body().substring(0, Math.min(200, response.body().length())));
        } catch (Exception e) {
            System.out.println("HTTP request failed: " + e.getMessage());
        }

        // 异步发送请求（返回 CompletableFuture）
        // client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
        //       .thenApply(HttpResponse::body)
        //       .thenAccept(System.out::println)
        //       .join();
    }

    // ==================== 4. Collection.toArray ====================

    /**
     * Java 11 为 Collection 接口新增了 toArray(IntFunction) 方法，
     * 使得将集合转换为指定类型数组更加方便。
     */
    public void collectionToArray() {
        List<String> list = Arrays.asList("A", "B", "C");

        // Java 11 之前
        String[] oldWay = list.toArray(new String[0]);

        // Java 11：使用方法引用更简洁
        String[] newWay = list.toArray(String[]::new);

        System.out.println("toArray result: " + Arrays.toString(newWay));
    }

    // ==================== 5. Predicate.not ====================

    /**
     * Java 11 新增了 Predicate.not() 静态方法，
     * 用于更方便地对谓词取反，避免编写 x -> !condition 的 lambda。
     */
    public void predicateNot() {
        List<String> names = Arrays.asList("Alice", "", "Bob", "  ", "Charlie");

        // Java 11：Predicate.not() 更直观
        List<String> nonBlank = names.stream()
                .filter(Predicate.not(String::isBlank))
                .collect(Collectors.toList());
        System.out.println("Non-blank names: " + nonBlank);
    }

    // ==================== 6. Files 便捷方法 ====================

    /**
     * Java 11 为 Files 类新增了 readString 和 writeString 方法，
     * 让文件的读写操作更加简洁，无需繁琐的 InputStream/OutputStream 操作。
     * 这是 API 变更而非语法变更，但在实际开发中非常实用。
     */
    public void filesConvenience() {
        try {
            java.nio.file.Path path = java.nio.file.Files.createTempFile("java11-demo", ".txt");
            // 写入字符串到文件（一行代码，无需手动关闭流）
            java.nio.file.Files.writeString(path, "Hello, Java 11!\n这是Files.writeString写入的内容");
            // 从文件读取字符串（一行代码）
            String content = java.nio.file.Files.readString(path);
            System.out.println("文件内容: " + content);

            // 清理临时文件
            java.nio.file.Files.deleteIfExists(path);
        } catch (Exception e) {
            System.out.println("文件操作异常: " + e.getMessage());
        }
    }

    // ==================== main ====================

    public static void main(String[] args) {
        Java11 demo = new Java11();

        System.out.println("=== 1. var in Lambda ===");
        demo.varInLambda();

        System.out.println("\n=== 2. String New Methods ===");
        demo.stringNewMethods();

        System.out.println("\n=== 3. HTTP Client API ===");
        demo.httpClientApi();

        System.out.println("\n=== 4. Collection toArray ===");
        demo.collectionToArray();

        System.out.println("\n=== 5. Predicate.not ===");
        demo.predicateNot();

        System.out.println("\n=== 6. Files.readString / writeString ===");
        demo.filesConvenience();
    }
}
