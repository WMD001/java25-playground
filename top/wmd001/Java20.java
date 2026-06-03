package wmd001;

/**
 * Java 20 主要改进（2023年3月发布）：
 * 1. Record Patterns（第二次预览）：改进了Java 19的Record Patterns预览特性，增加了对泛型类型推断的支持。
 * 2. Switch模式匹配（第二次预览）：改进了Switch模式匹配预览特性，优化了语法和性能。
 * 3. 作用域值（预览）：ScopedValue，用于在特定作用域内传递不可变数据，替代ThreadLocal。
 * 4. 虚拟线程（第二次预览）：继续改进虚拟线程，接近正式发布。
 * 5. 没有重大新语法变更：Java 20主要是在预览特性的基础上进行改进和优化。
 * 注意：以上预览特性需要使用 --enable-preview 编译和运行。
 * @author WYQ
 * @since 2026/6/3
 */
public class Java20 {

    // ==================== Record Patterns（第二次预览） ====================
    // 需要 --enable-preview 编译
    // Java 20改进了Java 19的Record Patterns，增加了泛型类型推断支持

    record Pair<A, B>(A first, B second) {}
    record Point(int x, int y) {}
    record Box<T>(T value) {}

    /**
     * 演示Record Patterns的改进（第二次预览）
     * 需要 --enable-preview
     *
     * 新增特性：
     * - 泛型类型推断：可以在解构时省略泛型类型参数
     *
     * 语法示例：
     * // Java 19需要显式指定泛型类型
     * if (obj instanceof Pair<String, Integer>(var s, var i)) { ... }
     *
     * // Java 20支持类型推断，可以省略泛型类型
     * if (obj instanceof Pair(var s, var i)) { ... }
     *
     * // 嵌套解构
     * if (obj instanceof Box(Point(int x, int y))) { ... }
     */
    public void demonstrateRecordPatterns() {
        // Record Patterns泛型类型推断（第二次预览，需要 --enable-preview）
        Object pair = new Pair<>("Hello", 42);

        // Java 19: 需要显式指定泛型类型
        // if (pair instanceof Pair<String, Integer>(var first, var second)) {
        //     System.out.println("first=" + first + ", second=" + second);
        // }

        // Java 20: 支持类型推断
        // if (pair instanceof Pair(var first, var second)) {
        //     System.out.println("first=" + first + ", second=" + second);
        // }

        // 传统方式演示
        if (pair instanceof Pair p) {
            System.out.println("Pair: first=" + p.first() + ", second=" + p.second());
        }

        // 嵌套Record Patterns（预览）
        Object box = new Box(new Point(10, 20));
        // if (box instanceof Box(Point(int x, int y))) {
        //     System.out.println("Box中的点: x=" + x + ", y=" + y);
        // }

        if (box instanceof Box b && b.value() instanceof Point p) {
            System.out.println("Box中的点: x=" + p.x() + ", y=" + p.y());
        }
    }

    // ==================== Switch模式匹配（第二次预览） ====================
    // 需要 --enable-preview 编译
    // Java 20继续改进Switch模式匹配

    sealed interface Shape permits Circle, Rectangle {}
    record Circle(double radius) implements Shape {}
    record Rectangle(double width, double height) implements Shape {}

    /**
     * 使用Switch模式匹配（第二次预览）
     * 需要 --enable-preview
     *
     * 改进：
     * - 当case中使用了record patterns时，不允许再使用传统case
     * - 优化了模式匹配的性能
     *
     * 语法示例：
     * double area = switch (shape) {
     *     case Circle(var r) -> Math.PI * r * r;
     *     case Rectangle(var w, var h) -> w * h;
     * };
     */
    public double calculateArea(Shape shape) {
        // Switch模式匹配（第二次预览，需要 --enable-preview）
        // return switch (shape) {
        //     case Circle(var r) -> Math.PI * r * r;
        //     case Rectangle(var w, var h) -> w * h;
        // };

        // 传统方式演示
        if (shape instanceof Circle c) {
            return Math.PI * c.radius() * c.radius();
        } else if (shape instanceof Rectangle r) {
            return r.width() * r.height();
        }
        throw new IllegalArgumentException("未知形状");
    }

    // ==================== 作用域值（预览） ====================
    // 需要 --enable-preview 编译
    // ScopedValue用于在特定作用域内传递不可变数据
    // 是ThreadLocal的现代替代方案

    // 定义ScopedValue（预览特性）
    // import jdk.incubator.concurrent.ScopedValue;
    // private static final ScopedValue<String> CURRENT_USER = ScopedValue.newInstance();
    // private static final ScopedValue<Integer> REQUEST_ID = ScopedValue.newInstance();

    /**
     * 演示ScopedValue的使用（预览特性）
     * 需要 --enable-preview 和 jdk.incubator.concurrent 模块
     *
     * ScopedValue特点：
     * 1. 不可变：在作用域内不能修改值
     * 2. 自动清理：离开作用域后自动恢复
     * 3. 虚拟线程友好：专为虚拟线程设计
     * 4. 性能优秀：比ThreadLocal更高效
     *
     * 语法示例：
     * // 定义ScopedValue
     * private static final ScopedValue<String> USER = ScopedValue.newInstance();
     *
     * // 在作用域内绑定值
     * ScopedValue.runWhere(USER, "Alice", () -> {
     *     // 在这个作用域内，USER.get()返回"Alice"
     *     System.out.println("当前用户: " + USER.get());
     *
     *     // 嵌套作用域
     *     ScopedValue.runWhere(USER, "Bob", () -> {
     *         // 这里USER.get()返回"Bob"
     *         System.out.println("嵌套用户: " + USER.get());
     *     });
     *
     *     // 离开嵌套作用域后，USER.get()恢复为"Alice"
     *     System.out.println("恢复用户: " + USER.get());
     * });
     */
    public void demonstrateScopedValue() {
        // ScopedValue演示（预览特性，需要 --enable-preview）
        // ScopedValue<String> USER = ScopedValue.newInstance();
        //
        // ScopedValue.runWhere(USER, "Alice", () -> {
        //     System.out.println("用户: " + USER.get());
        //     processRequest();
        // });
        //
        // private void processRequest() {
        //     // 可以在任何地方获取当前用户
        //     String user = USER.get();
        //     System.out.println("处理请求: " + user);
        // }

        // 传统方式演示
        System.out.println("=== ScopedValue演示（预览特性）===");
        System.out.println("ScopedValue需要 --enable-preview 和 jdk.incubator.concurrent 模块");
        System.out.println("ScopedValue特点：");
        System.out.println("1. 不可变：在作用域内不能修改值");
        System.out.println("2. 自动清理：离开作用域后自动恢复");
        System.out.println("3. 虚拟线程友好：专为虚拟线程设计");
        System.out.println("4. 性能优秀：比ThreadLocal更高效");
        System.out.println("5. 替代ThreadLocal的现代方案");
    }

    // ==================== 虚拟线程（第二次预览） ====================
    // 需要 --enable-preview 编译
    // Java 20继续改进虚拟线程，接近正式发布（Java 21正式）

    /**
     * 演示虚拟线程的改进（第二次预览）
     * 需要 --enable-preview
     *
     * 改进：
     * - 性能优化
     * - 更好的诊断支持
     * - 与现有代码更好的兼容性
     */
    public void demonstrateVirtualThreads() {
        // 虚拟线程（第二次预览，需要 --enable-preview）
        // Thread.startVirtualThread(() -> {
        //     System.out.println("虚拟线程: " + Thread.currentThread());
        // });
        //
        // try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
        //     for (int i = 0; i < 100; i++) {
        //         final int taskId = i;
        //         executor.submit(() -> {
        //             System.out.println("任务" + taskId + ": " + Thread.currentThread());
        //         });
        //     }
        // }

        // 传统方式演示
        System.out.println("=== 虚拟线程演示（第二次预览）===");
        System.out.println("虚拟线程在Java 20中继续改进，Java 21正式发布");
        System.out.println("改进：");
        System.out.println("1. 性能优化");
        System.out.println("2. 更好的诊断支持");
        System.out.println("3. 与现有代码更好的兼容性");
    }

    public static void main(String[] args) {
        Java20 demo = new Java20();

        System.out.println("=== Record Patterns演示（第二次预览）===");
        demo.demonstrateRecordPatterns();

        System.out.println("\n=== Switch模式匹配演示（第二次预览）===");
        Shape circle = new Circle(5.0);
        Shape rect = new Rectangle(4.0, 6.0);
        System.out.println("圆形面积: " + demo.calculateArea(circle));
        System.out.println("矩形面积: " + demo.calculateArea(rect));

        System.out.println("\n=== ScopedValue演示（预览）===");
        demo.demonstrateScopedValue();

        System.out.println("\n=== 虚拟线程演示（第二次预览）===");
        demo.demonstrateVirtualThreads();

        System.out.println("\n注意：以上预览特性需要使用 --enable-preview 编译运行");
        System.out.println("编译：javac --enable-preview --source 20 Java20.java");
        System.out.println("运行：java --enable-preview wmd001.Java20");
    }
}
