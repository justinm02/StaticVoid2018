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

        longitudinalDistance(6);
        rotateDegrees(-90);
        //Locates the gold mineral from one of the three given locations
        prospect();

        rotateDegrees(90);
        //Move bot based on where the Gold Mineral is, knocks it, and moves to above the rightmost mineral
        intake.lockIntake();
        switch(cam.getGoldPosition()) {
            case LEFT:
                longitudinalDistance(6);
                rotatePreciseDegrees(-45);
                longitudinalDistance(32);
                rotatePreciseDegrees(-90);
                longitudinalDistance(-24);
                dispenseMarker();
                strafe(10, .3, "right");
                //rotatePreciseDegrees(3);
                longitudinalDistance(64, 0.6f);
                park();
                break;
            case RIGHT:
                longitudinalDistance(6);
                rotatePreciseDegrees(45);
                longitudinalDistance(24);
                longitudinalDistance(-18);
                rotatePreciseDegrees(45);
                longitudinalDistance(-60);
                rotatePreciseDegrees(135);
                longitudinalDistance(-54);
                dispenseMarker();
                rotatePreciseDegrees(3);
                strafe(10,.3, "right");
                longitudinalDistance(54, 0.6f);
                park();
                break;
            case CENTER:
            case NULL:
                //rotateDegrees(-90);
                longitudinalDistance(40);
                dispenseMarker();
                resetPosition();
                rotatePreciseDegrees(-80);
                longitudinalDistance(35);
                rotateDegrees(-40);
                strafe(10, .3, "right");
                longitudinalDistance(28);
                park();
                break;
        }
    }
}
