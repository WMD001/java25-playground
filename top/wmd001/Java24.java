package wmd001;

import java.util.List;
import java.util.concurrent.StructuredTaskScope;

/**
 * Java 24 主要语法变更：
 * 1. 灵活构造函数体（正式）：允许在构造函数中调用super()或this()之前执行语句（如参数验证、日志记录、计算中间值等），正式解决了Java长期以来构造函数中super()必须是第一条语句的限制，提升了构造函数的表达能力和安全性。
 * 2. 模块导入声明（正式）：允许使用 import module java.base; 语法导入整个模块的所有导出包，正式简化了模块化编程中的导入语句，避免逐个导入各个包的繁琐操作。
 * 3. Stream收集器增强（预览）：为Stream API引入新的收集器方法，如gatherer()，允许以更灵活的方式对流元素进行转换、过滤和分组操作，增强了Stream API的表达能力。此功能需要 --enable-preview 开启。
 * 4. 结构化并发（预览）：将结构化并发从孵化模块升级为预览特性，提供StructuredTaskScope等API，以结构化的方式管理并发任务的生命周期，使并发编程更加可靠和易维护。此功能需要 --enable-preview 开启。
 * 5. 作用域值（预览）：引入ScopedValue API，作为线程局部变量（ThreadLocal）的安全替代方案，特别适用于虚拟线程场景，提供了自动清理和不可变的跨线程上下文传递能力。此功能需要 --enable-preview 开启。
 * 6. 原始类型模式匹配（正式）：在switch中正式支持原始类型（int、long、double等）的模式匹配和守卫条件，使switch能够更自然地处理原始类型的复杂分支逻辑。
 * @author WYQ
 * @since 2026/6/3
 */
public class Java24 {

    /**
     * Record类型定义
     */
    record Point(int x, int y) {}

    /**
     * 灵活构造函数体（正式）：允许在super()之前执行语句
     */
    static class ValidatedUser {
        private final String username;
        private final String email;
        private final String normalizedEmail;

        ValidatedUser(String username, String email) {
            // 正式特性：在构造逻辑中进行参数验证和预处理
            if (username == null || username.isBlank()) {
                throw new IllegalArgumentException("用户名不能为空");
            }
            if (email == null || !email.contains("@")) {
                throw new IllegalArgumentException("邮箱格式无效");
            }
            // 预处理后再赋值
            this.normalizedEmail = email.trim().toLowerCase();
            this.username = username.trim();
            this.email = this.normalizedEmail;
        }

        @Override
        public String toString() {
            return "ValidatedUser{username='%s', email='%s'}".formatted(username, email);
        }
    }

    /**
     * 模块导入声明（正式）：使用 import module 导入整个模块
     */
    static void moduleImportDemo() {
        // 正式特性：在文件顶部可以使用
        // import module java.base;      // 导入java.base模块
        // import module java.sql;       // 导入java.sql模块
        //
        // 这等价于导入该模块中所有导出包的所有public类型
        // 无需再单独写 import java.util.List; import java.util.Map; 等
        System.out.println("模块导入声明已正式支持，可在文件顶部使用 import module java.base;");
        System.out.println("List, Map, Set, Optional等无需单独导入");
    }

    /**
     * 原始类型模式匹配（正式）：在switch中对原始类型使用模式匹配
     */
    static String primitivePatternMatching(int value) {
        // 正式特性：switch中原始类型的模式匹配和守卫条件
        return switch (value) {
            case 0                         -> "零";
            case int i when i < 0          -> "负整数: " + i;
            case int i when i <= 100       -> "小正整数: " + i;
            case int i                     -> "大正整数: " + i;
        };
    }

    /**
     * Stream收集器增强（预览）：需要 --enable-preview
     * 使用gatherer()方法对流进行自定义转换
     */
    static List<String> streamGathererDemo(List<String> items) {
        // Stream收集器增强（预览特性，需 --enable-preview）
        // 使用gatherer进行自定义流转换：
        // return items.stream()
        //     .gather(Gatherer.of(
        //         () -> new int[]{0},
        //         (state, element, downstream) -> {
        //             state[0]++;
        //             if (state[0] % 2 == 1) {
        //                 downstream.push("第" + state[0] + "个: " + element);
        //             }
        //             return true;
        //         }
        //     ))
        //     .toList();

        // 传统实现等效逻辑：取奇数位置的元素并编号
        var result = new java.util.ArrayList<String>();
        for (int i = 0; i < items.size(); i++) {
            if (i % 2 == 0) {
                result.add("第" + (i + 1) + "个: " + items.get(i));
            }
        }
        return result;
    }

    /**
     * 结构化并发（预览）：需要 --enable-preview
     * 使用StructuredTaskScope管理并发任务
     */
    static String structuredConcurrencyDemo() throws Exception {
        // 结构化并发（预览特性，需 --enable-preview）
        // try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
        //     var userTask = scope.fork(() -> fetchUser());
        //     var orderTask = scope.fork(() -> fetchOrder());
        //     scope.join().throwIfFailed();
        //     return userTask.get().name() + " - " + orderTask.get().orderId();
        // }
        return "结构化并发为预览特性，需要 --enable-preview。" +
               "StructuredTaskScope可管理并发任务的生命周期，确保所有子任务在父任务完成前完成。";
    }

    /**
     * 作用域值（预览）：需要 --enable-preview
     * ScopedValue作为ThreadLocal的安全替代
     */
    static void scopedValueDemo() {
        // 作用域值（预览特性，需 --enable-preview）
        // ScopedValue<String> CURRENT_USER = ScopedValue.newInstance();
        //
        // ScopedValue.where(CURRENT_USER, "Alice").run(() -> {
        //     System.out.println("当前用户: " + CURRENT_USER.get());
        //     // 嵌套调用也能获取到该值
        //     processData();
        // });
        // // 离开作用域后，值自动清除，CURRENT_USER.get() 会抛出异常
        System.out.println("作用域值为预览特性，需要 --enable-preview。" +
                           "ScopedValue是ThreadLocal的安全替代，特别适用于虚拟线程。");
    }

    public static void main(String[] args) throws Exception {
        // 灵活构造函数体（正式）
        ValidatedUser user = new ValidatedUser("Alice", "Alice@Example.COM");
        System.out.println(user);

        // 模块导入声明（正式）
        moduleImportDemo();

        // 原始类型模式匹配（正式）
        System.out.println(primitivePatternMatching(0));
        System.out.println(primitivePatternMatching(-42));
        System.out.println(primitivePatternMatching(50));
        System.out.println(primitivePatternMatching(500));

        // Stream收集器增强（预览）
        List<String> items = List.of("apple", "banana", "cherry", "date", "elderberry");
        System.out.println("收集器结果: " + streamGathererDemo(items));

        // 结构化并发（预览）
        System.out.println(structuredConcurrencyDemo());

        // 作用域值（预览）
        scopedValueDemo();
    }
}
