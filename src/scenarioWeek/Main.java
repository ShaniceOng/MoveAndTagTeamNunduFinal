package scenarioWeek;

import java.util.ArrayList;

public class Main {
    private static int NUM_CLUSTERS = 5; // CHANGE FOR A DIFFERENT NUMBER OF CLUSTERS (Produces different timings)

    public static void main(String[] args){
        FileClass.readInputFile("robots.mat");

        FileClass fileClass = new FileClass();

        ArrayList<ArrayList<Point>> robotsList = fileClass.allRobots;

        for (ArrayList<Point> robots : robotsList) {
            robots.get(0).isAwake = true;
        }

        ArrayList<ArrayList<Point>> inputPoints = fileClass.allInput;
        ArrayList<ArrayList<Point[]>> obstaclesList = fileClass.allObstacles;

        ArrayList<String> lines = new ArrayList<String>();


        for(int i = 0; i < inputPoints.size(); i++){
            MAT mat = new MAT();

            ArrayList<Point> pointsList = inputPoints.get(i);
            Point[] points = pointsList.toArray(new Point[pointsList.size()]);

            mat.setAllVisible(points, obstaclesList.get(i));

            int robots = robotsList.get(i).size();

            mat.initialiseTable(points.length);
            mat.genFloydTable1(points);
            mat.genFloydTable2(points);
            mat.meanDistanceRobots(points, robots);

            KMeans kmeans = new KMeans();
            kmeans.doKMeans(NUM_CLUSTERS, robots, robotsList.get(i));
            ArrayList<Point> clusteredPoints = kmeans.getPoints();
//            for (Point p : robotsList.get(i)) System.out.println(p);
//            for (Point p : clusteredPoints) {
//                System.out.println(p + " belongs to cluster " + p.getCluster());
//            }
            Point[] robotClusteredPoints = new Point[clusteredPoints.size()];
            int index = 0;
            for (Point p : clusteredPoints) points[index++].setCluster(p.getCluster());
//          for (Point p : clusteredPoints) robotClusteredPoints[index++] = p;
            ArrayList<Cluster> clusters = kmeans.clusters;

            String sol = (i+1) + ": ";

            sol += mat.buildPathScheduleKMeans(points, robots, clusters.size(), clusters);

            lines.add(sol);

            if(i==29) break;
        }

        FileClass.writeOutputFile(lines, "output.txt");


        //write solution to file
        //FileClass.writeOutputFile(inputPoints, "out.txt");
        /*

        Point[] points = new Point[2];
        points[0] = new Point(-1.0, -1.0, true);
        points[1] = new Point(4.0, 4.0,true);

        Point[] points2 = new Point[5];
        points2[0] = new Point(0.0,1.0, true);
        points2[1] = new Point(2.0,0.0, true);
        points2[2] = new Point(3.0,5.0, true);
        points2[3] = new Point(6.0,2.0, true);
        points2[4] = new Point(9.0,0.0, true);

        Point[] obstacles1 = new Point[14];
        obstacles1[0] = new Point(1.0, 6.0);
        obstacles1[1] = new Point(1.0, 1.0);
        obstacles1[2] = new Point(5.0, 1.0);
        obstacles1[3] = new Point(5.0, 5.0);
        obstacles1[4] = new Point(3.0, 5.0);
        obstacles1[5] = new Point(3.0, 3.0);
        obstacles1[6] = new Point(4.0, 3.0);
        obstacles1[7] = new Point(4.0, 2.0);
        obstacles1[8] = new Point(2.0, 2.0);
        obstacles1[9] = new Point(2.0, 6.0);
        obstacles1[10] = new Point(6.0, 6.0);
        obstacles1[11] = new Point(6.0, 0.0);
        obstacles1[12] = new Point(0.0, 0.0);
        obstacles1[13] = new Point(0.0, 6.0);


        Point[] obstacles2 = new Point[4];
        obstacles2[0] = new Point(8.0, 1.0);
        obstacles2[1] = new Point(4.0, 1.0);
        obstacles2[2] = new Point(4.0, 4.0);
        obstacles2[3] = new Point(5.0, 2.0);

        Point[] obstacles3 = new Point[4];
        obstacles3[0] = new Point(1.0, 2.0);
        obstacles3[1] = new Point(1.0, 4.0);
        obstacles3[2] = new Point(3.0, 4.0);
        obstacles3[3] = new Point(3.0, 2.0);

        ArrayList<Point[]> obstacles = new ArrayList<Point[]>();
        ArrayList<Point[]> anotherObstacles = new ArrayList<Point[]>();
        obstacles.add(obstacles1);
        anotherObstacles.add(obstacles2);
        anotherObstacles.add(obstacles3);*/
/*
        MAT alg = new MAT();
//
        Point[] all_points = new Point[16];
        int index = 0;
        for (Point point : points) {
            all_points[index++] = point;
        }
        for (Point[] obstacle : obstacles) {
            for (Point p : obstacle) {
                all_points[index++] = p;
            }
        }


        alg.setAllVisible(all_points, obstacles);

        for (Point p : all_points) {
            System.out.println("*** Visible from point " + p.toString() + "***");
            ArrayList<Point> visible = p.visible;
            for (Point point : visible) {
                System.out.println(point.toString() + ", ");
            }
        }

        alg.initialiseTable(16);
        alg.genFloydTable1(all_points);
        alg.genFloydTable2(all_points);

//        alg.print_table(all_points);
        alg.visitAll(all_points, 2);*/
/*
        System.out.println("");
        System.out.println("***** QUESTION 3 *****");
        MAT alg2 = new MAT();
        Point[] all_points2 = new Point[13];
        int index2 = 0;
        for (Point point : points2) {
            all_points2[index2++] = point;
        }
        for (Point[] obstacle : anotherObstacles) {
            for (Point p : obstacle) {
                all_points2[index2++] = p;
            }
        }

        alg2.setAllVisible(all_points2, anotherObstacles);

        for (Point p : all_points2) {
            System.out.println("*** Visible from point " + p.toString() + "***");
            ArrayList<Point> visible = p.visible;
            for (Point point : visible) {
                System.out.print(point.toString() + ", ");
            }
            System.out.println("");
        }

        alg2.initialiseTable(13);
        alg2.genFloydTable1(all_points2);
        alg2.genFloydTable2(all_points2);

//        alg.print_table(all_points);
        alg2.visitAll(all_points2, 5);*/
    }



}
