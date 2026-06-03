package wmd001;

import java.util.List;

/**
 * Java 22 主要语法变更：
 * 1. 未命名变量和模式（正式）：允许在变量声明、lambda参数和模式匹配中使用下划线 _ 表示不需要使用的变量，提高代码可读性，减少无意义的命名。例如在 for-each 循环中使用 for (String _ : list)，或在模式匹配中使用 case Point(int x, _)。
 * 2. 灵活的构造函数体（预览）：允许在构造函数中调用 super() 或 this() 之前执行语句（如参数验证、日志记录等），解决了Java长期以来构造函数中 super() 必须是第一条语句的限制。此功能需要 --enable-preview 开启。
 * 3. 字符串模板（第二次预览）：字符串模板在第二次预览中进行了改进，继续优化STR模板处理器的API设计和安全性。此功能需要 --enable-preview 开启。
 * 4. 简单源文件实例（预览）：允许编写不需要显式类声明的Java源文件，顶层可以直接写语句和方法定义，降低初学者入门门槛，适用于小型脚本和教学场景。此功能需要 --enable-preview 开启。
 * @author WYQ
 * @since 2026/6/3
 */
public class Java22 {

    /**
     * Record类型定义
     */
    record Point(int x, int y) {}

    record Person(String name, int age) {}

    /**
     * 未命名变量（正式）：在模式匹配中使用 _ 忽略不需要的组件
     */
    static int extractX(Object obj) {
        // 正式特性：使用 _ 忽略不需要的Record组件
        if (obj instanceof Point(int x, _)) {
            return x;
        }
        return 0;
    }

    /**
     * 未命名变量（正式）：在for-each循环中使用 _ 忽略迭代变量
     */
    static void unnamedVariableInLoop(List<String> list) {
        // 正式特性：当只需要执行次数而不需要元素值时，使用 _ 忽略
        for (String _ : list) {
            System.out.println("遍历中...");
        }
    }

    /**
     * 灵活的构造函数体（预览）：需要 --enable-preview
     * 允许在 super() 调用之前执行语句
     */
    static class FlexibleConstructor {
        private final String name;
        private final int value;

        FlexibleConstructor(String name, int value) {
            // 灵活构造函数体（预览特性，需 --enable-preview）
            // 在调用super之前可以进行参数验证和处理
            // if (name == null || name.isBlank()) {
            //     throw new IllegalArgumentException("名称不能为空");
            // }
            // String processedName = name.trim().toLowerCase();
            // super();  // 传统上必须是第一条语句
            this.name = name.trim().toLowerCase();
            this.value = value;
        }

        @Override
        public String toString() {
            return "FlexibleConstructor{name='%s', value=%d}".formatted(name, value);
        }
    }

    /**
     * 简单源文件实例（预览）：需要 --enable-preview
     * 传统类文件无法演示此特性，需要单独创建 .java 文件
     */
    static void simpleSourceFileDemo() {
        // 简单源文件实例（预览特性，需 --enable-preview）
        // 可以创建一个不需要类声明的Java文件，例如 SimpleDemo.java:
        //
        // void main() {
        //     System.out.println("Hello from simple source file!");
        //     for (var arg : args) {
        //         System.out.println(arg);
        //     }
        // }
        //
        // 编译运行：javac --enable-preview --source 22 SimpleDemo.java
        //           java --enable-preview SimpleDemo arg1 arg2
        System.out.println("简单源文件实例为预览特性，需要 --enable-preview 并创建独立的源文件演示");
    }

    /**
     * 字符串模板（第二次预览）：需要 --enable-preview
     * 继续改进STR模板处理器
     */
    static String stringTemplateV2(String name, int score) {
        // 字符串模板第二次预览（需 --enable-preview）
        // return STR."\{name}的分数是\{score}分，等级为\{score >= 90 ? "优秀" : "良好"}";
        return name + "的分数是" + score + "分，等级为" + (score >= 90 ? "优秀" : "良好");
    }

    public static void main(String[] args) {
        // 未命名变量 - 模式匹配
        System.out.println("提取x坐标: " + extractX(new Point(10, 20)));

        // 未命名变量 - for-each循环
        unnamedVariableInLoop(List.of("a", "b", "c"));

        // 灵活构造函数体
        FlexibleConstructor fc = new FlexibleConstructor("  Hello  ", 42);
        System.out.println(fc);

        // 字符串模板
        System.out.println(stringTemplateV2("Alice", 95));

        // 简单源文件实例
        simpleSourceFileDemo();
    }
}
