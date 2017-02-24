package scenarioWeek;

/* Some of the code used in this file were adapted from external sources such as Stack Overflow and GitHub. The main websites that we used are
* http://alienryderflex.com/polygon/ for checking if a point is in a polygon
* https://github.com/lorgan3/GG2server/blob/master/objects/Hitboxes/Line.cs for checking if two lines intersect
* http://stackoverflow.com/questions/217578/how-can-i-determine-whether-a-2d-point-is-within-a-polygon for ideas on the cases where paths and obstacles would intersect
* */
import java.awt.geom.Point2D;
import java.util.*;


public class MAT {
    public double[][] floyd_table;
    public int[][] warshall_table;
    public double epsilon = 0.000000001;

    int numPoints;

    private double pathLength(Point p1, Point p2){
        double dist = Math.sqrt((p2.x-p1.x)*(p2.x-p1.x) + (p2.y-p1.y)*(p2.y-p1.y));
        return dist;
    }

    // uses the y = mx + c equation to find x for input y
    public double findX (Point p1, Point p2, double new_y) {
        double x1 = p1.x;
        double y1 = p1.y;
        double x2 = p2.x;
        double y2 = p2.y;
        double current_m = (y2-y1)/(x2-x1);
        double current_c = y1 - (x1*current_m);
        return (new_y - current_c) / current_m;
    }

    // uses the y = mx + c equation to find y for input x
    public double findY (Point p1, Point p2, double new_x) {
        double x1 = p1.x;
        double y1 = p1.y;
        double x2 = p2.x;
        double y2 = p2.y;
        double current_m = (y2-y1)/(x2-x1);
        double current_c = y1 - (x1*current_m);
        return (current_m * new_x) + current_c;
    }

    public void setAllVisible (Point[] points, ArrayList<Point[]> obstacles) {
        int len = points.length;
        ArrayList<Point> visiblePoints = new ArrayList<>();
        boolean collides = false;
        for (int i=0; i<len; i++) {
            for (int j=0; j<len; j++) {
                if (j!=i) {
                    for (Point[] obstacle : obstacles) {
                        if (pathIntersects(points[i], points[j], obstacle)){
                            collides = true;
                            break;
                        }
                    }
                    if (!collides) visiblePoints.add(points[j]);
                    collides = false;
                }
            }
            points[i].setVisible(visiblePoints);
            visiblePoints = new ArrayList<>();
        }
    }

    public void initialiseTable(int numPoints) {
        this.numPoints = numPoints;
        floyd_table = new double[numPoints][numPoints];
        warshall_table = new int[numPoints][numPoints];
        for (int i=0; i<numPoints; i++) {
            for (int j=0; j<numPoints; j++) {
                warshall_table[i][j] = -1; // default: no route
            }
        }
    }

    // populate Floyd's and Warshall's tables with initial data: the points that can be reached directly from each other
    public void genFloydTable1(Point[] points) {
        int len = points.length;
        for (int i=0; i<len; i++) {
            for (int j=0; j<len; j++) {
                if (i==j) {
                    floyd_table[i][j] = 0;
                    warshall_table[i][j] = -3; // same point
                }
                else if (points[i].isVisible(points[j])) {
                    floyd_table[i][j] = pathLength(points[i], points[j]);
                    warshall_table[i][j] = -2; //straight path
                }
                else {
//                    System.out.println(points[i] + " and " + points[j] + " not visible");
                    floyd_table[i][j] = Double.MAX_VALUE / 2 - 1;
                }
            }
        }
    }

    // use existing information to populate Floyd and Warshall's tables
    public void genFloydTable2(Point[] points) {
        int len = points.length;
        for (int k=0; k<len; k++) {
            for (int i=0; i<len; i++) {
                for (int j=0; j<len; j++) {
                    if (floyd_table[i][k] + floyd_table[k][j] < floyd_table[i][j]) {
                        floyd_table[i][j] = floyd_table[i][k] + floyd_table[k][j];
                        warshall_table[i][j] = k;
                    }
                }
            }
        }
    }

    // print table method for debugging purposes
    public void print_table(Point[] points) {
        for (int i=0; i<numPoints; i++) {
            for (int j=0; j<numPoints; j++) {
                System.out.println("Shortest distance from " + points[i].toString() + " to " + points[j].toString() +
                        " is " + floyd_table[i][j]);
            }
        }
    }

    // prints Warshall's table
    public void printWarshalls (Point[] points) {
        for (int i=0; i<numPoints; i++) {
            for (int j=0; j<numPoints; j++) {
                if (warshall_table[i][j] >= 0) System.out.println(points[i].toString() + " to " + points[j].toString() + " = " + points[warshall_table[i][j]].toString());
                else if (warshall_table[i][j] == -1) System.out.println(points[i].toString() + " to " + points[j].toString() + " cannot join!");
                else if (warshall_table[i][j] == -2) System.out.println("Straight line from " + points[i].toString() + " to " + points[j].toString());
            }
        }
    }
/*
    public String path (Point[] points, int x, int y) {
        int k = warshall_table[x][y];
        if (k==-1) return points[y].toString() + ", ";
        return path(points, x, k) + path(points, k, y);
    }

    public String path (ArrayList<Point> points, int x, int y) {
        int k = warshall_table[x][y];
        if (k==-1) return points.get(y).toString() + ", ";
        return path(points, x, k) + path(points, k, y);
    }
*/

    // generates path recursively using Warshall's algorithm and previously generated table

    // Returns nearest point and distance for BASIC ALGORITHM
    public double[] findNearestAndDist (int current, int numRobots, boolean[] awaken) {
        double[] map = new double[2];
        double min_dist = Double.MAX_VALUE;
        int min_index = current;
        for (int i=0; i<numRobots; i++) {
            if (!awaken[i]) {
                if (min_dist>floyd_table[current][i]) {
                    min_dist = floyd_table[current][i];
                    min_index = i;
                }
            }
        }
        globalPoints[min_index].isAwake = true;
        map[0] = min_index;
        map[1] = min_dist;
        return map;
    }

    boolean allFound = false;
    Point[] globalPoints = new Point[numPoints];
    ArrayList<ArrayList<Point>> allPaths = new ArrayList<>();


    double meanDistance;

    // Calculates the mean distance between the robots
    public void meanDistanceRobots (Point points[], int numRobots) {
        double sum = 0;
        for (int i=0; i<numRobots-1; i++) {
            for (int j=i+1; j<numRobots; j++) {
                sum += floyd_table[i][j];
            }
        }
        meanDistance = sum / numRobots;
    }

    // find Nearest Point function for Queue MAT
    public double[] findNearestPoint (int current) {
        double[] res = new double[2];
        double min_dist = Double.MAX_VALUE;
        int min_index = current;
        for (int i=0; i<totalRobots; i++) {
            if (!globalPoints[i].isAwake) {
                if (min_dist>floyd_table[current][i]) {
                    min_dist = floyd_table[current][i];
                    min_index = i;
                }
            }
        }
//        globalPoints[min_index].isAwake = true;
        res[0] = min_index;
        res[1] = min_dist;
        return res;
    }

    public double[] findNearestPointWithChosen (int current, int chosen) {
        boolean isNearby = true;
        double[] res = new double[2];
        double min_dist = Double.MAX_VALUE;
        int min_index = current;
        for (int i=0; i<totalRobots; i++) {
            if (!globalPoints[i].isAwake) {
                double dist = floyd_table[current][i];
                double dist2 = floyd_table[chosen][i];
                if (isNearby && dist2>meanDistance) {
                    isNearby = !isNearby;
                    min_dist = dist;
                    min_index = i;
                }
                else if (!isNearby && dist2>meanDistance && min_dist>dist) {
                    min_dist = dist;
                    min_index = i;
                }
                else if (isNearby && min_dist>dist) {
                    min_dist = dist;
                    min_index = i;
                    isNearby = dist2 < meanDistance;
                }
            }
        }
//        globalPoints[min_index].isAwake = true;
        res[0] = min_index;
        res[1] = min_dist;
        return res;
    }

    /* Initial Greedy Algorithm ============================================================================== */
    public int totalRobots;
    int robotsFound = 1;

    public void setGlobalPoints(Point[] points) {
        globalPoints = points;
    }

    public void setTotalRobots(int numRobots) {
        totalRobots = numRobots;
    }
    // Builds an implicitly tree using a queue to search for an optimal path
//    public String greedyAlg (Point[] points, int numRobots) {
//        setGlobalPoints(points);
//        setTotalRobots(numRobots);
//        PriorityQueue<PathItem> pathItems = new PriorityQueue<PathItem>((a, b)->(int)(a.totalDistance-b.totalDistance));
//        double[] nearestArray = findNearestPoint(0);
//        int nearest = (int) nearestArray[0];
//        double dist = nearestArray[1];
//        PathItem first_elem = new PathItem(0, nearest, dist, 0);
//        pathItems.add(first_elem);
//        ArrayList<PathItem> savedPathItems = new ArrayList<>();
//        int numInQueue = 1;
//
//        while(!pathItems.isEmpty()) {
//            if (robotsFound==totalRobots) {
//                break;
//            }
//            PathItem current = pathItems.poll();
//            if (globalPoints[current.destination].isAwake) {
//                double[] new_dest = findNearestPoint(current.robot);
//                if (new_dest[0]!=current.destination) {
//                    current.destination = (int) new_dest[0];
//                    pathItems.add(new PathItem(current.robot, (int)new_dest[0], current.totalBefore + new_dest[1], current.totalBefore));
//                }
//                continue;
//            }
//            savedPathItems.add(current);
//            globalPoints[current.destination].isAwake = true;
//            robotsFound++;
//            if (robotsFound==totalRobots) {
//                break;
//            }
//            double[] nearest1 = findNearestPoint(current.destination);
//            int nearest1_index = (int) nearest1[0];
//            if (nearest1_index!=current.robot) {
//                double nearest1_dist = nearest1[1];
//                pathItems.add(new PathItem(current.robot, nearest1_index, current.totalDistance + nearest1_dist, current.totalDistance));
//            }
//
//            double[] nearest2 = findNearestPoint(current.destination);
//            int nearest2_index = (int) nearest2[0];
//            if (nearest2_index!=current.destination) {
//                double nearest2_dist = nearest2[1];
//                pathItems.add(new PathItem(current.destination, nearest2_index, current.totalDistance + nearest2_dist, current.totalDistance));
//            }
//        }
//
//        ArrayList<ArrayList<Integer>> chosenPaths = new ArrayList<ArrayList<Integer>>(totalRobots);
//        for (int i=0; i<totalRobots; i++) {
//            ArrayList<Integer> chosenPath = new ArrayList<>();
//            chosenPath.add(i);
//            int elemSize = savedPathItems.size();
//            for (int j=0; j<elemSize; j++) {
//                PathItem elem = savedPathItems.get(j);
//                if (elem.robot == i) {
//                    chosenPath.add(elem.destination);
//                }
//            }
//            if (chosenPath.size()>1) chosenPaths.add(chosenPath);
//        }
//
//        String result = "";
//
//        for (ArrayList<Integer> path : chosenPaths) {
//            if (path==null || path.size()==0) continue;
//            String current_path = getSchedule(path);
//            result += current_path + "; ";
//        }
//
//        return result.substring(0, result.length()-2);
//    }

    /* KMeans MAT ==================================================================== */

    int[] numVisited;
    int[] capacity;
    boolean[] toBeVisited;

    public boolean allVisited() {
        boolean visited = true;
        for (boolean b : toBeVisited) visited = visited && b;
        return visited;
    }

    public void initialiseClusterInformation (int numClusters, ArrayList<Cluster> clusters) {
        this.numVisited = new int[numClusters];
        this.capacity = new int[numClusters];
        this.toBeVisited = new boolean[numClusters];
        for (int i=0; i<numClusters; i++) {
            numVisited[i] = 0;
            capacity[i] = clusters.get(i).numPoints;
            toBeVisited[i] = false;
        }
    }

    public double[] findNearestPointInCluster (int current, int clusterID) {
        double[] res = new double[2];
        double min_dist = Double.MAX_VALUE;
        int min_index = current;
        for (int i=0; i<totalRobots; i++) {
            if (!globalPoints[i].isAwake && globalPoints[i].getCluster() == clusterID) {
                if (min_dist>floyd_table[current][i]) {
                    min_dist = floyd_table[current][i];
                    min_index = i;
                }
            }
        }
//        globalPoints[min_index].isAwake = true;
        res[0] = min_index;
        res[1] = min_dist;
        return res;
    }

    public double[] findNearestPointInCluster (int current, int clusterID, int alreadyChosen) {
        double[] res = new double[2];
        double min_dist = Double.MAX_VALUE;
        int min_index = current;
        for (int i=0; i<totalRobots; i++) {
            if (!globalPoints[i].isAwake && globalPoints[i].getCluster() == clusterID && i!=alreadyChosen) {
                if (min_dist>floyd_table[current][i]) {
                    min_dist = floyd_table[current][i];
                    min_index = i;
                }
            }
        }
//        globalPoints[min_index].isAwake = true;
        res[0] = min_index;
        res[1] = min_dist;
        return res;
    }

    public double[] findNearestPointNotInCluster (int current, int clusterID) {
        double[] res = new double[2];
        double min_dist = Double.MAX_VALUE;
        int min_index = current;
        for (int i=0; i<totalRobots; i++) {
            int thisCluster = globalPoints[i].getCluster();
            if (!globalPoints[i].isAwake &&  thisCluster != clusterID && (!toBeVisited[thisCluster] || allVisited())) {
                if (min_dist>floyd_table[current][i]) {
                    min_dist = floyd_table[current][i];
                    min_index = i;
                }
            }
        }
//        globalPoints[min_index].isAwake = true;
        res[0] = min_index;
        res[1] = min_dist;
        return res;
    }

    public double[] findNearestPointNotInCluster (int current, int clusterID, int alreadyChosen) {
        double[] res = new double[2];
        double min_dist = Double.MAX_VALUE;
        int min_index = current;
        for (int i=0; i<totalRobots; i++) {
            int thisCluster = globalPoints[i].getCluster();
            if (!globalPoints[i].isAwake &&  thisCluster != clusterID && (!toBeVisited[thisCluster] || allVisited()) && i!=alreadyChosen) {
                if (min_dist>floyd_table[current][i]) {
                    min_dist = floyd_table[current][i];
                    min_index = i;
                }
            }
        }
//        globalPoints[min_index].isAwake = true;
        res[0] = min_index;
        res[1] = min_dist;
        return res;
    }

    public String buildPathScheduleKMeans(Point[] points, int numRobots, int numClusters, ArrayList<Cluster> clusters) {
        globalPoints = points;
        totalRobots = numRobots;
        initialiseClusterInformation(numClusters, clusters);
        PriorityQueue<PathItem> pathItems = new PriorityQueue<PathItem>((a, b)->(int)(a.totalDistance-b.totalDistance));
        int currentCluster = globalPoints[0].getCluster();
        toBeVisited[currentCluster] = true;
        numVisited[currentCluster]++;
        double[] nearestArray;
        if (numVisited[currentCluster]==capacity[currentCluster]) nearestArray = findNearestPointNotInCluster(0, currentCluster);
        else nearestArray = findNearestPointInCluster(0, currentCluster);
        int nearest = (int) nearestArray[0];
        double dist = nearestArray[1];
        PathItem first_elem = new PathItem(0, nearest, dist, 0);
        toBeVisited[globalPoints[nearest].getCluster()] = true;
        pathItems.add(first_elem);
        ArrayList<PathItem> savedPathItems = new ArrayList<>();
        int numInQueue = 1;

        while(!pathItems.isEmpty()) {
            if (robotsFound==totalRobots) {
                break;
            }
            PathItem current = pathItems.poll();
//            currentQueue--;
            if (globalPoints[current.destination].isAwake) {
                double[] new_dest;
                int cluster = globalPoints[current.robot].getCluster();
                if (numVisited[cluster]==capacity[cluster]) new_dest = findNearestPoint(current.robot);
                else new_dest = findNearestPointInCluster(current.robot, cluster);
                if (new_dest[0]!=current.destination) {
//                    System.out.println("Find Nearest Point In / Not In on " + globalPoints[current.destination] + " produced " + globalPoints[(int)new_dest[0]] + " from cluster " + globalPoints[(int)new_dest[0]].getCluster());
                    current.destination = (int) new_dest[0];
                    pathItems.add(new PathItem(current.robot, (int)new_dest[0], current.totalBefore + new_dest[1], current.totalBefore));
                }
//                else
//                {
//                    new_dest = findNearestPointNotInCluster(current.robot, globalPoints[current.robot].getCluster());
//                    if (new_dest[0]!=current.destination) {
//                        pathItems.add(new PathItem(current.robot, (int)new_dest[0], current.totalBefore + new_dest[1], current.totalBefore));
//                        toBeVisited[globalPoints[(int) new_dest[0]].getCluster()] = true;
//                    }
//                }
                continue;
            }
            savedPathItems.add(current);
            int currCluster = globalPoints[current.destination].getCluster();
            globalPoints[current.destination].isAwake = true;
            numVisited[currCluster]++;
            robotsFound++;
//            globalPoints[current.destination].isAwake = true;
            if (robotsFound==totalRobots) {
                break;
            }
            double[] nearest1 = findNearestPointInCluster(current.destination, currCluster);
            int nearest1_index = (int) nearest1[0];
            if (nearest1_index!=current.destination) {
//                System.out.println("Find Nearest Point In Cluster on " + globalPoints[current.destination] + " produced " + globalPoints[nearest1_index] + " from cluster " + globalPoints[nearest1_index].getCluster());
                double nearest1_dist = nearest1[1];
                pathItems.add(new PathItem(current.robot, nearest1_index, current.totalDistance + nearest1_dist, current.totalDistance));
//                currentQueue++;
            }
            else
            {
                nearest1 = findNearestPointNotInCluster(current.destination, currCluster);
                nearest1_index = (int) nearest1[0];
                if (nearest1_index!=current.destination) {
//                    System.out.println("Find Nearest Point Not In Cluster on " + globalPoints[current.destination] + " produced " + globalPoints[nearest1_index] + " from cluster " + globalPoints[nearest1_index].getCluster());
                    toBeVisited[globalPoints[nearest1_index].getCluster()] = true;
                    double nearest1_dist = nearest1[1];
                    pathItems.add(new PathItem(current.robot, nearest1_index, current.totalDistance + nearest1_dist, current.totalDistance));
                }
            }
//            robotsFound++;
//            if (robotsFound==totalRobots) {
//                continue;
//            }
            double[] nearest2;
            if (!allVisited()) {
                nearest2 = findNearestPointNotInCluster(current.destination, currCluster, (int) nearest1[0]);
                toBeVisited[globalPoints[(int) nearest2[0]].getCluster()] = true;
            }
            else {
                nearest2 = findNearestPointInCluster(current.destination, currCluster, (int) nearest1[0]);
            }

            int nearest2_index = (int) nearest2[0];
            if (nearest2_index!=current.destination) {
//                System.out.println("Find Nearest Point In / Not In on " + globalPoints[current.destination] + " produced " + globalPoints[nearest1_index] + " from cluster " + globalPoints[nearest1_index].getCluster());
                double nearest2_dist = nearest2[1];
                pathItems.add(new PathItem(current.destination, nearest2_index, current.totalDistance + nearest2_dist, current.totalDistance));
//                currentQueue++;
            }
//            robotsFound++;
        }

        ArrayList<ArrayList<Integer>> chosenPaths = new ArrayList<ArrayList<Integer>>(totalRobots);
        for (int i=0; i<totalRobots; i++) {
            ArrayList<Integer> chosenPath = new ArrayList<>();
            chosenPath.add(i);
            int elemSize = savedPathItems.size();
            for (int j=0; j<elemSize; j++) {
                PathItem elem = savedPathItems.get(j);
                if (elem.robot == i) {
                    chosenPath.add(elem.destination);
                }
            }
            if (chosenPath.size()>1) chosenPaths.add(chosenPath);
        }

        String result = "";

        for (ArrayList<Integer> path : chosenPaths) {
            if (path==null || path.size()==0) continue;
            String current_path = getSchedule(path);
            result += current_path + "; ";
        }

        return result.substring(0, result.length()-2);
    }

    // Prints path from Robot position x to Robot position y (including obstacle coordinates)
    public String individualSchedule(int x1, int x2) {
        int z = warshall_table[x1][x2];
        if(z==-1) return globalPoints[x1].toString() + " cannot be reached from " + globalPoints[x2].toString();
//        else System.out.println("K IS " + k);
        else if (z==-2) return globalPoints[x2].toString() + ", ";
        else if (z==-3) return "same point!";
        return individualSchedule(x1, z) + individualSchedule(z, x2);
    }

    // Calls individualSchedule for each robot
    public String getSchedule(ArrayList<Integer> pointsIndex) {
        int len = pointsIndex.size();
        String output = globalPoints[pointsIndex.get(0)].toString() + ", ";
        for (int i=1; i<len; i++) {
            output = output + individualSchedule(pointsIndex.get(i-1), pointsIndex.get(i));
        }
        return output.substring(0, output.length()-2);
    }

    // Checks if a point is within the boundaries of an obstacle
    public boolean isInPolygon(Point p, Point[] polygon) {
        int len = polygon.length;
        double x = p.x;
        double y = p.y;
        boolean oddNodes = false;
        for (int i=0; i<len-1; i++) {
            double xi = polygon[i].x;
            double yi = polygon[i].y;
            double xj = polygon[i+1].x;
            double yj = polygon[i+1].y;
            if ((yi<y && yj>y) || (yj<y && yi>y)) {
                if (xi+(y-yi)/(yj-yi)*(xj-xi)<x) {
                    oddNodes = !oddNodes;
                }
            }
        }
        double xi = polygon[0].x;
        double yi = polygon[0].y;
        double xj = polygon[len-1].x;
        double yj = polygon[len-1].y;
        if ((yi<y && yj>y) || (yj<y && yi>y)) {
            if (xi+(y-yi)/(yj-yi)*(xj-xi)<x) {
                oddNodes = !oddNodes;
            }
        }
        return oddNodes;
    }

    public boolean pathIntersects(Point p1, Point p2, Point[] obstacle){
        // Test the ray against all sides
        int intersect;
        for (int i = 0; i < obstacle.length-1; i++) {
            // Test if current side intersects with ray.
            intersect = myIntersect(p1,p2, obstacle[i], obstacle[i+1]);
            if (intersect==1) {
//                System.out.println(p1 + " and " + p2 + " collide at " + obstacle[i] + ", " + obstacle[i+1]);
                return true;
            }
            if (intersect==3) {
                double new_x = p1.x < p2.x ? p1.x + epsilon : p1.x - epsilon;
                double new_y = findY(p1, p2, new_x);
                if (isInPolygon(new Point(new_x, new_y), obstacle)) {
//                   System.out.println(p1 + " and " + p2 + " collide at " + obstacle[i] + ", " + obstacle[i+1]);
                    return true;
                }
            }
            if (intersect==4) {
                double new_x = p2.x < p1.x ? p2.x + epsilon : p2.x - epsilon;
                double new_y = findY(p1, p2, new_x);
                if (isInPolygon(new Point(new_x, new_y), obstacle)) {
//                        System.out.println(p1 + " and " + p2 + " collide at " + obstacle[i] + ", " + obstacle[i+1]);
                    return true;
                }
            }
        }
        return false;
    }

    /* FINAL - Intersecting Function ================================================================ */
    public static boolean isPointOnTheLine(Point2D.Double A, Point2D.Double B, Point2D.Double P) {
        double m = (B.y - A.y) / (B.x - A.x);

        //handle special case where the line is vertical
        if (Double.isInfinite(m)) {
            if(A.x == P.x) return true;
            else return false;
        }

        if ((P.y - A.y) == m * (P.x - A.x)) return true;
        else return false;
    }

    public boolean IsPointRightOfLine (Point start, Point end, double x1, double y1) {
        Point tmpStart = new Point (0,0);
        Point tmpEnd = new Point(end.x - start.x, end.y - start.y);
        x1 -= start.x;
        y1 -= start.y;
        Point x1y1 = new Point (x1, y1);
        return crossProduct(tmpEnd, x1y1) < 0;
    }

    public boolean IsPointOnLine (Point p1, Point p2, double x1, double y1) {
        Point tmp1 = new Point (0, 0);
        Point tmp2 = new Point(p2.x - p1.x, p2.y - p1.y);
        x1 -= p1.x;
        y1 -= p1.y;
        Point x1y1 = new Point (x1, y1);
        double r = crossProduct(tmp2, x1y1);
        return Math.abs(r) < epsilon;
    }

    public double crossProduct(Point p1, Point p2) {
        return p1.x*p2.y - p1.y*p2.x;
    }

    public boolean LineTouchesOrCrossesLine(Point start1, Point end1, Point start2, Point end2) {
        return IsPointOnLine(start1, end1, start2.x, start2.y)
                || IsPointOnLine(start1, end1, end2.x, end2.y)
                || (IsPointRightOfLine(start1, end1, start2.x, start2.y) ^ IsPointRightOfLine(start1, end1, end2.x, end2.y));
    }

    public boolean DoLinesIntersect(Point start1, Point end1, Point start2, Point end2) {
        return LineTouchesOrCrossesLine(start1, end1, start2, end2) && LineTouchesOrCrossesLine(start2, end2, start1, end1);
    }

    public int myIntersect (Point start1, Point end1, Point start2, Point end2) {
        int NO = 0, YES = 1, COLLINEAR = 2, P1_SAME = 3, P2_SAME = 4;
        if ((start1.isEqual(start2) && end1.equals(end2)) || (start1.equals(end2) && end1.equals(start2))) return NO;
        if (start1.isEqual(start2) || start1.isEqual(end2)) {
            return P1_SAME;
        }

        if (end1.isEqual(start2) || end1.isEqual(end2)) {
            return P2_SAME;
        }

        if (DoLinesIntersect(start1, end1, start2, end2)) {
            return YES;
        }
//        double m1 = (end1.y - start1.y) / (end1.x - start1.x);
//        double m2 = (end2.y - start2.y) / (end2.x - start2.x);
//        double c1 = start1.y - (m1 * start1.x);
//        double c2 = start2.y - (m2 * start2.x);
//
//        if (m1==m2) return NO;
//
//        double intersectionX = (c2-c1) / (m1-m2);
//        double intersectionY = (m1 * intersectionX) + c1;
//
//        if (intersectionX > Math.min(start1.x, end1.x) && intersectionX < Math.max(start1.x, end1.x)) {
//            if (intersectionY > Math.min(start1.y,end1.y) && intersectionY < Math.max(start1.y, end1.y)) {
//                if (intersectionX > Math.min(start2.x, end2.x) && intersectionX < Math.max(start2.x, end2.x)){
//                    if (intersectionY > Math.min(start2.y, end2.y) && intersectionY < Math.max(start2.y, end2.y)) {
////                        System.out.println("Collision");
//                        return YES;
//                    }
//                }
//            }
//        }
        return NO;
    }

     /* ======================================================================================================= */

    /* VERSION 1 - Intersecting Function ================================================================ */
    public int isIntersecting(Point p1, Point p2, Point p3, Point p4) {
        double v1x1 = p1.x;
        double v1y1 = p1.y;
        double v1x2 = p2.x;
        double v1y2 = p2.y;
        double v2x1 = p3.x;
        double v2y1 = p3.y;
        double v2x2 = p4.x;
        double v2y2 = p4.y;

        int NO = 0, YES = 1, COLLINEAR = 2, P1_SAME = 3, P2_SAME = 4;

        // If one of the robots are on the corner of a polygon
        if (p1.isEqual(p3) || p1.isEqual(p4)) {
            return P1_SAME;
        }

        if (p2.isEqual(p3) || p2.isEqual(p4)) {
            return P2_SAME;
        }
        double d1, d2;
        double a1, a2, b1, b2, c1, c2;

        // Convert vector 1 to a line (line 1) of infinite length.
        // We want the line in linear equation standard form: A*x + B*y + C = 0
        // See: http://en.wikipedia.org/wiki/Linear_equation
        a1 = v1y2 - v1y1;
        b1 = v1x1 - v1x2;
        c1 = (v1x2 * v1y1) - (v1x1 * v1y2);

        // Every point (x,y), that solves the equation above, is on the line,
        // every point that does not solve it, is not. The equation will have a
        // positive result if it is on one side of the line and a negative one
        // if is on the other side of it. We insert (x1,y1) and (x2,y2) of vector
        // 2 into the equation above.
        d1 = (a1 * v2x1) + (b1 * v2y1) + c1;
        d2 = (a1 * v2x2) + (b1 * v2y2) + c1;

        // If d1 and d2 both have the same sign, they are both on the same side
        // of our line 1 and in that case no intersection is possible. Careful,
        // 0 is a special case, that's why we don't test ">=" and "<=",
        // but "<" and ">".
        if (d1 != 0 && d2 != 0 && ((d1>0 && d2>0)||(d1<0 && d2<0))) {
            return NO; // DONT_INTERSECT
        }

        // The fact that vector 2 intersected the infinite line 1 above doesn't
        // mean it also intersects the vector 1. Vector 1 is only a subset of that
        // infinite line 1, so it may have intersected that line before the vector
        // started or after it ended. To know for sure, we have to repeat the
        // the same test the other way round. We start by calculating the
        // infinite line 2 in linear equation standard form.
        a2 = v2y2 - v2y1;
        b2 = v2x1 - v2x2;
        c2 = (v2x2 * v2y1) - (v2x1 * v2y2);

        // Calculate d1 and d2 again, this time using points of vector 1.
        d1 = (a2 * v1x1) + (b2 * v1y1) + c2;
        d2 = (a2 * v1x2) + (b2 * v1y2) + c2;

        // Again, if both have the same sign (and neither one is 0),
        // no intersection is possible.
        if (d1 > 0 && d2 > 0) return NO;
        if (d1 < 0 && d2 < 0) return NO;

        // If we get here, only two possibilities are left. Either the two
        // vectors intersect in exactly one point or they are collinear, which
        // means they intersect in any number of points from zero to infinite.
        if ((a1 * b2) - (a2 * b1) <= epsilon) return COLLINEAR;

        // If they are not collinear, they must intersect in exactly one point.
        return YES;
    }

     /* ======================================================================================================= */

    /* VERSION 2 - Intersecting Function ================================================================ */
    public boolean ccw(Point a, Point b, Point c){
        return (c.y-a.y)*(b.x-a.x) > (b.y-a.y)*(c.x-a.x);
    }

    public static boolean shareAnyPoint(Point2D.Double A, Point2D.Double B, Point2D.Double C, Point2D.Double D) {
        if (isPointOnTheLine(A, B, C)) return true;
        else if (isPointOnTheLine(A, B, D)) return true;
        else if (isPointOnTheLine(C, D, A)) return true;
        else if (isPointOnTheLine(C, D, B)) return true;
        else return false;
    }

     /* ======================================================================================================= */

}
