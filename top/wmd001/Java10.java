package wmd001;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Java 10 主要语法变更（2018年3月发布）：
 * 1. 局部变量类型推断（var）：允许在声明局部变量时使用var关键字，编译器根据初始化表达式自动推断变量类型。
 *    var只能用于局部变量，不能用于方法参数、返回类型、成员变量等。
 * 2. 不可变集合复制方法：List.copyOf()、Set.copyOf()、Map.copyOf()提供了创建不可变集合副本的便捷方式。
 * 3. Collectors新方法：toUnmodifiableList()、toUnmodifiableSet()、toUnmodifiableMap()用于Stream收集为不可变集合。
 * 4. Optional.orElseThrow()：无参数的orElseThrow()方法，在Optional为空时抛出NoSuchElementException。
 * 5. Stream.collect(Collectors.teeing())：将Stream元素同时传递给两个收集器，然后合并结果。
 * @author WYQ
 * @since 2026/6/3
 */
public class Java10 {

    // ==================== 1. 局部变量类型推断（var） ====================

    /**
     * var是Java 10引入的局部变量类型推断关键字。
     * 编译器根据变量的初始化表达式自动推断类型，编译后变量有明确的类型（不是动态类型）。
     *
     * 使用限制：
     * - 只能用于局部变量，且必须有初始化表达式
     * - 不能用于方法参数、返回类型、成员变量、catch参数
     * - 不能赋值为null（无法推断类型）
     * - var不是保留字，可以用作变量名、方法名等（向后兼容）
     */
    public static void testVar() {
        // 基本类型推断
        var name = "Java 10";          // 推断为 String
        var version = 10;              // 推断为 int
        var pi = 3.14;                 // 推断为 double
        var flag = true;               // 推断为 boolean

        System.out.println("name: " + name + ", type: " + ((Object) name).getClass().getSimpleName());
        System.out.println("version: " + version);

        // 集合类型推断
        var numbers = List.of(1, 2, 3, 4, 5);    // 推断为 List<Integer>
        var map = Map.of("key", "value");         // 推断为 Map<String, String>

        // 复杂类型推断
        var stream = Stream.of("a", "b", "c");   // 推断为 Stream<String>

        // 循环中的var
        for (var item : numbers) {
            System.out.print(item + " ");
        }
        System.out.println();

        // for循环中的var
        for (var i = 0; i < 5; i++) {
            System.out.print(i + " ");
        }
        System.out.println();

        // var结合泛型 - 菱形操作符不能与var一起使用
        // var list = new ArrayList<>();  // 编译错误！无法推断泛型类型
        var list = new ArrayList<String>();  // 正确：明确指定泛型类型
        list.add("Hello");

        // var的实际价值：简化复杂类型声明
        var entries = Map.of("a", 1, "b", 2).entrySet(); // 推断为 Set<Map.Entry<String, Integer>>
        for (var entry : entries) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }
    }

    // ==================== 2. 不可变集合复制方法 ====================

    /**
     * Java 10新增了List.copyOf()、Set.copyOf()、Map.copyOf()静态方法，
     * 用于创建不可变的集合副本。与Collections.unmodifiableList()不同的是，
     * copyOf()创建的是真正的不可变副本，不持有原集合的引用。
     */
    public static void testCopyOf() {
        // List.copyOf() - 创建不可变List副本
        List<String> original = new ArrayList<>(Arrays.asList("A", "B", "C"));
        List<String> immutable = List.copyOf(original);
        System.out.println("List.copyOf: " + immutable);
        // immutable.add("D"); // 抛出 UnsupportedOperationException

        // 修改原集合不影响副本
        original.add("D");
        System.out.println("原集合: " + original);     // [A, B, C, D]
        System.out.println("不可变副本: " + immutable); // [A, B, C] - 不受影响

        // Set.copyOf() - 创建不可变Set副本（自动去重）
        Set<Integer> numbers = Set.copyOf(List.of(1, 2, 2, 3, 3));
        System.out.println("Set.copyOf: " + numbers); // [1, 2, 3]

        // Map.copyOf() - 创建不可变Map副本
        Map<String, Integer> scores = Map.copyOf(Map.of("Alice", 95, "Bob", 87));
        System.out.println("Map.copyOf: " + scores);
    }

    // ==================== 3. Collectors 收集为不可变集合 ====================

    /**
     * Java 10为Collectors新增了toUnmodifiableList()、toUnmodifiableSet()、toUnmodifiableMap()方法，
     * 将Stream元素收集为不可变集合。与toList()不同，返回的集合不允许修改。
     */
    public static void testUnmodifiableCollectors() {
        // toUnmodifiableList() - 收集为不可变List
        List<String> names = Stream.of("Alice", "Bob", "Charlie")
                .filter(n -> n.length() > 3)
                .collect(Collectors.toUnmodifiableList());
        System.out.println("toUnmodifiableList: " + names);
        // names.add("Dave"); // 抛出 UnsupportedOperationException

        // toUnmodifiableSet() - 收集为不可变Set
        Set<Integer> evens = Stream.of(1, 2, 3, 4, 5, 2, 4)
                .filter(n -> n % 2 == 0)
                .collect(Collectors.toUnmodifiableSet());
        System.out.println("toUnmodifiableSet: " + evens);

        // toUnmodifiableMap() - 收集为不可变Map
        Map<String, Integer> lengths = Stream.of("Alice", "Bob", "Charlie")
                .collect(Collectors.toUnmodifiableMap(
                        name -> name,           // keyMapper
                        String::length           // valueMapper
                ));
        System.out.println("toUnmodifiableMap: " + lengths);
    }

    // ==================== 4. Optional.orElseThrow() ====================

    /**
     * Java 10为Optional新增了无参数的orElseThrow()方法。
     * 与orElseThrow(Supplier)不同，无参版本在Optional为空时直接抛出NoSuchElementException，
     * 语义更清晰，表达了"这个值必须存在"的意图。
     */
    public static void testOptionalOrElseThrow() {
        Optional<String> present = Optional.of("Hello");
        Optional<String> empty = Optional.empty();

        // 有值时正常返回
        String value = present.orElseThrow();
        System.out.println("orElseThrow: " + value);

        // 无值时抛出 NoSuchElementException
        try {
            empty.orElseThrow();
        } catch (java.util.NoSuchElementException e) {
            System.out.println("异常: " + e.getMessage());
        }

        // 对比：orElseThrow(Supplier) - Java 8引入，可以自定义异常
        try {
            empty.orElseThrow(() -> new IllegalStateException("值不存在"));
        } catch (IllegalStateException e) {
            System.out.println("自定义异常: " + e.getMessage());
        }
    }

    // ==================== 5. Stream.collectors.teeing() ====================

    /**
     * Java 10新增了Collectors.teeing()方法（JDK 12正式引入，Java 10中尚未包含）。
     * 注意：teeing()实际上是Java 12引入的，此处仅做说明。
     * 它将Stream元素同时传递给两个收集器，然后通过BiFunction合并两个收集器的结果。
     */

    // ==================== main ====================

    public static void main(String[] args) {
        System.out.println("=== 1. var 局部变量类型推断 ===");
        testVar();

        System.out.println("\n=== 2. 不可变集合复制 (copyOf) ===");
        testCopyOf();

        System.out.println("\n=== 3. Collectors 不可变集合收集 ===");
        testUnmodifiableCollectors();

        System.out.println("\n=== 4. Optional.orElseThrow() ===");
        testOptionalOrElseThrow();
    }
}
