package wmd001;

/**
 * Java 18 主要改进（2022年3月发布）：
 * 1. 没有重大语法变更：Java 18主要聚焦于API改进、JVM优化和开发工具增强。
 * 2. @snippet标签：为Javadoc引入了新的代码片段标签，支持更丰富的代码示例展示。
 * 3. 简单的Web服务器：jwebserver工具，用于快速启动一个简单的静态文件服务器。
 * 4. UTF-8作为默认字符集：在所有平台上默认使用UTF-8字符编码。
 * 5. 互联网地址解析改进：提供更灵活的主机名和地址解析。
 * @author WYQ
 * @since 2026/6/3
 */
public class Java18 {

    // ==================== @snippet标签演示 ====================
    // Java 18为Javadoc引入了@snippet标签，用于更好地展示代码示例
    // 注意：@snippet是Javadoc注释的一部分，不是Java代码语法

    /**
     * 计算两个数的和。
     *
     * 使用示例：
     * {@snippet :
     * int result = add(3, 5);  // result = 8
     * System.out.println(result);
     * }
     *
     * @param a 第一个数
     * @param b 第二个数
     * @return 两数之和
     */
    public int add(int a, int b) {
        return a + b;
    }

    /**
     * 判断一个字符串是否为空或仅包含空白字符。
     *
     * {@snippet :
     * // 传统的判断方式
     * boolean isEmpty = str == null || str.trim().isEmpty();
     *
     * // Java 11+ 的新方式
     * boolean isBlank = str == null || str.isBlank();
     * }
     *
     * @param str 要检查的字符串
     * @return 如果为空或仅包含空白字符返回true
     */
    public boolean isBlank(String str) {
        return str == null || str.isBlank();
    }

    // ==================== UTF-8默认字符集说明 ====================
    // Java 18之前，不同平台的默认字符集可能不同（如Windows是GBK，Linux是UTF-8）
    // Java 18将UTF-8设为所有平台的标准默认字符集
    // 这使得跨平台开发更加一致，避免了字符编码问题

    public void demonstrateDefaultCharset() {
        // 获取当前平台的默认字符集
        java.nio.charset.Charset defaultCharset = java.nio.charset.Charset.defaultCharset();
        System.out.println("默认字符集: " + defaultCharset.name());

        // Java 18之前，Windows平台默认可能是GBK
        // Java 18之后，所有平台默认都是UTF-8
        if (defaultCharset.name().equals("UTF-8")) {
            System.out.println("使用UTF-8字符集（Java 18标准）");
        } else {
            System.out.println("当前字符集不是UTF-8");
        }

        // UTF-8字符集支持所有Unicode字符
        String chinese = "你好，世界";
        String emoji = "Hello World";
        String mixed = "Java 18: " + chinese + " " + emoji;

        System.out.println("混合字符串: " + mixed);
        System.out.println("字符串长度: " + mixed.length());
        System.out.println("字节长度（UTF-8）: " + mixed.getBytes(java.nio.charset.StandardCharsets.UTF_8).length);
    }

    // ==================== 简单Web服务器说明 ====================
    // Java 18引入了jwebserver命令行工具
    // 可以在命令行中快速启动一个静态文件服务器
    // 用法：jwebserver -p 8080 -d /path/to/files
    // 这不是Java代码语法变更，而是开发工具的改进

    // ==================== 代码示例展示Java 18的API改进 ====================
    public void demonstrateAPIImprovements() {
        // 1. String.indent() - 已在Java 12引入，Java 18中继续优化
        String text = "Hello\nWorld";
        String indented = text.indent(4); // 每行前加4个空格
        System.out.println("缩进后:\n" + indented);

        // 2. String.transform() - 已在Java 12引入
        String result = "hello".transform(s -> s + " world").transform(String::toUpperCase);
        System.out.println("转换结果: " + result);

        // 3. Files.mismatch() - 已在Java 12引入，比较两个文件
        // 可用于检查两个文件是否完全相同

        // 4. 新的Vector API（第三轮孵化）
        // 用于SIMD向量计算，提高数值计算性能
        // 这是孵化器模块，不是正式API

        System.out.println("\nJava 18主要聚焦于JVM和工具层面的改进");
        System.out.println("语法层面没有重大变更");
    }

    public static void main(String[] args) {
        Java18 demo = new Java18();

        System.out.println("=== @snippet标签演示（查看Javadoc） ===");
        System.out.println("add(3, 5) = " + demo.add(3, 5));
        System.out.println("isBlank(\"\") = " + demo.isBlank(""));
        System.out.println("isBlank(\" \") = " + demo.isBlank(" "));
        System.out.println("isBlank(\"hello\") = " + demo.isBlank("hello"));

        System.out.println("\n=== 默认字符集演示 ===");
        demo.demonstrateDefaultCharset();

        System.out.println("\n=== API改进演示 ===");
        demo.demonstrateAPIImprovements();
    }
}
