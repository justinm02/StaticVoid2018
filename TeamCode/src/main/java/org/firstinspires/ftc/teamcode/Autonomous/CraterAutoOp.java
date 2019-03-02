package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.Autonomous.AutoOp;

@Autonomous(name = "CraterOp", group = "Autonomous")
public class CraterAutoOp extends AutoOp {

    @Override
    public void runOpMode() {
        initialize();

        waitForStart();

        //Descend robot from Lander, move off the hook
        lowerBot();

        longitudinalDistance(8);

        rotateDegrees(-80); //turn towards minerals
        //Locates the gold mineral from one of the three given locations
        prospect();

        //rotateDegrees(90);
        //Move bot based on where the Gold Mineral is, knocks it, and continues into friendly team's crater
        switch(cam.getGoldPosition()) {
            case LEFT:
                rotateDegrees(45);
                longitudinalDistance(24);
                longitudinalDistance(-12);
                rotatePreciseDegrees(-45);
                longitudinalDistance(40);
                break;
            case RIGHT:
                rotateDegrees(125);
                longitudinalDistance(18);
                longitudinalDistance(-12);
                rotatePreciseDegrees(-120);
                longitudinalDistance(36);
                break;
            case CENTER:
            case NULL:
                rotateDegrees(80);
                longitudinalDistance(12);
                resetPosition();
                rotatePreciseDegrees(-80);
                longitudinalDistance(36);
                break;
        }
        rotateDegrees(125);
        strafe(26, .3, "left");
        strafe(4, .3, "right");
        longitudinalDistance(-30, 0.65f);
        dispenseMarker();
        //rotateDegrees(10);
        longitudinalDistance(45, 0.65f);
        strafe(8, .3, "left");
        longitudinalDistance(15, .65f);
        park();
    }
}
