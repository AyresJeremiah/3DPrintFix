import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Program to fix Gcode after a 3D print failure. USER MUST KNOW THE X, Y, Z
 * COORDINATES AT TIME OF FAILURE.
 *
 * @author Jeremiah Ayres
 */

public final class Fix3DPrint {

    /*
     * This function gets the last extrude command value from "in"
     *
     * @pram in = Array of string containing the last 100 lines of Gcode. i =
     * current line in the in array.
     *
     * @return e = The E value from the Gcodem
     */
    public static double getE(String[] in, int i) {
        int inLength = in[i].length();
        double e = -1;
        if (in[i].contains("E")) {
            int index = in[i].indexOf("E");

            if (index + 1 < inLength) {
                if (Character.isDigit(in[i].charAt(index + 1))) {
                    String temp2 = in[i].substring(index + 1);
                    int indexEnd = temp2.indexOf(" ");

                    if (indexEnd == -1) {
                        //nothing after the integer Value
                        e = Double.parseDouble(temp2);
                    } else {
                        e = Double.parseDouble(temp2.substring(0, indexEnd));
                        temp2 = temp2.substring(indexEnd);
                    }

                }
            }
            //E Doesn't Exist in the current index string
        } else {
            if (i == 0) {
                i = 100;
            }
            e = getE(in, i--);
        }

        return e;

    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments; unused here
     *
     */
    public static void main(String[] args) throws FileNotFoundException {
        String x, y, z, line, fileName = ".gcode";
        String[] previousLines = new String[100];
        double e;
        int i = 0;

        Scanner usrIn = new Scanner(System.in);
        PrintWriter out = new PrintWriter("data/output.gcode");

        System.out.println("Enter Coordinates: ");

        //Get X,Y,Z
        System.out.println("Enter Gcode file name:");
        fileName = usrIn.nextLine() + fileName;

        File input = new File("data/" + fileName);
        Scanner in = new Scanner(input);

        System.out.println("Enter X: ");
        x = "X" + usrIn.nextLine();
        System.out.println(x);

        System.out.println("Enter Y: ");
        y = "Y" + usrIn.nextLine();

        System.out.println(y);

        System.out.println("Enter Z: ");
        z = "Z" + usrIn.nextLine();
        System.out.println(z);

        System.out.println("Running!");

        while (in.hasNext()) {
            line = in.nextLine();
            if (line.contains(z)) {
                while (in.hasNext()) {

                    //Rotate i back to zero if it is at the end of the array
                    if (i == 100) {
                        i = 0;
                    }
                    //Save previous lines.
                    previousLines[i] = line;
                    i++;

                    if (line.contains(x) && line.contains(y)) {

                        //Print the current position to the output file;
                        e = getE(previousLines, i - 1);
                        //e should be > 0 at this point.
                        out.println("G92 " + x + " " + y + " " + z + " E" + e);

                        line = in.nextLine();
                        out.println(line);
                        while (in.hasNext()) {
                            line = in.nextLine();
                            out.println(line);
                        }
                    } else { //X and Y not fund yet
                        line = in.nextLine();
                    }
                }
            }
        }

        System.out.println();
        System.out.println("COMPLETE!");
        usrIn.close();
        in.close();
        out.close();
    }

}
