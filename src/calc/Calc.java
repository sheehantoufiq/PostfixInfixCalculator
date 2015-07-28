/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package calc;

/**
 * 
 * Calc: A program that uses the Stack data structure to calculate an infix expression by converting it to postfix, and takes a variable x.
 *
 * @author sheehantoufiq
 * 
 * Problem: Convert an infix expression into a postfix expression. Prompt for a variable x. Calculate the expression and output the result. 
 * If there are any errors, the program must output the error and exit the program.
 * 
 * Calc Class: 
 * Public class to run main method and program. Contains following methods: 
 *      void main()             --> prompts for user input, runs methods, prompts for variable, runs simple tests against input variable.
 *      void illegalInput()     --> runs switch statement returning Strings stating corresponding error
 *      String postFixEngine()  --> builds postfix expression string from infix expression.
 *      String checkStack()     --> checks to see the order of operations when building postfix expression string.
 *      void testInput()        --> runs tests against user's input expression.
 *      int calculator()        --> takes postfix string as a parameter and calculates result. returns result as a string.
 *      void doOperator()       --> pops last 2 operands from stack and performs arithmetic based on parameter.
 *      boolean isOperator()    --> method to check if a parameter character is an operator ('+', '-', '/', '*', '%').
 * 
 * Algorithm Used:
 * Step 1: Prompt for infix expression.
 * Step 2: Replace all special characters and spaces.
 * Step 3: Call postfixEval() with infix String as input.
 * Step 4: Check to see if illegal input.
 * Step 5: If illegal input terminate.
 * Step 6: Else call isOperator for each character[i].
 * Step 7: If isOperator false, add char to String conversion.
 * Step 8: If isOperator true, check to see character priority. Parenthesis vs Add, Sub, Mul, Div.
 * Step 9: Pop and push to stack depending on priority, add chars to String conversion
 * Step 10: Return postfix expression and print to screen.
 * Step 11: Ask for variable x.
 * Step 11: Test to see if legal input.
 * Step 12: If illegal terminate.
 * Step 13: Else call calculator() with parameter postfix String.
 * Step 14: If digit, push to stack.
 * Step 15: If operator, call doOperator with parameter operator char.
 * Step 16: Pop top 2 digits in stack and perform arithmetic. Push result onto stack.
 * Step 17: Repeat until end of String and all operators have had an operation.
 * Step 18: Return result to main() method
 * Step 19: Print result onto screen
 * Step 20: End program successfully
 * 
 * Data Structures Used: Stack
 * 
 */

import java.util.*;
public class Calc {
    
    /*
     * Global Stack variables, opStack for postfix conversion, dataStack for calculating postfix expression
     */
    public static Stack<Integer> dataStack = new Stack();
    public static Stack opStack = new Stack();
    
    /*
     * illegalInput Method --> runs switch statement returning Strings stating corresponding error
     * @param {int} n
     * @return void
     * Precondition: Must have an int parameter 1-6. There must be an error.
     * Postcondition: A line is printed with corresponding error. System exits.
     */    
    public static void illegalInput(int n) {
        String output = "Error in expression! ";
        switch(n) {
            case 1: output += "There must be 1 or more variables x. ";
                    break;
            case 2: output += "First and last token must be an operand. ";
                    break;
            case 3: output += "Cannot accept floating numbers. ";
                    break;
            case 4: output += "Missing operand. ";
                    break;
            case 5: output += "Must be a number or q to quit. ";
                    break;
            case 6: output += "Please enter 1 number. ";
                    break;
        }
        System.out.println(output);
        System.exit(0);
    }
    
    /*
     * postfixEngine Method --> builds postfix expression string from infix expression, and returns postfix expression.
     * @param {String} infix --> infix expression entered by user
     * @return String --> converted postfix expression
     * Precondition: Must have a String infix parameter. Infix expression must not have any errors and must have been tested
     *               for illegal inputs. Must have new Stack opStack initialized.
     * Postcondition: A postfix expression is returned as a String.
     */    
    public static String postfixEngine(String infix) {
        String conversion = "";
        boolean firstChar = true;   
        for (int i = 0; i < infix.length(); i++) {
            char currentChar  = infix.charAt(i);
            testInput(currentChar, i, infix.length());
            if (!isOperator(currentChar)) {
                conversion += Character.toString(currentChar);
                if (i == infix.length()-1) {
                    while(!opStack.empty()) {
                        conversion += opStack.pop();
                    }
                }
            } else {
                if (firstChar == false) {
                    if (currentChar == '(') {
                        opStack.push(currentChar);
                    } else if (currentChar == ')') {
                        while (opStack.peek() != '(') {
                            conversion += opStack.pop();
                        }
                        opStack.pop();
                    }
                    conversion += checkStack(currentChar, i, infix.length());
                } else {
                    opStack.push(currentChar);
                    firstChar = false;
                }
            }
        }
        conversion = conversion.replaceAll("\\(","");
        conversion = conversion.replaceAll("\\)","");
        return conversion;
    }
    
    /*
     * checkStack Method --> checks to see the order of operations when building postfix expression string.
     * @param {char} currentChar --> current character within an infix string
     * @param {int} i --> integer of index within an infix string
     * @param {int} n --> integer of the length of an infix string
     * @return String
     * Precondition: Must have a current character, index of char in string, and length of infix string. Must come from postfixEngine.
     * Postcondition: If the current char has a priority over last char, or if the string is ended, it pops top of stack and returns it as a string.
     *                It pushes the currentChar onto the stack.
     */        
    public static String checkStack(char currentChar, int i, int n) {
        String returnString = "";
        if ((currentChar == '*' || currentChar == '/' || currentChar == '%') && (opStack.peek() == '+' || opStack.peek() == '-')) {
            returnString += opStack.pop();
        }
        opStack.push(currentChar);
        if (i == n-1) {
            while (!opStack.empty()) {
                returnString += opStack.pop();
            }
        }
        return returnString;
    }
    
    /*
     * testInput Method --> runs tests against user's input expression.
     * @param {char} currentChar --> current character within an infix string
     * @param {int} i --> integer of index within an infix string
     * @param {int} n --> integer of the length of an infix string
     * @return void
     * Precondition: Must have a current character, index of char in string, and length of infix string. Must come from postfixEngine
     * Postcondition: Tests the infix string. If illegal, runs illegalInput with corresponding parameter.
     */     
    public static void testInput(char currentChar, int i, int n) {
        if (currentChar == '.') {
            illegalInput(3);
        }
        if (!opStack.empty()) {
            char last = opStack.peek().toString().charAt(0);
            if (isOperator(currentChar) && last == '(') {
                illegalInput(4);
            } else if (isOperator(currentChar) && isOperator(last)) {
                illegalInput(4);
            } else if (isOperator(currentChar) && i == n-1) {
                illegalInput(2);
            } else if (currentChar == '(' && i == n-1) {
                illegalInput(2);
            }
        } else if (i == 0) {
            if (currentChar == ')' || isOperator(currentChar)) {
                illegalInput(3);
            }
        }
    }
    
    /*
     * calculator Method --> takes postfix string as a parameter and calculates result. returns result as a string.
     * @param {String} x --> String of postfix expression
     * @return int --> Result of calculated postfix expression
     * Precondition: Must have an already converted postfix String as a parameter. Must have new Stack dataStack initialized.
     * Postcondition: Returns the value of the result of the postfix expression.
     */     
    private static int calculator(String x) {
        int intval = 0;
	char c;
        int i = 0;
        while (i < x.length()) {
            c = x.charAt(i);
            switch (c) {
                case '0': 
                case '1': 
                case '2': 
                case '3': 
                case '4':
                case '5': 
                case '6': 
                case '7': 
                case '8': 
                case '9':
                    intval = Character.getNumericValue(c);     
                    dataStack.push(intval);
                    break;
	        case '+':
                    doOperator('+');
                    break;
	        case '-':
                    doOperator(c);
                    break;
	        case '*':
                    doOperator(c);
                    break;
	        case '/':
                    doOperator(c);
                    break;
                case '%':
                    doOperator(c);
                    break;
	    }
            i++;
        }
        return dataStack.pop();
    }
    
    /*
     * doOperator Method --> pops last 2 operands from stack and performs arithmetic based on parameter.
     * @param {char} operation --> Char of operator
     * @return void
     * Precondition: Must have an operation based on if operator in Stack dataStack. Must have already been tested for priority. 
     * Postcondition: Pushes the result of the operation of the two top numbers in stack
     */   
    private static void doOperator(char operation) {
	int right = dataStack.pop();
    	int left = dataStack.pop();
    	int result = 0;
    	switch (operation) {
            case '+':
            	result = left + right;
                break;
            case '-':
                result = left - right;
                break;
            case '*':
                result = left * right;
                break;
            case '/':
                result = left / right;
                break;
            case '%':
                result = left % right;
                break;
        }
        dataStack.push(result);
    }
    
    /*
     * isOperator Method --> method to check if a parameter character is an operator ('+', '-', '/', '*', '%').
     * @param {char} op --> Char of operator
     * @return boolean
     * Precondition: Must have a char as a parameter.
     * Postcondition: Return true if the char parameter is an operator ('+', '-', '/', '*', '%'). Returns false otherwise.
     */ 
    public static boolean isOperator(char op) {
        switch(op) {
            case '%':
                return true;
            case '+':
                return true;
            case '-':
                return true;
            case '*':
                return true;
            case '/':
                return true;
            default:
                return false;
        }
    }
    
    /*
     * main Method --> prompts for user input, runs methods, prompts for variable, runs simple tests against input variable.
     * @param {String[]} args
     * @return void
     * Precondition: Must have all methods initialized. Must have all global variables initialized.
     * Postcondition: Must have result for user input or exit.
     */ 
    public static void main(String[] args) {
        System.out.print("Enter infix expression: ");
        Scanner in = new Scanner(System.in);
        String infix = in.nextLine();
        infix = infix.replaceAll("\\s+","");
        infix = infix.replaceAll("\0","");
        boolean var = false;
        for (int i = 0; i < infix.length(); i++) {
            if (infix.charAt(i) == '.') {
                illegalInput(3);
            } else if (infix.charAt(i) == ' ') {
                illegalInput(6);
            } else if (Character.isLetter(infix.charAt(i))) {
                if (infix.charAt(i) == 'x') {
                    var = true;
                } else {
                    illegalInput(1);
                }
            }
        }
        if (var == false) {
            illegalInput(1);
        }            
        String postfix = postfixEngine(infix);
        System.out.println("Converted expression: " + postfix);
        System.out.print("Enter a value for x: ");
        String xInput = in.next();
        int x;
        if (xInput.equals("q")) {
            System.exit(0);
        } else {
            for (int i = 0; i < xInput.length(); i++) {
                if (xInput.charAt(i) == '.') {
                    illegalInput(3);
                } else if (xInput.charAt(i) == ' ') {
                    illegalInput(6);
                }
            }
            if (var == false) {
                illegalInput(1);
            }
            try {
                x = Integer.parseInt(xInput);
            } catch(NumberFormatException e) {
                illegalInput(5);
            }
        }
        postfix = postfix.replaceAll("x", xInput);
        int answer = calculator(postfix);
        System.out.println("The result is: " + answer);
    }
}
