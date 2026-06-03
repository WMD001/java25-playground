package wmd001;

/**
 * Java 23 主要语法变更：
 * 1. 模块导入声明（预览）：允许使用 import module java.base; 语法导入整个模块的所有导出包，简化模块化编程中的导入语句，避免逐个导入各个包。此功能需要 --enable-preview 开启。
 * 2. Markdown文档注释（预览）：允许在Javadoc中使用Markdown语法，使用 /// 开头的注释块，支持Markdown的格式化特性如粗体、斜体、代码块、列表等，使API文档编写更加直观和便捷。此功能需要 --enable-preview 开启。
 * 3. 灵活构造函数体（第二次预览）：继续改进灵活构造函数体特性，允许在调用super()或this()之前执行更多类型的语句。此功能需要 --enable-preview 开启。
 * 4. switch中的原始类型模式匹配（预览）：将模式匹配扩展到switch中的原始类型（int、long、double等），允许在case标签中使用原始类型的模式匹配和守卫条件。此功能需要 --enable-preview 开启。
 * 5. Java 23没有重大的正式语法变更，主要以预览特性的改进和完善为主。
 * @author WYQ
 * @since 2026/6/3
 */
public class Java23 {

    /**
     * 模块导入声明（预览）：需要 --enable-preview
     * 使用 import module 导入整个模块
     */
    static void moduleImportDemo() {
        // 模块导入声明（预览特性，需 --enable-preview）
        // 在文件顶部可以使用：
        // import module java.base;      // 导入java.base模块的所有导出包
        // import module java.sql;       // 导入java.sql模块的所有导出包
        //
        // 这样就不需要单独写：
        // import java.util.List;
        // import java.util.Map;
        // import java.util.Set;
        // 等多个导入语句
        System.out.println("模块导入声明为预览特性，需要 --enable-preview");
        System.out.println("使用 import module java.base; 可以导入整个模块的导出包");
    }

    /**
     * Markdown文档注释（预览）：需要 --enable-preview
     * 使用 /// 开头的注释块，支持Markdown语法
     */
    // 使用Markdown语法的文档注释（预览特性，需 --enable-preview）：
    ///
    /// ## 方法说明
    /// 这是一个演示 **Markdown文档注释** 的方法。
    ///
    /// ### 功能特性
    /// - 支持 `粗体` 和 *斜体*
    /// - 支持代码块：
    ///   ```java
    ///   var list = List.of(1, 2, 3);
    ///   ```
    /// - 支持[链接](https://openjdk.org)
    ///
    /// @param input 输入参数
    /// @return 处理结果字符串
    static String markdownJavadocDemo(String input) {
        // 传统的Javadoc注释使用 /** ... */ 格式
        // Java 23预览支持 /// 开头的Markdown格式文档注释
        return "处理结果: " + input.toUpperCase();
    }

    /**
     * 灵活构造函数体（第二次预览）：需要 --enable-preview
     * 继续改进构造函数中super()之前的语句支持
     */
    static class EnhancedConstructor {
        private final String data;

        EnhancedConstructor(String raw) {
            // 灵活构造函数体第二次预览（需 --enable-preview）
            // 允许在super()之前进行更复杂的初始化逻辑
            // 比如资源清理、验证链、计算等
            // String validated = validate(raw);
            // super(validated);
            this.data = raw == null ? "" : raw.trim();
        }

        public String getData() {
            return data;
        }
    }

    /**
     * switch中的原始类型模式匹配（预览）：需要 --enable-preview
     * 在switch中对原始类型使用模式匹配和守卫条件
     */
    static String primitiveTypePatternMatching(int value) {
        // 原始类型模式匹配（预览特性，需 --enable-preview）
        // return switch (value) {
        //     case 0          -> "零";
        //     case int i when i < 0  -> "负数: " + i;
        //     case int i when i <= 100 -> "小正数: " + i;
        //     case int i       -> "大正数: " + i;
        // };
        if (value == 0) return "零";
        if (value < 0) return "负数: " + value;
        if (value <= 100) return "小正数: " + value;
        return "大正数: " + value;
    }

    public static void main(String[] args) {
        // 模块导入声明
        moduleImportDemo();

        // Markdown文档注释
        System.out.println(markdownJavadocDemo("hello java 23"));

        // 灵活构造函数体
        EnhancedConstructor ec = new EnhancedConstructor("  test data  ");
        System.out.println("构造结果: " + ec.getData());

        // 原始类型模式匹配
        System.out.println(primitiveTypePatternMatching(0));
        System.out.println(primitiveTypePatternMatching(-5));
        System.out.println(primitiveTypePatternMatching(50));
        System.out.println(primitiveTypePatternMatching(200));
    }
}
