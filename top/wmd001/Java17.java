package wmd001;

/**
 * Java 17 主要语法变更（2021年9月发布，LTS长期支持版本）：
 * 1. Sealed Classes（正式特性）：密封类从预览转为正式，用于限制哪些类可以继承或实现某个类/接口。
 * 2. sealed、non-sealed、permits关键字：配合密封类使用，permits指定允许的子类，sealed表示子类也是密封的，non-sealed表示子类可以被任意继承。
 * 3. 模式匹配instanceof增强：在Java 16正式特性的基础上进一步优化，模式变量的作用域更加精确。
 * 4. 注意：Java 17是继Java 11之后的下一个LTS版本，对生产环境具有重要意义。
 * @author WYQ
 * @since 2026/6/3
 */
public class Java17 {

    // ==================== 1. Sealed Classes（正式特性） ====================
    // Sealed Classes允许类的作者明确控制哪些类可以继承该类
    // 提供了更精确的类型层次结构控制

    // 定义一个密封类，只允许Circle、Rectangle和Triangle继承
    // 使用permits关键字指定允许的子类
    sealed class Shape permits Circle, Rectangle, Triangle {
        // 密封类可以是抽象类
        abstract double area();
        abstract double perimeter();
    }

    // 子类必须是以下三种之一：final、sealed或non-sealed
    // final类：不能被进一步继承
    final class Circle extends Shape {
        private final double radius;

        Circle(double radius) {
            this.radius = radius;
        }

        @Override
        double area() {
            return Math.PI * radius * radius;
        }

        @Override
        double perimeter() {
            return 2 * Math.PI * radius;
        }

        double getRadius() {
            return radius;
        }
    }

    // sealed子类：继续控制继承层次
    sealed class Rectangle extends Shape permits Square {
        protected final double width;
        protected final double height;

        Rectangle(double width, double height) {
            this.width = width;
            this.height = height;
        }

        @Override
        double area() {
            return width * height;
        }

        @Override
        double perimeter() {
            return 2 * (width + height);
        }
    }

    // final的子类
    final class Square extends Rectangle {
        Square(double side) {
            super(side, side);
        }

        // 可以添加额外的方法
        double diagonal() {
            return width * Math.sqrt(2);
        }
    }

    // non-sealed子类：可以被任意类继承
    // 当不想限制继承时使用non-sealed
    non-sealed class Triangle extends Shape {
        private final double a, b, c; // 三边长度

        Triangle(double a, double b, double c) {
            if (a + b <= c || a + c <= b || b + c <= a) {
                throw new IllegalArgumentException("无法构成三角形");
            }
            this.a = a;
            this.b = b;
            this.c = c;
        }

        @Override
        double area() {
            // 海伦公式
            double s = (a + b + c) / 2;
            return Math.sqrt(s * (s - a) * (s - b) * (s - c));
        }

        @Override
        double perimeter() {
            return a + b + c;
        }
    }

    // ==================== 2. Sealed Interfaces ====================
    // 密封类同样适用于接口

    // 定义密封接口
    sealed interface JsonValue permits JsonString, JsonNumber, JsonArray, JsonNull {
        String toJson();
    }

    // final实现类
    record JsonString(String value) implements JsonValue {
        @Override
        public String toJson() {
            return "\"" + value.replace("\"", "\\\"") + "\"";
        }
    }

    record JsonNumber(double value) implements JsonValue {
        @Override
        public String toJson() {
            if (value == Math.floor(value) && !Double.isInfinite(value)) {
                return String.valueOf((long) value);
            }
            return String.valueOf(value);
        }
    }

    // non-sealed实现类：允许被任意类实现
    non-sealed class JsonArray implements JsonValue {
        private final java.util.List<JsonValue> elements;

        JsonArray(java.util.List<JsonValue> elements) {
            this.elements = elements;
        }

        @Override
        public String toJson() {
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < elements.size(); i++) {
                if (i > 0) sb.append(", ");
                sb.append(elements.get(i).toJson());
            }
            return sb.append("]").toString();
        }
    }

    record JsonNull() implements JsonValue {
        @Override
        public String toJson() {
            return "null";
        }
    }

    // ==================== 3. 模式匹配instanceof增强 ====================
    // 在Java 16的基础上，模式变量的作用域更加精确
    public void demonstratePatternMatching() {
        Object obj = "Hello, Java 17!";

        // 模式变量可以用于更复杂的逻辑
        if (obj instanceof String s && s.length() > 5) {
            System.out.println("长字符串: " + s);
            System.out.println("长度: " + s.length());
        }

        // 模式变量在逻辑运算中的短路行为
        // 如果instanceof为false，后续条件不会执行
        Object number = "not a number";
        if (number instanceof Integer i && i > 0) {
            // 这里不会执行，因为number不是Integer
            System.out.println("正整数: " + i);
        }

        // 使用密封类进行模式匹配（预览特性，Java 17中还不是正式特性）
        // 但在Java 17中，可以更好地利用密封类的特性
        Shape shape = new Circle(5.0);
        String description;
        if (shape instanceof Circle c) {
            description = "圆形，半径: " + c.getRadius();
        } else if (shape instanceof Rectangle r) {
            description = "矩形，宽: " + r.width + "，高: " + r.height;
        } else if (shape instanceof Triangle) {
            description = "三角形";
        } else {
            description = "未知形状";
        }
        System.out.println("形状描述: " + description);
    }

    // 辅助方法：计算形状的描述信息
    private String describeShape(Shape shape) {
        // 使用密封类，编译器知道所有可能的子类
        // 未来配合switch模式匹配（Java 21正式）会更强大
        if (shape instanceof Circle c) {
            return String.format("圆形 - 半径: %.2f, 面积: %.2f, 周长: %.2f",
                c.getRadius(), c.area(), c.perimeter());
        } else if (shape instanceof Square sq) {
            return String.format("正方形 - 边长: %.2f, 面积: %.2f, 对角线: %.2f",
                sq.width, sq.area(), sq.diagonal());
        } else if (shape instanceof Rectangle r) {
            return String.format("矩形 - 宽: %.2f, 高: %.2f, 面积: %.2f",
                r.width, r.height, r.area());
        } else if (shape instanceof Triangle t) {
            return String.format("三角形 - 面积: %.2f, 周长: %.2f",
                t.area(), t.perimeter());
        } else {
            return "未知形状";
        }
    }

    public void demonstrateSealedClasses() {
        // 创建不同类型的Shape
        Shape circle = new Circle(5.0);
        Shape rectangle = new Rectangle(4.0, 6.0);
        Shape square = new Square(3.0);
        Shape triangle = new Triangle(3.0, 4.0, 5.0);

        // 打印形状信息
        System.out.println("=== 形状信息 ===");
        System.out.println(describeShape(circle));
        System.out.println(describeShape(rectangle));
        System.out.println(describeShape(square));
        System.out.println(describeShape(triangle));

        // 计算总面积
        double totalArea = circle.area() + rectangle.area() + square.area() + triangle.area();
        System.out.printf("总面积: %.2f%n", totalArea);

        // 演示JSON值
        System.out.println("\n=== JSON值演示 ===");
        JsonValue str = new JsonString("Hello");
        JsonValue num = new JsonNumber(42.5);
        JsonValue arr = new JsonArray(java.util.List.of(str, num, new JsonNull()));
        JsonValue nil = new JsonNull();

        System.out.println("字符串: " + str.toJson());
        System.out.println("数字: " + num.toJson());
        System.out.println("数组: " + arr.toJson());
        System.out.println("空值: " + nil.toJson());
    }

    public static void main(String[] args) {
        Java17 demo = new Java17();

        System.out.println("=== Sealed Classes演示 ===");
        demo.demonstrateSealedClasses();

        System.out.println("\n=== 模式匹配演示 ===");
        demo.demonstratePatternMatching();
    }
}
