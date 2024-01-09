import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    
    public static void main(String[] args) {
        Gramatica gramatica = new Gramatica();
        String currentDirectory = new File("").getAbsolutePath();
        String grammarFilePath = currentDirectory + "/grammar.txt";

        try {
            BufferedReader reader = new BufferedReader(new FileReader(grammarFilePath));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                String key = parts[0].trim();
                String value = parts[1].trim();

                switch (key) {
                    case "VN":
                        List<Character> VN = new ArrayList<>();
                        for (char c : value.toCharArray()) {
                            VN.add(c);
                        }
                        gramatica.VN = VN;
                        break;
                    case "VT":
                        List<Character> VT = new ArrayList<>();
                        for (char c : value.toCharArray()) {
                            VT.add(c);
                        }
                        gramatica.VT = VT;
                        break;
                    case "S":
                        gramatica.S = value.charAt(0);
                        break;
                    case "P":
                        Map<Character, List<String>> P = new HashMap<>();
                        String[] productions = value.split(",");
                        for (String production : productions) {
                            String[] parts2 = production.split("->");
                            char nonTerminal = parts2[0].trim().charAt(0);
                            String[] productionStrings = parts2[1].split("\\|");
                            List<String> productionList = new ArrayList<>();
                            for (String prod : productionStrings) {
                                productionList.add(prod.trim());
                            }
                            P.put(nonTerminal, productionList);
                        }
                        gramatica.P = P;
                        break;
                    default:
                        
                        break;
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (gramatica.verificare()) {
            gramatica.afișare();
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter the number of words to generate: ");
            int numWords = scanner.nextInt();
            List<String> generatedWords = gramatica.generare(numWords);
           
        }
    }
}