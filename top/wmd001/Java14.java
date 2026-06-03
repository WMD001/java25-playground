package wmd001;

/**
 * Java 14 主要语法变更（2020）：
 * 1. Switch 表达式（正式特性） - 从预览转为正式，不再需要 --enable-preview
 * 2. Records（预览特性） - record Point(int x, int y) {} 简洁数据类
 * 3. instanceof 模式匹配（预览特性） - if (obj instanceof String s) { ... }
 * 4. NullPointerException 增强提示 - 明确指出是哪个变量为 null
 * 5. Helpful NullPointerExceptions（有用的空指针异常信息）
 * 注意：Records 和 instanceof 模式匹配是预览特性，需要用 --enable-preview 编译
 *
 * @author WYQ
 * @since 2026/6/3
 */
public class Java14 {

    // ==================== 1. Switch 表达式（正式特性） ====================

    /**
     * Switch 表达式从 Java 12/13 的预览特性正式转为标准特性。
     * 不再需要 --enable-preview 标志。
     *
     * 特点：
     * - 箭头语法（->）：无贯穿（no fall-through）
     * - 可作为表达式返回值
     * - 支持 yield 关键字在代码块中返回值
     * - 编译器强制要求穷举所有可能值（exhaustiveness）
     */
    public String getDayType(String day) {
        // 正式语法，无需 --enable-preview
        return switch (day) {
            case "Monday", "Tuesday", "Wednesday", "Thursday", "Friday" -> "Weekday";
            case "Saturday", "Sunday" -> "Weekend";
            default -> "Unknown";
        };
    }

    /**
     * 使用 yield 在代码块中返回值
     */
    public int getScore(String grade) {
        return switch (grade) {
            case "A" -> 90;
            case "B" -> {
                System.out.println("Good grade!");
                yield 80;
            }
            case "C" -> 70;
            case "D" -> 60;
            case "F" -> 0;
            default -> {
                System.out.println("Unknown grade: " + grade);
                yield -1;
            }
        };
    }

    /**
     * switch 表达式的穷举性检查：
     * 对于 enum 类型，编译器会确保所有枚举值都被处理。
     */
    enum Season { SPRING, SUMMER, AUTUMN, WINTER }

    public String describeSeason(Season season) {
        // 编译器会确保所有 Season 枚举值都有对应的 case
        return switch (season) {
            case SPRING -> "Warm and blooming";
            case SUMMER -> "Hot and sunny";
            case AUTUMN -> "Cool and colorful";
            case WINTER -> "Cold and snowy";
        };
        // 如果遗漏某个枚举值，编译器会报错
    }

    // ==================== 2. Records（预览特性） ====================

    /**
     * 【预览特性 - 需要用 --enable-preview 编译】
     *
     * Records 是一种新的类声明形式，用于创建不可变的数据载体类。
     * 它自动生成：构造方法、getter 方法、equals()、hashCode()、toString()。
     *
     * 语法：record RecordName(Type field1, Type field2) {}
     *
     * 限制：
     * - Record 类是隐式 final 的，不能被继承
     * - 不能声明实例字段（除了 record header 中的参数）
     * - 可以声明静态字段、方法、构造器
     */
    // record Point(int x, int y) {}
    //
    // record Range(int start, int end) {
    //     // 紧凑构造器（canonical constructor）：可以添加验证逻辑
    //     Range {
    //         if (start > end) {
    //             throw new IllegalArgumentException("start must be <= end");
    //         }
    //     }
    //
    //     // 自定义方法
    //     public int length() {
    //         return end - start;
    //     }
    //
    //     // 静态工厂方法
    //     public static Range of(int start, int end) {
    //         return new Range(start, end);
    //     }
    // }

    /**
     * 模拟 record 的效果：展示 Java 14 之前如何实现数据类
     */
    public static final class PointSimulated {
        private final int x;
        private final int y;

        public PointSimulated(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int x() { return x; }
        public int y() { return y; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof PointSimulated)) return false;
            PointSimulated that = (PointSimulated) o;
            return x == that.x && y == that.y;
        }

        @Override
        public int hashCode() {
            return 31 * x + y;
        }

        @Override
        public String toString() {
            return "Point[x=" + x + ", y=" + y + "]";
        }
    }

    public void recordsDemo() {
        // 使用模拟的 record 类
        PointSimulated p1 = new PointSimulated(3, 4);
        PointSimulated p2 = new PointSimulated(3, 4);
        System.out.println("p1 = " + p1);
        System.out.println("p2 = " + p2);
        System.out.println("p1.equals(p2) = " + p1.equals(p2));

        // Java 14 预览写法（注释中展示）：
        // Point p = new Point(3, 4);
        // System.out.println(p.x());  // 3
        // System.out.println(p.y());  // 4
        // System.out.println(p);      // Point[x=3, y=4]
        //
        // Range r = new Range(1, 10);
        // System.out.println(r.length());  // 9
        //
        // // Records 可以解构（在 switch 模式匹配中，后续版本引入）
        // // if (p instanceof Point(var x, var y)) { ... }
    }

    // ==================== 3. instanceof 模式匹配（预览特性） ====================

    /**
     * 【预览特性 - 需要用 --enable-preview 编译】
     *
     * Java 14 引入了 instanceof 的模式匹配，简化了类型检查和转换的代码。
     *
     * 传统写法：
     * if (obj instanceof String) {
     *     String s = (String) obj;
     *     // 使用 s
     * }
     *
     * 模式匹配写法：
     * if (obj instanceof String s) {
     *     // 直接使用 s，无需显式转换
     * }
     *
     * 绑定变量 s 的作用域：
     * - 在 if 块内有效
     * - 也可在 && 的右侧使用（短路保证安全）
     */
    public String processObject(Object obj) {
        // 传统写法
        if (obj instanceof String) {
            String s = (String) obj;
            return "String of length " + s.length();
        }

        // Java 14 预览写法（注释中展示）：
        // if (obj instanceof String s) {
        //     return "String of length " + s.length();
        // }
        // if (obj instanceof Integer i && i > 0) {
        //     return "Positive integer: " + i;
        // }
        // if (obj instanceof List<?> list && !list.isEmpty()) {
        //     return "Non-empty list of size " + list.size();
        // }

        if (obj instanceof Integer) {
            Integer i = (Integer) obj;
            if (i > 0) {
                return "Positive integer: " + i;
            }
        }
        return "Unknown type: " + obj.getClass().getSimpleName();
    }

    /**
     * instanceof 模式匹配在 equals() 方法中特别有用
     */
    static final class Person {
        final String name;
        final int age;

        Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public boolean equals(Object obj) {
            // Java 14 预览写法：
            // if (obj instanceof Person p) {
            //     return this.name.equals(p.name) && this.age == p.age;
            // }
            // return false;

            // 传统写法
            if (obj instanceof Person) {
                Person p = (Person) obj;
                return this.name.equals(p.name) && this.age == p.age;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return 31 * name.hashCode() + age;
        }

        @Override
        public String toString() {
            return "Person[name=" + name + ", age=" + age + "]";
        }
    }

    public void instanceofDemo() {
        System.out.println("processObject(\"hello\"): " + processObject("hello"));
        System.out.println("processObject(42): " + processObject(42));
        System.out.println("processObject(3.14): " + processObject(3.14));

        Person p1 = new Person("Alice", 30);
        Person p2 = new Person("Alice", 30);
        System.out.println("p1.equals(p2): " + p1.equals(p2));
    }

    // ==================== 4. NullPointerException 增强提示 ====================

    /**
     * Java 14 引入了 Helpful NullPointerExceptions，
     * 当抛出 NPE 时，错误信息会明确指出是哪个变量为 null。
     *
     * 例如：
     * String s = null;
     * s.length();
     * // Java 14 之前：NullPointerException
     * // Java 14 之后：Cannot invoke "String.length()" because "s" is null
     *
     * 更复杂的例子：
     * a.b.c.d = 10;
     * // Java 14：Cannot read field "c" because "a.b" is null
     *
     * 这是 JVM 级别的改进，无需修改代码即可享受。
     * 默认启用，可通过 -XX:-ShowCodeDetailsInExceptionMessages 关闭。
     */
    public void npeEnhancement() {
        try {
            String s = null;
            // Java 14 会提示：Cannot invoke "String.length()" because "<local1>" is null
            s.length();
        } catch (NullPointerException e) {
            System.out.println("NPE message: " + e.getMessage());
            // 输出: Cannot invoke "String.length()" because "s" is null
        }

        try {
            int[] arr = null;
            // Java 14 会提示：Cannot read the array length because "<local1>" is null
            int len = arr.length;
        } catch (NullPointerException e) {
            System.out.println("NPE message: " + e.getMessage());
            // 输出: Cannot read the array length because "arr" is null
        }
    }

    // ==================== 5. 其他特性 ====================

    /**
     * Java 14 的其他值得注意的特性：
     *
     * - Packaging Tool（jpackage）：将 Java 应用打包为本地安装包
     *   支持：Windows（.msi/.exe）、macOS（.pkg/.dmg）、Linux（.deb/.rpm）
     *
     * - ZGC（实验性）：低延迟垃圾收集器转为产品特性
     *
     * - G1 的 NUMA 感知内存分配
     *
     * - 删除 CMS 垃圾收集器
     */
    public void otherFeatures() {
        System.out.println("Java 14 also includes:");
        System.out.println("- jpackage tool for native packaging");
        System.out.println("- ZGC as a product feature");
        System.out.println("- Removal of CMS garbage collector");
    }

    // ==================== main ====================

    public static void main(String[] args) {
        Java14 demo = new Java14();

        System.out.println("=== 1. Switch Expressions (Standard) ===");
        System.out.println("Monday -> " + demo.getDayType("Monday"));
        System.out.println("Saturday -> " + demo.getDayType("Saturday"));
        System.out.println("Score for B -> " + demo.getScore("B"));
        System.out.println("SPRING -> " + demo.describeSeason(Season.SPRING));

        System.out.println("\n=== 2. Records (Preview) ===");
        demo.recordsDemo();

        System.out.println("\n=== 3. instanceof Pattern Matching (Preview) ===");
        demo.instanceofDemo();

        System.out.println("\n=== 4. Helpful NullPointerExceptions ===");
        demo.npeEnhancement();

        System.out.println("\n=== 5. Other Features ===");
        demo.otherFeatures();
    }
}
