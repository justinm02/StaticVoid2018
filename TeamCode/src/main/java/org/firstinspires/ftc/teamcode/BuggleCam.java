package org.firstinspires.ftc.teamcode;

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
    }

    public void update() {
        List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
        if (updatedRecognitions != null) {
            telemetry.addData("# Object Detected", updatedRecognitions.size());
            if (updatedRecognitions.size() == 3) {
                int goldMineralX = -1;
                int silverMineral1X = -1;
                int silverMineral2X = -1;
                for (Recognition recognition : updatedRecognitions) {
                    if (recognition.getLabel().equals("Gold Mineral")) {
                        goldMineralX = (int) recognition.getLeft();
                    } else if (silverMineral1X == -1) {
                        silverMineral1X = (int) recognition.getLeft();
                    } else {
                        silverMineral2X = (int) recognition.getLeft();
                    }
                }
                if (goldMineralX != -1 && silverMineral1X != -1 && silverMineral2X != -1) {
                    if (goldMineralX < silverMineral1X && goldMineralX < silverMineral2X) {
                        telemetry.addData("Gold Mineral Position", "Left");
                        goldPosition = GOLD_POSITION.LEFT;
                    } else if (goldMineralX > silverMineral1X && goldMineralX > silverMineral2X) {
                        telemetry.addData("Gold Mineral Position", "Right");
                        goldPosition = GOLD_POSITION.RIGHT;
                    } else {
                        telemetry.addData("Gold Mineral Position", "Center");
                        goldPosition = GOLD_POSITION.CENTER;
                    }
                }
            }
            telemetry.update();
        }
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
