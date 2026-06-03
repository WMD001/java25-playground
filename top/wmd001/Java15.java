package wmd001;

/**
 * Java 15 主要语法变更（2020）：
 * 1. 文本块（正式特性） - """ 多行字符串""" 从预览转为正式
 * 2. Sealed Classes（预览特性） - sealed class Shape permits Circle, Rectangle {}
 * 3. Records（第二次预览） - record 关键字继续预览，增加改进
 * 4. instanceof 模式匹配（第二次预览）
 * 5. 隐藏类（Hidden Classes） - JVM 层面特性
 * 6. Edwards-Curve 数字签名算法（EdDSA）
 * 注意：Sealed Classes 是预览特性，需要用 --enable-preview 编译
 *
 * @author WYQ
 * @since 2026/6/3
 */
public class Java15 {

    // ==================== 1. 文本块（正式特性） ====================

    /**
     * 文本块从 Java 13/14 的预览特性正式转为标准特性。
     * 不再需要 --enable-preview 标志。
     *
     * 语法：
     * - 以 """ 开头（后面必须换行），以 """ 结尾
     * - 自动处理换行符
     * - 自动去除公共前导空白（re-indentation）
     * - 支持转义字符：\n（换行）、\t（制表符）、\s（空格）、\（续行）
     */
    public void textBlocks() {
        // JSON 示例
        String json = """
                {
                    "name": "Alice",
                    "age": 30,
                    "hobbies": ["reading", "coding"]
                }
                """;
        System.out.println("JSON:\n" + json);

        // HTML 示例
        String html = """
                <html>
                    <body>
                        <p>Hello, Java 15!</p>
                    </body>
                </html>
                """;
        System.out.println("HTML:\n" + html);

        // SQL 示例
        String sql = """
                SELECT u.id, u.name, u.email
                FROM users u
                JOIN orders o ON u.id = o.user_id
                WHERE u.age > 18
                ORDER BY u.name
                """;
        System.out.println("SQL:\n" + sql);

        // 使用转义字符
        String withEscapes = """
                Line 1\twith tab
                Line 2  with trailing spaces  \s
                Long line split across \\
                multiple source lines
                """;
        System.out.println("With escapes:\n" + withEscapes);
    }

    // ==================== 2. Sealed Classes（预览特性） ====================

    /**
     * 【预览特性 - 需要用 --enable-preview 编译】
     *
     * Sealed Classes（密封类）允许类的作者明确控制哪些类可以继承/实现它。
     * 这提供了更精确的类型层次结构建模能力。
     *
     * 语法：
     * sealed class ClassName permits SubClass1, SubClass2, SubClass3 {}
     *
     * 子类必须是以下之一：
     * - final：不能再被继承
     * - sealed：继续限制继承
     * - non-sealed：放弃限制（开放继承）
     *
     * 优势：
     * - 与 pattern matching（模式匹配）配合使用时，编译器可以检查穷举性
     * - 更精确地表达领域模型中的"封闭"概念
     * - 比枚举更灵活（子类可以有不同字段和行为）
     */

    // 使用传统方式模拟 sealed class 的效果
    // 因为 sealed class 是预览特性，我们用注释展示语法

    /*
    // Java 15 预览写法：
    sealed class Shape permits Circle, Rectangle, Triangle {}

    record Circle(double radius) implements Shape {
        public double area() { return Math.PI * radius * radius; }
    }

    record Rectangle(double width, double height) implements Shape {
        public double area() { return width * height; }
    }

    non-sealed class Triangle implements Shape {
        double base, height;
        Triangle(double base, double height) { this.base = base; this.height = height; }
        public double area() { return 0.5 * base * height; }
    }
    */

    // 使用传统抽象类模拟
    static abstract class ShapeSimulated {
        abstract double area();
        abstract String describe();
    }

    static final class CircleSimulated extends ShapeSimulated {
        private final double radius;
        CircleSimulated(double radius) { this.radius = radius; }
        @Override double area() { return Math.PI * radius * radius; }
        @Override String describe() { return "Circle[radius=" + radius + "]"; }
    }

    static final class RectangleSimulated extends ShapeSimulated {
        private final double width, height;
        RectangleSimulated(double w, double h) { this.width = w; this.height = h; }
        @Override double area() { return width * height; }
        @Override String describe() { return "Rectangle[" + width + "x" + height + "]"; }
    }

    static final class TriangleSimulated extends ShapeSimulated {
        private final double base, height;
        TriangleSimulated(double b, double h) { this.base = b; this.height = h; }
        @Override double area() { return 0.5 * base * height; }
        @Override String describe() { return "Triangle[base=" + base + ", height=" + height + "]"; }
    }

    public void sealedClassesDemo() {
        ShapeSimulated[] shapes = {
            new CircleSimulated(5),
            new RectangleSimulated(4, 6),
            new TriangleSimulated(3, 8)
        };

        for (ShapeSimulated shape : shapes) {
            System.out.printf("%s -> area = %.2f%n", shape.describe(), shape.area());
        }

        // Java 15 预览写法中，sealed class 的好处：
        // 当配合 switch 模式匹配使用时，编译器知道所有可能的子类
        // 可以确保 switch 处理了所有情况，无需 default 分支
        //
        // double area = switch (shape) {
        //     case Circle c -> Math.PI * c.radius() * c.radius();
        //     case Rectangle r -> r.width() * r.height();
        //     case Triangle t -> 0.5 * t.base() * t.height();
        //     // 不需要 default！编译器知道只有这三种子类
        // };
    }

    // ==================== 3. Records（第二次预览） ====================

    /**
     * Records 在 Java 14 作为第一次预览引入，Java 15 是第二次预览。
     * 主要改进：
     * - 允许在 record 中声明局部接口和其他类型
     * - 改进了规范化形式（canonical form）的相关规则
     * - 修正了与反射相关的规范
     *
     * record 将在 Java 16 中正式转为标准特性。
     */
    public void recordsDemo() {
        // Java 15 预览写法（注释中展示）：
        // record Employee(String name, String department, double salary) {
        //     // 紧凑构造器：验证参数
        //     Employee {
        //         if (salary < 0) throw new IllegalArgumentException("Salary must be non-negative");
        //     }
        //
        //     // 局部接口（Java 15 新增允许）
        //     interface Bonus {
        //         double calculate(Employee e);
        //     }
        //
        //     // 自定义方法
        //     public double annualSalary() { return salary * 12; }
        // }

        // 用传统类模拟 record 效果
        System.out.println("Records (second preview in Java 15) provide:");
        System.out.println("- Auto-generated: constructor, getters, equals, hashCode, toString");
        System.out.println("- Immutable data carrier classes");
        System.out.println("- Will become standard in Java 16");
    }

    // ==================== 4. instanceof 模式匹配（第二次预览） ====================

    /**
     * instanceof 模式匹配在 Java 14 作为第一次预览引入，Java 15 是第二次预览。
     * 将在 Java 16 中正式转为标准特性。
     *
     * 进一步改进了模式变量的作用域规则。
     */
    public void instanceofPatternMatching() {
        Object obj = "Hello, Java 15!";

        // Java 15 预览写法（注释中展示）：
        // if (obj instanceof String s && s.length() > 5) {
        //     System.out.println("Long string: " + s);
        // }

        // 传统写法模拟
        if (obj instanceof String) {
            String s = (String) obj;
            if (s.length() > 5) {
                System.out.println("Long string: " + s);
            }
        }
    }

    // ==================== 5. 隐藏类（Hidden Classes） ====================

    /**
     * Hidden Classes（隐藏类）是 JVM 层面的特性，主要面向框架开发者。
     *
     * 特点：
     * - 不能被其他类直接发现或链接（不出现在类路径中）
     * - 有自己的生命周期管理（支持更灵活的类卸载）
     * - 主要用于动态语言实现和框架（如 lambda 表达式、动态代理的内部实现）
     *
     * 使用 MethodHandles.Lookup.defineHiddenClass() 创建
     * 这不是日常应用开发使用的 API，但改进了 JVM 内部实现的效率。
     */
    public void hiddenClasses() {
        System.out.println("Hidden Classes are a JVM-level feature:");
        System.out.println("- Used internally by lambda expressions and dynamic proxies");
        System.out.println("- Framework-level feature, not for everyday use");
        System.out.println("- Enables better class lifecycle management");
        System.out.println("- Created via MethodHandles.Lookup.defineHiddenClass()");
    }

    // ==================== 6. 其他特性 ====================

    /**
     * Java 15 的其他重要特性：
     *
     * - Edwards-Curve 数字签名算法（EdDSA）
     *   实现了 Edwards-Curve Digital Signature Algorithm (EdDSA)
     *   Ed25519 和 Ed448 签名方案
     *
     * - Shenandoah 垃圾收集器转为产品特性
     *   低暂停时间的垃圾收集器
     *
     * - ZGC 转为产品特性
     *   超低延迟的垃圾收集器（实验性阶段在 Java 11 引入）
     *
     * - 移除 Nashorn JavaScript 引擎
     *
     * - DatagramSocket API 重构
     */
    public void otherFeatures() {
        System.out.println("Java 15 also includes:");
        System.out.println("- EdDSA (Ed25519/Ed448) digital signature algorithm");
        System.out.println("- Shenandoah GC as a product feature");
        System.out.println("- ZGC as a product feature");
        System.out.println("- Removal of Nashorn JavaScript engine");
    }

    // ==================== main ====================

    public static void main(String[] args) {
        Java15 demo = new Java15();

        System.out.println("=== 1. Text Blocks (Standard) ===");
        demo.textBlocks();

        System.out.println("\n=== 2. Sealed Classes (Preview) ===");
        demo.sealedClassesDemo();

        System.out.println("\n=== 3. Records (Second Preview) ===");
        demo.recordsDemo();

        System.out.println("\n=== 4. instanceof Pattern Matching (Second Preview) ===");
        demo.instanceofPatternMatching();

        System.out.println("\n=== 5. Hidden Classes ===");
        demo.hiddenClasses();

        System.out.println("\n=== 6. Other Features ===");
        demo.otherFeatures();
    }
}
