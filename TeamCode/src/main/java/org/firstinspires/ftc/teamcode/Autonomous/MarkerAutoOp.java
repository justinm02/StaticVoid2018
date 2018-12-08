package org.firstinspires.ftc.teamcode.Autonomous;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "MarkerOp", group = "Autonomous")
public class MarkerAutoOp extends AutoOp {

    @Override
    public void runOpMode() {
        initialize();
        while(!isStarted()) {
            intake.lift(0);
            telemetry.addData("Started", isStarted());
            telemetry.addData("Is Active", opModeIsActive());
        }
        waitForStart();

        //Descend robot from Lander, move off the hook
        lowerBot();

        //Locates the gold mineral from one of the three given locations
        prospect();

        //Move bot based on where the Gold Mineral is, knocks it, and moves to above the rightmost mineral
        intake.lockIntake();
        switch(cam.getGoldPosition()) {
            case LEFT:
                longitudinalDistance(-12);
                rotatePreciseDegrees(-45);
                longitudinalDistance(-24);
                rotatePreciseDegrees(90);
                longitudinalDistance(-24);
                dispenseMarker();
                longitudinalDistance(48);
                break;
            case RIGHT:
                longitudinalDistance(-12);
                rotatePreciseDegrees(45);
                longitudinalDistance(-24);
                rotatePreciseDegrees(-90);
                longitudinalDistance(-18);
                dispenseMarker();
                longitudinalDistance(18);
                rotatePreciseDegrees(135);
                longitudinalDistance(48);
                rotateDegrees(-45);
                longitudinalDistance(12);
                break;
            case CENTER:
            case NULL:
                longitudinalDistance(-36);
                dispenseMarker();
                longitudinalDistance(18);
                rotatePreciseDegrees(90);
                longitudinalDistance(36);
                rotatePreciseDegrees(-45);
                longitudinalDistance(24);
                break;
        }

        //Moves robot into opposing team's crater
        //driveTrain.rotateDegrees(45);
    }
}
