package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous (name="ProgressiveOp", group = "Autonomous")
public class ProgressiveOp extends AutoOp  {

    @Override
    public void runOpMode() throws InterruptedException {
        cam = new BuggleCam(telemetry, hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName()));

        driveTrain = new DriveTrain(
                hardwareMap.get(DcMotorEx.class, "backLeft"),
                hardwareMap.get(DcMotorEx.class, "backRight"),
                hardwareMap.get(DcMotorEx.class, "frontLeft"),
                hardwareMap.get(DcMotorEx.class, "frontRight")
        );
        driveTrain.resetEncoders();
        driveTrain.progressiveOp();

        driveTrain.setTelemetry(this.telemetry);
        //intake = new Intake(hardwareMap.get(DcMotorEx.class, "lift"), null, null, null);



        //initialize();
        waitForStart();

        //prospect();

        driveTrain.longitudinalDistance(24);


        ElapsedTime runtime = new ElapsedTime();
        double time = runtime.milliseconds();

        while(runtime.milliseconds() < time + 10000) {}

        driveTrain.rotateDegrees(90);

        time = runtime.milliseconds();
        while(runtime.milliseconds() < time + 5000) {}
    }
}