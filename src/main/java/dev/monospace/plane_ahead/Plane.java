package dev.monospace.plane_ahead;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

import java.util.Random;

public class Plane extends Pane {
    private final SVGPath body;
    private final SVGPath wings;
    private final SVGPath windows;

    public Plane() {
        body = new SVGPath();
        body.setContent("M130 158c46.944 0 85 38.056 85 85v68.252L950.221 310c212.395 5.766 283.71 101.159 283.78 207.152l-.02 3.215c0 54.457-31.637 56.843-89.554 56.843L308.687 579C130.56 575.421 0 517.716 0 444.325v-34.282c0-33.53 17.8-63.151 44.999-80.965L45 158h85Zm641.42-18c27.93 0 50.58 22.645 50.58 50.58v27.84c0 27.935-22.65 50.58-50.58 50.58H613.58c-27.93 0-50.58-22.645-50.58-50.58v-27.84c0-27.935 22.65-50.58 50.58-50.58h157.84Zm-57.88 506c31.18 0 56.46 25.28 56.46 56.46v31.08c0 31.18-25.28 56.46-56.46 56.46H535.46c-31.18 0-56.46-25.28-56.46-56.46v-31.08c0-31.18 25.28-56.46 56.46-56.46h178.08Z");
        wings = new SVGPath();
        wings.setContent("M439.184 13.425C463.11.575 531.418-6.144 548.281 7.571c24.483 11.712 145.334 173.18 245.625 302.697l-197.089.336c-11.238-17.038-22.968-34.438-34.753-51.863l-6.243-9.228C486.097 146.483 417.13 44.638 439.184 13.425ZM113 445h68v131c0 37.555-30.44 68-68 68H45V513c0-37.555 30.44-68 68-68Zm225.79 469.79c-37.71-49.32 156.91-265.225 239.99-403.245 27.18-22.034 39.66-32.18 119.38-33.545 89.06 1.984 106.16 4.407 122.84 33.545C701.94 648.025 498.49 905.66 463.81 920.98c-19.33 14.52-97.6 7.41-125.02-6.19Z");
        windows = new SVGPath();
        windows.setContent("M826 411.5c0-19.054 15.45-34.5 34.5-34.5s34.5 15.446 34.5 34.5v21c0 19.054-15.45 34.5-34.5 34.5S826 451.554 826 432.5v-21Zm-129 0c0-19.054 15.45-34.5 34.5-34.5s34.5 15.446 34.5 34.5v21c0 19.054-15.45 34.5-34.5 34.5S697 451.554 697 432.5v-21Zm-128 0c0-19.054 15.45-34.5 34.5-34.5s34.5 15.446 34.5 34.5v21c0 19.054-15.45 34.5-34.5 34.5S569 451.554 569 432.5v-21Zm-128-1c0-19.054 15.45-34.5 34.5-34.5s34.5 15.446 34.5 34.5v20c0 19.054-15.45 34.5-34.5 34.5S441 449.554 441 430.5v-20Zm-129 2c0-19.054 15.45-34.5 34.5-34.5s34.5 15.446 34.5 34.5v20c0 19.054-15.45 34.5-34.5 34.5S312 451.554 312 432.5v-20Zm729.009-34.5H1176.6l26.718 30.423c7.496 10.95 13.513 22.528 18.186 34.577l6.496 24h-186.991C1016.151 467 996 447.076 996 422.5c0-24.577 20.151-44.5 45.009-44.5Z");
        setBodyFill(Color.rgb(226, 239, 246));
        setWingFill(Color.rgb(220, 233, 240));
        setWindowFill(Color.rgb(49, 86, 140));
        this.getChildren().addAll(body, wings, windows);
    }

    public Color getBodyFill() {
        return (Color) body.getFill();
    }

    public void setBodyFill(Color color) {
        body.setFill(color);
    }

    public Color getWingFill() {
        return (Color) wings.getFill();
    }

    public void setWingFill(Color color) {
        wings.setFill(color);
    }

    public Color getWindowFill() {
        return (Color) windows.getFill();
    }

    public void setWindowFill(Color color) {
        windows.setFill(color);
    }

    public void setTheme(Color color) {
        if (color.equals(Color.rgb(255, 230, 128))) {
            this.setBodyFill(Color.rgb(244, 235, 170));
            this.setWingFill(Color.rgb(240, 219, 128));
            this.setWindowFill(Color.rgb(240, 219, 128));
        } else {
            Random random = new Random();
            this.setBodyFill(color);

            double h = color.getHue();
            double s = color.getSaturation();
            double b = color.getBrightness();
            this.setWingFill(Color.hsb(h, s * random.nextDouble(0.5, 0.98), b * random.nextDouble(0.5, 0.98)));

            if (b < 0.8) {
                this.setWindowFill(Color.rgb(226, 239, 246));
            } else {
                this.setWindowFill(Color.rgb(49, 86, 140));
            }
        }
    }
}