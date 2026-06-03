package wmd001;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Java 8引入了许多新特性和改进，以下是一些主要的特性：
 * 1. Lambda表达式：Lambda表达式是一种匿名函数，可以用来简化代码，使得代码更加简洁和可读。Lambda表达式可以用来实现函数式接口，即只包含一个抽象方法的接口。Lambda表达式的语法非常简单，可以直接在需要函数式接口的地方使用。
 * 2. Stream API：Stream API是Java 8引入的一套用于处理集合的工具类库。它提供了一种声明式的方式来处理集合数据，使得代码更加简洁和易读。Stream API支持链式调用，可以进行过滤、映射、排序、聚合等操作，并且可以利用多核处理器的优势来提高性能。
 * 3. 函数式接口：函数式接口是指只包含一个抽象方法的接口。Java 8引入了几个常用的函数式接口，如Predicate、Function、Consumer和Supplier。这些接口可以用来表示不同类型的函数，例如Predicate表示一个接受一个输入参数并返回一个布尔值的函数，Function表示一个接受一个输入参数并返回一个结果的函数，Consumer表示一个接受一个输入参数并执行某些操作但不返回结果的函数，Supplier表示一个不接受任何输入参数但返回一个结果的函数。
 * 4. 方法引用：方法引用是一种简化Lambda表达式的语法，可以直接引用已有的方法来实现函数式接口。方法引用的语法非常简单，可以使用类名::方法名的形式来引用静态方法，使用对象::方法名的形式来引用实例方法，使用类名::new的形式来引用构造方法。
 * 5. Optional类：Optional类是Java 8引入的一个容器类，用于表示一个可能存在或不存在的值。Optional类提供了一些方法来处理可能为null的值，例如isPresent、ifPresent、orElse等。使用Optional类可以避免NullPointerException，并且使得代码更加健壮和易读。
 * 6. 默认方法和静态方法：Java 8允许在接口中定义默认方法和静态方法。默认方法是指在接口中提供一个默认的实现，如果实现类没有提供自己的实现，就会使用默认方法的实现。静态方法是指在接口中定义一个静态方法，可以直接通过接口名调用。默认方法和静态方法的引入使得接口更加灵活和强大，可以在不破坏现有接口的情况下添加新的方法。
 * 7. 重复注解：Java 8引入了重复注解的概念，允许在同一个元素上使用相同类型的注解多次。重复注解可以通过使用@Repeatable注解来实现，@Repeatable注解指定了一个容器注解，用于存储重复的注解实例。重复注解的引入使得代码更加简洁和易读，特别是在需要使用多个相同类型的注解时。
 * 8. 类型注解：Java 8引入了类型注解的概念，允许在类型使用的地方添加注解。类型注解可以用于提供额外的类型信息，例如在泛型类型参数、方法返回类型、字段类型等地方添加注解。类型注解的引入使得代码更加灵活和强大，可以提供更多的类型信息来帮助编译器进行类型检查和错误提示。
 * 9. 改进的日期和时间API：Java 8引入了新的日期和时间API，提供了更好的日期和时间处理功能。新的日期和时间API包括了新的类，如LocalDate、LocalTime、LocalDateTime、ZonedDateTime等，这些类提供了更丰富的日期和时间操作方法，并且支持时区和日期时间格式化等功能。新的日期和时间API使得代码更加简洁和易读，并且提供了更好的日期和时间处理能力。
 * 总的来说，Java 8引入了许多新特性和改进，使得开发者可以更好地利用函数式编程的优势，提高代码的简洁性和可读性。这些特性使得Java 8成为一个重要的版本，为开发者提供了更多的工具和功能来构建高质量的应用程序。
 * @author WYQ
 * @since 2026/6/3
 */
public class Java8 {

    public static void main(String[] args) {
        System.out.println("=== Predicate ===");
        System.out.println("test(\"hello\"): " + test("hello"));
        System.out.println("test(\"\"): " + test(""));

        System.out.println("\n=== Function ===");
        System.out.println("test2(\"hello\"): " + test2("hello"));

        System.out.println("\n=== Consumer ===");
        test3("Hello Consumer");

        System.out.println("\n=== Supplier ===");
        System.out.println("test4(\"Java 8\"): " + test4("Java 8"));

        System.out.println("\n=== flatMap ===");
        testFlatMap();

        System.out.println("\n=== parallelStream ===");
        testParallelStream();

        System.out.println("\n=== Optional ===");
        testOptional();

        System.out.println("\n=== 默认方法和静态方法 ===");
        Car car = new Car();
        car.start();
        car.stop();
        System.out.println("isMotorized: " + Vehicle.isMotorized(car));

        System.out.println("\n=== 重复注解 ===");
        testRepeatableAnnotation();

        System.out.println("\n=== 日期时间API ===");
        testDateTime();
    }


    /**
     * Predicate接口是Java 8引入的一个函数式接口，位于java.util.function包中。它表示一个接受一个输入参数并返回一个布尔值的函数。Predicate接口常用于过滤和条件判断等场景。
     * 该接口定义了一个抽象方法test(T t)，用于测试输入参数是否满足某个条件。除此之外，Predicate接口还提供了默认方法and、or和negate，用于组合多个Predicate实例。
     * @param numStr 输入字符串
     * @return  布尔值，表示输入字符串是否满足条件
     */
    public static boolean test(String numStr) {
        Predicate<String> predicate = (s) -> !s.isEmpty();
        return predicate.test(numStr);
    }

    /**
     * Function接口是Java 8引入的一个函数式接口，位于java.util.function包中。它表示一个接受一个输入参数并返回一个结果的函数。Function接口常用于数据转换和映射等场景。
     * 该接口定义了一个抽象方法apply(T t)，用于将输入参数转换为结果。除此之外，Function接口还提供了默认方法andThen和compose，用于组合多个Function实例。
     * @param str 输入字符串
     * @return 转换后的字符串
     */
    public static String test2(String str) {
         Function<String, String> function = String::toUpperCase;
         return function.apply(str);
    }

    /**
     * Consumer接口是Java 8引入的一个函数式接口，位于java.util.function包中。它表示一个接受一个输入参数并执行某些操作但不返回结果的函数。Consumer接口常用于处理数据和执行副作用等场景。
     * 该接口定义了一个抽象方法accept(T t)，用于执行对输入参数的操作。除此之外，Consumer接口还提供了默认方法andThen，用于组合多个Consumer实例。
     * @param str 输入字符串
     */
    public static void test3(String str) {
        Consumer<String> consumer = System.out::println;
        consumer.accept(str);
    }

    /**
     * Supplier接口是Java 8引入的一个函数式接口，位于java.util.function包中。它表示一个不接受任何输入参数但返回一个结果的函数。Supplier接口常用于提供数据和延迟计算等场景。
     * 该接口定义了一个抽象方法get()，用于获取结果。Supplier接口没有默认方法，因为它不需要组合多个实例。
     * @param str 输入字符串
     * @return 生成的字符串
     */
    public static String test4(String str) {
        Supplier<String> supplier = () -> "Hello, " + str;
        return supplier.get();
    }

    /**
     * flatMap方法是Java 8引入的Stream API中的一个重要方法，用于将一个Stream中的元素映射为另一个Stream，并将结果扁平化为一个单一的Stream。它常用于处理嵌套的数据结构，例如列表中的列表。
     */
    public static void testFlatMap() {
        Stream.of(List.of(1,2,3), List.of(4,5,6)).flatMap(Collection::stream).forEach(System.out::println);
    }

    /**
     * parallelStream方法是Java 8引入的Stream API中的一个方法，用于创建一个并行的Stream。并行Stream可以利用多核处理器的优势来提高性能，特别是在处理大量数据时。
     * 当使用parallelStream时，Stream会自动将数据分成多个部分，并在不同的线程上并行处理这些部分。需要注意的是，并行Stream并不总是比顺序Stream更快，具体性能取决于数据量、操作的复杂性以及系统的硬件配置等因素。
     */
    public static void testParallelStream() {
        Stream.of(1, 2, 3, 4, 5).parallel().forEach(System.out::println);
        List.of(1, 2, 3, 4, 5).parallelStream().forEach(System.out::println);
    }

    /**
     * Optional类是Java 8引入的一个容器对象，用于优雅地处理可能为null的值。
     * Optional提供了丰富的方法来安全地操作值，避免NullPointerException。
     * 常用方法：of、empty、ofNullable、isPresent、ifPresent、orElse、orElseGet、orElseThrow、map、flatMap、filter。
     */
    public static void testOptional() {
        // 创建Optional
        Optional<String> nonEmpty = Optional.of("Hello");
        Optional<String> nullable = Optional.ofNullable(getNullableValue());
        Optional<String> empty = Optional.empty();

        // isPresent - 判断是否有值
        System.out.println("nonEmpty.isPresent() = " + nonEmpty.isPresent()); // true
        System.out.println("empty.isPresent() = " + empty.isPresent());       // false

        // ifPresent - 如果有值则执行操作
        nonEmpty.ifPresent(v -> System.out.println("值为: " + v));

        // orElse - 提供默认值
        String value = nullable.orElse("默认值");

        // orElseGet - 通过Supplier提供默认值（延迟计算）
        String value2 = empty.orElseGet(() -> "延迟计算的默认值");

        // orElseThrow - 无值时抛出异常
        try {
            empty.orElseThrow(() -> new IllegalStateException("值不存在"));
        } catch (IllegalStateException e) {
            System.out.println("异常: " + e.getMessage());
        }

        // map - 对值进行转换
        Optional<Integer> length = nonEmpty.map(String::length);
        System.out.println("字符串长度: " + length.orElse(0));

        // filter - 条件过滤
        Optional<String> filtered = nonEmpty.filter(v -> v.startsWith("H"));
        System.out.println("过滤结果: " + filtered.orElse("不匹配"));

        // flatMap - 嵌套Optional扁平化
        Optional<String> flatResult = nonFlatMap();
        System.out.println("flatMap结果: " + flatResult.orElse("空"));
    }

    private static String getNullableValue() {
        return Math.random() > 0.5 ? "随机值" : null;
    }

    private static Optional<String> nonFlatMap() {
        return Optional.of("data")
                .flatMap(s -> Optional.of(s.toUpperCase()));
    }

    /**
     * 默认方法和静态方法是Java 8在接口中引入的重要特性。
     * 默认方法（default method）：在接口中提供默认实现，实现类可以选择覆盖或使用默认实现。
     * 静态方法（static method）：在接口中定义静态工具方法，通过接口名直接调用。
     * 这两个特性的引入使得在不破坏现有实现的情况下，可以向接口添加新方法。
     */
    interface Vehicle {
        // 抽象方法
        String getBrand();

        // 默认方法 - 提供默认实现
        default void start() {
            System.out.println(getBrand() + " 正在启动...");
            honk();
        }

        default void stop() {
            System.out.println(getBrand() + " 已停止");
        }

        // 静态方法 - 通过接口名调用
        static boolean isMotorized(Vehicle vehicle) {
            return vehicle != null && vehicle.getBrand() != null;
        }

        // 私有方法（Java 9引入，此处仅声明用于接口内部复用）
        // private void honk() { System.out.println("嘟嘟！"); }
        void honk();
    }

    static class Car implements Vehicle {
        @Override
        public String getBrand() {
            return "Tesla";
        }

        @Override
        public void honk() {
            System.out.println("嘀嘀！");
        }

        // 可选择覆盖默认方法
        @Override
        public void start() {
            System.out.println(getBrand() + " 无声启动（电动车）");
        }
    }

    /**
     * 重复注解是Java 8引入的特性，允许在同一个声明上多次使用相同类型的注解。
     * 通过@Repeatable元注解定义注解的容器，实现重复注解的支持。
     * 使用@Repeatable时，需要定义一个容器注解来存放重复的注解实例。
     */
    @java.lang.annotation.Repeatable(Schedules.class)
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
    @interface Schedule {
        String day();
        String time() default "09:00";
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
    @interface Schedules {
        Schedule[] value();
    }

    @Schedule(day = "Monday", time = "09:00")
    @Schedule(day = "Wednesday", time = "14:00")
    @Schedule(day = "Friday", time = "10:00")
    static class Meeting {
        // Java 8允许在同一元素上重复使用@Schedule注解
    }

    public static void testRepeatableAnnotation() {
        // 通过反射获取重复注解
        Schedule[] schedules = Meeting.class.getAnnotationsByType(Schedule.class);
        for (Schedule s : schedules) {
            System.out.println("会议安排: " + s.day() + " " + s.time());
        }
        // 也可以通过容器注解获取
        Schedules container = Meeting.class.getAnnotation(Schedules.class);
        if (container != null) {
            System.out.println("总共 " + container.value().length + " 个会议");
        }
    }

    /**
     * 改进的日期和时间API是Java 8引入的全新日期时间框架（java.time包），
     * 替代了老旧且线程不安全的java.util.Date和Calendar。
     * 新API的核心类包括：LocalDate、LocalTime、LocalDateTime、ZonedDateTime、Instant、Duration、Period等。
     * 所有日期时间类都是不可变的，天然线程安全。
     */
    public static void testDateTime() {
        // LocalDate - 只有日期，没有时间
        LocalDate today = LocalDate.now();
        LocalDate birthday = LocalDate.of(2000, Month.JUNE, 15);
        LocalDate parsed = LocalDate.parse("2024-01-01");
        System.out.println("今天: " + today);
        System.out.println("生日: " + birthday);
        System.out.println("星期几: " + today.getDayOfWeek());
        System.out.println("是否闰年: " + today.isLeapYear());

        // LocalTime - 只有时间，没有日期
        LocalTime now = LocalTime.now();
        LocalTime meeting = LocalTime.of(14, 30);
        System.out.println("现在: " + now);
        System.out.println("会议时间: " + meeting);

        // LocalDateTime - 日期 + 时间
        LocalDateTime dateTime = LocalDateTime.now();
        System.out.println("日期时间: " + dateTime);

        // ZonedDateTime - 带时区的日期时间
        ZonedDateTime zoned = ZonedDateTime.now(ZoneId.of("Asia/Shanghai"));
        System.out.println("上海时间: " + zoned);

        // Instant - 时间戳（UTC）
        Instant timestamp = Instant.now();
        System.out.println("时间戳: " + timestamp);

        // Duration - 时间段（基于时间）
        Duration duration = Duration.ofHours(2).plusMinutes(30);
        System.out.println("时长: " + duration.toMinutes() + " 分钟");

        // Period - 日期段（基于日期）
        Period period = Period.between(birthday, today);
        System.out.println("年龄: " + period.getYears() + " 岁 " + period.getMonths() + " 月");

        // DateTimeFormatter - 格式化
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss");
        System.out.println("格式化: " + dateTime.format(formatter));
    }

}
