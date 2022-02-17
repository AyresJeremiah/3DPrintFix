import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Simple HelloWorld program (clear of Checkstyle and FindBugs warnings).
 *
 * @author Jeremiah Ayres
 */
public final class Fix3DPrint {

    public static String parseXYZ(String in, double x, double y, double z) {
        String temp = in;
        double currentX, currentY, currentZ;

        int inLength = in.length();

        //Change X
        if (in.contains("X")) {
            int index = in.indexOf("X");

            if (index + 1 < inLength) {
                if (Character.isDigit(in.charAt(index + 1))) {
                    String temp2 = in.substring(index + 1);
                    String temp3 = in.substring(0, index + 1);
                    int indexEnd = temp2.indexOf(" ");
                    if (indexEnd == -1) {
                        //nothing after the integer Value
                        currentX = Double.parseDouble(temp2);
                    } else {
                        currentX = Double
                                .parseDouble(temp2.substring(0, indexEnd));
                        temp2 = temp2.substring(indexEnd);
                    }
                    x = x - currentX;
                    x = ((double) ((int) (x * 1000))) / 1000;
                    if (indexEnd == -1) {
                        temp = temp3 + x;
                    } else {
                        temp = temp3 + x + temp2;
                    }
                }
            }
        }

        //Change Y
        if (in.contains("Y")) {
            int index = in.indexOf("Y");

            if (index + 1 < inLength) {
                if (Character.isDigit(in.charAt(index + 1))) {
                    String temp2 = in.substring(index + 1);
                    String temp3 = in.substring(0, index + 1);
                    int indexEnd = temp2.indexOf(" ");
                    if (indexEnd == -1) {
                        //nothing after the integer Value
                        currentY = Double.parseDouble(temp2);
                    } else {
                        currentY = Double
                                .parseDouble(temp2.substring(0, indexEnd));
                        temp2 = temp2.substring(indexEnd);
                    }
                    y = y - currentY;
                    y = ((double) ((int) (y * 1000))) / 1000;
                    if (indexEnd == -1) {
                        temp = temp3 + y;
                    } else {
                        temp = temp3 + y + temp2;
                    }
                }
            }
        }
        //Change Z
        if (in.contains("Z")) {
            int index = in.indexOf("Z");

            if (index + 1 < inLength) {
                if (Character.isDigit(in.charAt(index + 1))) {
                    String temp2 = in.substring(index + 1);
                    String temp3 = in.substring(0, index + 1);
                    int indexEnd = temp2.indexOf(" ");
                    if (indexEnd == -1) {
                        //nothing after the integer Value
                        currentZ = Double.parseDouble(temp2);
                    } else {
                        currentZ = Double
                                .parseDouble(temp2.substring(0, indexEnd));
                        temp2 = temp2.substring(indexEnd);
                    }
                    z = z - currentZ;
                    z = ((double) ((int) (z * 1000))) / 1000;
                    if (indexEnd == -1) {
                        temp = temp3 + z;
                    } else {
                        temp = temp3 + z + temp2;
                    }
                }
            }
        }

        return temp;
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments; unused here
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        String x, y, z, line, fileName = ".gcode";
        double xDouble = 96, yDouble = 107.1, zDouble = 23;
        boolean powercycle = true;
        int numAstrix = 0;

        Scanner usrIn = new Scanner(System.in);
        PrintWriter out = new PrintWriter("data/output.gcode");

        System.out.println("Enter Coordinates: ");

        //Get X,Y,Z
        System.out.println("Enter Gcode file name:");
        fileName = usrIn.nextLine() + fileName;

        File input = new File("data/" + fileName);
        Scanner in = new Scanner(input);

        System.out.println("Enter X: ");
        x = usrIn.nextLine();
        xDouble = Double.parseDouble(x);
        x = "X" + x;
        System.out.println(x);

        System.out.println("Enter Y: ");
        y = usrIn.nextLine();
        yDouble = Double.parseDouble(y);
        y = "Y" + y;
        System.out.println(y);

        System.out.println("Enter Z: ");
        z = usrIn.nextLine();
        zDouble = Double.parseDouble(z);
        z = "Z" + z;
        System.out.println(z);

        System.out.println("Did the printer have a power cycle? y/n");
        powercycle = usrIn.nextLine().contains("y");

        System.out.println("Running!");

        if (!powercycle) {
            while (in.hasNext()) {
                line = in.nextLine();
                if (line.contains(z)) {
                    while (in.hasNext()) {
                        if (line.contains(x) && line.contains(y)) {
                            line = in.nextLine();
                            out.println(line);
                            while (in.hasNext()) {
                                line = in.nextLine();
                                out.println(line);
                            }
                        } else {
                            line = in.nextLine();
                        }
                    }
                }
            }
        } else {
            while (in.hasNext()) {
                line = in.nextLine();
                if (line.contains(z)) {
                    while (in.hasNext()) {
                        if (line.contains(x) && line.contains(y)) {
                            line = in.nextLine();

                            out.println(
                                    parseXYZ(line, xDouble, yDouble, zDouble));
                            while (in.hasNext()) {
                                line = in.nextLine();
                                out.println(parseXYZ(line, xDouble, yDouble,
                                        zDouble));
                            }
                        } else {
                            line = in.nextLine();
                        }
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
