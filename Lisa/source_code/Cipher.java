

import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.List;
import java.util.Random;

public class Cipher {
    public static final char[] ALPHABET_LETTERS_AND_SPACE_CHAR = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', ' '};

  

    private boolean bugEmptyFile;
    private boolean bugPara;
    private boolean bugParse;
    private int count = 0;
    
    public Cipher (boolean b0, boolean b1, boolean b2)
    {
        bugEmptyFile = b0;
        bugPara = b1;
        bugParse = b2;
        count = 0;
    }
    
    private List<String> readFile(String file,Boolean bugEmptyFile,Boolean bugPara) throws Exception {
        Charset charset = Charset.forName("UTF-8");
        List<String> lines = null;
        try {
            lines = Files.readAllLines(FileSystems.getDefault().getPath(file), charset);
        } catch (Exception e) {
            System.err.println("Invalid input file! (stacktrace follows)");
            e.printStackTrace(System.err);
            System.exit(1);
        }
        System.out.println("there are " + lines.size() + " test cases to be executed");
        if (bugEmptyFile) {
        	for(int cp =0;cp<lines.size();cp++){
        		if (lines.get(cp).toString().length()==0) {
        			cp++;
        			System.out.println("Number of instructions executed before exception: " + cp);
        			count=cp;
        			throw new Exception("The empty execution bug has been detected!");
        		}
        	}
        }
        
        if(bugPara){
        	for(int cp =0;cp<lines.size();cp++){
        		if (lines.get(cp).toString().length()>100) {
        			cp++;
        			System.out.println("Number of instructions executed before exception: " + cp );
        			count=cp;
        			throw new Exception("The execution_file_is_too_long_bug has been detected!");
        		}
        	}
        }
        return lines;
    }
    
    
    public static void main(String[] args) throws Exception {
        int cp = 0;
        if (args.length != 4) {
            System.err.println("(Usage: java Cipher <inputfile> <bug0> <bug1> <bug2>\")");
            System.exit(1);
        }
        
        String file = null;
        boolean bugEmptyFile = false;
        boolean bugPara = false;
        boolean bugParse = false;

        try {
        	file = args[0];
            bugEmptyFile = Boolean.parseBoolean(args[1]);
            bugPara = Boolean.parseBoolean(args[2]);
            bugParse = Boolean.parseBoolean(args[3]);
        } catch (Exception e) {
            System.err.println("Invalid boolean arguments provided! (stacktrace follows)");
            e.printStackTrace(System.err);
            System.exit(1);
        }

        
        
        try {
            List<String> plainText = new Cipher(bugEmptyFile,bugPara,bugParse).readFile(file,bugEmptyFile,bugPara);
            for (int i = 0; i < plainText.size(); i++) {
                String message = plainText.get(cp);
                char[] plainTextChars = message.toCharArray();

                char[] lettersAndSpace = ALPHABET_LETTERS_AND_SPACE_CHAR;

                char[] mapping = createCipher(lettersAndSpace);

                String cipherText = encrypt(plainTextChars, mapping, lettersAndSpace,bugParse);

                System.out.println("plain Text: "+message);
                System.out.println("cipher message: "+cipherText);
                cp += 1;
            }

//        } catch (NullPointerException exception) {
//            System.err.println("This stimulates an empty file bug");
//        } catch (java.util.NoSuchElementException exception) {
//            System.err.println("This stimulates an empty file bug");
            
        }
       
        catch (ArrayIndexOutOfBoundsException exception) {
        	cp++;
            System.err.println("The non_letter or non_space bug has been detected!");
            System.out.println("Number of instructions executed before exception: " + cp);
            System.exit(1);   
        }
    }

    
    public static char[] createCipher(char[] lettersAndSpace) {
        char[] mapping = new char[lettersAndSpace.length];
        System.arraycopy(lettersAndSpace, 0, mapping, 0, lettersAndSpace.length);

        Random generator = new Random();

        for (int index = 0; index < mapping.length; index++) {
            int otherIndex = generator.nextInt(mapping.length);

            char temp = mapping[index];

            mapping[index] = mapping[otherIndex];
            mapping[otherIndex] = temp;
        }
        return mapping;
    }

    public static String encrypt(char[] plainTextChars, char[] mapping, char[] lettersAndSpace, Boolean bugParse) throws ArrayIndexOutOfBoundsException {
        char[] cipherTextChars = new char[plainTextChars.length];
        try {
        	 for (int index = 0; index < cipherTextChars.length; index++) {
                 int count1 = 0;
                 while (plainTextChars[index] != lettersAndSpace[count1]) {
                     count1++;      
                 }
                 cipherTextChars[index] = mapping[count1];
             }
        } catch(Exception e){
        	if(bugParse) {

        		throw new ArrayIndexOutOfBoundsException();
        	}else {
        		//do nothing
        	}
        }
        String cipherText = new String(cipherTextChars);
        return cipherText;
    }
    
    public int getCount(){
        return count;
    }
    

}
