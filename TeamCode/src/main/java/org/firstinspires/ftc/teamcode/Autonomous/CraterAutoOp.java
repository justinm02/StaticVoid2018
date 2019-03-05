package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.Autonomous.AutoOp;

@Autonomous(name = "CraterOp", group = "Autonomous")
public class CraterAutoOp extends AutoOp {

    @Override
    public void runOpMode() {
        //Hi Justin
        initialize();

        waitForStart();

        //Descend robot from Lander, move off the hook
        lowerBot();

        longitudinalDistance(8);

        rotatePreciseDegrees(-80); //turn towards minerals
        //Locates the gold mineral from one of the three given locations
        prospect();

        //rotateDegrees(90);
        //Move bot based on where the Gold Mineral is, knocks it, and continues into friendly team's crater
        switch(cam.getGoldPosition()) {
            case LEFT:
                rotatePreciseDegrees(45);
                longitudinalDistance(24);
                longitudinalDistance(-10);
                rotatePreciseDegrees(-45);
                longitudinalDistance(28);
                break;
            case RIGHT:
                rotateDegrees(125);
                longitudinalDistance(20);
                longitudinalDistance(-6);
                rotatePreciseDegrees(-120);
                longitudinalDistance(40);
                break;
            case CENTER:
            case NULL:
                rotateDegrees(80);
                longitudinalDistance(16);
                resetPosition();
                rotatePreciseDegrees(-80);
                longitudinalDistance(32);
                break;
        }
        rotatePreciseDegrees(120);
        strafe(32, .3, "left");
        strafe(2, .3, "right");
        longitudinalDistance(-36, 0.65f);
        dispenseMarker();
        //rotateDegrees(10);
        longitudinalDistance(32, 0.65f);
        strafe(8, .3, "left");
        longitudinalDistance(24, .65f);
        park();
    }
}
