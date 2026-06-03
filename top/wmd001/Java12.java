package wmd001;

/**
 * Java 12 主要语法变更（2019）：
 * 1. Switch 表达式（预览特性） - 使用箭头语法 -> 替代传统 case/break
 * 2. String 新方法 - indent() 和 transform()
 * 3. NumberFormat 新增紧凑格式
 * 4. Teeing Collector（Collectors.teeing）
 * 注意：switch 表达式是预览特性，需要用 --enable-preview 编译
 *
 * @author WYQ
 * @since 2026/6/3
 */
public class Java12 {

    // ==================== 1. Switch 表达式（预览特性） ====================

    /**
     * 【预览特性 - 需要用 --enable-preview 编译】
     *
     * Java 12 引入了 Switch 表达式的预览版，主要改进：
     * - 箭头语法（->）：无需 break，不会贯穿（fall-through）
     * - Switch 可以作为表达式返回值
     * - 多个 case 值可以用逗号合并
     */
    public void switchExpressions() {
        String day = "MON";

        // 传统 switch 语句（啰嗦，容易忘记 break）
        int resultOld;
        switch (day) {
            case "MON":
            case "TUE":
            case "WED":
            case "THU":
            case "FRI":
                resultOld = 5;
                break;
            case "SAT":
            case "SUN":
                resultOld = 2;
                break;
            default:
                resultOld = 0;
        }

        // Java 12 switch 表达式（预览）：箭头语法，无需 break，无贯穿
        // 注意：编译时需要 --enable-preview
        // int resultNew = switch (day) {
        //     case "MON", "TUE", "WED", "THU", "FRI" -> 5;
        //     case "SAT", "SUN" -> 2;
        //     default -> 0;
        // };

        // 传统写法用于演示（模拟上面箭头语法的效果）
        int resultNew;
        switch (day) {
            case "MON": case "TUE": case "WED": case "THU": case "FRI":
                resultNew = 5; break;
            case "SAT": case "SUN":
                resultNew = 2; break;
            default:
                resultNew = 0;
        }

        System.out.println("Day " + day + " -> working days: " + resultNew);
    }

    /**
     * 【预览特性 - 需要用 --enable-preview 编译】
     *
     * switch 表达式用于根据类型返回不同描述。
     * 以下用传统语法模拟箭头语法的逻辑。
     */
    public String describeNumber(int num) {
        // Java 12 预览写法（注释中展示）：
        // return switch (num) {
        //     case 0 -> "zero";
        //     case 1 -> "one";
        //     default -> "other: " + num;
        // };

        // 传统写法模拟
        switch (num) {
            case 0: return "zero";
            case 1: return "one";
            default: return "other: " + num;
        }
    }

    // ==================== 2. String 新方法 ====================

    /**
     * Java 12 为 String 类新增了两个方法：
     * - indent(int n)：调整字符串缩进
     * - transform(Function)：对字符串进行函数式转换
     */
    public void stringNewMethods() {
        // indent(int n) - 调整缩进
        // n > 0 增加缩进，n < 0 减少缩进，n == 0 不变但会确保以换行结尾
        String code = "public class Foo {\nint x;\n}";
        System.out.println("--- indent(4) ---");
        System.out.println(code.indent(4));

        System.out.println("--- indent(-1) on indented ---");
        String indented = "    Hello\n    World";
        System.out.println(indented.indent(-2));

        // transform(Function) - 对字符串应用函数并返回结果
        // 可以将一系列字符串操作链式组合
        String input = "  hello, world  ";
        String transformed = input
                .strip()                                    // 去除空白
                .transform(s -> s.replace(",", ""))         // 去除逗号
                .transform(String::toUpperCase)             // 转大写
                .transform(s -> "[" + s + "]");             // 加括号
        System.out.println("transform result: " + transformed); // [HELLO WORLD]

        // transform 也常用于类型转换
        int length = "Hello".transform(String::length);
        System.out.println("length via transform: " + length);
    }

    // ==================== 3. Compact Number Format ====================

    /**
     * Java 12 的 NumberFormat 新增了紧凑格式（Compact Number Format），
     * 可以将数字格式化为更易读的简短形式。
     *
     * 例如：1000 -> "1K", 1000000 -> "1M"
     */
    public void compactNumberFormat() {
        // 需要 import java.text.NumberFormat
        // NumberFormat cnf = NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT);
        // System.out.println(cnf.format(1000));      // 1K
        // System.out.println(cnf.format(1500000));    // 2M
        // System.out.println(cnf.format(2500000000)); // 3B

        java.text.NumberFormat cnf = java.text.NumberFormat.getCompactNumberInstance(
                java.util.Locale.US, java.text.NumberFormat.Style.SHORT);
        System.out.println("1000 -> " + cnf.format(1000));          // 1K
        System.out.println("1500000 -> " + cnf.format(1500000));    // 2M
        System.out.println("250000000 -> " + cnf.format(250000000)); // 3B
    }

    // ==================== 4. Files.mismatch ====================

    /**
     * Java 12 新增了 Files.mismatch() 方法，
     * 用于比较两个文件的内容，返回第一个不同字节的位置。
     * 如果文件完全相同，返回 -1。
     */
    // public void filesMismatch() throws Exception {
    //     Path path1 = Path.of("file1.txt");
    //     Path path2 = Path.of("file2.txt");
    //     long mismatchPos = Files.mismatch(path1, path2);
    //     if (mismatchPos == -1) {
    //         System.out.println("Files are identical");
    //     } else {
    //         System.out.println("Files differ at byte position: " + mismatchPos);
    //     }
    // }

    // ==================== main ====================


    public static void main(String[] args) {
        Java12 demo = new Java12();

        System.out.println("=== 1. Switch Expressions (Preview) ===");
        demo.switchExpressions();

        System.out.println("\ndescribeNumber(0): " + demo.describeNumber(0));
        System.out.println("describeNumber(5): " + demo.describeNumber(5));

        System.out.println("\n=== 2. String New Methods ===");
        demo.stringNewMethods();

        System.out.println("\n=== 3. Compact Number Format ===");
        demo.compactNumberFormat();
    }
}
