package org.firstinspires.ftc.teamcode.Autonomous;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous(name = "MarkerOp", group = "Autonomous")
public class MarkerAutoOp extends AutoOp {

    @Override
    public void runOpMode() {
        initialize();

        waitForStart();

        /*while(opModeIsActive()) {
            telemetry.addData("Limit Switch", limitSwitch.getState());
            telemetry.update();
        }*/

        //Descend robot from Lander, move off the hook
        lowerBot();

        //Locates the gold mineral from one of the three given locations
        prospect();

        /*cam.activateTFOD();
        while(opModeIsActive()) {
            cam.betterUpdate(telemetry);
            telemetry.update();
        }
*/
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
                longitudinalDistance(48, 0.5f);
                break;
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
                longitudinalDistance(72, 0.5f);
                break;
            case CENTER:
            case NULL:
                longitudinalDistance(-52);
                dispenseMarker();
                resetPosition();
                rotatePreciseDegrees(90);
                longitudinalDistance(42);
                rotateDegrees(-45);
                longitudinalDistance(24);
                break;
        }

        //Moves robot into opposing team's crater
        //driveTrain.rotateDegrees(45);
    }
}
