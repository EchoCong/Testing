import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class FuzzC {
    private static final int NUMOFTESTS = 100;   // generate x testcase
    private Random random = new Random();
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890+-=~!@#$%^&*()abcdefghijklmnopqrstuvwxyz{}|';?/.,><\"' ";


    public static void main(String[] args) throws IOException {
        FuzzC fuzzc = new FuzzC();
        // fuzzc.createFuzz();
        String filename;
        String keyname;
        for (int i = 1; i <= NUMOFTESTS; i++) {
            filename = fuzzc.cre_inFile(Integer.toString(i));
            keyname = fuzzc.create_inKey(Integer.toString(i));
            // writeFuzz(filename,keyname);
        }
    }

    private List<String> readValidFile(String filepath) {
        Charset charset = Charset.forName("UTF-8");
        List<String> lines = null;
        try {
            lines = Files.readAllLines(FileSystems.getDefault().getPath(filepath), charset);
        } catch (Exception e) {
            System.err.println("Invalid input file! (stacktrace follows)");
            e.printStackTrace(System.err);
            System.exit(1);
        }
        return lines;
    }

    public static void writeFile(String filepath, ArrayList message) throws IOException {
        FileWriter fw = new FileWriter(filepath);

        for (int i = 0; i < message.size(); i++) {
            fw.write(message.get(i).toString());
            fw.write("\n");
        }
        fw.close();
    }

    // private static void writeFuzz(String filename,String keyname) throws IOException{
    //     String filePath = "testcase/fuzz.s";
    //     FileWriter fw = new FileWriter(filePath,true);
    //     System.out.println(filename);
    //     System.out.println(keyname);
    //     fw.write(keyname+" "+filename+"\n");
    //     fw.close();
    // }

    private String cre_inFile(String order) throws IOException {
        String filename = "infile" + order + ".txt";
        File file = new File(filename);
        if (!file.exists()) file.createNewFile();
        List validFile = readValidFile("infile.txt");
        String message = validFile.get(0).toString();
        int mutate_Part = random.nextInt(5);    //random choose part to mutate
        int num_Each_Part = message.length() / 5;
        for (int i = mutate_Part * num_Each_Part; i < mutate_Part * num_Each_Part + num_Each_Part; i++) {
            if (random.nextInt(51) % 9 == 0) {
                message += "\n";
                message += "jkxhzciuglwejknjn2k32438!@*@&&$}|{>>>???><SA";
            } else {
                int mutatePosition = random.nextInt(11);
                System.out.println("mutationPosition: " + mutatePosition);
                if (mutatePosition < 5) continue;
                if (mutatePosition < 7) {
                    char[] messageChar = message.toCharArray();
                    System.out.println("message length" + messageChar.length);
                    System.out.println(i);
                    messageChar[i] = CHARACTERS.charAt(i);
                    message = String.valueOf(messageChar);
                } else if (mutatePosition < 8) {
                    message += "///";
                } else if (mutatePosition < 9) {
                    message += "~!@#@&@";
                } else if (mutatePosition == 10 && random.nextInt(10) == 2) {
                    message = "";
                    break;
                }
            }
        }
        ArrayList<String> myList = new ArrayList<String>(Arrays.asList(message.split("\n")));
        System.out.println(myList);
        writeFile(filename, myList);
        return filename;
    }


    private String create_inKey(String order) throws IOException {
        String filename = "inkey" + order + ".txt";
        File file = new File(filename);
        if (!file.exists()) file.createNewFile();
        List validKey = readValidFile("inkey.txt");
        int mutateline = random.nextInt(validKey.size());
        int replacenum = random.nextInt(2000);
        String start = Integer.toString(replacenum);
        String replacemsg = "";
        for (int i = 0; i < start.length(); i++) {
            replacemsg += start.charAt(i);
            if (i != start.length() - 1) {
                replacemsg += " ";
            }
        }
        validKey.set(mutateline, replacemsg);
        ArrayList mutateKey = new ArrayList(validKey);
        writeFile(filename, mutateKey);
        return filename;
    }

    // private void createFuzz() throws IOException {
    //     File file = new File("testcase/fuzzz.s");
    //     if (!file.exists()) file.createNewFile();
    // }
}
