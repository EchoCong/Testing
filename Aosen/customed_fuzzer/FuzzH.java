import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class FuzzH {
    private static final String VALID_INPUT = "QXJ0IG9mIENvbXB1dGVyIFByb2dyYW1taW5nLCBWb2x1bWUgMSwgRmFzY2ljbGUgMSwgVGhlOiBNTUlYIC0tIEEgUklTQyBDb21wdXRlciBmb3IgdGhlIE5ldyBNaWxsZW5uaXVt";
    public static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890+-=~!@#$%^&*()abcdefghijklmnopqrstuvwxyz{}|';?/.,><\"' ";
    private static final int LEN = VALID_INPUT.length();
    private static final int NUMOFTESTS = 100;   // generate x testcase
    private static final int PARTS = LEN / 10;
    public static final int WORDS_EACHPART = LEN / PARTS;
    public static Random random = new Random();

    public static void main(String[] args) throws IOException {
        FuzzH fuzzH = new FuzzH();
        File file = fuzzH.createFile();
        for (int i = 0; i < NUMOFTESTS; i++) {
            String mutated_msg = fuzzH.control_mutate();
            fuzzH.writeFuzz(file, mutated_msg);
        }
    }

    private File createFile() throws IOException {
        File file = new File("fuzzh.s");
        if (!file.exists()) file.createNewFile();
        return file;
    }

    private String control_mutate() throws IOException {
        StringBuilder sb = new StringBuilder(VALID_INPUT);
        int start = random.nextInt(PARTS) * WORDS_EACHPART;
        System.out.println(start);
        for (int i = start; i < start + WORDS_EACHPART; i++) {
            Operation op = randomOperation();
            op.mutate(sb, start);
        }
        String mutated_msg = sb.toString();
        return mutated_msg;
    }

    private void writeFuzz(File file, String message) throws IOException {
        FileWriter fileWriter = new FileWriter(file, true);
        fileWriter.write(message + "\n");
        fileWriter.close();
    }

    private Operation randomOperation() {
        int pick = random.nextInt(Operation.values().length);
        return Operation.values()[pick];
    }
}

enum Operation {
    Insertboolean,
    InsertChar,
    InsertNumber,
    Append,
    Delete,
    Replace,
    Remove,
    KEEP,
    REVERSE;

    public void mutate(StringBuilder sb, int start) {
        int offset = FuzzH.random.nextInt(start + 1) + FuzzH.WORDS_EACHPART;
        System.out.println("offset: " +offset+"\n"+"offet%cap: "+offset%sb.capacity());
        System.out.println(sb.length());
        try {
            switch (this) {
                case Insertboolean:
                    if (offset % 2 == 0) {
                        sb.insert(offset % sb.length(), false);
                    } else
                        sb.insert(offset % sb.length(), true);
                    break;
                case InsertChar:
                    sb.insert(offset % sb.length(), FuzzH.CHARACTERS.charAt(FuzzH.random.nextInt(90)));
                    break;
                case InsertNumber:
                    sb.insert(offset % sb.length(), offset);
                    break;
                case Append:
                    sb.append(FuzzH.CHARACTERS.charAt(FuzzH.random.nextInt(90)));
                    break;
                case Delete:
                    sb.delete(offset % sb.length(), offset + 3);
                    break;
                case Remove:
                    sb.delete(0, sb.length() / 2);
                    break;
                case Replace:
                    sb.replace(offset%sb.length(), offset+1,"HelloWorld*@&#^^$!");
                    break;
                case KEEP:
                    //do
                    break;
                case REVERSE:
                    sb.reverse();
                    break;
                default:
                    //do
            }
        } catch (Exception e) {
            // do nothing
        }

    }
}