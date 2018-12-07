package org.firstinspires.ftc.teamcode.Robot;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

public class BuggleCam {

    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;
    private Telemetry telemetry;

    private int foundMinerals;

    private GOLD_POSITION goldPosition;

    public enum GOLD_POSITION {
        LEFT,
        CENTER,
        RIGHT,
        NULL
    }

    public BuggleCam(Telemetry telemetry,  int tfodMonitorViewId) {
        this.telemetry = telemetry;
        init(tfodMonitorViewId);
        goldPosition = GOLD_POSITION.NULL;
        foundMinerals = 0;
    }



    public void betterUpdate(Telemetry telemetry) {
        telemetry.addData("Status", "Prospecting");
        List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
        if(updatedRecognitions != null) {
            if(updatedRecognitions.size() >= 1) {
                int goldMineralX = -1;
                int silverMineral1X = -1;
                int silverMineral2X = -1;
                int recognitionNum = 0;
                for(Recognition recognition : updatedRecognitions) {
                    if(recognition.getLabel().equals("Gold Mineral"))
                        goldMineralX = (int) (recognition.getLeft() + recognition.getRight()) / 2;
                    else if (silverMineral1X == -1)
                        silverMineral1X = (int) (recognition.getRight() + recognition.getLeft()) / 2;
                    else
                        silverMineral2X = (int) (recognition.getRight() + recognition.getLeft()) / 2;
                    telemetry.addData("Recognition" + ++recognitionNum, recognition.getLabel());
                }
                if(goldMineralX != -1) {
                    //Test closest value 0, 600, and 1200
                    //Max X value is 1200
                    if(Math.abs(goldMineralX) < Math.abs(goldMineralX - 600) && Math.abs(goldMineralX) < Math.abs(goldMineralX - 1200)){
                        goldPosition = GOLD_POSITION.LEFT;
                    } else if(Math.abs(goldMineralX) > Math.abs(goldMineralX - 600) && Math.abs(goldMineralX - 1200) > Math.abs(goldMineralX - 600)) {
                        goldPosition = GOLD_POSITION.CENTER;
                    } else {
                        goldPosition = GOLD_POSITION.RIGHT;
                    }
                    telemetry.addData("Gold Position", goldPosition);
                } else if(silverMineral1X != -1 && silverMineral2X != -1) {
                    if(silverMineral2X < silverMineral1X) {
                        int swap = silverMineral1X;
                        silverMineral1X = silverMineral2X;
                        silverMineral2X = swap;
                    }

                    //If Silver1 is on the left
                    if(Math.abs(silverMineral1X) < Math.abs(silverMineral1X - 600) && Math.abs(silverMineral1X) < Math.abs(silverMineral1X - 1200)){
                        //If Silver2 is in the center
                        if(Math.abs(silverMineral2X) > Math.abs(silverMineral2X - 600) && Math.abs(silverMineral2X - 1200) > Math.abs(silverMineral2X - 600)) {
                            goldPosition = GOLD_POSITION.RIGHT;
                        } else {
                            goldPosition = GOLD_POSITION.CENTER;
                        }
                    //Else if Silver 1 is in the middle, Silver 2 has to be on the right
                    } else if(Math.abs(silverMineral1X) > Math.abs(silverMineral1X - 600) && Math.abs(silverMineral1X - 1200) > Math.abs(silverMineral1X - 600)) {
                        goldPosition = GOLD_POSITION.LEFT;
                    }
                }
            }
        }
        telemetry.update();
    }

    public GOLD_POSITION getGoldPosition() {
        return goldPosition;
    }

    public void activateTFOD() {
        tfod.activate();
    }

    public void stopTFOD() {
        tfod.shutdown();
    }

    public void init(int tfodMonitorViewId) {
        //Construct the correct parameters to use with Vuforia
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.vuforiaLicenseKey = "AXSqbfD/////AAABmS9cPhiInU+XkDP0H71ke4F7jpikB4F6PY5sAJN5oDUCUFtHlqIWEZlbkvEgbPzYTScdm+NsHX6OEr+9mtw/nnimWj4twQ/imuKSIM468vXZERR/sYs+4TkLhNSCjI5fU8+FRV5GoZT8Z49qsxe5gF4h/KM0/H+rDPDxmmG73xVZd7pIZbUFKMEKM8mjznS4ZYjlmWqgBkkq6lfc3/GI88qMWs4hHDLQ2lzId6D4hyV1RPmpEMbaYUQ/HyGmoDoSSfdTH0phLMPf1nLQvzhkeDuQIg3FV+PMka21LoyMpLA9ZN6lUvL5rvJRsKHQ5sPWdwm8wPP4ZNeHE1MxiAvV+lGIaWgRwx+yjNCw8P8zmg0D";
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

        //Instantiate Vuforia Object with license key and back camera
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        //Construct correct parameters for TensorFlow
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);

        //Instantiate TensorFlow Object
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset("RoverRuckus.tflite", "Gold Mineral", "Silver Mineral");
    }
}
