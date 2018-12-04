package org.firstinspires.ftc.teamcode;

//@Autonomous(name = "CraterOp", group = "Autonomous")
public class CraterAutoOp extends AutoOp {

    @Override
    public void runOpMode() throws InterruptedException {
        initialize();
        waitForStart();

        //Descend robot from Lander, move off the hook
        lowerBot();

        //Locates the gold mineral from one of the three given locations
        prospect();

        //Move bot based on where the Gold Mineral is, knocks it, and continues into friendly team's crater
        switch(cam.getGoldPosition()) {
            case LEFT:
                driveTrain.longitudinalDistance(-12);
                driveTrain.rotateDegrees(-45);
                driveTrain.longitudinalDistance(-24);
                break;
            case RIGHT:
                driveTrain.longitudinalDistance(-12);
                driveTrain.rotateDegrees(45);
                driveTrain.longitudinalDistance(-24);
                break;
            case CENTER:
            case NULL:
                driveTrain.longitudinalDistance(-36);
                break;
        }
        driveTrain.longitudinalDistance(18);
    }
}
