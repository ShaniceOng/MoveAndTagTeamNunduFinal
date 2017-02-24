package Visualisation;

import processing.core.PApplet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;


class Main extends PApplet {

    public static void main(String args[]) {
        Main pt = new Main();
        PApplet.runSketch(new String[]{"ProcessingTest"}, pt);
    }

    //enter full directory paths to robots.txt as String inputFile and output.txt as String routesFile
    String inputFile="C:\\Users\\username\\...\\robots.txt";
    String routesFile="C:\\Users\\username\\...\\output.txt";

    List<String[]> ObListSplit = new ArrayList<String[]>();
    List<float[]> ObListInts = new ArrayList<float[]>();
    List<Point> plots = new ArrayList<Point>();
    List<Robot> robots = new ArrayList<Robot>();
    float[] lineList;
    Random random = new Random();
    Controller controller = new Controller();
    ArrayList<Polygon> polygons=new ArrayList<Polygon>();
    int maxPolygons;
    int currentPolygons=0;
    int maxRobots;
    int currentRobots=0;
    int maxRoutes;
    int currentRoutes=0;
    int currentLines=0;
    int maxLines;
    List<Line> lines=new ArrayList<Line>();
    List<Line> screenLines=new ArrayList<Line>();
    List<String[]> LinesSplit = new ArrayList<>();
    List<float[]> LinesSplitFloats = new ArrayList<>();
    List<Route> routes= new ArrayList<Route>();
    List<Route> screenRoutes= new ArrayList<Route>();
    int lasttime=millis();
    int x=800;
    int y=450;
    int s=20;
    boolean drawing=false;
    boolean backwards=false;


    class Point{
        float x;
        float y;

        public Point(float x, float y){
            this.x = x;
            this.y = y;
        }

        public void setX (float x){
            this.x = x;
        }

        public void setY (float y){
            this.y = y;
        }

        public float getX(){
            return x;
        }

        public float getY(){
            return y;
        }
    }

    class Robot{
        float x;
        float y;
        int r,g,b;
        float w;
        float l;
        boolean visited;

        public Robot(float x, float y) {
            this.x = x;
            this.y = y;
            this.w = 0.3f;
            this.l = 0.3f;
            this.r=100;
            this.g=100;
            this.b=100;
            this.visited=false;
        }
        public void setRGB(int r, int g, int b){
            this.r=r;
            this.g=g;
            this.b=b;
        }
        public void draw(){
            fill(r,g,b);
            ellipse(x, y, w, l);
        }
    }

    class Polygon{
        float[] arr;
        float r,g,b;

        public Polygon(float[] arr,int r, int g, int b){
            this.arr=arr;
            this.r=r;
            this.g=g;
            this.b=b;
        }
        public void draw(){
            fill(r,g,b);
            beginShape();
            for (int i=0;i<arr.length/2;i++){

                vertex(arr[i*2],arr[(i*2)+1]);
            }
            endShape(CLOSE);
        }
    }

    class Route{
        float[] arr;
        int r,g,b;
        public Route(float[] arr,int r,int g, int b){
            this.arr=arr;
            this.r=r;
            this.g=g;
            this.b=b;
        }
        public void draw(){
            stroke(r,g,b);
           for (int i=0;i<arr.length-3;i+=2){
               line(arr[(i)], arr[(i) + 1], arr[(i) + 2], arr[(i) + 3]);

            }
            stroke(0,0,0);
        }
    }

    class Line {
        float[] arr;
        int r,g,b;

        public Line(float[] arr, int r, int g, int b){
            this.arr=arr;
            this.r=r;
            this.g=g;
            this.b=b;
        }

        public void draw(){
            stroke(r,g,b);
                line(arr[0], arr[1], arr[2], arr[3]);
            stroke(0,0,0);

        }
    }

    class InputKey {
        public boolean keyAvailable() {
            if ((key=='a'||key=='w'||key=='s'||key=='d'||key=='p'||key=='o'||key=='b'||key=='v')&&keyPressed) {
                return true;
            } else {
                return false;
            }
        }
    }

    class Controller {
        private InputKey inputKey;
        public Controller()
        {
            inputKey=new InputKey();
        }
        public void refresh()
        {
            if (inputKey.keyAvailable())
            {
                switch(key)
                {
                    case 'a':
                        x+=10;
                        break;
                    case 's':
                        y-=10;
                        break;
                    case 'd':
                        x-=10;
                        break;
                    case 'w':
                        y+=10;
                        break;
                    case 'o':
                        s-=1;
                        break;
                    case 'p':
                        s+=1;
                        break;
                    case 'b':
                        drawing=true;
                        break;
                    case 'v':
                        backwards=true;
                        break;

                }
            }
        }
    }

    public void settings(){
        size(1600, 900);
        smooth(2);

    }
    public void setup(){
        background(50,50,50);

        List<String> input = new ArrayList<>();

        try {
            BufferedReader inFile = new BufferedReader(new FileReader(inputFile));
            int situations = 30;
            for (int i = 0; i < situations; i++) {
                input.add(inFile.readLine());
            }
            inFile.close();

        }
        catch (Exception e) {
        }

        List<String> outputs = new ArrayList<>();

        try {
            BufferedReader inFile = new BufferedReader(new FileReader(routesFile));
            int situations = 32;
            for (int i = 0; i < situations; i++) {
                outputs.add(inFile.readLine());
            }
            inFile.close();

        }
        catch (Exception e) {
        }

        Scanner reader = new Scanner(System.in);
        System.out.println("Enter a number: ");
        int n = reader.nextInt();
        int m = n+2;

        String sampleString = input.get(n-1);
        String testString = outputs.get(m-1);

        String[] splitRobotsAndObstacles;
        String[] stripRobots;
        String RobotPositions;
        String RobotPositionsStripped;
        String[] data;
        String ObPositions;
        String ObPositionsStripped;
        String[] Obdata;

        //Split at the # get points for robots and points for obstacles
        if (arrayCheck(input.get(n-1))==1){
            splitRobotsAndObstacles = sampleString.split("#");
            stripRobots = splitRobotsAndObstacles[0].split(":");

            //Remove all the brackets and whitesapce
            RobotPositions = stripRobots[1].replaceAll("[\\[\\](){}]","");
            RobotPositionsStripped = RobotPositions.replaceAll("\\s+","");
            data = RobotPositionsStripped.split(",");

            ObPositions = splitRobotsAndObstacles[1].replaceAll("[\\[\\](){}]","");
            ObPositionsStripped = ObPositions.replaceAll("\\s+","");
            Obdata = ObPositionsStripped.split(";");
        }
        else{
            splitRobotsAndObstacles = new String[2];
            splitRobotsAndObstacles[0]= sampleString;
            stripRobots = splitRobotsAndObstacles[0].split(":");

            //Remove all the brackets and whitesapce
            RobotPositions = stripRobots[1].replaceAll("[\\[\\](){}]","");
            RobotPositionsStripped = RobotPositions.replaceAll("\\s+","");
            data = RobotPositionsStripped.split(",");

            ObPositions = "";
            Obdata = new String[0];
        }

        System.out.println(Arrays.toString(stripRobots));

        //Parse the output file
        String outputline = testString.replaceAll("[\\[\\](){}]","");
        String outputstripped = outputline.replaceAll("\\s+","");
        String[] removeColon = outputstripped.split(":");
        String removespaces = removeColon[1].replaceAll("\\s+","");
        String[] output = removespaces.split(";");

        for (int i = 0;i<output.length;i++){
            LinesSplit.add(output[i].split(","));

            int outputLen = LinesSplit.get(i).length;
            float floats[] = new float[outputLen];
            String[] temp = LinesSplit.get(i);

            for(int j = 0;j < outputLen;j++){
                floats[j]=Float.parseFloat(temp[j]);
            }
            LinesSplitFloats.add(floats);
        }

        for (int i =0;i<Obdata.length;i++){
            ObListSplit.add((Obdata[i].split(",")));

            int obLen = ObListSplit.get(i).length;
            float integers[] = new float[obLen];
            String[] temp = ObListSplit.get(i);

            for(int j = 0;j < obLen; j++) {
                integers[j] = Float.parseFloat(temp[j]);
            }

            ObListInts.add(integers);
        }

        // cast strings to ints for Robots

        int robLen = data.length;
        float integers[] = new float[robLen];

        for(int i = 0; i < robLen ; i++){
            integers[i] = Float.parseFloat(data[i]);
            //System.out.println(integers[i]);
        }

        // add the points to an arraylist and make the robot object and draw it

        int counter = robLen/2;
        for(int i = 0; i < counter;i+=1){
            plots.add(new Point(integers[i*2],integers[(i*2)+1])); //i*2 skips to the next coordinate
            robots.add(new Robot(integers[i*2],integers[(i*2)+1]));
        }

        setupShapes();
        setupLines();
        setupRobots();
    }

    public int arrayCheck(String a){;
        if (a.contains("#")){
            return 1;
        }
        return 0;
    }

    public int randomColour() {
        int number;
        number=(int)((random.nextFloat()*255)+75);
        return number;
    }




    public void setupShapes(){
        maxPolygons=ObListInts.size();
        while (currentPolygons< maxPolygons){
            for (float[] i: ObListInts){
                polygons.add(new Polygon(i,randomColour(),randomColour(),randomColour()));
                currentPolygons++;
            }
        }
    }
    public void manageShapes(){

        for (Polygon i: polygons ) {
            i.draw();
        }
        fill(255,255,255);
    }

    boolean first=true;

    public void setupLines(){
        maxRoutes = LinesSplitFloats.size();
        while (currentRoutes < maxRoutes) {
            for (float[] i : LinesSplitFloats) {
                int r=randomColour();
                int g=randomColour();
                int b=randomColour();
                if (first){
                     r = 255;
                    g = 255;
                    b= 255;
                    routes.add(new Route(i,r,g,b));
                    first=false;
                }
                routes.add(new Route(i,r,g,b));


                for (int j=0;j<i.length-3;j+=2){
                    float[] temp={i[j], i[j+1], i[j+2], i[j+3]};
                    lines.add(new Line(temp,r,g,b));
                    currentLines++;
                }
                currentRoutes++;
            }
        }
    }


    ArrayList<ArrayList<Line>> bigArrayList=new ArrayList<ArrayList<Line>>();
    ArrayList<ArrayList<Line>> screenBigArrayList=new ArrayList<ArrayList<Line>>();
    ArrayList<Line> lineArrayList=new ArrayList<Line>();
    ArrayList<Line> screenLineArrayList=new ArrayList<Line>();
    ArrayList<float []> path=new ArrayList<float []>();

//        public void setupLines(){
//            path.add(LinesSplitFloats.get(0));
//            for (int l=0;l<path.size();l++) {
//                int r=randomColour();
//                int g=randomColour();
//                int b=randomColour();
//                for (int j=0;j<path.get(l).length-3;j+=2) {
//                    float[] temp = {path.get(l)[j],path.get(l)[j+1],path.get(l)[j+2],path.get(l)[j+3]};
//                    if (first) {
//                        lineArrayList.add(new Line(temp,r,g,b));
//                        bigArrayList.add(lineArrayList);
//                        first=false;
//                    }else{
//                        ArrayList<Line> lineArrayList2=new ArrayList<Line>();
//                        lineArrayList2.add(new Line(temp,r,g,b));
//                        for (float[] k: LinesSplitFloats){
//                            if((path.get(l)[j]==k[0])&&(path.get(l)[j+1]==k[1]&&(j!=0))){
//                                path.add(k);
//                                float[] temp2={k[0],k[1],k[2],k[3]};
//                                lineArrayList2.add(new Line(temp2,r,g,b));
//                            }
//                        }
//                        bigArrayList.add(lineArrayList2);
//                    }
//                }
//            }
//        }

//


    public void manageLines(){
        if (i<lines.size()){
            screenLines.add(lines.get(i));
            i++;
        }
        for (Line i : screenLines) {

            i.draw();
        }
    }


    int i=0;
    int j=0;

//    public void manageLines(){
//        if (i<bigArrayList.size()) {
//            screenBigArrayList.add(bigArrayList.get(i));
//            i++;
//        }
//
//        for (Line i : screenBigArrayList.get(i)) {
//            i.draw();
//        }
//    }

//int k =0;
//
//    public void manageLines(){
//        if (i<bigArrayList.size()&&j>screenBigArrayList.get(i).size()) {
//            screenBigArrayList.add(bigArrayList.get(i));
//            i++;
//        }
//
////        for (int j=0; j<screenBigArrayList.get(i-1).size();j++) {
////                Line m = screenBigArrayList.get(i-1).get(j);
////                screenLineArrayList.add(m);
////        }
//
//        if(j<screenBigArrayList.get(i-1).size())
//        {
//            Line m= screenBigArrayList.get(i-1).get(j);
//            screenLineArrayList.add(m);
//        }
//
//        for (Line i : screenLineArrayList) {
//            i.draw();
//        }
//    }



    public void backwards(){
        if(screenLines.size()>0) {
            screenLines.remove(screenLines.size() - 1);
            i--;
        }
        for(Line i: screenLines){
            i.draw();
        }


    }

    public void manageRobots(){
        for (Robot robots:robots){
            robots.draw();
        }
    }

    public void setupRobots(){
        for (Robot i: robots){
            for (Route j: routes){
                if ((i.x==j.arr[0])&&(i.y==j.arr[1])){
                    i.setRGB(j.r,j.g,j.b);
                }
            }
        }
    }

    public void draw() {
        controller.refresh();
        translate(x, y);
        scale(s);
            if (millis() - lasttime > 30) {
                if(drawing) {
                    controller.refresh();
                    background(50, 50, 50);
                    strokeWeight(0.1f);
                    manageShapes();
                    strokeWeight(0.05f);
                    manageLines();
                    strokeWeight(0.05f);
                    manageRobots();
                    lasttime = millis();
                }
                if (backwards){
                    controller.refresh();
                    background(50, 50, 50);
                    strokeWeight(0.1f);
                    manageShapes();
                    strokeWeight(0.05f);
                    backwards();
                    strokeWeight(0.05f);
                    manageRobots();
                    lasttime = millis();
                }

                drawing=false;
                backwards=false;
            }
    }

}



