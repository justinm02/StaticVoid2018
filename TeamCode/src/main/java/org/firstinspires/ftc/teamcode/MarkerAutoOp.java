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
        resetEncoders();

        driveTrain.lateralDistance(.5);
        resetEncoders();

        driveTrain.longitudinalDistance(-.5);
        resetEncoders();

        driveTrain.lateralDistance(-.5);

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
                resetEncoders();
                break;
            case LEFT:
                driveTrain.lateralDistance(12);
                resetEncoders();
                driveTrain.longitudinalDistance(-12);
                resetEncoders();
                break;
            case RIGHT:
                driveTrain.lateralDistance(-12);
                resetEncoders();
                driveTrain.longitudinalDistance(-12);
                resetEncoders();
                break;
        }

        //Crater Method()

        while(opModeIsActive()) {}
    }
}
