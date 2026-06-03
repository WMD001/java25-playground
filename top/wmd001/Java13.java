package wmd001;

/**
 * Java 13 主要语法变更（2019）：
 * 1. Switch 表达式增强 - 引入 yield 关键字（预览特性）
 * 2. 文本块（Text Blocks）- 支持多行字符串（预览特性）
 * 3. Socket API 重构（底层实现变更）
 * 4. FileSystems 新增方法
 * 注意：switch 表达式和文本块均为预览特性，需要用 --enable-preview 编译
 *
 * @author WYQ
 * @since 2026/6/3
 */
public class Java13 {

    // ==================== 1. Switch 表达式增强：yield 关键字（预览） ====================

    /**
     * 【预览特性 - 需要用 --enable-preview 编译】
     *
     * Java 12 的 switch 表达式在箭头（->）后只能跟单个表达式。
     * Java 13 增强了 switch 表达式，允许在 case 块中使用多条语句，
     * 并通过 yield 关键字返回值。
     *
     * yield 的作用：
     * - 在 switch 表达式的 case 块中，用于"产出"（yield）一个值
     * - 类似于 return，但只用于 switch 表达式内部
     */
    public String getSeason(int month) {
        // Java 13 预览写法（注释中展示）：
        // return switch (month) {
        //     case 12, 1, 2 -> {
        //         System.out.println("Winter month: " + month);
        //         yield "Winter";
        //     }
        //     case 3, 4, 5 -> "Spring";
        //     case 6, 7, 8 -> "Summer";
        //     case 9, 10, 11 -> "Autumn";
        //     default -> {
        //         yield "Unknown";
        //     }
        // };

        // 传统写法模拟 yield 的逻辑
        switch (month) {
            case 12: case 1: case 2:
                System.out.println("Winter month: " + month);
                return "Winter";
            case 3: case 4: case 5:
                return "Spring";
            case 6: case 7: case 8:
                return "Summer";
            case 9: case 10: case 11:
                return "Autumn";
            default:
                return "Unknown";
        }
    }

    /**
     * yield 可以返回复杂表达式的结果
     */
    public int classifyNumber(int num) {
        // 预览写法：
        // return switch (num) {
        //     case 0 -> 0;
        //     default -> {
        //         int result = num * num;
        //         yield result;  // 返回计算结果
        //     }
        // };

        // 传统写法模拟
        switch (num) {
            case 0: return 0;
            default:
                int result = num * num;
                return result;
        }
    }

    // ==================== 2. 文本块（Text Blocks）（预览） ====================

    /**
     * 【预览特性 - 需要用 --enable-preview 编译】
     *
     * Java 13 引入了文本块（Text Blocks）的预览版，用于简化多行字符串的编写。
     *
     * 语法：以三个双引号 """ 开头（必须换行），以三个双引号 """ 结尾
     *
     * 特点：
     * - 自动处理换行符，无需手动添加 \n
     * - 自动去除公共前导空白（re-indentation）
     * - 支持转义字符
     * - 结尾 """ 的位置决定缩进的去除
     */
    public void textBlocks() {
        // 传统写法：JSON 字符串（难以阅读和维护）
        String jsonOld = "{\n" +
                "  \"name\": \"Alice\",\n" +
                "  \"age\": 30,\n" +
                "  \"city\": \"Beijing\"\n" +
                "}";

        // Java 13 文本块写法（预览）：更直观，所见即所得
        // 注意：编译时需要 --enable-preview
        // String jsonNew = """
        //         {
        //           "name": "Alice",
        //           "age": 30,
        //           "city": "Beijing"
        //         }
        //         """;

        // 模拟文本块的效果（传统写法）
        String jsonNew = "{\n" +
                "  \"name\": \"Alice\",\n" +
                "  \"age\": 30,\n" +
                "  \"city\": \"Beijing\"\n" +
                "}\n";

        System.out.println("JSON (old way):");
        System.out.println(jsonOld);
        System.out.println("JSON (text block style):");
        System.out.println(jsonNew);

        // 传统 HTML 写法
        String htmlOld = "<html>\n" +
                "  <body>\n" +
                "    <p>Hello</p>\n" +
                "  </body>\n" +
                "</html>";

        // 文本块 HTML 写法（预览）：
        // String htmlNew = """
        //         <html>
        //           <body>
        //             <p>Hello</p>
        //           </body>
        //         </html>
        //         """;

        System.out.println("\nHTML:");
        System.out.println(htmlOld);

        // 传统 SQL 写法
        String sqlOld = "SELECT id, name, email\n" +
                "FROM users\n" +
                "WHERE age > 18\n" +
                "ORDER BY name";

        // 文本块 SQL 写法（预览）：
        // String sqlNew = """
        //         SELECT id, name, email
        //         FROM users
        //         WHERE age > 18
        //         ORDER BY name
        //         """;

        System.out.println("\nSQL:");
        System.out.println(sqlOld);
    }

    /**
     * 文本块的缩进规则：
     * - 开头 """ 之后必须换行
     * - 结尾 """ 的位置决定了去除多少前导空白
     * - 内容相对于结尾 """ 进行缩进
     */
    public void textBlockIndentation() {
        // 结尾 """ 靠左：所有内容都向右缩进
        // String block1 = """
        //         Hello
        //         World
        //         """;
        // 等效于 "    Hello\n    World\n"

        // 结尾 """ 靠内容对齐：去除公共前导空白
        // String block2 = """
        //             Hello
        //             World
        //         """;
        // 等效于 "    Hello\n    World\n"

        System.out.println("Text blocks help maintain clean indentation in multi-line strings.");
    }

    /**
     * 文本块支持转义序列：
     * - \n 换行（但文本块本身就支持换行，通常不需要）
     * - \ 续行（不产生换行符，用于将长行拆分显示）
     * - \s 空格（确保尾部空格不被去除）
     * - \t 制表符
     */
    public void textBlockEscapes() {
        // \ 续行符示例（预览）：
        // String longLine = """
        //         This is a very long line that we want to \
        //         display on multiple source lines but render \
        //         as a single line in the output.""";
        // 结果："This is a very long line that we want to display on multiple source lines but render as a single line in the output."

        // \s 空格示例（预览）：
        // String withTrailingSpaces = "Hello   \s";
        // 保证尾部有 3 个空格

        System.out.println("Text blocks support escape sequences: \\ (line continuation), \\s (preserve spaces)");
    }

    // ==================== main ====================

    public static void main(String[] args) {
        Java13 demo = new Java13();

        System.out.println("=== 1. Switch Expression with yield (Preview) ===");
        System.out.println("Month 1: " + demo.getSeason(1));
        System.out.println("Month 6: " + demo.getSeason(6));
        System.out.println("classifyNumber(5): " + demo.classifyNumber(5));

        System.out.println("\n=== 2. Text Blocks (Preview) ===");
        demo.textBlocks();

        System.out.println("\n=== 3. Text Block Indentation ===");
        demo.textBlockIndentation();

        System.out.println("\n=== 4. Text Block Escapes ===");
        demo.textBlockEscapes();
    }
}
