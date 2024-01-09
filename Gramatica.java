import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Gramatica {
	List<Character> VN;
	List<Character> VT;
	char S;
	Map<Character, List<String>> P;

	public Gramatica() {
	    VN = new ArrayList<>();
	    VT = new ArrayList<>();
	    P = new HashMap<>();
	}

	public void citire(List<Character> VN, List<Character> VT, char S, Map<Character, List<String>> P) {
	    this.VN = VN;
	    this.VT = VT;
	    this.S = S;
	    this.P = P;
	}

    public boolean verificare() {
        Set<Character> intersection = new HashSet<>(VN);
        intersection.retainAll(VT);
        if (!intersection.isEmpty()) {
            System.out.println("Verificare failed: VN ∩ VT is not empty.");
            return false;
        }

        if (!VN.contains(S)) {
            System.out.println("Verificare failed: S is not in VN.");
            return false;
        }
        
        for (char nonTerminal : P.keySet()) {
            for (String production : P.get(nonTerminal)) {
                if (!VN.contains(nonTerminal)) {
                    System.out.println("Verificare failed: Rule " + nonTerminal + " -> " + production + " doesn't contain a nonterminal symbol on the left-hand side.");
                    return false;
                }
            }
        }

        if (!P.containsKey(S)) {
            System.out.println("Verificare failed: There is no production with S on the left-hand side.");
            return false;
        }

        for (char nonTerminal : P.keySet()) {
            for (String production : P.get(nonTerminal)) {
                for (char symbol : production.toCharArray()) {
                    if (!VN.contains(symbol) && !VT.contains(symbol)) {
                        System.out.println("Verificare failed: Production " + nonTerminal + " -> " + production + " contains invalid symbols.");
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public void afișare() {
        System.out.println("VN: " + VN);
        System.out.println("VT: " + VT);
        System.out.println("S: " + S);
        System.out.println("P:");
        for (char symbol : P.keySet()) {
            List<String> productions = P.get(symbol);
            for (String production : productions) {
                System.out.println(symbol + " -> " + production);
            }}
    }
  
    public List<String> generare(int numWords) {
        List<String> generatedWords = new ArrayList<>();

        for (int wordCount = 0; wordCount < numWords; wordCount++) {
            StringBuilder word = new StringBuilder();
            List<String> derivationStages = new ArrayList<>();
            String symbols = String.valueOf(S);

            while (symbols.chars().anyMatch(c -> VN.contains((char) c))) {
                StringBuilder newSymbols = new StringBuilder();

                List<Character> nonterminals = symbols.chars()
                        .filter(c -> VN.contains((char) c))
                        .mapToObj(c -> (char) c)
                        .collect(Collectors.toList());

                if (!nonterminals.isEmpty()) {
                    char randomNonterminal = nonterminals.get(ThreadLocalRandom.current().nextInt(nonterminals.size()));

                    boolean nonterminalDerived = false;

                    for (char symbol : symbols.toCharArray()) {
                        if (symbol == randomNonterminal && !nonterminalDerived) {
                            List<String> productions = P.get(symbol);
                            if (productions != null && !productions.isEmpty()) {
                                String randomProduction = productions.get(ThreadLocalRandom.current().nextInt(productions.size()));
                                newSymbols.append(randomProduction);
                                nonterminalDerived = true;
                            } else {
                                newSymbols.append(symbol);
                            }
                        } else {
                            newSymbols.append(symbol);
                        }
                    }

                    word.append(newSymbols.toString());
                    derivationStages.add(newSymbols.toString());
                    symbols = newSymbols.toString();
                } else {
                    break;
                }
            }

            generatedWords.add(word.toString());
            displayDerivation(derivationStages, word.toString());
        }

        return generatedWords;
    }
   
    private void displayDerivation(List<String> stages, String word) {
        System.out.println("Intermediate Stages:");
        for (String stage : stages) {
            System.out.println(stage);
        }
        System.out.println("Generated word: " + stages.get(stages.size() - 1).replace("0", ""));
        System.out.println("------------");
    }

}