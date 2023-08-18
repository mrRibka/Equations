package com.example.equations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.jexl3.*;

import java.util.List;

@Service
public class EquationService {

    private final EquationRepository equationRepository;

    @Autowired
    public EquationService(EquationRepository equationRepository) {
        this.equationRepository = equationRepository;
    }

    public void save(Equation equation) {
        equationRepository.save(equation);
    }

    public Equation findById(long id){
        return equationRepository.findById(id).get();
    }

    public List<Equation> findAll(){
        return equationRepository.findAll();
    }

    public List<Equation> findEquationsByRoot(Double root) {
        return equationRepository.findEquationsByRootsValue(root);
    }

    private static boolean isValidEquation(String equation) {
        String equationWithoutSpaces = equation.replaceAll(" ", "");

        if (equationWithoutSpaces.isEmpty()) {
            return false;
        }


        if (!equationWithoutSpaces.matches("^(?:[+\\-*/xX()]|\\d+(?:\\.\\d+)?)*$")) {
            return false;
        }

        int openBracketCount = 0;
        int closeBracketCount = 0;
        for (char c : equationWithoutSpaces.toCharArray()) {
            if (c == '(') {
                openBracketCount++;
            } else if (c == ')') {
                closeBracketCount++;
                if (closeBracketCount > openBracketCount) {
                    return false;
                }
            }
        }
        if (openBracketCount != closeBracketCount) {
            return false;
        }

        String operators = "+-*/";
        for (int i = 0; i < equationWithoutSpaces.length(); i++) {
            char c = equationWithoutSpaces.charAt(i);
            if (operators.contains(Character.toString(c)) && c != 'x') {
                if (i == 0) {
                    if (c == '-') {
                        continue;
                    }
                    return false;
                }
                char prevChar = equationWithoutSpaces.charAt(i - 1);
                if (i == equationWithoutSpaces.length() - 1) {
                    return false;
                }

                if (operators.contains(Character.toString(prevChar))) {
                    if (operators.contains(Character.toString(prevChar)) && c == '-') {
                        continue;
                    }
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean isValidFullEquation(String fullEquation) {
        String[] equationParts = fullEquation.split("=");

        if (equationParts.length != 2) {
            return false;
        }

        String leftPart = equationParts[0];
        String rightPart = equationParts[1];

        return (isValidEquation(leftPart) && isValidEquation(rightPart));
    }

    private static double evaluateEquationWithVariable(String equation, double xValue) {
        JexlEngine jexlEngine = new JexlBuilder().create();
        JexlExpression jexlExpression = jexlEngine.createExpression(equation);

        JexlContext context = new MapContext();
        context.set("x", xValue);

        Object result = jexlExpression.evaluate(context);

        if (result instanceof Number) {
            return ((Number) result).doubleValue();
        } else {
            throw new IllegalArgumentException("Equation result is not a valid number");
        }
    }

    public static boolean checkRoot(String fullEquation, double xValue) {
        String[] equationParts = fullEquation.split("=");

        if (equationParts.length == 2) {
            return evaluateEquationWithVariable(equationParts[0], xValue) == evaluateEquationWithVariable(equationParts[1], xValue);
        }
        return false;
    }



}

