package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "MarkerOp", group = "Autonomous")
public class MarkerAutoOp extends AutoOp {


    @Override
    public void runOpMode() throws InterruptedException {
        initialize();
        waitForStart();

        //Descend Bot from Lander
        lowerBot();

        driveTrain.lateralDistance(4);

        driveTrain.longitudinalDistance(-4);

        driveTrain.lateralDistance(-4);

        //Marker Method()

        //While the Gold Position is undetermined, keep updating the camera
        cam.activateTFOD();
        while(cam.getGoldPosition() == BuggleCam.GOLD_POSITION.NULL)
            cam.update();
        cam.stopTFOD();

        //Move bot based on where the Gold Mineral is
        switch(cam.getGoldPosition()) {
            case CENTER:
                driveTrain.longitudinalDistance(-12);
                driveTrain.longitudinalDistance(12);
                driveTrain.lateralDistance(12);
                break;
            case LEFT:
                driveTrain.lateralDistance(-12);
                driveTrain.longitudinalDistance(-12);
                driveTrain.longitudinalDistance(12);
                driveTrain.lateralDistance(24);
                break;
            case RIGHT:
                driveTrain.lateralDistance(-12);
                driveTrain.longitudinalDistance(-12);
                driveTrain.longitudinalDistance(12);
                break;
        }

        driveTrain.lateralDistance(32);
        driveTrain.rotateDegrees(45);
        driveTrain.lateralDistance(14);
        //Crater Method()
    }
}
