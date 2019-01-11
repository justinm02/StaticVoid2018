package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Safe Marker Op", group = "Autonomous")
public class SafeMarkerOp extends AutoOp {
    @Override
    public void runOpMode() {
        initialize();
        waitForStart();

        lowerBot();

        prospect();

        switch(cam.getGoldPosition()) {
            case RIGHT:
                longitudinalDistance(-12);
                rotatePreciseDegrees(45);
                longitudinalDistance(-24);
                longitudinalDistance(24);
                rotatePreciseDegrees(45);
                longitudinalDistance(60);
                rotatePreciseDegrees(-45);
                longitudinalDistance(-48);
                dispenseMarker();
                break;
            case LEFT:
                longitudinalDistance(-12);
                rotatePreciseDegrees(-45);
                longitudinalDistance(-24);
                rotatePreciseDegrees(90);
                longitudinalDistance(-48);
                dispenseMarker();
                break;
            case CENTER:
            case NULL:
                longitudinalDistance(-36);
                dispenseMarker();
                break;

        }
    }
}
