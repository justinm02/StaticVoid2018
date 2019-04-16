package org.firstinspires.ftc.teamcode.Autonomous;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous(name = "MarkerOp", group = "Autonomous")
public class MarkerAutoOp extends AutoOp {

    @Override
    public void runOpMode() {
        initialize();

        waitForStart();
        lowerBot();

        longitudinalDistance(8);
        rotatePreciseDegrees(-80);
        //Locates the gold mineral from one of the three given locations
        prospect();

        //rotateDegrees(90);
        //Move bot based on where the Gold Mineral is, knocks it, and moves to above the rightmost mineral
        //intake.lockIntake();
        switch(cam.getGoldPosition()) {
            case LEFT:
                //longitudinalDistance(12);
                rotateDegrees(35);
                longitudinalDistance(27);
                longitudinalDistance(-3);
                strafe(8, .2, "left");
                rotatePreciseDegrees(-90);
                longitudinalDistance(-24);
                rotatePreciseDegrees(5);
                strafe(20, .2, "right");
                dispenseMarker();
                strafe(4, .3, "left");
                longitudinalDistance(30, 0.9f);
                strafe(10, .3, "right");
                longitudinalDistance(36, 0.9f);
                park();
                break;
            case RIGHT:
                rotateDegrees(120);
                longitudinalDistance(24);
                longitudinalDistance(-12);
                rotatePreciseDegrees(50);
                longitudinalDistance(-42);
                rotatePreciseDegrees(140);
                strafe(16, .3, "right");
                longitudinalDistance(-30, 0.7f);
                dispenseMarker();
                rotatePreciseDegrees(3);
                strafe(18,.3, "right");
                strafe(2, .3, "left");
                longitudinalDistance(36, 0.9f);
                strafe(8,.3, "right");
                longitudinalDistance(20, 0.9f);
                park();
                break;
            case CENTER:
            case NULL:
                rotateDegrees(-90);
                longitudinalDistance(-40);
                dispenseMarker();
                rotatePreciseDegrees(45);
                strafe(20, .3, "right");
                longitudinalDistance(45, 0.9f);
                strafe(10, .3, "right");
                longitudinalDistance(25, 0.9f);
                park();
                break;

        }
    }
}
