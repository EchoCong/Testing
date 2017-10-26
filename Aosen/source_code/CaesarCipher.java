import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class CaesarCipher {
    private final String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"; // 26+26+10
    private final String characters = "~!@#$%^&*()_+{}|:<>?abcdefghijklmnopqrstuvwxyz"; // length of 18+26
    private String message;
    private int shiftFactor;
    private boolean use_div; // whether add extra algorithm or not

    public static void main(String[] args) throws Exception {
        if (args.length != 8) {
            System.err.println("Usage: java CaesarCipher <inputfile> <bug0> <bug1> <bug2> <bug3> <bug4> <bug5> <bug6>\")");
            System.exit(1);
        }
        CaesarCipher caesarCipher = new CaesarCipher();
        String fuzzname = args[0];
        boolean bugEmptyFile = false;
        boolean bugNoenoughPara = false;
        boolean bugParse = false;
        boolean bugDivZero = false;
        boolean bugIntOverflow = false;
        boolean bugNoCorsb = false;
        boolean bugBadPara = false;
        try {

            bugEmptyFile = Boolean.parseBoolean(args[1]);
            bugNoenoughPara = Boolean.parseBoolean(args[2]);
            bugParse = Boolean.parseBoolean(args[3]);
            bugDivZero = Boolean.parseBoolean(args[4]);
            bugIntOverflow = Boolean.parseBoolean(args[5]);
            bugNoCorsb = Boolean.parseBoolean(args[6]);
            bugBadPara = Boolean.parseBoolean(args[7]);
        } catch (Exception e) {
            System.err.println("Invalid boolean arguments provided! (stacktrace follows)");
            e.printStackTrace(System.err);
            System.exit(1);
        }
        caesarCipher.run(fuzzname, bugEmptyFile, bugNoenoughPara, bugParse, bugDivZero, bugIntOverflow, bugNoCorsb, bugBadPara);
    }

    private void run(String fuzzname, Boolean bugEmptyFile, Boolean bugNoenoughPara, Boolean bugParse, Boolean bugDivZero, Boolean bugIntOverflow, Boolean bugNoCorsb, Boolean bugBadPara) throws Exception {
        List<String> lines = readFile(fuzzname);
        for (int pc = 0; pc < lines.size(); pc++) {
            String[] ins_and_msg = analyzeContent(lines, bugNoenoughPara, bugEmptyFile, pc);
            encrypt(ins_and_msg, bugParse, bugDivZero, bugIntOverflow, bugNoCorsb, bugBadPara);
            System.out.println(pc + 1 + " ins executed" + "\n");
        }
    }

    /**
     * Read txt file
     *
     * @return String content
     * @throws Exception
     */

    private List<String> readFile(String fuzzname) throws Exception {
        Charset charset = Charset.forName("UTF-8");
        List<String> lines = null;
        try {
            lines = Files.readAllLines(FileSystems.getDefault().getPath(fuzzname), charset);
        } catch (Exception e) {
            System.err.println("Invalid input file! (stacktrace follows)");
            e.printStackTrace(System.err);
            System.exit(1);
        }
        System.out.println("there are " + lines.size() + " test cases to execute");
        return lines;
    }

    /**
     * AnalyazeContent
     *
     * @param lines
     * @return the instruction and information as a String array
     * @throws Exception
     */
    private String[] analyzeContent(List<String> lines, Boolean bugNoenoughPara, Boolean bugEmptyFile, int pc) throws Exception {
        String[] ins_and_msg = lines.get(pc).split(";");
        if (bugNoenoughPara) {
            for (int i = 0; i < ins_and_msg.length; i++) {
                System.out.println(ins_and_msg[i]);
            }
            if (ins_and_msg.length != 3 && ins_and_msg.length != 1) throw new Exception("This stimulates the not enough Parameter Bug");
        }
        if (bugEmptyFile) {
            if (lines.get(pc).length() == 0) {
                throw new Exception("This stimulates the file empty bug");
            }
        }
        return ins_and_msg;
    }

    /**
     * Encrypt the message based on instruction
     *
     * @param ins_and_msg
     * @return the encrypted message
     */

    private String encrypt(String[] ins_and_msg, Boolean bugParse, Boolean bugDivZero, Boolean bugIntOverflow, Boolean bugCorsb, Boolean bugBadPara) throws Exception {
        String output_Mesg = "";
        try {
            shiftFactor = Integer.parseInt(ins_and_msg[0]);
            use_div = Boolean.parseBoolean(ins_and_msg[1]);
        } catch (Exception e) {
            if (bugParse && !(ins_and_msg[0].isEmpty()) && ins_and_msg.length == 3)
                throw new Exception("This bug simulates an invalid memory access during parsing a non-integer register number.");
        }
        try {
            message = ins_and_msg[2];
        } catch (Exception e) {
            // do nothing
        }
        HashMap<Character, Character> alphaMap = new HashMap<>();
        System.out.println("message: " + message);
        for (int i = 0; i < alphabet.length(); i++) {
            alphaMap.put(alphabet.charAt(i), alphabet.charAt((i + shiftFactor) % 52));
        }
        if (bugBadPara && message != null) {
            for (int i = 0; i < message.length(); i++) {
                if (!Character.isLetterOrDigit(message.charAt(i))) {
                    throw new Exception("This bug stimulates a bad parameter bug, the message can not include symbol");
                }
            }
        }
        try {
            for (int i = 0; i < message.length(); i++) {
                output_Mesg = output_Mesg.concat(alphaMap.get(message.charAt(i)).toString());
            }
        } catch (Exception e) {
            // do nothing
        }
        if (use_div) {
            output_Mesg = extra_encrpt(output_Mesg, bugDivZero, bugIntOverflow, bugCorsb);
        }
        System.out.println("cipher: " + output_Mesg);
        return output_Mesg;
    }

    private String extra_encrpt(String cipher, Boolean bugDivZero, Boolean bugIntOverflow, Boolean bugCorsb) throws Exception {
        Random random = new Random();
        int weight = 0;
        for (int i = 0; i < cipher.length(); i++) {
            int contributor = alphabet.indexOf(cipher.charAt(i));
            weight = i % 2 == 1 ? weight + contributor : weight - contributor;       // according to the odd and even feature to calculate feature
        }
        int div_seed = weight >= 0 ? random.nextInt(6) : 6;    // if weight >= 0, div_seed is a random variable, otherwise, its fixed
        int mul_seed = weight < 0 ? random.nextInt(Integer.MAX_VALUE) : 6;          // if weight < 0, div_seed is a random variable, otherwise, its fixed
//        System.out.println("weight: " + weight);
//        System.out.println("div_seed: " + div_seed);
//        System.out.println("mul_seed: " + mul_seed);
//        System.out.println("length: " + cipher.length());
        if (bugDivZero) {
            if (div_seed == 0) throw new Exception("This stimulates the Division by Zero bug");
        }
        if (bugIntOverflow) {
            if (Math.abs(weight) * mul_seed < mul_seed) {
                throw new Exception("This stimulates the Integer Overflow bug");
            }
        }
        System.out.println(div_seed);
        int adjust_pos1 = div_seed == 0 ? 1 : weight % div_seed % (cipher.length()+1);
        int adjust_pos2 = weight * mul_seed % (cipher.length()+1);
        int varX = 0;
        int varY = 0;
        char[] tem = cipher.toCharArray();
        try {
            varX = alphabet.indexOf(tem[adjust_pos1]);
            varY = alphabet.indexOf(tem[adjust_pos2]);
        } catch (Exception e) {
            // do nothing
        }
        if (bugCorsb) {
            if (varX > characters.length() || varY > characters.length()) {
                throw new Exception("this bug stimulates the noCorsb Bug");
            }
        }
        try {
            tem[adjust_pos1] = characters.charAt(varX);
            tem[adjust_pos2] = characters.charAt(varY);
        } catch (Exception e) {
            // do nothing
        }
        cipher = String.valueOf(tem);
        return cipher;
    }
}


