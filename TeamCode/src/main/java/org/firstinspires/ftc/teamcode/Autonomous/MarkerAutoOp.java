package org.firstinspires.ftc.teamcode.Autonomous;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "MarkerOp", group = "Autonomous")
public class MarkerAutoOp extends AutoOp {

    @Override
    public void runOpMode() {
        initialize();
        waitForStart();

        //Descend robot from Lander, move off the hook
        lowerBot();

        //Locates the gold mineral from one of the three given locations
        prospect();

        //Move bot based on where the Gold Mineral is, knocks it, and moves to above the rightmost mineral
        intake.lockIntake();
        switch(cam.getGoldPosition()) {
            case LEFT:
                driveTrain.longitudinalDistance(-12);
                driveTrain.rotatePreciseDegrees(-45);
                driveTrain.longitudinalDistance(-24);
                driveTrain.rotatePreciseDegrees(90);
                driveTrain.longitudinalDistance(24);
                break;
            case RIGHT:
                driveTrain.longitudinalDistance(-12, 0.5f);
                driveTrain.rotatePreciseDegrees(45, 0.5f);
                driveTrain.longitudinalDistance(-24, 0.5f);
                driveTrain.longitudinalDistance(24, 0.5f);
                driveTrain.rotatePreciseDegrees(45);
                driveTrain.longitudinalDistance(48, 0.5f);
                driveTrain.rotateDegrees(-45);
                driveTrain.longitudinalDistance(12, 0.5f);
                break;
            case CENTER:
            case NULL:
                driveTrain.longitudinalDistance(-36);
                driveTrain.longitudinalDistance(18);
                driveTrain.rotatePreciseDegrees(90);
                driveTrain.longitudinalDistance(36);
                driveTrain.rotatePreciseDegrees(-45);
                driveTrain.longitudinalDistance(24);
                break;
        }

        //Moves robot into opposing team's crater
        //driveTrain.rotateDegrees(45);
    }
}
