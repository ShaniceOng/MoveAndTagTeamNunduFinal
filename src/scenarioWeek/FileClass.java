package scenarioWeek;

import java.io.*;
import java.util.ArrayList;

public class FileClass {

    public static ArrayList<ArrayList<Point>> allInput = new ArrayList<ArrayList<Point>>();
    public static ArrayList<ArrayList<Point>> allRobots = new ArrayList<ArrayList<Point>>();
    public static ArrayList<ArrayList<Point[]>> allObstacles = new ArrayList<ArrayList<Point[]>>();

    public static void writeOutputFile(ArrayList<String> lines, String destPath){
        FileOutputStream outputStream = null;
        try {
            File output = new File(destPath);
            if (!output.exists()) output.createNewFile();
            outputStream = new FileOutputStream(output, true);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(outputStream));

            for(int i = 0; i < lines.size(); i++){
                out.write(lines.get(i));
                out.newLine();
            }

            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readInputFile(String filePath){
        String line = "";

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            while((line = reader.readLine()) != null){
                ArrayList<Point> robotsList = new ArrayList<Point>();
                ArrayList<Point[]> obstaclesList = new ArrayList<Point[]>();

                line = line.replaceAll("\\s",""); //remove all whitespaces
                String[] parts = line.split(":")[1].split("#");
                String[] robots = parts[0].split(",");
                for(int i = 0; i < robots.length - 1; i+=2){
                    robots[i] = robots[i].replaceAll("[()]", "");
                    robots[i+1] = robots[i+1].replaceAll("[()]", "");
                    double x = Double.parseDouble(robots[i]);
                    double y = Double.parseDouble(robots[i+1]);
                    robotsList.add(new Point(x, y, true));
                }

                if(parts.length > 1){
                    String[] obstacles = parts[1].split(";");

                    for(int i = 0; i < obstacles.length; i++){
                        String[] obstacleVertices = obstacles[i].split(",");
                        Point[] obstaclePoints = new Point[obstacleVertices.length/2];
                        int index = 0;
                        for(int j = 0; j < obstacleVertices.length - 1; j+=2){
                            obstacleVertices[j] = obstacleVertices[j].replaceAll("[()]", "");
                            obstacleVertices[j+1] = obstacleVertices[j+1].replaceAll("[()]", "");
                            double x = Double.parseDouble(obstacleVertices[j]);
                            double y = Double.parseDouble(obstacleVertices[j+1]);
                            obstaclePoints[index++] = new Point(x, y, false);
                        }
                        obstaclesList.add(obstaclePoints);
                    }
                }

                ArrayList<Point> all_points = new ArrayList<Point>();
                for (Point robot : robotsList) all_points.add(robot);

                for (Point[] obstacle : obstaclesList) {
                    for (Point point : obstacle) {
                        all_points.add(point);
                    }
                }
                allInput.add(all_points);
                allObstacles.add(obstaclesList);
                allRobots.add(robotsList);

            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
