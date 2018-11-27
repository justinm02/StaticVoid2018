package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

//@Autonomous(name = "MarkerOp", group = "Autonomous")
public class MarkerAutoOp extends AutoOp {

    @Override
    public void runOpMode() throws InterruptedException {
        initialize();
        waitForStart();

        //Descend robot from Lander, move off the hook
        lowerBot();

        //Locates the gold mineral from one of the three given locations
        findGold();

        //Move bot based on where the Gold Mineral is, knocks it, and moves to above the rightmost mineral
        switch(cam.getGoldPosition()) {
            case LEFT:
                driveTrain.longitudinalDistance(12);
                driveTrain.longitudinalDistance(-12);
                break;
            case CENTER:
                driveTrain.longitudinalDistance(12);
                driveTrain.longitudinalDistance(-12);
                break;
            case RIGHT:
                driveTrain.longitudinalDistance(12);
                driveTrain.longitudinalDistance(12);
                break;
        }

        //Moves robot into opposing team's crater
        driveTrain.rotateDegrees(45);
    }
}
