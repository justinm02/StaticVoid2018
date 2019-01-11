package org.firstinspires.ftc.teamcode.Autonomous;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.Autonomous.AutoOp;
import org.firstinspires.ftc.teamcode.Robot.BuggleCam;

@Autonomous (name="ProgressiveOp", group = "Autonomous")
public class ProgressiveOp extends AutoOp {

    @Override
    public void runOpMode() {
        /*cam = new BuggleCam(telemetry, hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName()));*/
        /*
        driveTrain = new DriveTrain(
                hardwareMap.get(DcMotorEx.class, "backLeft"),
                hardwareMap.get(DcMotorEx.class, "backRight"),
                hardwareMap.get(DcMotorEx.class, "frontLeft"),
                hardwareMap.get(DcMotorEx.class, "frontRight")
        );
        driveTrain.resetEncoders();
        driveTrain.reverseMotors();

        driveTrain.setTelemetry(this.telemetry);
        //intake = new Intake(hardwareMap.get(DcMotorEx.class, "lift"), null, null, null);

        */

        initialize();
        waitForStart();

        //prospect();

/*        while(opModeIsActive()) {
            telemetry.addData("Position", cam.getGoldPosition());
            telemetry.update();
        }*/
        /*cam.activateTFOD();
        while (opModeIsActive()){
            cam.betterUpdate(telemetry);
            telemetry.update();
        }*/

        //longitudinalDistance(-100);
        lowerBot();

        /*ElapsedTime runtime = new ElapsedTime();
        double time = runtime.milliseconds();

        while(runtime.milliseconds() < time + 10000) {}

        driveTrain.rotateDegrees(90);

        time = runtime.milliseconds();
        while(runtime.milliseconds() < time + 5000) {}*/
    }
}