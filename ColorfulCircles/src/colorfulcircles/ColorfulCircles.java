/*
 * Copyright (c) 2011, 2012 Oracle and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 *
 * This file is available and licensed under the following license:
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  - Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the distribution.
 *  - Neither the name of Oracle nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package colorfulcircles;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.BoxBlur;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;
import javafx.util.Duration;
import static java.lang.Math.random;

public class ColorfulCircles extends Application {

	// http://docs.oracle.com/javafx/2/get_started/animation.htm
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        Scene scene = new Scene(root, 800, 600, Color.BLACK);
        primaryStage.setScene(scene);
        
        // Creates a group named circles
        Group circles = new Group();
        for (int i = 0; i < 30; i++) {
            /*  
             * Each circle has a radius of 150, 
             * fill color of white, and 
             * opacity level of 5 percent, 
             * meaning it is mostly transparent.
        	 */
        	Circle circle = new Circle(150, Color.web("white", 0.05));
        	
            /* 
             * A stroke type of OUTSIDE means 
             * the boundary of the circle is extended 
             * outside the interior by the StrokeWidth value, 
             * which is 4. 
             */
            circle.setStrokeType(StrokeType.OUTSIDE);
            circle.setStroke(Color.web("white", 0.16));
            circle.setStrokeWidth(4);
            
            // Adds the circles group to the root node
            circles.getChildren().add(circle);
        }
        
        /*** 1. Simplest effect ***/
        /*
         * root.getChildren().add(circles);
         */
        
        /***  2. Animation effect ***/
        /*
         *  Creates a rectangle named colors. 
         *  The rectangle is the same width and height as the scene and 
         *  is filled with a linear gradient that starts 
         *  in the lower left-hand corner (0, 1) and ends in the upper right-hand corner (1, 0). 
         *  The value of true means the gradient is proportional to the rectangle, and 
         *  NO_CYCLE indicates that the color cycle will not repeat. 
         *  The Stop[] sequence indicates what the gradient color should be at a particular spot. 
         */
        Rectangle colors = new Rectangle(scene.getWidth(), scene.getHeight(),
                new LinearGradient(0f, 1f, 1f, 0f, true, CycleMethod.NO_CYCLE, new Stop[]{
                    new Stop(0, Color.web("#f8bd55")),
                    new Stop(0.14, Color.web("#c0fe56")),
                    new Stop(0.28, Color.web("#5dfbc1")),
                    new Stop(0.43, Color.web("#64c2f8")),
                    new Stop(0.57, Color.web("#be4af7")),
                    new Stop(0.71, Color.web("#ed5fc2")),
                    new Stop(0.85, Color.web("#ef504c")),
                    new Stop(1, Color.web("#f2660f")),}));
        
        /* 
         * Make the linear gradient adjust as the size of the scene changes 
         * by binding the width and height of the rectangle to the width and height of the scene. 
         * See Using JavaFX Properties and Bindings for more information on binding. 
         */
        colors.widthProperty().bind(scene.widthProperty());
        colors.heightProperty().bind(scene.heightProperty());

        // Adds the colors rectangle to the root node.(Choose one out of two to see difference. This is first choice.)
        //root.getChildren().add(colors);
        
        // Adds the colors rectangle to the root node.(Choose one out of two to see difference. This is second choice.)
        /* 
         * The group blendModeGroup sets up the structure for the overlay blend. 
         * The group contains two children. 
         * The first child is a new (unnamed) Group containing a new (unnamed) black rectangle and 
         * the previously created circles group. 
         * The second child is the previously created colors rectangle. 
         */
        Group blendModeGroup =
                new Group(new Group(new Rectangle(scene.getWidth(), scene.getHeight(),
                Color.BLACK), circles), colors);
        
        // The setBlendMode() method applies the overlay blend to the colors rectangle. 
        colors.setBlendMode(BlendMode.OVERLAY);
        // Add the blendModeGroup to the scene graph as a child of the root node
        root.getChildren().add(blendModeGroup);
        
        // Add this code before the primaryStage.show() line. 
        /* 
         * This code sets the blur radius to 10 pixels wide by 10 pixels high, 
         * and the blur iteration to 3, 
         * making it approximate a Gaussian blur. 
         */
        circles.setEffect(new BoxBlur(10, 10, 3));
        
        /* Animation is driven by a timeline, 
         * so this code creates a timeline, 
         * then uses a for loop to add two key frames to each of the 30 circles. 
         * The first key frame at 0 seconds 
         * uses the properties translateXProperty and translateYProperty to set a random position of the circles within the window. 
         * The second key frame at 40 seconds does the same. 
         * Thus, when the timeline is played, it animates all circles from one random position to another over a period of 40 seconds. 
         */
        Timeline timeline = new Timeline();
        for (Node circle : circles.getChildren()) {
            timeline.getKeyFrames().addAll(
                    new KeyFrame(Duration.ZERO, // set start position at 0
                    new KeyValue(circle.translateXProperty(), random() * 800),
                    new KeyValue(circle.translateYProperty(), random() * 600)),
                    new KeyFrame(new Duration(40000), // set end position at 40s
                    new KeyValue(circle.translateXProperty(), random()      * 800),
                    new KeyValue(circle.translateYProperty(), random() * 600)));
        }
        // play 40s of animation
        timeline.play();

        primaryStage.show();
    }
}
