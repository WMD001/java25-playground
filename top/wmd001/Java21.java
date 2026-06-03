package wmd001;

import java.util.List;
import java.util.SequencedCollection;

/**
 * Java 21 主要语法变更（LTS长期支持版本）：
 * 1. 虚拟线程（正式）：Java 21正式引入虚拟线程（Virtual Threads），这是Project Loom的核心成果。虚拟线程是轻量级线程，由JVM管理而非操作系统，可以轻松创建数百万个并发虚拟线程而不耗尽系统资源。虚拟线程在阻塞操作（如I/O）时会自动让出底层平台线程，极大提升了高并发场景下的吞吐量和资源利用效率。
 * 2. Record Patterns（正式）：Record Patterns允许在switch表达式和instanceof中对Record类型进行解构匹配，可以直接提取Record组件的值，使代码更加简洁和类型安全。
 * 3. switch模式匹配（预览）：switch表达式和语句支持模式匹配，可以在case标签中使用类型模式和守卫条件（when子句），使switch能够处理更复杂的类型判断逻辑。此功能需要 --enable-preview 开启。
 * 4. 序列化集合（Sequenced Collections）：引入了SequencedCollection、SequencedSet和SequencedMap接口，为有序集合提供了统一的首尾元素访问API（如getFirst()、getLast()、addFirst()、addLast()），填补了集合框架中缺乏统一有序访问方式的空白。
 * 5. 字符串模板（预览）：引入STR模板处理器，允许使用 STR."Hello \{name}" 的语法进行字符串插值，比传统的字符串拼接和String.format更加直观和安全。此功能需要 --enable-preview 开启。
 * 6. 未命名模式和变量（预览）：允许使用下划线 _ 表示不需要使用的变量或模式组件，提高代码可读性，减少无意义的命名。此功能需要 --enable-preview 开启。
 * 注意：Java 21是LTS（长期支持）版本，企业级开发推荐升级的目标版本。
 * @author WYQ
 * @since 2026/6/3
 */
public class Java21 {

    /**
     * Record类型定义，用于演示Record Patterns
     */
    record Point(int x, int y) {}

    record Line(Point start, Point end) {}

    /**
     * 虚拟线程（正式）：使用Thread.startVirtualThread启动虚拟线程
     * 虚拟线程是轻量级的，可以创建大量并发线程而不会耗尽系统资源
     */
    static void virtualThreads() {
        // 直接启动虚拟线程
        Thread.startVirtualThread(() -> {
            System.out.println("运行在虚拟线程中: " + Thread.currentThread());
        });

        // 使用虚拟线程执行器处理批量任务
        try (var executor = java.util.concurrent.Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < 1000; i++) {
                final int taskId = i;
                executor.submit(() -> {
                    System.out.println("虚拟线程任务: " + taskId);
                    return taskId;
                });
            }
        }
    }

    /**
     * Record Patterns（正式）：在instanceof中解构Record
     */
    static int extractX(Object obj) {
        // 使用Record Pattern直接解构Point
        if (obj instanceof Point(int x, int y)) {
            return x + y;
        }
        return 0;
    }

    /**
     * Record Patterns 嵌套解构
     */
    static String describeLine(Object obj) {
        // 嵌套Record Pattern，解构Line中的Point
        if (obj instanceof Line(Point(int x1, int y1), Point(int x2, int y2))) {
            return "从(%d,%d)到(%d,%d)".formatted(x1, y1, x2, y2);
        }
        return "未知";
    }

    /**
     * switch模式匹配（预览）：需要 --enable-preview
     * 在case标签中使用类型模式和守卫条件（when子句）
     */
    static String switchPatternMatching(Object obj) {
        // switch中使用模式匹配（预览特性，需 --enable-preview）
        // return switch (obj) {
        //     case Integer i when i > 0  -> "正整数: " + i;
        //     case Integer i             -> "非正整数: " + i;
        //     case String s              -> "字符串: " + s;
        //     case Point(int x, int y)   -> "坐标: (%d, %d)".formatted(x, y);
        //     default                    -> "未知类型";
        // };
        return "switch模式匹配为预览特性，需要 --enable-preview 编译运行";
    }

    /**
     * 序列化集合（Sequenced Collections）：统一的首尾元素访问API
     */
    static void sequencedCollections() {
        // List本身就是一个SequencedCollection
        SequencedCollection<String> names = new java.util.ArrayList<>(List.of("Alice", "Bob", "Charlie"));

        // 访问首尾元素
        String first = names.getFirst();  // Alice
        String last = names.getLast();    // Charlie

        // 在首尾添加元素
        names.addFirst("Zero");
        names.addLast("Dave");

        // 反转视图
        SequencedCollection<String> reversed = names.reversed();

        System.out.println("顺序: " + names);
        System.out.println("逆序: " + reversed);
        System.out.println("首元素: " + first + ", 尾元素: " + last);
    }

    /**
     * 字符串模板（预览）：需要 --enable-preview
     * 使用STR模板处理器进行字符串插值
     */
    static String stringTemplate(String name, int age) {
        // 字符串模板（预览特性，需 --enable-preview）
        // return STR."Hello, \{name}! You are \{age} years old.";
        return "字符串模板为预览特性，需要 --enable-preview。传统写法: Hello, " + name + "! You are " + age + " years old.";
    }

    /**
     * 未命名模式和变量（预览）：需要 --enable-preview
     * 使用下划线 _ 表示不需要使用的变量
     */
    static void unnamedPatterns(Point p) {
        // 未命名变量（预览特性，需 --enable-preview）
        // 只关心x坐标，y坐标用 _ 忽略
        // if (p instanceof Point(var x, _)) {
        //     System.out.println("x坐标: " + x);
        // }
        System.out.println("未命名模式和变量为预览特性，需要 --enable-preview");
    }

    public static void main(String[] args) {
        // 虚拟线程
        virtualThreads();

        // Record Patterns
        System.out.println("x+y = " + extractX(new Point(3, 4)));
        Line line = new Line(new Point(0, 0), new Point(5, 5));
        System.out.println("线段: " + describeLine(line));

        // 序列化集合
        sequencedCollections();

        // 字符串模板
        System.out.println(stringTemplate("Alice", 25));

        // 未命名模式和变量
        unnamedPatterns(new Point(10, 20));
    }
}
