
import java.io.PrintWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;

public class Fuzzer {

    public static void main(String[] arg) throws IOException {
    	String infiletext="ABCDEFGHIJKLMNOPQRSTUVWXYZ 0123456789~!@#$%^&*()_+a";
    	String replaceText ="ABCDEFGHIJKLMNOPQRSTUVWXYZ 0123456789~!@#$%^&*()_+=[]'\'.<>?/{},abcdefghijklmnopqrstuvwxyz";
    	String validSky = "QXJ0IG9mIENvbXB1dGVyIFByb2dyYW1taW5nLCBWb2x1bWUgMSwgRmFzY2ljbGUgMSwgVGhlOiBNTUlYIC0tIEEgUklTQyBDb21wdXRlciBmb3IgdGhlIE5ldyBNaWxsZW5uaXVt";
    	String validAosen ="6;true;WelcomeToBeijing2008";
    	String newForSky = new String();
    	String newForAosen = new String();

        int mutNum =100;
        PrintWriter writerForSky = new PrintWriter("fuzz_Lisa_Sky_.s");
        PrintWriter writerForAosen = new PrintWriter("fuzz_Lisa_Aosen_.s");
    	for (int i = 0;i<mutNum;i++){
    		List<String> randomLines = new ArrayList<String>();
    		String newText = new String();
    		randomLines=randomMutation();
    		newText = randomMutText(infiletext,replaceText);
    		newForSky +=randomMutText(validSky,replaceText) +'\n';
    		newForAosen += randomMutText(validAosen,replaceText) + '\n';
            PrintWriter writer = new PrintWriter("inkey" + i + ".txt");
            PrintWriter writer1 = new PrintWriter("infile" + i + ".txt");
            for(int j=0;j<randomLines.size();j++){
            	System.out.println(randomLines.get(j));
            	writer.write(randomLines.get(j)+'\n');  	
            }
            
            writer1.write(newText+'\n');
            writer.close(); 
            writer1.close();

    	}
    	writerForSky.write(newForSky);
    	writerForAosen.write(newForAosen);
    	writerForSky.close();
    	writerForAosen.close();
    	
 }     
           
        
    public static  List<String> randomMutation () throws IOException {
    	Random r = new Random();
    	Charset charset = Charset.forName("UTF-8");
        List<String> lines = null;
        List<String> randomLines = new ArrayList<String>();
        lines = Files.readAllLines(FileSystems.getDefault().getPath("inkey2.txt"), charset);
        int randomIndex = r.nextInt(4);
        String pickOneLine = lines.get(randomIndex);
        System.out.println(pickOneLine);
        String oldString = String.valueOf(pickOneLine.charAt(r.nextInt(pickOneLine.length())));
        System.out.println("oldString "+ oldString);
        String newString = String.valueOf((int)(Math.random()*(20)));
        System.out.println("newString "+ newString);

        pickOneLine = pickOneLine.replace(oldString,newString);
        System.out.println(pickOneLine);
        
        for(int i =0; i<lines.size();i++){
        	if(i!=randomIndex){
            	randomLines.add(i,lines.get(i));

        	}
        	else{
        		randomLines.add(i, pickOneLine);
        	}
        }
        return  randomLines;
        
    }
    
    public static String randomMutText (String infiletext,String replaceText) {
    	
        int randomIndexReplace = (int)(Math.random()*(replaceText.length()));
        int randomIndexInfile = (int)(Math.random()*(infiletext.length()));
        System.out.println("randomIndexReplace: " + randomIndexReplace);
        System.out.println("randomIndexInfile: " + randomIndexInfile);
        String oldString = String.valueOf(infiletext.charAt(randomIndexInfile));
        String newString = String.valueOf(replaceText.charAt(randomIndexReplace));
        System.out.println("oldString1 "+ oldString);
        System.out.println("newString1 "+ newString);
        String newText =infiletext.replace(oldString, newString);
        System.out.println("newText: " + newText);
        return newText;
    }
    

}