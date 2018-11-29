package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous (name="ProgressiveOp", group = "Autonomous")
public class ProgressiveOp extends AutoOp  {

    @Override
    public void runOpMode() throws InterruptedException {
        cam = new BuggleCam(telemetry, hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName()));

        waitForStart();

        findGold();

        driveTrain.longitudinalDistance(-2);
        driveTrain.rotateDegrees(90);
    }
}
