// Christian Ortiz
// April 5th 2022
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
public class PA5 {

    private static final String INPUT = System.getProperty("user.dir") + "/src/"; // for the file directory

    /**
     * The main method does the input handling of the program
     */
    public static void main(String args[]) {
        Scanner keyboard = new Scanner(System.in);
        String choice = "";
        String firstFile, secondFile;
        System.out.println("Welcome!");
        while (!choice.equals("quit")) {
            System.out.println();
            System.out.println("What would you like to do?");
            System.out.println("Options:\n convert source.xxx destination.yyy\n normalize source.xxx\n quit");
            choice = keyboard.next();
            switch (choice) { // switch case for the possible input values
                case "convert":
                    firstFile = keyboard.next();
                    secondFile = keyboard.next();
                    try {
                        converter(INPUT + firstFile, INPUT + secondFile);
                    } catch (Exception e) {
                        System.out.println("File does not exist. Try again.");
                    }
                    break;

                case "normalize":
                    firstFile = keyboard.next();
                    try {
                        normalize(INPUT + firstFile);
                    } catch (Exception e) {
                        System.out.println("File does not exist. Try again.");
                    }

                    break;

                case "quit":
                    System.out.println("Goodbye!");
                    System.exit(0);

                default:
                    System.out.println("That input was incorrect. Try again.");
                    break;

            }
        }
    }

    /**
     * This method takes two files, and converts first file's info into
     * the format of the second file, and stores it there. File types are either
     * .csv or .txt. If the file types are the same, it just copies the info
     * from the first file into the second.
     **/
    public static void converter(String src, String trg) throws Exception
    {
        String srcExt = src. substring(src.length() - 3);
        String trgExt = trg.substring(trg.length() - 3);
        String token, nextToken;
        if(!srcExt.equals("txt") && !srcExt.equals("csv")) // if the source file doesnt end with .txt or .csv, send an exception
            throw new Exception("First file is not .csv or .txt");
        if(!trgExt.equals("txt") && !trgExt.equals("csv")) // if the target file doesnt end with .txt or .csv, send an exception
            throw new Exception("Second file is not .csv or .txt");

        if (src.equals(trg)) // if files are the same, throw an exception, cant be the same
            throw new Exception("same i/o");

        Scanner in = new Scanner(new File(src));
        Scanner lineScanner;
        PrintWriter out = new PrintWriter(trg);
        if(srcExt.equals(trgExt)) // same format
        {
            if (srcExt.equals("txt")) // txt to txt
            {
                while(in.hasNextLine())
                {
                    lineScanner = new Scanner(in.nextLine());
                    lineScanner.useDelimiter("[\t]");
                    while(lineScanner.hasNext())
                        out.print(lineScanner.next() + (lineScanner.hasNext() ? "\t" : in.hasNextLine() ? "\n" : ""));

                    out.flush();
                }

            }
            else // csv to csv
            {
                while(in.hasNextLine())
                {
                    lineScanner = new Scanner(in.nextLine());
                    lineScanner.useDelimiter(",");
                    while(lineScanner.hasNext())
                        out.print(lineScanner.next() + (lineScanner.hasNext() ? "," : in.hasNextLine() ? "\n" : ""));
                }

            }
        }
        else // not the same format
        {
            if (srcExt.equals("txt")) // txt to csv
            {
                while(in.hasNextLine())
                {
                    lineScanner = new Scanner(in.nextLine());
                    lineScanner.useDelimiter("[\t]");
                    while(lineScanner.hasNext())
                        out.print(lineScanner.next() + (lineScanner.hasNext() ? "," : in.hasNextLine() ? "\n" : ""));

                }
            }
            else // csv to txt
            {
                while(in.hasNextLine())
                {
                    lineScanner = new Scanner(in.nextLine());
                    lineScanner.useDelimiter("[,]");
                    while(lineScanner.hasNext())
                    {
                        token = lineScanner.next();
                        if (token.startsWith("\"")) // if the token starts with a " then we add a comma after it
                        {
                            nextToken = lineScanner.next();
                            token = token + "," + nextToken;
                            while(!nextToken.endsWith("\"")) // if the next token doesn't end with a " we continue to concatenate the next string and add another comma between
                                // once the next token ends with a " we can move on
                            {
                                nextToken = lineScanner.next();
                                token = token + "," + nextToken;
                            }
                            token = token + "\t"; // then we add a tab to the end of the quote string
                            out.print(token);
                        }
                        else // a normal token, no quotation marks
                            out.print(token + (lineScanner.hasNext() ? "\t" : in.hasNextLine() ? "\n" : ""));
                    }
                }
            }

        }

        out.close();
        in.close();
    }

    /**
     * This method will normalize the data within the file based on parameters given to each data type
     **/
    public static void normalize(String fileName) throws FileNotFoundException
    {
        ArrayList<String> content = new ArrayList<String>();
        Scanner in = new Scanner(new File(fileName));
        String srcExt = fileName. substring(fileName.length() - 3);
        if(srcExt.equals("txt")) // if the file is .txt, use tab delimiter
          in.useDelimiter("\t");
        else if(srcExt.equals("csv")) // if the file is .csv, use comma delimiter
           in.useDelimiter(",");
        int intValue;
        double doubleValue;
        String stringValue;

        while(in.hasNext())
        {
            content.add(in.next());
        }
        PrintWriter out = new PrintWriter(fileName);
        if(!srcExt.equals("txt") && !srcExt.equals("csv"))
            throw new FileNotFoundException("File is not .txt or .csv");

       for (int i = 0; i < content.size(); i++)
        {
           stringValue = content.get(i);
            if(stringValue.length() == 0) // if the value is empty, print N/A
            {
                if(srcExt.equals("txt")) { // for .txt files
                    out.print("N/A\t");
                    out.flush();
                }
                else { // for .csv files
                    out.print("N/A,");
                    out.flush();
                }
                continue;
            }
            try {
                intValue = Integer.parseInt(stringValue);
                if(srcExt.equals("txt")) // for .txt files
                    out.printf("%+010d\t", intValue);
                else // for .csv files
                    out.printf("%+010d,", intValue);
                out.flush();
            }
            catch(NumberFormatException e) {
                try {
                    doubleValue = Double.parseDouble(stringValue);
                    if(doubleValue > 100.0 || doubleValue < 0.01) // if the value is outside of parameter, use scientific notation
                        if(srcExt.equals("txt")) // for .txt files
                            out.printf("%.2e\t", doubleValue);
                        else // for .csv files
                            out.printf("%.2e,", doubleValue);
                    else // else use normal float notation
                    if(srcExt.equals("txt")) // for .txt files
                        out.printf("%.2f\t", doubleValue);
                    else // for .csv files
                        out.printf("%.2f,", doubleValue);
                    out.flush();


                }catch(NumberFormatException e2) {
                    if (stringValue.length() > 13)
                    {
                        if(srcExt.equals("txt")) // for .txt files
                            out.printf("%.10s...\t", stringValue);
                        else // for .csv files
                            out.printf("%.10s...,", stringValue);
                        out.flush();
                    }
                    else // string is less than 13 characters
                    {
                        if(srcExt.equals("txt")) // for .txt files
                            out.print(stringValue + "\t");
                        else // for .csv files
                            out.print(stringValue + ",");
                        out.flush();
                    }
                }
            }
        }
        in.close();
        out.close();
    }
}